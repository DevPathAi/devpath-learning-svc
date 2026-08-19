package ai.devpath.learning.contentgen.content;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GenerateContentsCommand {

  public static void main(String[] args) throws Exception {
    var model = args.length > 0 && !args[0].isBlank() ? args[0] : "qwen2.5:7b";
    var only = args.length > 1 && !args[1].isBlank() ? args[1] : null;
    var tracks = only == null ? ContentQuota.TRACKS : List.of(only);
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/content-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/contents.draft.jsonl");
    Files.createDirectories(output.getParent());

    // 한 번에 30개를 요구하면 레벨 목표를 지키지 않는다(실측: INTRO 6 · INTERMEDIATE 14 ·
    // ADVANCED 14). 레벨별 소배치로 나눠 거두고, 게이트가 막을 행은 받는 자리에서 버린다.
    var attempts = Integer.getInteger("harvest.attempts", 4);
    var client = new OllamaContentDraftClient(baseUrl, model);
    var harvester = new ContentHarvester(client, attempts);
    System.out.println("Harvest attempts per cell: " + attempts);

    var draft = new StringBuilder();
    for (String track : tracks) {
      var trackPromptPath = Path.of("tools/content-gen/prompts/tracks/" + slug(track) + ".md");
      var trackSystemPrompt = Files.exists(trackPromptPath)
          ? systemPrompt + "\n\n" + Files.readString(trackPromptPath)
          : systemPrompt;
      var harvested = harvester.harvest(track, trackSystemPrompt);
      harvested.forEach(line -> draft.append(line).append("\n"));
      System.out.println("Harvested " + harvested.size() + "/" + ContentQuota.PER_TRACK
          + " contents for " + track);
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft contents for " + tracks + " to " + output);
  }

  private static String slug(String track) {
    return track.toLowerCase().replace('_', '-');
  }
}
