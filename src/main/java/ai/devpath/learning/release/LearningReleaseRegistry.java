package ai.devpath.learning.release;

import ai.devpath.learning.assessment.dto.AssessmentResultView;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/** Candidate/run-bound release observations. Raw guest answers and result values are never stored. */
@Component
public class LearningReleaseRegistry {
  private static final Pattern CANDIDATE = Pattern.compile("^[0-9a-f]{64}$");
  private static final Pattern RUN_KEY = Pattern.compile("^[A-Za-z0-9_-]{22,128}$");
  private static final int MAX_RUNS = 64;

  private final boolean enabled;
  private final JsonMapper jsonMapper;
  private final Map<Key, Observation> runs = new ConcurrentHashMap<>();

  public LearningReleaseRegistry(
      @Value("${devpath.release.enabled:false}") boolean enabled,
      JsonMapper jsonMapper) {
    this.enabled = enabled;
    this.jsonMapper = jsonMapper;
  }

  public void bindUser(String candidate, String runKey, long userId) {
    requireEnabled();
    if (userId <= 0) throw new IllegalArgumentException("release fixture user id is invalid");
    Observation observation = state(requireKey(candidate, runKey));
    synchronized (observation) {
      bindOwner(observation, userId);
    }
  }

  public void recordGuestStarted(String candidate, String runKey, String guestId) {
    Observation observation = browserState(candidate, runKey);
    if (observation == null || guestId == null) return;
    synchronized (observation) {
      observation.guestIdSha256 = digest(guestId);
      observation.guestStarted = true;
    }
  }

  public void recordGuestCompleted(
      String candidate,
      String runKey,
      String guestId,
      AssessmentResultView preview) {
    Observation observation = browserState(candidate, runKey);
    if (observation == null || guestId == null || preview == null) return;
    synchronized (observation) {
      String guestHash = digest(guestId);
      if (!observation.guestStarted || !guestHash.equals(observation.guestIdSha256)) return;
      observation.guestPreviewSha256 = digest(preview);
      observation.guestCompleted = true;
    }
  }

  public void armClaimReplay(String candidate, String runKey, long userId) {
    bindUser(candidate, runKey, userId);
    Observation observation = runs.get(new Key(candidate, runKey));
    synchronized (observation) {
      if (!observation.guestCompleted) {
        throw new IllegalStateException("release guest assessment is not complete");
      }
      observation.claimReplayArmed = true;
    }
  }

  public ClaimPlan claimPlan(String candidate, String runKey, long userId, String guestId) {
    if (!enabled || !valid(candidate, CANDIDATE) || !valid(runKey, RUN_KEY)
        || userId <= 0 || guestId == null) return ClaimPlan.NONE;
    Key key = new Key(candidate, runKey);
    Observation observation = runs.get(key);
    if (observation == null) return ClaimPlan.NONE;
    synchronized (observation) {
      if (!observation.claimReplayArmed || observation.userId != userId
          || !digest(guestId).equals(observation.guestIdSha256)) return ClaimPlan.NONE;
      observation.claimReplayArmed = false;
      return new ClaimPlan(key, userId, true);
    }
  }

  public void recordClaim(
      ClaimPlan plan,
      long firstAssessmentId,
      long replayAssessmentId,
      AssessmentResultView persisted,
      long ownerBoundRows) {
    Observation observation = observation(plan);
    if (observation == null || persisted == null) return;
    synchronized (observation) {
      observation.claimAssessmentId = firstAssessmentId;
      observation.claimReplaySame = firstAssessmentId > 0
          && firstAssessmentId == replayAssessmentId
          && ownerBoundRows == 1;
      observation.savedPreviewEqual = observation.guestPreviewSha256 != null
          && observation.guestPreviewSha256.equals(digest(persisted));
    }
  }

  public void recordAuthoritativeTask(
      String candidate,
      String runKey,
      long userId,
      long taskId,
      Long contentId,
      int completedCount,
      boolean workspaceContextParity) {
    bindUser(candidate, runKey, userId);
    Observation observation = runs.get(new Key(candidate, runKey));
    synchronized (observation) {
      if (taskId <= 0 || completedCount < 0) return;
      if (!observation.authoritativeTask) {
        observation.firstTaskId = taskId;
        observation.firstContentId = contentId;
        observation.baselineCompletedCount = completedCount;
      }
      observation.authoritativeTask = true;
      observation.workspaceContextParity |= workspaceContextParity;
    }
  }

  public void recordBelowThreshold(
      String candidate, String runKey, long userId, boolean passed) {
    Observation observation = owned(candidate, runKey, userId);
    synchronized (observation) {
      observation.contentLinkedBelowThreshold = passed;
    }
  }

  public void recordContentLinkedReplay(
      String candidate,
      String runKey,
      long userId,
      long firstTaskId,
      Long nextTaskId,
      int completedBeforeReplay,
      int completedAfterReplay,
      boolean timestampStable) {
    Observation observation = owned(candidate, runKey, userId);
    synchronized (observation) {
      observation.contentLinkedReplay = observation.firstTaskId != null
          && observation.firstTaskId == firstTaskId
          && nextTaskId != null
          && nextTaskId != firstTaskId
          && completedBeforeReplay == completedAfterReplay
          && completedAfterReplay > observation.baselineCompletedCount
          && timestampStable;
      if (observation.contentLinkedReplay) {
        observation.contentlessTaskId = nextTaskId;
        observation.afterLinkedCompletedCount = completedAfterReplay;
      }
    }
  }

  public void recordContentlessReplay(
      String candidate,
      String runKey,
      long userId,
      long taskId,
      Long nextTaskId,
      int completedBeforeReplay,
      int completedAfterReplay,
      boolean timestampStable) {
    Observation observation = owned(candidate, runKey, userId);
    synchronized (observation) {
      observation.contentlessReplay = observation.contentlessTaskId != null
          && observation.contentlessTaskId == taskId
          && (nextTaskId == null || nextTaskId != taskId)
          && completedBeforeReplay == completedAfterReplay
          && completedAfterReplay > observation.afterLinkedCompletedCount
          && timestampStable;
      if (observation.contentlessReplay) {
        observation.afterContentlessCompletedCount = completedAfterReplay;
      }
    }
  }

  public boolean checkpoint(String candidate, String runKey, String checkpoint) {
    if (!enabled || !valid(candidate, CANDIDATE) || !valid(runKey, RUN_KEY)) return false;
    Observation observation = runs.get(new Key(candidate, runKey));
    if (observation == null) return false;
    synchronized (observation) {
      return switch (checkpoint) {
        case "guest-preview-owned-by-guest" ->
            observation.guestStarted && observation.guestCompleted && observation.userId == 0;
        case "claim-replay-one-owned-result" -> observation.claimReplaySame;
        case "saved-preview-deep-equal" -> observation.savedPreviewEqual;
        case "authoritative-first-task", "authoritative-workspace-task" ->
            observation.authoritativeTask;
        case "workspace-context-parity" -> observation.workspaceContextParity;
        case "content-linked-below-threshold" -> observation.contentLinkedBelowThreshold;
        case "content-linked-advanced-once" -> observation.contentLinkedReplay;
        case "contentless-advanced-once" -> observation.contentlessReplay;
        case "completion-replays-noop" -> observation.contentLinkedReplay
            && observation.contentlessReplay
            && observation.afterContentlessCompletedCount
                > observation.afterLinkedCompletedCount;
        default -> false;
      };
    }
  }

  public Snapshot snapshot(String candidate, String runKey) {
    Observation observation = runs.get(requireKey(candidate, runKey));
    if (observation == null) throw new IllegalStateException("Learning release run is unavailable");
    synchronized (observation) {
      return new Snapshot(
          observation.userId,
          observation.guestIdSha256,
          observation.guestPreviewSha256,
          observation.guestCompleted,
          observation.claimAssessmentId,
          observation.claimReplaySame,
          observation.savedPreviewEqual,
          observation.firstTaskId,
          observation.firstContentId,
          observation.baselineCompletedCount,
          observation.contentlessTaskId,
          observation.contentLinkedReplay,
          observation.contentlessReplay);
    }
  }

  private Observation browserState(String candidate, String runKey) {
    if (!enabled || !valid(candidate, CANDIDATE) || !valid(runKey, RUN_KEY)) return null;
    return state(new Key(candidate, runKey));
  }

  private Observation owned(String candidate, String runKey, long userId) {
    bindUser(candidate, runKey, userId);
    return runs.get(new Key(candidate, runKey));
  }

  private Observation observation(ClaimPlan plan) {
    return !enabled || plan == null || !plan.tracked() ? null : runs.get(plan.key());
  }

  private Observation state(Key key) {
    Observation current = runs.get(key);
    if (current != null) return current;
    if (runs.size() >= MAX_RUNS) {
      throw new IllegalStateException("Learning release run capacity is exhausted");
    }
    return runs.computeIfAbsent(key, ignored -> new Observation());
  }

  private String digest(Object value) {
    try {
      byte[] bytes = value instanceof String text
          ? text.getBytes(java.nio.charset.StandardCharsets.UTF_8)
          : jsonMapper.writeValueAsBytes(value);
      return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    } catch (Exception failure) {
      throw new IllegalStateException("Learning release digest failed", failure);
    }
  }

  private void requireEnabled() {
    if (!enabled) throw new IllegalStateException("Learning release hooks are disabled");
  }

  private static Key requireKey(String candidate, String runKey) {
    if (!valid(candidate, CANDIDATE) || !valid(runKey, RUN_KEY)) {
      throw new IllegalArgumentException("Learning release binding is invalid");
    }
    return new Key(candidate, runKey);
  }

  private static boolean valid(String value, Pattern pattern) {
    return value != null && pattern.matcher(value).matches();
  }

  private static void bindOwner(Observation observation, long userId) {
    if (observation.userId == 0) observation.userId = userId;
    if (observation.userId != userId) {
      throw new IllegalArgumentException("Learning release owner binding does not match");
    }
  }

  private record Key(String candidate, String runKey) {}

  private static final class Observation {
    private long userId;
    private String guestIdSha256;
    private String guestPreviewSha256;
    private boolean guestStarted;
    private boolean guestCompleted;
    private boolean claimReplayArmed;
    private Long claimAssessmentId;
    private boolean claimReplaySame;
    private boolean savedPreviewEqual;
    private boolean authoritativeTask;
    private Long firstTaskId;
    private Long firstContentId;
    private int baselineCompletedCount;
    private boolean workspaceContextParity;
    private boolean contentLinkedBelowThreshold;
    private Long contentlessTaskId;
    private int afterLinkedCompletedCount;
    private int afterContentlessCompletedCount;
    private boolean contentLinkedReplay;
    private boolean contentlessReplay;
  }

  public record ClaimPlan(Key key, long userId, boolean tracked) {
    public static final ClaimPlan NONE = new ClaimPlan(null, 0, false);
  }

  public record Snapshot(
      long userId,
      String guestIdSha256,
      String guestPreviewSha256,
      boolean guestCompleted,
      Long claimAssessmentId,
      boolean claimReplaySame,
      boolean savedPreviewEqual,
      Long firstTaskId,
      Long firstContentId,
      int baselineCompletedCount,
      Long contentlessTaskId,
      boolean contentLinkedReplay,
      boolean contentlessReplay) {}
}
