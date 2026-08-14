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
    var model = args.length > 0 && !args[0].isBlank() ? args[0] : "qwen2.5:7b";
    var only = args.length > 1 && !args[1].isBlank() ? args[1] : null;
    var tracks = only == null ? ContentQuota.TRACKS : List.of(only);
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/content-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/contents.draft.jsonl");
    Files.createDirectories(output.getParent());

    var draft = new StringBuilder();
    for (String track : tracks) {
      var trackPromptPath = Path.of("tools/content-gen/prompts/tracks/" + slug(track) + ".md");
      var trackSystemPrompt = Files.exists(trackPromptPath)
          ? systemPrompt + "\n\n" + Files.readString(trackPromptPath)
          : systemPrompt;
      draft.append(generate(baseUrl, model, track, trackSystemPrompt));
      if (!draft.toString().endsWith("\n")) {
        draft.append("\n");
      }
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft contents for " + tracks + " to " + output);
  }

  private static String slug(String track) {
    return track.toLowerCase().replace('_', '-');
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
            Map.of("role", "user", "content", userPrompt)),
        // 기본 num_ctx(4096)는 시스템+트랙 프롬프트만으로 상당 부분을 소진해, 30개 항목을 담기에
        // 부족하다. 이 상태에서 실측했더니 모델이 컨텍스트 한계에 몰려 뒷부분이 깨진 JSON과
        // 레벨 쏠림(INTERMEDIATE 반복)으로 저하됐다. 응답에 여유를 주기 위해 확장한다.
        "options", Map.of("num_ctx", 16384)));
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
