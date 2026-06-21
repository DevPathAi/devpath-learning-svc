package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
class ContentControllerTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  private long userId;

  @BeforeEach
  void reset() {
    jdbc.execute("""
        TRUNCATE user_content_progress, path_weekly_tasks, path_milestones,
          learning_paths, content_embeddings, contents
        RESTART IDENTITY CASCADE
        """);
    userId = uniqueId();
    seedUser(userId);
  }

  @Test
  void getContentByNumericIdReturnsMarkdownTagsAndProgress() throws Exception {
    long contentId = seedContent("spring-tx-boundary", "BACKEND_SPRING", "PUBLISHED",
        "## Transaction Boundary\nRead this.", "[\"spring-tx\",\"backend-spring\"]");
    jdbc.update("""
        insert into user_content_progress(user_id, content_id, scroll_pct, dwell_sec)
        values (?, ?, 0.42, 73)
        """, userId, contentId);

    mvc.perform(get("/contents/{id}", contentId).with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(contentId))
        .andExpect(jsonPath("$.slug").value("spring-tx-boundary"))
        .andExpect(jsonPath("$.markdown").value("## Transaction Boundary\nRead this."))
        .andExpect(jsonPath("$.conceptTags[0]").value("spring-tx"))
        .andExpect(jsonPath("$.progress.scrollPct").value(0.42))
        .andExpect(jsonPath("$.progress.dwellSec").value(73))
        .andExpect(jsonPath("$.progress.completed").value(false));
  }

  @Test
  void getContentBySlugReturnsPublishedOnly() throws Exception {
    seedContent("published-content", "BACKEND_SPRING", "PUBLISHED", "## Published", "[\"tag\"]");
    seedContent("draft-content", "BACKEND_SPRING", "DRAFT", "## Draft", "[\"tag\"]");

    mvc.perform(get("/contents/published-content").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.slug").value("published-content"));
    mvc.perform(get("/contents/draft-content").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("CONTENT_NOT_FOUND"));
    mvc.perform(get("/contents/missing-content").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("CONTENT_NOT_FOUND"));
  }

  @Test
  void getContentRejectsOverflowingNumericIdAndRequiresAuthentication() throws Exception {
    mvc.perform(get("/contents/999999999999999999999999999999999"))
        .andExpect(status().isUnauthorized());
    mvc.perform(get("/contents/999999999999999999999999999999999")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_CONTENT_ID"));
  }

  @Test
  void postProgressIsMonotonicAndRejectsInvalidValues() throws Exception {
    seedContent("progress-monotonic", "BACKEND_SPRING", "PUBLISHED", "## Body", "[\"tag\"]");

    mvc.perform(post("/contents/progress-monotonic/progress")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"scrollPct\":0.5,\"dwellSec\":10}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.scrollPct").value(0.5))
        .andExpect(jsonPath("$.dwellSec").value(10))
        .andExpect(jsonPath("$.completed").value(false));
    mvc.perform(post("/contents/progress-monotonic/progress")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"scrollPct\":0.1,\"dwellSec\":2}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.scrollPct").value(0.5))
        .andExpect(jsonPath("$.dwellSec").value(10));
    mvc.perform(post("/contents/progress-monotonic/progress")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"scrollPct\":1.5,\"dwellSec\":2}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_PROGRESS"));
    mvc.perform(post("/contents/progress-monotonic/progress")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"scrollPct\":0.5,\"dwellSec\":-1}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_PROGRESS"));
  }

  @Test
  void completingProgressCompletesActivePathTaskAndUpdatesPathAndDashboardViews() throws Exception {
    long contentId = seedContent("progress-task", "BACKEND_SPRING", "PUBLISHED", "## Body", "[\"tag\"]");
    long pathId = seedPath(userId, "ACTIVE");
    seedTask(pathId, contentId, 1, "Read content");
    seedTask(pathId, seedContent("second-task", "BACKEND_SPRING", "PUBLISHED", "## Other", "[\"tag\"]"),
        2, "Read next");

    mvc.perform(get("/dashboard/me").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.progressPercent").value(0));
    mvc.perform(post("/contents/progress-task/progress")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"scrollPct\":0.8,\"dwellSec\":45}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true))
        .andExpect(jsonPath("$.completedAt").exists())
        .andExpect(jsonPath("$.taskCompletedCount").value(1));

    mvc.perform(get("/learning-paths/me").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.milestones[0].tasks[0].completed").value(true))
        .andExpect(jsonPath("$.milestones[0].tasks[1].completed").value(false));
    mvc.perform(get("/dashboard/me").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.progressPercent").value(50));
  }

  @Test
  void progressListSupportsLatestOrderFiltersAndLimitClamp() throws Exception {
    long backend = seedContent("backend-progress", "BACKEND_SPRING", "PUBLISHED", "## Backend", "[\"tag\"]");
    long frontend = seedContent("frontend-progress", "FRONTEND_REACT", "PUBLISHED", "## Frontend", "[\"tag\"]");
    seedProgress(userId, backend, 0.9, 50, true, Instant.parse("2026-06-21T00:00:00Z"));
    seedProgress(userId, frontend, 0.2, 10, false, Instant.parse("2026-06-22T00:00:00Z"));

    mvc.perform(get("/contents/me/progress").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(2))
        .andExpect(jsonPath("$.items[0].slug").value("frontend-progress"))
        .andExpect(jsonPath("$.items[1].slug").value("backend-progress"));
    mvc.perform(get("/contents/me/progress?completed=true")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(1))
        .andExpect(jsonPath("$.items[0].completed").value(true));
    mvc.perform(get("/contents/me/progress?completed=false&track=FRONTEND_REACT&limit=500")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(1))
        .andExpect(jsonPath("$.items[0].track").value("FRONTEND_REACT"));

    for (int i = 0; i < 105; i++) {
      long contentId = seedContent("bulk-progress-" + i, "BACKEND_SPRING", "PUBLISHED", "## Bulk", "[\"tag\"]");
      seedProgress(userId, contentId, 0.1, i, false, Instant.parse("2026-06-23T00:00:00Z").plusSeconds(i));
    }
    mvc.perform(get("/contents/me/progress?limit=500")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(100));
  }

  private long seedContent(String slug, String track, String status, String markdown, String tags) {
    return jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, ?, 10, 0.4, 'APPLY', cast(? as jsonb), ?)
        returning id
        """, Long.class, slug, slug, track, markdown, tags, status);
  }

  private void seedProgress(long userId, long contentId, double scrollPct, int dwellSec,
      boolean completed, Instant updatedAt) {
    jdbc.update("""
        insert into user_content_progress(user_id, content_id, scroll_pct, dwell_sec,
          completed_at, created_at, updated_at)
        values (?, ?, ?, ?, ?, ?, ?)
        """, userId, contentId, scrollPct, dwellSec,
        completed ? java.sql.Timestamp.from(updatedAt) : null,
        java.sql.Timestamp.from(updatedAt),
        java.sql.Timestamp.from(updatedAt));
  }

  private void seedUser(long userId) {
    jdbc.update("""
        insert into users(id, status, role, onboarding_status, created_at, updated_at, last_active_at)
        values (?, 'ACTIVE', 'LEARNER', 'IN_PROGRESS', now(), now(), now())
        on conflict (id) do nothing
        """, userId);
  }

  private long seedPath(long userId, String status) {
    return jdbc.queryForObject("""
        insert into learning_paths(user_id, generated_at, track, total_weeks,
          gen_prompt_version, source_embedding_version, status, ai_rationale)
        values (?, now(), 'BACKEND_SPRING', 12, 'test', 'test', ?, 'rationale')
        returning id
        """, Long.class, userId, status);
  }

  private long seedTask(long pathId, long contentId, int order, String title) {
    Long milestoneId = milestoneId(pathId);
    return jdbc.queryForObject("""
        insert into path_weekly_tasks(milestone_id, order_num, content_id, task_type, title, required)
        values (?, ?, ?, 'READ', ?, true)
        returning id
        """, Long.class, milestoneId, order, contentId, title);
  }

  private Long milestoneId(long pathId) {
    var existing = jdbc.queryForList("""
        select id from path_milestones
        where path_id = ? and week_num = 1
        order by id
        limit 1
        """, Long.class, pathId);
    if (!existing.isEmpty()) {
      return existing.get(0);
    }
    return jdbc.queryForObject("""
        insert into path_milestones(path_id, week_num, title, goal_description, target_skills,
          estimated_hours, why_this_order, expected_outcome)
        values (?, 1, 'week 1', 'goal', cast('[\"spring\"]' as jsonb), 2, 'why', 'outcome')
        returning id
        """, Long.class, pathId);
  }

  private long uniqueId() {
    return System.nanoTime() % 1_000_000_000L;
  }
}
