package ai.devpath.learning.path;

import ai.devpath.learning.path.ai.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class LearningPathGenerationService {
  private final LatestDiagnosisRepository diagnoses;
  private final AiPathClient aiClient;
  private final ContentEmbeddingMatcher matcher;
  private final LearningPathPersistenceService persistence;

  public LearningPathGenerationService(LatestDiagnosisRepository diagnoses, AiPathClient aiClient,
      ContentEmbeddingMatcher matcher, LearningPathPersistenceService persistence) {
    this.diagnoses = diagnoses;
    this.aiClient = aiClient;
    this.matcher = matcher;
    this.persistence = persistence;
  }

  public LearningPath generate(long userId, String goal, Consumer<PathProgressEvent> progress) {
    progress.accept(PathProgressEvent.collecting());
    LatestDiagnosis diagnosis = diagnoses.findLatestCompleted(userId)
        .orElseThrow(() -> new NoCompletedAssessmentException(userId));

    progress.accept(PathProgressEvent.generating());
    PathGenerateResult aiResult = aiClient.generate(new PathGenerateCommand(
        diagnosis.track(),
        diagnosis.diagnosedLevel(),
        diagnosis.strengthConcepts(),
        diagnosis.weaknessConcepts(),
        goal));
    validate(aiResult);

    progress.accept(PathProgressEvent.matching());
    List<String> queryTexts = aiResult.milestones().stream().map(this::queryText).toList();
    List<List<Double>> embeddings = aiClient.embed(queryTexts);
    if (embeddings.size() != aiResult.milestones().size()) {
      throw new PathContractException("embed result count does not match milestone count");
    }

    List<GeneratedLearningPath.GeneratedMilestone> generatedMilestones = new ArrayList<>();
    for (int i = 0; i < aiResult.milestones().size(); i++) {
      var milestone = aiResult.milestones().get(i);
      List<MatchedContent> matched = matcher.match(diagnosis.track(), embeddings.get(i), 3);
      List<GeneratedLearningPath.GeneratedTask> generatedTasks = new ArrayList<>();
      List<PathGenerateResult.Task> tasks = milestone.tasks().stream().limit(3).toList();
      for (int j = 0; j < tasks.size(); j++) {
        Long contentId = j < matched.size() ? matched.get(j).contentId() : null;
        generatedTasks.add(new GeneratedLearningPath.GeneratedTask(tasks.get(j), contentId));
      }
      generatedMilestones.add(new GeneratedLearningPath.GeneratedMilestone(milestone, generatedTasks));
    }

    LearningPath path = persistence.persist(userId,
        new GeneratedLearningPath(diagnosis, aiResult, generatedMilestones));
    progress.accept(PathProgressEvent.done(path.getId()));
    return path;
  }

  private void validate(PathGenerateResult result) {
    if (result == null || isBlank(result.rationale()) || result.milestones() == null
        || result.milestones().isEmpty()) {
      throw new PathContractException("path generation result is incomplete");
    }
    for (PathGenerateResult.Milestone milestone : result.milestones()) {
      if (milestone.weekNum() <= 0 || isBlank(milestone.title()) || isBlank(milestone.expectedOutcome())
          || milestone.tasks() == null || milestone.tasks().size() < 3) {
        throw new PathContractException("milestone must include required fields and 3 tasks");
      }
      for (PathGenerateResult.Task task : milestone.tasks().stream().limit(3).toList()) {
        if (task.orderNum() <= 0 || isBlank(task.title()) || !validTaskType(task.taskType())) {
          throw new PathContractException("task contract violation");
        }
      }
    }
  }

  private String queryText(PathGenerateResult.Milestone milestone) {
    return String.join(" ", milestone.title(), milestone.goalDescription(),
        String.join(" ", milestone.targetSkills()));
  }

  private boolean validTaskType(String taskType) {
    return "READ".equals(taskType) || "PRACTICE".equals(taskType) || "QUIZ".equals(taskType);
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
