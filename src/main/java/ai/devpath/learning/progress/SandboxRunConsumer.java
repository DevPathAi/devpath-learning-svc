package ai.devpath.learning.progress;

import ai.devpath.shared.event.SandboxRunSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class SandboxRunConsumer {

  private static final Logger log = LoggerFactory.getLogger(SandboxRunConsumer.class);

  private final SandboxActivityLogRepository sandboxActivity;
  private final JsonMapper jsonMapper;

  public SandboxRunConsumer(SandboxActivityLogRepository sandboxActivity, JsonMapper jsonMapper) {
    this.sandboxActivity = sandboxActivity;
    this.jsonMapper = jsonMapper;
  }

  @KafkaListener(topics = SandboxRunSubmittedEvent.EVENT_TYPE, groupId = "devpath-learning")
  public void onSandboxRunSubmitted(String payload) {
    SandboxRunSubmittedEvent event;
    try {
      event = jsonMapper.readValue(payload, SandboxRunSubmittedEvent.class);
    } catch (Exception e) {
      log.warn("SandboxRunSubmittedEvent 역직렬화 실패 — skip: {}", payload, e);
      return;
    }
    sandboxActivity.record(event.userId(), event.occurredAt());
  }
}
