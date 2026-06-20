package ai.devpath.learning.path;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "learning_paths")
public class LearningPath {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id", nullable = false) private Long userId;
  @Column(name = "generated_at", nullable = false) private Instant generatedAt;
  @Column(nullable = false) private String track;
  @Column(name = "total_weeks", nullable = false) private Integer totalWeeks;
  @Column(name = "gen_prompt_version") private String genPromptVersion;
  @Column(name = "source_embedding_version") private String sourceEmbeddingVersion;
  @Column(nullable = false) private String status;
  @Column(name = "ai_rationale") private String aiRationale;
  @OneToMany(mappedBy = "path", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("weekNum ASC")
  private List<PathMilestone> milestones = new ArrayList<>();

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public Instant getGeneratedAt() { return generatedAt; }
  public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
  public String getTrack() { return track; }
  public void setTrack(String track) { this.track = track; }
  public Integer getTotalWeeks() { return totalWeeks; }
  public void setTotalWeeks(Integer totalWeeks) { this.totalWeeks = totalWeeks; }
  public String getGenPromptVersion() { return genPromptVersion; }
  public void setGenPromptVersion(String genPromptVersion) { this.genPromptVersion = genPromptVersion; }
  public String getSourceEmbeddingVersion() { return sourceEmbeddingVersion; }
  public void setSourceEmbeddingVersion(String sourceEmbeddingVersion) { this.sourceEmbeddingVersion = sourceEmbeddingVersion; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public String getAiRationale() { return aiRationale; }
  public void setAiRationale(String aiRationale) { this.aiRationale = aiRationale; }
  public List<PathMilestone> getMilestones() { return milestones; }

  public void addMilestone(PathMilestone milestone) {
    milestone.setPath(this);
    milestones.add(milestone);
  }
}
