package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

class ClaimServiceTest {

  private static final String GUEST_ID = "aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa";

  private final GuestSessionStore guestStore = mock(GuestSessionStore.class);
  private final AssessmentRepository assessments = mock(AssessmentRepository.class);
  private final ClaimPersistenceService persistence = mock(ClaimPersistenceService.class);
  private final LegacyClaimHintStore legacyHints = mock(LegacyClaimHintStore.class);
  private final ClaimService service =
      new ClaimService(guestStore, assessments, persistence, legacyHints);

  @Test
  void nullOrMalformedGuestIdIsRejectedBeforeAnyLookup() {
    assertThatThrownBy(() -> service.claim(42L, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("식별자");
    assertThatThrownBy(() -> service.claim(42L, "not-a-uuid"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("식별자");

    verifyNoInteractions(assessments, guestStore, persistence, legacyHints);
  }

  @Test
  void uuidInputIsCanonicalizedBeforeDatabaseAndRedisLookup() {
    Assessment committed = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.of(committed));

    assertThat(service.claim(42L, GUEST_ID.toUpperCase(java.util.Locale.ROOT)))
        .isEqualTo(777L);

    verify(assessments).findBySourceGuestId(GUEST_ID);
    verify(guestStore).delete(GUEST_ID);
    verify(legacyHints).delete(GUEST_ID);
  }

  @Test
  void sameOwnerReplayUsesCommittedDatabaseRowEvenAfterGuestExpiry() {
    Assessment committed = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.of(committed));

    long result = service.claim(42L, GUEST_ID);

    assertThat(result).isEqualTo(777L);
    verify(guestStore, never()).find(anyString());
    verify(guestStore).delete(GUEST_ID);
    verify(legacyHints).delete(GUEST_ID);
    verify(persistence, never()).persist(anyLong(), anyString(), any(GuestSession.class));
  }

  @Test
  void differentOwnerReplayIsDeniedWithoutLookingUpGuestOrCreatingData() {
    Assessment committed = assessment(777L, 41L);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.of(committed));

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID))
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageNotContaining("777");

    verify(guestStore, never()).find(anyString());
    verify(persistence, never()).persist(anyLong(), anyString(), any(GuestSession.class));
  }

  @Test
  void completedGuestIsPersistedThenDeletedAfterCommit() {
    GuestSession session = completedSession(GUEST_ID);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));
    when(persistence.persist(42L, GUEST_ID, session)).thenReturn(777L);

    long result = service.claim(42L, GUEST_ID);

    assertThat(result).isEqualTo(777L);
    verify(persistence).persist(42L, GUEST_ID, session);
    verify(guestStore).delete(GUEST_ID);
    verify(legacyHints).delete(GUEST_ID);
  }

  @Test
  void cleanupFailureAfterCommitDoesNotTurnSuccessIntoFailure() {
    GuestSession session = completedSession(GUEST_ID);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));
    when(persistence.persist(42L, GUEST_ID, session)).thenReturn(777L);
    org.mockito.Mockito.doThrow(new IllegalStateException("redis unavailable"))
        .when(guestStore).delete(GUEST_ID);

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);
    verify(legacyHints).delete(GUEST_ID);
  }

  @Test
  void uniqueConflictReturnsCommittedWinnerOnlyForTheSameOwner() {
    GuestSession session = completedSession(GUEST_ID);
    Assessment winner = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(winner));
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));
    when(persistence.persist(42L, GUEST_ID, session))
        .thenThrow(new DataIntegrityViolationException("duplicate source_guest_id"));

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);
    verify(guestStore).delete(GUEST_ID);
  }

  @Test
  void uniqueConflictWithAnotherOwnerIsDeniedWithoutLeakingAssessmentId() {
    GuestSession session = completedSession(GUEST_ID);
    Assessment winner = assessment(777L, 41L);
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(winner));
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));
    when(persistence.persist(42L, GUEST_ID, session))
        .thenThrow(new DataIntegrityViolationException("duplicate source_guest_id"));

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID))
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageNotContaining("777");
    verify(guestStore, never()).delete(anyString());
  }

  @Test
  void missingWinnerAfterConstraintFailurePreservesTheDatabaseFailure() {
    GuestSession session = completedSession(GUEST_ID);
    DataIntegrityViolationException failure =
        new DataIntegrityViolationException("duplicate source_guest_id");
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));
    when(persistence.persist(42L, GUEST_ID, session)).thenThrow(failure);

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID)).isSameAs(failure);
    verify(guestStore, never()).delete(anyString());
  }

  @Test
  void guestDeletedByConcurrentWinnerIsRecoveredFromDatabaseRecheck() {
    Assessment winner = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(winner));
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.empty());

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);
    verify(persistence, never()).persist(anyLong(), anyString(), any(GuestSession.class));
    verify(guestStore).delete(GUEST_ID);
  }

  @Test
  void oldVersionMarkerIsOnlyAHintAndBindsItsOwnerInTheDatabase() {
    Assessment winner = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(winner));
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.empty());
    when(legacyHints.take(GUEST_ID)).thenReturn(OptionalLong.of(777L));
    when(assessments.bindLegacyClaimSource(777L, 42L, GUEST_ID)).thenReturn(1);

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);

    verify(assessments).bindLegacyClaimSource(777L, 42L, GUEST_ID);
    verify(legacyHints).delete(GUEST_ID);
    verify(guestStore).delete(GUEST_ID);
  }

  @Test
  void oldVersionMarkerCannotAuthorizeAnotherOwnerOrLeakItsAssessmentId() {
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.empty());
    when(legacyHints.take(GUEST_ID)).thenReturn(OptionalLong.of(777L));
    when(assessments.bindLegacyClaimSource(777L, 42L, GUEST_ID)).thenReturn(0);

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID))
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageNotContaining("777");

    verify(legacyHints, never()).delete(anyString());
    verify(guestStore, never()).delete(anyString());
  }

  @Test
  void concurrentLegacyBindingUniqueConflictResolvesOnlyTheDatabaseWinner() {
    Assessment winner = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(winner));
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.empty());
    when(legacyHints.take(GUEST_ID)).thenReturn(OptionalLong.of(777L));
    when(assessments.bindLegacyClaimSource(777L, 42L, GUEST_ID))
        .thenThrow(new DataIntegrityViolationException("duplicate source_guest_id"));

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);
  }

  @Test
  void legacyMarkerCleanupFailureStillAttemptsGuestCleanupAndReturnsCommittedId() {
    Assessment committed = assessment(777L, 42L);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.of(committed));
    org.mockito.Mockito.doThrow(new IllegalStateException("redis unavailable"))
        .when(legacyHints).delete(GUEST_ID);

    assertThat(service.claim(42L, GUEST_ID)).isEqualTo(777L);

    verify(legacyHints).delete(GUEST_ID);
    verify(guestStore).delete(GUEST_ID);
  }

  @Test
  void expiredGuestWithoutCommittedRowIsNotFound() {
    when(assessments.findBySourceGuestId(GUEST_ID))
        .thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID))
        .isInstanceOf(java.util.NoSuchElementException.class)
        .hasMessageContaining("없음/만료");
  }

  @Test
  void incompleteGuestIsRejectedBeforePersistence() {
    GuestSession session = new GuestSession(
        GUEST_ID, "BACKEND_SPRING", 0.5, null, List.of(), false, null);
    when(assessments.findBySourceGuestId(GUEST_ID)).thenReturn(Optional.empty());
    when(guestStore.find(GUEST_ID)).thenReturn(Optional.of(session));

    assertThatThrownBy(() -> service.claim(42L, GUEST_ID))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("완료되지 않은");
    verify(persistence, never()).persist(anyLong(), anyString(), any(GuestSession.class));
  }

  private GuestSession completedSession(String guestId) {
    return new GuestSession(
        guestId,
        "BACKEND_SPRING",
        0.5,
        null,
        List.of(new GuestSession.Presented(1L, 0.5, true, false, "\"A\"", 5)),
        true,
        "MID");
  }

  private Assessment assessment(long id, long userId) {
    Assessment assessment = mock(Assessment.class);
    when(assessment.getId()).thenReturn(id);
    when(assessment.getUserId()).thenReturn(userId);
    return assessment;
  }
}
