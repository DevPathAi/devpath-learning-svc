package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import tools.jackson.databind.json.JsonMapper;

class LearningPathPersistenceServiceTest {

  @Test
  void persistTranslatesUniqueViolationToActivePathConflict() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    OutboxRepository outbox = mock(OutboxRepository.class);
    JsonMapper jsonMapper = JsonMapper.builder().build();
    when(paths.saveAndFlush(any(LearningPath.class)))
        .thenThrow(new DataIntegrityViolationException("uq learning_paths active user"));
    var service = new LearningPathPersistenceService(paths, outbox, jsonMapper);

    var diagnosis = new LatestDiagnosis(1L, "BACKEND_SPRING", "JUNIOR",
        List.of(), List.of(), 0.9);
    var aiResult = new PathGenerateResult("rationale", List.of());
    var generated = new GeneratedLearningPath(diagnosis, aiResult, List.of());

    assertThatThrownBy(() -> service.persist(42L, generated))
        .isInstanceOf(ActivePathConflictException.class)
        .hasMessageContaining("PATH_GENERATION_CONFLICT");
  }
}
