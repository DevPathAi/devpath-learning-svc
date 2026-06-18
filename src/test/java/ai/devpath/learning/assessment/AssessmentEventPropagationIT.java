package ai.devpath.learning.assessment;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ai.devpath.learning.outbox.OutboxRelay;
import java.time.Duration;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"learning.assessment.completed"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class AssessmentEventPropagationIT {

  @Autowired AssessmentEventPublisher publisher;
  @Autowired OutboxRelay relay;
  @Autowired ConsumerFactory<String, String> cf;
  @Autowired EmbeddedKafkaBroker broker;

  @Test
  void completedEventReachesKafka() {
    publisher.publishCompleted(55L, 42L, "BACKEND_SPRING", "MID", Map.of("spring", 0.7));
    try (Consumer<String, String> c = cf.createConsumer("it-pub-grp", "it")) {
      broker.consumeFromAnEmbeddedTopic(c, "learning.assessment.completed");
      int published = relay.relayOnce();
      assertTrue(published >= 1);
      ConsumerRecords<String, String> recs = KafkaTestUtils.getRecords(c, Duration.ofSeconds(10));
      boolean found = StreamSupport.stream(recs.spliterator(), false)
          .anyMatch(r -> r.value().contains("\"assessmentId\"") && r.value().contains("55")
              && r.value().contains("MID"));
      if (!found) {
        fail("이벤트 레코드에 assessmentId=55·MID 포함 안됨");
      }
    }
  }
}
