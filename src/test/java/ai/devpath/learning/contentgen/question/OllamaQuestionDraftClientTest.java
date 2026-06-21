package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

class OllamaQuestionDraftClientTest {

  @Test
  void callsConfiguredOllamaEndpointAndReturnsMessageContent() throws Exception {
    try (var server = new MockWebServer()) {
      server.enqueue(new MockResponse()
          .addHeader("Content-Type", "application/json")
          .setBody("{\"message\":{\"content\":\"{\\\"track\\\":\\\"BACKEND_SPRING\\\"}\\n\"}}"));
      server.start();
      var client = new OllamaQuestionDraftClient(server.url("/").toString(), "test-model");

      var draft = client.generate("BACKEND_SPRING", 1, "system prompt");
      var request = server.takeRequest(2, TimeUnit.SECONDS);

      assertThat(draft).isEqualTo("{\"track\":\"BACKEND_SPRING\"}\n");
      assertThat(request).isNotNull();
      assertThat(request.getPath()).isEqualTo("/api/chat");
      assertThat(request.getBody().readUtf8())
          .contains("test-model")
          .contains("BACKEND_SPRING")
          .contains("system prompt");
    }
  }
}
