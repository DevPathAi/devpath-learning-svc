package ai.devpath.learning.contentgen.content;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class MakeContentSeedSqlCommand {

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: MakeContentSeedSqlCommand <contents.jsonl> "
          + "<content_embeddings.jsonl> <output.sql> [copy.sql...]");
      System.exit(2);
    }

    var contentInput = Path.of(args[0]);
    var embeddingInput = Path.of(args[1]);
    var outputs = List.of(args).subList(2, args.length).stream().map(Path::of).toList();

    var contents = new ContentJsonlReader().read(contentInput);
    var contentReport = new ContentValidator().validate(contents);
    contentReport.warnings().forEach(warning -> System.err.println("WARN " + warning));
    if (!contentReport.valid()) {
      contentReport.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }

    var embeddings = new EmbeddingJsonlReader().read(embeddingInput);
    var embeddingReport = new EmbeddingValidator().validate(embeddings);
    if (!embeddingReport.valid()) {
      embeddingReport.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }
    assertEveryContentHasEmbedding(contents, embeddings);

    var sql = new ContentSeedSqlWriter().toSql(contents, embeddings);
    for (Path output : outputs) {
      if (output.getParent() != null) {
        Files.createDirectories(output.getParent());
      }
      Files.writeString(output, sql);
      System.out.println("Wrote " + output);
    }
  }

  private static void assertEveryContentHasEmbedding(List<ApprovedContent> contents,
      List<EmbeddingRecord> embeddings) {
    var slugsWithEmbeddings = embeddings.stream()
        .filter(e -> "ACTIVE".equals(e.status()))
        .map(EmbeddingRecord::slug)
        .collect(Collectors.toSet());
    var missing = contents.stream()
        .map(ApprovedContent::slug)
        .filter(slug -> !slugsWithEmbeddings.contains(slug))
        .toList();
    if (!missing.isEmpty()) {
      throw new IllegalStateException("contents missing embeddings: " + missing);
    }
  }
}
