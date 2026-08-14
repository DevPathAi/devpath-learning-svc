package ai.devpath.learning.contentgen.question;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReviewQuestionsCommand {

  private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";
  private static final String MODEL = "claude-opus-5";
  private static final int CHAR_BUDGET = 40_000;

  public static void main(String[] args) throws Exception {
    var approved = Path.of(args[0]);
    var track = args.length > 1 ? args[1] : "";
    var outDir = Path.of(args.length > 2 ? args[2] : "tools/content-gen/generated/review");
    if (track.isBlank()) {
      System.err.println("track is required: -Ptrack=PYTHON_BACKEND");
      System.exit(2);
    }
    Files.createDirectories(outDir);

    List<ApprovedQuestion> all = new QuestionJsonlReader().read(approved);
    var target = all.stream().filter(q -> track.equals(q.track())).toList();
    if (target.isEmpty()) {
      System.err.println("no questions for track " + track);
      System.exit(2);
    }
    var batches = QuestionReviewPrompt.batch(target, CHAR_BUDGET);
    System.out.println(track + ": " + target.size() + " questions in " + batches.size() + " batches");

    var apiKey = System.getenv("ANTHROPIC_API_KEY");
    if (apiKey == null || apiKey.isBlank()) {
      for (int i = 0; i < batches.size(); i++) {
        Files.writeString(outDir.resolve(track + "-prompt-" + i + ".txt"),
            QuestionReviewPrompt.build(track, batches.get(i)));
      }
      System.out.println("ANTHROPIC_API_KEY 가 없어 프롬프트만 썼다: " + outDir);
      return;
    }

    var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
    var rawBodies = new ArrayList<String>();
    for (int i = 0; i < batches.size(); i++) {
      var prompt = QuestionReviewPrompt.build(track, batches.get(i));
      var body = "{\"model\":\"" + MODEL + "\",\"max_tokens\":8000,\"messages\":[{\"role\":\"user\","
          + "\"content\":" + jsonString(prompt) + "}]}";
      var request = HttpRequest.newBuilder(URI.create(ENDPOINT))
          .header("content-type", "application/json")
          .header("x-api-key", apiKey)
          .header("anthropic-version", "2023-06-01")
          .timeout(Duration.ofMinutes(10))
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new IllegalStateException("Claude " + response.statusCode() + ": " + response.body());
      }
      Files.writeString(outDir.resolve(track + "-raw-" + i + ".json"), response.body());
      rawBodies.add(response.body());
      System.out.println("batch " + i + " reviewed");
    }
    var report = outDir.resolve(track + "-review.json");
    Files.writeString(report, QuestionReviewPrompt.mergeReports(rawBodies));
    System.out.println("Wrote " + report);
  }

  private static String jsonString(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"")
        .replace("\n", "\\n").replace("\r", "").replace("\t", "\\t") + "\"";
  }
}
