package ai.devpath.learning.content;

/** ai-svc 멘토 context용 내부 콘텐츠 조회 응답(본문은 truncate). */
public record InternalContentView(long id, String slug, String title, String track, String body) {
}
