package ai.devpath.learning.dashboard;

import ai.devpath.learning.content.ContentProgressRepository;
import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.path.LearningPathView;
import ai.devpath.learning.path.WeeklyTaskView;
import ai.devpath.learning.progress.UserStreakRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
  private final LearningPathQueryService paths;
  private final UserStreakRepository streaks;
  private final CommunityBadgeClient badgeClient;
  private final ContentProgressRepository contentProgress;

  public DashboardService(LearningPathQueryService paths, UserStreakRepository streaks,
      CommunityBadgeClient badgeClient, ContentProgressRepository contentProgress) {
    this.paths = paths;
    this.streaks = streaks;
    this.badgeClient = badgeClient;
    this.contentProgress = contentProgress;
  }

  @Transactional(readOnly = true)
  public DashboardSummary summary(long userId) {
    int streakDays = streaks.findById(userId).map(s -> s.getCurrentDays()).orElse(0);
    List<String> badges = badgeClient.badgeNamesOf(userId);
    int completedContentCount = contentProgress.countCompleted(userId);

    LearningPathView path = paths.currentOptional(userId).orElse(null);
    if (path == null) {
      return new DashboardSummary(streakDays, 0, null, badges, completedContentCount);
    }

    List<WeeklyTaskView> tasks = path.milestones().stream()
        .flatMap(m -> m.tasks().stream())
        .toList();
    long completed = tasks.stream().filter(WeeklyTaskView::completed).count();
    int progress = tasks.isEmpty() ? 0 : (int) Math.round(completed * 100.0 / tasks.size());
    String nextTask = tasks.stream()
        .filter(t -> !t.completed())
        .findFirst()
        .map(WeeklyTaskView::title)
        .orElse(null);

    return new DashboardSummary(streakDays, progress, nextTask, badges, completedContentCount);
  }
}
