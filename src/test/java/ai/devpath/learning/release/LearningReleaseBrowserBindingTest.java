package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.assessment.claim.ClaimController;
import ai.devpath.learning.assessment.claim.ClaimService;
import ai.devpath.learning.assessment.claim.dto.ClaimRequest;
import ai.devpath.learning.assessment.dto.AssessmentResultView;
import ai.devpath.learning.assessment.dto.StartAssessmentRequest;
import ai.devpath.learning.assessment.guest.GuestAssessmentController;
import ai.devpath.learning.assessment.guest.GuestAssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class LearningReleaseBrowserBindingTest {
  private static final String CANDIDATE = "a".repeat(64);
  private static final String RUN = "R".repeat(43);
  private static final String GUEST = "12345678-1234-4123-8123-123456789abc";

  @Test
  void guestStartAndCompleteAreRecordedOnlyAfterProductionServiceSuccess() {
    GuestAssessmentService service = mock(GuestAssessmentService.class);
    LearningReleaseRegistry release = mock(LearningReleaseRegistry.class);
    GuestAssessmentController controller = new GuestAssessmentController(service, release);
    StartAssessmentRequest request = new StartAssessmentRequest("BACKEND_SPRING");
    AssessmentResultView preview = new AssessmentResultView(
        "INTERMEDIATE", null, null, null, 1.0);
    when(service.start("BACKEND_SPRING")).thenReturn(GUEST);
    when(service.complete(GUEST)).thenReturn(preview);

    assertThat(controller.start(request, CANDIDATE, RUN).getBody())
        .containsEntry("guestAssessmentId", GUEST);
    assertThat(controller.complete(GUEST, CANDIDATE, RUN).getBody()).isEqualTo(preview);
    verify(release).recordGuestStarted(CANDIDATE, RUN, GUEST);
    verify(release).recordGuestCompleted(CANDIDATE, RUN, GUEST, preview);
  }

  @Test
  void armedClaimUsesTheReleaseVerifierAndUnarmedClaimUsesProductionOnce() {
    ClaimService service = mock(ClaimService.class);
    LearningReleaseRegistry release = mock(LearningReleaseRegistry.class);
    LearningReleaseVerificationService verification = mock(
        LearningReleaseVerificationService.class);
    ClaimController controller = new ClaimController(service, release, verification);
    var plan = mock(LearningReleaseRegistry.ClaimPlan.class);
    when(plan.tracked()).thenReturn(true);
    when(release.claimPlan(CANDIDATE, RUN, 42L, GUEST)).thenReturn(plan);
    when(verification.claimWithReplay(plan, GUEST)).thenReturn(81L);

    assertThat(controller.claim(jwt(), new ClaimRequest(GUEST), CANDIDATE, RUN).getBody())
        .containsEntry("assessmentId", 81L);
    verify(service, never()).claim(42L, GUEST);
  }

  private static Jwt jwt() {
    return Jwt.withTokenValue("token").header("alg", "none").subject("42").build();
  }
}
