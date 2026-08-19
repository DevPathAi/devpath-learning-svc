package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OllamaContentDraftClient implements ContentDraftClient {

  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final HttpClient http = HttpClient.newHttpClient();
  private final String baseUrl;
  private final String model;
  /** 같은 요청을 되풀이하면 같은 답이 온다. 재시도마다 표본을 옮긴다. */
  private final AtomicLong seed = new AtomicLong(1);

  public OllamaContentDraftClient(String baseUrl, String model) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.model = model;
  }

  @Override
  public String generate(String track, String level, int count, String prompt) throws Exception {
    var userPrompt = "Generate " + count + " approved DevPath learning contents for track "
        + track + " at level " + level + " as JSONL only.\n"
        + "모든 항목의 level 은 정확히 \"" + level + "\" 이어야 한다.\n"
        + "contentMd 에는 raw HTML 을 쓰지 않는다(마크다운만). 코드블록을 열면 반드시 닫는다.\n"
        + "slug 는 kebab-case 이고 항목마다 서로 다르다.";
    var body = mapper.writeValueAsString(Map.of(
        "model", model,
        "stream", false,
        "messages", List.of(
            Map.of("role", "system", "content", prompt),
            Map.of("role", "user", "content", userPrompt)),
        // 기본 num_ctx(4096)로는 프롬프트만으로 상당 부분이 소진돼 뒷부분이 깨진다(실측).
        "options", Map.of("num_ctx", 16384, "seed", seed.getAndIncrement(), "temperature", 0.85)));
    var request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/chat"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
    var response = http.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException("Ollama returned HTTP " + response.statusCode());
    }
    JsonNode content = mapper.readTree(response.body()).path("message").path("content");
    if (!content.isTextual()) {
      throw new IllegalStateException("Ollama response did not contain message.content");
    }
    return content.asText();
  }
}
