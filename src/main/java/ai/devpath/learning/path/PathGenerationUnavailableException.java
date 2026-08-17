package ai.devpath.learning.path;

/** 생성 실행기가 새 작업을 받을 수 없을 때. */
public class PathGenerationUnavailableException extends RuntimeException {
  public PathGenerationUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
