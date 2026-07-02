package ai.devpath.learning.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserStreakRepositoryTest {

  @Autowired UserStreakRepository streaks;

  @Test
  void savesAndFindsByUserId() {
    UserStreak s = new UserStreak();
    s.setUserId(777001L);
    s.setCurrentDays(3);
    s.setLongestDays(5);
    streaks.save(s);

    UserStreak found = streaks.findById(777001L).orElseThrow();
    assertThat(found.getCurrentDays()).isEqualTo(3);
    assertThat(found.getLongestDays()).isEqualTo(5);
  }

  @Test
  void findByIdReturnsEmptyWhenNoRow() {
    assertThat(streaks.findById(777999L)).isEmpty();
  }
}
