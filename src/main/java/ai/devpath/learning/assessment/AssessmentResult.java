package ai.devpath.learning.assessment;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assessment_results")
public class AssessmentResult {
  @Id @Column(name = "assessment_id") private Long assessmentId;
  @Column(name = "diagnosed_level", nullable = false) private String diagnosedLevel;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "concept_scores") private String conceptScores;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "strength_concepts") private String strengthConcepts;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "weakness_concepts") private String weaknessConcepts;
  @Column(name = "confidence_weight") private Double confidenceWeight;

  public Long getAssessmentId() { return assessmentId; }
  public void setAssessmentId(Long v) { this.assessmentId = v; }
  public String getDiagnosedLevel() { return diagnosedLevel; }
  public void setDiagnosedLevel(String v) { this.diagnosedLevel = v; }
  public String getConceptScores() { return conceptScores; }
  public void setConceptScores(String v) { this.conceptScores = v; }
  public String getStrengthConcepts() { return strengthConcepts; }
  public void setStrengthConcepts(String v) { this.strengthConcepts = v; }
  public String getWeaknessConcepts() { return weaknessConcepts; }
  public void setWeaknessConcepts(String v) { this.weaknessConcepts = v; }
  public Double getConfidenceWeight() { return confidenceWeight; }
  public void setConfidenceWeight(Double v) { this.confidenceWeight = v; }
}
