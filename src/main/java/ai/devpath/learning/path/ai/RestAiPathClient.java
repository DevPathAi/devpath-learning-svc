package ai.devpath.learning.path.ai;

import ai.devpath.learning.path.AiServiceUnavailableException;
import ai.devpath.learning.path.PathContractException;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class RestAiPathClient implements AiPathClient {
  private final RestClient restClient;

  public RestAiPathClient(
      @Value("${devpath.ai-svc.base-url:http://localhost:8081}") String baseUrl,
      @Value("${devpath.ai-svc.timeout:PT8S}") Duration timeout) {
    var requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(timeout);
    requestFactory.setReadTimeout(timeout);
    this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(requestFactory).build();
  }

  @Override
  public PathGenerateResult generate(PathGenerateCommand command) {
    try {
      PathGenerateResult result = restClient.post()
          .uri("/ai/path/generate")
          .body(command)
          .retrieve()
          .body(PathGenerateResult.class);
      if (result == null || result.rationale() == null || result.milestones() == null
          || result.milestones().isEmpty()) {
        throw new PathContractException("ai-svc path response is incomplete");
      }
      return result;
    } catch (PathContractException e) {
      throw e;
    } catch (RestClientException e) {
      throw new AiServiceUnavailableException("ai-svc path generate failed", e);
    }
  }

  @Override
  public List<List<Double>> embed(List<String> texts) {
    try {
      EmbedResponse response = restClient.post()
          .uri("/ai/embed")
          .body(new EmbedRequest(texts))
          .retrieve()
          .body(EmbedResponse.class);
      if (response == null || response.embeddings() == null
          || response.embeddings().size() != texts.size()) {
        throw new PathContractException("ai-svc embed response size mismatch");
      }
      return response.embeddings();
    } catch (PathContractException e) {
      throw e;
    } catch (RestClientException e) {
      throw new AiServiceUnavailableException("ai-svc embed failed", e);
    }
  }

  private record EmbedRequest(List<String> texts) {}
  private record EmbedResponse(List<List<Double>> embeddings) {}
}
