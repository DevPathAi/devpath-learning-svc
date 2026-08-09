package ai.devpath.learning.knowledge;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 지식베이스 HNSW 코사인 검색. ContentEmbeddingMatcher와 같은 형태지만 chunk_text를 함께 준다.
 *
 * <p>{@code @Component}(not {@code @Repository}): 이 앱에 JPA 리포지토리가 있어
 * {@code PersistenceExceptionTranslationPostProcessor}가 활성화되어 있다. {@code @Repository}로
 * 선언하면 이 클래스가 던지는 {@link IllegalArgumentException}이 Spring의 JPA 예외 변환
 * ({@code HibernateJpaDialect#translateExceptionIfPossible})을 거쳐
 * {@code InvalidDataAccessApiUsageException}으로 감싸져, 호출자가 기대하는
 * {@code IllegalArgumentException} 타입이 아니게 된다(실측 확인). 기존 {@code ContentEmbeddingMatcher}가
 * 같은 이유로 커스텀 {@code PathContractException}을 쓰는 것과 같은 회피다.
 */
@Component
public class KnowledgeEmbeddingMatcher {

  private static final int DIMENSIONS = 768;

  private final JdbcTemplate jdbc;

  public KnowledgeEmbeddingMatcher(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<KnowledgeChunk> search(List<Double> queryEmbedding, int limit) {
    String vector = toVectorLiteral(queryEmbedding);
    String sql = """
        select d.doc_key, d.title, d.category, ke.chunk_text,
               ke.embedding <=> cast(? as vector) as distance
        from knowledge_embeddings ke
        join knowledge_documents d on d.id = ke.document_id
        where ke.status = 'ACTIVE'
          and d.status = 'ACTIVE'
        order by ke.embedding <=> cast(? as vector), d.id desc
        limit ?
        """;
    return jdbc.query(sql, (rs, rowNum) -> new KnowledgeChunk(
        rs.getString("doc_key"),
        rs.getString("title"),
        rs.getString("category"),
        rs.getString("chunk_text"),
        rs.getDouble("distance")), vector, vector, limit);
  }

  private String toVectorLiteral(List<Double> embedding) {
    if (embedding == null || embedding.size() != DIMENSIONS) {
      throw new IllegalArgumentException("embedding must be 768 dimensions");
    }
    var sb = new StringBuilder("[");
    for (int i = 0; i < embedding.size(); i++) {
      if (i > 0) sb.append(',');
      Double v = embedding.get(i);
      if (v == null || v.isNaN() || v.isInfinite()) {
        throw new IllegalArgumentException("embedding contains invalid value");
      }
      sb.append(v);
    }
    return sb.append(']').toString();
  }
}
