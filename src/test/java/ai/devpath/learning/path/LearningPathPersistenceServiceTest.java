package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import tools.jackson.databind.json.JsonMapper;

class LearningPathPersistenceServiceTest {

  private final JsonMapper jsonMapper = JsonMapper.builder().build();

  private GeneratedLearningPath sampleGenerated() {
    var diagnosis = new LatestDiagnosis(1L, "BACKEND_SPRING", "JUNIOR",
        List.of(), List.of(), 0.9);
    var aiResult = new PathGenerateResult("rationale", List.of());
    return new GeneratedLearningPath(diagnosis, aiResult, List.of());
  }

  @Test
  void persistTranslatesUniqueViolationToActivePathConflict() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    OutboxRepository outbox = mock(OutboxRepository.class);
    when(paths.saveAndFlush(any(LearningPath.class)))
        .thenThrow(new DataIntegrityViolationException(
            "duplicate key", new SQLException("duplicate", "23505")));
    var service = new LearningPathPersistenceService(paths, outbox, jsonMapper);

    assertThatThrownBy(() -> service.persist(42L, sampleGenerated()))
        .isInstanceOf(ActivePathConflictException.class)
        .hasMessageContaining("PATH_GENERATION_CONFLICT");
  }

  @Test
  void persistRethrowsNonUniqueIntegrityViolation() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    OutboxRepository outbox = mock(OutboxRepository.class);
    when(paths.saveAndFlush(any(LearningPath.class)))
        .thenThrow(new DataIntegrityViolationException(
            "not-null violation", new SQLException("null value", "23502")));
    var service = new LearningPathPersistenceService(paths, outbox, jsonMapper);

    assertThatThrownBy(() -> service.persist(42L, sampleGenerated()))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
