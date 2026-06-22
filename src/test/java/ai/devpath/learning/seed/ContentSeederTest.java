package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.ContentRepository;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
class ContentSeederTest {

  @Autowired ContentRepository contentRepository;
  @Autowired DataSource dataSource;
  @Autowired JdbcTemplate jdbc;

  @Test
  void partialSeedThrowsAndDoesNotTouchDataSource() {
    ContentRepository repo = mock(ContentRepository.class);
    DataSource ds = mock(DataSource.class);
    when(repo.count()).thenReturn(11L);
    var seeder = new ContentSeeder(repo, ds);

    assertThatThrownBy(seeder::run)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("partial seed");
    verifyNoInteractions(ds);
  }

  @Test
  void alreadySeededIsNoOp() {
    ContentRepository repo = mock(ContentRepository.class);
    DataSource ds = mock(DataSource.class);
    when(repo.count()).thenReturn(150L);
    var seeder = new ContentSeeder(repo, ds);

    seeder.run();

    verifyNoInteractions(ds);
  }

  @Test
  @Sql(statements = "TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE",
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(statements = "TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void emptyDbLoadsOneHundredFifty() {
    new ContentSeeder(contentRepository, dataSource).run();

    assertThat(contentRepository.count()).isEqualTo(150L);
    Integer embeddings = jdbc.queryForObject(
        "select count(*) from content_embeddings", Integer.class);
    assertThat(embeddings).isEqualTo(150);
  }
}
