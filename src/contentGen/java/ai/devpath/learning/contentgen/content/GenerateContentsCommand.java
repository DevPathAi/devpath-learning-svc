package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GenerateContentsCommand {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    var model = args.length > 0 ? args[0] : "qwen2.5:7b";
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/content-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/contents.draft.jsonl");
    Files.createDirectories(output.getParent());

    var draft = new StringBuilder();
    for (String track : ContentQuota.TRACKS) {
      draft.append(generate(baseUrl, model, track, systemPrompt));
      if (!draft.toString().endsWith("\n")) {
        draft.append("\n");
      }
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft contents to " + output);
  }

  private static String generate(String baseUrl, String model, String track, String systemPrompt)
      throws Exception {
    var base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    var userPrompt = "Generate 30 approved DevPath learning contents for track "
        + track + " as JSONL only.";
    var body = MAPPER.writeValueAsString(Map.of(
        "model", model,
        "stream", false,
        "messages", List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userPrompt))));
    var request = HttpRequest.newBuilder(URI.create(base + "/api/chat"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
    var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new IllegalStateException("Ollama returned HTTP " + response.statusCode());
    }
    JsonNode content = MAPPER.readTree(response.body()).path("message").path("content");
    if (!content.isTextual()) {
      throw new IllegalStateException("Ollama response did not contain message.content");
    }
    return content.asText();
  }
}
