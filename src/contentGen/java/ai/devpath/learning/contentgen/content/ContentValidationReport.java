package ai.devpath.learning.contentgen.content;

import java.util.List;

public record ContentValidationReport(List<String> errors, List<String> warnings) {

  public boolean valid() {
    return errors.isEmpty();
  }
}
