package ai.devpath.learning.contentgen.question;

import java.util.List;

public record QuestionValidationReport(List<String> errors, List<String> warnings) {

  public boolean valid() {
    return errors.isEmpty();
  }
}
