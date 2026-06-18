package ai.devpath.learning.assessment.guest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/seed/question_bank_seed.sql")
class GuestAssessmentControllerTest {

  @Autowired MockMvc mvc;

  @Test
  void guestFlowStartToResultNoAuth() throws Exception {
    String start = mvc.perform(post("/onboarding/assessments/guest")
            .contentType(MediaType.APPLICATION_JSON).content("{\"track\":\"BACKEND_SPRING\"}"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    String gid = com.jayway.jsonpath.JsonPath.parse(start).read("$.guestAssessmentId");

    for (int i = 0; i < 15; i++) {
      String next = mvc.perform(get("/onboarding/assessments/guest/" + gid + "/next"))
          .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
      long qid = com.jayway.jsonpath.JsonPath.parse(next).read("$.question.id", Long.class);
      mvc.perform(post("/onboarding/assessments/guest/" + gid + "/answer")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"questionId\":" + qid + ",\"answer\":\"{\\\"correct\\\":0}\",\"skipped\":false,\"timeSpentSec\":3}"))
          .andExpect(status().isOk());
    }
    mvc.perform(post("/onboarding/assessments/guest/" + gid + "/complete"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.diagnosedLevel").exists());
  }
}
