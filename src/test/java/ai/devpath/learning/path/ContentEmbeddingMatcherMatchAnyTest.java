package ai.devpath.learning.path;

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
class ContentEmbeddingMatcherMatchAnyTest {

  @Autowired ContentEmbeddingMatcher matcher;
  @Autowired JdbcTemplate jdbc;

  @BeforeEach
  void reset() {
    jdbc.execute("TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE");
  }

  @Test
  void matchAnyReturnsPublishedContentAcrossTracks() {
    seed("be-1", "BACKEND_SPRING", 0.10);
    seed("fe-1", "FRONTEND_REACT", 0.11);
    seed("be-2", "BACKEND_SPRING", 0.12);

    List<MatchedContent> result = matcher.matchAny(Collections.nCopies(768, 0.10), 5);

    assertThat(result).hasSize(3);
    assertThat(result).allSatisfy(m -> {
      String status = jdbc.queryForObject(
          "select status from contents where id = ?", String.class, m.contentId());
      assertThat(status).isEqualTo("PUBLISHED");
    });
  }

  @Test
  void matchAnyIsNotRestrictedToASingleTrack() {
    seed("be-1", "BACKEND_SPRING", 0.10);
    seed("fe-1", "FRONTEND_REACT", 0.11);

    List<MatchedContent> result = matcher.matchAny(Collections.nCopies(768, 0.10), 50);

    long distinctTracks = result.stream()
        .map(m -> jdbc.queryForObject(
            "select track from contents where id = ?", String.class, m.contentId()))
        .distinct()
        .count();
    assertThat(distinctTracks).isGreaterThan(1);
  }

  @Test
  void matchAnyExcludesNonPublishedOrInactive() {
    long draft = seedRaw("draft-1", "BACKEND_SPRING", 0.10, "DRAFT", "ACTIVE");
    long inactive = seedRaw("inactive-1", "BACKEND_SPRING", 0.10, "PUBLISHED", "INACTIVE");
    seed("ok-1", "BACKEND_SPRING", 0.10);

    List<MatchedContent> result = matcher.matchAny(Collections.nCopies(768, 0.10), 50);

    assertThat(result).extracting(MatchedContent::contentId)
        .doesNotContain(draft, inactive)
        .hasSize(1);
  }

  private long seed(String slug, String track, double value) {
    return seedRaw(slug, track, value, "PUBLISHED", "ACTIVE");
  }

  private long seedRaw(String slug, String track, double value, String contentStatus,
      String embeddingStatus) {
    long id = jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, '## Body', 10, 0.4, 'APPLY', cast('["tag"]' as jsonb), ?)
        returning id
        """, Long.class, slug, slug, track, contentStatus);
    String vector = "[" + String.join(",", Collections.nCopies(768, Double.toString(value))) + "]";
    jdbc.update("""
        insert into content_embeddings(content_id, chunk_index, chunk_text, embedding, chunk_hash, status)
        values (?, 0, 'chunk', cast(? as vector), 'hash-' || ?, ?)
        """, id, vector, slug, embeddingStatus);
    return id;
  }
}
