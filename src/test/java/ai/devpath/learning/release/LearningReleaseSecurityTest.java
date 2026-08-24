package ai.devpath.learning.release;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.devpath.learning.config.ReleaseInternalAuthenticationFilter;
import ai.devpath.learning.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LearningReleaseController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "devpath.release.enabled=true",
    "devpath.auth.internal-token=release-internal-token"
})
class LearningReleaseSecurityTest {
  @Autowired MockMvc mvc;
  @MockitoBean LearningReleaseRegistry release;
  @MockitoBean LearningReleaseVerificationService verification;

  @Test
  void releaseCheckpointRequiresWorkloadAuthWhileOtherInternalContractIsUnchanged()
      throws Exception {
    String candidate = "a".repeat(64);
    String run = "R".repeat(43);
    String path = "/internal/release/learning/" + candidate + "/" + run
        + "/checkpoints/guest-preview-owned-by-guest";
    when(verification.checkpoint(
        candidate, run, null, "guest-preview-owned-by-guest")).thenReturn(true);

    mvc.perform(get(path)).andExpect(status().isUnauthorized());
    mvc.perform(get(path).header(
            ReleaseInternalAuthenticationFilter.HEADER, "release-internal-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.passed").value(true));

    mvc.perform(get("/internal/contents/999999999"))
        .andExpect(status().isNotFound());
  }
}
