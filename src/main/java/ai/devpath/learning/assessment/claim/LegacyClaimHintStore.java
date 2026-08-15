package ai.devpath.learning.assessment.claim;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.OptionalLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Time-boxed bridge for markers written by the pre-database-authoritative claim service.
 *
 * <p>The marker is only a hint. {@link ClaimService} always binds and verifies ownership in
 * PostgreSQL before returning an assessment. Keep the bridge disabled by default and remove it
 * after the compatibility deployment window.
 */
@Component
public class LegacyClaimHintStore {

  private static final Logger log = LoggerFactory.getLogger(LegacyClaimHintStore.class);
  private static final String PREFIX = "assessment:claim:";
  private static final Duration MAX_WINDOW = Duration.ofMinutes(30);

  private final StringRedisTemplate redis;
  private final Clock clock;
  private final Instant bridgeUntil;

  @Autowired
  public LegacyClaimHintStore(StringRedisTemplate redis,
      @Value("${devpath.claim.legacy-bridge-until:}") String configuredUntil) {
    this(redis, Clock.systemUTC(), parseUntil(configuredUntil));
  }

  LegacyClaimHintStore(StringRedisTemplate redis, Clock clock, Instant configuredUntil) {
    this.redis = redis;
    this.clock = clock;
    Instant now = clock.instant();
    if (configuredUntil != null && configuredUntil.isAfter(now.plus(MAX_WINDOW))) {
      throw new IllegalArgumentException("legacy claim bridge는 시작 시점부터 30분을 넘길 수 없음");
    }
    this.bridgeUntil = configuredUntil;
  }

  public OptionalLong take(String guestId) {
    Instant now = clock.instant();
    if (bridgeUntil == null || !now.isBefore(bridgeUntil)) {
      return OptionalLong.empty();
    }
    String key = PREFIX + guestId;
    String value;
    try {
      value = redis.opsForValue().getAndExpire(key, Duration.between(now, bridgeUntil));
    } catch (RuntimeException redisFailure) {
      log.warn("legacy claim hint read failed; cause={}",
          redisFailure.getClass().getSimpleName());
      return OptionalLong.empty();
    }
    if (value == null) return OptionalLong.empty();
    try {
      long assessmentId = Long.parseLong(value);
      if (assessmentId > 0) return OptionalLong.of(assessmentId);
    } catch (NumberFormatException ignored) {
      // Invalid pre-migration data is deleted below and is never an authorization source.
    }
    try {
      redis.delete(key);
    } catch (RuntimeException cleanupFailure) {
      log.warn("invalid legacy claim hint cleanup failed; cause={}",
          cleanupFailure.getClass().getSimpleName());
    }
    return OptionalLong.empty();
  }

  public void delete(String guestId) {
    redis.delete(PREFIX + guestId);
  }

  private static Instant parseUntil(String configuredUntil) {
    if (configuredUntil == null || configuredUntil.isBlank()) return null;
    try {
      return Instant.parse(configuredUntil);
    } catch (DateTimeParseException invalid) {
      throw new IllegalArgumentException(
          "devpath.claim.legacy-bridge-until은 ISO-8601 instant여야 함", invalid);
    }
  }
}
