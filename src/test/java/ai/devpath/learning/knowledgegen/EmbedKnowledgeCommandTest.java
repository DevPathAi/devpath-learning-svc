package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EmbedKnowledgeCommandTest {

  /** 아무 것도 하지 않는 배치 싱크. 디스크 기록을 검증하지 않는 테스트에서 쓴다. */
  private static final EmbedKnowledgeCommand.BatchSink NO_OP_SINK = batch -> {};

  /** 요청된 배치 크기를 기록하는 가짜 클라이언트. */
  private static final class RecordingClient implements BatchEmbeddingClient {
    final List<Integer> batchSizes = new ArrayList<>();

    @Override
    public List<List<Double>> embedAll(List<String> texts) {
      batchSizes.add(texts.size());
      var out = new ArrayList<List<Double>>();
      for (int i = 0; i < texts.size(); i++) {
        out.add(Collections.nCopies(768, 0.1));
      }
      return out;
    }
  }

  private KnowledgeDoc doc(String key, int sections) {
    var sb = new StringBuilder("# 제목\n\n인트로.\n");
    for (int i = 0; i < sections; i++) {
      sb.append("\n## 섹션 ").append(i).append("\n본문 ").append(i).append(".\n");
    }
    return new KnowledgeDoc(key, "제목", "AWS", "hash-" + key, sb.toString());
  }

  @Test
  void embedsInBatchesOfFifty() throws Exception {
    // 섹션 60개 + 인트로 = 청크 61개 → 배치 50 + 11
    var client = new RecordingClient();

    List<KnowledgeEmbeddingRecord> records = EmbedKnowledgeCommand.run(
        List.of(doc("AWS/a.md", 60)), client, "abc1234", Set.of(), NO_OP_SINK);

    assertThat(records).hasSize(61);
    assertThat(client.batchSizes).containsExactly(50, 11);
  }

  @Test
  void carriesDocumentMetadataAndSourceCommitOntoEveryChunk() throws Exception {
    List<KnowledgeEmbeddingRecord> records = EmbedKnowledgeCommand.run(
        List.of(doc("AWS/a.md", 2)), new RecordingClient(), "deadbeef", Set.of(), NO_OP_SINK);

    assertThat(records).allSatisfy(r -> {
      assertThat(r.docKey()).isEqualTo("AWS/a.md");
      assertThat(r.category()).isEqualTo("AWS");
      assertThat(r.docHash()).isEqualTo("hash-AWS/a.md");
      assertThat(r.sourceCommit()).isEqualTo("deadbeef");
      assertThat(r.embedding()).hasSize(768);
      assertThat(r.chunkHash()).isNotBlank();
    });
    assertThat(records).extracting(KnowledgeEmbeddingRecord::chunkIndex)
        .containsExactly(0, 1, 2);
  }

  @Test
  void skipsDocumentsAlreadyEmbedded() throws Exception {
    var client = new RecordingClient();

    List<KnowledgeEmbeddingRecord> records = EmbedKnowledgeCommand.run(
        List.of(doc("AWS/a.md", 1), doc("AWS/b.md", 1)),
        client, "abc",
        Set.of(EmbedKnowledgeCommand.checkpointKey("AWS/a.md", "hash-AWS/a.md")),
        NO_OP_SINK);

    assertThat(records).extracting(KnowledgeEmbeddingRecord::docKey).containsOnly("AWS/b.md");
  }

  @Test
  void batchesAcrossDocumentBoundaries() throws Exception {
    var client = new RecordingClient();

    // 문서 3개 × 청크 3개 = 9청크 → 한 배치로 묶여야 한다(문서별 9회가 아니라)
    EmbedKnowledgeCommand.run(
        List.of(doc("AWS/a.md", 2), doc("AWS/b.md", 2), doc("AWS/c.md", 2)),
        client, "abc", Set.of(), NO_OP_SINK);

    assertThat(client.batchSizes).containsExactly(9);
  }

  @Test
  void resumeAfterCrashDoesNotReembedFullyWrittenDocsOrDuplicateLines(@TempDir Path tempDir)
      throws Exception {
    var mapper = JsonMapper.builder().build();
    Path documentsFile = tempDir.resolve("documents.jsonl");
    Path embeddingsFile = tempDir.resolve("embeddings.jsonl");

    KnowledgeDoc docA = doc("AWS/a.md", 1); // 청크 2개 (인트로 + 섹션 1개)
    KnowledgeDoc docB = doc("AWS/b.md", 1); // 청크 2개
    writeJsonl(documentsFile, mapper, docA, docB);

    // ① 첫 실행: 두 문서 모두 임베딩되어 embeddings.jsonl에 기록된다.
    var firstRunClient = new RecordingClient();
    int firstWritten =
        EmbedKnowledgeCommand.embedToFile(documentsFile, embeddingsFile, firstRunClient, "abc1234");

    assertThat(firstWritten).isEqualTo(4);
    List<String> linesAfterFirstRun = Files.readAllLines(embeddingsFile, StandardCharsets.UTF_8);
    assertThat(linesAfterFirstRun).hasSize(4);

    // ② 같은 embeddings.jsonl로 재실행한다.
    var secondRunClient = new RecordingClient();
    int secondWritten =
        EmbedKnowledgeCommand.embedToFile(documentsFile, embeddingsFile, secondRunClient, "abc1234");

    // ③ 이미 완전히 기록된 두 문서는 다시 임베딩되지 않고(배치 호출 자체가 없고),
    //    파일에 중복 줄도 생기지 않는다.
    assertThat(secondWritten).isZero();
    assertThat(secondRunClient.batchSizes).isEmpty();
    List<String> linesAfterSecondRun = Files.readAllLines(embeddingsFile, StandardCharsets.UTF_8);
    assertThat(linesAfterSecondRun).hasSize(4);
  }

  /**
   * I3 회귀 테스트. 한 줄은 768개 double + 청크 본문 ≈ 11~13KB인데 BufferedWriter 기본 버퍼는
   * 8192자다 — write(line) 한 번이 이미 여러 OS write로 쪼개지므로, flush 사이가 아니라 "한 줄을
   * 쓰는 도중"에도 프로세스가 죽으면 잘린(구문상 손상된) 줄이 남을 수 있다. 손상된 줄을 만나도
   * readCompletedCheckpoints가 예외로 전체를 죽이지 않고, 그 줄을 건너뛰며(체크포인트 미형성 →
   * 해당 문서는 미완료로 재임베딩), 나머지 정상 줄은 그대로 처리됨을 검증한다.
   */
  @Test
  void corruptedCheckpointLineIsSkippedWithoutThrowing(@TempDir Path tempDir) throws Exception {
    var mapper = JsonMapper.builder().build();
    Path documentsFile = tempDir.resolve("documents.jsonl");
    Path embeddingsFile = tempDir.resolve("embeddings.jsonl");

    KnowledgeDoc docA = doc("AWS/a.md", 1); // 청크 2개
    KnowledgeDoc docB = doc("AWS/b.md", 1); // 청크 2개
    writeJsonl(documentsFile, mapper, docA, docB);

    // docA의 청크 0은 정상 기록되고, 청크 1은 쓰는 도중 잘린 상태를 재현한다(실제 JSON 절반만 남음).
    KnowledgeEmbeddingRecord validChunk = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목", "AWS", "hash-AWS/a.md", 0, "청크 본문 0",
        Collections.nCopies(768, 0.1), "chunk-hash-0", "abc1234");
    String fullSecondLine = mapper.writeValueAsString(new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목", "AWS", "hash-AWS/a.md", 1, "청크 본문 1",
        Collections.nCopies(768, 0.1), "chunk-hash-1", "abc1234"));
    String truncatedSecondLine = fullSecondLine.substring(0, fullSecondLine.length() / 2);
    Files.writeString(embeddingsFile,
        mapper.writeValueAsString(validChunk) + "\n" + truncatedSecondLine + "\n",
        StandardCharsets.UTF_8);

    var client = new RecordingClient();

    int written = EmbedKnowledgeCommand.embedToFile(documentsFile, embeddingsFile, client, "abc1234");

    // 손상된 줄 때문에 docA는 청크 1개만 유효하게 관측돼(기대 2개와 불일치) 미완료로 판정되고
    // 전체 재임베딩된다. docB는 체크포인트가 아예 없어 마찬가지로 임베딩된다. 예외는 나지 않는다.
    assertThat(written).isEqualTo(4); // docA 2개 + docB 2개
    List<String> finalLines = Files.readAllLines(embeddingsFile, StandardCharsets.UTF_8);
    assertThat(finalLines).hasSize(6); // 기존 2줄(정상+손상) + 이번 실행 4줄(손상된 줄은 지워지지 않는다)
  }

  /**
   * I5 회귀 테스트(부분-기록 후 재개). 기존 resumeAfterCrash 테스트는 "완전 기록 후 재실행"만
   * 덮는다 — Task 5의 flush-per-batch fix가 실제로 존재하는 이유인 "부분 기록 후 재개"는
   * 코드 정독으로만 확인됐었다. 첫 실행 후 docA의 마지막 줄(청크 1)만 지워 부분 기록을
   * 재현하고, 재실행이 ① docA를 전량(청크 2개) 재임베딩하고 ② 파일 최종 줄 수가 기대값과
   * 맞으며 ③ 이미 온전한 docB는 재임베딩하지 않는지(=체크포인트가 docB만 완료로 판정) 검증한다.
   */
  @Test
  void resumeAfterPartialWriteReembedsWholeDocumentButSkipsCompletedDoc(@TempDir Path tempDir)
      throws Exception {
    var mapper = JsonMapper.builder().build();
    Path documentsFile = tempDir.resolve("documents.jsonl");
    Path embeddingsFile = tempDir.resolve("embeddings.jsonl");

    KnowledgeDoc docA = doc("AWS/a.md", 1); // 청크 2개 (인트로 + 섹션 1개)
    KnowledgeDoc docB = doc("AWS/b.md", 1); // 청크 2개
    writeJsonl(documentsFile, mapper, docA, docB);

    // ① 첫 실행: 두 문서 모두 임베딩되어 embeddings.jsonl에 4줄(docA 0,1 · docB 0,1)로 기록된다.
    var firstRunClient = new RecordingClient();
    int firstWritten =
        EmbedKnowledgeCommand.embedToFile(documentsFile, embeddingsFile, firstRunClient, "abc1234");
    assertThat(firstWritten).isEqualTo(4);
    List<String> linesAfterFirstRun = Files.readAllLines(embeddingsFile, StandardCharsets.UTF_8);
    assertThat(linesAfterFirstRun).hasSize(4);

    // ② 프로세스가 docA의 마지막 줄(청크 1)을 쓰는 도중 죽은 상황을 재현 — 그 줄만 제거한다.
    //    (구문이 깨진 잘린 줄이 아니라 아예 없는 경우도 같은 결과여야 한다: 체크포인트가
    //    chunkIndex 개수 불일치를 관측한다.)
    List<String> partiallyWritten = new ArrayList<>();
    partiallyWritten.add(linesAfterFirstRun.get(0)); // docA 청크 0
    partiallyWritten.add(linesAfterFirstRun.get(2)); // docB 청크 0
    partiallyWritten.add(linesAfterFirstRun.get(3)); // docB 청크 1
    Files.write(embeddingsFile, partiallyWritten, StandardCharsets.UTF_8);

    // ③ 재실행한다.
    var secondRunClient = new RecordingClient();
    int secondWritten =
        EmbedKnowledgeCommand.embedToFile(documentsFile, embeddingsFile, secondRunClient, "abc1234");

    // docA는 청크 1개만 관측돼(기대 2개) 미완료로 판정되어 전체(2개) 재임베딩된다.
    // docB는 청크 2개 모두 관측돼 완료로 판정되어 재임베딩되지 않는다(배치 호출 자체가 없다).
    assertThat(secondRunClient.batchSizes).containsExactly(2);
    assertThat(secondWritten).isEqualTo(2);
    List<String> finalLines = Files.readAllLines(embeddingsFile, StandardCharsets.UTF_8);
    assertThat(finalLines).hasSize(5); // 잔여 3줄(docA 1 + docB 2) + 재실행 2줄(docA)
  }

  private void writeJsonl(Path path, com.fasterxml.jackson.databind.ObjectMapper mapper,
      KnowledgeDoc... docs) throws IOException {
    var sb = new StringBuilder();
    for (KnowledgeDoc doc : docs) {
      sb.append(mapper.writeValueAsString(doc)).append('\n');
    }
    Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
  }
}
