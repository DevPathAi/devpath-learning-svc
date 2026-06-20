package ai.devpath.learning.path;

import java.util.List;

public record LearningPathView(
    Long pathId,
    String track,
    Integer totalWeeks,
    String rationale,
    DiagnosisView diagnosis,
    List<MilestoneView> milestones
) {
  public record DiagnosisView(
      String diagnosedLevel,
      List<String> strengthConcepts,
      List<String> weaknessConcepts
  ) {
  }
}
