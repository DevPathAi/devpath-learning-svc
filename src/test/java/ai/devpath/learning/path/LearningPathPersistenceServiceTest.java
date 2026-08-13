package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.ai.PathGenerateResult;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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

  /**
   * 재진단(트랙 갈아타기)이 성립하는 조건 — persist() 는 새 ACTIVE 를 넣기 전에
   * 옛 ACTIVE 를 내려야 한다. 이 호출이 사라지면 uq_learning_paths_active_user 위반으로
   * 재진단이 깨진다. LearningPathArchiveOnSwitchTest 는 쿼리 자체만 보므로,
   * 「persist() 가 그 쿼리를 부른다」는 여기서 고정한다.
   */
  @Test
  void persistArchivesActivePathBeforeInsertingNewOne() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    OutboxRepository outbox = mock(OutboxRepository.class);
    LearningPath saved = mock(LearningPath.class);
    when(saved.getId()).thenReturn(7L);
    when(saved.getUserId()).thenReturn(42L);
    when(paths.saveAndFlush(any(LearningPath.class))).thenReturn(saved);
    var service = new LearningPathPersistenceService(paths, outbox, jsonMapper);

    service.persist(42L, sampleGenerated());

    InOrder order = inOrder(paths);
    order.verify(paths).archiveActiveByUserId(42L);
    order.verify(paths).saveAndFlush(any(LearningPath.class));
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
