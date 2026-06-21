package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
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
class ContentEmbeddingMatcherSeedTest {

  @Autowired ContentEmbeddingMatcher matcher;
  @Autowired JdbcTemplate jdbc;

  @Test
  void matcherReturnsPublishedContentFromRequestedTrackOnly() {
    var result = matcher.match("DEVOPS", Collections.nCopies(768, 0.08), 5);

    assertThat(result).hasSize(5);
    assertThat(result).allSatisfy(match -> {
      assertThat(match.slug()).startsWith("devops-");
      String status = jdbc.queryForObject(
          "select status from contents where id = ?", String.class, match.contentId());
      assertThat(status).isEqualTo("PUBLISHED");
    });
  }
}
