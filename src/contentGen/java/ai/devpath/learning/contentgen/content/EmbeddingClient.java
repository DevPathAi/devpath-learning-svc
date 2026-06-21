package ai.devpath.learning.contentgen.content;

import java.util.List;

public interface EmbeddingClient {
  List<Double> embed(String text) throws Exception;
}
