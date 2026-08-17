package ai.devpath.learning.path;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 사용자 한 명의 학습경로 생성 작업. 생성은 요청 수명과 분리돼 실행되므로 구독자가 사라지거나
 * 예외를 던져도 생성과 저장은 계속된다.
 */
public class PathGenerationJob {
  private static final Logger log = LoggerFactory.getLogger(PathGenerationJob.class);

  public enum State { RUNNING, SUCCEEDED, FAILED }

  public record Status(State state, Long pathId, String errorMessage) {}

  private final long userId;
  private final String goal;
  private final Object lock = new Object();
  private final List<PathGenerationListener> listeners = new CopyOnWriteArrayList<>();
  /** 늦게 붙은 구독자도 진행 단계를 처음부터 본다. 생성 1회당 이벤트는 네 개다. */
  private final List<PathProgressEvent> history = new ArrayList<>();
  private volatile Status status = new Status(State.RUNNING, null, null);
  private volatile Long pathId;

  PathGenerationJob(long userId, String goal) {
    this.userId = userId;
    this.goal = goal;
  }

  long userId() {
    return userId;
  }

  String goal() {
    return goal;
  }

  public Status status() {
    return status;
  }

  /**
   * 이미 지나간 진행 단계와 끝난 결과를 즉시 재생한 뒤 구독을 건다. 등록과 재생을 같은 잠금 안에서
   * 처리해 이벤트가 중복되거나 누락되지 않는다. SSE 가 끊긴 뒤의 재조회 경로이기도 하다.
   */
  public void addListener(PathGenerationListener listener) {
    synchronized (lock) {
      for (PathProgressEvent past : history) {
        notifySafely(listener, () -> listener.onProgress(past));
      }
      if (status.state() == State.RUNNING) {
        listeners.add(listener);
      } else {
        replay(listener, status);
      }
    }
  }

  void publishProgress(PathProgressEvent event) {
    synchronized (lock) {
      history.add(event);
      if (event.pathId() != null) {
        pathId = event.pathId();
      }
      for (PathGenerationListener listener : listeners) {
        notifySafely(listener, () -> listener.onProgress(event));
      }
    }
  }

  void complete() {
    Long generated = pathId;
    if (generated == null) {
      fail(new PathContractException("path generation finished without a path id"));
      return;
    }
    List<PathGenerationListener> targets = finish(new Status(State.SUCCEEDED, generated, null));
    for (PathGenerationListener listener : targets) {
      notifySafely(listener, () -> listener.onSuccess(generated));
    }
  }

  void fail(Throwable error) {
    List<PathGenerationListener> targets = finish(new Status(State.FAILED, null, message(error)));
    for (PathGenerationListener listener : targets) {
      notifySafely(listener, () -> listener.onFailure(error));
    }
  }

  private List<PathGenerationListener> finish(Status terminal) {
    synchronized (lock) {
      status = terminal;
      List<PathGenerationListener> targets = new ArrayList<>(listeners);
      listeners.clear();
      return targets;
    }
  }

  private void replay(PathGenerationListener listener, Status finished) {
    if (finished.state() == State.SUCCEEDED) {
      notifySafely(listener, () -> listener.onSuccess(finished.pathId()));
    } else {
      notifySafely(listener, () -> listener.onFailure(new IllegalStateException(finished.errorMessage())));
    }
  }

  /** 구독자(SSE emitter)의 실패는 생성에 영향을 주지 않는다. 실패한 구독자는 떨어뜨린다. */
  private void notifySafely(PathGenerationListener listener, Runnable notification) {
    try {
      notification.run();
    } catch (Throwable t) {
      listeners.remove(listener);
      log.debug("학습경로 생성 구독자 알림 실패 — 구독을 해제한다 (userId={})", userId, t);
    }
  }

  private static String message(Throwable error) {
    return error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage();
  }
}
