package ai.devpath.learning.assessment.guest;

import ai.devpath.learning.assessment.dto.*;
import ai.devpath.learning.release.LearningReleaseRegistry;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding/assessments/guest")
public class GuestAssessmentController {

  private final GuestAssessmentService service;
  private final LearningReleaseRegistry release;

  public GuestAssessmentController(
      GuestAssessmentService service, LearningReleaseRegistry release) {
    this.service = service;
    this.release = release;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> start(
      @RequestBody StartAssessmentRequest req,
      @RequestHeader(name = "X-Candidate-Spec-Sha256", required = false) String candidate,
      @RequestHeader(name = "X-Release-Run-Key", required = false) String runKey) {
    String guestId = service.start(req.track());
    release.recordGuestStarted(candidate, runKey, guestId);
    return ResponseEntity.ok(Map.of("guestAssessmentId", guestId));
  }

  @GetMapping("/{gid}/next")
  public ResponseEntity<NextQuestionResponse> next(@PathVariable String gid) {
    return service.next(gid).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{gid}/answer")
  public ResponseEntity<Void> answer(@PathVariable String gid, @RequestBody AnswerRequest req) {
    service.answer(gid, req);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{gid}/complete")
  public ResponseEntity<AssessmentResultView> complete(
      @PathVariable String gid,
      @RequestHeader(name = "X-Candidate-Spec-Sha256", required = false) String candidate,
      @RequestHeader(name = "X-Release-Run-Key", required = false) String runKey) {
    AssessmentResultView result = service.complete(gid);
    release.recordGuestCompleted(candidate, runKey, gid, result);
    return ResponseEntity.ok(result);
  }
}
