package ai.devpath.learning.progress;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SandboxActivityLogRepository {
  private final NamedParameterJdbcTemplate jdbc;

  public SandboxActivityLogRepository(NamedParameterJdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public void record(long userId, java.time.Instant occurredAt) {
    jdbc.update("INSERT INTO sandbox_activity_log(user_id, occurred_at) VALUES (:userId, :occurredAt)",
        Map.of("userId", userId, "occurredAt", java.sql.Timestamp.from(occurredAt)));
  }

  public boolean hasActivityOnDate(long userId, LocalDate date) {
    var sql = """
        SELECT EXISTS (
          SELECT 1 FROM sandbox_activity_log
          WHERE user_id = :userId AND occurred_at >= :dayStart::timestamptz AND occurred_at < :dayEnd::timestamptz
        )
        """;
    return Boolean.TRUE.equals(jdbc.queryForObject(sql, Map.of(
        "userId", userId,
        "dayStart", java.sql.Timestamp.from(date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant()),
        "dayEnd", java.sql.Timestamp.from(date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant()))
        , Boolean.class));
  }
}
