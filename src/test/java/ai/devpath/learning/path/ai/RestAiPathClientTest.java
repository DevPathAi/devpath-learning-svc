package ai.devpath.learning.path.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ai.devpath.learning.path.AiServiceUnavailableException;
import ai.devpath.learning.path.PathContractException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import tools.jackson.databind.json.JsonMapper;

/**
 * ai-svc 경계 계약 테스트. 실제 ai-svc/Ollama 없이 MockWebServer로
 * 정상 파싱과 4xx/5xx/불완전 응답 → 예외 변환을 검증한다.
 */
@SpringBootTest
@ActiveProfiles("test")
class RestAiPathClientTest {
  private static final MockWebServer AI = startServer();

  @Autowired RestAiPathClient client;
  @Autowired JsonMapper jsonMapper;

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("devpath.ai-svc.base-url", () -> AI.url("/").toString());
    registry.add("devpath.ai-svc.timeout", () -> "PT1S");
  }

  @AfterAll
  static void shutdown() throws IOException {
    AI.shutdown();
  }

  @Test
  void generateParsesValidResponse() throws Exception {
    AI.enqueue(json(jsonMapper.writeValueAsString(validResult())));

    PathGenerateResult result = client.generate(command());

    assertThat(result.rationale()).isNotBlank();
    assertThat(result.milestones()).hasSize(1);
    assertThat(result.milestones().get(0).expectedOutcome()).isNotBlank();
    assertThat(result.milestones().get(0).tasks()).hasSize(3);
    assertThat(AI.getRequestCount()).isGreaterThan(0);
  }

  @Test
  void generateMapsServerErrorToAiServiceUnavailable() {
    AI.enqueue(new MockResponse().setResponseCode(500).setBody("boom"));

    assertThatThrownBy(() -> client.generate(command()))
        .isInstanceOf(AiServiceUnavailableException.class);
  }

  @Test
  void generateMapsIncompleteResponseToContract() {
    AI.enqueue(json("{\"rationale\":null,\"milestones\":[]}"));

    assertThatThrownBy(() -> client.generate(command()))
        .isInstanceOf(PathContractException.class);
  }

  @Test
  void embedReturnsVectorsOnValidResponse() throws Exception {
    AI.enqueue(json(jsonMapper.writeValueAsString(Map.of(
        "embeddings", List.of(List.of(0.1, 0.2, 0.3))))));

    List<List<Double>> out = client.embed(List.of("hello"));

    assertThat(out).hasSize(1);
    assertThat(out.get(0)).hasSize(3);
  }

  @Test
  void embedMapsSizeMismatchToContract() throws Exception {
    AI.enqueue(json(jsonMapper.writeValueAsString(Map.of("embeddings", List.of()))));

    assertThatThrownBy(() -> client.embed(List.of("hello")))
        .isInstanceOf(PathContractException.class);
  }

  @Test
  void embedMapsServerErrorToAiServiceUnavailable() {
    AI.enqueue(new MockResponse().setResponseCode(503));

    assertThatThrownBy(() -> client.embed(List.of("hello")))
        .isInstanceOf(AiServiceUnavailableException.class);
  }

  private MockResponse json(String body) {
    return new MockResponse().setHeader("Content-Type", "application/json").setBody(body);
  }

  private PathGenerateResult validResult() {
    return new PathGenerateResult("백엔드 기초부터 다집니다.", List.of(
        new PathGenerateResult.Milestone(
            1, "Spring MVC 입문", "controller 흐름 이해",
            List.of("Spring MVC"), 6, "기초가 먼저", "간단한 API 작성 가능",
            List.of(
                new PathGenerateResult.Task(1, "READ", "개념 읽기", true),
                new PathGenerateResult.Task(2, "PRACTICE", "controller 작성", true),
                new PathGenerateResult.Task(3, "QUIZ", "HTTP 퀴즈", false)))));
  }

  private PathGenerateCommand command() {
    return new PathGenerateCommand("BACKEND_SPRING", "JUNIOR",
        List.of("Java"), List.of("Spring MVC"), "취업 준비");
  }

  private static MockWebServer startServer() {
    try {
      MockWebServer server = new MockWebServer();
      server.start();
      return server;
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }
}
