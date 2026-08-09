package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OllamaBatchEmbeddingClientTest {

  private HttpServer server;

  @AfterEach
  void stop() {
    if (server != null) server.stop(0);
  }

  private String startServer(String responseBody, AtomicReference<String> capturedRequest)
      throws Exception {
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext("/api/embed", exchange -> {
      String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      capturedRequest.set(body);
      byte[] out = responseBody.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().add("Content-Type", "application/json");
      exchange.sendResponseHeaders(200, out.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(out);
      }
    });
    server.start();
    return "http://localhost:" + server.getAddress().getPort();
  }

  private static String embeddingsJson(int count) {
    String one = "[" + String.join(",", Collections.nCopies(768, "0.1")) + "]";
    return "{\"embeddings\":[" + String.join(",", Collections.nCopies(count, one)) + "]}";
  }

  @Test
  void sendsAllTextsInOneRequestAsArray() throws Exception {
    var captured = new AtomicReference<String>();
    String baseUrl = startServer(embeddingsJson(3), captured);
    var client = new OllamaBatchEmbeddingClient(baseUrl, "nomic-embed-text");

    List<List<Double>> result = client.embedAll(List.of("a", "b", "c"));

    assertThat(result).hasSize(3);
    assertThat(result.get(0)).hasSize(768);
    assertThat(captured.get()).contains("\"input\":[\"a\",\"b\",\"c\"]");
    assertThat(captured.get()).contains("nomic-embed-text");
  }

  @Test
  void rejectsResponseCountMismatch() throws Exception {
    String baseUrl = startServer(embeddingsJson(2), new AtomicReference<>());
    var client = new OllamaBatchEmbeddingClient(baseUrl, "nomic-embed-text");

    assertThatThrownBy(() -> client.embedAll(List.of("a", "b", "c")))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("개수");
  }

  @Test
  void rejectsWrongDimension() throws Exception {
    String wrong = "{\"embeddings\":[[0.1,0.2,0.3]]}";
    String baseUrl = startServer(wrong, new AtomicReference<>());
    var client = new OllamaBatchEmbeddingClient(baseUrl, "nomic-embed-text");

    assertThatThrownBy(() -> client.embedAll(List.of("a")))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("768");
  }
}
