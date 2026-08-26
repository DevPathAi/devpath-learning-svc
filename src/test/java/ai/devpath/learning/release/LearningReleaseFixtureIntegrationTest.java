package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.path.CurrentMissionOutcome;
import ai.devpath.learning.path.CurrentMissionQueryRepository;
import ai.devpath.learning.path.LearningPathRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "devpath.release.enabled=true")
class LearningReleaseFixtureIntegrationTest {
  @Autowired LearningReleaseFixtureService fixtures;
  @Autowired LearningPathRepository paths;
  @Autowired CurrentMissionQueryRepository missions;
  @Autowired JdbcTemplate jdbc;

  @Test
  @Transactional
  void preparedFixtureIsARealIdempotentContentLinkedCurrentMission() {
    long userId = Math.abs(System.nanoTime());
    jdbc.update("""
        INSERT INTO contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        VALUES (?, 'Release fixture content', 'BACKEND_SPRING', '## body', 10, 0.4,
          'APPLY', '[]'::jsonb, 'PUBLISHED')
        """, "release-fixture-" + userId);
    Map<String, Object> expectedContent = jdbc.queryForMap("""
        SELECT id, slug
        FROM contents
        WHERE track='BACKEND_SPRING' AND status='PUBLISHED'
        ORDER BY id ASC
        LIMIT 1
        """);
    String candidate = "a".repeat(64);
    String run = "R".repeat(43);

    fixtures.prepare(candidate, run, userId);
    fixtures.prepare(candidate, run, userId);

    assertThat(paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE"))
        .isPresent();
    assertThat(jdbc.queryForObject(
        "SELECT count(*) FROM learning_paths WHERE user_id=? AND status='ACTIVE'",
        Integer.class, userId)).isEqualTo(1);
    var current = missions.findForUser(userId);
    assertThat(current.outcome()).isEqualTo(CurrentMissionOutcome.AVAILABLE);
    assertThat(current.nextTask()).isNotNull();
    assertThat(current.nextTask().contentId())
        .isEqualTo(((Number) expectedContent.get("id")).longValue());
    assertThat(current.nextTask().contentSlug()).isEqualTo(expectedContent.get("slug"));
  }
}
