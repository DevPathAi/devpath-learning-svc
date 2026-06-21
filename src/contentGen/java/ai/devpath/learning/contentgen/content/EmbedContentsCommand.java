package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.file.Files;
import java.nio.file.Path;

public class EmbedContentsCommand {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    var input = Path.of(args.length > 0 ? args[0]
        : "tools/content-gen/generated/approved/contents.jsonl");
    var output = Path.of(args.length > 1 ? args[1]
        : "tools/content-gen/generated/approved/content_embeddings.jsonl");
    var model = args.length > 2 ? args[2] : "nomic-embed-text";
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");

    var contents = new ContentJsonlReader().read(input);
    var contentReport = new ContentValidator().validate(contents);
    if (!contentReport.valid()) {
      contentReport.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }

    var client = new OllamaEmbeddingClient(baseUrl, model);
    var chunker = new ContentChunker();
    var jsonl = new StringBuilder();
    for (ApprovedContent content : contents) {
      for (ContentChunk chunk : chunker.chunksFor(content)) {
        var record = new EmbeddingRecord(
            chunk.slug(),
            chunk.chunkIndex(),
            chunk.chunkText(),
            client.embed(chunk.chunkText()),
            chunk.chunkHash(),
            "ACTIVE");
        jsonl.append(MAPPER.writeValueAsString(record)).append('\n');
      }
    }
    Files.createDirectories(output.getParent());
    Files.writeString(output, jsonl.toString());
    System.out.println("Wrote embeddings to " + output);
  }
}
