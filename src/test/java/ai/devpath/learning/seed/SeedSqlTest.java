package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.assessment.QuestionBankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/seed/question_bank_seed.sql")
class SeedSqlTest {

  @Autowired QuestionBankRepository questions;

  @Test
  void seedLoadsBackendQuestions() {
    assertThat(questions.findByTrack("BACKEND_SPRING").size()).isGreaterThanOrEqualTo(15);
  }
}
