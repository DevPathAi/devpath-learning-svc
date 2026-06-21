package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.assessment.QuestionBank;
import ai.devpath.learning.assessment.QuestionBankRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "TRUNCATE question_bank RESTART IDENTITY CASCADE")
@Sql("/seed/question_bank_md2_seed.sql")
class SeedSqlTest {

  @Autowired QuestionBankRepository questions;

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void seedLoadsFiveHundredMd2QuestionsWithTrackQuotas() throws Exception {
    var rows = questions.findAll();

    assertThat(rows).hasSize(500);
    assertThat(rows).extracting(QuestionBank::getBloomLevel).doesNotContain("CREATE");

    var byTrack = rows.stream().collect(Collectors.groupingBy(QuestionBank::getTrack));
    assertThat(byTrack.keySet()).containsExactlyInAnyOrder(
        "BACKEND_SPRING", "FRONTEND_REACT", "MOBILE_FLUTTER", "DEVOPS", "FULLSTACK");
    for (var entry : byTrack.entrySet()) {
      assertThat(entry.getValue()).hasSize(100);
      var byType = entry.getValue().stream()
          .collect(Collectors.groupingBy(QuestionBank::getQuestionType, Collectors.counting()));
      assertThat(byType).containsEntry("MCQ", 70L).containsEntry("CODE_READING", 30L);
      assertThat(byType).doesNotContainKey("SHORT_ANSWER");
    }

    for (var row : rows) {
      var options = mapper.readTree(row.getOptions());
      var answerKey = mapper.readTree(row.getAnswerKey());
      assertThat(answerKey.get("correct").asInt())
          .isBetween(0, options.size() - 1);
      assertThat(mapper.readValue(row.getConceptTags(), String[].class))
          .isNotEmpty();
    }
  }

  @Test
  void md2SeedResourceExistsForDevSeeder() {
    assertThat(getClass().getResource("/db/seed/question_bank_md2_seed.sql")).isNotNull();
  }
}
