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
        Map.of("userId", userId, "occurredAt", occurredAt));
  }

  public boolean hasActivityOnDate(long userId, LocalDate date) {
    var sql = """
        SELECT EXISTS (
          SELECT 1 FROM sandbox_activity_log
          WHERE user_id = :userId AND occurred_at >= :dayStart AND occurred_at < :dayEnd
        )
        """;
    return Boolean.TRUE.equals(jdbc.queryForObject(sql, Map.of(
        "userId", userId,
        "dayStart", date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant(),
        "dayEnd", date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant())
        , Boolean.class));
  }
}
