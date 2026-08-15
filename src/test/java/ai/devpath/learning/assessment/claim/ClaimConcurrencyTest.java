package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.devpath.learning.assessment.guest.GuestSession;
import ai.devpath.learning.assessment.guest.GuestSessionStore;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClaimConcurrencyTest {

  @Autowired ClaimService service;
  @Autowired GuestSessionStore guestStore;
  @Autowired StringRedisTemplate redis;
  @Autowired JdbcTemplate jdbc;
  @Autowired MockMvc mvc;

  @Test
  void firstClaimAndResponseLossReplayKeepExactlyOneCommittedAggregate() {
    long userId = seedUser();
    String guestId = UUID.randomUUID().toString();
    guestStore.save(completedSession(guestId));

    long first = service.claim(userId, guestId);
    long replay = service.claim(userId, guestId);

    assertThat(replay).isEqualTo(first);
    assertAggregateCounts(guestId, first, 1, 2, 1, 1);
    assertThat(guestStore.find(guestId)).isEmpty();
  }

  @Test
  void concurrentSameOwnerClaimsReturnOneAssessmentIdAndOneAggregate() throws Exception {
    long userId = seedUser();
    String guestId = UUID.randomUUID().toString();
    guestStore.save(completedSession(guestId));
    var pool = Executors.newFixedThreadPool(2);
    var start = new CountDownLatch(1);
    Callable<Long> claim = () -> {
      start.await();
      return service.claim(userId, guestId);
    };

    var first = pool.submit(claim);
    var second = pool.submit(claim);
    start.countDown();
    long firstId = first.get(20, TimeUnit.SECONDS);
    long secondId = second.get(20, TimeUnit.SECONDS);
    pool.shutdown();

    assertThat(secondId).isEqualTo(firstId);
    assertAggregateCounts(guestId, firstId, 1, 2, 1, 1);
  }

  @Test
  void differentOwnerReplayIsForbiddenAndHttpBodyDoesNotExposeAssessmentId() throws Exception {
    long ownerId = seedUser();
    long attackerId = seedUser();
    String guestId = UUID.randomUUID().toString();
    guestStore.save(completedSession(guestId));
    long assessmentId = service.claim(ownerId, guestId);

    assertThatThrownBy(() -> service.claim(attackerId, guestId))
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageNotContaining(Long.toString(assessmentId));

    String body = mvc.perform(post("/onboarding/assessments/claim")
            .with(jwt().jwt(token -> token.subject(Long.toString(attackerId))))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"guest_assessment_id\":\"" + guestId + "\"}"))
        .andExpect(status().isForbidden())
        .andReturn().getResponse().getContentAsString();
    assertThat(body)
        .contains("\"code\":\"FORBIDDEN\"")
        .doesNotContain("assessmentId");
    assertAggregateCounts(guestId, assessmentId, 1, 2, 1, 1);
  }

  @Test
  void expiredGuestReplaysCommittedDatabaseRowButMissingClaimStaysNotFound() {
    long userId = seedUser();
    String committedGuestId = UUID.randomUUID().toString();
    Long assessmentId = jdbc.queryForObject("""
        insert into assessments(
          user_id, source_guest_id, track, status, current_difficulty, started_at, completed_at)
        values (?, ?, 'BACKEND_SPRING', 'COMPLETED', 0.5, now(), now()) returning id
        """, Long.class, userId, committedGuestId);

    assertThat(service.claim(userId, committedGuestId)).isEqualTo(assessmentId);

    String missingGuestId = UUID.randomUUID().toString();
    assertThatThrownBy(() -> service.claim(userId, missingGuestId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("없음/만료");
  }

  @Test
  void legacyRedisClaimMarkerIsNeverAnAuthorizationSource() {
    long attackerId = seedUser();
    String guestId = UUID.randomUUID().toString();
    redis.opsForValue().set("assessment:claim:" + guestId, "777");

    assertThatThrownBy(() -> service.claim(attackerId, guestId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageNotContaining("777");
    assertThat(jdbc.queryForObject(
        "select count(*) from assessments where source_guest_id = ?", Integer.class, guestId))
        .isZero();
    redis.delete("assessment:claim:" + guestId);
  }

  @Test
  void nullAndMalformedClaimIdsReturnBadRequestInsteadOfQueryingNullableLegacyRows()
      throws Exception {
    long userId = seedUser();
    var auth = jwt().jwt(token -> token.subject(Long.toString(userId)));

    mvc.perform(post("/onboarding/assessments/claim")
            .with(auth)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"guest_assessment_id\":null}"))
        .andExpect(status().isBadRequest());
    mvc.perform(post("/onboarding/assessments/claim")
            .with(auth)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"guest_assessment_id\":\"not-a-uuid\"}"))
        .andExpect(status().isBadRequest());
  }

  private void assertAggregateCounts(String guestId, long assessmentId, int assessments,
      int items, int results, int outbox) {
    assertThat(jdbc.queryForObject(
        "select count(*) from assessments where source_guest_id = ?", Integer.class, guestId))
        .isEqualTo(assessments);
    assertThat(jdbc.queryForObject(
        "select count(*) from assessment_items where assessment_id = ?",
        Integer.class, assessmentId)).isEqualTo(items);
    assertThat(jdbc.queryForObject(
        "select count(*) from assessment_results where assessment_id = ?",
        Integer.class, assessmentId)).isEqualTo(results);
    assertThat(jdbc.queryForObject(
        "select count(*) from outbox where aggregate_type = 'assessment' and aggregate_id = ?",
        Integer.class, Long.toString(assessmentId))).isEqualTo(outbox);
  }

  private long seedUser() {
    return jdbc.queryForObject("insert into users default values returning id", Long.class);
  }

  private GuestSession completedSession(String guestId) {
    long firstQuestionId = seedQuestion(0.3);
    long secondQuestionId = seedQuestion(0.7);
    return new GuestSession(
        guestId,
        "BACKEND_SPRING",
        0.5,
        null,
        List.of(
            new GuestSession.Presented(firstQuestionId, 0.3, true, false, "\"A\"", 3),
            new GuestSession.Presented(secondQuestionId, 0.7, null, true, null, 2)),
        true,
        "MID");
  }

  private long seedQuestion(double difficulty) {
    return jdbc.queryForObject("""
        insert into question_bank(
          track, question_type, content, options, answer_key, bloom_level, difficulty, concept_tags)
        values ('BACKEND_SPRING', 'MCQ', ?, cast('[]' as jsonb), cast('{}' as jsonb),
          'REMEMBER', ?, cast('[]' as jsonb)) returning id
        """, Long.class, "claim-test-" + UUID.randomUUID(), difficulty);
  }
}
