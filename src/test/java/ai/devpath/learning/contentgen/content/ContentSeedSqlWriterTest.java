package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContentSeedSqlWriterTest {

  @Test
  void sqlOutputIsDeterministicAndUsesSlugLookupForEmbeddings() {
    var writer = new ContentSeedSqlWriter();
    var first = content("frontend-react-b", "FRONTEND_REACT");
    var second = content("backend-spring-a", "BACKEND_SPRING");
    var embeddings = List.of(
        embedding(first.slug(), 0, "b".repeat(64)),
        embedding(second.slug(), 0, "a".repeat(64)));

    var sqlA = writer.toSql(List.of(first, second), embeddings);
    var sqlB = writer.toSql(List.of(second, first), embeddings);

    assertThat(sqlA).isEqualTo(sqlB);
    assertThat(sqlA).contains("INSERT INTO contents");
    assertThat(sqlA).contains("concept_tags, status");
    assertThat(sqlA).contains("::jsonb");
    assertThat(sqlA).contains("INSERT INTO content_embeddings");
    assertThat(sqlA).contains("SELECT c.id");
    assertThat(sqlA).contains("WHERE c.slug = 'backend-spring-a'");
    assertThat(sqlA).contains("'[0.100000");
    assertThat(sqlA).contains("::vector");
    assertThat(sqlA.indexOf("backend-spring-a")).isLessThan(sqlA.indexOf("frontend-react-b"));
  }

  private static ApprovedContent content(String slug, String track) {
    return new ApprovedContent(
        slug,
        slug,
        track,
        "INTRO",
        "## Goal\nRead the concept.",
        15,
        0.2,
        "UNDERSTAND",
        List.of(slug),
        "PUBLISHED");
  }

  private static EmbeddingRecord embedding(String slug, int index, String hash) {
    return new EmbeddingRecord(
        slug,
        index,
        "chunk text",
        Collections.nCopies(EmbeddingValidator.DIMENSIONS, 0.1),
        hash,
        "ACTIVE");
  }
}
