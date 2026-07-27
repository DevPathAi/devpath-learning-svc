package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.AssessmentEventPublisher;
import ai.devpath.learning.assessment.AssessmentItemRepository;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.AssessmentResultRepository;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class ClaimServiceTest {

  private static final String CLAIM_PREFIX = "assessment:claim:";

  private final GuestSessionStore guestStore = mock(GuestSessionStore.class);
  private final AssessmentRepository assessments = mock(AssessmentRepository.class);
  private final AssessmentItemRepository items = mock(AssessmentItemRepository.class);
  private final AssessmentResultRepository results = mock(AssessmentResultRepository.class);
  private final AssessmentEventPublisher publisher = mock(AssessmentEventPublisher.class);
  private final AdaptiveEngine engine = mock(AdaptiveEngine.class);
  private final StringRedisTemplate redis = mock(StringRedisTemplate.class);

  @SuppressWarnings("unchecked")
  private final ValueOperations<String, String> valueOps = mock(ValueOperations.class);

  private final ClaimService service = new ClaimService(
      guestStore, assessments, items, results, publisher, engine, redis);

  private GuestSession completedSession(String guestId) {
    return new GuestSession(guestId, "BACKEND_SPRING", 0.5, null,
        List.of(new GuestSession.Presented(1L, 0.5, true, false, "\"A\"", 5)),
        true, "MID");
  }

  @Test
  void idempotentWhenClaimKeyAlreadyPresent() {
    when(redis.opsForValue()).thenReturn(valueOps);
    when(valueOps.get(CLAIM_PREFIX + "guest-1")).thenReturn("777");

    long result = service.claim(42L, "guest-1");

    assertThat(result).isEqualTo(777L);
    // 멱등: guest 세션을 조회하지 않고 기존 assessmentId 반환
    verify(guestStore, never()).find(anyString());
  }

  @Test
  void throwsWhenLockNotAcquired() {
    when(redis.opsForValue()).thenReturn(valueOps);
    when(valueOps.get(CLAIM_PREFIX + "guest-1")).thenReturn(null);
    when(valueOps.setIfAbsent(eq(CLAIM_PREFIX + "lock:guest-1"), eq("1"), any(Duration.class)))
        .thenReturn(false);

    assertThatThrownBy(() -> service.claim(42L, "guest-1"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("claim 처리 중");
  }

  @Test
  void throwsWhenGuestSessionNotCompleted() {
    when(redis.opsForValue()).thenReturn(valueOps);
    when(valueOps.get(CLAIM_PREFIX + "guest-1")).thenReturn(null);
    when(valueOps.setIfAbsent(eq(CLAIM_PREFIX + "lock:guest-1"), eq("1"), any(Duration.class)))
        .thenReturn(true);
    GuestSession notCompleted = new GuestSession("guest-1", "BACKEND_SPRING", 0.5, null,
        List.of(), false, null);
    when(guestStore.find("guest-1")).thenReturn(Optional.of(notCompleted));

    assertThatThrownBy(() -> service.claim(42L, "guest-1"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("완료되지 않은");
  }
}
