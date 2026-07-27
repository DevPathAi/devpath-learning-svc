package ai.devpath.learning.progress;

import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** 현재 활성 학습경로의 짧은 요약 문자열(재참여 문구용). 없으면 empty. */
@Repository
public class ActivePathSummaryReader {
	private final NamedParameterJdbcTemplate jdbc;

	public ActivePathSummaryReader(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public Optional<String> summarize(long userId) {
		var sql = """
				SELECT track, total_weeks FROM learning_paths
				WHERE user_id = :userId AND status = 'ACTIVE'
				ORDER BY generated_at DESC LIMIT 1
				""";
		return jdbc.query(sql, Map.of("userId", userId), (rs, n) ->
				rs.getString("track") + " 트랙 (" + rs.getInt("total_weeks") + "주 과정)")
				.stream().findFirst();
	}
}
