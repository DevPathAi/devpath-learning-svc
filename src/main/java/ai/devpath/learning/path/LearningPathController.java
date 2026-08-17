package ai.devpath.learning.path;

import ai.devpath.shared.error.ErrorCode;
import ai.devpath.shared.error.SseSupport;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/learning-paths")
public class LearningPathController {
  private final LearningPathGenerationService generation;
  private final PathGenerationJobService generationJobs;
  private final LearningPathQueryService queries;
  private final long sseTimeoutMs;

  private final PathWeeklyTaskRepository weeklyTasks;

  public LearningPathController(LearningPathGenerationService generation,
      PathGenerationJobService generationJobs, LearningPathQueryService queries,
      PathWeeklyTaskRepository weeklyTasks,
      @Value("${devpath.path.sse-timeout-ms:180000}") long sseTimeoutMs) {
    this.generation = generation;
    this.generationJobs = generationJobs;
    this.queries = queries;
    this.weeklyTasks = weeklyTasks;
    this.sseTimeoutMs = sseTimeoutMs;
  }

  /** content_id가 없어 콘텐츠 진척 완료로 자동 처리되지 않는 주간 과제의 명시적 완료 처리. */
  @PostMapping("/tasks/{taskId}/complete")
  public ResponseEntity<Void> completeTask(@AuthenticationPrincipal Jwt jwt, @PathVariable long taskId) {
    int updated = weeklyTasks.completeTaskIfOwned(uid(jwt), taskId);
    return updated == 1 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  /**
   * 생성은 요청 수명과 분리된 작업으로 돌고, 이 스트림은 그 작업을 구독만 한다.
   * 스트림이 끊겨도 생성과 저장은 계속되며 결과는 {@code GET /me/generation} 으로 되찾을 수 있다.
   */
  @PostMapping(path = "/me/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter generate(@AuthenticationPrincipal Jwt jwt,
      @RequestBody(required = false) GeneratePathRequest request) {
    long userId = uid(jwt);
    String goal = request == null ? null : request.goal();
    SseEmitter emitter = new SseEmitter(sseTimeoutMs);
    PathGenerationJob job;
    try {
      job = generationJobs.submit(userId, goal);
    } catch (PathGenerationUnavailableException e) {
      SseSupport.sendError(emitter, ErrorCode.INTERNAL_ERROR, e.getMessage());
      emitter.complete();
      return emitter;
    }
    job.addListener(new PathGenerationListener() {
      @Override
      public void onProgress(PathProgressEvent event) {
        send(emitter, event);
      }

      @Override
      public void onSuccess(long pathId) {
        emitter.complete();
      }

      @Override
      public void onFailure(Throwable error) {
        SseSupport.sendError(emitter, ErrorCode.INTERNAL_ERROR, error.getMessage());
        emitter.complete();
      }
    });
    return emitter;
  }

  /** 스트림이 끊긴 뒤에도 진행/결과를 조회한다. 생성 이력이 없으면 {@code state=NONE}. */
  @GetMapping("/me/generation")
  public ResponseEntity<PathGenerationStatusView> generationStatus(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(generationJobs.find(uid(jwt))
        .map(job -> PathGenerationStatusView.of(job.status()))
        .orElseGet(PathGenerationStatusView::none));
  }

  @PostMapping("/me/regenerate")
  public ResponseEntity<Map<String, Long>> regenerate(@AuthenticationPrincipal Jwt jwt,
      @RequestBody(required = false) GeneratePathRequest request) {
    LearningPath path = generation.generate(uid(jwt), request == null ? null : request.goal(), event -> {});
    return ResponseEntity.ok(Map.of("pathId", path.getId()));
  }

  @GetMapping("/me")
  public ResponseEntity<LearningPathView> me(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(queries.current(uid(jwt)));
  }

  @GetMapping("/me/this-week")
  public ResponseEntity<ThisWeekView> thisWeek(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(queries.thisWeek(uid(jwt)));
  }

  @GetMapping("/{id}/rationale")
  public ResponseEntity<RationaleView> rationale(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
    return ResponseEntity.ok(queries.rationale(uid(jwt), id));
  }

  private void send(SseEmitter emitter, PathProgressEvent event) {
    try {
      emitter.send(SseEmitter.event().name("progress").data(event));
    } catch (IOException e) {
      throw new IllegalStateException("SSE send failed", e);
    }
  }

  private static long uid(Jwt jwt) {
    return Long.parseLong(jwt.getSubject());
  }
}
