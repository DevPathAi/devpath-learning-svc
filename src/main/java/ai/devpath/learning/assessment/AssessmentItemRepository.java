package ai.devpath.learning.assessment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentItemRepository extends JpaRepository<AssessmentItem, Long> {
  List<AssessmentItem> findByAssessmentIdOrderByOrderNumAsc(Long assessmentId);
}
