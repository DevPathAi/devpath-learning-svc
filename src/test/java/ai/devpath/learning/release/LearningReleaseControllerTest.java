package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;

class LearningReleaseControllerTest {
  @Test
  void routesClaimAndCompletionReplaysThroughTheirProductionVerifiers() {
    String candidate = "a".repeat(64);
    String run = "R".repeat(43);
    LearningReleaseRegistry registry = mock(LearningReleaseRegistry.class);
    LearningReleaseVerificationService verification = mock(
        LearningReleaseVerificationService.class);
    LearningReleaseController controller = new LearningReleaseController(registry, verification);

    assertThat(controller.command(
        candidate, run, "replay-claim", Map.of("user_id", 42L)))
        .containsEntry("accepted", true);
    verify(registry).armClaimReplay(candidate, run, 42L);

    controller.command(candidate, run, "replay-content-linked-completion",
        Map.of("user_id", 42L));
    verify(verification).replayContentLinked(candidate, run, 42L);
    controller.command(candidate, run, "replay-contentless-completion",
        Map.of("user_id", 42L));
    verify(verification).replayContentless(candidate, run, 42L);

    when(verification.checkpoint(
        candidate, run, 42L, "authoritative-first-task")).thenReturn(true);
    assertThat(controller.checkpoint(
        candidate, run, "authoritative-first-task", 42L))
        .containsEntry("passed", true);
  }
}
