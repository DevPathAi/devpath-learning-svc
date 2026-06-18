package ai.devpath.learning.seed;

import ai.devpath.learning.assessment.QuestionBank;
import ai.devpath.learning.assessment.QuestionBankRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** 로컬(dev) 끝단간용 최소 문항 시드. 비어있을 때만 적재. */
@Component
@Profile("dev")
public class QuestionBankSeeder implements CommandLineRunner {

  private final QuestionBankRepository questions;

  public QuestionBankSeeder(QuestionBankRepository questions) { this.questions = questions; }

  @Override
  public void run(String... args) {
    if (!questions.findByTrack("BACKEND_SPRING").isEmpty()) return;
    for (int i = 0; i < 17; i++) {
      QuestionBank q = new QuestionBank();
      q.setTrack("BACKEND_SPRING");
      q.setQuestionType("MCQ");
      q.setContent("샘플 진단 문항 " + (i + 1));
      q.setOptions("[\"a\",\"b\"]");
      q.setAnswerKey("{\"correct\":0}");
      q.setBloomLevel(new String[]{"REMEMBER","UNDERSTAND","APPLY","ANALYZE","EVALUATE"}[i % 5]);
      q.setDifficulty(0.1 + (i % 9) * 0.1);
      q.setConceptTags("[\"concept-" + (i % 4) + "\"]");
      questions.save(q);
    }
  }
}
