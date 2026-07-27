package ai.devpath.learning.assessment.engine;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.assessment.engine.AdaptiveEngine.AnswerOutcome;
import java.util.List;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;

/**
 * jqwik 속성 기반 테스트 — 적응형 난이도 엔진의 불변식·수렴 속성 검증.
 *
 * <p>실제 로직(AdaptiveEngine, 설계서 §4 확정값): CORRECT +0.1, WRONG -0.05, SKIP ±0,
 * 결과는 항상 [0.0, 1.0]로 클램프. 순수 함수이므로 mock 불필요.
 */
class AdaptiveEnginePropertyTest {

  private final AdaptiveEngine engine = new AdaptiveEngine();

  /** 불변식: 임의의 시작 난이도 d∈[0,1]와 임의 outcome에 대해 결과는 항상 [0,1] 범위. */
  @Property
  void nextDifficultyStaysWithinBounds(
      @ForAll @DoubleRange(min = 0.0, max = 1.0) double current,
      @ForAll AnswerOutcome outcome) {
    double next = engine.nextDifficulty(current, outcome);
    assertThat(next).isBetween(0.0, 1.0);
  }

  /**
   * 불변식: 시작 난이도가 이미 [0,1]를 벗어난 극단값이어도 결과는 [0,1]로 클램프된다.
   * (방어적 클램핑 보장 — 상한/하한 밖 입력을 넣어도 범위를 넘지 않음)
   */
  @Property
  void nextDifficultyClampsExtremeInputs(
      @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double current,
      @ForAll AnswerOutcome outcome) {
    double next = engine.nextDifficulty(current, outcome);
    assertThat(next).isBetween(0.0, 1.0);
  }

  /** 단조성: CORRECT는 결코 난이도를 낮추지 않고, WRONG은 결코 높이지 않는다(클램프 하에서도). */
  @Property
  void correctNeverLowersAndWrongNeverRaises(
      @ForAll @DoubleRange(min = 0.0, max = 1.0) double current) {
    assertThat(engine.nextDifficulty(current, AnswerOutcome.CORRECT))
        .isGreaterThanOrEqualTo(current);
    assertThat(engine.nextDifficulty(current, AnswerOutcome.WRONG))
        .isLessThanOrEqualTo(current);
  }

  /** SKIP은 난이도를 그대로 유지한다(항등). */
  @Property
  void skipIsIdentity(@ForAll @DoubleRange(min = 0.0, max = 1.0) double current) {
    assertThat(engine.nextDifficulty(current, AnswerOutcome.SKIP)).isEqualTo(current);
  }

  /**
   * 수렴: 임의 시작 난이도에서 충분히(>=100회) 연속 CORRECT를 적용하면 상한 1.0으로 수렴한다.
   * (STEP_UP=0.1이므로 최악의 경우 0.0→1.0에 10스텝, 100스텝이면 항상 도달)
   */
  @Property
  void repeatedCorrectConvergesToUpperBound(
      @ForAll @DoubleRange(min = 0.0, max = 1.0) double start,
      @ForAll @IntRange(min = 100, max = 200) int steps) {
    double d = start;
    for (int i = 0; i < steps; i++) {
      d = engine.nextDifficulty(d, AnswerOutcome.CORRECT);
    }
    assertThat(d).isEqualTo(1.0);
  }

  /**
   * 수렴: 임의 시작 난이도에서 충분히(>=100회) 연속 WRONG을 적용하면 하한 0.0으로 수렴한다.
   * (STEP_DOWN=0.05이므로 최악의 경우 1.0→0.0에 20스텝, 100스텝이면 항상 도달)
   */
  @Property
  void repeatedWrongConvergesToLowerBound(
      @ForAll @DoubleRange(min = 0.0, max = 1.0) double start,
      @ForAll @IntRange(min = 100, max = 200) int steps) {
    double d = start;
    for (int i = 0; i < steps; i++) {
      d = engine.nextDifficulty(d, AnswerOutcome.WRONG);
    }
    assertThat(d).isEqualTo(0.0);
  }

  /**
   * 단조 수렴 경로: 연속 CORRECT 시퀀스에서 난이도는 단조 비감소이며 매 스텝 [0,1]를 유지한다.
   */
  @Property
  void correctSequenceIsMonotonicNonDecreasing(
      @ForAll @DoubleRange(min = 0.0, max = 1.0) double start,
      @ForAll @IntRange(min = 1, max = 50) int steps) {
    double prev = start;
    for (int i = 0; i < steps; i++) {
      double next = engine.nextDifficulty(prev, AnswerOutcome.CORRECT);
      assertThat(next).isBetween(0.0, 1.0);
      assertThat(next).isGreaterThanOrEqualTo(prev);
      prev = next;
    }
  }

  /** diagnoseLevel은 임의의 [0,1] 난이도 리스트에 대해 항상 세 등급 중 하나를 반환한다(전역성). */
  @Property
  void diagnoseLevelAlwaysReturnsKnownLevel(
      @ForAll List<@DoubleRange(min = 0.0, max = 1.0) Double> difficulties) {
    String level = engine.diagnoseLevel(difficulties);
    assertThat(level).isIn("JUNIOR", "MID", "SENIOR");
  }
}
