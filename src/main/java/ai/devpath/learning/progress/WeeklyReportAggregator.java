package ai.devpath.learning.progress;

import ai.devpath.learning.dashboard.BadgeAwardView;
import ai.devpath.learning.dashboard.CommunityBadgeClient;
import ai.devpath.learning.path.LearningPathQueryService;
import ai.devpath.learning.path.LearningPathView;
import ai.devpath.learning.path.WeeklyTaskView;
import ai.devpath.shared.event.WeeklyReportGeneratedEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 유저별 주간 리포트 이벤트 조립(스트릭 + 진척% + 다음과제 + 이번주 배지). */
@Service
public class WeeklyReportAggregator {

	private final UserStreakRepository streaks;
	private final LearningPathQueryService paths;
	private final CommunityBadgeClient badgeClient;

	public WeeklyReportAggregator(UserStreakRepository streaks, LearningPathQueryService paths,
			CommunityBadgeClient badgeClient) {
		this.streaks = streaks;
		this.paths = paths;
		this.badgeClient = badgeClient;
	}

	@Transactional(readOnly = true)
	public WeeklyReportGeneratedEvent aggregate(long userId, LocalDate weekOf, Instant now) {
		int streakDays = streaks.findById(userId).map(UserStreak::getCurrentDays).orElse(0);

		int progressPercent = 0;
		String nextTaskTitle = null;
		try {
			LearningPathView path = paths.current(userId);
			List<WeeklyTaskView> tasks = path.milestones().stream()
					.flatMap(m -> m.tasks().stream())
					.toList();
			long completed = tasks.stream().filter(WeeklyTaskView::completed).count();
			progressPercent = tasks.isEmpty() ? 0 : (int) Math.round(completed * 100.0 / tasks.size());
			nextTaskTitle = tasks.stream().filter(t -> !t.completed()).findFirst()
					.map(WeeklyTaskView::title).orElse(null);
		} catch (NoSuchElementException e) {
			// 활성 학습경로 없음 → progress 0, nextTask null
		}

		Instant weekAgo = now.minus(7, ChronoUnit.DAYS);
		List<String> badgesThisWeek = badgeClient.badgesOf(userId).stream()
				.filter(b -> b.awardedAt() != null && b.awardedAt().isAfter(weekAgo))
				.map(BadgeAwardView::name)
				.toList();

		return new WeeklyReportGeneratedEvent(UUID.randomUUID(), now, userId, weekOf,
				streakDays, progressPercent, badgesThisWeek, nextTaskTitle);
	}
}
