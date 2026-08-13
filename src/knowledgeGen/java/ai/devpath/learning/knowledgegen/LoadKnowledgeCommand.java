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
    // 레포 관례(application.yml)는 DB_USER를 쓴다. DB_USERNAME은 이 커맨드의 옛 계획 문서에만
    // 있던 이름이라 하위호환으로 남기되, DB_USER를 우선 본다.
    dataSource.setUsername(firstNonBlank(
        System.getenv("DB_USERNAME"), System.getenv("DB_USER"), "devpath"));
    dataSource.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "localdev"));

    var jdbc = new JdbcTemplate(dataSource);
    var records = new ArrayList<KnowledgeEmbeddingRecord>();
    int lineNo = 0;
    try (var lines = Files.lines(input, StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        lineNo++;
        if (line.isBlank()) continue;
        try {
          records.add(MAPPER.readValue(line, KnowledgeEmbeddingRecord.class));
        } catch (com.fasterxml.jackson.core.JacksonException e) {
          System.err.printf("경고: %s %d번째 줄이 손상돼 건너뜁니다: %s%n",
              input, lineNo, e.getOriginalMessage());
        }
      }
    }

    int loaded = new KnowledgeLoader().load(jdbc, records, sourceRepo);
    System.out.printf("문서 %d개 · 청크 %d개를 적재했습니다%n",
        records.stream().map(KnowledgeEmbeddingRecord::docKey).distinct().count(), loaded);
  }

  /** 순서대로 보아 공백이 아닌 첫 값을 반환한다. 전부 없거나 공백이면 마지막 인자(기본값)를 반환한다. */
  private static String firstNonBlank(String... candidates) {
    for (String candidate : candidates) {
      if (candidate != null && !candidate.isBlank()) {
        return candidate;
      }
    }
    return candidates[candidates.length - 1];
  }
}
