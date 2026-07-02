package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.progress.UserStreak;
import ai.devpath.learning.progress.UserStreakRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DashboardServiceTest {

  @Test
  void returnsZeroStreakWhenNoStreakRow() {
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.current(42L)).thenThrow(new NoSuchElementException());
    UserStreakRepository streaks = mock(UserStreakRepository.class);
    when(streaks.findById(42L)).thenReturn(Optional.empty());
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgeNamesOf(42L)).thenReturn(List.of());

    DashboardService service = new DashboardService(paths, streaks, badges);
    DashboardSummary summary = service.summary(42L);

    assertThat(summary.streakDays()).isEqualTo(0);
    assertThat(summary.badges()).isEmpty();
  }

  @Test
  void returnsActualStreakDaysWhenRowExists() {
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.current(43L)).thenThrow(new NoSuchElementException());
    UserStreakRepository streaks = mock(UserStreakRepository.class);
    UserStreak streak = new UserStreak();
    streak.setUserId(43L);
    streak.setCurrentDays(12);
    when(streaks.findById(43L)).thenReturn(Optional.of(streak));
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgeNamesOf(43L)).thenReturn(List.of("첫 질문", "학생"));

    DashboardService service = new DashboardService(paths, streaks, badges);
    DashboardSummary summary = service.summary(43L);

    assertThat(summary.streakDays()).isEqualTo(12);
    assertThat(summary.badges()).containsExactly("첫 질문", "학생");
  }
}
