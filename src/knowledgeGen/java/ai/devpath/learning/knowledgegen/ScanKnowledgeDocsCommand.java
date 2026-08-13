package ai.devpath.learning.knowledgegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** 사용: ScanKnowledgeDocsCommand &lt;문서루트&gt; [출력경로] */
public class ScanKnowledgeDocsCommand {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("사용: ScanKnowledgeDocsCommand <문서루트> [출력경로]");
      System.exit(2);
    }
    Path root = Path.of(args[0]);
    Path output = Path.of(args.length > 1 ? args[1]
        : "tools/knowledge-gen/generated/documents.jsonl");

    List<KnowledgeDoc> docs = new KnowledgeDocScanner().scan(root);

    var sb = new StringBuilder();
    for (KnowledgeDoc doc : docs) {
      sb.append(MAPPER.writeValueAsString(doc)).append('\n');
    }
    Files.createDirectories(output.getParent());
    Files.writeString(output, sb.toString(), StandardCharsets.UTF_8);

    long totalChars = docs.stream().mapToLong(d -> d.markdown().length()).sum();
    System.out.printf("문서 %d개 · %,d자 → %s%n", docs.size(), totalChars, output);
    docs.stream()
        .collect(java.util.stream.Collectors.groupingBy(
            KnowledgeDoc::category, java.util.TreeMap::new, java.util.stream.Collectors.counting()))
        .forEach((category, count) -> System.out.printf("  %-26s %4d%n", category, count));
  }
}
