package ai.devpath.learning.path;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {
  @EntityGraph(attributePaths = {"milestones"})
  Optional<LearningPath> findFirstByUserIdAndStatusOrderByGeneratedAtDesc(Long userId, String status);

  @Modifying
  @Query("update LearningPath p set p.status = 'ARCHIVED' where p.userId = :userId and p.status = 'ACTIVE'")
  int archiveActiveByUserId(@Param("userId") Long userId);
}
