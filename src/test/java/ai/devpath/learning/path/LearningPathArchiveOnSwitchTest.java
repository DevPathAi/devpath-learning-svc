package ai.devpath.learning.path;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랙을 바꿔 재진단할 때 옛 학습 경로가 아카이브되는지 본다.
 *
 * <p>이 동작은 이미 LearningPathPersistenceService.persist() 안에 있다
 * (새 경로를 만들기 전에 archiveActiveByUserId 호출). 지키는 테스트가 없어
 * 그 한 줄이 지워지면 uq_learning_paths_active_user 위반으로 재진단이 깨진다.
 * 트랙 선택이 열리면서 이 경로가 처음으로 실제로 쓰인다.
 */
@SpringBootTest
@ActiveProfiles("test")
class LearningPathArchiveOnSwitchTest {

  @Autowired LearningPathRepository paths;
  @Autowired EntityManager em;

  private LearningPath activePath(long userId, String track) {
    LearningPath p = new LearningPath();
    p.setUserId(userId);
    p.setTrack(track);
    p.setStatus("ACTIVE");
    p.setGeneratedAt(Instant.now());
    p.setTotalWeeks(12);
    return p;
  }

  @Test
  @Transactional
  void archivesActivePathOfSameUser() {
    long userId = System.nanoTime();
    long pathId = paths.saveAndFlush(activePath(userId, "BACKEND_SPRING")).getId();

    int archived = paths.archiveActiveByUserId(userId);

    assertThat(archived).isEqualTo(1);
    // 벌크 UPDATE 는 영속성 컨텍스트를 건너뛴다 — 비우지 않으면 캐시의 옛 값을 읽는다.
    em.clear();
    assertThat(paths.findById(pathId).orElseThrow().getStatus()).isEqualTo("ARCHIVED");
  }

  @Test
  @Transactional
  void doesNotTouchOtherUsers() {
    long mine = System.nanoTime();
    long other = mine + 1;
    long othersPath = paths.saveAndFlush(activePath(other, "DEVOPS")).getId();
    paths.saveAndFlush(activePath(mine, "BACKEND_SPRING"));

    paths.archiveActiveByUserId(mine);

    // ★비우지 않으면 이 테스트는 판별력이 0이다 — 쿼리가 남의 행을 아카이브해도
    //  캐시가 ACTIVE 를 돌려줘 통과해 버린다.
    em.clear();
    assertThat(paths.findById(othersPath).orElseThrow().getStatus()).isEqualTo("ACTIVE");
  }

  @Test
  @Transactional
  void archiveMakesRoomForANewActivePath() {
    long userId = System.nanoTime();
    paths.saveAndFlush(activePath(userId, "BACKEND_SPRING"));

    paths.archiveActiveByUserId(userId);
    em.clear();

    // uq_learning_paths_active_user 는 ACTIVE 만 본다 — 아카이브 후에는 새 ACTIVE 가 들어간다.
    long newId = paths.saveAndFlush(activePath(userId, "DEVOPS")).getId();
    assertThat(paths.findById(newId).orElseThrow().getStatus()).isEqualTo("ACTIVE");
  }
}
