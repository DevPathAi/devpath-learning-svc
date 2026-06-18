package ai.devpath.learning.assessment;

import ai.devpath.learning.assessment.dto.*;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding/assessments")
public class AssessmentController {

  private final AssessmentService service;

  public AssessmentController(AssessmentService service) { this.service = service; }

  private static long uid(Jwt jwt) { return Long.parseLong(jwt.getSubject()); }

  @PostMapping
  public ResponseEntity<Map<String, Long>> start(@AuthenticationPrincipal Jwt jwt,
      @RequestBody StartAssessmentRequest req) {
    return ResponseEntity.ok(Map.of("assessmentId", service.start(uid(jwt), req.track())));
  }

  @GetMapping("/{id}/next")
  public ResponseEntity<NextQuestionResponse> next(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
    return service.next(uid(jwt), id).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{id}/answer")
  public ResponseEntity<Void> answer(@AuthenticationPrincipal Jwt jwt, @PathVariable long id,
      @RequestBody AnswerRequest req) {
    service.answer(uid(jwt), id, req);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/complete")
  public ResponseEntity<AssessmentResultView> complete(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
    return ResponseEntity.ok(service.complete(uid(jwt), id));
  }

  @GetMapping("/{id}/result")
  public ResponseEntity<AssessmentResultView> result(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
    return service.result(uid(jwt), id).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
