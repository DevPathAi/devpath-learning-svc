package ai.devpath.learning.content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContentProgressRepository {
  private final NamedParameterJdbcTemplate jdbc;

  public ContentProgressRepository(NamedParameterJdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public ProgressRow upsert(long userId, long contentId, double scrollPct, int dwellSec,
      double scrollThreshold, int minDwellSec) {
    var sql = """
        INSERT INTO user_content_progress(user_id, content_id, scroll_pct, dwell_sec, completed_at)
        VALUES (:userId, :contentId, :scrollPct, :dwellSec,
          CASE WHEN :scrollPct >= :scrollThreshold AND :dwellSec >= :minDwellSec
            THEN now() ELSE NULL END)
        ON CONFLICT (user_id, content_id) DO UPDATE SET
          scroll_pct = GREATEST(user_content_progress.scroll_pct, EXCLUDED.scroll_pct),
          dwell_sec = GREATEST(user_content_progress.dwell_sec, EXCLUDED.dwell_sec),
          completed_at = CASE
            WHEN GREATEST(user_content_progress.scroll_pct, EXCLUDED.scroll_pct) >= :scrollThreshold
             AND GREATEST(user_content_progress.dwell_sec, EXCLUDED.dwell_sec) >= :minDwellSec
            THEN COALESCE(user_content_progress.completed_at, now())
            ELSE user_content_progress.completed_at
          END,
          updated_at = now()
        RETURNING content_id, scroll_pct, dwell_sec, completed_at, updated_at
        """;
    return jdbc.queryForObject(sql, Map.of(
        "userId", userId,
        "contentId", contentId,
        "scrollPct", scrollPct,
        "dwellSec", dwellSec,
        "scrollThreshold", scrollThreshold,
        "minDwellSec", minDwellSec), this::progressRow);
  }

  public int completeActivePathTasks(long userId, long contentId) {
    var sql = """
        UPDATE path_weekly_tasks t
        SET completed_at = COALESCE(t.completed_at, now())
        FROM path_milestones m
        JOIN learning_paths p ON p.id = m.path_id
        WHERE t.milestone_id = m.id
          AND p.user_id = :userId
          AND p.status = 'ACTIVE'
          AND t.content_id = :contentId
          AND t.completed_at IS NULL
        """;
    return jdbc.update(sql, Map.of("userId", userId, "contentId", contentId));
  }

  public Optional<ProgressRow> find(long userId, long contentId) {
    var rows = jdbc.query("""
        SELECT content_id, scroll_pct, dwell_sec, completed_at, updated_at
        FROM user_content_progress
        WHERE user_id = :userId AND content_id = :contentId
        """, Map.of("userId", userId, "contentId", contentId), this::progressRow);
    return rows.stream().findFirst();
  }

  public List<ContentProgressItem> list(long userId, Boolean completed, String track, int limit) {
    var params = new HashMap<String, Object>();
    params.put("userId", userId);
    params.put("limit", limit);
    var sql = new StringBuilder("""
        SELECT ucp.content_id, c.slug, c.title, c.track, ucp.scroll_pct, ucp.dwell_sec,
          ucp.completed_at, ucp.updated_at
        FROM user_content_progress ucp
        JOIN contents c ON c.id = ucp.content_id
        WHERE ucp.user_id = :userId
          AND c.status = 'PUBLISHED'
        """);
    if (completed != null) {
      sql.append(completed
          ? " AND ucp.completed_at IS NOT NULL\n"
          : " AND ucp.completed_at IS NULL\n");
    }
    if (track != null && !track.isBlank()) {
      sql.append(" AND c.track = :track\n");
      params.put("track", track);
    }
    sql.append(" ORDER BY ucp.updated_at DESC, ucp.id DESC LIMIT :limit");
    return jdbc.query(sql.toString(), params, (rs, rowNum) -> new ContentProgressItem(
        rs.getLong("content_id"),
        rs.getString("slug"),
        rs.getString("title"),
        rs.getString("track"),
        rs.getDouble("scroll_pct"),
        rs.getInt("dwell_sec"),
        timestamp(rs, "completed_at") != null,
        timestamp(rs, "completed_at"),
        timestamp(rs, "updated_at")));
  }

  private ProgressRow progressRow(ResultSet rs, int rowNum) throws SQLException {
    return new ProgressRow(
        rs.getLong("content_id"),
        rs.getDouble("scroll_pct"),
        rs.getInt("dwell_sec"),
        timestamp(rs, "completed_at"),
        timestamp(rs, "updated_at"));
  }

  private Instant timestamp(ResultSet rs, String column) throws SQLException {
    Timestamp value = rs.getTimestamp(column);
    return value == null ? null : value.toInstant();
  }

  public record ProgressRow(
      Long contentId,
      double scrollPct,
      int dwellSec,
      Instant completedAt,
      Instant updatedAt
  ) {
    public boolean completed() {
      return completedAt != null;
    }
  }
}
