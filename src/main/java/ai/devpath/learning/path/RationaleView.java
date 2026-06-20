package ai.devpath.learning.path;

import java.util.List;

public record RationaleView(Long pathId, String rationale, List<MilestoneRationale> milestones) {
  public record MilestoneRationale(Integer weekNum, String whyThisOrder) {
  }
}
