package ai.devpath.learning.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

  @Autowired MockMvc mvc;

  @Test
  void healthIsPublic() throws Exception {
    mvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }

  @Test
  void protectedAssessmentRequiresAuth() throws Exception {
    mvc.perform(get("/onboarding/assessments/1/next")).andExpect(status().isUnauthorized());
  }

  @Test
  void internalPathIsPublic() throws Exception {
    // /internal/**는 permitAll — 미존재 콘텐츠는 401이 아니라 404로 수렴한다.
    mvc.perform(get("/internal/contents/999999999")).andExpect(status().isNotFound());
  }
}
