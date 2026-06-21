package ai.devpath.learning.contentgen.question;

import java.util.List;
import java.util.Map;

public final class QuestionQuota {

  public static final List<String> TRACKS = List.of(
      "BACKEND_SPRING",
      "FRONTEND_REACT",
      "MOBILE_FLUTTER",
      "DEVOPS",
      "FULLSTACK");

  public static final Map<String, Integer> TYPE_TARGETS = Map.of(
      "MCQ", 70,
      "CODE_READING", 30);

  public static final Map<String, Integer> BLOOM_TARGETS = Map.of(
      "REMEMBER", 10,
      "UNDERSTAND", 25,
      "APPLY", 30,
      "ANALYZE", 25,
      "EVALUATE", 10);

  public static final Map<String, Integer> DIFFICULTY_BAND_TARGETS = Map.of(
      "0.1-0.2", 10,
      "0.3-0.4", 25,
      "0.5-0.6", 30,
      "0.7-0.8", 25,
      "0.9", 10);

  static final int PER_TRACK = 100;

  private QuestionQuota() {}
}
