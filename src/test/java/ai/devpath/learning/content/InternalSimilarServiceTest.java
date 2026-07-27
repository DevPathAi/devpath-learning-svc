package ai.devpath.learning.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.ContentEmbeddingMatcher;
import ai.devpath.learning.path.MatchedContent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class InternalSimilarServiceTest {

  private final ContentEmbeddingMatcher matcher = mock(ContentEmbeddingMatcher.class);
  private final InternalSimilarService service = new InternalSimilarService(matcher);

  private final List<Double> embedding = List.of(0.1, 0.2, 0.3);

  private SimilarQuery query(Integer limit, String track) {
    return new SimilarQuery(embedding, limit, track);
  }

  @Test
  void clampLimitDefaultsWhenNull() {
    when(matcher.matchAny(any(), anyInt())).thenReturn(List.of());

    service.search(query(null, null));

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(matcher).matchAny(any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(3);
  }

  @Test
  void clampLimitDefaultsWhenZero() {
    when(matcher.matchAny(any(), anyInt())).thenReturn(List.of());

    service.search(query(0, null));

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(matcher).matchAny(any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(3);
  }

  @Test
  void clampLimitCapsAtMax() {
    when(matcher.matchAny(any(), anyInt())).thenReturn(List.of());

    service.search(query(20, null));

    ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(matcher).matchAny(any(), limitCaptor.capture());
    assertThat(limitCaptor.getValue()).isEqualTo(10);
  }

  @Test
  void usesTrackMatchWhenTrackPresent() {
    when(matcher.match(eq("BACKEND_SPRING"), any(), anyInt()))
        .thenReturn(List.of(new MatchedContent(1L, "intro", "Intro", 0.1)));

    List<SimilarContent> result = service.search(query(5, "BACKEND_SPRING"));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).slug()).isEqualTo("intro");
    verify(matcher).match(eq("BACKEND_SPRING"), any(), anyInt());
    verify(matcher, never()).matchAny(any(), anyInt());
  }

  @Test
  void usesMatchAnyWhenTrackBlank() {
    when(matcher.matchAny(any(), anyInt())).thenReturn(List.of());

    service.search(query(5, "  "));

    verify(matcher).matchAny(any(), anyInt());
    verify(matcher, never()).match(any(), any(), anyInt());
  }
}
