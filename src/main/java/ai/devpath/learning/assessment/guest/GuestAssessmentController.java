package ai.devpath.learning.assessment.guest;

import ai.devpath.learning.assessment.dto.*;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding/assessments/guest")
public class GuestAssessmentController {

  private final GuestAssessmentService service;

  public GuestAssessmentController(GuestAssessmentService service) { this.service = service; }

  @PostMapping
  public ResponseEntity<Map<String, String>> start(@RequestBody StartAssessmentRequest req) {
    return ResponseEntity.ok(Map.of("guestAssessmentId", service.start(req.track())));
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
  public ResponseEntity<AssessmentResultView> complete(@PathVariable String gid) {
    return ResponseEntity.ok(service.complete(gid));
  }
}
