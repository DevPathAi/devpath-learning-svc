package ai.devpath.learning.progress;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActiveLearnerRepository {
  private final NamedParameterJdbcTemplate jdbc;

  public ActiveLearnerRepository(NamedParameterJdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  /** 활성(ACTIVE) 학습 경로를 보유한 전체 유저 ID — 스트릭 롤오버 스캔 대상. */
  public List<Long> activeLearnerUserIds() {
    return jdbc.queryForList(
        "SELECT DISTINCT user_id FROM learning_paths WHERE status = 'ACTIVE'", Map.of(), Long.class);
  }
}
