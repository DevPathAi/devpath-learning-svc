package ai.devpath.learning.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 대시보드 시계열 계산(순수 로직, DB·시계 비의존 → 결정적 단위 테스트 대상). */
final class DashboardTimeseries {
  static final int ACTIVITY_DAYS = 7;
  static final int HISTORY_DAYS = 14;

  private DashboardTimeseries() {}

  /** 최근 ACTIVITY_DAYS일(today 포함, 오름차순). 없는 날은 0. date는 ISO 문자열. */
  static List<DailyActivity> weeklyActivity(LocalDate today, Map<LocalDate, Integer> countsByDate) {
    List<DailyActivity> out = new ArrayList<>(ACTIVITY_DAYS);
    for (int i = ACTIVITY_DAYS - 1; i >= 0; i--) {
      LocalDate d = today.minusDays(i);
      out.add(new DailyActivity(d.toString(), countsByDate.getOrDefault(d, 0)));
    }
    return out;
  }

  /** 최근 HISTORY_DAYS일 누적 완료율(%). totalTasks<=0이면 빈 배열. date는 ISO 문자열. */
  static List<ProgressPoint> progressHistory(
      LocalDate today, int totalTasks, List<LocalDate> completedDates) {
    if (totalTasks <= 0) {
      return List.of();
    }
    List<ProgressPoint> out = new ArrayList<>(HISTORY_DAYS);
    for (int i = HISTORY_DAYS - 1; i >= 0; i--) {
      LocalDate d = today.minusDays(i);
      long done = completedDates.stream().filter(cd -> !cd.isAfter(d)).count();
      int percent = (int) Math.round(done * 100.0 / totalTasks);
      out.add(new ProgressPoint(d.toString(), percent));
    }
    return out;
  }
}
