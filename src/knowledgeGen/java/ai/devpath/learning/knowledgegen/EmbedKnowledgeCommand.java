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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 사용: EmbedKnowledgeCommand &lt;documents.jsonl&gt; &lt;embeddings.jsonl&gt; &lt;sourceCommit&gt; [model]
 *
 * <p>배치 50으로 묶어 임베딩한다(실측 58ms/청크). 배치가 끝날 때마다 즉시 {@code embeddings.jsonl}에
 * append+flush하므로, 중단 시 손실 범위는 마지막 배치(최대 50청크)로 제한된다 - 전체 실행이 아니다.
 *
 * <p>재개 판정은 (docKey, docHash) 쌍을 키로 삼는다. 단순히 파일에 그 문서의 줄이 존재하는지가
 * 아니라, 그 문서의 전체 청크 개수(재청킹으로 재계산)만큼 줄이 실제로 기록돼 있는지까지 확인한다
 * - 배치 경계가 문서 경계와 무관하므로, 한 문서의 청크 일부만 기록된 채 중단됐을 수 있기 때문이다.
 * 이 조건을 만족하지 못하면 그 문서는 다음 실행에서 처음부터 다시 임베딩되며(청크 재작성으로 파일에
 * 중복 줄이 남을 수 있음), 완전히 기록된 문서만 건너뛴다.
 */
public class EmbedKnowledgeCommand {

  static final int BATCH_SIZE = 50;

  /** docKey/docHash 조합 키의 구분자. 둘 중 어디에도 나타나지 않는 NUL 문자를 쓴다. */
  private static final char CHECKPOINT_KEY_SEPARATOR = '\u0000';

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  /** 배치 하나가 임베딩을 마칠 때마다 호출된다 - 디스크에 즉시 기록할 기회를 준다. */
  interface BatchSink {
    void onBatchComplete(List<KnowledgeEmbeddingRecord> batch) throws Exception;
  }

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

    var client = new OllamaBatchEmbeddingClient(baseUrl, model);
    int written = embedToFile(input, output, client, sourceCommit);
    System.out.printf("청크 %d개를 %s 에 추가했습니다%n", written, output);
  }

  /**
   * main()의 파일 왕복 본체: 문서를 읽고, 이미 온전히 기록된 문서를 건너뛰고, 배치가 끝날 때마다
   * 즉시 flush하며 이어 쓴다. 실제 Ollama 없이도 재개 시나리오를 테스트할 수 있도록 분리했다.
   *
   * @return 이번 실행에서 새로 기록한 청크 개수
   */
  static int embedToFile(Path input, Path output, BatchEmbeddingClient client, String sourceCommit)
      throws Exception {
    List<KnowledgeDoc> docs = readDocuments(input);
    var chunker = new ContentChunker();
    Set<String> alreadyDone = readCompletedCheckpoints(output, docs, chunker);
    if (!alreadyDone.isEmpty()) {
      System.out.printf("이미 임베딩된 문서 %d개는 건너뜁니다%n", alreadyDone.size());
    }

    Files.createDirectories(output.getParent());
    try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
      List<KnowledgeEmbeddingRecord> records =
          run(docs, client, sourceCommit, alreadyDone, batch -> writeBatch(writer, batch));
      return records.size();
    }
  }

  static String checkpointKey(String docKey, String docHash) {
    return docKey + CHECKPOINT_KEY_SEPARATOR + docHash;
  }

  /**
   * 문서 경계와 무관하게 청크를 모아 BATCH_SIZE씩 임베딩한다. 배치가 끝날 때마다 {@code sink}를
   * 호출해 즉시 디스크에 기록할 기회를 준다(중단 시 손실 범위를 마지막 배치로 제한).
   */
  static List<KnowledgeEmbeddingRecord> run(List<KnowledgeDoc> docs, BatchEmbeddingClient client,
      String sourceCommit, Set<String> alreadyDone, BatchSink sink) throws Exception {
    var chunker = new ContentChunker();

    var pending = new ArrayList<KnowledgeEmbeddingRecord>();
    for (KnowledgeDoc doc : docs) {
      if (alreadyDone.contains(checkpointKey(doc.docKey(), doc.docHash()))) continue;
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
      var embedded = new ArrayList<KnowledgeEmbeddingRecord>(batch.size());
      for (int i = 0; i < batch.size(); i++) {
        KnowledgeEmbeddingRecord r = batch.get(i);
        embedded.add(new KnowledgeEmbeddingRecord(r.docKey(), r.title(), r.category(), r.docHash(),
            r.chunkIndex(), r.chunkText(), vectors.get(i), r.chunkHash(), r.sourceCommit()));
      }
      sink.onBatchComplete(embedded);
      result.addAll(embedded);
      System.out.printf("  %d / %d 청크 (기록 완료)%n", result.size(), pending.size());
    }
    return List.copyOf(result);
  }

  private static void writeBatch(BufferedWriter writer, List<KnowledgeEmbeddingRecord> batch)
      throws Exception {
    for (KnowledgeEmbeddingRecord record : batch) {
      writer.write(MAPPER.writeValueAsString(record));
      writer.newLine();
    }
    writer.flush();
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

  /**
   * 체크포인트: 출력 파일에 문서 전체 청크가 온전히 기록된 (docKey, docHash) 키 집합.
   * 배치 flush가 문서 경계와 무관하므로, 파일에 그 문서의 줄이 일부만 있을 수 있다 - 그래서 줄이
   * 존재한다는 사실만으로 완료 처리하지 않고, 문서를 재청킹해 얻은 기대 청크 개수와 파일에서 관측한
   * chunkIndex 개수를 비교해 정확히 일치할 때만 완료로 인정한다.
   */
  private static Set<String> readCompletedCheckpoints(
      Path path, List<KnowledgeDoc> docs, ContentChunker chunker) throws Exception {
    if (!Files.exists(path)) return Set.of();

    var seenChunkIndexesByKey = new HashMap<String, Set<Integer>>();
    int lineNo = 0;
    try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        lineNo++;
        if (line.isBlank()) continue;
        KnowledgeEmbeddingRecord record;
        try {
          record = MAPPER.readValue(line, KnowledgeEmbeddingRecord.class);
        } catch (com.fasterxml.jackson.core.JacksonException e) {
          System.err.printf("경고: %s %d번째 줄이 손상돼 건너뜁니다: %s%n",
              path, lineNo, e.getOriginalMessage());
          continue;
        }
        String key = checkpointKey(record.docKey(), record.docHash());
        seenChunkIndexesByKey.computeIfAbsent(key, k -> new HashSet<>()).add(record.chunkIndex());
      }
    }

    var completed = new HashSet<String>();
    for (KnowledgeDoc doc : docs) {
      String key = checkpointKey(doc.docKey(), doc.docHash());
      Set<Integer> seen = seenChunkIndexesByKey.get(key);
      if (seen == null) continue;
      int expectedChunkCount = chunker.chunksFor(doc.docKey(), doc.markdown()).size();
      if (seen.size() == expectedChunkCount) {
        completed.add(key);
      }
    }
    return Set.copyOf(completed);
  }
}
