package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@ActiveProfiles("test")
class ClaimCleanupFailureTest {

  @Autowired ClaimService service;
  @MockitoSpyBean GuestSessionStore guestStore;
  @Autowired JdbcTemplate jdbc;

  @Test
  void cleanupFailureAfterCommitReturnsSuccessAndNextReplayRetriesCleanup() {
    long userId = jdbc.queryForObject(
        "insert into users default values returning id", Long.class);
    String guestId = UUID.randomUUID().toString();
    guestStore.save(completedSession(guestId));
    doThrow(new IllegalStateException("redis unavailable"))
        .when(guestStore).delete(guestId);

    long first = service.claim(userId, guestId);

    assertThat(jdbc.queryForObject(
        "select count(*) from assessments where source_guest_id = ?", Integer.class, guestId))
        .isOne();
    assertThat(guestStore.find(guestId)).isPresent();

    reset(guestStore);
    long replay = service.claim(userId, guestId);

    assertThat(replay).isEqualTo(first);
    assertThat(guestStore.find(guestId)).isEmpty();
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
