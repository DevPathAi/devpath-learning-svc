package ai.devpath.learning.release;

import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** Narrow release reads for invariants not exposed by production repositories. */
@Component
public class LearningReleaseFacts {
  private final JdbcTemplate jdbc;

  public LearningReleaseFacts(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public long countOwnedClaims(long userId, String guestId) {
    Long count = jdbc.queryForObject(
        "SELECT count(*) FROM assessments WHERE user_id=? AND source_guest_id=?",
        Long.class, userId, guestId);
    return count == null ? 0 : count;
  }

  public int countCompletedTasks(long userId) {
    Integer count = jdbc.queryForObject("""
        SELECT count(*)
        FROM path_weekly_tasks task
        JOIN path_milestones milestone ON milestone.id=task.milestone_id
        JOIN learning_paths path ON path.id=milestone.path_id
        WHERE path.user_id=? AND path.status='ACTIVE' AND task.completed_at IS NOT NULL
        """, Integer.class, userId);
    return count == null ? 0 : count;
  }

  public Instant taskCompletedAt(long taskId) {
    Timestamp value = jdbc.queryForObject(
        "SELECT completed_at FROM path_weekly_tasks WHERE id=?",
        Timestamp.class, taskId);
    return value == null ? null : value.toInstant();
  }
}
