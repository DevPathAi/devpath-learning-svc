package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class KnowledgeLoaderTest {

  @Autowired JdbcTemplate jdbc;

  private final KnowledgeLoader loader = new KnowledgeLoader();

  @BeforeEach
  void reset() {
    jdbc.execute("TRUNCATE knowledge_embeddings, knowledge_documents RESTART IDENTITY CASCADE");
  }

  private KnowledgeEmbeddingRecord record(String docKey, int chunkIndex, String docHash) {
    return new KnowledgeEmbeddingRecord(docKey, "제목 " + docKey, "AWS", docHash,
        chunkIndex, "청크 본문 " + chunkIndex, Collections.nCopies(768, 0.1),
        "chunk-hash-" + docKey + "-" + chunkIndex, "commit1");
  }

  @Test
  void insertsDocumentsAndChunks() {
    int loaded = loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "h1"), record("AWS/a.md", 1, "h1"),
        record("MSA/b.md", 0, "h2")), "develop-study-documents");

    assertThat(loaded).isEqualTo(3);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_documents", Integer.class))
        .isEqualTo(2);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(3);
    assertThat(jdbc.queryForObject(
        "select category from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("AWS");
  }

  @Test
  void reloadingSameDataProducesNoDuplicates() {
    List<KnowledgeEmbeddingRecord> records =
        List.of(record("AWS/a.md", 0, "h1"), record("AWS/a.md", 1, "h1"));

    loader.load(jdbc, records, "repo");
    loader.load(jdbc, records, "repo");

    assertThat(jdbc.queryForObject("select count(*) from knowledge_documents", Integer.class))
        .isEqualTo(1);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(2);
  }

  @Test
  void changedDocumentReplacesItsChunks() {
    loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "old"), record("AWS/a.md", 1, "old")), "repo");

    // 개정판은 청크가 1개로 줄었다
    loader.load(jdbc, List.of(record("AWS/a.md", 0, "new")), "repo");

    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(1);
    assertThat(jdbc.queryForObject(
        "select doc_hash from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("new");
  }

  @Test
  void storesEmbeddingAsQueryableVector() {
    loader.load(jdbc, List.of(record("AWS/a.md", 0, "h1")), "repo");

    Double distance = jdbc.queryForObject("""
        select embedding <=> cast(? as vector) from knowledge_embeddings limit 1
        """, Double.class, "[" + String.join(",", Collections.nCopies(768, "0.1")) + "]");

    assertThat(distance).isNotNull().isLessThan(0.0001);
  }

  /**
   * Task 5의 EmbedKnowledgeCommand는 배치마다 append+flush한다. 중단 후 재실행하면 부분 기록 위에
   * 재실행분이 그대로 append돼, embeddings.jsonl 한 파일 안에 같은 (docKey, chunkIndex)가
   * 두 번 이상 나타날 수 있다(앞=중단된 부분 기록, 뒤=재실행이 만든 완전한 기록).
   * load()는 이를 dedup해 뒤에 온 레코드를 채택해야 하고, 문서 행 값도 그 문서의
   * 마지막 레코드 기준이어야 한다 — 그렇지 않으면 uq_ke_doc_chunk UNIQUE 제약을 위반해
   * 적재 전체가 예외로 죽는다.
   */
  @Test
  void deduplicatesRepeatedChunkKeepingLastOccurrenceAndUsesLastDocumentValues() {
    KnowledgeEmbeddingRecord partialRunRemnant = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md (구)", "AWS", "old-hash", 0,
        "옛 청크 본문", Collections.nCopies(768, 0.1), "chunk-hash-old", "commit-old");
    KnowledgeEmbeddingRecord fullRerun = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md (신)", "AWS", "new-hash", 0,
        "새 청크 본문", Collections.nCopies(768, 0.1), "chunk-hash-new", "commit-new");

    int loaded = loader.load(jdbc, List.of(partialRunRemnant, fullRerun), "repo");

    assertThat(loaded).isEqualTo(1);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_documents", Integer.class))
        .isEqualTo(1);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(1);
    assertThat(jdbc.queryForObject(
        "select chunk_text from knowledge_embeddings limit 1", String.class))
        .isEqualTo("새 청크 본문");
    assertThat(jdbc.queryForObject(
        "select doc_hash from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("new-hash");
  }

  /**
   * lastByDoc은 chunksByDoc(청크 인덱스별 dedup 맵)과 독립적으로, 원본 입력 리스트를 그대로
   * 순회하며 도출해야 한다. 만약 lastByDoc을 chunksByDoc에서 파생시키면(예: 그 문서의
   * chunksByDoc.values() 중 마지막 항목을 쓰면), LinkedHashMap은 키의 "최초 삽입 위치"를
   * 유지하므로 반복 순서가 "각 chunkIndex가 파일에서 마지막으로 갱신된 순서"가 아니라
   * "각 chunkIndex가 파일에서 처음 등장한 순서"가 된다. 이 테스트는 그 회귀를 잡는다:
   * chunkIndex 1(구버전)이 chunkIndex 0(신버전, 파일에서 실제로 더 나중에 옴)보다 먼저
   * 삽입되므로, chunksByDoc에서 파생하면 "마지막 값"이 chunkIndex 1(옛 값)이 되어 오답이 된다.
   */
  @Test
  void deduplicationUsesTrulyLastRecordAcrossMultipleChunkIndices() {
    int loaded = loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "old"),
        record("AWS/a.md", 1, "old"),
        record("AWS/a.md", 0, "new")), "repo");

    assertThat(loaded).isEqualTo(2);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(2);
    assertThat(jdbc.queryForObject(
        "select doc_hash from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("new");
  }

  /**
   * 문서 단위 트랜잭션 검증. 먼저 문서를 정상 적재한 뒤, 같은 문서를 재적재하되 두 번째 청크의
   * 임베딩 차원이 잘못돼(toVectorLiteral이 거부) 예외가 나는 상황을 재현한다. load()는
   * IllegalArgumentException을 그대로 전파해야 하고(fail-loud), 그 문서에 대해 이미 실행된
   * DB 변경(문서 행 upsert·기존 청크 delete·첫 청크 insert)은 전부 롤백돼 재적재 이전 상태가
   * 그대로 보존돼야 한다 — 그렇지 않으면 그 문서가 청크 0개 또는 일부만 있는 상태로 남는다.
   */
  @Test
  void rollsBackFailedDocumentPreservingPreviousChunks() {
    loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "h1"), record("AWS/a.md", 1, "h1")), "repo");

    KnowledgeEmbeddingRecord invalidEmbeddingChunk = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md", "AWS", "h2", 1,
        "깨진 청크", Collections.nCopies(10, 0.1), "chunk-hash-bad", "commit2");

    assertThatThrownBy(() -> loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "h2"), invalidEmbeddingChunk), "repo"))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(2);
    assertThat(jdbc.queryForObject(
        "select doc_hash from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("h1");
  }
}
