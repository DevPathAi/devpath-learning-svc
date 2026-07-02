package ai.devpath.learning.dashboard;

import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.path.LearningPathView;
import ai.devpath.learning.path.WeeklyTaskView;
import ai.devpath.learning.progress.UserStreakRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
  private final LearningPathQueryService paths;
  private final UserStreakRepository streaks;
  private final CommunityBadgeClient badgeClient;

  public DashboardService(LearningPathQueryService paths, UserStreakRepository streaks,
      CommunityBadgeClient badgeClient) {
    this.paths = paths;
    this.streaks = streaks;
    this.badgeClient = badgeClient;
  }

  @Transactional(readOnly = true)
  public DashboardSummary summary(long userId) {
    int streakDays = streaks.findById(userId).map(s -> s.getCurrentDays()).orElse(0);
    List<String> badges = badgeClient.badgeNamesOf(userId);

    LearningPathView path;
    try {
      path = paths.current(userId);
    } catch (NoSuchElementException e) {
      return new DashboardSummary(streakDays, 0, null, badges);
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

    return new DashboardSummary(streakDays, progress, nextTask, badges);
  }
}
