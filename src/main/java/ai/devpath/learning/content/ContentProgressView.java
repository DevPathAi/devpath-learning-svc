package ai.devpath.learning.content;

import java.time.Instant;

public record ContentProgressView(
    double scrollPct,
    int dwellSec,
    boolean completed,
    Instant completedAt
) {
  public static ContentProgressView empty() {
    return new ContentProgressView(0.0, 0, false, null);
  }
}
