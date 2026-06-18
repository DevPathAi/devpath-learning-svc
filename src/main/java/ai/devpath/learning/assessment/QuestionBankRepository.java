package ai.devpath.learning.assessment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
  List<QuestionBank> findByTrack(String track);
}
