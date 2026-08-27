package ai.devpath.learning.release;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.AssessmentResult;
import ai.devpath.learning.assessment.AssessmentResultRepository;
import ai.devpath.learning.assessment.claim.ClaimService;
import ai.devpath.learning.assessment.dto.AssessmentResultView;
import ai.devpath.learning.content.ContentProgressRepository;
import ai.devpath.learning.content.ContentService;
import ai.devpath.learning.content.UpsertContentProgressRequest;
import ai.devpath.learning.path.CurrentMissionOutcome;
import ai.devpath.learning.path.CurrentMissionQueryRepository;
import ai.devpath.learning.path.PathWeeklyTaskRepository;
import ai.devpath.learning.path.ThisWeekView;
import ai.devpath.learning.path.WeeklyTaskView;
import java.time.Instant;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** Executes release replays through production services and records only derived invariants. */
@Service
public class LearningReleaseVerificationService {
  private final LearningReleaseRegistry release;
  private final ClaimService claims;
  private final AssessmentRepository assessments;
  private final AssessmentResultRepository results;
  private final CurrentMissionQueryRepository missions;
  private final ContentProgressRepository progress;
  private final ContentService content;
  private final PathWeeklyTaskRepository weeklyTasks;
  private final LearningReleaseFacts facts;

  public LearningReleaseVerificationService(
      LearningReleaseRegistry release,
      ClaimService claims,
      AssessmentRepository assessments,
      AssessmentResultRepository results,
      CurrentMissionQueryRepository missions,
      ContentProgressRepository progress,
      ContentService content,
      PathWeeklyTaskRepository weeklyTasks,
      LearningReleaseFacts facts) {
    this.release = release;
    this.claims = claims;
    this.assessments = assessments;
    this.results = results;
    this.missions = missions;
    this.progress = progress;
    this.content = content;
    this.weeklyTasks = weeklyTasks;
    this.facts = facts;
  }

  public long claimWithReplay(LearningReleaseRegistry.ClaimPlan plan, String guestId) {
    if (plan == null || !plan.tracked()) {
      throw new IllegalArgumentException("release claim replay is not armed");
    }
    long first = claims.claim(plan.userId(), guestId);
    long replayed = claims.claim(plan.userId(), guestId);
    Assessment assessment = assessments.findById(first)
        .filter(value -> Objects.equals(value.getUserId(), plan.userId()))
        .orElseThrow(() -> new IllegalStateException("release claim owner is unavailable"));
    AssessmentResult result = results.findById(first)
        .orElseThrow(() -> new IllegalStateException("release claim result is unavailable"));
    AssessmentResultView persisted = new AssessmentResultView(
        result.getDiagnosedLevel(),
        result.getConceptScores(),
        result.getStrengthConcepts(),
        result.getWeaknessConcepts(),
        result.getConfidenceWeight());
    release.recordClaim(
        plan, first, replayed, persisted, facts.countOwnedClaims(plan.userId(), guestId));
    return first;
  }

  public boolean checkpoint(
      String candidate, String runKey, Long userId, String checkpoint) {
    return switch (checkpoint) {
      case "authoritative-first-task", "authoritative-workspace-task" ->
          observeAuthoritative(candidate, runKey, requireUser(userId));
      case "workspace-context-parity" ->
          observeWorkspaceParity(candidate, runKey, requireUser(userId));
      case "content-linked-below-threshold" ->
          observeBelowThreshold(candidate, runKey, requireUser(userId));
      default -> release.checkpoint(candidate, runKey, checkpoint);
    };
  }

  public void replayContentLinked(String candidate, String runKey, long userId) {
    release.bindUser(candidate, runKey, userId);
    var snapshot = release.snapshot(candidate, runKey);
    if (snapshot.firstTaskId() == null || snapshot.firstContentId() == null) {
      throw new IllegalStateException("release content-linked task is unavailable");
    }
    var row = progress.find(userId, snapshot.firstContentId())
        .filter(ContentProgressRepository.ProgressRow::completed)
        .orElseThrow(() -> new IllegalStateException(
            "release content progress has not crossed the threshold"));
    Instant completedAt = facts.taskCompletedAt(snapshot.firstTaskId());
    int before = facts.countCompletedTasks(userId);
    var replayed = content.upsertProgress(
        userId,
        String.valueOf(snapshot.firstContentId()),
        new UpsertContentProgressRequest(row.scrollPct(), row.dwellSec()));
    int after = facts.countCompletedTasks(userId);
    Instant afterCompletedAt = facts.taskCompletedAt(snapshot.firstTaskId());
    ThisWeekView current = missions.findForUser(userId);
    WeeklyTaskView successor = current.nextTask();
    if (successor == null || successor.contentId() != null) {
      throw new IllegalStateException("release successor task is not contentless");
    }
    Long nextTaskId = successor.taskId();
    boolean stable = replayed.completed()
        && replayed.taskCompletedCount() == 0
        && completedAt != null
        && completedAt.equals(afterCompletedAt);
    release.recordContentLinkedReplay(
        candidate, runKey, userId, snapshot.firstTaskId(), nextTaskId, before, after, stable);
  }

  public void replayContentless(String candidate, String runKey, long userId) {
    release.bindUser(candidate, runKey, userId);
    var snapshot = release.snapshot(candidate, runKey);
    if (snapshot.contentlessTaskId() == null) {
      throw new IllegalStateException("release contentless task is unavailable");
    }
    long taskId = snapshot.contentlessTaskId();
    Instant completedAt = facts.taskCompletedAt(taskId);
    int before = facts.countCompletedTasks(userId);
    int replayed = weeklyTasks.completeTaskIfOwned(userId, taskId);
    int after = facts.countCompletedTasks(userId);
    Instant afterCompletedAt = facts.taskCompletedAt(taskId);
    ThisWeekView current = missions.findForUser(userId);
    Long nextTaskId = current.nextTask() == null ? null : current.nextTask().taskId();
    boolean stable = replayed == 1
        && completedAt != null
        && completedAt.equals(afterCompletedAt);
    release.recordContentlessReplay(
        candidate, runKey, userId, taskId, nextTaskId, before, after, stable);
  }

  private boolean observeAuthoritative(String candidate, String runKey, long userId) {
    ThisWeekView view = missions.findForUser(userId);
    WeeklyTaskView task = requireAvailableTask(view);
    boolean parity = task.contentId() != null
        && task.contentSlug() != null
        && !task.contentSlug().isBlank();
    release.recordAuthoritativeTask(
        candidate, runKey, userId, task.taskId(), task.contentId(),
        facts.countCompletedTasks(userId), parity);
    return release.checkpoint(candidate, runKey, "authoritative-first-task");
  }

  private boolean observeWorkspaceParity(String candidate, String runKey, long userId) {
    ThisWeekView view = missions.findForUser(userId);
    WeeklyTaskView task = requireAvailableTask(view);
    boolean parity = task.contentId() != null
        && task.contentSlug() != null
        && !task.contentSlug().isBlank()
        && task.title() != null
        && !task.title().isBlank();
    release.recordAuthoritativeTask(
        candidate, runKey, userId, task.taskId(), task.contentId(),
        facts.countCompletedTasks(userId), parity);
    return release.checkpoint(candidate, runKey, "workspace-context-parity");
  }

  private boolean observeBelowThreshold(String candidate, String runKey, long userId) {
    release.bindUser(candidate, runKey, userId);
    var snapshot = release.snapshot(candidate, runKey);
    if (snapshot.firstContentId() == null) return false;
    boolean passed = progress.find(userId, snapshot.firstContentId())
        .map(row -> !row.completed())
        .orElse(true);
    release.recordBelowThreshold(candidate, runKey, userId, passed);
    return release.checkpoint(candidate, runKey, "content-linked-below-threshold");
  }

  private static WeeklyTaskView requireAvailableTask(ThisWeekView view) {
    if (view == null || view.outcome() != CurrentMissionOutcome.AVAILABLE
        || view.nextTask() == null || view.nextTask().taskId() == null) {
      throw new IllegalStateException("release authoritative task is unavailable");
    }
    return view.nextTask();
  }

  private static long requireUser(Long value) {
    if (value == null || value <= 0) {
      throw new IllegalArgumentException("release fixture user id is required");
    }
    return value;
  }
}
