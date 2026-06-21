package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ContentProgressRepositoryTest {
  private static final double SCROLL_THRESHOLD = 0.8;
  private static final int MIN_DWELL_SEC = 45;

  @Autowired ContentProgressRepository progress;
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
  void firstProgressRequestInsertsRow() {
    long userId = uniqueId();
    seedUser(userId);
    long contentId = seedContent("repo-insert", "BACKEND_SPRING", "PUBLISHED");

    var row = progress.upsert(userId, contentId, 0.35, 12, SCROLL_THRESHOLD, MIN_DWELL_SEC);

    assertThat(row.contentId()).isEqualTo(contentId);
    assertThat(row.scrollPct()).isEqualTo(0.35);
    assertThat(row.dwellSec()).isEqualTo(12);
    assertThat(row.completed()).isFalse();
    assertThat(progress.find(userId, contentId)).contains(row);
  }

  @Test
  void lowerProgressReplayDoesNotDecreaseStoredValues() {
    long userId = uniqueId();
    seedUser(userId);
    long contentId = seedContent("repo-monotonic", "BACKEND_SPRING", "PUBLISHED");

    progress.upsert(userId, contentId, 0.6, 30, SCROLL_THRESHOLD, MIN_DWELL_SEC);
    var row = progress.upsert(userId, contentId, 0.2, 5, SCROLL_THRESHOLD, MIN_DWELL_SEC);

    assertThat(row.scrollPct()).isEqualTo(0.6);
    assertThat(row.dwellSec()).isEqualTo(30);
    assertThat(row.completed()).isFalse();
  }

  @Test
  void completionRequiresScrollAndDwellThresholdsAndKeepsExistingCompletedAt() {
    long userId = uniqueId();
    seedUser(userId);
    long contentId = seedContent("repo-complete", "BACKEND_SPRING", "PUBLISHED");

    var scrollOnly = progress.upsert(userId, contentId, 0.9, 10, SCROLL_THRESHOLD, MIN_DWELL_SEC);
    var completed = progress.upsert(userId, contentId, 0.2, 45, SCROLL_THRESHOLD, MIN_DWELL_SEC);
    var replay = progress.upsert(userId, contentId, 0.95, 60, SCROLL_THRESHOLD, MIN_DWELL_SEC);

    assertThat(scrollOnly.completedAt()).isNull();
    assertThat(completed.completedAt()).isNotNull();
    assertThat(replay.completedAt()).isEqualTo(completed.completedAt());
  }

  @Test
  void taskCompletionUpdatesOnlyActivePathTasksForUserAndContent() {
    long userId = uniqueId();
    long otherUser = userId + 1;
    seedUser(userId);
    seedUser(otherUser);
    long contentId = seedContent("repo-task", "BACKEND_SPRING", "PUBLISHED");
    long outsideContent = seedContent("repo-outside", "BACKEND_SPRING", "PUBLISHED");
    long activePath = seedPath(userId, "ACTIVE");
    long archivedPath = seedPath(userId, "ARCHIVED");
    long otherPath = seedPath(otherUser, "ACTIVE");
    long firstTask = seedTask(activePath, contentId, 1);
    long secondTask = seedTask(activePath, contentId, 2);
    long outsideTask = seedTask(activePath, outsideContent, 3);
    long archivedTask = seedTask(archivedPath, contentId, 1);
    long otherTask = seedTask(otherPath, contentId, 1);

    int updated = progress.completeActivePathTasks(userId, contentId);
    int replay = progress.completeActivePathTasks(userId, contentId);

    assertThat(updated).isEqualTo(2);
    assertThat(replay).isZero();
    assertThat(completedAt(firstTask)).isNotNull();
    assertThat(completedAt(secondTask)).isNotNull();
    assertThat(completedAt(outsideTask)).isNull();
    assertThat(completedAt(archivedTask)).isNull();
    assertThat(completedAt(otherTask)).isNull();
  }

  private Instant completedAt(long taskId) {
    return jdbc.queryForObject(
        "select completed_at from path_weekly_tasks where id = ?",
        (rs, rowNum) -> rs.getTimestamp("completed_at") == null
            ? null
            : rs.getTimestamp("completed_at").toInstant(),
        taskId);
  }

  private long seedContent(String slug, String track, String status) {
    return jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, '## Body', 10, 0.4, 'APPLY', cast('[\"spring-tx\"]' as jsonb), ?)
        returning id
        """, Long.class, slug, slug, track, status);
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

  private long seedTask(long pathId, long contentId, int order) {
    Long milestoneId = jdbc.queryForObject("""
        insert into path_milestones(path_id, week_num, title, goal_description, target_skills,
          estimated_hours, why_this_order, expected_outcome)
        values (?, ?, ?, 'goal', cast('[\"spring\"]' as jsonb), 2, 'why', 'outcome')
        returning id
        """, Long.class, pathId, order, "week " + order);
    return jdbc.queryForObject("""
        insert into path_weekly_tasks(milestone_id, order_num, content_id, task_type, title, required)
        values (?, ?, ?, 'READ', ?, true)
        returning id
        """, Long.class, milestoneId, order, contentId, "task " + order);
  }

  private long uniqueId() {
    return System.nanoTime() % 1_000_000_000L;
  }
}
