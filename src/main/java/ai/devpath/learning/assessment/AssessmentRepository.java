package ai.devpath.learning.assessment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

  Optional<Assessment> findBySourceGuestId(String sourceGuestId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Query(value = """
      update assessments
      set source_guest_id = :guestId
      where id = :assessmentId
        and user_id = :userId
        and source_guest_id is null
      """, nativeQuery = true)
  int bindLegacyClaimSource(@Param("assessmentId") long assessmentId,
      @Param("userId") long userId, @Param("guestId") String guestId);

  /**
   * 새 진단을 시작할 때 그 이용자의 옛 IN_PROGRESS 세션을 정리한다.
   * status CHECK 에 ABANDONED 가 이미 있어 스키마 변경이 없다.
   */
  @Modifying
  @Query(
      "update Assessment a set a.status = 'ABANDONED' "
          + "where a.userId = :userId and a.status = 'IN_PROGRESS'")
  int abandonInProgressByUserId(@Param("userId") Long userId);
}
