package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.assessment.QuestionBank;
import ai.devpath.learning.assessment.QuestionBankRepository;
import ai.devpath.learning.contentgen.question.QuestionQuota;
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

  // QuestionQuota.PER_TRACK 의 실제 값과 같다(100). 그 상수는 package-private 이라
  // contentgen.question 밖인 이 seed 패키지에서 참조할 수 없어 이름 있는 값으로 복제한다.
  private static final int PER_TRACK = 100;

  @Test
  void seedLoadsAllTracksWithQuotas() throws Exception {
    var rows = questions.findAll();

    assertThat(rows).hasSize(QuestionQuota.TRACKS.size() * PER_TRACK);
    assertThat(rows).extracting(QuestionBank::getBloomLevel).doesNotContain("CREATE");

    var byTrack = rows.stream().collect(Collectors.groupingBy(QuestionBank::getTrack));
    assertThat(byTrack.keySet()).containsExactlyInAnyOrderElementsOf(QuestionQuota.TRACKS);
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
