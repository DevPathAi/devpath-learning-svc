package ai.devpath.learning.progress;

import ai.devpath.learning.outbox.OutboxEntry;
import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.learning.path.PathWeeklyTaskRepository;
import ai.devpath.shared.event.StreakReachedEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Service
public class StreakRolloverService {
  private static final Set<Integer> MILESTONES = Set.of(7, 14, 30, 60, 100);

  private final UserStreakRepository streaks;
  private final PathWeeklyTaskRepository weeklyTasks;
  private final SandboxActivityLogRepository sandboxActivity;
  private final OutboxRepository outbox;
  private final JsonMapper jsonMapper = new JsonMapper();

  public StreakRolloverService(UserStreakRepository streaks, PathWeeklyTaskRepository weeklyTasks,
      SandboxActivityLogRepository sandboxActivity, OutboxRepository outbox) {
    this.streaks = streaks;
    this.weeklyTasks = weeklyTasks;
    this.sandboxActivity = sandboxActivity;
    this.outbox = outbox;
  }

  /** localDate는 유저의 로컬 자정이 막 지난 "오늘" — 판정 대상은 그 전날(localDate.minusDays(1))의 활동. */
  @Transactional
  public void rollover(long userId, LocalDate localDate) {
    LocalDate yesterday = localDate.minusDays(1);
    boolean hadActivity = weeklyTasks.hasCompletedTaskOnDate(userId, yesterday)
        || sandboxActivity.hasActivityOnDate(userId, yesterday);

    UserStreak streak = streaks.findById(userId).orElseGet(() -> {
      UserStreak s = new UserStreak();
      s.setUserId(userId);
      return s;
    });

    if (hadActivity) {
      int newCurrent = streak.getCurrentDays() + 1;
      streak.setCurrentDays(newCurrent);
      streak.setLongestDays(Math.max(streak.getLongestDays(), newCurrent));
      streak.setLastActiveDate(yesterday);
      streak.setUpdatedAt(Instant.now());
      streaks.save(streak);
      if (MILESTONES.contains(newCurrent)) {
        publishStreakReached(userId, newCurrent);
      }
    } else {
      streak.setCurrentDays(0);
      streak.setUpdatedAt(Instant.now());
      streaks.save(streak);
    }
  }

  private void publishStreakReached(long userId, int days) {
    var event = new StreakReachedEvent(UUID.randomUUID(), Instant.now(), userId, days);
    OutboxEntry entry = new OutboxEntry();
    entry.setAggregateType("user_streak");
    entry.setAggregateId(String.valueOf(userId));
    entry.setEventType(StreakReachedEvent.EVENT_TYPE);
    entry.setPayload(jsonMapper.writeValueAsString(event));
    entry.setCreatedAt(Instant.now());
    outbox.save(entry);
  }
}
