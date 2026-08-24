package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.assessment.dto.AssessmentResultView;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class LearningReleaseRegistryTest {
  private static final String CANDIDATE = "a".repeat(64);
  private static final String RUN = "R".repeat(43);
  private static final String GUEST = "12345678-1234-4123-8123-123456789abc";

  @Test
  void guestPreviewAndClaimReplayKeepOnlyHashesAndExactOwnerFacts() {
    LearningReleaseRegistry registry = new LearningReleaseRegistry(
        true, JsonMapper.builder().build());
    AssessmentResultView preview = new AssessmentResultView(
        "INTERMEDIATE", null, null, null, 1.0);

    registry.recordGuestStarted(CANDIDATE, RUN, GUEST);
    registry.recordGuestCompleted(CANDIDATE, RUN, GUEST, preview);
    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "guest-preview-owned-by-guest")).isTrue();

    registry.armClaimReplay(CANDIDATE, RUN, 42L);
    var plan = registry.claimPlan(CANDIDATE, RUN, 42L, GUEST);
    assertThat(plan.tracked()).isTrue();
    registry.recordClaim(plan, 81L, 81L, preview, 1L);

    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "claim-replay-one-owned-result")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "saved-preview-deep-equal")).isTrue();
    assertThat(registry.snapshot(CANDIDATE, RUN).toString())
        .doesNotContain(GUEST, "INTERMEDIATE");
  }

  @Test
  void pathReplayFactsRequireStableCountsTasksAndCompletionTimestamps() {
    LearningReleaseRegistry registry = new LearningReleaseRegistry(
        true, JsonMapper.builder().build());
    registry.bindUser(CANDIDATE, RUN, 42L);
    registry.recordAuthoritativeTask(CANDIDATE, RUN, 42L, 10L, 20L, 0, true);
    registry.recordBelowThreshold(CANDIDATE, RUN, 42L, true);
    registry.recordContentLinkedReplay(
        CANDIDATE, RUN, 42L, 10L, 11L, 1, 1, true);
    registry.recordContentlessReplay(
        CANDIDATE, RUN, 42L, 11L, 12L, 2, 2, true);

    assertThat(registry.checkpoint(CANDIDATE, RUN, "authoritative-first-task")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "workspace-context-parity")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "content-linked-below-threshold")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "content-linked-advanced-once")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "contentless-advanced-once")).isTrue();
    assertThat(registry.checkpoint(CANDIDATE, RUN, "completion-replays-noop")).isTrue();
  }

  @Test
  void disabledOrUnboundBrowserHeadersRemainInert() {
    LearningReleaseRegistry registry = new LearningReleaseRegistry(
        false, JsonMapper.builder().build());
    registry.recordGuestStarted(CANDIDATE, RUN, GUEST);
    assertThat(registry.checkpoint(
        CANDIDATE, RUN, "guest-preview-owned-by-guest")).isFalse();
    assertThat(registry.claimPlan(CANDIDATE, RUN, 42L, GUEST).tracked()).isFalse();
  }
}
