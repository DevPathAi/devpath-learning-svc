package ai.devpath.learning.content;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contents")
public class ContentController {
  private final ContentService service;

  public ContentController(ContentService service) {
    this.service = service;
  }

  @GetMapping("/{idOrSlug}")
  public ResponseEntity<LearningContentView> get(
      @AuthenticationPrincipal Jwt jwt, @PathVariable String idOrSlug) {
    return ResponseEntity.ok(service.get(uid(jwt), idOrSlug));
  }

  @PostMapping("/{idOrSlug}/progress")
  public ResponseEntity<UpsertContentProgressResponse> progress(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable String idOrSlug,
      @RequestBody(required = false) UpsertContentProgressRequest request) {
    return ResponseEntity.ok(service.upsertProgress(uid(jwt), idOrSlug, request));
  }

  @GetMapping("/me/progress")
  public ResponseEntity<ProgressListView> myProgress(
      @AuthenticationPrincipal Jwt jwt,
      @RequestParam(required = false) Boolean completed,
      @RequestParam(required = false) String track,
      @RequestParam(required = false) Integer limit) {
    return ResponseEntity.ok(service.myProgress(uid(jwt), completed, track, limit));
  }

  private static long uid(Jwt jwt) {
    return Long.parseLong(jwt.getSubject());
  }
}
