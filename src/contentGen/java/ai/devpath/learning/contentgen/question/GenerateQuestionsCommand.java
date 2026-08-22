package ai.devpath.learning.contentgen.question;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GenerateQuestionsCommand {

  public static void main(String[] args) throws Exception {
    var model = args.length > 0 && !args[0].isBlank() ? args[0] : "qwen2.5:7b";
    var only = args.length > 1 && !args[1].isBlank() ? args[1] : null;
    var tracks = only == null ? QuestionQuota.TRACKS : List.of(only);
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var client = new OllamaQuestionDraftClient(baseUrl, model);
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/question-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/questions.draft.jsonl");
    Files.createDirectories(output.getParent());

    // 트랙당 한 번에 100개를 요구하면 모델이 한 형태로 붕괴한다(실측: NODE_TYPESCRIPT 355줄 중
    // 고유 16건, PYTHON_BACKEND 1차 30줄 중 고유 6건). 쿼터 셀 단위 소배치로 나눠 거둔다.
    var attempts = Integer.getInteger("harvest.attempts", 4);
    var harvester = new QuestionHarvester(client, attempts);
    System.out.println("Harvest attempts per cell: " + attempts);
    var draft = new StringBuilder();
    for (String track : tracks) {
      var trackPrompt = Files.readString(
          Path.of("tools/content-gen/prompts/tracks/" + slug(track) + ".md"));
      var harvested = harvester.harvest(track, systemPrompt + "\n\n" + trackPrompt);
      harvested.forEach(line -> draft.append(line).append("\n"));
      System.out.println("Harvested " + harvested.size() + "/" + QuestionQuota.PER_TRACK
          + " questions for " + track);
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft questions for " + tracks + " to " + output);
  }

  private static String slug(String track) {
    return track.toLowerCase().replace('_', '-');
  }
}
