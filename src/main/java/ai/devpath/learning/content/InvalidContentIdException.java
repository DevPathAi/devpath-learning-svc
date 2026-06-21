package ai.devpath.learning.content;

public class InvalidContentIdException extends RuntimeException {
  public InvalidContentIdException(String idOrSlug) {
    super("invalid content id: " + idOrSlug);
  }
}
