package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.Assessment;
import ai.devpath.learning.assessment.AssessmentRepository;
import ai.devpath.learning.assessment.AssessmentResult;
import ai.devpath.learning.assessment.AssessmentResultRepository;
import ai.devpath.learning.assessment.claim.ClaimService;
import ai.devpath.learning.assessment.dto.AssessmentResultView;
import ai.devpath.learning.content.ContentProgressRepository;
import ai.devpath.learning.content.ContentService;
import ai.devpath.learning.content.UpsertContentProgressResponse;
import ai.devpath.learning.path.CurrentMissionOutcome;
import ai.devpath.learning.path.CurrentMissionQueryRepository;
import ai.devpath.learning.path.PathWeeklyTaskRepository;
import ai.devpath.learning.path.ThisWeekView;
import ai.devpath.learning.path.WeeklyTaskView;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class LearningReleaseVerificationServiceTest {
  private static final String CANDIDATE = "a".repeat(64);
  private static final String RUN = "R".repeat(43);
  private static final String GUEST = "12345678-1234-4123-8123-123456789abc";

  private final LearningReleaseRegistry registry = new LearningReleaseRegistry(
      true, JsonMapper.builder().build());
  private final ClaimService claims = mock(ClaimService.class);
  private final AssessmentRepository assessments = mock(AssessmentRepository.class);
  private final AssessmentResultRepository results = mock(AssessmentResultRepository.class);
  private final CurrentMissionQueryRepository missions = mock(CurrentMissionQueryRepository.class);
  private final ContentProgressRepository progress = mock(ContentProgressRepository.class);
  private final ContentService content = mock(ContentService.class);
  private final PathWeeklyTaskRepository weeklyTasks = mock(PathWeeklyTaskRepository.class);
  private final LearningReleaseFacts facts = mock(LearningReleaseFacts.class);
  private LearningReleaseVerificationService verification;

  @BeforeEach
  void setUp() {
    verification = new LearningReleaseVerificationService(
        registry, claims, assessments, results, missions, progress, content, weeklyTasks, facts);
  }

  @Test
  void claimReplayUsesTheProductionClaimTwiceAndComparesThePersistedProjection() {
    AssessmentResultView preview = new AssessmentResultView(
        "INTERMEDIATE", null, null, null, 1.0);
    registry.recordGuestStarted(CANDIDATE, RUN, GUEST);
    registry.recordGuestCompleted(CANDIDATE, RUN, GUEST, preview);
    registry.armClaimReplay(CANDIDATE, RUN, 42L);
    var plan = registry.claimPlan(CANDIDATE, RUN, 42L, GUEST);
    when(claims.claim(42L, GUEST)).thenReturn(81L);
    Assessment assessment = new Assessment();
    assessment.setUserId(42L);
    AssessmentResult persisted = new AssessmentResult();
    persisted.setAssessmentId(81L);
    persisted.setDiagnosedLevel("INTERMEDIATE");
    persisted.setConfidenceWeight(1.0);
    when(assessments.findById(81L)).thenReturn(Optional.of(assessment));
    when(results.findById(81L)).thenReturn(Optional.of(persisted));
    when(facts.countOwnedClaims(42L, GUEST)).thenReturn(1L);

    assertThat(verification.claimWithReplay(plan, GUEST)).isEqualTo(81L);
    verify(claims, org.mockito.Mockito.times(2)).claim(42L, GUEST);
    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "claim-replay-one-owned-result")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "saved-preview-deep-equal")).isTrue();
  }

  @Test
  void contentAndContentlessReplayKeepCompletionCountsAndTimestampsStable() {
    WeeklyTaskView linked = new WeeklyTaskView(
        1, "READ", "linked", true, 20L, "linked", false, 10L, null);
    WeeklyTaskView contentless = new WeeklyTaskView(
        2, "QUIZ", "quiz", true, null, null, false, 11L, null);
    when(missions.findForUser(42L)).thenReturn(
        new ThisWeekView(1L, 1, List.of(linked, contentless),
            CurrentMissionOutcome.AVAILABLE, linked, false),
        new ThisWeekView(1L, 1, List.of(linked, contentless),
            CurrentMissionOutcome.AVAILABLE, contentless, false),
        new ThisWeekView(1L, 1, List.of(linked, contentless),
            CurrentMissionOutcome.PATH_COMPLETED, null, true));
    when(facts.countCompletedTasks(42L)).thenReturn(0, 1, 1, 2, 2);
    Instant linkedAt = Instant.parse("2026-08-24T00:00:00Z");
    Instant contentlessAt = Instant.parse("2026-08-24T00:01:00Z");
    when(facts.taskCompletedAt(10L)).thenReturn(linkedAt, linkedAt);
    when(facts.taskCompletedAt(11L)).thenReturn(contentlessAt, contentlessAt);
    when(progress.find(42L, 20L)).thenReturn(Optional.of(
        new ContentProgressRepository.ProgressRow(20L, 0.9, 60, linkedAt, linkedAt)));
    when(content.upsertProgress(
        org.mockito.ArgumentMatchers.eq(42L),
        org.mockito.ArgumentMatchers.eq("20"),
        org.mockito.ArgumentMatchers.any()))
        .thenReturn(new UpsertContentProgressResponse(20L, 0.9, 60, true, linkedAt, 0));
    when(weeklyTasks.completeTaskIfOwned(42L, 11L)).thenReturn(1);

    assertThat(verification.checkpoint(
        CANDIDATE, RUN, 42L, "authoritative-first-task")).isTrue();
    verification.replayContentLinked(CANDIDATE, RUN, 42L);
    verification.replayContentless(CANDIDATE, RUN, 42L);

    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "content-linked-advanced-once")).isTrue();
    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "contentless-advanced-once")).isTrue();
    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "completion-replays-noop")).isTrue();
  }
}
