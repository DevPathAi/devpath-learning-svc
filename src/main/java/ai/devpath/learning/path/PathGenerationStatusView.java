package ai.devpath.learning.path;

/** 생성 상태 조회 응답. 생성 이력이 없으면 {@code state=NONE}. */
public record PathGenerationStatusView(String state, Long pathId, String errorMessage) {

  public static PathGenerationStatusView none() {
    return new PathGenerationStatusView("NONE", null, null);
  }

  public static PathGenerationStatusView of(PathGenerationJob.Status status) {
    return new PathGenerationStatusView(status.state().name(), status.pathId(), status.errorMessage());
  }
}
