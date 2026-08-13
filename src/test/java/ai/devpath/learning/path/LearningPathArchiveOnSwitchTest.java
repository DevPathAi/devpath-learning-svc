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
 * 트랙을 바꿔 재진단할 때 쓰이는 아카이브 <b>쿼리</b>를 고정한다.
 *
 * <p>범위는 {@code LearningPathRepository.archiveActiveByUserId} 하나다 — 내 ACTIVE 만
 * ARCHIVED 로 내리는지, 그 뒤 새 ACTIVE 가 들어가는지(uq_learning_paths_active_user).
 * 이 테스트는 {@code LearningPathPersistenceService.persist()} 를 호출하지 않으므로
 * <b>persist() 가 이 쿼리를 부른다는 사실은 지키지 않는다.</b> 그 호출은
 * {@code LearningPathPersistenceServiceTest.persistArchivesActivePathBeforeInsertingNewOne} 이
 * 고정한다. 둘이 함께 있어야 재진단이 지켜진다.
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
