package ai.devpath.learning.assessment;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/** 다음 문항 결정적 선택: track 일치·미출제 후보 중 |difficulty-current| 최소, 동률 시 id 오름차순. */
@Component
public class NextQuestionSelector {

  public QuestionBank select(String track, double currentDifficulty, Set<Long> excludedIds, List<QuestionBank> pool) {
    return pool.stream()
        .filter(q -> track.equals(q.getTrack()))
        .filter(q -> q.getId() == null || !excludedIds.contains(q.getId()))
        .min(Comparator
            .comparingDouble((QuestionBank q) -> Math.abs(q.getDifficulty() - currentDifficulty))
            .thenComparing(q -> q.getId() == null ? Long.MAX_VALUE : q.getId()))
        .orElse(null);
  }
}
