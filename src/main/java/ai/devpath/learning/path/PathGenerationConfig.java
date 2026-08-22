package ai.devpath.learning.path;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathGenerationConfig {

  @Bean
  public PathGenerator pathGenerator(LearningPathGenerationService generation) {
    return (userId, goal, progress) -> generation.generate(userId, goal, progress);
  }

  /**
   * 생성 1건이 CPU Ollama 에서 노드 CPU 두 코어를 오래 점유한다(2026-08-14 실측).
   * 동시 실행 수를 작게 고정해 다른 요청 처리까지 느려지는 것을 막는다.
   */
  @Bean(name = "pathGenerationExecutor", destroyMethod = "shutdownNow")
  public ExecutorService pathGenerationExecutor(
      @Value("${devpath.path.generation-workers:2}") int workers) {
    AtomicInteger sequence = new AtomicInteger();
    return Executors.newFixedThreadPool(workers, runnable -> {
      Thread thread = new Thread(runnable, "path-gen-" + sequence.incrementAndGet());
      thread.setDaemon(true);
      return thread;
    });
  }
}
