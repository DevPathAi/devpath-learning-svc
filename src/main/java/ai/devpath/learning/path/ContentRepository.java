package ai.devpath.learning.path;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
  Optional<Content> findByIdAndStatus(Long id, String status);
  Optional<Content> findBySlugAndStatus(String slug, String status);
}
