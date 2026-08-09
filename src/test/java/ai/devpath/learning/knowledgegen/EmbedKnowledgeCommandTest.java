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

  private void writeJsonl(Path path, com.fasterxml.jackson.databind.ObjectMapper mapper,
      KnowledgeDoc... docs) throws IOException {
    var sb = new StringBuilder();
    for (KnowledgeDoc doc : docs) {
      sb.append(mapper.writeValueAsString(doc)).append('\n');
    }
    Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
  }
}
