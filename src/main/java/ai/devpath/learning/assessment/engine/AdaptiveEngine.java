package ai.devpath.learning.assessment.engine;

import java.util.List;
import org.springframework.stereotype.Component;

/** 순수 적응형 진단 엔진(저장소·전송 무관). 설계서 §4 확정값. */
@Component
public class AdaptiveEngine {

  public static final double START_DIFFICULTY = 0.3;
  public static final int TOTAL_QUESTIONS = 15;
  private static final double STEP_UP = 0.1;
  private static final double STEP_DOWN = 0.05;

  public enum AnswerOutcome { CORRECT, WRONG, SKIP }

  public double nextDifficulty(double current, AnswerOutcome outcome) {
    double next = switch (outcome) {
      case CORRECT -> current + STEP_UP;
      case WRONG -> current - STEP_DOWN;
      case SKIP -> current;
    };
    return Math.max(0.0, Math.min(1.0, next));
  }

  public boolean isComplete(int answeredCount) {
    return answeredCount >= TOTAL_QUESTIONS;
  }

  /** θ = 정답(non-skip) 문항 difficulty 평균(없으면 0). <0.4 JUNIOR, 0.4~0.7 MID, >0.7 SENIOR. */
  public String diagnoseLevel(List<Double> correctDifficulties) {
    double theta = correctDifficulties.isEmpty() ? 0.0
        : correctDifficulties.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    if (theta < 0.4) return "JUNIOR";
    if (theta <= 0.7) return "MID";
    return "SENIOR";
  }
}
