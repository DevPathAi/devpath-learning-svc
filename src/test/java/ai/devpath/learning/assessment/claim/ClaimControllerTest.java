package ai.devpath.learning.assessment.claim;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.devpath.learning.assessment.AssessmentRepository;
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
class ClaimControllerTest {

  @Autowired MockMvc mvc;
  @Autowired AssessmentRepository assessments;

  @Test
  void claimMigratesGuestToMember() throws Exception {
    String start = mvc.perform(post("/onboarding/assessments/guest")
            .contentType(MediaType.APPLICATION_JSON).content("{\"track\":\"BACKEND_SPRING\"}"))
        .andReturn().getResponse().getContentAsString();
    String gid = com.jayway.jsonpath.JsonPath.parse(start).read("$.guestAssessmentId");
    for (int i = 0; i < 15; i++) {
      String next = mvc.perform(get("/onboarding/assessments/guest/" + gid + "/next"))
          .andReturn().getResponse().getContentAsString();
      long qid = com.jayway.jsonpath.JsonPath.parse(next).read("$.question.id", Long.class);
      mvc.perform(post("/onboarding/assessments/guest/" + gid + "/answer")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"questionId\":" + qid + ",\"answer\":\"{\\\"correct\\\":0}\",\"skipped\":false,\"timeSpentSec\":3}"));
    }
    mvc.perform(post("/onboarding/assessments/guest/" + gid + "/complete"));

    long before = assessments.count();
    var auth = jwt().jwt(j -> j.subject("99"));
    mvc.perform(post("/onboarding/assessments/claim").with(auth)
            .contentType(MediaType.APPLICATION_JSON).content("{\"guest_assessment_id\":\"" + gid + "\"}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.assessmentId").exists());
    org.assertj.core.api.Assertions.assertThat(assessments.count()).isEqualTo(before + 1);

    // 멱등: 재요청은 추가 행 없음
    mvc.perform(post("/onboarding/assessments/claim").with(auth)
        .contentType(MediaType.APPLICATION_JSON).content("{\"guest_assessment_id\":\"" + gid + "\"}"))
        .andExpect(status().isOk());
    org.assertj.core.api.Assertions.assertThat(assessments.count()).isEqualTo(before + 1);
  }
}
