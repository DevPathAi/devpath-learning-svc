package ai.devpath.learning.contentgen.question;

import java.nio.file.Files;
import java.nio.file.Path;

public class GenerateQuestionsCommand {

  public static void main(String[] args) throws Exception {
    var model = args.length > 0 ? args[0] : "qwen2.5:7b";
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var client = new OllamaQuestionDraftClient(baseUrl, model);
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/question-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/questions.draft.jsonl");
    Files.createDirectories(output.getParent());

    var draft = new StringBuilder();
    for (String track : QuestionQuota.TRACKS) {
      var trackPrompt = Files.readString(Path.of("tools/content-gen/prompts/tracks/" + slug(track) + ".md"));
      draft.append(client.generate(track, QuestionQuota.PER_TRACK, systemPrompt + "\n\n" + trackPrompt));
      if (!draft.toString().endsWith("\n")) {
        draft.append("\n");
      }
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft questions to " + output);
  }

  private static String slug(String track) {
    return track.toLowerCase().replace('_', '-');
  }
}
