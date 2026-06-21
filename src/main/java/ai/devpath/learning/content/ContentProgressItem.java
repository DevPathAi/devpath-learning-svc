package ai.devpath.learning.content;

import java.time.Instant;

public record ContentProgressItem(
    Long contentId,
    String slug,
    String title,
    String track,
    double scrollPct,
    int dwellSec,
    boolean completed,
    Instant completedAt,
    Instant updatedAt
) {
}
