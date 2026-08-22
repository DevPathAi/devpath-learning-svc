package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MissionCompletionIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  @BeforeEach
  void reset() {
    jdbc.execute("""
        TRUNCATE user_content_progress, path_weekly_tasks, path_milestones,
          learning_paths, content_embeddings, contents
        RESTART IDENTITY CASCADE
        """);
  }

  @Test
  void contentThresholdCrossingAdvancesMissionAndReplayIsAWriteThroughNoOp() throws Exception {
    long userId = uniqueId();
    long path = seedPath(userId, "ACTIVE");
    long week = seedMilestone(path, 1);
    long content = seedContent("threshold-content", "PUBLISHED");
    long first = seedTask(week, 1, content, "READ", "읽기", true);
    long sameContent = seedTask(week, 2, content, "PRACTICE", "같은 콘텐츠 실습", false);
    long next = seedTask(week, 3, null, "QUIZ", "다음 퀴즈", true);

    assertNext(userId, first);
    postProgress(userId, "threshold-content", 0.79, 45)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(false))
        .andExpect(jsonPath("$.taskCompletedCount").value(0));
    assertThat(completedAt(first)).isNull();
    assertNext(userId, first);

    postProgress(userId, "threshold-content", 0.8, 45)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true))
        .andExpect(jsonPath("$.taskCompletedCount").value(2));
    Instant firstCompletedAt = completedAt(first);
    Instant sameContentCompletedAt = completedAt(sameContent);
    String firstXmin = xmin(first);
    assertThat(firstCompletedAt).isNotNull();
    assertThat(sameContentCompletedAt).isNotNull();
    assertNext(userId, next);

    postProgress(userId, "threshold-content", 0.95, 80)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true))
        .andExpect(jsonPath("$.taskCompletedCount").value(0));
    assertThat(completedAt(first)).isEqualTo(firstCompletedAt);
    assertThat(completedAt(sameContent)).isEqualTo(sameContentCompletedAt);
    assertThat(xmin(first)).isEqualTo(firstXmin);

    long stranger = uniqueId();
    postProgress(stranger, "threshold-content", 0.9, 60).andExpect(status().isOk());
    assertThat(completedAt(first)).isEqualTo(firstCompletedAt);
    assertNext(userId, next);
  }

  @Test
  void contentlessCompletionIsOwnerBoundIdempotentAndCanFinishPath() throws Exception {
    long owner = uniqueId();
    long path = seedPath(owner, "ACTIVE");
    long week = seedMilestone(path, 1);
    long first = seedTask(week, 1, null, "PRACTICE", "직접 완료", true);
    long second = seedTask(week, 2, null, "QUIZ", "마지막 직접 완료", false);
    long linkedContent = seedContent("linked-cannot-direct-complete", "PUBLISHED");
    long linked = seedTask(week, 3, linkedContent, "READ", "콘텐츠로 완료", true);

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", first)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNoContent());
    Instant completedAt = completedAt(first);
    String xmin = xmin(first);
    assertThat(completedAt).isNotNull();
    assertNext(owner, second);

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", first)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNoContent());
    assertThat(completedAt(first)).isEqualTo(completedAt);
    assertThat(xmin(first)).isEqualTo(xmin);

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", first)
            .with(jwt().jwt(j -> j.subject(String.valueOf(uniqueId())))))
        .andExpect(status().isNotFound());
    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", linked)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNotFound());
    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", Long.MAX_VALUE)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNotFound());

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", second)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNoContent());
    assertNext(owner, linked);

    postProgress(owner, "linked-cannot-direct-complete", 0.8, 45)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.taskCompletedCount").value(1));
    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("PATH_COMPLETED"))
        .andExpect(jsonPath("$.pathCompleted").value(true))
        .andExpect(jsonPath("$.nextTask").doesNotExist());
  }

  @Test
  void contentlessCompletionHidesArchivedTasksLikeMissingTasks() throws Exception {
    long owner = uniqueId();
    long path = seedPath(owner, "ARCHIVED");
    long task = seedTask(seedMilestone(path, 1), 1, null, "PRACTICE", "보관됨", true);

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", task)
            .with(jwt().jwt(j -> j.subject(String.valueOf(owner)))))
        .andExpect(status().isNotFound());
    assertThat(completedAt(task)).isNull();
  }

  private org.springframework.test.web.servlet.ResultActions postProgress(
      long userId, String slug, double scrollPct, int dwellSec) throws Exception {
    return mvc.perform(post("/contents/{slug}/progress", slug)
        .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"scrollPct\":" + scrollPct + ",\"dwellSec\":" + dwellSec + "}"));
  }

  private void assertNext(long userId, long taskId) throws Exception {
    mvc.perform(get("/learning-paths/me/this-week")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.outcome").value("AVAILABLE"))
        .andExpect(jsonPath("$.nextTask.taskId").value(taskId));
  }

  private Instant completedAt(long taskId) {
    return jdbc.queryForObject("SELECT completed_at FROM path_weekly_tasks WHERE id = ?",
        (rs, row) -> rs.getTimestamp(1) == null ? null : rs.getTimestamp(1).toInstant(), taskId);
  }

  private String xmin(long taskId) {
    return jdbc.queryForObject(
        "SELECT xmin::text FROM path_weekly_tasks WHERE id = ?", String.class, taskId);
  }

  private long seedPath(long userId, String status) {
    return jdbc.queryForObject("""
        INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status)
        VALUES (?, now(), 'BACKEND_SPRING', 12, ?) RETURNING id
        """, Long.class, userId, status);
  }

  private long seedMilestone(long pathId, int weekNum) {
    return jdbc.queryForObject("""
        INSERT INTO path_milestones(path_id, week_num, title)
        VALUES (?, ?, ?) RETURNING id
        """, Long.class, pathId, weekNum, "week " + weekNum);
  }

  private long seedTask(long milestoneId, int orderNum, Long contentId, String taskType,
      String title, boolean required) {
    return jdbc.queryForObject("""
        INSERT INTO path_weekly_tasks(milestone_id, order_num, content_id, task_type, title, required)
        VALUES (?, ?, ?, ?, ?, ?) RETURNING id
        """, Long.class, milestoneId, orderNum, contentId, taskType, title, required);
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
