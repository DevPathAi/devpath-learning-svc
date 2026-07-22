package ai.devpath.learning.assessment.guest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.NextQuestionSelector;
import ai.devpath.learning.assessment.QuestionBank;
import ai.devpath.learning.assessment.QuestionBankRepository;
import ai.devpath.learning.assessment.dto.AnswerRequest;
import ai.devpath.learning.assessment.dto.AssessmentResultView;
import ai.devpath.learning.assessment.engine.AdaptiveEngine;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GuestAssessmentServiceTest {

  private final GuestSessionStore store = mock(GuestSessionStore.class);
  private final QuestionBankRepository questions = mock(QuestionBankRepository.class);
  private final AdaptiveEngine engine = mock(AdaptiveEngine.class);
  private final NextQuestionSelector selector = mock(NextQuestionSelector.class);

  private final GuestAssessmentService service =
      new GuestAssessmentService(store, questions, engine, selector);

  private GuestSession session(String guestId, Long pendingQuestionId,
      List<GuestSession.Presented> presented) {
    return new GuestSession(guestId, "BACKEND_SPRING", 0.5, pendingQuestionId,
        presented, false, null);
  }

  private QuestionBank question(String answerKey) {
    QuestionBank q = new QuestionBank();
    q.setTrack("BACKEND_SPRING");
    q.setDifficulty(0.5);
    q.setAnswerKey(answerKey);
    return q;
  }

  @Test
  void answerRejectsWrongQuestionId() {
    when(store.find("g1")).thenReturn(Optional.of(session("g1", 100L, List.of())));

    var req = new AnswerRequest(999L, "A", false, 5);
    assertThatThrownBy(() -> service.answer("g1", req))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("현재 출제된 문항이 아님");
  }

  @Test
  void completeDiagnosesLevelAndConfidence() {
    var presented = List.of(
        new GuestSession.Presented(1L, 0.6, true, false, "\"A\"", 5),
        new GuestSession.Presented(2L, 0.5, false, false, "\"B\"", 8));
    when(store.find("g1")).thenReturn(Optional.of(session("g1", null, presented)));
    when(engine.diagnoseLevel(anyList())).thenReturn("SENIOR");

    AssessmentResultView view = service.complete("g1");

    assertThat(view.diagnosedLevel()).isEqualTo("SENIOR");
    // 2 non-skip / 2 presented = 1.0 confidence
    assertThat(view.confidenceWeight()).isEqualTo(1.0);
  }

  @Test
  void requireThrowsWhenGuestSessionMissing() {
    when(store.find("missing")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.complete("missing"))
        .isInstanceOf(NoSuchElementException.class);
  }
}
