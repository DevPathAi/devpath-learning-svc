package ai.devpath.learning.assessment;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "question_bank")
public class QuestionBank {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false) private String track;
  @Column(name = "question_type", nullable = false) private String questionType;
  @Column(nullable = false) private String content;
  @JdbcTypeCode(SqlTypes.JSON) private String options;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "answer_key", nullable = false) private String answerKey;
  @Column(name = "bloom_level", nullable = false) private String bloomLevel;
  @Column(nullable = false) private double difficulty;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "concept_tags") private String conceptTags;

  public Long getId() { return id; }
  public String getTrack() { return track; }
  public void setTrack(String v) { this.track = v; }
  public String getQuestionType() { return questionType; }
  public void setQuestionType(String v) { this.questionType = v; }
  public String getContent() { return content; }
  public void setContent(String v) { this.content = v; }
  public String getOptions() { return options; }
  public void setOptions(String v) { this.options = v; }
  public String getAnswerKey() { return answerKey; }
  public void setAnswerKey(String v) { this.answerKey = v; }
  public String getBloomLevel() { return bloomLevel; }
  public void setBloomLevel(String v) { this.bloomLevel = v; }
  public double getDifficulty() { return difficulty; }
  public void setDifficulty(double v) { this.difficulty = v; }
  public String getConceptTags() { return conceptTags; }
  public void setConceptTags(String v) { this.conceptTags = v; }
}
