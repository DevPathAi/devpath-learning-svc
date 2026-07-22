package ai.devpath.learning.assessment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.dto.AnswerRequest;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.json.JsonMapper;

class AssessmentServiceTest {

  private final JsonMapper jsonMapper = JsonMapper.builder().build();

  private final AssessmentRepository assessments = mock(AssessmentRepository.class);
  private final AssessmentItemRepository items = mock(AssessmentItemRepository.class);
  private final AssessmentResultRepository results = mock(AssessmentResultRepository.class);
  private final QuestionBankRepository questions = mock(QuestionBankRepository.class);
  private final AdaptiveEngine engine = mock(AdaptiveEngine.class);
  private final NextQuestionSelector selector = mock(NextQuestionSelector.class);
  private final AssessmentEventPublisher publisher = mock(AssessmentEventPublisher.class);

  private final AssessmentService service = new AssessmentService(
      assessments, items, results, questions, engine, selector, publisher, jsonMapper);

  private Assessment inProgress(long id, long userId) {
    Assessment a = new Assessment();
    a.setUserId(userId);
    a.setTrack("BACKEND_SPRING");
    a.setStatus("IN_PROGRESS");
    a.setCurrentDifficulty(0.3);
    return a;
  }

  private AssessmentItem outstandingItem(long questionBankId) {
    AssessmentItem item = new AssessmentItem();
    item.setAssessmentId(1L);
    item.setQuestionBankId(questionBankId);
    item.setOrderNum(1);
    return item;
  }

  private QuestionBank question(long id, String answerKey) {
    QuestionBank q = new QuestionBank();
    q.setTrack("BACKEND_SPRING");
    q.setQuestionType("MCQ");
    q.setContent("content");
    q.setBloomLevel("APPLY");
    q.setDifficulty(0.5);
    q.setAnswerKey(answerKey);
    return q;
  }

  @Test
  void answerRejectsWrongQuestionId() {
    when(assessments.findById(1L)).thenReturn(Optional.of(inProgress(1L, 42L)));
    when(items.findByAssessmentIdOrderByOrderNumAsc(1L))
        .thenReturn(List.of(outstandingItem(100L)));

    var req = new AnswerRequest(999L, "A", false, 5);
    assertThatThrownBy(() -> service.answer(42L, 1L, req))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("현재 출제된 문항이 아님");
  }

  @Test
  void completeRejectsWhenNotAllQuestionsAnswered() {
    when(assessments.findById(1L)).thenReturn(Optional.of(inProgress(1L, 42L)));
    // 0 answered items < AdaptiveEngine.TOTAL_QUESTIONS (15)
    when(items.findByAssessmentIdOrderByOrderNumAsc(1L)).thenReturn(List.of());

    assertThatThrownBy(() -> service.complete(42L, 1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("15문항");
  }

  @Test
  void resultRejectsNonOwner() {
    when(assessments.findById(1L)).thenReturn(Optional.of(inProgress(1L, 42L)));

    assertThatThrownBy(() -> service.result(999L, 1L))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void nextRejectsNonOwner() {
    when(assessments.findById(1L)).thenReturn(Optional.of(inProgress(1L, 42L)));

    assertThatThrownBy(() -> service.next(999L, 1L))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void answerWithCorrectAnswerAdvancesDifficultyWithCorrectOutcome() {
    when(assessments.findById(1L)).thenReturn(Optional.of(inProgress(1L, 42L)));
    when(items.findByAssessmentIdOrderByOrderNumAsc(1L))
        .thenReturn(List.of(outstandingItem(100L)));
    when(questions.findById(100L)).thenReturn(Optional.of(question(100L, "\"A\"")));
    when(engine.nextDifficulty(anyDouble(), eq(AdaptiveEngine.AnswerOutcome.CORRECT)))
        .thenReturn(0.4);

    var req = new AnswerRequest(100L, "\"A\"", false, 5);
    service.answer(42L, 1L, req);

    verify(engine, times(1))
        .nextDifficulty(anyDouble(), eq(AdaptiveEngine.AnswerOutcome.CORRECT));
  }
}
