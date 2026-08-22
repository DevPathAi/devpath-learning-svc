package ai.devpath.learning.contentgen.content;

import java.util.ArrayList;
import java.util.List;

/**
 * 트랙 하나의 콘텐츠 쿼터를 레벨별 소배치로 쪼갠 계획.
 *
 * <p>한 번에 30개를 요구하면 모델이 레벨 목표를 지키지 않는다 — 실측으로 NODE_TYPESCRIPT 가
 * INTRO 6 · INTERMEDIATE 14 · ADVANCED 14 로 나와 게이트에 걸렸다.
 */
public final class ContentHarvestPlan {

  /**
   * 콘텐츠 본문은 문항보다 훨씬 길다. 한 요청이 담아야 할 분량을 줄여 출력 상한(num_predict)
   * 안에 들어오게 한다 — 6건으로 두었을 때 한 호출이 70분 넘게 늘어졌다.
   */
  static final int MAX_BATCH = 3;

  private ContentHarvestPlan() {}

  public static List<ContentCell> cells() {
    var cells = new ArrayList<ContentCell>();
    ContentQuota.LEVEL_TARGETS.forEach((level, total) -> {
      var remaining = total;
      while (remaining > 0) {
        var batch = Math.min(MAX_BATCH, remaining);
        cells.add(new ContentCell(level, batch));
        remaining -= batch;
      }
    });
    return List.copyOf(cells);
  }
}
