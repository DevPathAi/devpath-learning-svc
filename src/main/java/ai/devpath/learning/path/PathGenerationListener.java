package ai.devpath.learning.path;

/** 생성 진행을 구독한다. 구현이 예외를 던져도 생성은 계속된다. */
public interface PathGenerationListener {
  void onProgress(PathProgressEvent event);

  void onSuccess(long pathId);

  void onFailure(Throwable error);
}
