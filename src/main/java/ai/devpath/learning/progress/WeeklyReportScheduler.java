package ai.devpath.learning.progress;

import ai.devpath.learning.outbox.OutboxEntry;
import ai.devpath.learning.outbox.OutboxRepository;
import ai.devpath.shared.event.WeeklyReportGeneratedEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/** 매주 일 20:00(KST) 전체 활성 유저의 주간 리포트를 집계해 Outbox로 발행한다. */
@Component
@Profile("!test")
public class WeeklyReportScheduler {

	private static final Logger log = LoggerFactory.getLogger(WeeklyReportScheduler.class);
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	private final ActiveLearnerRepository activeLearners;
	private final WeeklyReportAggregator aggregator;
	private final OutboxRepository outbox;
	private final JsonMapper jsonMapper = new JsonMapper();

	public WeeklyReportScheduler(ActiveLearnerRepository activeLearners, WeeklyReportAggregator aggregator,
			OutboxRepository outbox) {
		this.activeLearners = activeLearners;
		this.aggregator = aggregator;
		this.outbox = outbox;
	}

	@Scheduled(cron = "0 0 20 * * SUN", zone = "Asia/Seoul")
	public void generateWeeklyReports() {
		LocalDate weekOf = LocalDate.now(KST);
		Instant now = Instant.now();
		for (Long userId : activeLearners.activeLearnerUserIds()) {
			try {
				WeeklyReportGeneratedEvent event = aggregator.aggregate(userId, weekOf, now);
				OutboxEntry entry = new OutboxEntry();
				entry.setAggregateType("weekly_report");
				entry.setAggregateId(String.valueOf(userId));
				entry.setEventType(WeeklyReportGeneratedEvent.EVENT_TYPE);
				entry.setPayload(jsonMapper.writeValueAsString(event));
				entry.setCreatedAt(Instant.now());
				outbox.save(entry);
			} catch (RuntimeException e) {
				log.warn("주간 리포트 생성 실패 — userId={} skip", userId, e); // 한 유저 실패가 전체를 막지 않음
			}
		}
	}
}
