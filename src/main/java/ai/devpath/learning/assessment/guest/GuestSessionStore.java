package ai.devpath.learning.assessment.guest;

import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class GuestSessionStore {

  private static final String PREFIX = "assessment:guest:";
  private static final Duration TTL = Duration.ofMinutes(30);

  private final StringRedisTemplate redis;
  private final JsonMapper jsonMapper;

  public GuestSessionStore(StringRedisTemplate redis, JsonMapper jsonMapper) {
    this.redis = redis;
    this.jsonMapper = jsonMapper;
  }

  public void save(GuestSession session) {
    try {
      redis.opsForValue().set(PREFIX + session.guestId(), jsonMapper.writeValueAsString(session), TTL);
    } catch (Exception e) {
      throw new IllegalStateException("guest 세션 직렬화 실패", e);
    }
  }

  public Optional<GuestSession> find(String guestId) {
    String json = redis.opsForValue().get(PREFIX + guestId);
    if (json == null) return Optional.empty();
    try {
      return Optional.of(jsonMapper.readValue(json, GuestSession.class));
    } catch (Exception e) {
      throw new IllegalStateException("guest 세션 역직렬화 실패", e);
    }
  }

  public void delete(String guestId) {
    redis.delete(PREFIX + guestId);
  }
}
