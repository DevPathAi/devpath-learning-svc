package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PathWeeklyTaskRepositoryTest {

  @Autowired PathWeeklyTaskRepository tasks;
  @Autowired JdbcTemplate jdbc;

  private long newTaskWithoutContent(long userId) {
    Long pathId = jdbc.queryForObject("""
        INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status)
        VALUES (?, now(), 'BACKEND_SPRING', 12, 'ACTIVE') RETURNING id
        """, Long.class, userId);
    Long milestoneId = jdbc.queryForObject("""
        INSERT INTO path_milestones(path_id, week_num, title) VALUES (?, 1, 'w1') RETURNING id
        """, Long.class, pathId);
    return jdbc.queryForObject("""
        INSERT INTO path_weekly_tasks(milestone_id, order_num, task_type, title, required)
        VALUES (?, 1, 'PRACTICE', 'task without content', true) RETURNING id
        """, Long.class, milestoneId);
  }

  @Test
  void completeTaskIfOwnedSetsCompletedAtForOwner() {
    long userId = 888001L;
    long taskId = newTaskWithoutContent(userId);

    int updated = tasks.completeTaskIfOwned(userId, taskId);

    assertThat(updated).isEqualTo(1);
    Instant completedAt = jdbc.queryForObject(
        "select completed_at from path_weekly_tasks where id = ?", Instant.class, taskId);
    assertThat(completedAt).isNotNull();
  }

  @Test
  void completeTaskIfOwnedReturnsZeroForNonOwner() {
    long ownerUserId = 888002L;
    long otherUserId = 888003L;
    long taskId = newTaskWithoutContent(ownerUserId);

    int updated = tasks.completeTaskIfOwned(otherUserId, taskId);

    assertThat(updated).isEqualTo(0);
  }

  @Test
  void hasCompletedTaskOnDateTrueWhenCompletedThatDay() {
    long userId = 888004L;
    long taskId = newTaskWithoutContent(userId);
    tasks.completeTaskIfOwned(userId, taskId);
    LocalDate today = Instant.now().atZone(ZoneOffset.UTC).toLocalDate();

    assertThat(tasks.hasCompletedTaskOnDate(userId, today)).isTrue();
  }

  @Test
  void hasCompletedTaskOnDateFalseWhenNoActivity() {
    long userId = 888005L;
    LocalDate today = Instant.now().atZone(ZoneOffset.UTC).toLocalDate();

    assertThat(tasks.hasCompletedTaskOnDate(userId, today)).isFalse();
  }
}
