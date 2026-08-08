package ai.devpath.learning.content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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

  public int countCompleted(long userId) {
    Integer n =
        jdbc.queryForObject(
            "SELECT count(*) FROM user_content_progress WHERE user_id = :userId AND completed_at IS NOT NULL",
            Map.of("userId", userId),
            Integer.class);
    return n == null ? 0 : n;
  }

  /** 최근 완료 콘텐츠를 KST 일별로 집계(since 이후). 없는 날은 결과에 미포함. */
  public Map<LocalDate, Integer> dailyCompletedCounts(long userId, Instant since) {
    var sql = """
        SELECT (completed_at AT TIME ZONE 'Asia/Seoul')::date AS d, count(*) AS n
        FROM user_content_progress
        WHERE user_id = :userId AND completed_at IS NOT NULL AND completed_at >= :since
        GROUP BY d
        """;
    Map<LocalDate, Integer> out = new HashMap<>();
    jdbc.query(sql,
        Map.of("userId", userId, "since", Timestamp.from(since)),
        rs -> {
          out.put(rs.getObject("d", LocalDate.class), rs.getInt("n"));
        });
    return out;
  }

  /** 활성 경로의 전체/유형별 과제 수 + 완료 과제의 KST 완료일 목록. */
  public ActivePathCompletions activePathCompletions(long userId) {
    var totalSql = """
        SELECT t.task_type AS tt, count(*) AS n
        FROM path_weekly_tasks t
        JOIN path_milestones m ON m.id = t.milestone_id
        JOIN learning_paths p ON p.id = m.path_id
        WHERE p.user_id = :userId AND p.status = 'ACTIVE'
        GROUP BY t.task_type
        """;
    Map<String, Integer> totalByType = new HashMap<>();
    // 블록 람다로 둔다 — 표현식 람다로 줄이면 Map.put의 반환값 때문에
    // query(.., RowCallbackHandler)와 query(.., RowMapper) 오버로드가 모호해진다.
    jdbc.query(totalSql, Map.of("userId", userId), rs -> {
      totalByType.put(rs.getString("tt"), rs.getInt("n"));
    });

    var datesSql = """
        SELECT t.task_type AS tt, (t.completed_at AT TIME ZONE 'Asia/Seoul')::date AS d
        FROM path_weekly_tasks t
        JOIN path_milestones m ON m.id = t.milestone_id
        JOIN learning_paths p ON p.id = m.path_id
        WHERE p.user_id = :userId AND p.status = 'ACTIVE' AND t.completed_at IS NOT NULL
        """;
    Map<String, List<LocalDate>> completedByType = new HashMap<>();
    List<LocalDate> allDates = new ArrayList<>();
    jdbc.query(datesSql, Map.of("userId", userId), rs -> {
      LocalDate d = rs.getObject("d", LocalDate.class);
      completedByType.computeIfAbsent(rs.getString("tt"), k -> new ArrayList<>()).add(d);
      allDates.add(d);
    });

    // 전체 과제 수는 유형별 합계다 — task_type이 NOT NULL이라 GROUP BY가 모든 행을 덮는다.
    int total = totalByType.values().stream().mapToInt(Integer::intValue).sum();
    return new ActivePathCompletions(total, allDates, totalByType, completedByType);
  }

  /**
   * 활성 경로의 전체/유형별 과제 수와 완료일. taskType 키는 DB 원문(READ·PRACTICE·QUIZ).
   *
   * <p>완료가 0건인 유형은 completedByType에 **키 자체가 없다** — 「0건」과 「없음」은 다르다.
   */
  public record ActivePathCompletions(
      int totalTasks,
      List<LocalDate> completedDates,
      Map<String, Integer> totalByType,
      Map<String, List<LocalDate>> completedByType) {}

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
