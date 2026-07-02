package ai.devpath.learning.dashboard;

import java.util.List;

public interface CommunityBadgeClient {
  List<String> badgeNamesOf(long userId);
}
