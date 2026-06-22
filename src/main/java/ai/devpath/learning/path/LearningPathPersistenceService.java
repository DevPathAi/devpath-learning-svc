package ai.devpath.learning.path;

import ai.devpath.learning.outbox.OutboxEntry;
import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.shared.event.LearningPathGeneratedEvent;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Service
public class LearningPathPersistenceService {
  private static final String PROMPT_VERSION = "path-v1";
  private static final String EMBEDDING_VERSION = "nomic-embed-text";

  private final LearningPathRepository paths;
  private final OutboxRepository outbox;
  private final JsonMapper jsonMapper;

  public LearningPathPersistenceService(LearningPathRepository paths, OutboxRepository outbox,
      JsonMapper jsonMapper) {
    this.paths = paths;
    this.outbox = outbox;
    this.jsonMapper = jsonMapper;
  }

  @Transactional
  public LearningPath persist(long userId, GeneratedLearningPath generated) {
    paths.archiveActiveByUserId(userId);

    LearningPath path = new LearningPath();
    path.setUserId(userId);
    path.setGeneratedAt(Instant.now());
    path.setTrack(generated.diagnosis().track());
    path.setTotalWeeks(12);
    path.setGenPromptVersion(PROMPT_VERSION);
    path.setSourceEmbeddingVersion(EMBEDDING_VERSION);
    path.setStatus("ACTIVE");
    path.setAiRationale(generated.aiResult().rationale());

    for (GeneratedLearningPath.GeneratedMilestone generatedMilestone : generated.milestones()) {
      var source = generatedMilestone.source();
      PathMilestone milestone = new PathMilestone();
      milestone.setWeekNum(source.weekNum());
      milestone.setTitle(source.title());
      milestone.setGoalDescription(source.goalDescription());
      milestone.setTargetSkills(writeJson(source.targetSkills()));
      milestone.setEstimatedHours(source.estimatedHours());
      milestone.setWhyThisOrder(source.whyThisOrder());
      milestone.setExpectedOutcome(source.expectedOutcome());
      for (GeneratedLearningPath.GeneratedTask generatedTask : generatedMilestone.tasks()) {
        var sourceTask = generatedTask.source();
        PathWeeklyTask task = new PathWeeklyTask();
        task.setOrderNum(sourceTask.orderNum());
        task.setTaskType(sourceTask.taskType());
        task.setTitle(sourceTask.title());
        task.setRequired(sourceTask.required());
        task.setContentId(generatedTask.contentId());
        milestone.addTask(task);
      }
      path.addMilestone(milestone);
    }

    LearningPath saved;
    try {
      saved = paths.saveAndFlush(path);
    } catch (DataIntegrityViolationException e) {
      if (isUniqueViolation(e)) {
        throw new ActivePathConflictException(
            "PATH_GENERATION_CONFLICT: an active learning path already exists for user " + userId, e);
      }
      throw e;
    }
    publishGenerated(saved);
    return saved;
  }

  /** PostgreSQL unique_violation(SQL state 23505)만 충돌로 본다. FK·NOT NULL 등은 원래대로 전파. */
  private static boolean isUniqueViolation(DataIntegrityViolationException e) {
    for (Throwable cause = e; cause != null; cause = cause.getCause()) {
      if (cause instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
        return true;
      }
    }
    return false;
  }

  private void publishGenerated(LearningPath path) {
    var event = new LearningPathGeneratedEvent(UUID.randomUUID(), Instant.now(),
        path.getUserId(), path.getId(), path.getTrack());
    OutboxEntry entry = new OutboxEntry();
    entry.setAggregateType("learning_path");
    entry.setAggregateId(String.valueOf(path.getId()));
    entry.setEventType(LearningPathGeneratedEvent.EVENT_TYPE);
    entry.setPayload(writeJson(event));
    entry.setCreatedAt(Instant.now());
    outbox.save(entry);
  }

  private String writeJson(Object value) {
    try {
      return jsonMapper.writeValueAsString(value);
    } catch (Exception e) {
      throw new PathContractException("path JSON serialization failed", e);
    }
  }
}
