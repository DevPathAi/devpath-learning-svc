package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentEventPublisher;
import ai.devpath.learning.assessment.AssessmentItem;
import ai.devpath.learning.assessment.AssessmentItemRepository;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.AssessmentResult;
import ai.devpath.learning.assessment.AssessmentResultRepository;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import ai.devpath.learning.assessment.guest.GuestSession;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

class ClaimPersistenceServiceTest {

  private final AssessmentRepository assessments = mock(AssessmentRepository.class);
  private final AssessmentItemRepository items = mock(AssessmentItemRepository.class);
  private final AssessmentResultRepository results = mock(AssessmentResultRepository.class);
  private final AssessmentEventPublisher publisher = mock(AssessmentEventPublisher.class);
  private final AdaptiveEngine engine = mock(AdaptiveEngine.class);
  private final ClaimPersistenceService service =
      new ClaimPersistenceService(assessments, items, results, publisher, engine);

  @Test
  void persistsImmutableGuestOwnershipAndAllClaimArtifacts() {
    Assessment saved = mock(Assessment.class);
    when(saved.getId()).thenReturn(777L);
    when(assessments.save(any(Assessment.class))).thenReturn(saved);
    GuestSession session = new GuestSession(
        "guest-1",
        "BACKEND_SPRING",
        0.6,
        null,
        List.of(
            new GuestSession.Presented(11L, 0.4, true, false, "\"A\"", 3),
            new GuestSession.Presented(12L, 0.7, null, true, null, 2)),
        true,
        "MID");

    long result = service.persist(42L, "guest-1", session);

    assertThat(result).isEqualTo(777L);
    ArgumentCaptor<Assessment> assessmentCaptor = ArgumentCaptor.forClass(Assessment.class);
    verify(assessments).save(assessmentCaptor.capture());
    Assessment assessment = assessmentCaptor.getValue();
    assertThat(assessment.getUserId()).isEqualTo(42L);
    assertThat(assessment.getSourceGuestId()).isEqualTo("guest-1");
    assertThat(assessment.getStatus()).isEqualTo("COMPLETED");
    assertThat(assessment.getCompletedAt()).isNotNull();

    ArgumentCaptor<AssessmentItem> itemCaptor = ArgumentCaptor.forClass(AssessmentItem.class);
    verify(items, org.mockito.Mockito.times(2)).save(itemCaptor.capture());
    assertThat(itemCaptor.getAllValues())
        .extracting(AssessmentItem::getAssessmentId, AssessmentItem::getOrderNum)
        .containsExactly(org.assertj.core.groups.Tuple.tuple(777L, 1),
            org.assertj.core.groups.Tuple.tuple(777L, 2));

    ArgumentCaptor<AssessmentResult> resultCaptor = ArgumentCaptor.forClass(AssessmentResult.class);
    verify(results).save(resultCaptor.capture());
    assertThat(resultCaptor.getValue().getAssessmentId()).isEqualTo(777L);
    assertThat(resultCaptor.getValue().getDiagnosedLevel()).isEqualTo("MID");
    assertThat(resultCaptor.getValue().getConfidenceWeight()).isEqualTo(0.5);
    verify(publisher).publishCompleted(777L, 42L, "BACKEND_SPRING", "MID", java.util.Map.of());
  }

  @Test
  void ownsANewTransactionSoPublisherFailureRollsBackAllDatabaseWrites() throws Exception {
    Transactional transactional = ClaimPersistenceService.class
        .getMethod("persist", long.class, String.class, GuestSession.class)
        .getAnnotation(Transactional.class);

    assertThat(transactional).isNotNull();
    assertThat(transactional.propagation()).isEqualTo(Propagation.REQUIRES_NEW);
  }

  @Test
  void computesLevelOnlyWhenGuestResultDidNotAlreadyFreezeIt() {
    Assessment saved = mock(Assessment.class);
    when(saved.getId()).thenReturn(777L);
    when(assessments.save(any(Assessment.class))).thenReturn(saved);
    when(engine.diagnoseLevel(List.of(0.4))).thenReturn("JUNIOR");
    GuestSession session = new GuestSession(
        "guest-1",
        "BACKEND_SPRING",
        0.6,
        null,
        List.of(new GuestSession.Presented(11L, 0.4, true, false, "\"A\"", 3)),
        true,
        null);

    service.persist(42L, "guest-1", session);

    verify(engine).diagnoseLevel(eq(List.of(0.4)));
    ArgumentCaptor<AssessmentResult> resultCaptor = ArgumentCaptor.forClass(AssessmentResult.class);
    verify(results).save(resultCaptor.capture());
    assertThat(resultCaptor.getValue().getDiagnosedLevel()).isEqualTo("JUNIOR");
  }
}
