package ai.devpath.learning.dashboard;

import java.util.List;

public interface CommunityBadgeClient {
  List<String> badgeNamesOf(long userId);

  /** awardedAt 포함 배지 목록(주간 리포트 "이번주 배지" 필터용). 실패 시 빈 목록. */
  List<BadgeAwardView> badgesOf(long userId);
}
