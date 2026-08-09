package ai.devpath.learning.knowledgegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/** 사용: LoadKnowledgeCommand &lt;embeddings.jsonl&gt; [sourceRepo] — DB는 환경변수로 준다. */
public class LoadKnowledgeCommand {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("사용: LoadKnowledgeCommand <embeddings.jsonl> [sourceRepo]");
      System.exit(2);
    }
    Path input = Path.of(args[0]);
    String sourceRepo = args.length > 1 ? args[1] : "develop-study-documents";

    var dataSource = new DriverManagerDataSource();
    dataSource.setUrl(System.getenv().getOrDefault(
        "DB_URL", "jdbc:postgresql://localhost:5432/devpath"));
    dataSource.setUsername(System.getenv().getOrDefault("DB_USERNAME", "devpath"));
    dataSource.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "devpath"));

    var jdbc = new JdbcTemplate(dataSource);
    var records = new ArrayList<KnowledgeEmbeddingRecord>();
    try (var lines = Files.lines(input, StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        if (!line.isBlank()) {
          records.add(MAPPER.readValue(line, KnowledgeEmbeddingRecord.class));
        }
      }
    }

    int loaded = new KnowledgeLoader().load(jdbc, records, sourceRepo);
    System.out.printf("문서 %d개 · 청크 %d개를 적재했습니다%n",
        records.stream().map(KnowledgeEmbeddingRecord::docKey).distinct().count(), loaded);
  }
}
