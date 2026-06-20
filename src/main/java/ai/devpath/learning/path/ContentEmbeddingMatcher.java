package ai.devpath.learning.path;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ContentEmbeddingMatcher {
  private final JdbcTemplate jdbc;

  public ContentEmbeddingMatcher(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<MatchedContent> match(String track, List<Double> queryEmbedding, int limit) {
    String vector = toVectorLiteral(queryEmbedding);
    String sql = """
        select c.id, c.slug, c.title, ce.embedding <=> cast(? as vector) as distance
        from content_embeddings ce
        join contents c on c.id = ce.content_id
        where ce.status = 'ACTIVE'
          and c.status = 'PUBLISHED'
          and c.track = ?
        order by ce.embedding <=> cast(? as vector), c.id desc
        limit ?
        """;
    return jdbc.query(sql, (rs, rowNum) -> new MatchedContent(
        rs.getLong("id"),
        rs.getString("slug"),
        rs.getString("title"),
        rs.getDouble("distance")), vector, track, vector, limit);
  }

  private String toVectorLiteral(List<Double> embedding) {
    if (embedding == null || embedding.size() != 768) {
      throw new PathContractException("embedding must be 768 dimensions");
    }
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < embedding.size(); i++) {
      if (i > 0) sb.append(',');
      Double value = embedding.get(i);
      if (value == null || value.isNaN() || value.isInfinite()) {
        throw new PathContractException("embedding contains invalid value");
      }
      sb.append(value);
    }
    return sb.append(']').toString();
  }
}
