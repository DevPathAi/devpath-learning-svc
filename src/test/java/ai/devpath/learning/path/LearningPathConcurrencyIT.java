package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.ai.AiPathClient;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class LearningPathConcurrencyIT {
  @Autowired LearningPathGenerationService generation;
  @Autowired JdbcTemplate jdbc;
  @MockitoBean AiPathClient aiClient;

  @Test
  void concurrentGenerateKeepsExactlyOneActivePathAndConflictsAre409() throws Exception {
    long userId = uniqueId();
    seedUser(userId);
    seedCompletedAssessment(userId);
    seedContent("c1-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.10);
    seedContent("c2-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.11);
    seedContent("c3-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.12);
    when(aiClient.generate(any())).thenReturn(aiResult());
    when(aiClient.embed(any())).thenReturn(List.of(vector(0.10)));

    ExecutorService pool = Executors.newFixedThreadPool(2);
    CountDownLatch start = new CountDownLatch(1);
    Callable<Object> call = () -> {
      start.await();
      try {
        return generation.generate(userId, "goal", e -> {});
      } catch (Throwable t) {
        return t;
      }
    };
    Future<Object> f1 = pool.submit(call);
    Future<Object> f2 = pool.submit(call);
    start.countDown();
    Object r1 = f1.get(30, TimeUnit.SECONDS);
    Object r2 = f2.get(30, TimeUnit.SECONDS);
    pool.shutdown();

    List<Object> results = List.of(r1, r2);
    long success = results.stream().filter(r -> r instanceof LearningPath).count();

    Integer active = jdbc.queryForObject(
        "select count(*) from learning_paths where user_id = ? and status = 'ACTIVE'",
        Integer.class, userId);
    assertThat(active).isEqualTo(1);          // 핵심 불변식: ACTIVE는 항상 정확히 1
    assertThat(success).isGreaterThanOrEqualTo(1);
    // 충돌이 발생했다면 반드시 ActivePathConflictException(500/기타 예외 금지)
    results.stream().filter(r -> r instanceof Throwable)
        .forEach(r -> assertThat(r).isInstanceOf(ActivePathConflictException.class));
  }

  private void seedCompletedAssessment(long userId) {
    Long assessmentId = jdbc.queryForObject("""
        insert into assessments(user_id, track, status, current_difficulty, started_at, completed_at)
        values (?, 'BACKEND_SPRING', 'COMPLETED', 0.3, now(), now()) returning id
        """, Long.class, userId);
    jdbc.update("""
        insert into assessment_results(assessment_id, diagnosed_level, concept_scores,
          strength_concepts, weakness_concepts, confidence_weight)
        values (?, 'JUNIOR', cast('{}' as jsonb), cast('[\"Java syntax\"]' as jsonb),
          cast('[\"Spring MVC\"]' as jsonb), 0.9)
        """, assessmentId);
  }

  private long seedContent(String slug, String track, String status, double value) {
    Long contentId = jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, 'body', 15, 0.3, 'UNDERSTAND', cast('[\"Spring MVC\"]' as jsonb), ?)
        returning id
        """, Long.class, slug, slug, track, status);
    jdbc.update("""
        insert into content_embeddings(content_id, chunk_index, chunk_text, embedding, status)
        values (?, 0, ?, cast(? as vector), 'ACTIVE')
        """, contentId, slug, vectorLiteral(value));
    return contentId;
  }

  private void seedUser(long userId) {
    jdbc.update("""
        insert into users(id, status, role, onboarding_status, created_at, updated_at, last_active_at)
        values (?, 'ACTIVE', 'LEARNER', 'IN_PROGRESS', now(), now(), now())
        on conflict (id) do nothing
        """, userId);
  }

  private PathGenerateResult aiResult() {
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

  private List<Double> vector(double value) {
    return java.util.Collections.nCopies(768, value);
  }

  private String vectorLiteral(double value) {
    return "[" + String.join(",", java.util.Collections.nCopies(768, String.valueOf(value))) + "]";
  }

  private long uniqueId() {
    return System.nanoTime() % 1_000_000_000L;
  }
}
