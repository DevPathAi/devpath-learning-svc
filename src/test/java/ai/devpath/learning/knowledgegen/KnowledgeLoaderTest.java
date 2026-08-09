package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;

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
}
