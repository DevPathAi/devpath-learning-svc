package ai.devpath.learning.path;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 활성 경로에서 현재 미션에 필요한 최소 projection만 한 SQL로 읽는다.
 * 진단과 콘텐츠 본문은 읽지 않으며, milestone/task 정렬은 producer 순서를 보존한다.
 */
@Repository
public class CurrentMissionQueryRepository {
  private static final String CURRENT_MISSION_SQL = """
      WITH active_path AS MATERIALIZED (
        SELECT p.id
        FROM learning_paths p
        WHERE p.user_id = :userId
          AND p.status = 'ACTIVE'
        ORDER BY p.generated_at DESC, p.id DESC
        LIMIT 1
      ),
      path_shape AS MATERIALIZED (
        SELECT p.id AS path_id,
          count(DISTINCT m.id) AS milestone_count,
          count(t.id) AS task_count
        FROM active_path p
        LEFT JOIN path_milestones m ON m.path_id = p.id
        LEFT JOIN path_weekly_tasks t ON t.milestone_id = m.id
        GROUP BY p.id
      ),
      first_incomplete AS MATERIALIZED (
        SELECT m.id AS milestone_id, t.id AS task_id
        FROM active_path p
        JOIN path_milestones m ON m.path_id = p.id
        JOIN path_weekly_tasks t ON t.milestone_id = m.id
        WHERE t.completed_at IS NULL
        ORDER BY m.week_num ASC, m.id ASC, t.order_num ASC, t.id ASC
        LIMIT 1
      ),
      first_empty_milestone AS MATERIALIZED (
        SELECT m.id AS milestone_id, m.week_num
        FROM active_path p
        JOIN path_milestones m ON m.path_id = p.id
        LEFT JOIN path_weekly_tasks t ON t.milestone_id = m.id
        GROUP BY m.id, m.week_num
        HAVING count(t.id) = 0
        ORDER BY m.week_num ASC, m.id ASC
        LIMIT 1
      ),
      final_milestone AS MATERIALIZED (
        SELECT m.id AS milestone_id
        FROM active_path p
        JOIN path_milestones m ON m.path_id = p.id
        ORDER BY m.week_num DESC, m.id DESC
        LIMIT 1
      ),
      candidate AS MATERIALIZED (
        SELECT p.id AS path_id,
          CASE
            WHEN s.milestone_count = 0
              OR s.task_count = 0
              OR (empty.milestone_id IS NOT NULL
                AND (fi.task_id IS NULL OR empty.week_num <= incomplete.week_num))
              THEN 'MALFORMED_PATH'
            WHEN fi.task_id IS NULL THEN 'PATH_COMPLETED'
            ELSE 'AVAILABLE'
          END AS candidate_outcome,
          CASE
            WHEN s.milestone_count = 0
              OR s.task_count = 0
              OR (empty.milestone_id IS NOT NULL
                AND (fi.task_id IS NULL OR empty.week_num <= incomplete.week_num))
              THEN NULL
            WHEN fi.task_id IS NULL THEN fm.milestone_id
            ELSE fi.milestone_id
          END AS selected_milestone_id,
          CASE
            WHEN s.milestone_count = 0
              OR s.task_count = 0
              OR (empty.milestone_id IS NOT NULL
                AND (fi.task_id IS NULL OR empty.week_num <= incomplete.week_num))
              THEN NULL
            ELSE fi.task_id
          END AS next_task_id
        FROM active_path p
        JOIN path_shape s ON s.path_id = p.id
        LEFT JOIN first_incomplete fi ON true
        LEFT JOIN path_milestones incomplete ON incomplete.id = fi.milestone_id
        LEFT JOIN first_empty_milestone empty ON true
        LEFT JOIN final_milestone fm ON true
      ),
      validated AS MATERIALIZED (
        SELECT candidate.*,
          CASE
            WHEN candidate.candidate_outcome = 'MALFORMED_PATH'
              OR NOT EXISTS (
                SELECT 1
                FROM path_weekly_tasks selected_task
                WHERE selected_task.milestone_id = candidate.selected_milestone_id
              )
              OR EXISTS (
                SELECT 1
                FROM path_weekly_tasks selected_task
                LEFT JOIN contents selected_content
                  ON selected_content.id = selected_task.content_id
                WHERE selected_task.milestone_id = candidate.selected_milestone_id
                  AND selected_task.content_id IS NOT NULL
                  AND (selected_content.id IS NULL OR selected_content.status <> 'PUBLISHED')
              )
              THEN 'MALFORMED_PATH'
            ELSE candidate.candidate_outcome
          END AS outcome
        FROM candidate
      ),
      decision AS MATERIALIZED (
        SELECT path_id, outcome,
          CASE WHEN outcome = 'MALFORMED_PATH' THEN NULL ELSE selected_milestone_id END
            AS selected_milestone_id,
          CASE WHEN outcome = 'AVAILABLE' THEN next_task_id ELSE NULL END AS next_task_id
        FROM validated
      )
      SELECT d.path_id, d.outcome, d.next_task_id,
        m.week_num,
        t.id AS task_id, t.order_num, t.task_type, t.title, t.required,
        t.content_id, c.slug AS content_slug, t.completed_at
      FROM decision d
      LEFT JOIN path_milestones m ON m.id = d.selected_milestone_id
      LEFT JOIN path_weekly_tasks t ON t.milestone_id = m.id
      LEFT JOIN contents c ON c.id = t.content_id AND c.status = 'PUBLISHED'
      ORDER BY m.week_num ASC NULLS LAST, m.id ASC NULLS LAST,
        t.order_num ASC NULLS LAST, t.id ASC NULLS LAST
      """;

  private final NamedParameterJdbcTemplate jdbc;

  public CurrentMissionQueryRepository(NamedParameterJdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public ThisWeekView findForUser(long userId) {
    List<MissionRow> rows = jdbc.query(
        CURRENT_MISSION_SQL, Map.of("userId", userId), this::mapRow);
    if (rows.isEmpty()) {
      return ThisWeekView.noActivePath();
    }

    MissionRow first = rows.getFirst();
    if (first.outcome() == CurrentMissionOutcome.MALFORMED_PATH) {
      return ThisWeekView.malformed(first.pathId());
    }

    List<WeeklyTaskView> tasks = rows.stream()
        .filter(row -> row.task() != null)
        .map(MissionRow::task)
        .toList();
    WeeklyTaskView nextTask = first.nextTaskId() == null ? null : tasks.stream()
        .filter(task -> first.nextTaskId().equals(task.taskId()))
        .findFirst()
        .orElse(null);
    if (first.outcome() == CurrentMissionOutcome.AVAILABLE && nextTask == null) {
      return ThisWeekView.malformed(first.pathId());
    }
    return new ThisWeekView(
        first.pathId(),
        first.weekNum(),
        tasks,
        first.outcome(),
        nextTask,
        first.outcome() == CurrentMissionOutcome.PATH_COMPLETED);
  }

  private MissionRow mapRow(ResultSet rs, int rowNum) throws SQLException {
    Long taskId = nullableLong(rs, "task_id");
    WeeklyTaskView task = taskId == null ? null : new WeeklyTaskView(
        nullableInteger(rs, "order_num"),
        rs.getString("task_type"),
        rs.getString("title"),
        rs.getBoolean("required"),
        nullableLong(rs, "content_id"),
        rs.getString("content_slug"),
        rs.getTimestamp("completed_at") != null,
        taskId,
        timestamp(rs, "completed_at"));
    return new MissionRow(
        nullableLong(rs, "path_id"),
        nullableInteger(rs, "week_num"),
        CurrentMissionOutcome.valueOf(rs.getString("outcome")),
        nullableLong(rs, "next_task_id"),
        task);
  }

  private Long nullableLong(ResultSet rs, String column) throws SQLException {
    Number value = (Number) rs.getObject(column);
    return value == null ? null : value.longValue();
  }

  private Integer nullableInteger(ResultSet rs, String column) throws SQLException {
    Number value = (Number) rs.getObject(column);
    return value == null ? null : value.intValue();
  }

  private Instant timestamp(ResultSet rs, String column) throws SQLException {
    Timestamp value = rs.getTimestamp(column);
    return value == null ? null : value.toInstant();
  }

  private record MissionRow(
      Long pathId,
      Integer weekNum,
      CurrentMissionOutcome outcome,
      Long nextTaskId,
      WeeklyTaskView task) {}
}
