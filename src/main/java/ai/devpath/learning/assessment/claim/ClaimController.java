package ai.devpath.learning.assessment.claim;

import ai.devpath.learning.assessment.claim.dto.ClaimRequest;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding/assessments/claim")
public class ClaimController {

  private final ClaimService service;

  public ClaimController(ClaimService service) { this.service = service; }

  @PostMapping
  public ResponseEntity<Map<String, Long>> claim(@AuthenticationPrincipal Jwt jwt,
      @RequestBody ClaimRequest req) {
    long userId = Long.parseLong(jwt.getSubject());
    return ResponseEntity.ok(Map.of("assessmentId", service.claim(userId, req.guestAssessmentId())));
  }
}
