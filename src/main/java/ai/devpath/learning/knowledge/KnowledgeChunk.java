package ai.devpath.learning.knowledge;

/**
 * 지식베이스 검색 결과 한 건.
 *
 * <p>{@code chunkText}가 핵심이다 — ai-svc가 이 본문을 멘토 프롬프트에 근거로 주입한다.
 * 기존 {@code SimilarContent}가 제목만 돌려주던 것과 다르다.
 */
public record KnowledgeChunk(
    String docKey, String title, String category, String chunkText, double distance) {}
