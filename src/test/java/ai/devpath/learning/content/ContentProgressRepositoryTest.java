package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
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

  @Test
  void countCompletedCountsOnlyCompletedRows() {
    long userId = uniqueId();
    seedUser(userId);
    long done = seedContent("repo-done", "BACKEND_SPRING", "PUBLISHED");
    long partial = seedContent("repo-partial", "BACKEND_SPRING", "PUBLISHED");

    progress.upsert(userId, done, 0.9, 60, SCROLL_THRESHOLD, MIN_DWELL_SEC);
    progress.upsert(userId, partial, 0.3, 10, SCROLL_THRESHOLD, MIN_DWELL_SEC);

    assertThat(progress.countCompleted(userId)).isEqualTo(1);
  }

  @Test
  void dailyCompletedCountsBucketsTodayByKst() {
    long userId = uniqueId();
    seedUser(userId);
    long a = seedContent("ts-a", "BACKEND_SPRING", "PUBLISHED");
    long b = seedContent("ts-b", "BACKEND_SPRING", "PUBLISHED");
    progress.upsert(userId, a, 0.9, 60, SCROLL_THRESHOLD, MIN_DWELL_SEC);
    progress.upsert(userId, b, 0.9, 60, SCROLL_THRESHOLD, MIN_DWELL_SEC);

    Instant since = Instant.now().minus(java.time.Duration.ofDays(7));
    var counts = progress.dailyCompletedCounts(userId, since);

    java.time.LocalDate todayKst = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Seoul"));
    assertThat(counts.get(todayKst)).isEqualTo(2);
  }

  @Test
  void activePathCompletionsReturnsTotalAndCompletedDates() {
    long userId = uniqueId();
    seedUser(userId);
    long content = seedContent("ts-path", "BACKEND_SPRING", "PUBLISHED");
    long path = seedPath(userId, "ACTIVE");
    seedTask(path, content, 1);
    seedTask(path, content, 2);
    progress.completeActivePathTasks(userId, content); // content_id 일치 과제 전부 완료

    var result = progress.activePathCompletions(userId);

    assertThat(result.totalTasks()).isEqualTo(2);
    assertThat(result.completedDates()).hasSize(2);
    assertThat(result.completedDates())
        .containsOnly(java.time.LocalDate.now(java.time.ZoneId.of("Asia/Seoul")));
  }

  @Test
  void activePathCompletionsGroupsTotalsAndDatesByTaskType() {
    long userId = uniqueId();
    seedUser(userId);
    long readContent = seedContent("ts-type-read", "BACKEND_SPRING", "PUBLISHED");
    long practiceContent = seedContent("ts-type-practice", "BACKEND_SPRING", "PUBLISHED");
    long path = seedPath(userId, "ACTIVE");
    seedTask(path, readContent, 1, "READ");
    seedTask(path, practiceContent, 2, "PRACTICE");
    seedTask(path, practiceContent, 3, "PRACTICE");
    // READ 과제만 완료시킨다 — 유형마다 분모·분자가 실제로 갈리는 상태를 만든다.
    // 「유형별로 나눴다」를 선언하는 것과 조건이 성립하는 것은 다르다(3-A의 반복 교훈).
    progress.completeActivePathTasks(userId, readContent);

    var result = progress.activePathCompletions(userId);

    LocalDate todayKst = LocalDate.now(ZoneId.of("Asia/Seoul"));
    // 기존 두 필드는 그대로여야 한다 — DashboardService의 전체 진행률 경로가 이것을 쓴다.
    assertThat(result.totalTasks()).isEqualTo(3);
    assertThat(result.completedDates()).containsExactly(todayKst);
    // 분모가 유형마다 다르다. PRACTICE는 2개인데 하나도 완료되지 않았다.
    assertThat(result.totalByType())
        .containsExactlyInAnyOrderEntriesOf(Map.of("READ", 1, "PRACTICE", 2));
    // 완료가 0건인 유형은 **키 자체가 없다** — 「0건」과 「없음」을 구분한다.
    assertThat(result.completedByType()).containsOnlyKeys("READ");
    assertThat(result.completedByType().get("READ")).containsExactly(todayKst);
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
    return seedTask(pathId, contentId, order, "READ");
  }

  private long seedTask(long pathId, long contentId, int order, String taskType) {
    Long milestoneId = jdbc.queryForObject("""
        insert into path_milestones(path_id, week_num, title, goal_description, target_skills,
          estimated_hours, why_this_order, expected_outcome)
        values (?, ?, ?, 'goal', cast('[\"spring\"]' as jsonb), 2, 'why', 'outcome')
        returning id
        """, Long.class, pathId, order, "week " + order);
    return jdbc.queryForObject("""
        insert into path_weekly_tasks(milestone_id, order_num, content_id, task_type, title, required)
        values (?, ?, ?, ?, ?, true)
        returning id
        """, Long.class, milestoneId, order, contentId, taskType, "task " + order);
  }

  private long uniqueId() {
    return System.nanoTime() % 1_000_000_000L;
  }
}
