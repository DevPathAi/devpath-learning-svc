package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionReviewPromptTest {

  private static ApprovedQuestion question(String content) {
    return new ApprovedQuestion("PYTHON_BACKEND", "MCQ", content,
        List.of("보기 하나", "보기 둘", "보기 셋", "보기 넷"),
        new ApprovedQuestion.AnswerKey(0), "APPLY", 0.5, List.of("python-async"), "해설");
  }

  @Test
  void promptContainsTrackAndEveryQuestionWithIndex() {
    var prompt = QuestionReviewPrompt.build("PYTHON_BACKEND",
        List.of(question("첫 문항입니다"), question("둘째 문항입니다")));

    assertThat(prompt).contains("PYTHON_BACKEND");
    assertThat(prompt).contains("첫 문항입니다").contains("둘째 문항입니다");
    assertThat(prompt).contains("\"index\": 0").contains("\"index\": 1");
  }

  @Test
  void promptAsksForTheFourReviewAxes() {
    var prompt = QuestionReviewPrompt.build("PYTHON_BACKEND", List.of(question("문항")));

    assertThat(prompt).contains("사실오류").contains("정답키").contains("정답을 흘리는")
        .contains("한국어");
  }

  @Test
  void batchesSplitByCharacterBudget() {
    var big = question("가".repeat(30_000));
    var batches = QuestionReviewPrompt.batch(List.of(big, big, big), 40_000);

    assertThat(batches).hasSize(3);
    assertThat(batches.get(0)).hasSize(1);
  }
}
