package ai.devpath.learning.path;

import ai.devpath.learning.path.ai.PathGenerateResult;
import java.util.List;

public record GeneratedLearningPath(
    LatestDiagnosis diagnosis,
    PathGenerateResult aiResult,
    List<GeneratedMilestone> milestones
) {
  public record GeneratedMilestone(
      PathGenerateResult.Milestone source,
      List<GeneratedTask> tasks
  ) {
  }

  public record GeneratedTask(
      PathGenerateResult.Task source,
      Long contentId
  ) {
  }
}
