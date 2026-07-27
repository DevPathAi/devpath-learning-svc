package ai.devpath.learning.progress;

import java.util.List;

public interface NotificationPrefsClient {
  List<UserTimezoneView> timezonesOf(List<Long> userIds);
}
