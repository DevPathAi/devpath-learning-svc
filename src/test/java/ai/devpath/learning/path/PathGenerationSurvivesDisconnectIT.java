package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.ai.AiPathClient;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * 2026-08-14 운영 이슈: 생성이 10분 넘게 걸리는 동안 구독이 끊기면 완성된 결과가 저장되지 못하고
 * 버려졌다. 구독자가 매 이벤트에서 실패해도 경로는 DB 에 남아야 한다.
 */
@SpringBootTest
@ActiveProfiles("test")
class PathGenerationSurvivesDisconnectIT {

  @Autowired PathGenerationJobService jobs;
  @Autowired LearningPathRepository paths;
  @Autowired JdbcTemplate jdbc;
  @MockitoBean AiPathClient aiClient;

  @Test
  void pathIsPersistedEvenWhenEverySubscriberNotificationFails() throws Exception {
    long userId = uniqueId();
    seedCompletedAssessment(userId);
    seedContent("disconnect-" + userId, "BACKEND_SPRING", 0.10);
    when(aiClient.generate(any())).thenReturn(aiResult());
    when(aiClient.embed(any())).thenReturn(List.of(vector(0.10)));

    PathGenerationJob job = jobs.submit(userId, "끊겨도 저장되어야 한다");
    job.addListener(new PathGenerationListener() {
      @Override public void onProgress(PathProgressEvent event) {
        throw new IllegalStateException("SSE send failed");
      }
      @Override public void onSuccess(long pathId) {
        throw new IllegalStateException("SSE send failed");
      }
      @Override public void onFailure(Throwable error) {
        throw new IllegalStateException("SSE send failed");
      }
    });

    awaitTerminal(job);

    assertThat(job.status().state()).isEqualTo(PathGenerationJob.State.SUCCEEDED);
    LearningPath saved = paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE")
        .orElseThrow();
    assertThat(job.status().pathId()).isEqualTo(saved.getId());
    Integer taskCount = jdbc.queryForObject("""
        select count(*) from path_weekly_tasks t join path_milestones m on t.milestone_id = m.id
        where m.path_id = ?
        """, Integer.class, saved.getId());
    assertThat(taskCount).isEqualTo(3);
  }

  private static void awaitTerminal(PathGenerationJob job) throws InterruptedException {
    CountDownLatch done = new CountDownLatch(1);
    job.addListener(new PathGenerationListener() {
      @Override public void onProgress(PathProgressEvent event) {}
      @Override public void onSuccess(long pathId) { done.countDown(); }
      @Override public void onFailure(Throwable error) { done.countDown(); }
    });
    assertThat(done.await(30, TimeUnit.SECONDS)).isTrue();
  }

  private void seedCompletedAssessment(long userId) {
    jdbc.update("""
        insert into users(id, status, role, onboarding_status, created_at, updated_at, last_active_at)
        values (?, 'ACTIVE', 'LEARNER', 'IN_PROGRESS', now(), now(), now())
        on conflict (id) do nothing
        """, userId);
    Long assessmentId = jdbc.queryForObject("""
        insert into assessments(user_id, track, status, current_difficulty, started_at, completed_at)
        values (?, 'BACKEND_SPRING', 'COMPLETED', 0.3, now(), ?)
        returning id
        """, Long.class, userId, Timestamp.from(Instant.parse("2026-08-17T00:00:00Z")));
    jdbc.update("""
        insert into assessment_results(assessment_id, diagnosed_level, concept_scores,
          strength_concepts, weakness_concepts, confidence_weight)
        values (?, 'JUNIOR', cast('{"spring":0.5}' as jsonb), cast('["Java syntax"]' as jsonb),
          cast('["Spring MVC"]' as jsonb), 0.9)
        """, assessmentId);
  }

  private void seedContent(String slug, String track, double value) {
    Long contentId = jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, 'body', 15, 0.3, 'UNDERSTAND', cast('["Spring MVC"]' as jsonb), 'PUBLISHED')
        returning id
        """, Long.class, slug, slug, track);
    jdbc.update("""
        insert into content_embeddings(content_id, chunk_index, chunk_text, embedding, status)
        values (?, 0, ?, cast(? as vector), 'ACTIVE')
        """, contentId, slug, vectorLiteral(value));
  }

  private static PathGenerateResult aiResult() {
    return new PathGenerateResult("Spring MVC 전에 Java 기초를 굳힙니다.", List.of(
        new PathGenerateResult.Milestone(
            1,
            "Spring MVC 입문",
            "HTTP 요청 흐름을 이해하고 controller를 만든다.",
            List.of("Java syntax", "Spring MVC"),
            6,
            "Java 문법을 바탕으로 MVC를 학습합니다.",
            "간단한 Spring controller를 설명하고 만들 수 있다.",
            List.of(
                new PathGenerateResult.Task(1, "READ", "Spring MVC 개념 읽기", true),
                new PathGenerateResult.Task(2, "PRACTICE", "Controller 만들기", true),
                new PathGenerateResult.Task(3, "QUIZ", "HTTP 흐름 퀴즈", true)))));
  }

  private static List<Double> vector(double value) {
    return Collections.nCopies(768, value);
  }

  private static String vectorLiteral(double value) {
    return "[" + String.join(",", Collections.nCopies(768, String.valueOf(value))) + "]";
  }

  private static long uniqueId() {
    return System.nanoTime() % 1_000_000_000L;
  }
}
