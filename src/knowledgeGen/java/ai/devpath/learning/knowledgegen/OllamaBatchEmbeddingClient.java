package ai.devpath.learning.knowledgegen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Ollama /api/embed 배치 클라이언트. contentGen의 OllamaEmbeddingClient(단건 /api/embeddings)와
 * 달리 input에 배열을 넘긴다. 실측: 단건 2,284ms/청크 → 배치 50개 58ms/청크(39배).
 */
public class OllamaBatchEmbeddingClient implements BatchEmbeddingClient {

  private static final int DIMENSIONS = 768;

  private final HttpClient http = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10)).build();
  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final String baseUrl;
  private final String model;

  public OllamaBatchEmbeddingClient(String baseUrl, String model) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.model = model;
  }

  @Override
  public List<List<Double>> embedAll(List<String> texts) throws Exception {
    var payload = new LinkedHashMap<String, Object>();
    payload.put("model", model);
    payload.put("input", texts);

    var request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/embed"))
        .header("Content-Type", "application/json")
        .timeout(Duration.ofMinutes(10))
        .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
        .build();

    var response = http.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException("Ollama returned HTTP " + response.statusCode());
    }

    JsonNode embeddings = mapper.readTree(response.body()).path("embeddings");
    if (!embeddings.isArray()) {
      throw new IllegalStateException("Ollama 응답에 embeddings 배열이 없습니다");
    }
    if (embeddings.size() != texts.size()) {
      throw new IllegalStateException(
          "Ollama embed 응답 개수가 요청과 다릅니다: 요청 " + texts.size() + ", 응답 " + embeddings.size());
    }

    var result = new ArrayList<List<Double>>(embeddings.size());
    for (JsonNode row : embeddings) {
      if (!row.isArray() || row.size() != DIMENSIONS) {
        throw new IllegalStateException(
            "Ollama embed 응답 차원이 768이 아닙니다: " + (row.isArray() ? row.size() : -1));
      }
      var vector = new ArrayList<Double>(DIMENSIONS);
      for (JsonNode v : row) {
        vector.add(v.asDouble());
      }
      result.add(List.copyOf(vector));
    }
    return List.copyOf(result);
  }
}
