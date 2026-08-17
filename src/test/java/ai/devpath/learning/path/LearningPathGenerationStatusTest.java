package ai.devpath.learning.path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * SSE 가 끊겨도 사용자가 결과를 되찾을 수 있어야 한다. 생성 상태를 별도로 조회한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LearningPathGenerationStatusTest {

  @Autowired MockMvc mvc;
  @MockitoBean PathGenerator pathGenerator;

  /**
   * 매핑이 없어도 404 가 나오므로 "없음"을 404 로 표현하면 이 테스트는 구현 없이도 통과한다.
   * 판별력을 위해 없음도 200 + state=NONE 으로 응답한다.
   */
  @Test
  void statusReportsNoneBeforeAnyGeneration() throws Exception {
    mvc.perform(get("/learning-paths/me/generation").with(jwt().jwt(j -> j.subject("770001"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.state").value("NONE"));
  }

  @Test
  void statusReportsThePathIdAfterGenerationFinished() throws Exception {
    long userId = 770002L;
    doAnswer(invocation -> {
      Consumer<PathProgressEvent> progress = invocation.getArgument(2);
      progress.accept(PathProgressEvent.collecting());
      progress.accept(PathProgressEvent.done(1234L));
      return null;
    }).when(pathGenerator).generatePath(anyLong(), any(), any());

    streamGeneration(userId);

    mvc.perform(get("/learning-paths/me/generation").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.state").value("SUCCEEDED"))
        .andExpect(jsonPath("$.pathId").value(1234));
  }

  @Test
  void statusReportsFailureAfterGenerationFailed() throws Exception {
    long userId = 770003L;
    doThrow(new AiServiceUnavailableException("ai-svc path generate failed", new IllegalStateException("timeout")))
        .when(pathGenerator).generatePath(anyLong(), any(), any());

    streamGeneration(userId);

    mvc.perform(get("/learning-paths/me/generation").with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.state").value("FAILED"))
        .andExpect(jsonPath("$.errorMessage").value("ai-svc path generate failed"));
  }

  private void streamGeneration(long userId) throws Exception {
    var started = mvc.perform(post("/learning-paths/me/generate")
            .with(jwt().jwt(j -> j.subject(String.valueOf(userId)))))
        .andExpect(request().asyncStarted())
        .andReturn();
    mvc.perform(asyncDispatch(started)).andExpect(status().isOk());
  }
}
