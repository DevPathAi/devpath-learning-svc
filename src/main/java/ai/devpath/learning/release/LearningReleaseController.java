package ai.devpath.learning.release;

import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/release/learning/{candidate}/{runKey}")
@ConditionalOnProperty(name = "devpath.release.enabled", havingValue = "true")
public class LearningReleaseController {
  private final LearningReleaseRegistry release;
  private final LearningReleaseVerificationService verification;

  public LearningReleaseController(
      LearningReleaseRegistry release,
      LearningReleaseVerificationService verification) {
    this.release = release;
    this.verification = verification;
  }

  @PostMapping("/commands/{command}")
  public Map<String, Object> command(
      @PathVariable String candidate,
      @PathVariable String runKey,
      @PathVariable String command,
      @RequestBody(required = false) Map<String, Object> body) {
    Object rawUserId = body == null ? null : body.get("user_id");
    if (!(rawUserId instanceof Number number) || number.longValue() <= 0) {
      throw new IllegalArgumentException("release fixture user id is required");
    }
    long userId = number.longValue();
    switch (command) {
      case "replay-claim" -> release.armClaimReplay(candidate, runKey, userId);
      case "replay-content-linked-completion" ->
          verification.replayContentLinked(candidate, runKey, userId);
      case "replay-contentless-completion" ->
          verification.replayContentless(candidate, runKey, userId);
      default -> throw new IllegalArgumentException("unsupported Learning release command");
    }
    return Map.of("accepted", true);
  }

  @GetMapping("/checkpoints/{checkpoint}")
  public Map<String, Object> checkpoint(
      @PathVariable String candidate,
      @PathVariable String runKey,
      @PathVariable String checkpoint,
      @RequestParam(name = "user_id", required = false) Long userId) {
    return Map.of(
        "passed", verification.checkpoint(candidate, runKey, userId, checkpoint));
  }
}
