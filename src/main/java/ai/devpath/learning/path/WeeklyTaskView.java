package ai.devpath.learning.path;

public record WeeklyTaskView(
    Integer orderNum,
    String taskType,
    String title,
    boolean required,
    Long contentId,
    String contentSlug,
    boolean completed
) {
}
