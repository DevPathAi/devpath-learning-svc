package ai.devpath.learning.assessment;

import ai.devpath.learning.outbox.OutboxEntry;
import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.shared.event.AssessmentCompletedEvent;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AssessmentEventPublisher {

  private final OutboxRepository outbox;
  private final JsonMapper jsonMapper;

  public AssessmentEventPublisher(OutboxRepository outbox, JsonMapper jsonMapper) {
    this.outbox = outbox;
    this.jsonMapper = jsonMapper;
  }

  public void publishCompleted(long assessmentId, long userId, String track,
      String diagnosedLevel, Map<String, Double> conceptScores) {
    var event = new AssessmentCompletedEvent(UUID.randomUUID(), Instant.now(),
        assessmentId, userId, track, diagnosedLevel, conceptScores, Instant.now());
    OutboxEntry entry = new OutboxEntry();
    entry.setAggregateType("assessment");
    entry.setAggregateId(String.valueOf(assessmentId));
    entry.setEventType(AssessmentCompletedEvent.EVENT_TYPE);
    entry.setPayload(serialize(event));
    entry.setCreatedAt(Instant.now());
    outbox.save(entry);
  }

  private String serialize(AssessmentCompletedEvent event) {
    try {
      return jsonMapper.writeValueAsString(event);
    } catch (Exception e) {
      throw new IllegalStateException("AssessmentCompletedEvent 직렬화 실패", e);
    }
  }
}
