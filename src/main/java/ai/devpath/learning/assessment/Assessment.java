package ai.devpath.learning.assessment;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assessments")
public class Assessment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id") private Long userId;
  @Column(nullable = false) private String track;
  @Column(nullable = false) private String status;
  @Column(name = "current_difficulty", nullable = false) private double currentDifficulty;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "bloom_distribution") private String bloomDistribution;
  @Column(name = "started_at", nullable = false) private Instant startedAt;
  @Column(name = "completed_at") private Instant completedAt;

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public void setUserId(Long v) { this.userId = v; }
  public String getTrack() { return track; }
  public void setTrack(String v) { this.track = v; }
  public String getStatus() { return status; }
  public void setStatus(String v) { this.status = v; }
  public double getCurrentDifficulty() { return currentDifficulty; }
  public void setCurrentDifficulty(double v) { this.currentDifficulty = v; }
  public String getBloomDistribution() { return bloomDistribution; }
  public void setBloomDistribution(String v) { this.bloomDistribution = v; }
  public Instant getStartedAt() { return startedAt; }
  public void setStartedAt(Instant v) { this.startedAt = v; }
  public Instant getCompletedAt() { return completedAt; }
  public void setCompletedAt(Instant v) { this.completedAt = v; }
}
