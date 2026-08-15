package ai.devpath.learning.assessment.claim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
class ClaimLegacyBridgeTest {

  private static final Instant BRIDGE_UNTIL = Instant.now().plusSeconds(600);

  @DynamicPropertySource
  static void bridgeWindow(DynamicPropertyRegistry registry) {
    registry.add("devpath.claim.legacy-bridge-until", BRIDGE_UNTIL::toString);
  }

  @Autowired ClaimService service;
  @Autowired JdbcTemplate jdbc;
  @Autowired StringRedisTemplate redis;

  @Test
  void oldWriterMarkerBindsItsOwnerToTheDatabaseThenDeletesTheMarker() {
    long ownerId = seedUser();
    String guestId = UUID.randomUUID().toString();
    long assessmentId = seedLegacyAssessment(ownerId);
    String markerKey = "assessment:claim:" + guestId;
    redis.opsForValue().set(markerKey, Long.toString(assessmentId));
    assertThat(redis.getExpire(markerKey, TimeUnit.SECONDS)).isEqualTo(-1L);

    assertThat(service.claim(ownerId, guestId)).isEqualTo(assessmentId);

    assertThat(jdbc.queryForObject(
        "select source_guest_id from assessments where id = ?", String.class, assessmentId))
        .isEqualTo(guestId);
    assertThat(redis.hasKey(markerKey)).isFalse();
  }

  @Test
  void oldWriterMarkerNeverAuthorizesAnotherOwnerAndBecomesTimeBoundAtomically() {
    long ownerId = seedUser();
    long attackerId = seedUser();
    String guestId = UUID.randomUUID().toString();
    long assessmentId = seedLegacyAssessment(ownerId);
    String markerKey = "assessment:claim:" + guestId;
    redis.opsForValue().set(markerKey, Long.toString(assessmentId));

    assertThatThrownBy(() -> service.claim(attackerId, guestId))
        .isInstanceOf(AccessDeniedException.class)
        .hasMessageNotContaining(Long.toString(assessmentId));

    assertThat(jdbc.queryForObject(
        "select source_guest_id from assessments where id = ?", String.class, assessmentId))
        .isNull();
    Long remaining = redis.getExpire(markerKey, TimeUnit.SECONDS);
    assertThat(remaining).isPositive().isLessThanOrEqualTo(600L);
    redis.delete(markerKey);
  }

  private long seedUser() {
    return jdbc.queryForObject("insert into users default values returning id", Long.class);
  }

  private long seedLegacyAssessment(long userId) {
    return jdbc.queryForObject("""
        insert into assessments(
          user_id, source_guest_id, track, status, current_difficulty, started_at, completed_at)
        values (?, null, 'BACKEND_SPRING', 'COMPLETED', 0.5, now(), now()) returning id
        """, Long.class, userId);
  }
}
