package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import ai.devpath.learning.assessment.AssessmentEventPublisher;
import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class ClaimRollbackTest {

  @Autowired ClaimService service;
  @Autowired GuestSessionStore guestStore;
  @Autowired JdbcTemplate jdbc;
  @MockitoBean AssessmentEventPublisher publisher;

  @Test
  void publisherFailureRollsBackAssessmentItemsResultAndOutboxAndKeepsGuest() {
    long userId = jdbc.queryForObject(
        "insert into users default values returning id", Long.class);
    String guestId = UUID.randomUUID().toString();
    guestStore.save(completedSession(guestId));
    int assessmentsBefore = count("assessments");
    int itemsBefore = count("assessment_items");
    int resultsBefore = count("assessment_results");
    int outboxBefore = count("outbox");
    doThrow(new IllegalStateException("outbox failed"))
        .when(publisher).publishCompleted(anyLong(), anyLong(), anyString(), anyString(), anyMap());

    assertThatThrownBy(() -> service.claim(userId, guestId))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("outbox failed");

    assertThat(count("assessments")).isEqualTo(assessmentsBefore);
    assertThat(count("assessment_items")).isEqualTo(itemsBefore);
    assertThat(count("assessment_results")).isEqualTo(resultsBefore);
    assertThat(count("outbox")).isEqualTo(outboxBefore);
    assertThat(guestStore.find(guestId)).isPresent();
  }

  private int count(String table) {
    return jdbc.queryForObject("select count(*) from " + table, Integer.class);
  }

  private GuestSession completedSession(String guestId) {
    Long questionId = jdbc.queryForObject("""
        insert into question_bank(
          track, question_type, content, options, answer_key, bloom_level, difficulty, concept_tags)
        values ('BACKEND_SPRING', 'MCQ', ?, cast('[]' as jsonb), cast('{}' as jsonb),
          'REMEMBER', 0.3, cast('[]' as jsonb)) returning id
        """, Long.class, "claim-test-" + UUID.randomUUID());
    return new GuestSession(
        guestId,
        "BACKEND_SPRING",
        0.5,
        null,
        List.of(new GuestSession.Presented(questionId, 0.3, true, false, "\"A\"", 3)),
        true,
        "MID");
  }
}
