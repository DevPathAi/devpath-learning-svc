package ai.devpath.learning.path;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "contents")
public class Content {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false) private String slug;
  @Column(nullable = false) private String title;
  @Column(nullable = false) private String track;
  @Column(name = "estimated_minutes") private Integer estimatedMinutes;
  private Double difficulty;
  @Column(name = "bloom_level") private String bloomLevel;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "concept_tags") private String conceptTags;
  @Column(nullable = false) private String status;

  public Long getId() { return id; }
  public String getSlug() { return slug; }
  public void setSlug(String slug) { this.slug = slug; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getTrack() { return track; }
  public void setTrack(String track) { this.track = track; }
  public Integer getEstimatedMinutes() { return estimatedMinutes; }
  public void setEstimatedMinutes(Integer estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
  public Double getDifficulty() { return difficulty; }
  public void setDifficulty(Double difficulty) { this.difficulty = difficulty; }
  public String getBloomLevel() { return bloomLevel; }
  public void setBloomLevel(String bloomLevel) { this.bloomLevel = bloomLevel; }
  public String getConceptTags() { return conceptTags; }
  public void setConceptTags(String conceptTags) { this.conceptTags = conceptTags; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
