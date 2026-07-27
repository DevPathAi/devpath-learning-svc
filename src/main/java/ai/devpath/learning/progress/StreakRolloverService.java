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

  private static final int STAGNATION_DAYS = 3;

  private final UserStreakRepository streaks;
  private final PathWeeklyTaskRepository weeklyTasks;
  private final SandboxActivityLogRepository sandboxActivity;
  private final OutboxRepository outbox;
  private final ActivePathSummaryReader pathSummary;
  private final JsonMapper jsonMapper = new JsonMapper();

  public StreakRolloverService(UserStreakRepository streaks, PathWeeklyTaskRepository weeklyTasks,
      SandboxActivityLogRepository sandboxActivity, OutboxRepository outbox,
      ActivePathSummaryReader pathSummary) {
    this.streaks = streaks;
    this.weeklyTasks = weeklyTasks;
    this.sandboxActivity = sandboxActivity;
    this.outbox = outbox;
    this.pathSummary = pathSummary;
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
      streak.setStagnationNotifiedAt(null); // 재활성 → 다음 정체 에피소드 재통지 허용
      streak.setUpdatedAt(Instant.now());
      streaks.save(streak);
      if (MILESTONES.contains(newCurrent)) {
        publishStreakReached(userId, newCurrent);
      }
    } else {
      streak.setCurrentDays(0);
      maybePublishStagnation(streak, yesterday);
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

  /** last_active_date 기준 미활동 정확히 3일째이고 아직 통지 전이면 UserStagnatedEvent 발행 + 마커 세팅. */
  private void maybePublishStagnation(UserStreak streak, java.time.LocalDate yesterday) {
    java.time.LocalDate lastActive = streak.getLastActiveDate();
    if (lastActive == null) return; // 한 번도 활동 없던 유저는 정체 대상 아님(재참여가 아니라 최초 참여)
    if (streak.getStagnationNotifiedAt() != null) return; // 이미 이 에피소드에 통지함
    long daysInactive = java.time.temporal.ChronoUnit.DAYS.between(lastActive, yesterday);
    if (daysInactive < STAGNATION_DAYS) return; // 3일 이상 첫 스캔에 1회 — 마커가 중복 방지하므로 틱 놓쳐도 누락 없음

    long userId = streak.getUserId();
    java.time.Instant lastActiveAt = lastActive.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
    String summary = pathSummary.summarize(userId).orElse(null);
    var event = new ai.devpath.shared.event.UserStagnatedEvent(
        UUID.randomUUID(), Instant.now(), userId, lastActiveAt, (int) daysInactive, summary);
    OutboxEntry entry = new OutboxEntry();
    entry.setAggregateType("user_streak");
    entry.setAggregateId(String.valueOf(userId));
    entry.setEventType(ai.devpath.shared.event.UserStagnatedEvent.EVENT_TYPE);
    entry.setPayload(jsonMapper.writeValueAsString(event));
    entry.setCreatedAt(Instant.now());
    outbox.save(entry);
    streak.setStagnationNotifiedAt(Instant.now());
  }
}
