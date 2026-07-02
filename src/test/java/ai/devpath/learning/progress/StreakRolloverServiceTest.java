package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.PathWeeklyTaskRepository;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StreakRolloverServiceTest {

  @Autowired UserStreakRepository streaks;
  @Autowired OutboxRepository outbox;
  @Autowired SandboxActivityLogRepository sandboxActivity;

  private StreakRolloverService serviceWithTaskActivity(boolean hadActivity) {
    PathWeeklyTaskRepository tasks = mock(PathWeeklyTaskRepository.class);
    when(tasks.hasCompletedTaskOnDate(any(Long.class), any(LocalDate.class))).thenReturn(hadActivity);
    return new StreakRolloverService(streaks, tasks, sandboxActivity, outbox);
  }

  @Test
  void incrementsCurrentDaysWhenActivityYesterday() {
    long userId = 777101L;
    UserStreak existing = new UserStreak();
    existing.setUserId(userId);
    existing.setCurrentDays(2);
    existing.setLongestDays(2);
    existing.setLastActiveDate(LocalDate.now().minusDays(1));
    existing.setUpdatedAt(Instant.now());
    streaks.save(existing);

    serviceWithTaskActivity(true).rollover(userId, LocalDate.now());

    UserStreak after = streaks.findById(userId).orElseThrow();
    assertThat(after.getCurrentDays()).isEqualTo(3);
    assertThat(after.getLongestDays()).isEqualTo(3);
  }

  @Test
  void resetsCurrentDaysToZeroWhenNoActivity() {
    long userId = 777102L;
    UserStreak existing = new UserStreak();
    existing.setUserId(userId);
    existing.setCurrentDays(5);
    existing.setLongestDays(5);
    existing.setUpdatedAt(Instant.now());
    streaks.save(existing);

    serviceWithTaskActivity(false).rollover(userId, LocalDate.now());

    UserStreak after = streaks.findById(userId).orElseThrow();
    assertThat(after.getCurrentDays()).isEqualTo(0);
    assertThat(after.getLongestDays()).isEqualTo(5);
  }

  @Test
  void publishesStreakReachedEventOnlyAtMilestone() {
    long userId = 777103L;
    UserStreak existing = new UserStreak();
    existing.setUserId(userId);
    existing.setCurrentDays(6);
    existing.setLongestDays(6);
    existing.setUpdatedAt(Instant.now());
    streaks.save(existing);

    long before = outbox.count();
    serviceWithTaskActivity(true).rollover(userId, LocalDate.now());
    assertThat(outbox.count()).isEqualTo(before + 1);

    // 8일째(비마일스톤)는 이벤트 미발행
    long afterFirst = outbox.count();
    serviceWithTaskActivity(true).rollover(userId, LocalDate.now().plusDays(1));
    assertThat(outbox.count()).isEqualTo(afterFirst);
  }
}
