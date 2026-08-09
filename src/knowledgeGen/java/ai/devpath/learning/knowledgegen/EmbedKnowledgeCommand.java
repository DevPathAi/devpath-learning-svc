package ai.devpath.learning.knowledgegen;

import ai.devpath.learning.contentgen.content.ContentChunk;
import ai.devpath.learning.contentgen.content.ContentChunker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 사용: EmbedKnowledgeCommand &lt;documents.jsonl&gt; &lt;embeddings.jsonl&gt; &lt;sourceCommit&gt; [model]
 *
 * <p>배치 50으로 묶어 임베딩한다(실측 58ms/청크). 이미 임베딩된 doc_hash는 건너뛰므로
 * 중단 후 같은 명령을 다시 실행하면 이어서 진행한다.
 */
public class EmbedKnowledgeCommand {

  static final int BATCH_SIZE = 50;

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println(
          "사용: EmbedKnowledgeCommand <documents.jsonl> <embeddings.jsonl> <sourceCommit> [model]");
      System.exit(2);
    }
    Path input = Path.of(args[0]);
    Path output = Path.of(args[1]);
    String sourceCommit = args[2];
    String model = args.length > 3 ? args[3] : "nomic-embed-text";
    String baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");

    List<KnowledgeDoc> docs = readDocuments(input);
    Set<String> alreadyDone = readCompletedDocHashes(output);
    if (!alreadyDone.isEmpty()) {
      System.out.printf("이미 임베딩된 문서 %d개는 건너뜁니다%n", alreadyDone.size());
    }

    var client = new OllamaBatchEmbeddingClient(baseUrl, model);
    List<KnowledgeEmbeddingRecord> records = run(docs, client, sourceCommit, alreadyDone);

    Files.createDirectories(output.getParent());
    try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
      for (KnowledgeEmbeddingRecord record : records) {
        writer.write(MAPPER.writeValueAsString(record));
        writer.newLine();
      }
    }
    System.out.printf("청크 %d개를 %s 에 추가했습니다%n", records.size(), output);
  }

  /** 문서 경계와 무관하게 청크를 모아 BATCH_SIZE씩 임베딩한다. */
  static List<KnowledgeEmbeddingRecord> run(List<KnowledgeDoc> docs, BatchEmbeddingClient client,
      String sourceCommit, Set<String> alreadyDone) throws Exception {
    var chunker = new ContentChunker();

    var pending = new ArrayList<KnowledgeEmbeddingRecord>();
    for (KnowledgeDoc doc : docs) {
      if (alreadyDone.contains(doc.docHash())) continue;
      for (ContentChunk chunk : chunker.chunksFor(doc.docKey(), doc.markdown())) {
        pending.add(new KnowledgeEmbeddingRecord(
            doc.docKey(), doc.title(), doc.category(), doc.docHash(),
            chunk.chunkIndex(), chunk.chunkText(), null, chunk.chunkHash(), sourceCommit));
      }
    }

    var result = new ArrayList<KnowledgeEmbeddingRecord>(pending.size());
    for (int start = 0; start < pending.size(); start += BATCH_SIZE) {
      List<KnowledgeEmbeddingRecord> batch =
          pending.subList(start, Math.min(pending.size(), start + BATCH_SIZE));
      List<List<Double>> vectors = client.embedAll(batch.stream().map(KnowledgeEmbeddingRecord::chunkText).toList());
      for (int i = 0; i < batch.size(); i++) {
        KnowledgeEmbeddingRecord r = batch.get(i);
        result.add(new KnowledgeEmbeddingRecord(r.docKey(), r.title(), r.category(), r.docHash(),
            r.chunkIndex(), r.chunkText(), vectors.get(i), r.chunkHash(), r.sourceCommit()));
      }
      System.out.printf("  %d / %d 청크%n", result.size(), pending.size());
    }
    return List.copyOf(result);
  }

  private static List<KnowledgeDoc> readDocuments(Path path) throws Exception {
    var docs = new ArrayList<KnowledgeDoc>();
    try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        if (!line.isBlank()) docs.add(MAPPER.readValue(line, KnowledgeDoc.class));
      }
    }
    return docs;
  }

  /** 체크포인트: 출력 파일에 이미 들어간 doc_hash 집합. */
  private static Set<String> readCompletedDocHashes(Path path) throws Exception {
    var hashes = new HashSet<String>();
    if (!Files.exists(path)) return hashes;
    try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        if (!line.isBlank()) {
          hashes.add(MAPPER.readValue(line, KnowledgeEmbeddingRecord.class).docHash());
        }
      }
    }
    return hashes;
  }
}
