package ai.devpath.learning.path;

public record PathProgressEvent(String stage, double progress, String message, Long pathId) {
  public static PathProgressEvent collecting() {
    return new PathProgressEvent("collecting", 0.15, "진단 결과를 분석하고 있어요.", null);
  }

  public static PathProgressEvent generating() {
    return new PathProgressEvent("generating", 0.45, "개인화 학습경로를 생성하고 있어요.", null);
  }

  public static PathProgressEvent matching() {
    return new PathProgressEvent("matching", 0.75, "학습 콘텐츠를 매칭하고 있어요.", null);
  }

  public static PathProgressEvent done(long pathId) {
    return new PathProgressEvent("done", 1.0, "학습경로가 준비됐어요.", pathId);
  }

  public static PathProgressEvent error(String message) {
    return new PathProgressEvent("error", 1.0, message, null);
  }
}
