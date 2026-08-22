package ai.devpath.learning.contentgen.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuestionValidator {

  private static final Pattern KEBAB_TAG = Pattern.compile("[a-z0-9]+(?:-[a-z0-9]+)*");
  private static final Pattern HANGUL = Pattern.compile("[\\uAC00-\\uD7A3]");
  private static final List<String> ALLOWED_BLOOM = List.of(
      "REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE");
  private static final double MAX_ANSWER_KEY_SHARE = 0.5;
  /**
   * 정답 길이 순위(오름차순, 1=최단…N=최장) 중 어느 한 순위가 차지하는 비율의 상한.
   * 「유일 최장 60% 초과 금지」(구 게이트)는 오답 하나만 정답보다 살짝 길게 만들면 우회할 수
   * 있었다 — 실제로 그렇게 교정된 트랙에서 정답이 rank 3 of 4("두 번째로 긴 보기")에 몰렸다
   * (원래 결함 73%보다 강한 단서).
   *
   * 임계 근거(승인 JSONL 600줄을 이 게이트와 **동일한 알고리즘** — 정답과 길이가 동률인
   * 보기가 하나라도 있으면 그 문항을 분모에서 제외 — 로 재측정한 값): FULLSTACK 30.7%
   * (분모 75) · BACKEND_SPRING 45.9%(85) · DEVOPS 54.1%(85) · FRONTEND_REACT 53.9%(89) ·
   * MOBILE_FLUTTER 59.0%(83, rank4) · PYTHON_BACKEND(우회 교정 후) 44.8%(87, rank3).
   * 기존 5트랙 최댓값은 MOBILE_FLUTTER **59.0%** — 0.6 문턱까지 여유가 **1%p뿐**이다.
   * (분모를 단순히 100으로 놓고 잰 52%는 이 게이트의 동률 제외 규칙을 적용하지 않은 채
   * 같은 지표를 다른 방식으로 잰 값이었다 — 8%p 여유가 있다는 착시였다.)
   * 여유가 얇다고 임계를 올리지 말 것 — MOBILE_FLUTTER가 이미 쏠려 있다는 기존 자산의
   * 사실이지 게이트의 결함이 아니다. 이 트랙이 걸리면 임계가 아니라 데이터를 고친다.
   */
  private static final double MAX_ANSWER_LENGTH_RANK_SHARE = 0.6;
  /**
   * 표본(동률 아니어서 셀 수 있는 문항)이 이 값 미만인 트랙은 순위 비율을 판정하지 않는다.
   * 실제 트랙의 표본은 75~89(위 실측 근거 참고) — 20은 그보다 한참 아래라 정상적으로
   * 완성된 트랙에서는 절대 발동하지 않고, 트랙이 절반도 채 안 채워진 중간 상태에서만
   * 판정을 유예하는 취지다.
   */
  private static final int MIN_ANSWER_LENGTH_RANK_SAMPLE = 20;

  public QuestionValidationReport validate(List<ApprovedQuestion> questions) {
    var errors = new ArrayList<String>();
    var warnings = new ArrayList<String>();
    if (questions == null) {
      return new QuestionValidationReport(List.of("questions must not be null"), warnings);
    }

    for (int i = 0; i < questions.size(); i++) {
      validateQuestion(i + 1, questions.get(i), errors);
    }
    validateTrackQuotas(questions, errors);
    validateDuplicateOptionSets(questions, errors);
    validateDuplicateContents(questions, errors);
    validateAnswerKeyBias(questions, errors);
    validateAnswerLengthRankBias(questions, errors);
    validateDistributionWarnings(questions, warnings);
    return new QuestionValidationReport(List.copyOf(errors), List.copyOf(warnings));
  }

  /**
   * 구조·분포 게이트에 더해 **사실 검증 축**을 판정한다: 모든 문항은 검증 장부에서
   * fingerprint 가 일치하는 PASS 판정을 가져야 한다. 채점 필드가 바뀌면 fingerprint 가
   * 어긋나므로, 리뷰 루프 없이 문항을 추가·수정하는 경로가 구조적으로 막힌다
   * (2026-08-22 검수: 사실 검증 없는 게이트는 결함율 29.6%).
   */
  public QuestionValidationReport validate(List<ApprovedQuestion> questions,
      List<QuestionVerification> verifications) {
    var base = validate(questions);
    var errors = new ArrayList<>(base.errors());
    var warnings = new ArrayList<>(base.warnings());
    validateFactVerifications(questions, verifications, errors, warnings);
    return new QuestionValidationReport(List.copyOf(errors), List.copyOf(warnings));
  }

  private void validateFactVerifications(List<ApprovedQuestion> questions,
      List<QuestionVerification> verifications, List<String> errors, List<String> warnings) {
    if (verifications == null) {
      errors.add("fact verification manifest is required");
      return;
    }
    var byFingerprint = new HashMap<String, QuestionVerification>();
    for (QuestionVerification v : verifications) {
      if (v == null || blank(v.fingerprint())) {
        errors.add("verification entry without fingerprint");
        continue;
      }
      if (byFingerprint.putIfAbsent(v.fingerprint(), v) != null) {
        errors.add("duplicate verification fingerprint " + v.fingerprint());
        continue;
      }
      if (!QuestionVerification.VERDICT_PASS.equals(v.verdict())) {
        errors.add("verification " + v.fingerprint() + " verdict must be PASS but was "
            + v.verdict());
      }
      if (v.axes() == null || !v.axes().containsAll(QuestionVerification.REQUIRED_AXES)) {
        errors.add("verification " + v.fingerprint() + " axes must cover "
            + QuestionVerification.REQUIRED_AXES);
      }
      if (blank(v.reviewer()) || blank(v.reviewedAt())) {
        errors.add("verification " + v.fingerprint() + " must name reviewer and reviewedAt");
      }
    }

    var used = new HashSet<String>();
    for (int i = 0; i < questions.size(); i++) {
      var q = questions.get(i);
      if (q == null) continue;
      var fingerprint = QuestionVerification.fingerprintOf(q);
      var verification = byFingerprint.get(fingerprint);
      if (verification == null
          || !QuestionVerification.VERDICT_PASS.equals(verification.verdict())) {
        errors.add("line " + (i + 1) + " (" + q.track()
            + "): missing or stale fact verification — 리뷰 루프를 거친 뒤 장부를 갱신하라");
        continue;
      }
      used.add(fingerprint);
    }

    for (String fingerprint : byFingerprint.keySet()) {
      if (!used.contains(fingerprint)) {
        warnings.add("orphan verification " + fingerprint + " matches no approved question");
      }
    }
  }

  private void validateQuestion(int line, ApprovedQuestion q, List<String> errors) {
    if (q == null) {
      errors.add("line " + line + ": question must not be null");
      return;
    }
    if (!QuestionQuota.TRACKS.contains(q.track())) {
      errors.add("line " + line + ": unsupported track " + q.track());
    }
    if ("SHORT_ANSWER".equals(q.questionType())) {
      errors.add("line " + line + ": SHORT_ANSWER is not allowed in MD2 seed");
    }
    if (!QuestionQuota.TYPE_TARGETS.containsKey(q.questionType())) {
      errors.add("line " + line + ": unsupported questionType " + q.questionType());
    }
    if ("CREATE".equals(q.bloomLevel())) {
      errors.add("line " + line + ": Bloom CREATE is not allowed in MD2 seed");
    }
    if (!ALLOWED_BLOOM.contains(q.bloomLevel())) {
      errors.add("line " + line + ": unsupported bloomLevel " + q.bloomLevel());
    }
    if (blank(q.content())) {
      errors.add("line " + line + ": content is required");
    }
    if (!blank(q.content()) && !HANGUL.matcher(q.content()).find()) {
      errors.add("line " + line + ": content must contain Korean");
    }
    if (q.difficulty() == null || q.difficulty() < 0.0 || q.difficulty() > 1.0) {
      errors.add("line " + line + ": difficulty must be between 0.0 and 1.0");
    }
    if (q.options() == null || q.options().size() < 2) {
      errors.add("line " + line + ": options must contain at least two choices");
    }
    if (q.options() != null) {
      var normalized = normalizeAll(q.options());
      if (new HashSet<>(normalized).size() != normalized.size()) {
        errors.add("line " + line + ": duplicate option inside question");
      }
    }
    if (q.answerKey() == null || q.answerKey().correct() == null) {
      errors.add("line " + line + ": answerKey.correct is required");
    } else if (q.options() != null
        && (q.answerKey().correct() < 0 || q.answerKey().correct() >= q.options().size())) {
      errors.add("line " + line + ": answerKey.correct must be inside options range");
    }
    if (q.conceptTags() == null || q.conceptTags().isEmpty()) {
      errors.add("line " + line + ": conceptTags must not be empty");
    } else {
      for (String tag : q.conceptTags()) {
        if (blank(tag) || !KEBAB_TAG.matcher(tag).matches()) {
          errors.add("line " + line + ": conceptTags must be kebab-case");
          break;
        }
      }
    }
  }

  private void validateTrackQuotas(List<ApprovedQuestion> questions, List<String> errors) {
    var byTrack = questions.stream()
        .filter(q -> q != null && q.track() != null)
        .collect(Collectors.groupingBy(ApprovedQuestion::track));
    for (String track : QuestionQuota.TRACKS) {
      var trackQuestions = byTrack.getOrDefault(track, List.of());
      if (trackQuestions.size() != QuestionQuota.PER_TRACK) {
        errors.add(track + " must contain exactly 100 questions but was " + trackQuestions.size());
      }
      for (var target : QuestionQuota.TYPE_TARGETS.entrySet()) {
        long actual = trackQuestions.stream()
            .filter(q -> target.getKey().equals(q.questionType()))
            .count();
        if (actual != target.getValue()) {
          errors.add(track + " must contain " + target.getValue() + " "
              + target.getKey() + " questions but was " + actual);
        }
      }
    }
  }

  private void validateDistributionWarnings(List<ApprovedQuestion> questions, List<String> warnings) {
    for (String track : QuestionQuota.TRACKS) {
      var trackQuestions = questions.stream()
          .filter(q -> q != null && track.equals(q.track()))
          .toList();
      warnIfDifferent(track, "difficulty", QuestionQuota.DIFFICULTY_BAND_TARGETS,
          countDifficultyBands(trackQuestions), warnings);
      warnIfDifferent(track, "bloom", QuestionQuota.BLOOM_TARGETS,
          countBloom(trackQuestions), warnings);
    }
  }

  private Map<String, Long> countDifficultyBands(List<ApprovedQuestion> questions) {
    var counts = new HashMap<String, Long>();
    for (ApprovedQuestion q : questions) {
      counts.merge(difficultyBand(q.difficulty()), 1L, Long::sum);
    }
    return counts;
  }

  private String difficultyBand(Double value) {
    if (value == null) return "missing";
    if (value >= 0.1 && value <= 0.2) return "0.1-0.2";
    if (value >= 0.3 && value <= 0.4) return "0.3-0.4";
    if (value >= 0.5 && value <= 0.6) return "0.5-0.6";
    if (value >= 0.7 && value <= 0.8) return "0.7-0.8";
    if (Double.compare(value, 0.9) == 0) return "0.9";
    return "other";
  }

  private Map<String, Long> countBloom(List<ApprovedQuestion> questions) {
    var counts = new HashMap<String, Long>();
    for (ApprovedQuestion q : questions) {
      counts.merge(q.bloomLevel(), 1L, Long::sum);
    }
    return counts;
  }

  private void warnIfDifferent(String track, String name, Map<String, Integer> expected,
      Map<String, Long> actual, List<String> warnings) {
    for (var target : expected.entrySet()) {
      long observed = actual.getOrDefault(target.getKey(), 0L);
      long wanted = target.getValue();
      if (observed != wanted) {
        warnings.add(track + " " + name + " distribution drift: expected "
            + target.getKey() + "=" + wanted + " actual=" + observed);
      }
    }
    for (var entry : actual.entrySet()) {
      if (!expected.containsKey(entry.getKey()) && entry.getValue() > 0) {
        warnings.add(track + " " + name + " distribution drift: unexpected "
            + entry.getKey() + "=" + entry.getValue());
      }
    }
  }

  private boolean blank(String value) {
    return value == null || value.isBlank();
  }

  /** 앞뒤 공백 제거 + 연속 공백 1칸. 대소문자는 건드리지 않는다(코드 문항이 있다). */
  private static String normalize(String value) {
    return value == null ? "" : value.trim().replaceAll("\\s+", " ");
  }

  private static List<String> normalizeAll(List<String> values) {
    return values == null ? List.of() : values.stream().map(QuestionValidator::normalize).toList();
  }

  private void validateDuplicateOptionSets(List<ApprovedQuestion> questions, List<String> errors) {
    var byTrack = new HashMap<String, Map<List<String>, Integer>>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.track() == null || q.options() == null) continue;
      byTrack.computeIfAbsent(q.track(), t -> new HashMap<>())
          .merge(normalizeAll(q.options()), 1, Integer::sum);
    }
    for (var track : byTrack.entrySet()) {
      for (var set : track.getValue().entrySet()) {
        if (set.getValue() > 1) {
          errors.add(track.getKey() + ": duplicate option set shared by "
              + set.getValue() + " questions");
        }
      }
    }
  }

  private void validateDuplicateContents(List<ApprovedQuestion> questions, List<String> errors) {
    var counts = new HashMap<String, Integer>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.content() == null) continue;
      counts.merge(normalize(q.content()), 1, Integer::sum);
    }
    for (var entry : counts.entrySet()) {
      if (entry.getValue() > 1) {
        errors.add("duplicate content shared by " + entry.getValue() + " questions");
      }
    }
  }

  private void validateAnswerKeyBias(List<ApprovedQuestion> questions, List<String> errors) {
    var byTrack = new HashMap<String, Map<Integer, Integer>>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.track() == null || q.answerKey() == null
          || q.answerKey().correct() == null) continue;
      byTrack.computeIfAbsent(q.track(), t -> new HashMap<>())
          .merge(q.answerKey().correct(), 1, Integer::sum);
    }
    for (var track : byTrack.entrySet()) {
      int total = track.getValue().values().stream().mapToInt(Integer::intValue).sum();
      if (total == 0) continue;
      for (var position : track.getValue().entrySet()) {
        double share = (double) position.getValue() / total;
        if (share > MAX_ANSWER_KEY_SHARE) {
          errors.add(track.getKey() + ": answer key bias — position " + position.getKey()
              + " holds " + position.getValue() + "/" + total);
        }
      }
    }
  }

  /**
   * 정답의 길이 순위(오름차순, 1=최단…N=최장)가 트랙 안에서 한쪽으로 쏠리지 않았는지 본다.
   * 정답이 다른 어느 보기와도 길이가 동률이 아닌 문항만 센다 — 동률이면 길이가 순위를 가르는
   * 단서가 되지 못하므로 분자·분모 모두에서 뺀다. 이렇게 세는 문항이 트랙당
   * {@link #MIN_ANSWER_LENGTH_RANK_SAMPLE} 미만이면 판정하지 않는다.
   */
  private void validateAnswerLengthRankBias(List<ApprovedQuestion> questions, List<String> errors) {
    var rankCountsByTrack = new HashMap<String, Map<Integer, Integer>>();
    var optionCountByTrack = new HashMap<String, Integer>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.track() == null) continue;
      if (q.options() == null || q.options().isEmpty()) continue;
      if (q.answerKey() == null || q.answerKey().correct() == null) continue;
      int correct = q.answerKey().correct();
      if (correct < 0 || correct >= q.options().size()) continue;

      var lengths = normalizeAll(q.options()).stream().map(String::length).toList();
      int correctLength = lengths.get(correct);
      boolean tiesWithCorrect = false;
      int rank = 1;
      for (int i = 0; i < lengths.size(); i++) {
        if (i == correct) continue;
        int len = lengths.get(i);
        if (len == correctLength) {
          tiesWithCorrect = true;
          break;
        }
        if (len < correctLength) rank++;
      }
      if (tiesWithCorrect) continue;

      optionCountByTrack.put(q.track(), q.options().size());
      rankCountsByTrack.computeIfAbsent(q.track(), t -> new HashMap<>())
          .merge(rank, 1, Integer::sum);
    }

    for (var track : rankCountsByTrack.entrySet()) {
      int total = track.getValue().values().stream().mapToInt(Integer::intValue).sum();
      if (total < MIN_ANSWER_LENGTH_RANK_SAMPLE) continue;
      int optionCount = optionCountByTrack.getOrDefault(track.getKey(), 0);
      for (var rankEntry : track.getValue().entrySet()) {
        double share = (double) rankEntry.getValue() / total;
        if (share > MAX_ANSWER_LENGTH_RANK_SHARE) {
          errors.add(track.getKey() + ": answer length rank bias — correct is rank "
              + rankEntry.getKey() + " of " + optionCount + " by length in "
              + rankEntry.getValue() + "/" + total);
        }
      }
    }
  }
}
