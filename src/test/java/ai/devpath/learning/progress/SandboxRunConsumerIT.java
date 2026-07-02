package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import ai.devpath.shared.event.SandboxRunSubmittedEvent;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {SandboxRunSubmittedEvent.EVENT_TYPE},
    bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class SandboxRunConsumerIT {

  @Autowired KafkaTemplate<String, String> kafka;
  @Autowired JsonMapper jsonMapper;
  @Autowired SandboxActivityLogRepository sandboxActivity;

  @Test
  void consumingEventRecordsActivityForToday() throws Exception {
    long userId = 777301L;
    var event = new SandboxRunSubmittedEvent(UUID.randomUUID(), Instant.now(), userId, 1L, "java21", null);
    kafka.send(SandboxRunSubmittedEvent.EVENT_TYPE, String.valueOf(userId), jsonMapper.writeValueAsString(event));

    await().atMost(Duration.ofSeconds(20)).untilAsserted(() ->
        assertThat(sandboxActivity.hasActivityOnDate(userId, LocalDate.now())).isTrue());
  }
}
