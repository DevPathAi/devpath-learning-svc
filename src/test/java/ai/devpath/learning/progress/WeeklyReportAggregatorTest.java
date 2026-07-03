package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.dashboard.BadgeAwardView;
import ai.devpath.learning.dashboard.CommunityBadgeClient;
import ai.devpath.learning.path.LearningPathQueryService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WeeklyReportAggregatorTest {

  @Autowired UserStreakRepository streaks;

  private WeeklyReportAggregator aggregator(LearningPathQueryService paths, CommunityBadgeClient badges) {
    return new WeeklyReportAggregator(streaks, paths, badges);
  }

  @Test
  void aggregatesStreakAndFiltersBadgesToThisWeek() {
    long userId = 660001L;
    UserStreak s = new UserStreak();
    s.setUserId(userId); s.setCurrentDays(12); s.setUpdatedAt(Instant.now());
    streaks.save(s);

    Instant now = Instant.now();
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.current(anyLong())).thenThrow(new java.util.NoSuchElementException()); // 활성 경로 없음 → progress 0
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgesOf(userId)).thenReturn(List.of(
        new BadgeAwardView("STUDENT", "학생", "BRONZE", now.minus(2, ChronoUnit.DAYS)),      // 이번주
        new BadgeAwardView("FIRST_QUESTION", "첫질문", "BRONZE", now.minus(30, ChronoUnit.DAYS)) // 지난달 → 제외
    ));

    var event = aggregator(paths, badges).aggregate(userId, LocalDate.of(2026, 7, 5), now);

    assertThat(event.userId()).isEqualTo(userId);
    assertThat(event.streakDays()).isEqualTo(12);
    assertThat(event.progressPercent()).isEqualTo(0);
    assertThat(event.nextTaskTitle()).isNull();
    assertThat(event.badgesEarnedThisWeek()).containsExactly("학생"); // 최근 7일 배지만
    assertThat(event.weekOf()).isEqualTo(LocalDate.of(2026, 7, 5));
  }
}
