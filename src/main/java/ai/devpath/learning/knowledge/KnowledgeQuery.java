package ai.devpath.learning.knowledge;

import java.util.List;

/** 지식베이스 유사검색 요청. track이 없다 — 학습 문서에는 track 개념이 없다. */
public record KnowledgeQuery(List<Double> embedding, Integer limit) {}
