package ai.devpath.learning.path.ai;

import java.util.List;

public record PathGenerateResult(String rationale, List<Milestone> milestones) {
  public record Milestone(
      int weekNum,
      String title,
      String goalDescription,
      List<String> targetSkills,
      int estimatedHours,
      String whyThisOrder,
      String expectedOutcome,
      List<Task> tasks
  ) {
  }

  public record Task(int orderNum, String taskType, String title, boolean required) {
  }
}
