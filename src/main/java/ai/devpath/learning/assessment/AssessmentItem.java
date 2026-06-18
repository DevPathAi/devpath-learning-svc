package ai.devpath.learning.assessment;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "assessment_items")
public class AssessmentItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "assessment_id", nullable = false) private Long assessmentId;
  @Column(name = "question_bank_id", nullable = false) private Long questionBankId;
  @Column(name = "order_num", nullable = false) private int orderNum;
  @Column(name = "presented_at", nullable = false) private Instant presentedAt;
  @Column(name = "answered_at") private Instant answeredAt;
  @JdbcTypeCode(SqlTypes.JSON) private String answer;
  @Column(name = "is_correct") private Boolean isCorrect;
  @Column(nullable = false) private boolean skipped;
  @Column(name = "time_spent_sec") private Integer timeSpentSec;

  public Long getId() { return id; }
  public Long getAssessmentId() { return assessmentId; }
  public void setAssessmentId(Long v) { this.assessmentId = v; }
  public Long getQuestionBankId() { return questionBankId; }
  public void setQuestionBankId(Long v) { this.questionBankId = v; }
  public int getOrderNum() { return orderNum; }
  public void setOrderNum(int v) { this.orderNum = v; }
  public Instant getPresentedAt() { return presentedAt; }
  public void setPresentedAt(Instant v) { this.presentedAt = v; }
  public Instant getAnsweredAt() { return answeredAt; }
  public void setAnsweredAt(Instant v) { this.answeredAt = v; }
  public String getAnswer() { return answer; }
  public void setAnswer(String v) { this.answer = v; }
  public Boolean getIsCorrect() { return isCorrect; }
  public void setIsCorrect(Boolean v) { this.isCorrect = v; }
  public boolean isSkipped() { return skipped; }
  public void setSkipped(boolean v) { this.skipped = v; }
  public Integer getTimeSpentSec() { return timeSpentSec; }
  public void setTimeSpentSec(Integer v) { this.timeSpentSec = v; }
}
