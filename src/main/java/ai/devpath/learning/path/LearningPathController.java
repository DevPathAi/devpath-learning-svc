package ai.devpath.learning.path;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
  private final LearningPathQueryService queries;

  public LearningPathController(LearningPathGenerationService generation, LearningPathQueryService queries) {
    this.generation = generation;
    this.queries = queries;
  }

  @PostMapping(path = "/me/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter generate(@AuthenticationPrincipal Jwt jwt,
      @RequestBody(required = false) GeneratePathRequest request) {
    long userId = uid(jwt);
    String goal = request == null ? null : request.goal();
    SseEmitter emitter = new SseEmitter(30_000L);
    CompletableFuture.runAsync(() -> {
      try {
        generation.generate(userId, goal, event -> send(emitter, event));
        emitter.complete();
      } catch (Exception e) {
        try {
          send(emitter, PathProgressEvent.error(e.getMessage()));
          emitter.complete();
        } catch (Exception ignored) {
          emitter.completeWithError(e);
        }
      }
    });
    return emitter;
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
