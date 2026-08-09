package ai.devpath.learning.knowledgegen;

import java.util.List;

/** embeddings.jsonl 한 줄. 적재 커맨드가 이 형태를 그대로 읽는다. */
public record KnowledgeEmbeddingRecord(
    String docKey,
    String title,
    String category,
    String docHash,
    int chunkIndex,
    String chunkText,
    List<Double> embedding,
    String chunkHash,
    String sourceCommit) {}
