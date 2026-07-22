package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import tools.jackson.databind.json.JsonMapper;

class ContentServiceTest {

  private final JsonMapper jsonMapper = JsonMapper.builder().build();

  private final ContentRepository contents = mock(ContentRepository.class);
  private final ContentProgressRepository progress = mock(ContentProgressRepository.class);
  private final ContentProgressProperties properties = mock(ContentProgressProperties.class);

  private final ContentService service =
      new ContentService(contents, progress, properties, jsonMapper);

  private UpsertContentProgressRequest req(Double scrollPct, Integer dwellSec) {
    return new UpsertContentProgressRequest(scrollPct, dwellSec);
  }

  private Content publishedContent() {
    Content c = new Content();
    c.setSlug("intro");
    c.setTitle("Intro");
    c.setTrack("BACKEND_SPRING");
    c.setContentMd("body");
    c.setStatus("PUBLISHED");
    try {
      var idField = Content.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(c, 5L);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
    return c;
  }

  @Test
  void validateRejectsNullScrollPct() {
    assertThatThrownBy(() -> service.upsertProgress(42L, "1", req(null, 10)))
        .isInstanceOf(InvalidProgressException.class);
  }

  @Test
  void validateRejectsNegativeScrollPct() {
    assertThatThrownBy(() -> service.upsertProgress(42L, "1", req(-0.1, 10)))
        .isInstanceOf(InvalidProgressException.class);
  }

  @Test
  void validateRejectsScrollPctAboveOne() {
    assertThatThrownBy(() -> service.upsertProgress(42L, "1", req(1.5, 10)))
        .isInstanceOf(InvalidProgressException.class);
  }

  @Test
  void validateRejectsNegativeDwellSec() {
    assertThatThrownBy(() -> service.upsertProgress(42L, "1", req(0.5, -1)))
        .isInstanceOf(InvalidProgressException.class);
  }

  @Test
  void clampLimitDefaultsWhenNull() {
    when(progress.list(eq(42L), any(), any(), anyInt())).thenReturn(List.of());

    service.myProgress(42L, null, null, null);

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(progress).list(eq(42L), any(), any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(50);
  }

  @Test
  void clampLimitDefaultsWhenBelowOne() {
    when(progress.list(eq(42L), any(), any(), anyInt())).thenReturn(List.of());

    service.myProgress(42L, null, null, 0);

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(progress).list(eq(42L), any(), any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(50);
  }

  @Test
  void clampLimitCapsAtMax() {
    when(progress.list(eq(42L), any(), any(), anyInt())).thenReturn(List.of());

    service.myProgress(42L, null, null, 200);

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(progress).list(eq(42L), any(), any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(100);
  }

  @Test
  void resolvePublishedUsesIdLookupForNumeric() {
    when(contents.findByIdAndStatus(5L, "PUBLISHED")).thenReturn(Optional.of(publishedContent()));
    when(progress.find(eq(42L), anyLong())).thenReturn(Optional.empty());

    var view = service.get(42L, "5");

    assertThat(view.slug()).isEqualTo("intro");
    verify(contents).findByIdAndStatus(5L, "PUBLISHED");
  }

  @Test
  void resolvePublishedUsesSlugLookupForNonNumeric() {
    when(contents.findBySlugAndStatus("intro", "PUBLISHED"))
        .thenReturn(Optional.of(publishedContent()));
    when(progress.find(eq(42L), anyLong())).thenReturn(Optional.empty());

    var view = service.get(42L, "intro");

    assertThat(view.slug()).isEqualTo("intro");
    verify(contents).findBySlugAndStatus("intro", "PUBLISHED");
  }

  @Test
  void resolvePublishedThrowsWhenNotFound() {
    when(contents.findBySlugAndStatus("missing", "PUBLISHED")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(42L, "missing"))
        .isInstanceOf(ContentNotFoundException.class);
  }
}
