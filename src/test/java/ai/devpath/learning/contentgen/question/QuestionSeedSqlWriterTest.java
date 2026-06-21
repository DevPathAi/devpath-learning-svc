package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionSeedSqlWriterTest {

  @Test
  void sqlOutputIsDeterministicForSameQuestions() {
    var writer = new QuestionSeedSqlWriter();
    var first = question("FRONTEND_REACT", "MCQ", 0.4, "B content", 1);
    var second = question("BACKEND_SPRING", "CODE_READING", 0.7, "A content", 0);

    var sqlA = writer.toSql(List.of(first, second));
    var sqlB = writer.toSql(List.of(second, first));

    assertThat(sqlA).isEqualTo(sqlB);
    assertThat(sqlA).contains("INSERT INTO question_bank");
    assertThat(sqlA.indexOf("BACKEND_SPRING")).isLessThan(sqlA.indexOf("FRONTEND_REACT"));
    assertThat(sqlA).contains("'[\"A\",\"B\",\"C\",\"D\"]'");
    assertThat(sqlA).contains("'{\"correct\":0}'");
  }

  private static ApprovedQuestion question(
      String track, String type, double difficulty, String content, int correct) {
    return new ApprovedQuestion(
        track,
        type,
        content,
        List.of("A", "B", "C", "D"),
        new ApprovedQuestion.AnswerKey(correct),
        "APPLY",
        difficulty,
        List.of("deterministic-sql"),
        "Because the option matches the concept.");
  }
}
