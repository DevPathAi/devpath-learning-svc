package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InternalContentServiceTest {

  private final ContentRepository contents = mock(ContentRepository.class);
  private final InternalContentService service = new InternalContentService(contents);

  private Content withBody(String body) {
    Content c = new Content();
    c.setSlug("intro");
    c.setTitle("Intro");
    c.setTrack("BACKEND_SPRING");
    c.setContentMd(body);
    c.setStatus("PUBLISHED");
    try {
      var idField = Content.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(c, 1L);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
    return c;
  }

  @Test
  void keepsBodyWhenAtOrUnderLimit() {
    String body = "a".repeat(InternalContentService.MAX_BODY_CHARS);
    when(contents.findByIdAndStatus(1L, "PUBLISHED")).thenReturn(Optional.of(withBody(body)));

    InternalContentView view = service.get(1L);

    assertThat(view.body()).isEqualTo(body);
    assertThat(view.body()).hasSize(InternalContentService.MAX_BODY_CHARS);
  }

  @Test
  void truncatesBodyWhenOverLimit() {
    String body = "a".repeat(InternalContentService.MAX_BODY_CHARS + 100);
    when(contents.findByIdAndStatus(1L, "PUBLISHED")).thenReturn(Optional.of(withBody(body)));

    InternalContentView view = service.get(1L);

    assertThat(view.body())
        .hasSize(InternalContentService.MAX_BODY_CHARS + 1)
        .endsWith("…")
        .startsWith("a".repeat(InternalContentService.MAX_BODY_CHARS));
  }

  @Test
  void nullBodyBecomesEmptyString() {
    when(contents.findByIdAndStatus(1L, "PUBLISHED")).thenReturn(Optional.of(withBody(null)));

    InternalContentView view = service.get(1L);

    assertThat(view.body()).isEmpty();
  }

  @Test
  void throwsWhenNotFoundOrNotPublished() {
    when(contents.findByIdAndStatus(99L, "PUBLISHED")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(99L))
        .isInstanceOf(ContentNotFoundException.class);
  }
}
