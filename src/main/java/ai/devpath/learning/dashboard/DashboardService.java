package ai.devpath.learning.dashboard;

import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.path.LearningPathView;
import ai.devpath.learning.path.WeeklyTaskView;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
  private final LearningPathQueryService paths;

  public DashboardService(LearningPathQueryService paths) {
    this.paths = paths;
  }

  @Transactional(readOnly = true)
  public DashboardSummary summary(long userId) {
    LearningPathView path;
    try {
      path = paths.current(userId);
    } catch (NoSuchElementException e) {
      return new DashboardSummary(0, 0, null, List.of());
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

    return new DashboardSummary(0, progress, nextTask, List.of("첫 학습 경로 생성"));
  }
}
