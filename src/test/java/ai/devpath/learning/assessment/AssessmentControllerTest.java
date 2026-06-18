package ai.devpath.learning.assessment;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AssessmentControllerTest {

  @Autowired MockMvc mvc;
  @Autowired QuestionBankRepository questions;

  private void seed() {
    for (int i = 0; i < 20; i++) {
      QuestionBank q = new QuestionBank();
      q.setTrack("BACKEND_SPRING");
      q.setQuestionType("MCQ");
      q.setContent("Q" + i);
      q.setOptions("[\"a\",\"b\"]");
      q.setAnswerKey("{\"correct\":0}");
      q.setBloomLevel("UNDERSTAND");
      q.setDifficulty(0.1 * (i % 10));
      q.setConceptTags("[\"c" + (i % 3) + "\"]");
      questions.save(q);
    }
  }

  private long start(RequestPostProcessor auth) throws Exception {
    String body = mvc.perform(post("/onboarding/assessments").with(auth)
            .contentType(MediaType.APPLICATION_JSON).content("{\"track\":\"BACKEND_SPRING\"}"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    return com.jayway.jsonpath.JsonPath.parse(body).read("$.assessmentId", Long.class);
  }

  @Test
  void memberFlowStartToResult() throws Exception {
    seed();
    var auth = jwt().jwt(j -> j.subject("42"));
    long id = start(auth);
    for (int i = 0; i < 15; i++) {
      String next = mvc.perform(get("/onboarding/assessments/" + id + "/next").with(auth))
          .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
      long qid = com.jayway.jsonpath.JsonPath.parse(next).read("$.question.id", Long.class);
      mvc.perform(post("/onboarding/assessments/" + id + "/answer").with(auth)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"questionId\":" + qid + ",\"answer\":\"{\\\"correct\\\":0}\",\"skipped\":false,\"timeSpentSec\":5}"))
          .andExpect(status().isOk());
    }
    mvc.perform(get("/onboarding/assessments/" + id + "/next").with(auth)).andExpect(status().isNoContent());
    mvc.perform(post("/onboarding/assessments/" + id + "/complete").with(auth))
        .andExpect(status().isOk()).andExpect(jsonPath("$.diagnosedLevel").exists());
    mvc.perform(get("/onboarding/assessments/" + id + "/result").with(auth))
        .andExpect(status().isOk()).andExpect(jsonPath("$.diagnosedLevel").exists());
  }

  @Test
  void repeatedNextReturnsSameQuestion() throws Exception {
    seed();
    var auth = jwt().jwt(j -> j.subject("43"));
    long id = start(auth);
    String n1 = mvc.perform(get("/onboarding/assessments/" + id + "/next").with(auth))
        .andReturn().getResponse().getContentAsString();
    String n2 = mvc.perform(get("/onboarding/assessments/" + id + "/next").with(auth))
        .andReturn().getResponse().getContentAsString();
    long q1 = com.jayway.jsonpath.JsonPath.parse(n1).read("$.question.id", Long.class);
    long q2 = com.jayway.jsonpath.JsonPath.parse(n2).read("$.question.id", Long.class);
    org.assertj.core.api.Assertions.assertThat(q1).isEqualTo(q2); // outstanding 재발급
  }

  @Test
  void arbitraryAnswerIsRejected() throws Exception {
    seed();
    var auth = jwt().jwt(j -> j.subject("44"));
    long id = start(auth);
    mvc.perform(get("/onboarding/assessments/" + id + "/next").with(auth)).andExpect(status().isOk());
    mvc.perform(post("/onboarding/assessments/" + id + "/answer").with(auth)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"questionId\":999999,\"answer\":\"{}\",\"skipped\":false,\"timeSpentSec\":1}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void completeBeforeFifteenIsRejected() throws Exception {
    seed();
    var auth = jwt().jwt(j -> j.subject("45"));
    long id = start(auth);
    mvc.perform(post("/onboarding/assessments/" + id + "/complete").with(auth))
        .andExpect(status().isConflict());
  }
}
