package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;

import ai.devpath.learning.content.ContentProgressRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DashboardTimeseriesTest {
  private static final LocalDate TODAY = LocalDate.of(2026, 7, 31);

  @Test
  void weeklyActivityReturns7AscendingDaysFillingGapsWithZero() {
    Map<LocalDate, Integer> counts = Map.of(
        LocalDate.of(2026, 7, 31), 3,
        LocalDate.of(2026, 7, 29), 1);

    List<DailyActivity> out = DashboardTimeseries.weeklyActivity(TODAY, counts);

    assertThat(out).hasSize(7);
    assertThat(out.get(0).date()).isEqualTo("2026-07-25"); // today-6
    assertThat(out.get(6).date()).isEqualTo("2026-07-31");
    assertThat(out.get(6).completedCount()).isEqualTo(3);
    assertThat(out.get(4).completedCount()).isEqualTo(1); // 7/29
    assertThat(out.get(5).completedCount()).isEqualTo(0); // 7/30 갭
  }

  @Test
  void progressHistoryComputesCumulativePercentOver14Days() {
    // 전체 4개 과제 중 2개는 7/15(윈도우 시작 7/18 이전), 1개는 7/28 완료
    List<LocalDate> done = List.of(
        LocalDate.of(2026, 7, 15),
        LocalDate.of(2026, 7, 15),
        LocalDate.of(2026, 7, 28));

    // 이 테스트가 지키는 것은 「전체 누적률」이다 — 유형별 단언은 아래 전용 테스트가 맡는다.
    var pc = new ContentProgressRepository.ActivePathCompletions(
        4, done, Map.of("READ", 4), Map.of("READ", done));

    List<ProgressPoint> out = DashboardTimeseries.progressHistory(TODAY, pc);

    assertThat(out).hasSize(14);
    assertThat(out.get(0).date()).isEqualTo("2026-07-18"); // today-13
    assertThat(out.get(0).percent()).isEqualTo(50); // 2/4 by 7/18
    assertThat(out.get(13).percent()).isEqualTo(75); // 3/4 by 7/31
  }

  @Test
  void progressHistoryEmptyWhenNoActivePathTasks() {
    var empty = new ContentProgressRepository.ActivePathCompletions(
        0, List.of(), Map.of(), Map.of());
    assertThat(DashboardTimeseries.progressHistory(TODAY, empty)).isEmpty();
  }

  @Test
  void progressHistoryIncludesPerTypeCumulativePercent() {
    LocalDate today = LocalDate.of(2026, 8, 7);
    var pc = new ContentProgressRepository.ActivePathCompletions(
        4,
        List.of(LocalDate.of(2026, 8, 5), LocalDate.of(2026, 8, 6)),
        Map.of("READ", 2, "PRACTICE", 2),
        Map.of(
            "READ", List.of(LocalDate.of(2026, 8, 5)),
            "PRACTICE", List.of(LocalDate.of(2026, 8, 6))));

    List<ProgressPoint> out = DashboardTimeseries.progressHistory(today, pc);

    assertThat(out).hasSize(DashboardTimeseries.HISTORY_DAYS);
    ProgressPoint last = out.get(out.size() - 1);
    assertThat(last.date()).isEqualTo("2026-08-07");
    assertThat(last.percent()).isEqualTo(50); // 전체 2/4
    assertThat(last.byType()).containsEntry("READ", 50); // 1/2
    assertThat(last.byType()).containsEntry("PRACTICE", 50);

    // 8/5 시점에는 READ만 완료됐다 — 유형마다 시점별 값이 실제로 갈리는지 본다.
    ProgressPoint onAug5 = out.stream()
        .filter(p -> p.date().equals("2026-08-05"))
        .findFirst()
        .orElseThrow();
    assertThat(onAug5.byType()).containsEntry("READ", 50);
    assertThat(onAug5.byType()).containsEntry("PRACTICE", 0);
  }

  @Test
  void progressHistoryOmitsKeyForTypeWithNoTasks() {
    LocalDate today = LocalDate.of(2026, 8, 7);
    var pc = new ContentProgressRepository.ActivePathCompletions(
        2, List.of(), Map.of("READ", 2), Map.of());

    List<ProgressPoint> out = DashboardTimeseries.progressHistory(today, pc);

    // 「QUIZ가 0%」와 「QUIZ 과제가 아예 없다」는 다르다 — 후자는 키가 없어야 한다.
    assertThat(out.get(0).byType()).containsOnlyKeys("READ");
  }
}
