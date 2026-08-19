package ai.devpath.learning.contentgen.question;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 트랙 하나의 쿼터를 작은 셀로 쪼갠 계획.
 *
 * <p>"100개를 한 번에 만들라"고 요구하면 모델이 한 형태로 붕괴한다 — 실측으로 NODE_TYPESCRIPT
 * 355줄 중 고유 16건(전부 MCQ·REMEMBER·0.1-0.2), PYTHON_BACKEND 1차 30줄 중 고유 6건이었다.
 * 그래서 요청을 셀 단위 소배치로 나눈다.
 */
public final class QuestionHarvestPlan {

  /** 셀 하나가 커지면 그 안에서 다시 붕괴한다. 소배치 상한을 둔다. */
  static final int MAX_BATCH = 6;

  private static final List<String> BLOOM_ORDER =
      List.of("REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE");
  private static final List<String> BAND_ORDER =
      List.of("0.1-0.2", "0.3-0.4", "0.5-0.6", "0.7-0.8", "0.9");

  private QuestionHarvestPlan() {}

  public static List<QuestionCell> cells() {
    var blooms = expand(BLOOM_ORDER, QuestionQuota.BLOOM_TARGETS);
    var bands = expand(BAND_ORDER, QuestionQuota.DIFFICULTY_BAND_TARGETS);

    // 슬롯 10개마다 MCQ 7 · CODE_READING 3 을 되풀이한다. 그러면 타입 주변부가 정확히
    // 70/30 이 되면서도 CODE_READING 이 특정 난이도에 몰리지 않는다.
    var grouped = new LinkedHashMap<List<String>, Integer>();
    for (int index = 0; index < blooms.size(); index++) {
      var type = index % 10 < 7 ? "MCQ" : "CODE_READING";
      var key = List.of(type, blooms.get(index), bands.get(index));
      grouped.merge(key, 1, Integer::sum);
    }

    var cells = new ArrayList<QuestionCell>();
    grouped.forEach((key, total) -> {
      var remaining = total;
      while (remaining > 0) {
        var batch = Math.min(MAX_BATCH, remaining);
        cells.add(new QuestionCell(key.get(0), key.get(1), key.get(2), batch));
        remaining -= batch;
      }
    });
    return List.copyOf(cells);
  }

  private static List<String> expand(List<String> order, Map<String, Integer> targets) {
    var expanded = new ArrayList<String>();
    for (String label : order) {
      var count = targets.get(label);
      if (count == null) {
        throw new IllegalStateException("쿼터에 없는 항목: " + label);
      }
      for (int i = 0; i < count; i++) {
        expanded.add(label);
      }
    }
    return expanded;
  }
}
