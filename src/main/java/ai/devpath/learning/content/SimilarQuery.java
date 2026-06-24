package ai.devpath.learning.content;

import java.util.List;

/** 멘토 참고자료 유사검색 요청(ai-svc가 질문 임베딩 768벡터를 전달). track null이면 전체. */
public record SimilarQuery(List<Double> embedding, Integer limit, String track) {
}
