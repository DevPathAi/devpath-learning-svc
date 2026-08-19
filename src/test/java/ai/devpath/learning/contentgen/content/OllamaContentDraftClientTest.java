package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

/**
 * 출력 상한이 없으면 한 번의 호출이 컨텍스트를 채울 때까지 늘어진다. 실측으로 DATA_AI 콘텐츠
 * 생성이 70분 넘게 한 요청에 매달렸다(GPU 는 계속 돌고 태스크 번호는 그대로였다).
 */
class OllamaContentDraftClientTest {

  private static final Pattern NUM_PREDICT = Pattern.compile("\"num_predict\"\\s*:\\s*(\\d+)");
  private static final Pattern SEED = Pattern.compile("\"seed\"\\s*:\\s*(-?\\d+)");

  private static MockResponse ok() {
    return new MockResponse()
        .addHeader("Content-Type", "application/json")
        .setBody("{\"message\":{\"content\":\"{}\\n\"}}");
  }

  @Test
  void boundsOutputSoOneCallCannotRunAway() throws Exception {
    try (var server = new MockWebServer()) {
      server.enqueue(ok());
      server.start();

      new OllamaContentDraftClient(server.url("/").toString(), "test-model")
          .generate("DATA_AI", "INTRO", 3, "system prompt");

      var body = server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8();
      var matcher = NUM_PREDICT.matcher(body);

      assertThat(matcher.find()).as("num_predict 가 요청에 실려야 한다").isTrue();
      assertThat(Integer.parseInt(matcher.group(1))).isBetween(1, 4096);
    }
  }

  @Test
  void carriesTheRequestedLevelAndCount() throws Exception {
    try (var server = new MockWebServer()) {
      server.enqueue(ok());
      server.start();

      new OllamaContentDraftClient(server.url("/").toString(), "test-model")
          .generate("DATA_AI", "ADVANCED", 3, "system prompt");

      var body = server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8();

      assertThat(body).contains("ADVANCED").contains("DATA_AI").contains("system prompt");
    }
  }

  @Test
  void variesSamplingSeedAcrossCalls() throws Exception {
    try (var server = new MockWebServer()) {
      server.enqueue(ok());
      server.enqueue(ok());
      server.start();
      var client = new OllamaContentDraftClient(server.url("/").toString(), "test-model");

      client.generate("DATA_AI", "INTRO", 1, "system prompt");
      client.generate("DATA_AI", "INTRO", 1, "system prompt");

      var first = SEED.matcher(server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8());
      var second = SEED.matcher(server.takeRequest(2, TimeUnit.SECONDS).getBody().readUtf8());

      assertThat(first.find()).isTrue();
      assertThat(second.find()).isTrue();
      assertThat(first.group(1)).isNotEqualTo(second.group(1));
    }
  }
}
