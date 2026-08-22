package ai.devpath.learning.assessment.claim;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentEventPublisher;
import ai.devpath.learning.assessment.AssessmentItem;
import ai.devpath.learning.assessment.AssessmentItemRepository;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.AssessmentResult;
import ai.devpath.learning.assessment.AssessmentResultRepository;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import ai.devpath.learning.assessment.guest.GuestSession;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimPersistenceService {

  private final AssessmentRepository assessments;
  private final AssessmentItemRepository items;
  private final AssessmentResultRepository results;
  private final AssessmentEventPublisher publisher;
  private final AdaptiveEngine engine;

  public ClaimPersistenceService(AssessmentRepository assessments, AssessmentItemRepository items,
      AssessmentResultRepository results, AssessmentEventPublisher publisher,
      AdaptiveEngine engine) {
    this.assessments = assessments;
    this.items = items;
    this.results = results;
    this.publisher = publisher;
    this.engine = engine;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public long persist(long userId, String guestId, GuestSession session) {
    Instant claimedAt = Instant.now();
    Assessment assessment = new Assessment();
    assessment.setUserId(userId);
    assessment.setSourceGuestId(guestId);
    assessment.setTrack(session.track());
    assessment.setStatus("COMPLETED");
    assessment.setCurrentDifficulty(session.currentDifficulty());
    assessment.setStartedAt(claimedAt);
    assessment.setCompletedAt(claimedAt);
    long assessmentId = assessments.save(assessment).getId();

    int order = 1;
    List<Double> correctDifficulties = new ArrayList<>();
    for (GuestSession.Presented presented : session.presented()) {
      AssessmentItem item = new AssessmentItem();
      item.setAssessmentId(assessmentId);
      item.setQuestionBankId(presented.questionId());
      item.setOrderNum(order++);
      item.setPresentedAt(claimedAt);
      item.setAnsweredAt(claimedAt);
      item.setSkipped(presented.skipped());
      item.setIsCorrect(presented.correct());
      item.setAnswer(presented.answer());
      item.setTimeSpentSec(presented.timeSpentSec());
      items.save(item);
      if (Boolean.TRUE.equals(presented.correct())) {
        correctDifficulties.add(presented.difficulty());
      }
    }

    String level = session.diagnosedLevel() != null
        ? session.diagnosedLevel()
        : engine.diagnoseLevel(correctDifficulties);
    AssessmentResult result = new AssessmentResult();
    result.setAssessmentId(assessmentId);
    result.setDiagnosedLevel(level);
    long scored = session.presented().stream().filter(presented -> !presented.skipped()).count();
    result.setConfidenceWeight(session.presented().isEmpty()
        ? 0.0
        : (double) scored / session.presented().size());
    results.save(result);

    publisher.publishCompleted(assessmentId, userId, session.track(), level, Map.of());
    return assessmentId;
  }
}
