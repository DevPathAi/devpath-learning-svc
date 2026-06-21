package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = "TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/seed/content_md2_seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = "TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ContentSeedSqlTest {

  @Autowired JdbcTemplate jdbc;

  @Test
  void seedLoadsOneHundredFiftyPublishedContentsWithActiveEmbeddings() {
    Integer contentCount = jdbc.queryForObject("select count(*) from contents", Integer.class);
    Integer embeddingCount = jdbc.queryForObject(
        "select count(*) from content_embeddings", Integer.class);
    Integer nonPublished = jdbc.queryForObject(
        "select count(*) from contents where status <> 'PUBLISHED'", Integer.class);
    Integer nonActive = jdbc.queryForObject(
        "select count(*) from content_embeddings where status <> 'ACTIVE'", Integer.class);
    Integer duplicateSlugs = jdbc.queryForObject("""
        select count(*) from (
          select slug from contents group by slug having count(*) > 1
        ) dup
        """, Integer.class);
    Integer contentsWithoutEmbedding = jdbc.queryForObject("""
        select count(*)
        from contents c
        where not exists (
          select 1 from content_embeddings ce
          where ce.content_id = c.id and ce.status = 'ACTIVE'
        )
        """, Integer.class);

    assertThat(contentCount).isEqualTo(150);
    assertThat(embeddingCount).isEqualTo(150);
    assertThat(nonPublished).isZero();
    assertThat(nonActive).isZero();
    assertThat(duplicateSlugs).isZero();
    assertThat(contentsWithoutEmbedding).isZero();

    Map<String, Integer> byTrack = jdbc.query("""
        select track, count(*)::int as cnt
        from contents
        group by track
        """, (rs, rowNum) -> Map.entry(rs.getString("track"), rs.getInt("cnt")))
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    assertThat(byTrack).containsOnly(
        Map.entry("BACKEND_SPRING", 30),
        Map.entry("FRONTEND_REACT", 30),
        Map.entry("MOBILE_FLUTTER", 30),
        Map.entry("DEVOPS", 30),
        Map.entry("FULLSTACK", 30));

    Double distance = jdbc.queryForObject("""
        select embedding <=> cast(? as vector)
        from content_embeddings
        limit 1
        """, Double.class, vectorLiteral(0.1));
    assertThat(distance).isNotNull().isGreaterThanOrEqualTo(0.0);
  }

  @Test
  void md2ContentSeedResourceExistsForLocalLoading() {
    assertThat(getClass().getResource("/db/seed/content_md2_seed.sql")).isNotNull();
  }

  private String vectorLiteral(double value) {
    return "[" + String.join(",", java.util.Collections.nCopies(768, String.valueOf(value))) + "]";
  }
}
