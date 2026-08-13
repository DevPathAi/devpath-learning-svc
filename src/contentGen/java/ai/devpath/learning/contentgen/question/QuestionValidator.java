package ai.devpath.learning.contentgen.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuestionValidator {

  private static final Pattern KEBAB_TAG = Pattern.compile("[a-z0-9]+(?:-[a-z0-9]+)*");
  private static final List<String> ALLOWED_BLOOM = List.of(
      "REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE");

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
    validateDistributionWarnings(questions, warnings);
    return new QuestionValidationReport(List.copyOf(errors), List.copyOf(warnings));
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
}
