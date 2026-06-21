package ai.devpath.learning.content;

import java.util.List;

public record LearningContentView(
    Long id,
    String slug,
    String title,
    String track,
    String markdown,
    Integer estimatedMinutes,
    Double difficulty,
    String bloomLevel,
    List<String> conceptTags,
    ContentProgressView progress
) {
}
