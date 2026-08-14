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
  void rejectsAnswerLengthRankBiasAtLongest() {
    var questions = validQuestions();
    applyLengthRankBias(questions, "FULLSTACK", 4, 70);

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("answer length rank bias").contains("FULLSTACK"));
  }

  /**
   * 「유일 최장 금지」게이트를 오답 하나만 정답보다 살짝 길게 만들어 우회한 실제 사고의
   * 재현 테스트. 정답을 "두 번째로 긴 보기"(오름차순 rank 3 of 4)로 몰아넣어도 걸려야 한다 —
   * 이것이 이번 수정의 핵심 증거다.
   */
  @Test
  void rejectsAnswerLengthRankBiasAtSecondLongest() {
    var questions = validQuestions();
    applyLengthRankBias(questions, "MOBILE_FLUTTER", 3, 70);

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("answer length rank bias").contains("MOBILE_FLUTTER"));
  }

  @Test
  void doesNotCountTiedRankOptions() {
    var report = validator.validate(validQuestions());

    assertThat(report.errors()).noneMatch(error -> error.contains("answer length rank bias"));
  }

  @Test
  void answerLengthRankBiasBoundaryAtSixtyPercent() {
    var atBoundary = validQuestions();
    applyLengthRankAcrossTrack(atBoundary, "DEVOPS", 4, 60);
    var boundaryReport = validator.validate(atBoundary);
    assertThat(boundaryReport.errors()).noneMatch(error -> error.contains("answer length rank bias"));

    var overBoundary = validQuestions();
    applyLengthRankAcrossTrack(overBoundary, "DEVOPS", 4, 61);
    var overReport = validator.validate(overBoundary);
    assertThat(overReport.errors()).anySatisfy(error ->
        assertThat(error).contains("answer length rank bias").contains("DEVOPS"));
  }

  /**
   * 표본(동률 아닌 채로 셀 수 있는 문항)이 20개 미만이면 비율이 100%여도 판정하지 않는다.
   */
  @Test
  void skipsAnswerLengthRankBiasJudgmentBelowMinimumSample() {
    var questions = validQuestions();
    applyLengthRankBias(questions, "FRONTEND_REACT", 4, 10);

    var report = validator.validate(questions);

    assertThat(report.errors()).noneMatch(error -> error.contains("answer length rank bias"));
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

  /** 순위 배치용 패딩 단위. 공백이 아니므로 normalize()의 공백 정규화에 영향받지 않는다. */
  private static final String PAD_UNIT = "가";

  /**
   * 지정한 트랙의 앞쪽 {@code count}개 문항만 네 보기 길이를 모두 다르게(동률 없이) 만들어
   * 정답이 {@code targetRank}(1=최단 … N=최장)가 되게 한다. 나머지 문항은 손대지 않아
   * 픽스처 그대로(네 보기 동률) 남는다 — 그 문항들은 게이트의 동률 배제 규칙에 걸려
   * 분모에서도 빠진다. 표본수·동률 배제 테스트에 쓴다.
   */
  private static void applyLengthRankBias(List<ApprovedQuestion> questions, String track, int targetRank, int count) {
    int applied = 0;
    for (int i = 0; i < questions.size() && applied < count; i++) {
      var q = questions.get(i);
      if (!track.equals(q.track())) continue;
      questions.set(i, withOptions(q, rankedOptions(q.options(), q.answerKey().correct(), targetRank)));
      applied++;
    }
  }

  /**
   * 트랙 전체(100문항)에 네 보기 길이를 모두 다르게(동률 없이) 만들되, 앞쪽
   * {@code majorityCount}개는 정답이 {@code targetRank}가 되게 하고 나머지는 남은 순위에
   * 고르게 분산시킨다. 트랙 전체가 항상 카운트되므로(분모=100 고정) 경계값(60%) 테스트에 쓴다.
   */
  private static void applyLengthRankAcrossTrack(
      List<ApprovedQuestion> questions, String track, int targetRank, int majorityCount) {
    var otherRanks = new ArrayList<Integer>();
    for (int r = 1; r <= 4; r++) {
      if (r != targetRank) otherRanks.add(r);
    }
    int matched = 0;
    int otherIdx = 0;
    for (int i = 0; i < questions.size(); i++) {
      var q = questions.get(i);
      if (!track.equals(q.track())) continue;
      int rank = matched < majorityCount ? targetRank : otherRanks.get(otherIdx++ % otherRanks.size());
      questions.set(i, withOptions(q, rankedOptions(q.options(), q.answerKey().correct(), rank)));
      matched++;
    }
  }

  /**
   * {@code options} 중 {@code correct} 위치가 정확히 {@code targetRank}(1=최단 … N=최장)가
   * 되도록 각 보기 뒤에 순위별로 서로 다른 길이의 패딩을 붙인다. 나머지 보기들도 서로 전부
   * 다른 길이를 갖게 되어 동률이 전혀 없는 문항이 된다.
   */
  private static List<String> rankedOptions(List<String> options, int correct, int targetRank) {
    int n = options.size();
    var otherRanks = new ArrayList<Integer>();
    for (int r = 1; r <= n; r++) {
      if (r != targetRank) otherRanks.add(r);
    }
    var result = new ArrayList<String>(options);
    int ri = 0;
    for (int idx = 0; idx < n; idx++) {
      int rank = idx == correct ? targetRank : otherRanks.get(ri++);
      result.set(idx, options.get(idx) + PAD_UNIT.repeat(rank * 4));
    }
    return result;
  }
}
