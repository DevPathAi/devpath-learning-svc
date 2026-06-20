package ai.devpath.learning.assessment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.test.database.replace=none")
@ActiveProfiles("test")
class AssessmentJpaTest {

  @Autowired QuestionBankRepository questions;
  @Autowired AssessmentRepository assessments;

  @Test
  void persistsQuestionAndAssessment() {
    QuestionBank q = new QuestionBank();
    q.setTrack("BACKEND_SPRING");
    q.setQuestionType("MCQ");
    q.setContent("What is a bean?");
    q.setOptions("[\"a\",\"b\"]");
    q.setAnswerKey("{\"correct\":0}");
    q.setBloomLevel("UNDERSTAND");
    q.setDifficulty(0.3);
    q.setConceptTags("[\"spring-core\"]");
    QuestionBank saved = questions.save(q);
    assertThat(saved.getId()).isNotNull();

    Assessment a = new Assessment();
    a.setUserId(null);
    a.setTrack("BACKEND_SPRING");
    a.setStatus("IN_PROGRESS");
    a.setCurrentDifficulty(0.3);
    a.setStartedAt(Instant.now());
    Assessment savedA = assessments.save(a);
    assertThat(savedA.getId()).isNotNull();
  }
}
