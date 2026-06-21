package ai.devpath.learning.dashboard;

import java.util.List;

public record DashboardSummary(
    int streakDays,
    int progressPercent,
    String nextTaskTitle,
    List<String> badges
) {
}
