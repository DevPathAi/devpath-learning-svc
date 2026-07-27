package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ActiveLearnerRepositoryTest {

  @Autowired ActiveLearnerRepository repo;
  @Autowired JdbcTemplate jdbc;

  @Test
  void returnsOnlyActivePathUsers() {
    long activeUserId = 777201L;
    long archivedUserId = 777202L;
    jdbc.update("INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status) VALUES (?, now(), 'BACKEND_SPRING', 12, 'ACTIVE')", activeUserId);
    jdbc.update("INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status) VALUES (?, now(), 'BACKEND_SPRING', 12, 'ARCHIVED')", archivedUserId);

    assertThat(repo.activeLearnerUserIds()).contains(activeUserId).doesNotContain(archivedUserId);
  }
}
