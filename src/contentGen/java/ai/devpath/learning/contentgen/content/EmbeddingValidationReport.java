package ai.devpath.learning.contentgen.content;

import java.util.List;

public record EmbeddingValidationReport(List<String> errors) {

  public boolean valid() {
    return errors.isEmpty();
  }
}
