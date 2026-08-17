package ai.devpath.learning.path;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 학습경로 생성을 요청 수명과 분리해 실행한다.
 *
 * <p>CPU Ollama 기준 생성이 10분을 넘기므로(2026-08-14 운영 실측) 요청 스레드에서 실행하면
 * 타임아웃과 함께 결과가 버려지고, 사용자가 버튼을 다시 누를 때마다 같은 비용이 중복된다.
 * 사용자당 실행 중인 작업은 하나로 고정하고, 결과는 구독자 유무와 무관하게 저장한다.
 */
@Service
public class PathGenerationJobService {
  private static final Logger log = LoggerFactory.getLogger(PathGenerationJobService.class);

  private final ConcurrentHashMap<Long, PathGenerationJob> jobs = new ConcurrentHashMap<>();
  private final PathGenerator generator;
  private final Executor executor;

  public PathGenerationJobService(PathGenerator generator,
      @Qualifier("pathGenerationExecutor") Executor executor) {
    this.generator = generator;
    this.executor = executor;
  }

  /** 실행 중인 작업이 있으면 그 작업을 돌려준다. 없으면 새로 시작한다. */
  public PathGenerationJob submit(long userId, String goal) {
    AtomicBoolean created = new AtomicBoolean();
    PathGenerationJob job = jobs.compute(userId, (id, existing) -> {
      if (existing != null && existing.status().state() == PathGenerationJob.State.RUNNING) {
        return existing;
      }
      created.set(true);
      return new PathGenerationJob(userId, goal);
    });

    if (created.get()) {
      try {
        executor.execute(() -> run(job));
      } catch (RejectedExecutionException e) {
        jobs.remove(userId, job);
        throw new PathGenerationUnavailableException("학습경로 생성 실행기가 작업을 받을 수 없습니다", e);
      }
    }
    return job;
  }

  public Optional<PathGenerationJob> find(long userId) {
    return Optional.ofNullable(jobs.get(userId));
  }

  private void run(PathGenerationJob job) {
    try {
      generator.generatePath(job.userId(), job.goal(), job::publishProgress);
      job.complete();
    } catch (Throwable t) {
      log.warn("학습경로 생성 실패 (userId={})", job.userId(), t);
      job.fail(t);
    }
  }
}
