package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurrentMissionIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;
  @MockitoSpyBean NamedParameterJdbcTemplate namedJdbc;

  @BeforeEach
  void reset() {
    jdbc.execute("""
        TRUNCATE user_content_progress, path_weekly_tasks, path_milestones,
          learning_paths, content_embeddings, contents
        RESTART IDENTITY CASCADE
        """);
    clearInvocations(namedJdbc);
  }

  @Test
  void weekOneUsesProducerOrderAndMapsStableTaskIdentityExactly() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long week1 = seedMilestone(pathId, 1, "첫 주");
    long week2 = seedMilestone(pathId, 2, "둘째 주");
    long contentId = seedContent("optional-first", "PUBLISHED");
    long optionalTask = seedTask(week1, 1, contentId, "READ", "선택 읽기", false, null);
    Instant completedAt = Instant.parse("2026-08-14T10:15:30Z");
    long completedRequired = seedTask(
        week1, 2, null, "PRACTICE", "완료한 필수 실습", true, completedAt);
    seedTask(week2, 1, null, "QUIZ", "다음 주 퀴즈", true, null);

    var response = mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pathId").value(pathId))
        .andExpect(jsonPath("$.weekNum").value(1))
        .andExpect(jsonPath("$.outcome").value("AVAILABLE"))
        .andExpect(jsonPath("$.pathCompleted").value(false))
        .andExpect(jsonPath("$.tasks.length()").value(2))
        .andExpect(jsonPath("$.tasks[0].taskId").value(optionalTask))
        .andExpect(jsonPath("$.tasks[0].orderNum").value(1))
        .andExpect(jsonPath("$.tasks[0].taskType").value("READ"))
        .andExpect(jsonPath("$.tasks[0].title").value("선택 읽기"))
        .andExpect(jsonPath("$.tasks[0].required").value(false))
        .andExpect(jsonPath("$.tasks[0].contentId").value(contentId))
        .andExpect(jsonPath("$.tasks[0].contentSlug").value("optional-first"))
        .andExpect(jsonPath("$.tasks[0].completed").value(false))
        .andExpect(jsonPath("$.tasks[0].completedAt").doesNotExist())
        .andExpect(jsonPath("$.tasks[1].taskId").value(completedRequired))
        .andExpect(jsonPath("$.tasks[1].required").value(true))
        .andExpect(jsonPath("$.tasks[1].contentId").doesNotExist())
        .andExpect(jsonPath("$.tasks[1].contentSlug").doesNotExist())
        .andExpect(jsonPath("$.tasks[1].completed").value(true))
        .andExpect(jsonPath("$.tasks[1].completedAt").value(completedAt.toString()))
        .andExpect(jsonPath("$.nextTask.taskId").value(optionalTask))
        .andExpect(jsonPath("$.nextTask.required").value(false))
        .andReturn().getResponse();

    assertThat(response.getContentAsByteArray().length).isLessThanOrEqualTo(20 * 1024);
    assertThat(response.getContentAsString(StandardCharsets.UTF_8)).doesNotContain("contentMd");
  }

  @Test
  void advancesToFirstIncompleteTaskAcrossWeeks() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long week1 = seedMilestone(pathId, 1, "첫 주");
    long week2 = seedMilestone(pathId, 2, "둘째 주");
    seedTask(week1, 1, null, "PRACTICE", "완료 1", true,
        Instant.parse("2026-08-13T00:00:00Z"));
    seedTask(week1, 2, null, "QUIZ", "완료 2", false,
        Instant.parse("2026-08-14T00:00:00Z"));
    long next = seedTask(week2, 1, null, "READ", "다음 과제", false, null);
    seedTask(week2, 2, null, "PRACTICE", "후속 과제", true, null);

    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("AVAILABLE"))
        .andExpect(jsonPath("$.weekNum").value(2))
        .andExpect(jsonPath("$.tasks.length()").value(2))
        .andExpect(jsonPath("$.nextTask.taskId").value(next));
  }

  @Test
  void futureEmptyOrDraftMilestonesDoNotHideAUsableCurrentMission() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long current = seedMilestone(pathId, 1, "현재 주");
    long next = seedTask(current, 1, null, "PRACTICE", "지금 할 일", true, null);
    seedMilestone(pathId, 11, "아직 비어 있는 미래 주");
    long future = seedMilestone(pathId, 12, "아직 공개 전인 미래 주");
    long draft = seedContent("future-draft", "DRAFT");
    seedTask(future, 1, draft, "READ", "나중에 공개할 읽기", true, null);

    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("AVAILABLE"))
        .andExpect(jsonPath("$.weekNum").value(1))
        .andExpect(jsonPath("$.nextTask.taskId").value(next));
  }

  @Test
  void reachedEmptyMilestoneDoesNotGetSkippedForALaterTask() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long completed = seedMilestone(pathId, 1, "완료한 주");
    seedTask(completed, 1, null, "READ", "완료한 일", true,
        Instant.parse("2026-08-14T00:00:00Z"));
    seedMilestone(pathId, 2, "도달했지만 비어 있는 주");
    long later = seedMilestone(pathId, 3, "건너뛰면 안 되는 다음 주");
    seedTask(later, 1, null, "PRACTICE", "나중 과제", true, null);

    assertMalformed(userId, pathId);
  }

  @Test
  void emptyMilestoneAmongCompletedWeeksPreventsFalsePathCompletion() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long first = seedMilestone(pathId, 1, "첫 주");
    seedTask(first, 1, null, "READ", "첫 완료", true,
        Instant.parse("2026-08-13T00:00:00Z"));
    seedMilestone(pathId, 2, "비어 있는 주");
    long finalWeek = seedMilestone(pathId, 3, "마지막 주");
    seedTask(finalWeek, 1, null, "QUIZ", "마지막 완료", true,
        Instant.parse("2026-08-14T00:00:00Z"));

    assertMalformed(userId, pathId);
  }

  @Test
  @SuppressWarnings("unchecked")
  void endpointUsesAtMostThreeDatabaseStatementsAgainstPostgres() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long current = seedMilestone(pathId, 1, "현재 주");
    seedTask(current, 1, null, "PRACTICE", "지금 할 일", true, null);
    clearInvocations(namedJdbc);

    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("AVAILABLE"));

    verify(namedJdbc, times(1)).query(anyString(), anyMap(), any(RowMapper.class));
  }

  @Test
  void allCompleteReturnsFinalMilestoneAndPathCompletedOutcome() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long week1 = seedMilestone(pathId, 1, "첫 주");
    long week3 = seedMilestone(pathId, 3, "마지막 주");
    seedTask(week1, 1, null, "READ", "처음", true, Instant.parse("2026-08-13T00:00:00Z"));
    long finalTask = seedTask(
        week3, 1, null, "QUIZ", "마지막", true, Instant.parse("2026-08-14T00:00:00Z"));

    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pathId").value(pathId))
        .andExpect(jsonPath("$.outcome").value("PATH_COMPLETED"))
        .andExpect(jsonPath("$.pathCompleted").value(true))
        .andExpect(jsonPath("$.weekNum").value(3))
        .andExpect(jsonPath("$.tasks.length()").value(1))
        .andExpect(jsonPath("$.tasks[0].taskId").value(finalTask))
        .andExpect(jsonPath("$.nextTask").doesNotExist());
  }

  @Test
  void noActivePathIsAStableEmptyOutcome() throws Exception {
    long userId = uniqueId();
    seedPath(userId, "ARCHIVED");

    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("NO_ACTIVE_PATH"))
        .andExpect(jsonPath("$.pathId").doesNotExist())
        .andExpect(jsonPath("$.weekNum").doesNotExist())
        .andExpect(jsonPath("$.tasks").isEmpty())
        .andExpect(jsonPath("$.nextTask").doesNotExist())
        .andExpect(jsonPath("$.pathCompleted").value(false));
  }

  @Test
  void malformedCoversMissingMilestonesEmptyMilestonesAndUnavailableContent() throws Exception {
    long noMilestones = uniqueId();
    long noMilestonesPath = seedPath(noMilestones, "ACTIVE");
    assertMalformed(noMilestones, noMilestonesPath);

    long emptyMilestone = uniqueId();
    long emptyMilestonePath = seedPath(emptyMilestone, "ACTIVE");
    seedMilestone(emptyMilestonePath, 1, "비어 있음");
    assertMalformed(emptyMilestone, emptyMilestonePath);

    long unavailableContent = uniqueId();
    long unavailablePath = seedPath(unavailableContent, "ACTIVE");
    long milestone = seedMilestone(unavailablePath, 1, "초안 콘텐츠");
    long draft = seedContent("draft-current-mission", "DRAFT");
    seedTask(milestone, 1, draft, "READ", "공개되지 않음", true, null);
    assertMalformed(unavailableContent, unavailablePath);
  }

  @Test
  void oversizedProjectionStaysWithinResponseBudgetByFailingClosed() throws Exception {
    long userId = uniqueId();
    long pathId = seedPath(userId, "ACTIVE");
    long milestone = seedMilestone(pathId, 1, "과대 경로");
    String largeTitle = "가".repeat(300);
    for (int order = 1; order <= 25; order++) {
      seedTask(milestone, order, null, "PRACTICE", largeTitle, true, null);
    }

    var response = mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("MALFORMED_PATH"))
        .andExpect(jsonPath("$.tasks").isEmpty())
        .andReturn().getResponse();

    assertThat(response.getContentAsByteArray().length).isLessThanOrEqualTo(20 * 1024);
  }

  private void assertMalformed(long userId, long pathId) throws Exception {
    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pathId").value(pathId))
        .andExpect(jsonPath("$.outcome").value("MALFORMED_PATH"))
        .andExpect(jsonPath("$.weekNum").doesNotExist())
        .andExpect(jsonPath("$.tasks").isEmpty())
        .andExpect(jsonPath("$.nextTask").doesNotExist())
        .andExpect(jsonPath("$.pathCompleted").value(false));
  }

  private long seedPath(long userId, String status) {
    return jdbc.queryForObject("""
        INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status)
        VALUES (?, now(), 'BACKEND_SPRING', 12, ?) RETURNING id
        """, Long.class, userId, status);
  }

  private long seedMilestone(long pathId, int weekNum, String title) {
    return jdbc.queryForObject("""
        INSERT INTO path_milestones(path_id, week_num, title)
        VALUES (?, ?, ?) RETURNING id
        """, Long.class, pathId, weekNum, title);
  }

  private long seedTask(long milestoneId, int orderNum, Long contentId, String taskType,
      String title, boolean required, Instant completedAt) {
    return jdbc.queryForObject("""
        INSERT INTO path_weekly_tasks(
          milestone_id, order_num, content_id, task_type, title, required, completed_at)
        VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id
        """, Long.class, milestoneId, orderNum, contentId, taskType, title, required,
        completedAt == null ? null : Timestamp.from(completedAt));
  }

  private long seedContent(String slug, String status) {
    return jdbc.queryForObject("""
        INSERT INTO contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        VALUES (?, ?, 'BACKEND_SPRING', '## body', 10, 0.4, 'APPLY', '[]'::jsonb, ?)
        RETURNING id
        """, Long.class, slug, slug, status);
  }

  private long uniqueId() {
    return Math.abs(System.nanoTime());
  }
}
