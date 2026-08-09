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
   * "각 chunkIndex가 처음 등장한 순서"가 된다. 이 테스트는 그 회귀를 잡는다: chunkIndex 0이
   * 먼저 삽입되고, chunkIndex 1이 그다음 삽입되며, chunkIndex 0이 리스트 맨 끝에서 다시
   * 갱신된다(진짜 마지막 레코드인데도 맵 위치는 여전히 맨 앞). chunksByDoc에서 파생하면
   * "마지막 값"이 chunkIndex 1(중간에 삽입되고 그 뒤로 갱신되지 않은 값)이 되어 오답이 된다.
   *
   * <p>★discriminator는 docHash가 아니라 sourceCommit이다★ — I2 수정(KnowledgeLoader 참고) 이후
   * 같은 docKey에서 docHash가 바뀌면 그 문서의 누적 청크를 통째로 비우므로, docHash를 바꿔
   * "어느 레코드가 이겼는지"를 구분하면 이 테스트가 그 clear 로직까지 함께 건드리게 되어 청크
   * 개수 자체가 달라진다(이 테스트가 원래 잡으려던 회귀와 다른 메커니즘이 섞인다). 그래서
   * docHash는 "h1"로 고정해 clear를 트리거하지 않고, lastByDoc이 쓰는 다른 문서 행 필드인
   * sourceCommit으로 "진짜 마지막 레코드"를 구분한다 — 같은 회귀를 정확히 잡으면서 I2 경계와
   * 무관하다.
   */
  @Test
  void deduplicationUsesTrulyLastRecordAcrossMultipleChunkIndices() {
    KnowledgeEmbeddingRecord chunk0First = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md", "AWS", "h1", 0,
        "청크0 최초", Collections.nCopies(768, 0.1), "chunk-hash-0a", "commit-first");
    KnowledgeEmbeddingRecord chunk1Only = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md", "AWS", "h1", 1,
        "청크1", Collections.nCopies(768, 0.1), "chunk-hash-1", "commit-middle");
    KnowledgeEmbeddingRecord chunk0UpdatedLast = new KnowledgeEmbeddingRecord(
        "AWS/a.md", "제목 AWS/a.md", "AWS", "h1", 0,
        "청크0 갱신", Collections.nCopies(768, 0.1), "chunk-hash-0b", "commit-last");

    int loaded = loader.load(jdbc, List.of(chunk0First, chunk1Only, chunk0UpdatedLast), "repo");

    assertThat(loaded).isEqualTo(2);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(2);
    assertThat(jdbc.queryForObject(
        "select source_commit from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("commit-last");
    assertThat(jdbc.queryForObject(
        "select chunk_text from knowledge_embeddings where chunk_index = 0", String.class))
        .isEqualTo("청크0 갱신");
  }

  /**
   * I2 회귀 테스트. embedKnowledge가 중단 없이 완주해도, 두 번째(증분) 실행에서 문서가 개정돼
   * 청크 수가 줄면(docHash 변경) embeddings.jsonl에는 옛 버전 전체 뒤에 새 버전 전체가 그대로
   * append된다 — dedup 키가 (docKey, chunkIndex)뿐이면 새 버전에 없는 옛 chunkIndex(여기서는
   * 2)가 대응하는 신버전 레코드가 없어 옛 본문 그대로 살아남는다. 한 번의 load() 호출 안에
   * 옛 버전(청크 3개) 전체 뒤에 새 버전(청크 2개) 전체가 오는 상황을 재현해, 살아남는 청크가
   * 정확히 새 버전 것만이어야 함을 단언한다.
   */
  @Test
  void docHashChangeDropsStaleChunksFromPreviousVersion() {
    int loaded = loader.load(jdbc, List.of(
        record("AWS/a.md", 0, "old"),
        record("AWS/a.md", 1, "old"),
        record("AWS/a.md", 2, "old"),
        record("AWS/a.md", 0, "new"),
        record("AWS/a.md", 1, "new")), "repo");

    assertThat(loaded).isEqualTo(2);
    assertThat(jdbc.queryForObject("select count(*) from knowledge_embeddings", Integer.class))
        .isEqualTo(2);
    assertThat(jdbc.queryForObject(
        "select doc_hash from knowledge_documents where doc_key = 'AWS/a.md'", String.class))
        .isEqualTo("new");
    List<String> chunkTexts = jdbc.queryForList(
        "select chunk_text from knowledge_embeddings order by chunk_index", String.class);
    assertThat(chunkTexts).containsExactly("청크 본문 0", "청크 본문 1");
    // index 2는 신버전에 없다 — 옛 본문이 남아 있으면 이 테스트가 그것을 잡는다.
    assertThat(jdbc.queryForObject(
        "select count(*) from knowledge_embeddings where chunk_index = 2", Integer.class))
        .isZero();
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
