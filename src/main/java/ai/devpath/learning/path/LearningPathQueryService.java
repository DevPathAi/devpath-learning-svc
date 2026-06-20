package ai.devpath.learning.path;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Service
public class LearningPathQueryService {
  private final LearningPathRepository paths;
  private final LatestDiagnosisRepository diagnoses;
  private final ContentRepository contents;
  private final JsonMapper jsonMapper;

  public LearningPathQueryService(LearningPathRepository paths, LatestDiagnosisRepository diagnoses,
      ContentRepository contents, JsonMapper jsonMapper) {
    this.paths = paths;
    this.diagnoses = diagnoses;
    this.contents = contents;
    this.jsonMapper = jsonMapper;
  }

  @Transactional(readOnly = true)
  public LearningPathView current(long userId) {
    LearningPath path = paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE")
        .orElseThrow(() -> new NoSuchElementException("active learning path 없음"));
    LatestDiagnosis diagnosis = diagnoses.findLatestCompleted(userId).orElse(null);
    return toView(path, diagnosis);
  }

  @Transactional(readOnly = true)
  public ThisWeekView thisWeek(long userId) {
    LearningPathView view = current(userId);
    MilestoneView first = view.milestones().stream()
        .filter(m -> m.weekNum() == 1).findFirst()
        .orElseThrow(() -> new NoSuchElementException("week 1 milestone 없음"));
    return new ThisWeekView(view.pathId(), first.weekNum(), first.tasks());
  }

  @Transactional(readOnly = true)
  public RationaleView rationale(long userId, long pathId) {
    LearningPath path = paths.findById(pathId)
        .orElseThrow(() -> new NoSuchElementException("learning path 없음"));
    if (!Objects.equals(path.getUserId(), userId)) {
      throw new AccessDeniedException("소유자 아님");
    }
    return new RationaleView(path.getId(), path.getAiRationale(), path.getMilestones().stream()
        .map(m -> new RationaleView.MilestoneRationale(m.getWeekNum(), m.getWhyThisOrder()))
        .toList());
  }

  private LearningPathView toView(LearningPath path, LatestDiagnosis diagnosis) {
    Set<Long> contentIds = path.getMilestones().stream()
        .flatMap(m -> m.getTasks().stream())
        .map(PathWeeklyTask::getContentId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    Map<Long, Content> contentById = contents.findAllById(contentIds).stream()
        .collect(Collectors.toMap(Content::getId, c -> c));

    List<MilestoneView> milestones = path.getMilestones().stream().map(m -> new MilestoneView(
        m.getWeekNum(),
        m.getTitle(),
        m.getGoalDescription(),
        parseList(m.getTargetSkills()),
        m.getEstimatedHours(),
        m.getWhyThisOrder(),
        m.getExpectedOutcome(),
        m.getWeekNum() > 1,
        m.getTasks().stream().map(t -> {
          Content content = t.getContentId() == null ? null : contentById.get(t.getContentId());
          return new WeeklyTaskView(t.getOrderNum(), t.getTaskType(), t.getTitle(),
              Boolean.TRUE.equals(t.getRequired()), t.getContentId(),
              content == null ? null : content.getSlug(), t.getCompletedAt() != null);
        }).toList())).toList();

    LearningPathView.DiagnosisView diagnosisView = diagnosis == null ? null
        : new LearningPathView.DiagnosisView(diagnosis.diagnosedLevel(),
            diagnosis.strengthConcepts(), diagnosis.weaknessConcepts());
    return new LearningPathView(path.getId(), path.getTrack(), path.getTotalWeeks(),
        path.getAiRationale(), diagnosisView, milestones);
  }

  private List<String> parseList(String json) {
    if (json == null || json.isBlank()) return List.of();
    try {
      return jsonMapper.readValue(json, new TypeReference<List<String>>() {});
    } catch (Exception e) {
      return List.of();
    }
  }
}
