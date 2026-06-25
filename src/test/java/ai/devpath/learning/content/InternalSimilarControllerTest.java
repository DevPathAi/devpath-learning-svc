package ai.devpath.learning.content;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InternalSimilarControllerTest {

  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;
  @Autowired JsonMapper jsonMapper;

  @BeforeEach
  void reset() {
    jdbc.execute("TRUNCATE content_embeddings, contents RESTART IDENTITY CASCADE");
  }

  @Test
  void similarWithoutTrackReturnsAcrossTracks() throws Exception {
    long backend = seedContentWithEmbedding("backend-one", "BACKEND_SPRING", 0.10);
    long frontend = seedContentWithEmbedding("frontend-one", "FRONTEND_REACT", 0.11);

    String body = jsonMapper.writeValueAsString(
        new SimilarQuery(Collections.nCopies(768, 0.10), 5, null));

    mvc.perform(post("/internal/contents/similar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[*].contentId",
            org.hamcrest.Matchers.containsInAnyOrder((int) backend, (int) frontend)));
  }

  @Test
  void similarWithTrackFiltersToThatTrack() throws Exception {
    seedContentWithEmbedding("backend-one", "BACKEND_SPRING", 0.10);
    long frontend = seedContentWithEmbedding("frontend-one", "FRONTEND_REACT", 0.11);

    String body = jsonMapper.writeValueAsString(
        new SimilarQuery(Collections.nCopies(768, 0.11), 5, "FRONTEND_REACT"));

    mvc.perform(post("/internal/contents/similar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].contentId").value((int) frontend))
        .andExpect(jsonPath("$[0].slug").value("frontend-one"));
  }

  @Test
  void nullLimitDefaultsAndClampsToTen() throws Exception {
    for (int i = 0; i < 15; i++) {
      seedContentWithEmbedding("bulk-" + i, "BACKEND_SPRING", 0.10 + i * 0.0001);
    }

    String nullLimitBody = jsonMapper.writeValueAsString(
        new SimilarQuery(Collections.nCopies(768, 0.10), null, null));
    mvc.perform(post("/internal/contents/similar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nullLimitBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));

    String bigLimitBody = jsonMapper.writeValueAsString(
        new SimilarQuery(Collections.nCopies(768, 0.10), 100, null));
    mvc.perform(post("/internal/contents/similar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bigLimitBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(10));
  }

  /** PUBLISHED content + ACTIVE 768-dim embedding(상수 벡터) 1쌍 시드. */
  private long seedContentWithEmbedding(String slug, String track, double value) {
    long id = jdbc.queryForObject("""
        insert into contents(slug, title, track, content_md, estimated_minutes, difficulty,
          bloom_level, concept_tags, status)
        values (?, ?, ?, '## Body', 10, 0.4, 'APPLY', cast('["tag"]' as jsonb), 'PUBLISHED')
        returning id
        """, Long.class, slug, slug, track);
    String vector = "[" + String.join(",", Collections.nCopies(768, Double.toString(value))) + "]";
    jdbc.update("""
        insert into content_embeddings(content_id, chunk_index, chunk_text, embedding, chunk_hash, status)
        values (?, 0, 'chunk', cast(? as vector), 'hash-' || ?, 'ACTIVE')
        """, id, vector, slug);
    return id;
  }
}
