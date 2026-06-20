package ai.devpath.learning.path;

public class NoCompletedAssessmentException extends RuntimeException {
  public NoCompletedAssessmentException(long userId) {
    super("NO_COMPLETED_ASSESSMENT: userId=" + userId);
  }
}
