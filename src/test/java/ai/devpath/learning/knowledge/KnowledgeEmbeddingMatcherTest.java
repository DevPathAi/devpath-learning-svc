package ai.devpath.learning.knowledge;

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
class KnowledgeEmbeddingMatcherTest {

  @Autowired KnowledgeEmbeddingMatcher matcher;
  @Autowired JdbcTemplate jdbc;

  @BeforeEach
  void reset() {
    jdbc.execute("TRUNCATE knowledge_embeddings, knowledge_documents RESTART IDENTITY CASCADE");
  }

  @Test
  void returnsChunkTextSoItCanBeInjectedIntoThePrompt() {
    seed("AWS/a.md", "AWS 개념", "AWS", "Pod Identity는 Fargate를 지원하지 않는다", 0.10, "ACTIVE", "ACTIVE");

    List<KnowledgeChunk> result = matcher.search(Collections.nCopies(768, 0.10), 3);

    assertThat(result).singleElement().satisfies(c -> {
      assertThat(c.chunkText()).isEqualTo("Pod Identity는 Fargate를 지원하지 않는다");
      assertThat(c.docKey()).isEqualTo("AWS/a.md");
      assertThat(c.title()).isEqualTo("AWS 개념");
      assertThat(c.category()).isEqualTo("AWS");
    });
  }

  @Test
  void ordersByCosineDistance() {
    // near = 질의와 완전히 동일한 방향 → 코사인 거리 0.
    seed("AWS/near.md", "가까움", "AWS", "가까운 청크", 0.10, "ACTIVE", "ACTIVE");
    // far = 앞 384차원은 질의와 같고 뒤 384차원은 부호 반전 → 내적이 0이 되어 질의와 직교(코사인 거리 1).
    // 원래 브리프의 far(전 차원 0.90, 즉 near의 스칼라배)는 코사인 거리가 near와 마찬가지로 정확히 0이 되어
    // (방향이 같으면 크기와 무관) 이 테스트가 항상 실패했다(psql로 실측: near_dist=0, far_dist=0).
    // 이 값(뒤 384차원 부호 반전)은 psql로 실측 확인함: near_dist=0, far_dist≈0.9999999976(≈1).
    seedHalfFlipped("AWS/far.md", "멂", "AWS", "먼 청크", "ACTIVE", "ACTIVE");

    List<KnowledgeChunk> result = matcher.search(Collections.nCopies(768, 0.10), 3);

    assertThat(result).extracting(KnowledgeChunk::docKey)
        .containsExactly("AWS/near.md", "AWS/far.md");
    assertThat(result.get(0).distance()).isLessThan(result.get(1).distance());
    // 순서만이 아니라 실제 값도 단언한다 — 두 거리가 우연히 같아져도(예: 둘 다 0) tie-break로 순서만 맞는
    // green을 막기 위함(near_dist=0, far_dist≈1, psql 실측 근거는 리포트 참조).
    assertThat(result.get(0).distance()).isLessThan(1e-6);
    assertThat(result.get(1).distance()).isGreaterThan(0.5);
  }

  @Test
  void respectsLimit() {
    for (int i = 0; i < 5; i++) {
      seed("AWS/" + i + ".md", "제목", "AWS", "청크 " + i, 0.10, "ACTIVE", "ACTIVE");
    }

    assertThat(matcher.search(Collections.nCopies(768, 0.10), 2)).hasSize(2);
  }

  @Test
  void excludesInactiveDocumentsAndChunks() {
    seed("AWS/inactive-doc.md", "제목", "AWS", "청크", 0.10, "INACTIVE", "ACTIVE");
    seed("AWS/inactive-chunk.md", "제목", "AWS", "청크", 0.10, "ACTIVE", "INACTIVE");
    seed("AWS/ok.md", "제목", "AWS", "청크", 0.10, "ACTIVE", "ACTIVE");

    List<KnowledgeChunk> result = matcher.search(Collections.nCopies(768, 0.10), 10);

    assertThat(result).extracting(KnowledgeChunk::docKey).containsExactly("AWS/ok.md");
  }

  @Test
  void rejectsWrongDimension() {
    assertThatThrownBy(() -> matcher.search(Collections.nCopies(512, 0.1), 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("768");
  }

  private void seed(String docKey, String title, String category, String chunkText,
      double value, String docStatus, String chunkStatus) {
    Long id = jdbc.queryForObject("""
        insert into knowledge_documents(doc_key, title, category, source_repo, source_commit,
          doc_hash, status)
        values (?, ?, ?, 'repo', 'commit', 'hash-' || ?, ?)
        returning id
        """, Long.class, docKey, title, category, docKey, docStatus);
    String vector = "[" + String.join(",", Collections.nCopies(768, Double.toString(value))) + "]";
    jdbc.update("""
        insert into knowledge_embeddings(document_id, chunk_index, chunk_text, embedding,
          chunk_hash, status)
        values (?, 0, ?, cast(? as vector), 'ch', ?)
        """, id, chunkText, vector, chunkStatus);
  }

  /**
   * 앞 384차원은 +0.1, 뒤 384차원은 -0.1인 벡터를 시드한다. 질의(전 차원 +0.1)와의 내적이 0이 되어
   * 코사인 거리 1(직교)이 나온다 — {@code seed}의 균일 벡터(스칼라배라 방향이 같아 코사인 거리가
   * 항상 0이 되는 문제)와 달리 실제로 다른 방향을 표현하기 위한 전용 헬퍼.
   */
  private void seedHalfFlipped(String docKey, String title, String category, String chunkText,
      String docStatus, String chunkStatus) {
    Long id = jdbc.queryForObject("""
        insert into knowledge_documents(doc_key, title, category, source_repo, source_commit,
          doc_hash, status)
        values (?, ?, ?, 'repo', 'commit', 'hash-' || ?, ?)
        returning id
        """, Long.class, docKey, title, category, docKey, docStatus);
    var parts = new java.util.ArrayList<String>(768);
    for (int i = 0; i < 768; i++) {
      parts.add(i < 384 ? "0.1" : "-0.1");
    }
    String vector = "[" + String.join(",", parts) + "]";
    jdbc.update("""
        insert into knowledge_embeddings(document_id, chunk_index, chunk_text, embedding,
          chunk_hash, status)
        values (?, 0, ?, cast(? as vector), 'ch', ?)
        """, id, chunkText, vector, chunkStatus);
  }
}
