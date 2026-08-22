package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class LegacyClaimHintStoreTest {

  private static final Instant NOW = Instant.parse("2026-08-15T00:00:00Z");
  private static final String GUEST_ID = "11111111-1111-4111-8111-111111111111";

  private final StringRedisTemplate redis = mock(StringRedisTemplate.class);
  @SuppressWarnings("unchecked")
  private final ValueOperations<String, String> values = mock(ValueOperations.class);
  private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);

  @Test
  void disabledOrExpiredBridgeNeverReadsAnUnboundedLegacyMarker() {
    var disabled = new LegacyClaimHintStore(redis, clock, null);
    var expired = new LegacyClaimHintStore(redis, clock, NOW);

    assertThat(disabled.take(GUEST_ID)).isEmpty();
    assertThat(expired.take(GUEST_ID)).isEmpty();
    verify(redis, never()).opsForValue();
  }

  @Test
  void activeBridgeUsesAtomicGetAndExpireAndReturnsOnlyPositiveAssessmentId() {
    when(redis.opsForValue()).thenReturn(values);
    when(values.getAndExpire(
        "assessment:claim:" + GUEST_ID, Duration.ofMinutes(10))).thenReturn("777");
    var store = new LegacyClaimHintStore(redis, clock, NOW.plus(Duration.ofMinutes(10)));

    assertThat(store.take(GUEST_ID)).hasValue(777L);

    verify(values).getAndExpire(
        "assessment:claim:" + GUEST_ID, Duration.ofMinutes(10));
  }

  @Test
  void bridgeConfigurationCannotExtendPastTheThirtyMinuteCompatibilityWindow() {
    assertThatThrownBy(() -> new LegacyClaimHintStore(
        redis, clock, NOW.plus(Duration.ofMinutes(30)).plusMillis(1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("30분");
  }

  @Test
  void malformedMarkerIsDeletedAndNeverReturned() {
    when(redis.opsForValue()).thenReturn(values);
    when(values.getAndExpire(
        "assessment:claim:" + GUEST_ID, Duration.ofMinutes(10))).thenReturn("not-an-id");
    var store = new LegacyClaimHintStore(redis, clock, NOW.plus(Duration.ofMinutes(10)));

    assertThat(store.take(GUEST_ID)).isEmpty();

    verify(redis).delete("assessment:claim:" + GUEST_ID);
  }
}
