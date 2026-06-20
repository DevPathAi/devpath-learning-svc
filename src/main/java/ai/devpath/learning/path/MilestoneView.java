package ai.devpath.learning.path;

import java.util.List;

public record MilestoneView(
    Integer weekNum,
    String title,
    String goalDescription,
    List<String> targetSkills,
    Integer estimatedHours,
    String whyThisOrder,
    String expectedOutcome,
    boolean locked,
    List<WeeklyTaskView> tasks
) {
}
