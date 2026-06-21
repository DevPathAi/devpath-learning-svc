package ai.devpath.learning.contentgen.content;

import java.util.List;

public record ApprovedContent(
    String slug,
    String title,
    String track,
    String level,
    String contentMd,
    Integer estimatedMinutes,
    Double difficulty,
    String bloomLevel,
    List<String> conceptTags,
    String status) {
}
