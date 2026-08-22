package ai.devpath.learning.path;

import java.util.List;

public record ThisWeekView(
    Long pathId,
    Integer weekNum,
    List<WeeklyTaskView> tasks,
    CurrentMissionOutcome outcome,
    WeeklyTaskView nextTask,
    boolean pathCompleted
) {
  public static ThisWeekView noActivePath() {
    return new ThisWeekView(
        null, null, List.of(), CurrentMissionOutcome.NO_ACTIVE_PATH, null, false);
  }

  public static ThisWeekView malformed(Long pathId) {
    return new ThisWeekView(
        pathId, null, List.of(), CurrentMissionOutcome.MALFORMED_PATH, null, false);
  }
}
