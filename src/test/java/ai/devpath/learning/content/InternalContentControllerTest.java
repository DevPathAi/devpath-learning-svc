package ai.devpath.learning.content;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InternalContentControllerTest {

  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  @BeforeEach
  void reset() {
    jdbc.execute("""
        TRUNCATE user_content_progress, path_weekly_tasks, path_milestones,
          learning_paths, content_embeddings, contents
        RESTART IDENTITY CASCADE
        """);
  }

  @Test
  void returnsPublishedContentBodyWithoutAuth() throws Exception {
    long id = seedContent("spring-tx-boundary", "BACKEND_SPRING", "PUBLISHED",
        "## Transaction Boundary\nRead this body.");

    mvc.perform(get("/internal/contents/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.slug").value("spring-tx-boundary"))
        .andExpect(jsonPath("$.title").value("spring-tx-boundary"))
        .andExpect(jsonPath("$.track").value("BACKEND_SPRING"))
        .andExpect(jsonPath("$.body").value("## Transaction Boundary\nRead this body."));
  }

  @Test
  void truncatesLongBodyTo4000Chars() throws Exception {
    String longBody = "x".repeat(5000);
    long id = seedContent("long-body", "BACKEND_SPRING", "PUBLISHED", longBody);

    mvc.perform(get("/internal/contents/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.body").value(("x".repeat(4000)) + "…"));
  }

  @Test
  void missingContentReturns404() throws Exception {
    mvc.perform(get("/internal/contents/999999999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void draftContentReturns404() throws Exception {
    long id = seedContent("draft-content", "BACKEND_SPRING", "DRAFT", "## Draft");

    mvc.perform(get("/internal/contents/" + id))
        .andExpect(status().isNotFound());
  }

  private long seedContent(String slug, String track, String status, String markdown) {
    return jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, ?, 10, 0.4, 'APPLY', cast('["tag"]' as jsonb), ?)
        returning id
        """, Long.class, slug, slug, track, markdown, status);
  }
}
