package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionReviewPromptTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

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

  @Test
  void mergeReportsOfEmptyListIsAValidEmptyJsonArray() throws Exception {
    var merged = QuestionReviewPrompt.mergeReports(List.of());

    JsonNode array = MAPPER.readTree(merged);
    assertThat(array.isArray()).isTrue();
    assertThat(array).hasSize(0);
  }

  @Test
  void mergeReportsOfOneBatchHasNoTrailingComma() throws Exception {
    var merged = QuestionReviewPrompt.mergeReports(List.of("{\"raw\":true}"));

    JsonNode array = MAPPER.readTree(merged);
    assertThat(array.isArray()).isTrue();
    assertThat(array).hasSize(1);
    assertThat(array.get(0).get("batch").asInt()).isEqualTo(0);
    assertThat(array.get(0).get("raw").asText()).isEqualTo("{\"raw\":true}");
  }

  @Test
  void mergeReportsOfThreeBatchesPreservesOrderAndIndices() throws Exception {
    var merged = QuestionReviewPrompt.mergeReports(List.of("first", "second", "third"));

    JsonNode array = MAPPER.readTree(merged);
    assertThat(array.isArray()).isTrue();
    assertThat(array).hasSize(3);
    for (int i = 0; i < 3; i++) {
      assertThat(array.get(i).get("batch").asInt()).isEqualTo(i);
    }
    assertThat(array.get(0).get("raw").asText()).isEqualTo("first");
    assertThat(array.get(1).get("raw").asText()).isEqualTo("second");
    assertThat(array.get(2).get("raw").asText()).isEqualTo("third");
  }
}
