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

  @Test
  void rejectsDuplicateOptionSetInsideTrack() {
    var questions = validQuestions();
    questions.set(1, withOptions(questions.get(1), questions.get(0).options()));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate option set").contains("BACKEND_SPRING"));
  }

  @Test
  void rejectsDuplicateContent() {
    var questions = validQuestions();
    questions.set(1, withContent(questions.get(1), questions.get(0).content()));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate content"));
  }

  @Test
  void rejectsDuplicateOptionInsideQuestion() {
    var questions = validQuestions();
    var first = questions.get(0);
    questions.set(0, withOptions(first, List.of("같은 보기", "같은 보기", "다른 보기", "또 다른 보기")));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate option inside question"));
  }

  @Test
  void rejectsAnswerKeyBias() {
    var questions = validQuestions();
    for (int i = 0; i < questions.size(); i++) {
      if ("BACKEND_SPRING".equals(questions.get(i).track())) {
        questions.set(i, withCorrect(questions.get(i), 0));
      }
    }

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("answer key bias").contains("BACKEND_SPRING"));
  }

  @Test
  void rejectsLongestAnswerBias() {
    var questions = validQuestions();
    applyLongestAnswerBias(questions, "FULLSTACK", 70);

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("longest answer bias").contains("FULLSTACK"));
  }

  @Test
  void doesNotCountTiedLongestOptions() {
    var report = validator.validate(validQuestions());

    assertThat(report.errors()).noneMatch(error -> error.contains("longest answer bias"));
  }

  @Test
  void longestAnswerBiasBoundaryAtSixtyPercent() {
    var atBoundary = validQuestions();
    applyLongestAnswerBias(atBoundary, "DEVOPS", 60);
    var boundaryReport = validator.validate(atBoundary);
    assertThat(boundaryReport.errors()).noneMatch(error -> error.contains("longest answer bias"));

    var overBoundary = validQuestions();
    applyLongestAnswerBias(overBoundary, "DEVOPS", 61);
    var overReport = validator.validate(overBoundary);
    assertThat(overReport.errors()).anySatisfy(error ->
        assertThat(error).contains("longest answer bias").contains("DEVOPS"));
  }

  @Test
  void rejectsContentWithoutKorean() {
    var questions = validQuestions();
    questions.set(0, withContent(questions.get(0), "What is the default bean scope?"));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("content must contain Korean"));
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
        track + " 진단 문항 " + index + " — 무엇이 맞는가?",
        List.of(
            "보기 가 " + track + "-" + index,
            "보기 나 " + track + "-" + index,
            "보기 다 " + track + "-" + index,
            "보기 라 " + track + "-" + index),
        new ApprovedQuestion.AnswerKey(index % 4),
        bloom,
        difficulty,
        List.of(tag),
        "해설 " + index);
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

  private static ApprovedQuestion withOptions(ApprovedQuestion q, List<String> options) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), options, q.answerKey(),
        q.bloomLevel(), q.difficulty(), q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withContent(ApprovedQuestion q, String content) {
    return new ApprovedQuestion(q.track(), q.questionType(), content, q.options(), q.answerKey(),
        q.bloomLevel(), q.difficulty(), q.conceptTags(), q.explanation());
  }

  /**
   * 지정한 트랙의 앞쪽 {@code count}개 문항에서 정답 보기만 길게 늘려
   * "유일한 최장 보기 = 정답"이 되게 만든다. 기존 픽스처는 네 보기 길이가 모두 같으므로
   * 텍스트를 덧붙이기만 해도 즉시 유일한 최장 보기가 된다.
   */
  private static void applyLongestAnswerBias(List<ApprovedQuestion> questions, String track, int count) {
    int applied = 0;
    for (int i = 0; i < questions.size() && applied < count; i++) {
      var q = questions.get(i);
      if (!track.equals(q.track())) continue;
      int correct = q.answerKey().correct();
      var options = new ArrayList<>(q.options());
      options.set(correct, options.get(correct) + " 상세하게 설명하면 다음과 같은 이유로 정답입니다");
      questions.set(i, withOptions(q, options));
      applied++;
    }
  }
}
