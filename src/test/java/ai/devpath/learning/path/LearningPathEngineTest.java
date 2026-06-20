package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.ai.AiPathClient;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LearningPathEngineTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;
  @Autowired LatestDiagnosisRepository latestDiagnosis;
  @Autowired ContentEmbeddingMatcher matcher;
  @Autowired LearningPathRepository paths;
  @Autowired OutboxRepository outbox;
  @MockitoBean AiPathClient aiClient;

  @Test
  void latestDiagnosisUsesNewestCompletedAssessmentForUser() {
    long userId = uniqueId();
    seedUser(userId);
    seedAssessment(userId, "COMPLETED", Instant.parse("2026-06-18T00:00:00Z"),
        "JUNIOR", "[\"old\"]", "[\"oldWeak\"]");
    long latest = seedAssessment(userId, "COMPLETED", Instant.parse("2026-06-19T00:00:00Z"),
        "MID", "[\"Spring MVC\"]", "[\"JPA\"]");
    seedAssessment(userId + 1, "COMPLETED", Instant.parse("2026-06-20T00:00:00Z"),
        "SENIOR", "[\"other\"]", "[\"otherWeak\"]");
    seedAssessment(userId, "IN_PROGRESS", null, "SENIOR", "[\"ignored\"]", "[\"ignoredWeak\"]");

    LatestDiagnosis diagnosis = latestDiagnosis.findLatestCompleted(userId).orElseThrow();

    assertThat(diagnosis.assessmentId()).isEqualTo(latest);
    assertThat(diagnosis.track()).isEqualTo("BACKEND_SPRING");
    assertThat(diagnosis.diagnosedLevel()).isEqualTo("MID");
    assertThat(diagnosis.strengthConcepts()).containsExactly("Spring MVC");
    assertThat(diagnosis.weaknessConcepts()).containsExactly("JPA");
  }

  @Test
  void vectorMatcherReturnsNearestPublishedContentForTrack() {
    long backendContent = seedContent("backend-" + uniqueId(), "BACKEND_SPRING", "PUBLISHED", 0.10);
    seedContent("frontend-" + uniqueId(), "FRONTEND_REACT", "PUBLISHED", 0.10);
    seedContent("draft-" + uniqueId(), "BACKEND_SPRING", "DRAFT", 0.10);

    List<MatchedContent> result = matcher.match("BACKEND_SPRING", vector(0.10), 5);

    assertThat(result).isNotEmpty();
    assertThat(result.get(0).contentId()).isEqualTo(backendContent);
  }

  @Test
  void generateSsePersistsPathMatchesContentAndWritesOutbox() throws Exception {
    long userId = uniqueId();
    seedUser(userId);
    seedAssessment(userId, "COMPLETED", Instant.parse("2026-06-19T00:00:00Z"),
        "JUNIOR", "[\"Java syntax\"]", "[\"Spring MVC\"]");
    seedContent("spring-read-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.10);
    seedContent("spring-practice-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.11);
    seedContent("spring-quiz-" + userId, "BACKEND_SPRING", "PUBLISHED", 0.12);

    when(aiClient.generate(any())).thenReturn(aiResult());
    when(aiClient.embed(any())).thenReturn(List.of(vector(0.10)));

    var result = mvc.perform(post("/learning-paths/me/generate").with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"goal\":\"첫 백엔드 포트폴리오\"}"))
        .andExpect(request().asyncStarted())
        .andReturn();

    String sse = mvc.perform(asyncDispatch(result))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    assertThat(sse).contains("\"stage\":\"collecting\"");
    assertThat(sse).contains("\"stage\":\"generating\"");
    assertThat(sse).contains("\"stage\":\"matching\"");
    assertThat(sse).contains("\"stage\":\"done\"");

    LearningPath saved = paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE").orElseThrow();
    assertThat(saved.getAiRationale()).isEqualTo("Spring MVC 전에 Java 기초를 굳힙니다.");
    assertThat(saved.getMilestones()).hasSize(1);
    assertThat(saved.getMilestones().get(0).getExpectedOutcome()).isEqualTo("간단한 Spring controller를 설명하고 만들 수 있다.");
    Integer taskCount = jdbc.queryForObject("""
        select count(*) from path_weekly_tasks t
        join path_milestones m on t.milestone_id = m.id
        where m.path_id = ? and t.content_id is not null
        """, Integer.class, saved.getId());
    assertThat(taskCount).isEqualTo(3);
    assertThat(outbox.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()).anySatisfy(e -> {
      assertThat(e.getEventType()).isEqualTo("learning.path.generated");
      assertThat(e.getAggregateType()).isEqualTo("learning_path");
      assertThat(e.getPayload()).contains("\"userId\": " + userId);
      assertThat(e.getPayload()).contains("\"learningPathId\": " + saved.getId());
      assertThat(e.getPayload()).contains("\"targetTrack\": \"BACKEND_SPRING\"");
    });

    mvc.perform(get("/learning-paths/me").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pathId").value(saved.getId()))
        .andExpect(jsonPath("$.diagnosis.diagnosedLevel").value("JUNIOR"))
        .andExpect(jsonPath("$.milestones[0].tasks.length()").value(3));
    mvc.perform(get("/learning-paths/me/this-week").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.weekNum").value(1))
        .andExpect(jsonPath("$.tasks.length()").value(3));
  }

  @Test
  void generateWithoutCompletedAssessmentReturns409BeforeAiCall() throws Exception {
    long userId = uniqueId();
    seedUser(userId);

    var result = mvc.perform(post("/learning-paths/me/generate").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(request().asyncStarted())
        .andReturn();

    String sse = mvc.perform(asyncDispatch(result))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    assertThat(sse).contains("\"stage\":\"collecting\"");
    assertThat(sse).contains("\"stage\":\"error\"");
    assertThat(sse).contains("NO_COMPLETED_ASSESSMENT");
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

  private long seedAssessment(long userId, String status, Instant completedAt, String level,
      String strengths, String weaknesses) {
    seedUser(userId);
    Long assessmentId = jdbc.queryForObject("""
        insert into assessments(user_id, track, status, current_difficulty, started_at, completed_at)
        values (?, 'BACKEND_SPRING', ?, 0.3, now(), ?)
        returning id
        """, Long.class, userId, status, completedAt == null ? null : Timestamp.from(completedAt));
    jdbc.update("""
        insert into assessment_results(assessment_id, diagnosed_level, concept_scores,
          strength_concepts, weakness_concepts, confidence_weight)
        values (?, ?, cast(? as jsonb), cast(? as jsonb), cast(? as jsonb), 0.9)
        """, assessmentId, level, "{\"spring\":0.5}", strengths, weaknesses);
    return assessmentId;
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
