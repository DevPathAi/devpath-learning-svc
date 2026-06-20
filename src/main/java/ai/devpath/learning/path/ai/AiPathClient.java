package ai.devpath.learning.path.ai;

import java.util.List;

public interface AiPathClient {
  PathGenerateResult generate(PathGenerateCommand command);
  List<List<Double>> embed(List<String> texts);
}
