package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmbedKnowledgeCommandTest {

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

    List<KnowledgeEmbeddingRecord> records =
        EmbedKnowledgeCommand.run(List.of(doc("AWS/a.md", 60)), client, "abc1234", Set.of());

    assertThat(records).hasSize(61);
    assertThat(client.batchSizes).containsExactly(50, 11);
  }

  @Test
  void carriesDocumentMetadataAndSourceCommitOntoEveryChunk() throws Exception {
    List<KnowledgeEmbeddingRecord> records =
        EmbedKnowledgeCommand.run(List.of(doc("AWS/a.md", 2)), new RecordingClient(),
            "deadbeef", Set.of());

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
        client, "abc", Set.of("hash-AWS/a.md"));

    assertThat(records).extracting(KnowledgeEmbeddingRecord::docKey).containsOnly("AWS/b.md");
  }

  @Test
  void batchesAcrossDocumentBoundaries() throws Exception {
    var client = new RecordingClient();

    // 문서 3개 × 청크 3개 = 9청크 → 한 배치로 묶여야 한다(문서별 9회가 아니라)
    EmbedKnowledgeCommand.run(
        List.of(doc("AWS/a.md", 2), doc("AWS/b.md", 2), doc("AWS/c.md", 2)),
        client, "abc", Set.of());

    assertThat(client.batchSizes).containsExactly(9);
  }
}
