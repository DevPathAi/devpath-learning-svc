package ai.devpath.learning.path;

import java.time.Instant;

public record WeeklyTaskView(
    Integer orderNum,
    String taskType,
    String title,
    boolean required,
    Long contentId,
    String contentSlug,
    boolean completed,
    Long taskId,
    Instant completedAt
) {
}
