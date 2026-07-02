package ai.devpath.learning.dashboard;

import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class RestCommunityBadgeClient implements CommunityBadgeClient {
  private final RestClient restClient;

  public RestCommunityBadgeClient(
      @Value("${devpath.community-svc.base-url:http://localhost:8086}") String baseUrl,
      @Value("${devpath.community-svc.timeout:PT3S}") Duration timeout) {
    var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(timeout);
    requestFactory.setReadTimeout(timeout);
    this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(requestFactory).build();
  }

  @Override
  public List<String> badgeNamesOf(long userId) {
    try {
      List<BadgeSummaryView> badges = restClient.get()
          .uri("/community/users/{userId}/badges", userId)
          .retrieve()
          .body(new ParameterizedTypeReference<List<BadgeSummaryView>>() {});
      return badges == null ? List.of() : badges.stream().map(BadgeSummaryView::name).toList();
    } catch (RestClientException e) {
      // 대시보드는 배지 조회 실패로 전체가 죽으면 안 됨 — 빈 목록으로 그레이스풀 디그레이드.
      return List.of();
    }
  }
}
