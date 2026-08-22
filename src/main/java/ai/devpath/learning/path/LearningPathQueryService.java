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
  private static final int CURRENT_MISSION_MAX_BYTES = 20 * 1024;

  private final LearningPathRepository paths;
  private final LatestDiagnosisRepository diagnoses;
  private final ContentRepository contents;
  private final CurrentMissionQueryRepository currentMissions;
  private final JsonMapper jsonMapper;

  public LearningPathQueryService(LearningPathRepository paths, LatestDiagnosisRepository diagnoses,
      ContentRepository contents, CurrentMissionQueryRepository currentMissions, JsonMapper jsonMapper) {
    this.paths = paths;
    this.diagnoses = diagnoses;
    this.contents = contents;
    this.currentMissions = currentMissions;
    this.jsonMapper = jsonMapper;
  }

  /**
   * 활성 학습 경로를 조회한다. 없으면 예외 대신 {@link Optional#empty()}를 반환한다.
   *
   * <p>대시보드처럼 "없음"이 정상 흐름인 호출자는 이 메서드를 써야 한다. {@link #current(long)}이
   * 던지는 {@link NoSuchElementException}을 상위 {@code @Transactional}에서 catch하면, 내부 트랜잭션이
   * 이미 공유 트랜잭션을 rollback-only로 마킹해 커밋 시 {@code UnexpectedRollbackException}이 난다.
   */
  @Transactional(readOnly = true)
  public Optional<LearningPathView> currentOptional(long userId) {
    return paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE")
        .map(path -> toView(path, diagnoses.findLatestCompleted(userId).orElse(null)));
  }

  @Transactional(readOnly = true)
  public LearningPathView current(long userId) {
    return currentOptional(userId)
        .orElseThrow(() -> new NoSuchElementException("active learning path 없음"));
  }

  @Transactional(readOnly = true)
  public ThisWeekView thisWeek(long userId) {
    ThisWeekView view = currentMissions.findForUser(userId);
    try {
      if (jsonMapper.writeValueAsBytes(view).length <= CURRENT_MISSION_MAX_BYTES) {
        return view;
      }
    } catch (Exception ignored) {
      // 직렬화할 수 없는 projection도 정상 응답으로 노출하지 않고 malformed로 닫는다.
    }
    return ThisWeekView.malformed(view.pathId());
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
              content == null ? null : content.getSlug(), t.getCompletedAt() != null,
              t.getId(), t.getCompletedAt());
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
