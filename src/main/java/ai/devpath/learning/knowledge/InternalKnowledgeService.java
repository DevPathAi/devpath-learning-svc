package ai.devpath.learning.knowledge;

import java.util.List;
import org.springframework.stereotype.Service;

/** 지식베이스 유사검색. track 필터가 없다 — 학습 문서에는 track 개념이 없다. */
@Service
public class InternalKnowledgeService {

  private static final int DEFAULT_LIMIT = 3;
  private static final int MAX_LIMIT = 10;

  private final KnowledgeEmbeddingMatcher matcher;

  public InternalKnowledgeService(KnowledgeEmbeddingMatcher matcher) {
    this.matcher = matcher;
  }

  public List<KnowledgeChunk> search(KnowledgeQuery query) {
    return matcher.search(query.embedding(), clampLimit(query.limit()));
  }

  private int clampLimit(Integer limit) {
    if (limit == null || limit < 1) return DEFAULT_LIMIT;
    return Math.min(limit, MAX_LIMIT);
  }
}
