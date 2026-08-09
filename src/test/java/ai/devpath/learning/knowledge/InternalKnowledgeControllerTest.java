package ai.devpath.learning.knowledge;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class InternalKnowledgeControllerTest {

  @Autowired WebApplicationContext context;
  @MockitoBean KnowledgeEmbeddingMatcher matcher;

  private MockMvc mockMvc() {
    return MockMvcBuilders.webAppContextSetup(context).build();
  }

  private String body(int limit) {
    String vector = String.join(",", Collections.nCopies(768, "0.1"));
    return "{\"embedding\":[" + vector + "],\"limit\":" + limit + "}";
  }

  @Test
  void returnsChunkTextInResponse() throws Exception {
    when(matcher.search(anyList(), anyInt())).thenReturn(List.of(
        new KnowledgeChunk("AWS/a.md", "AWS 개념", "AWS", "본문 내용", 0.12)));

    mockMvc().perform(post("/internal/knowledge/similar")
            .contentType(MediaType.APPLICATION_JSON).content(body(3)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].chunkText").value("본문 내용"))
        .andExpect(jsonPath("$[0].docKey").value("AWS/a.md"))
        .andExpect(jsonPath("$[0].category").value("AWS"));
  }

  @Test
  void clampsLimitToMaximum() throws Exception {
    when(matcher.search(anyList(), anyInt())).thenReturn(List.of());

    mockMvc().perform(post("/internal/knowledge/similar")
            .contentType(MediaType.APPLICATION_JSON).content(body(999)))
        .andExpect(status().isOk());

    verify(matcher).search(anyList(), eq(10));
  }

  @Test
  void defaultsLimitWhenAbsent() throws Exception {
    when(matcher.search(anyList(), anyInt())).thenReturn(List.of());
    String vector = String.join(",", Collections.nCopies(768, "0.1"));

    mockMvc().perform(post("/internal/knowledge/similar")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"embedding\":[" + vector + "]}"))
        .andExpect(status().isOk());

    verify(matcher).search(anyList(), eq(3));
  }
}
