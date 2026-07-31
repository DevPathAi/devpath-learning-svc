package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;

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

    List<ProgressPoint> out = DashboardTimeseries.progressHistory(TODAY, 4, done);

    assertThat(out).hasSize(14);
    assertThat(out.get(0).date()).isEqualTo("2026-07-18"); // today-13
    assertThat(out.get(0).percent()).isEqualTo(50); // 2/4 by 7/18
    assertThat(out.get(13).percent()).isEqualTo(75); // 3/4 by 7/31
  }

  @Test
  void progressHistoryEmptyWhenNoActivePathTasks() {
    assertThat(DashboardTimeseries.progressHistory(TODAY, 0, List.of())).isEmpty();
  }
}
