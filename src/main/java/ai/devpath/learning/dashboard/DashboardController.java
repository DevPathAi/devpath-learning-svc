package ai.devpath.learning.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
  private final DashboardService dashboards;

  public DashboardController(DashboardService dashboards) {
    this.dashboards = dashboards;
  }

  @GetMapping("/me")
  public ResponseEntity<DashboardSummary> me(@AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.ok(dashboards.summary(Long.parseLong(jwt.getSubject())));
  }
}
