package ai.devpath.learning.assessment;

import ai.devpath.learning.assessment.dto.*;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssessmentService {

  private final AssessmentRepository assessments;
  private final AssessmentItemRepository items;
  private final AssessmentResultRepository results;
  private final QuestionBankRepository questions;
  private final AdaptiveEngine engine;
  private final NextQuestionSelector selector;

  public AssessmentService(AssessmentRepository assessments, AssessmentItemRepository items,
      AssessmentResultRepository results, QuestionBankRepository questions,
      AdaptiveEngine engine, NextQuestionSelector selector) {
    this.assessments = assessments;
    this.items = items;
    this.results = results;
    this.questions = questions;
    this.engine = engine;
    this.selector = selector;
  }

  @Transactional
  public long start(long userId, String track) {
    Assessment a = new Assessment();
    a.setUserId(userId);
    a.setTrack(track);
    a.setStatus("IN_PROGRESS");
    a.setCurrentDifficulty(AdaptiveEngine.START_DIFFICULTY);
    a.setStartedAt(Instant.now());
    return assessments.save(a).getId();
  }

  @Transactional
  public Optional<NextQuestionResponse> next(long userId, long assessmentId) {
    Assessment a = ownedInProgress(userId, assessmentId);
    List<AssessmentItem> all = items.findByAssessmentIdOrderByOrderNumAsc(assessmentId);
    long answeredCount = all.stream().filter(i -> i.getAnsweredAt() != null).count();
    if (engine.isComplete((int) answeredCount)) return Optional.empty();
    Optional<AssessmentItem> outstanding = all.stream().filter(i -> i.getAnsweredAt() == null).findFirst();
    if (outstanding.isPresent()) {
      QuestionBank q = questions.findById(outstanding.get().getQuestionBankId()).orElseThrow();
      return Optional.of(toResponse(q, outstanding.get().getOrderNum()));
    }
    Set<Long> excluded = all.stream().map(AssessmentItem::getQuestionBankId).collect(Collectors.toSet());
    QuestionBank q = selector.select(a.getTrack(), a.getCurrentDifficulty(), excluded,
        questions.findByTrack(a.getTrack()));
    if (q == null) return Optional.empty();
    int order = all.size() + 1;
    AssessmentItem item = new AssessmentItem();
    item.setAssessmentId(assessmentId);
    item.setQuestionBankId(q.getId());
    item.setOrderNum(order);
    item.setPresentedAt(Instant.now());
    item.setSkipped(false);
    items.save(item);
    return Optional.of(toResponse(q, order));
  }

  private NextQuestionResponse toResponse(QuestionBank q, int orderNum) {
    var view = new QuestionView(q.getId(), q.getQuestionType(), q.getContent(), q.getOptions(),
        q.getBloomLevel(), q.getDifficulty());
    return new NextQuestionResponse(view, orderNum, AdaptiveEngine.TOTAL_QUESTIONS);
  }

  @Transactional
  public void answer(long userId, long assessmentId, AnswerRequest req) {
    Assessment a = ownedInProgress(userId, assessmentId);
    List<AssessmentItem> all = items.findByAssessmentIdOrderByOrderNumAsc(assessmentId);
    AssessmentItem item = all.stream().filter(i -> i.getAnsweredAt() == null).findFirst()
        .orElseThrow(() -> new IllegalStateException("응답할 outstanding 문항이 없음(먼저 next 호출)"));
    if (!item.getQuestionBankId().equals(req.questionId())) {
      throw new IllegalArgumentException("현재 출제된 문항이 아님");
    }
    QuestionBank q = questions.findById(req.questionId()).orElseThrow();
    item.setAnsweredAt(Instant.now());
    item.setTimeSpentSec(req.timeSpentSec());
    AdaptiveEngine.AnswerOutcome outcome;
    if (req.skipped()) {
      item.setSkipped(true);
      item.setAnswer(null);
      item.setIsCorrect(null);
      outcome = AdaptiveEngine.AnswerOutcome.SKIP;
    } else {
      item.setAnswer(req.answer());
      boolean correct = Objects.equals(normalize(req.answer()), normalize(q.getAnswerKey()));
      item.setIsCorrect(correct);
      outcome = correct ? AdaptiveEngine.AnswerOutcome.CORRECT : AdaptiveEngine.AnswerOutcome.WRONG;
    }
    items.save(item);
    a.setCurrentDifficulty(engine.nextDifficulty(a.getCurrentDifficulty(), outcome));
    assessments.save(a);
  }

  @Transactional
  public AssessmentResultView complete(long userId, long assessmentId) {
    Assessment a = ownedInProgress(userId, assessmentId);
    List<AssessmentItem> all = items.findByAssessmentIdOrderByOrderNumAsc(assessmentId);
    boolean hasOutstanding = all.stream().anyMatch(i -> i.getAnsweredAt() == null);
    long answered = all.stream().filter(i -> i.getAnsweredAt() != null).count();
    if (hasOutstanding || answered < AdaptiveEngine.TOTAL_QUESTIONS) {
      throw new IllegalStateException("15문항 응답 완료 후에만 complete 가능");
    }
    Map<Long, QuestionBank> byId = questions.findAllById(
        all.stream().map(AssessmentItem::getQuestionBankId).collect(Collectors.toSet()))
        .stream().collect(Collectors.toMap(QuestionBank::getId, x -> x));
    List<Double> correctDifficulties = all.stream()
        .filter(i -> Boolean.TRUE.equals(i.getIsCorrect()))
        .map(i -> byId.get(i.getQuestionBankId()).getDifficulty())
        .collect(Collectors.toList());
    String level = engine.diagnoseLevel(correctDifficulties);
    long scored = all.stream().filter(i -> !i.isSkipped()).count();
    double confidence = all.isEmpty() ? 0.0 : (double) scored / all.size();

    a.setStatus("COMPLETED");
    a.setCompletedAt(Instant.now());
    assessments.save(a);

    AssessmentResult r = new AssessmentResult();
    r.setAssessmentId(assessmentId);
    r.setDiagnosedLevel(level);
    r.setConfidenceWeight(confidence);
    results.save(r);
    return new AssessmentResultView(level, r.getConceptScores(), r.getStrengthConcepts(),
        r.getWeaknessConcepts(), confidence);
  }

  @Transactional(readOnly = true)
  public Optional<AssessmentResultView> result(long userId, long assessmentId) {
    owned(userId, assessmentId);
    return results.findById(assessmentId).map(r -> new AssessmentResultView(
        r.getDiagnosedLevel(), r.getConceptScores(), r.getStrengthConcepts(),
        r.getWeaknessConcepts(), r.getConfidenceWeight()));
  }

  private Assessment owned(long userId, long assessmentId) {
    Assessment a = assessments.findById(assessmentId)
        .orElseThrow(() -> new java.util.NoSuchElementException("assessment 없음"));
    if (a.getUserId() == null || a.getUserId() != userId) {
      throw new org.springframework.security.access.AccessDeniedException("소유자 아님");
    }
    return a;
  }

  private Assessment ownedInProgress(long userId, long assessmentId) {
    Assessment a = owned(userId, assessmentId);
    if (!"IN_PROGRESS".equals(a.getStatus())) {
      throw new IllegalStateException("진행 중 세션 아님");
    }
    return a;
  }

  private static String normalize(String json) {
    return json == null ? null : json.replaceAll("\\s+", "");
  }
}
