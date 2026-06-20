package ai.devpath.learning.assessment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NextQuestionSelectorTest {

  final NextQuestionSelector selector = new NextQuestionSelector();

  private QuestionBank q(double difficulty) {
    QuestionBank q = new QuestionBank();
    q.setTrack("BACKEND_SPRING");
    q.setDifficulty(difficulty);
    return q;
  }

  @Test
  void picksClosestDifficultyNotExcluded() {
    var pool = List.of(q(0.2), q(0.35), q(0.9));
    QuestionBank picked = selector.select("BACKEND_SPRING", 0.3, Set.of(), pool);
    assertThat(picked.getDifficulty()).isEqualTo(0.35);
  }

  @Test
  void returnsNullWhenPoolEmpty() {
    assertThat(selector.select("BACKEND_SPRING", 0.3, Set.of(), List.<QuestionBank>of())).isNull();
  }
}
