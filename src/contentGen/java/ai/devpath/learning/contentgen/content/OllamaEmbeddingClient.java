package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OllamaEmbeddingClient implements EmbeddingClient {

  private final HttpClient http = HttpClient.newHttpClient();
  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final String baseUrl;
  private final String model;

  public OllamaEmbeddingClient(String baseUrl, String model) {
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.model = model;
  }

  @Override
  public List<Double> embed(String text) throws Exception {
    var body = mapper.writeValueAsString(Map.of(
        "model", model,
        "prompt", text));
    var request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/embeddings"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
    var response = http.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException("Ollama returned HTTP " + response.statusCode());
    }
    JsonNode root = mapper.readTree(response.body());
    JsonNode embedding = root.path("embedding");
    if (embedding.isMissingNode() && root.path("embeddings").isArray()) {
      embedding = root.path("embeddings").path(0);
    }
    if (!embedding.isArray()) {
      throw new IllegalStateException("Ollama response did not contain embedding array");
    }
    var values = new ArrayList<Double>();
    for (JsonNode value : embedding) {
      values.add(value.asDouble());
    }
    return values;
  }
}
