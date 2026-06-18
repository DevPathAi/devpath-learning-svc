package ai.devpath.learning.assessment.guest;

import ai.devpath.learning.assessment.NextQuestionSelector;
import ai.devpath.learning.assessment.QuestionBank;
import ai.devpath.learning.assessment.QuestionBankRepository;
import ai.devpath.learning.assessment.dto.*;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GuestAssessmentService {

  private final GuestSessionStore store;
  private final QuestionBankRepository questions;
  private final AdaptiveEngine engine;
  private final NextQuestionSelector selector;

  public GuestAssessmentService(GuestSessionStore store, QuestionBankRepository questions,
      AdaptiveEngine engine, NextQuestionSelector selector) {
    this.store = store;
    this.questions = questions;
    this.engine = engine;
    this.selector = selector;
  }

  public String start(String track) {
    String guestId = UUID.randomUUID().toString();
    store.save(new GuestSession(guestId, track, AdaptiveEngine.START_DIFFICULTY, null,
        new ArrayList<>(), false, null));
    return guestId;
  }

  public Optional<NextQuestionResponse> next(String guestId) {
    GuestSession s = require(guestId);
    if (engine.isComplete(s.presented().size())) return Optional.empty();
    if (s.pendingQuestionId() != null) {
      QuestionBank q = questions.findById(s.pendingQuestionId()).orElseThrow();
      return Optional.of(view(q, s.presented().size() + 1));
    }
    Set<Long> excluded = s.presented().stream()
        .map(GuestSession.Presented::questionId).collect(Collectors.toSet());
    QuestionBank q = selector.select(s.track(), s.currentDifficulty(), excluded, questions.findByTrack(s.track()));
    if (q == null) return Optional.empty();
    store.save(new GuestSession(s.guestId(), s.track(), s.currentDifficulty(),
        q.getId(), s.presented(), false, null));
    return Optional.of(view(q, s.presented().size() + 1));
  }

  public void answer(String guestId, AnswerRequest req) {
    GuestSession s = require(guestId);
    if (s.pendingQuestionId() == null || s.pendingQuestionId() != req.questionId()) {
      throw new IllegalArgumentException("현재 출제된 문항이 아님(먼저 next 호출)");
    }
    QuestionBank q = questions.findById(req.questionId()).orElseThrow();
    AdaptiveEngine.AnswerOutcome outcome;
    Boolean correct;
    if (req.skipped()) {
      outcome = AdaptiveEngine.AnswerOutcome.SKIP;
      correct = null;
    } else {
      boolean ok = Objects.equals(normalize(req.answer()), normalize(q.getAnswerKey()));
      correct = ok;
      outcome = ok ? AdaptiveEngine.AnswerOutcome.CORRECT : AdaptiveEngine.AnswerOutcome.WRONG;
    }
    var presented = new ArrayList<>(s.presented());
    presented.add(new GuestSession.Presented(q.getId(), q.getDifficulty(), correct,
        req.skipped(), req.skipped() ? null : req.answer(), req.timeSpentSec()));
    double nextDiff = engine.nextDifficulty(s.currentDifficulty(), outcome);
    store.save(new GuestSession(s.guestId(), s.track(), nextDiff, null, presented, false, null));
  }

  public AssessmentResultView complete(String guestId) {
    GuestSession s = require(guestId);
    List<Double> correctDiffs = s.presented().stream()
        .filter(p -> Boolean.TRUE.equals(p.correct())).map(GuestSession.Presented::difficulty)
        .collect(Collectors.toList());
    String level = engine.diagnoseLevel(correctDiffs);
    long scored = s.presented().stream().filter(p -> !p.skipped()).count();
    double confidence = s.presented().isEmpty() ? 0.0 : (double) scored / s.presented().size();
    store.save(new GuestSession(s.guestId(), s.track(), s.currentDifficulty(), null,
        s.presented(), true, level));
    return new AssessmentResultView(level, null, null, null, confidence);
  }

  private NextQuestionResponse view(QuestionBank q, int index) {
    return new NextQuestionResponse(new QuestionView(q.getId(), q.getQuestionType(),
        q.getContent(), q.getOptions(), q.getBloomLevel(), q.getDifficulty()),
        index, AdaptiveEngine.TOTAL_QUESTIONS);
  }

  private GuestSession require(String guestId) {
    return store.find(guestId).orElseThrow(() -> new NoSuchElementException("guest 세션 없음/만료"));
  }

  private static String normalize(String json) {
    return json == null ? null : json.replaceAll("\\s+", "");
  }
}
