package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentSeedSqlWriter {

  private final ObjectMapper mapper = JsonMapper.builder().build();

  public String toSql(List<ApprovedContent> contents, List<EmbeddingRecord> embeddings) {
    var orderedContents = contents.stream().sorted(contentOrdering()).toList();
    var embeddingsBySlug = embeddings.stream()
        .collect(Collectors.groupingBy(EmbeddingRecord::slug));

    var sql = new StringBuilder();
    appendContents(sql, orderedContents);
    sql.append('\n');
    appendEmbeddings(sql, orderedContents, embeddingsBySlug);
    return sql.toString();
  }

  private void appendContents(StringBuilder sql, List<ApprovedContent> contents) {
    sql.append("INSERT INTO contents ")
        .append("(slug, title, track, content_md, estimated_minutes, difficulty, bloom_level, ")
        .append("concept_tags, status) VALUES\n");
    for (int i = 0; i < contents.size(); i++) {
      var c = contents.get(i);
      sql.append("(")
          .append(literal(c.slug())).append(",")
          .append(literal(c.title())).append(",")
          .append(literal(c.track())).append(",")
          .append(literal(c.contentMd())).append(",")
          .append(c.estimatedMinutes()).append(",")
          .append(c.difficulty()).append(",")
          .append(literal(c.bloomLevel())).append(",")
          .append(literal(json(c.conceptTags()))).append("::jsonb,")
          .append(literal(c.status()))
          .append(")");
      sql.append(i == contents.size() - 1 ? ";\n" : ",\n");
    }
  }

  private void appendEmbeddings(StringBuilder sql, List<ApprovedContent> contents,
      Map<String, List<EmbeddingRecord>> embeddingsBySlug) {
    for (ApprovedContent content : contents) {
      var ordered = embeddingsBySlug.getOrDefault(content.slug(), List.of()).stream()
          .sorted(Comparator.comparing(EmbeddingRecord::chunkIndex))
          .toList();
      for (EmbeddingRecord e : ordered) {
        sql.append("INSERT INTO content_embeddings ")
            .append("(content_id, chunk_index, chunk_text, embedding, chunk_hash, status)\n")
            .append("SELECT c.id, ")
            .append(e.chunkIndex()).append(", ")
            .append(literal(e.chunkText())).append(", ")
            .append(literal(vectorLiteral(e.embedding()))).append("::vector, ")
            .append(literal(e.chunkHash())).append(", ")
            .append(literal(e.status())).append("\n")
            .append("FROM contents c WHERE c.slug = ")
            .append(literal(e.slug())).append(";\n");
      }
    }
  }

  private Comparator<ApprovedContent> contentOrdering() {
    return Comparator
        .comparingInt((ApprovedContent c) -> orderOf(ContentQuota.TRACKS, c.track()))
        .thenComparingInt(c -> orderOf(List.copyOf(ContentQuota.LEVEL_TARGETS.keySet()), c.level()))
        .thenComparing(ApprovedContent::slug);
  }

  private int orderOf(List<String> values, String value) {
    var index = values.indexOf(value);
    return index < 0 ? Integer.MAX_VALUE : index;
  }

  private String json(Object value) {
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unable to serialize content JSON field", e);
    }
  }

  private String vectorLiteral(List<Double> values) {
    return values.stream()
        .map(value -> String.format(Locale.ROOT, "%.6f", value))
        .collect(Collectors.joining(",", "[", "]"));
  }

  private String literal(String value) {
    if (value == null) return "NULL";
    return "'" + value.replace("'", "''") + "'";
  }
}
