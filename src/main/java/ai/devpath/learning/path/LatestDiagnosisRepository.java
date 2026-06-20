package ai.devpath.learning.path;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Repository
public class LatestDiagnosisRepository {
  private final JdbcTemplate jdbc;
  private final JsonMapper jsonMapper;

  public LatestDiagnosisRepository(JdbcTemplate jdbc, JsonMapper jsonMapper) {
    this.jdbc = jdbc;
    this.jsonMapper = jsonMapper;
  }

  public Optional<LatestDiagnosis> findLatestCompleted(long userId) {
    String sql = """
        select r.assessment_id, a.track, r.diagnosed_level, r.strength_concepts,
               r.weakness_concepts, r.confidence_weight
        from assessment_results r
        join assessments a on r.assessment_id = a.id
        where a.user_id = ? and a.status = 'COMPLETED'
        order by a.completed_at desc nulls last, a.id desc
        limit 1
        """;
    return jdbc.query(sql, this::map, userId).stream().findFirst();
  }

  private LatestDiagnosis map(ResultSet rs, int rowNum) throws SQLException {
    return new LatestDiagnosis(
        rs.getLong("assessment_id"),
        rs.getString("track"),
        rs.getString("diagnosed_level"),
        parseList(rs.getString("strength_concepts")),
        parseList(rs.getString("weakness_concepts")),
        (Double) rs.getObject("confidence_weight"));
  }

  private List<String> parseList(String json) {
    if (json == null || json.isBlank()) return List.of();
    try {
      return jsonMapper.readValue(json, new TypeReference<List<String>>() {});
    } catch (Exception e) {
      return List.of();
    }
  }
}
