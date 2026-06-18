package ai.devpath.learning.outbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class OutboxRelayFailureTest {

  @Autowired OutboxRepository outbox;
  @Autowired OutboxRelay relay;
  @MockitoBean KafkaTemplate<String, String> kafka;

  @Test
  void sendFailureKeepsUnpublished() {
    when(kafka.send(any(), any(), any())).thenThrow(new RuntimeException("broker down"));
    OutboxEntry e = new OutboxEntry();
    e.setAggregateType("assessment");
    e.setAggregateId("888");
    e.setEventType("learning.assessment.completed");
    e.setPayload("{\"assessmentId\":888}");
    e.setCreatedAt(Instant.now());
    Long id = outbox.save(e).getId();

    int published = relay.relayOnce();
    assertThat(published).isEqualTo(0);
    assertThat(outbox.findById(id).orElseThrow().getPublishedAt()).isNull();
  }
}
