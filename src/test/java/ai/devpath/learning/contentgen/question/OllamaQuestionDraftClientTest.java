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

  // 같은 셀을 다시 요청할 때 요청이 완전히 같으면 모델은 같은 답을 되풀이한다.
  // 실측으로 355줄 중 고유 16건까지 붕괴했다. 시도마다 seed 가 달라져야 한다.
  @Test
  void variesSamplingSeedAcrossCallsSoRetriesDiffer() throws Exception {
    try (var server = new MockWebServer()) {
      for (int i = 0; i < 2; i++) {
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody("{\"message\":{\"content\":\"{}\\n\"}}"));
      }
      server.start();
      var client = new OllamaQuestionDraftClient(server.url("/").toString(), "test-model");

      client.generate("BACKEND_SPRING", 1, "system prompt");
      client.generate("BACKEND_SPRING", 1, "system prompt");

      var first = seedOf(server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8());
      var second = seedOf(server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8());

      assertThat(first).isNotNull();
      assertThat(second).isNotNull();
      assertThat(first).isNotEqualTo(second);
    }
  }

  private static String seedOf(String body) {
    var matcher = java.util.regex.Pattern.compile("\"seed\"\\s*:\\s*(-?\\d+)").matcher(body);
    return matcher.find() ? matcher.group(1) : null;
  }
}
