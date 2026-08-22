package ai.devpath.learning.path;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PathWeeklyTaskRepository {
  private final NamedParameterJdbcTemplate jdbc;

  public PathWeeklyTaskRepository(NamedParameterJdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /**
   * content_id가 없는 태스크의 명시적 완료 처리.
   * 본인 ACTIVE 경로이면 replay도 성공(1)하되 완료된 행은 다시 쓰지 않는다.
   */
  public int completeTaskIfOwned(long userId, long taskId) {
    var sql = """
        WITH eligible AS MATERIALIZED (
          SELECT t.id
          FROM path_weekly_tasks t
          JOIN path_milestones m ON m.id = t.milestone_id
          JOIN learning_paths p ON p.id = m.path_id
          WHERE t.id = :taskId
            AND t.content_id IS NULL
            AND p.user_id = :userId
            AND p.status = 'ACTIVE'
        ),
        updated AS (
          UPDATE path_weekly_tasks t
          SET completed_at = now()
          FROM eligible e
          WHERE t.id = e.id
            AND t.completed_at IS NULL
          RETURNING t.id
        )
        SELECT EXISTS (SELECT 1 FROM eligible)
        """;
    Boolean eligible = jdbc.queryForObject(
        sql, Map.of("taskId", taskId, "userId", userId), Boolean.class);
    return Boolean.TRUE.equals(eligible) ? 1 : 0;
  }

  /** 스트릭 활동 판정용: 해당 유저가 주어진 UTC 날짜에 완료한 주간 과제가 하나라도 있는지. */
  public boolean hasCompletedTaskOnDate(long userId, LocalDate date) {
    var sql = """
        SELECT EXISTS (
          SELECT 1 FROM path_weekly_tasks t
          JOIN path_milestones m ON t.milestone_id = m.id
          JOIN learning_paths p ON p.id = m.path_id
          WHERE p.user_id = :userId
            AND t.completed_at >= :dayStart::timestamptz
            AND t.completed_at < :dayEnd::timestamptz
        )
        """;
    return Boolean.TRUE.equals(jdbc.queryForObject(sql, Map.of(
        "userId", userId,
        "dayStart", java.sql.Timestamp.from(date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant()),
        "dayEnd", java.sql.Timestamp.from(date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant()))
        , Boolean.class));
  }
}
