package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * 학습경로 생성은 CPU Ollama 기준 10분 이상 걸린다(2026-08-14 운영 실측 4.2 t/s).
 * 그 시간 동안 요청이 끊기거나 사용자가 버튼을 다시 눌러도 생성이 중단되거나 중복되지 않아야 한다.
 */
class PathGenerationJobServiceTest {

  private final FakeGenerator generator = new FakeGenerator();
  private final ExecutorService executor = Executors.newFixedThreadPool(2);
  private final PathGenerationJobService service = new PathGenerationJobService(generator, executor);

  @AfterEach
  void tearDown() {
    executor.shutdownNow();
  }

  @Test
  void secondSubmitWhileRunningReusesTheRunningJob() throws Exception {
    generator.blockUntilReleased();

    PathGenerationJob first = service.submit(7L, "goal");
    PathGenerationJob second = service.submit(7L, "goal");

    assertThat(second).isSameAs(first);
    generator.release();
    awaitTerminal(first);
    assertThat(generator.invocations.get()).isEqualTo(1);
  }

  @Test
  void listenerFailureDoesNotAbortGeneration() throws Exception {
    generator.blockUntilReleased();
    PathGenerationJob job = service.submit(8L, null);
    job.addListener(new PathGenerationListener() {
      @Override public void onProgress(PathProgressEvent event) {
        throw new IllegalStateException("SSE send failed");
      }
      @Override public void onSuccess(long pathId) {
        throw new IllegalStateException("SSE send failed");
      }
      @Override public void onFailure(Throwable error) {
        throw new IllegalStateException("SSE send failed");
      }
    });

    generator.release();
    awaitTerminal(job);

    assertThat(generator.ranToCompletion).isTrue();
    assertThat(job.status().state()).isEqualTo(PathGenerationJob.State.SUCCEEDED);
    assertThat(job.status().pathId()).isEqualTo(FakeGenerator.PATH_ID);
  }

  @Test
  void failedGenerationIsRecordedAndAllowsResubmit() throws Exception {
    generator.failWith(new IllegalStateException("ai-svc path generate failed"));

    PathGenerationJob failed = service.submit(9L, null);
    awaitTerminal(failed);

    assertThat(failed.status().state()).isEqualTo(PathGenerationJob.State.FAILED);
    assertThat(failed.status().errorMessage()).contains("ai-svc path generate failed");

    generator.succeed();
    PathGenerationJob retried = service.submit(9L, null);
    assertThat(retried).isNotSameAs(failed);
    awaitTerminal(retried);

    assertThat(retried.status().state()).isEqualTo(PathGenerationJob.State.SUCCEEDED);
    assertThat(generator.invocations.get()).isEqualTo(2);
  }

  @Test
  void finishedJobStaysQueryableSoAClientCanRecoverAfterDisconnect() throws Exception {
    PathGenerationJob job = service.submit(10L, null);
    awaitTerminal(job);

    PathGenerationJob found = service.find(10L).orElseThrow();

    assertThat(found.status().state()).isEqualTo(PathGenerationJob.State.SUCCEEDED);
    assertThat(found.status().pathId()).isEqualTo(FakeGenerator.PATH_ID);
  }

  @Test
  void listenerAddedAfterCompletionStillSeesTheResult() throws Exception {
    PathGenerationJob job = service.submit(11L, null);
    awaitTerminal(job);

    AtomicInteger seen = new AtomicInteger();
    job.addListener(new PathGenerationListener() {
      @Override public void onProgress(PathProgressEvent event) {}
      @Override public void onSuccess(long pathId) { seen.set((int) pathId); }
      @Override public void onFailure(Throwable error) {}
    });

    assertThat(seen.get()).isEqualTo((int) FakeGenerator.PATH_ID);
  }

  @Test
  void findReturnsEmptyWhenTheUserNeverGenerated() {
    assertThat(service.find(12L)).isEmpty();
  }

  @Test
  void rejectedExecutionSurfacesInsteadOfLeavingAStuckJob() {
    ExecutorService closed = Executors.newSingleThreadExecutor();
    closed.shutdown();
    PathGenerationJobService rejecting = new PathGenerationJobService(generator, closed);

    assertThatThrownBy(() -> rejecting.submit(13L, null))
        .isInstanceOf(PathGenerationUnavailableException.class);
    assertThat(rejecting.find(13L)).isEmpty();
  }

  private static void awaitTerminal(PathGenerationJob job) throws InterruptedException {
    CountDownLatch done = new CountDownLatch(1);
    job.addListener(new PathGenerationListener() {
      @Override public void onProgress(PathProgressEvent event) {}
      @Override public void onSuccess(long pathId) { done.countDown(); }
      @Override public void onFailure(Throwable error) { done.countDown(); }
    });
    assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
  }

  private static final class FakeGenerator implements PathGenerator {
    static final long PATH_ID = 42L;

    final AtomicInteger invocations = new AtomicInteger();
    volatile boolean ranToCompletion;
    private final AtomicBoolean blocking = new AtomicBoolean();
    private final CountDownLatch gate = new CountDownLatch(1);
    private volatile RuntimeException failure;

    void blockUntilReleased() { blocking.set(true); }

    void release() { gate.countDown(); }

    void failWith(RuntimeException error) { failure = error; }

    void succeed() { failure = null; }

    @Override
    public void generatePath(long userId, String goal, Consumer<PathProgressEvent> progress) {
      invocations.incrementAndGet();
      if (blocking.get()) {
        try {
          if (!gate.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("gate was never released");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException(e);
        }
      }
      progress.accept(PathProgressEvent.collecting());
      if (failure != null) {
        throw failure;
      }
      progress.accept(PathProgressEvent.done(PATH_ID));
      ranToCompletion = true;
    }
  }
}
