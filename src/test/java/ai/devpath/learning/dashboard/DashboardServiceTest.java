package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.devpath.learning.content.ContentProgressRepository;
import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.progress.UserStreak;
import ai.devpath.learning.progress.UserStreakRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DashboardServiceTest {

  @Test
  void returnsZeroStreakWhenNoStreakRow() {
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.currentOptional(42L)).thenReturn(Optional.empty());
    UserStreakRepository streaks = mock(UserStreakRepository.class);
    when(streaks.findById(42L)).thenReturn(Optional.empty());
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgeNamesOf(42L)).thenReturn(List.of());
    ContentProgressRepository contentProgress = mock(ContentProgressRepository.class);
    when(contentProgress.countCompleted(42L)).thenReturn(0);
    when(contentProgress.dailyCompletedCounts(eq(42L), org.mockito.ArgumentMatchers.any()))
        .thenReturn(java.util.Map.of());
    when(contentProgress.activePathCompletions(42L))
        .thenReturn(new ContentProgressRepository.ActivePathCompletions(0, List.of()));

    DashboardService service = new DashboardService(paths, streaks, badges, contentProgress);
    DashboardSummary summary = service.summary(42L);

    assertThat(summary.streakDays()).isEqualTo(0);
    assertThat(summary.badges()).isEmpty();
    assertThat(summary.completedContentCount()).isEqualTo(0);
  }

  @Test
  void returnsActualStreakDaysWhenRowExists() {
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.currentOptional(43L)).thenReturn(Optional.empty());
    UserStreakRepository streaks = mock(UserStreakRepository.class);
    UserStreak streak = new UserStreak();
    streak.setUserId(43L);
    streak.setCurrentDays(12);
    when(streaks.findById(43L)).thenReturn(Optional.of(streak));
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgeNamesOf(43L)).thenReturn(List.of("첫 질문", "학생"));
    ContentProgressRepository contentProgress = mock(ContentProgressRepository.class);
    when(contentProgress.countCompleted(43L)).thenReturn(3);
    when(contentProgress.dailyCompletedCounts(eq(43L), org.mockito.ArgumentMatchers.any()))
        .thenReturn(java.util.Map.of());
    when(contentProgress.activePathCompletions(43L))
        .thenReturn(new ContentProgressRepository.ActivePathCompletions(0, List.of()));

    DashboardService service = new DashboardService(paths, streaks, badges, contentProgress);
    DashboardSummary summary = service.summary(43L);

    assertThat(summary.streakDays()).isEqualTo(12);
    assertThat(summary.badges()).containsExactly("첫 질문", "학생");
    assertThat(summary.completedContentCount()).isEqualTo(3);
  }

  @Test
  void populatesWeeklyActivityAndEmptyProgressHistoryWhenNoActivePath() {
    LearningPathQueryService paths = mock(LearningPathQueryService.class);
    when(paths.currentOptional(44L)).thenReturn(Optional.empty());
    UserStreakRepository streaks = mock(UserStreakRepository.class);
    when(streaks.findById(44L)).thenReturn(Optional.empty());
    CommunityBadgeClient badges = mock(CommunityBadgeClient.class);
    when(badges.badgeNamesOf(44L)).thenReturn(List.of());
    ContentProgressRepository contentProgress = mock(ContentProgressRepository.class);
    when(contentProgress.countCompleted(44L)).thenReturn(0);
    when(contentProgress.dailyCompletedCounts(eq(44L), org.mockito.ArgumentMatchers.any()))
        .thenReturn(java.util.Map.of());
    when(contentProgress.activePathCompletions(44L))
        .thenReturn(new ContentProgressRepository.ActivePathCompletions(0, List.of()));

    DashboardService service = new DashboardService(paths, streaks, badges, contentProgress);
    DashboardSummary summary = service.summary(44L);

    assertThat(summary.weeklyActivity()).hasSize(7);
    assertThat(summary.progressHistory()).isEmpty();
  }
}
