package ai.devpath.learning.path;

public class PathContractException extends RuntimeException {
  public PathContractException(String message) {
    super(message);
  }

  public PathContractException(String message, Throwable cause) {
    super(message, cause);
  }
}
