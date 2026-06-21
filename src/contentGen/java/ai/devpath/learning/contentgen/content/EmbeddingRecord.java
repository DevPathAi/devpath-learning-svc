package ai.devpath.learning.contentgen.content;

import java.util.List;

public record EmbeddingRecord(
    String slug,
    Integer chunkIndex,
    String chunkText,
    List<Double> embedding,
    String chunkHash,
    String status) {
}
