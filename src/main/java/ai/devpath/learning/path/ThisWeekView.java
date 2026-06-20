package ai.devpath.learning.path;

import java.util.List;

public record ThisWeekView(Long pathId, Integer weekNum, List<WeeklyTaskView> tasks) {
}
