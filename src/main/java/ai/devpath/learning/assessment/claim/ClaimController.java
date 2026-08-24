package ai.devpath.learning.assessment.claim;

import ai.devpath.learning.assessment.claim.dto.ClaimRequest;
import ai.devpath.learning.release.LearningReleaseRegistry;
import ai.devpath.learning.release.LearningReleaseVerificationService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding/assessments/claim")
public class ClaimController {

  private final ClaimService service;
  private final LearningReleaseRegistry release;
  private final LearningReleaseVerificationService verification;

  public ClaimController(
      ClaimService service,
      LearningReleaseRegistry release,
      LearningReleaseVerificationService verification) {
    this.service = service;
    this.release = release;
    this.verification = verification;
  }

  @PostMapping
  public ResponseEntity<Map<String, Long>> claim(@AuthenticationPrincipal Jwt jwt,
      @RequestBody ClaimRequest req,
      @RequestHeader(name = "X-Candidate-Spec-Sha256", required = false) String candidate,
      @RequestHeader(name = "X-Release-Run-Key", required = false) String runKey) {
    long userId = Long.parseLong(jwt.getSubject());
    var plan = release.claimPlan(candidate, runKey, userId, req.guestAssessmentId());
    long assessmentId = plan.tracked()
        ? verification.claimWithReplay(plan, req.guestAssessmentId())
        : service.claim(userId, req.guestAssessmentId());
    return ResponseEntity.ok(Map.of("assessmentId", assessmentId));
  }
}
