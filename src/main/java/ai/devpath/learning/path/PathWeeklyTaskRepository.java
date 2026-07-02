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

  /** content_id가 없는 태스크의 명시적 완료 처리. 본인 소유(ACTIVE 경로)의 태스크만 갱신, 이미 완료면 no-op. 반환값: 갱신된 행 수(0 또는 1). */
  public int completeTaskIfOwned(long userId, long taskId) {
    var sql = """
        UPDATE path_weekly_tasks t
        SET completed_at = COALESCE(t.completed_at, now())
        FROM path_milestones m
        JOIN learning_paths p ON p.id = m.path_id
        WHERE t.milestone_id = m.id
          AND t.id = :taskId
          AND p.user_id = :userId
          AND p.status = 'ACTIVE'
        """;
    return jdbc.update(sql, Map.of("taskId", taskId, "userId", userId));
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
        "dayStart", date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant(),
        "dayEnd", date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant())
        , Boolean.class));
  }
}
