package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.PathWeeklyTaskRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StreakRolloverStagnationTest {

  @Autowired UserStreakRepository streaks;
  @Autowired OutboxRepository outbox;
  @Autowired SandboxActivityLogRepository sandboxActivity;

  private StreakRolloverService serviceNoActivity() {
    PathWeeklyTaskRepository tasks = mock(PathWeeklyTaskRepository.class);
    when(tasks.hasCompletedTaskOnDate(any(Long.class), any(LocalDate.class))).thenReturn(false);
    ActivePathSummaryReader summary = mock(ActivePathSummaryReader.class);
    when(summary.summarize(any(Long.class))).thenReturn(Optional.of("백엔드 스프링 트랙 (12주 과정)"));
    // sandboxActivity도 활동 없음이어야 하므로 실제 빈 대신 mock 사용
    SandboxActivityLogRepository sandbox = mock(SandboxActivityLogRepository.class);
    when(sandbox.hasActivityOnDate(any(Long.class), any(LocalDate.class))).thenReturn(false);
    return new StreakRolloverService(streaks, tasks, sandbox, outbox, summary);
  }

  private UserStreak seed(long userId, LocalDate lastActive, java.time.Instant notified) {
    UserStreak s = new UserStreak();
    s.setUserId(userId);
    s.setCurrentDays(0);
    s.setLastActiveDate(lastActive);
    s.setStagnationNotifiedAt(notified);
    s.setUpdatedAt(java.time.Instant.now());
    return streaks.save(s);
  }

  @Test
  void publishesStagnatedEventExactlyOnThirdInactiveDay() {
    long userId = 770001L;
    // 오늘 = lastActive + 4 → yesterday = lastActive + 3 → daysInactive = 3
    LocalDate lastActive = LocalDate.of(2026, 6, 30);
    seed(userId, lastActive, null);
    long before = outbox.count();

    serviceNoActivity().rollover(userId, LocalDate.of(2026, 7, 4)); // yesterday=7/3, between(6/30,7/3)=3

    assertThat(outbox.count()).isEqualTo(before + 1);
    UserStreak after = streaks.findById(userId).orElseThrow();
    assertThat(after.getStagnationNotifiedAt()).isNotNull();
  }

  @Test
  void doesNotPublishBeforeThirdInactiveDay() {
    long userId = 770002L;
    seed(userId, LocalDate.of(2026, 6, 30), null);
    long before = outbox.count();
    serviceNoActivity().rollover(userId, LocalDate.of(2026, 7, 3)); // daysInactive=2 → 임계 미도달
    assertThat(outbox.count()).isEqualTo(before);
  }

  @Test
  void publishesOnFirstScanAtOrAfterThirdDay() {
    // 3일째 롤오버 틱을 놓쳐 4일째(daysInactive=4)에 처음 스캔 — 마커 null이므로 이때 정확히 1회 발행(누락 없음).
    long userId = 770004L;
    seed(userId, LocalDate.of(2026, 6, 30), null);
    long before = outbox.count();
    serviceNoActivity().rollover(userId, LocalDate.of(2026, 7, 5)); // daysInactive=4, 마커 null → 발행
    assertThat(outbox.count()).isEqualTo(before + 1);
    UserStreak after = streaks.findById(userId).orElseThrow();
    assertThat(after.getStagnationNotifiedAt()).isNotNull();
  }

  @Test
  void doesNotRepublishWhenAlreadyNotified() {
    long userId = 770003L;
    seed(userId, LocalDate.of(2026, 6, 30), java.time.Instant.now()); // 이미 통지됨
    long before = outbox.count();
    serviceNoActivity().rollover(userId, LocalDate.of(2026, 7, 4)); // daysInactive=3이나 마커 존재
    assertThat(outbox.count()).isEqualTo(before);
  }
}
