package ai.devpath.learning.progress;

import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class RestNotificationPrefsClient implements NotificationPrefsClient {
  private final RestClient restClient;

  public RestNotificationPrefsClient(
      @Value("${devpath.notification-svc.base-url:http://localhost:8088}") String baseUrl,
      @Value("${devpath.notification-svc.timeout:PT5S}") Duration timeout) {
    var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(timeout);
    requestFactory.setReadTimeout(timeout);
    this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(requestFactory).build();
  }

  @Override
  public List<UserTimezoneView> timezonesOf(List<Long> userIds) {
    if (userIds.isEmpty()) return List.of();
    String csv = userIds.stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse("");
    try {
      List<UserTimezoneView> result = restClient.get()
          .uri("/notifications/internal/prefs/timezones?userIds={ids}", csv)
          .retrieve()
          .body(new ParameterizedTypeReference<List<UserTimezoneView>>() {});
      return result == null ? List.of() : result;
    } catch (RestClientException e) {
      throw new NotificationPrefsUnavailableException("notification-svc timezone 조회 실패", e);
    }
  }
}
