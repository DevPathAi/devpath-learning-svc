package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionValidatorTest {

  private final QuestionValidator validator = new QuestionValidator();

  @Test
  void validQuestionSetPassesWithoutErrors() {
    var report = validator.validate(validQuestions());

    assertThat(report.errors()).isEmpty();
  }

  @Test
  void rejectsShortAnswer() {
    var questions = validQuestions();
    questions.set(0, withType(questions.get(0), "SHORT_ANSWER"));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("SHORT_ANSWER"));
  }

  @Test
  void rejectsCreateBloom() {
    var questions = validQuestions();
    questions.set(0, withBloom(questions.get(0), "CREATE"));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("CREATE"));
  }

  @Test
  void rejectsAnswerKeyOutsideOptions() {
    var questions = validQuestions();
    questions.set(0, withCorrect(questions.get(0), 9));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("correct").contains("options"));
  }

  @Test
  void rejectsTrackCountOtherThanOneHundred() {
    var questions = validQuestions();
    questions.remove(0);

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("BACKEND_SPRING").contains("100"));
  }

  @Test
  void rejectsWrongQuestionTypeQuota() {
    var questions = validQuestions();
    questions.set(0, withType(questions.get(0), "CODE_READING"));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("MCQ").contains("70"));
    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("CODE_READING").contains("30"));
  }

  @Test
  void rejectsDifficultyOutsideZeroToOne() {
    var questions = validQuestions();
    questions.set(0, withDifficulty(questions.get(0), 1.1));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("difficulty"));
  }

  @Test
  void rejectsMissingOrNonKebabConceptTags() {
    var missing = validQuestions();
    missing.set(0, withTags(missing.get(0), List.of()));
    var nonKebab = validQuestions();
    nonKebab.set(0, withTags(nonKebab.get(0), List.of("SpringCore")));

    assertThat(validator.validate(missing).errors()).anySatisfy(error ->
        assertThat(error).contains("conceptTags"));
    assertThat(validator.validate(nonKebab).errors()).anySatisfy(error ->
        assertThat(error).contains("kebab-case"));
  }

  @Test
  void distributionDriftIsWarningNotError() {
    var questions = validQuestions();
    questions.set(0, withDifficulty(questions.get(0), 0.25));

    var report = validator.validate(questions);

    assertThat(report.errors()).isEmpty();
    assertThat(report.warnings()).isNotEmpty();
  }

  static List<ApprovedQuestion> validQuestions() {
    var questions = new ArrayList<ApprovedQuestion>();
    for (String track : QuestionQuota.TRACKS) {
      for (int i = 0; i < 100; i++) {
        questions.add(question(track, i));
      }
    }
    return questions;
  }

  private static ApprovedQuestion question(String track, int index) {
    var type = index < 70 ? "MCQ" : "CODE_READING";
    var bloom = bloom(index);
    var difficulty = difficulty(index);
    var tag = track.toLowerCase().replace('_', '-') + "-" + (index % 10);
    return new ApprovedQuestion(
        track,
        type,
        track + " diagnostic question " + index,
        List.of("A", "B", "C", "D"),
        new ApprovedQuestion.AnswerKey(index % 4),
        bloom,
        difficulty,
        List.of(tag),
        "Explanation " + index);
  }

  private static String bloom(int index) {
    if (index < 10) return "REMEMBER";
    if (index < 35) return "UNDERSTAND";
    if (index < 65) return "APPLY";
    if (index < 90) return "ANALYZE";
    return "EVALUATE";
  }

  private static double difficulty(int index) {
    if (index < 10) return index % 2 == 0 ? 0.1 : 0.2;
    if (index < 35) return index % 2 == 0 ? 0.3 : 0.4;
    if (index < 65) return index % 2 == 0 ? 0.5 : 0.6;
    if (index < 90) return index % 2 == 0 ? 0.7 : 0.8;
    return 0.9;
  }

  private static ApprovedQuestion withType(ApprovedQuestion q, String type) {
    return new ApprovedQuestion(q.track(), type, q.content(), q.options(), q.answerKey(),
        q.bloomLevel(), q.difficulty(), q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withBloom(ApprovedQuestion q, String bloom) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), q.options(), q.answerKey(),
        bloom, q.difficulty(), q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withCorrect(ApprovedQuestion q, int correct) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), q.options(),
        new ApprovedQuestion.AnswerKey(correct), q.bloomLevel(), q.difficulty(),
        q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withDifficulty(ApprovedQuestion q, double difficulty) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), q.options(), q.answerKey(),
        q.bloomLevel(), difficulty, q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withTags(ApprovedQuestion q, List<String> tags) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), q.options(), q.answerKey(),
        q.bloomLevel(), q.difficulty(), tags, q.explanation());
  }
}
