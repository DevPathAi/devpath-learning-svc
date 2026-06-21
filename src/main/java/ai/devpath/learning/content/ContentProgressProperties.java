package ai.devpath.learning.content;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContentProgressProperties {
  private final double scrollThreshold;
  private final int minDwellSec;

  public ContentProgressProperties(
      @Value("${devpath.content.completion.scroll-threshold:0.8}") double scrollThreshold,
      @Value("${devpath.content.completion.min-dwell-sec:45}") int minDwellSec) {
    this.scrollThreshold = scrollThreshold;
    this.minDwellSec = minDwellSec;
  }

  public double scrollThreshold() {
    return scrollThreshold;
  }

  public int minDwellSec() {
    return minDwellSec;
  }
}
