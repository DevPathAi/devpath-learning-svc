package ai.devpath.learning.content;

import ai.devpath.learning.path.ContentEmbeddingMatcher;
import ai.devpath.learning.path.MatchedContent;
import java.util.List;
import org.springframework.stereotype.Service;

/** 멘토 참고자료: 질문 임베딩(768)으로 유사 콘텐츠 검색(ai-svc가 벡터 전달, 슬라이스 #7 M-4). */
@Service
public class InternalSimilarService {
  private static final int DEFAULT_LIMIT = 3;
  private static final int MAX_LIMIT = 10;

  private final ContentEmbeddingMatcher matcher;

  public InternalSimilarService(ContentEmbeddingMatcher matcher) {
    this.matcher = matcher;
  }

  public List<SimilarContent> search(SimilarQuery query) {
    int limit = clampLimit(query.limit());
    // track이 있으면 동일 트랙 우선(정확도↑), 없으면 전체 PUBLISHED(M-4 2-경로).
    List<MatchedContent> matched = hasTrack(query.track())
        ? matcher.match(query.track(), query.embedding(), limit)
        : matcher.matchAny(query.embedding(), limit);
    return matched.stream()
        .map(m -> new SimilarContent(m.contentId(), m.slug(), m.title()))
        .toList();
  }

  private boolean hasTrack(String track) {
    return track != null && !track.isBlank();
  }

  private int clampLimit(Integer limit) {
    if (limit == null || limit < 1) return DEFAULT_LIMIT;
    return Math.min(limit, MAX_LIMIT);
  }
}
