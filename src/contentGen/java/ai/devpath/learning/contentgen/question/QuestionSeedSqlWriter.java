package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;

public class QuestionSeedSqlWriter {

  private static final String INSERT_PREFIX = "INSERT INTO question_bank "
      + "(track, question_type, content, options, answer_key, bloom_level, difficulty, concept_tags) "
      + "VALUES\n";

  private final ObjectMapper mapper = JsonMapper.builder().build();

  public String toSql(List<ApprovedQuestion> questions) {
    var ordered = questions.stream().sorted(ordering()).toList();
    var sql = new StringBuilder(INSERT_PREFIX);
    for (int i = 0; i < ordered.size(); i++) {
      var q = ordered.get(i);
      sql.append("(")
          .append(literal(q.track())).append(",")
          .append(literal(q.questionType())).append(",")
          .append(literal(q.content())).append(",")
          .append(literal(json(q.options()))).append(",")
          .append(literal(json(q.answerKey()))).append(",")
          .append(literal(q.bloomLevel())).append(",")
          .append(q.difficulty()).append(",")
          .append(literal(json(q.conceptTags())))
          .append(")");
      sql.append(i == ordered.size() - 1 ? ";\n" : ",\n");
    }
    return sql.toString();
  }

  private Comparator<ApprovedQuestion> ordering() {
    return Comparator
        .comparingInt((ApprovedQuestion q) -> orderOf(QuestionQuota.TRACKS, q.track()))
        .thenComparingInt(q -> orderOf(List.of("MCQ", "CODE_READING"), q.questionType()))
        .thenComparingDouble(q -> q.difficulty() == null ? Double.POSITIVE_INFINITY : q.difficulty())
        .thenComparing(q -> sha256(normalize(q.content())));
  }

  private int orderOf(List<String> values, String value) {
    var index = values.indexOf(value);
    return index < 0 ? Integer.MAX_VALUE : index;
  }

  private String json(Object value) {
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unable to serialize question JSON field", e);
    }
  }

  private String literal(String value) {
    if (value == null) return "NULL";
    return "'" + value.replace("'", "''") + "'";
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().replaceAll("\\s+", " ").toLowerCase();
  }

  private String sha256(String value) {
    try {
      var digest = MessageDigest.getInstance("SHA-256")
          .digest(value.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 unavailable", e);
    }
  }
}
