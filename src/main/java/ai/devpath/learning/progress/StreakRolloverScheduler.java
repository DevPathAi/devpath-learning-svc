package ai.devpath.learning.progress;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class StreakRolloverScheduler {
  private static final Logger log = LoggerFactory.getLogger(StreakRolloverScheduler.class);

  private final ActiveLearnerRepository activeLearners;
  private final NotificationPrefsClient prefsClient;
  private final StreakRolloverService rolloverService;

  public StreakRolloverScheduler(ActiveLearnerRepository activeLearners,
      NotificationPrefsClient prefsClient, StreakRolloverService rolloverService) {
    this.activeLearners = activeLearners;
    this.prefsClient = prefsClient;
    this.rolloverService = rolloverService;
  }

  @Scheduled(cron = "0 0 * * * *")
  public void rolloverDueTimezones() {
    List<Long> userIds = activeLearners.activeLearnerUserIds();
    if (userIds.isEmpty()) return;

    List<UserTimezoneView> prefs;
    try {
      prefs = prefsClient.timezonesOf(userIds);
    } catch (NotificationPrefsUnavailableException e) {
      log.warn("notification-svc timezone 조회 실패 — 이번 주기 스킵, 다음 주기 재시도", e);
      return;
    }

    for (UserTimezoneView pref : prefs) {
      ZonedDateTime nowInTz = ZonedDateTime.now(ZoneId.of(pref.timezone()));
      if (nowInTz.toLocalTime().isBefore(LocalTime.of(1, 0))) {
        rolloverService.rollover(pref.userId(), nowInTz.toLocalDate());
      }
    }
  }
}
