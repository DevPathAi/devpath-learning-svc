package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

/** 대시보드 계약 직렬화 검증(필드명 + ISO 날짜 문자열). date가 String이라 Jackson date 모듈 비의존. */
class DashboardSummaryJsonTest {
  @Test
  void serializesTimeseriesWithContractFieldNames() throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    DashboardSummary s = new DashboardSummary(7, 62, "다음 과제", List.of("배지"), 12,
        List.of(new DailyActivity("2026-07-31", 3)),
        List.of(new ProgressPoint("2026-07-31", 42)));

    String json = mapper.writeValueAsString(s);

    assertThat(json).contains("\"weeklyActivity\"").contains("\"completedCount\":3");
    assertThat(json).contains("\"progressHistory\"").contains("\"percent\":42");
    assertThat(json).contains("\"date\":\"2026-07-31\"");
  }
}
