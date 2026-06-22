package ai.devpath.learning.path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LearningPathConflictMappingTest {
  @Autowired MockMvc mvc;
  @MockitoBean LearningPathGenerationService generation;

  @Test
  void regenerateReturns409WithErrorCodeOnConflict() throws Exception {
    when(generation.generate(anyLong(), any(), any()))
        .thenThrow(new ActivePathConflictException("PATH_GENERATION_CONFLICT: dup", null));

    mvc.perform(post("/learning-paths/me/regenerate")
            .with(jwt().jwt(j -> j.subject("42")))
            .contentType(MediaType.APPLICATION_JSON).content("{\"goal\":\"g\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.errorCode").value("PATH_GENERATION_CONFLICT"));
  }

  @Test
  void generateSseEmitsErrorStageWithConflictCode() throws Exception {
    when(generation.generate(anyLong(), any(), any()))
        .thenThrow(new ActivePathConflictException("PATH_GENERATION_CONFLICT: dup", null));

    var result = mvc.perform(post("/learning-paths/me/generate")
            .with(jwt().jwt(j -> j.subject("42"))))
        .andExpect(request().asyncStarted()).andReturn();
    String sse = mvc.perform(asyncDispatch(result)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    org.assertj.core.api.Assertions.assertThat(sse).contains("\"stage\":\"error\"");
    org.assertj.core.api.Assertions.assertThat(sse).contains("PATH_GENERATION_CONFLICT");
  }
}
