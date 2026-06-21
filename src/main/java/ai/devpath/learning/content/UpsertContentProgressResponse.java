package ai.devpath.learning.content;

import java.time.Instant;

public record UpsertContentProgressResponse(
    Long contentId,
    double scrollPct,
    int dwellSec,
    boolean completed,
    Instant completedAt,
    int taskCompletedCount
) {
}
