package ai.devpath.learning.content;

public class ContentNotFoundException extends RuntimeException {
  public ContentNotFoundException(String idOrSlug) {
    super("content not found: " + idOrSlug);
  }
}
