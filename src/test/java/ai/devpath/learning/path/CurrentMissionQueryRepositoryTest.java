package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class CurrentMissionQueryRepositoryTest {
  @Test
  @SuppressWarnings("unchecked")
  void projectionUsesOneDatabaseStatement() {
    NamedParameterJdbcTemplate jdbc = mock(NamedParameterJdbcTemplate.class);
    when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(List.of());
    CurrentMissionQueryRepository repository = new CurrentMissionQueryRepository(jdbc);

    ThisWeekView result = repository.findForUser(42L);

    assertThat(result.outcome()).isEqualTo(CurrentMissionOutcome.NO_ACTIVE_PATH);
    verify(jdbc).query(anyString(), anyMap(), any(RowMapper.class));
    verifyNoMoreInteractions(jdbc);
  }
}
