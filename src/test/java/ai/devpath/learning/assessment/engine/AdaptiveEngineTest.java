package ai.devpath.learning.assessment.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;
import org.junit.jupiter.api.Test;

class AdaptiveEngineTest {

  final AdaptiveEngine engine = new AdaptiveEngine();

  @Test
  void correctRaisesByPointOne() {
    assertThat(engine.nextDifficulty(0.3, AdaptiveEngine.AnswerOutcome.CORRECT)).isCloseTo(0.4, within(1e-9));
  }

  @Test
  void wrongLowersByPointZeroFive() {
    assertThat(engine.nextDifficulty(0.3, AdaptiveEngine.AnswerOutcome.WRONG)).isCloseTo(0.25, within(1e-9));
  }

  @Test
  void skipKeepsDifficulty() {
    assertThat(engine.nextDifficulty(0.3, AdaptiveEngine.AnswerOutcome.SKIP)).isCloseTo(0.3, within(1e-9));
  }

  @Test
  void clampsToBounds() {
    assertThat(engine.nextDifficulty(1.0, AdaptiveEngine.AnswerOutcome.CORRECT)).isEqualTo(1.0);
    assertThat(engine.nextDifficulty(0.0, AdaptiveEngine.AnswerOutcome.WRONG)).isEqualTo(0.0);
  }

  @Test
  void completesAtFifteen() {
    assertThat(engine.isComplete(14)).isFalse();
    assertThat(engine.isComplete(15)).isTrue();
  }

  @Test
  void diagnosesLevelByThreshold() {
    assertThat(engine.diagnoseLevel(List.of())).isEqualTo("JUNIOR");
    assertThat(engine.diagnoseLevel(List.of(0.2, 0.3))).isEqualTo("JUNIOR");
    assertThat(engine.diagnoseLevel(List.of(0.5, 0.6))).isEqualTo("MID");
    assertThat(engine.diagnoseLevel(List.of(0.8, 0.9))).isEqualTo("SENIOR");
  }
}
