package ai.devpath.learning.path;

import java.util.function.Consumer;

/** 학습경로 생성 실행부. 잡 서비스가 요청 수명과 분리해 호출한다. */
@FunctionalInterface
public interface PathGenerator {
  void generatePath(long userId, String goal, Consumer<PathProgressEvent> progress);
}
