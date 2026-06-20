package ai.devpath.learning.assessment.claim;

import ai.devpath.learning.assessment.*;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimService {

  private static final String CLAIM_PREFIX = "assessment:claim:";

  private final GuestSessionStore guestStore;
  private final AssessmentRepository assessments;
  private final AssessmentItemRepository items;
  private final AssessmentResultRepository results;
  private final AssessmentEventPublisher publisher;
  private final AdaptiveEngine engine;
  private final StringRedisTemplate redis;

  public ClaimService(GuestSessionStore guestStore, AssessmentRepository assessments,
      AssessmentItemRepository items, AssessmentResultRepository results,
      AssessmentEventPublisher publisher, AdaptiveEngine engine, StringRedisTemplate redis) {
    this.guestStore = guestStore;
    this.assessments = assessments;
    this.items = items;
    this.results = results;
    this.publisher = publisher;
    this.engine = engine;
    this.redis = redis;
  }

  @Transactional
  public long claim(long userId, String guestId) {
    String done = redis.opsForValue().get(CLAIM_PREFIX + guestId);
    if (done != null) return Long.parseLong(done); // 이미 이행됨(멱등)
    Boolean acquired = redis.opsForValue()
        .setIfAbsent(CLAIM_PREFIX + "lock:" + guestId, "1", Duration.ofSeconds(10));
    if (Boolean.FALSE.equals(acquired)) {
      throw new IllegalStateException("claim 처리 중(동시 요청) — 재시도하라");
    }

    GuestSession s = guestStore.find(guestId)
        .orElseThrow(() -> new NoSuchElementException("guest 세션 없음/만료"));
    if (!s.completed()) throw new IllegalStateException("완료되지 않은 guest 진단");

    Assessment a = new Assessment();
    a.setUserId(userId);
    a.setTrack(s.track());
    a.setStatus("COMPLETED");
    a.setCurrentDifficulty(s.currentDifficulty());
    a.setStartedAt(Instant.now());
    a.setCompletedAt(Instant.now());
    long assessmentId = assessments.save(a).getId();

    int order = 1;
    List<Double> correctDiffs = new ArrayList<>();
    for (GuestSession.Presented p : s.presented()) {
      AssessmentItem item = new AssessmentItem();
      item.setAssessmentId(assessmentId);
      item.setQuestionBankId(p.questionId());
      item.setOrderNum(order++);
      item.setPresentedAt(Instant.now());
      item.setAnsweredAt(Instant.now());
      item.setSkipped(p.skipped());
      item.setIsCorrect(p.correct());
      item.setAnswer(p.answer());
      item.setTimeSpentSec(p.timeSpentSec());
      items.save(item);
      if (Boolean.TRUE.equals(p.correct())) correctDiffs.add(p.difficulty());
    }

    String level = s.diagnosedLevel() != null ? s.diagnosedLevel() : engine.diagnoseLevel(correctDiffs);
    AssessmentResult r = new AssessmentResult();
    r.setAssessmentId(assessmentId);
    r.setDiagnosedLevel(level);
    long scored = s.presented().stream().filter(p -> !p.skipped()).count();
    r.setConfidenceWeight(s.presented().isEmpty() ? 0.0 : (double) scored / s.presented().size());
    results.save(r);

    publisher.publishCompleted(assessmentId, userId, s.track(), level, Map.of());

    redis.opsForValue().set(CLAIM_PREFIX + guestId, String.valueOf(assessmentId));
    guestStore.delete(guestId);
    return assessmentId;
  }
}
