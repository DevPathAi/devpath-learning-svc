package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class OllamaQuestionDraftClient implements QuestionDraftClient {

  private final HttpClient http = HttpClient.newHttpClient();
  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final String baseUrl;
  private final String model;

  /**
   * 같은 요청을 되풀이하면 모델도 같은 답을 되풀이한다(실측: 355줄 중 고유 16건).
   * 호출마다 seed 를 옮겨 재시도가 실제로 다른 표본을 뽑게 한다.
   */
  private final java.util.concurrent.atomic.AtomicLong seed =
      new java.util.concurrent.atomic.AtomicLong(1);

  public OllamaQuestionDraftClient(String baseUrl, String model) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.model = model;
  }

  @Override
  public String generate(String track, int count, String prompt) throws Exception {
    var userPrompt = "Generate " + count + " approved diagnostic questions for track "
        + track + " as JSONL only.";
    var body = mapper.writeValueAsString(Map.of(
        "model", model,
        "stream", false,
        "options", Map.of(
            "seed", seed.getAndIncrement(),
            "temperature", 0.9,
            "top_p", 0.95,
            "repeat_penalty", 1.15),
        "messages", List.of(
            Map.of("role", "system", "content", prompt),
            Map.of("role", "user", "content", userPrompt))));
    var request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/chat"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
    var response = http.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException("Ollama returned HTTP " + response.statusCode());
    }
    JsonNode root = mapper.readTree(response.body());
    var content = root.path("message").path("content");
    if (content.isMissingNode() || !content.isTextual()) {
      throw new IllegalStateException("Ollama response did not contain message.content");
    }
    return content.asText();
  }
}
