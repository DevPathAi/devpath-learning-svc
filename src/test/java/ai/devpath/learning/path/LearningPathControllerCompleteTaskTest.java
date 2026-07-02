package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LearningPathControllerCompleteTaskTest {

  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  private long newTaskWithoutContent(long userId) {
    Long pathId = jdbc.queryForObject("""
        INSERT INTO learning_paths(user_id, generated_at, track, total_weeks, status)
        VALUES (?, now(), 'BACKEND_SPRING', 12, 'ACTIVE') RETURNING id
        """, Long.class, userId);
    Long milestoneId = jdbc.queryForObject("""
        INSERT INTO path_milestones(path_id, week_num, title) VALUES (?, 1, 'w1') RETURNING id
        """, Long.class, pathId);
    return jdbc.queryForObject("""
        INSERT INTO path_weekly_tasks(milestone_id, order_num, task_type, title, required)
        VALUES (?, 1, 'PRACTICE', 'task without content', true) RETURNING id
        """, Long.class, milestoneId);
  }

  @Test
  void completeTaskReturns204ForOwner() throws Exception {
    long userId = 888101L;
    long taskId = newTaskWithoutContent(userId);
    Jwt jwt = Jwt.withTokenValue("t").header("alg", "none").subject(String.valueOf(userId)).build();

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", taskId)
            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
        .andExpect(status().isNoContent());
  }

  @Test
  void completeTaskReturns404ForNonOwnerOrMissing() throws Exception {
    long ownerUserId = 888102L;
    long otherUserId = 888103L;
    long taskId = newTaskWithoutContent(ownerUserId);
    Jwt jwt = Jwt.withTokenValue("t").header("alg", "none").subject(String.valueOf(otherUserId)).build();

    mvc.perform(post("/learning-paths/tasks/{taskId}/complete", taskId)
            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt)))
        .andExpect(status().isNotFound());
  }
}
