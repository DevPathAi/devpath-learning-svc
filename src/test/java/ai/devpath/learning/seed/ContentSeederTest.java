package ai.devpath.learning.seed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ai.devpath.learning.contentgen.content.ContentQuota;
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
    // 150 은 ContentQuota 트랙 총량이 아니라 ContentSeeder.MD2_SEED_COUNT(운영 코드의
    // "완전히 시드됨" 판정 임계값, 이번 Task 에서 변경하지 않음)와 같은 값이다. 그 임계값의
    // >= 경계를 가장 엄격하게 시험하려면 정확히 그 값을 써야 하므로 트랙 수에서 유도하지
    // 않고 그대로 둔다.
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
  void emptyDbLoadsAllTracks() {
    new ContentSeeder(contentRepository, dataSource).run();

    long expectedContentCount = (long) ContentQuota.TRACKS.size() * ContentQuota.PER_TRACK;
    assertThat(contentRepository.count()).isEqualTo(expectedContentCount);
    Integer embeddings = jdbc.queryForObject(
        "select count(*) from content_embeddings", Integer.class);
    // ContentChunker가 콘텐츠를 다중 청크로 분할할 수 있어 임베딩 수는 콘텐츠 수 이상이다.
    assertThat(embeddings).isGreaterThanOrEqualTo((int) expectedContentCount);
  }
}
