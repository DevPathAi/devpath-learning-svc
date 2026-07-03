package ai.devpath.learning.dashboard;

import java.time.Instant;

/** community-svc 배지 응답(awardedAt 포함) — 주간 리포트의 "이번주 배지" 필터용. */
public record BadgeAwardView(String code, String name, String tier, Instant awardedAt) {
}
