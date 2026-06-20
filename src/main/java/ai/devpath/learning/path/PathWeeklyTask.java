package ai.devpath.learning.path;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "path_weekly_tasks")
public class PathWeeklyTask {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "milestone_id")
  private PathMilestone milestone;
  @Column(name = "order_num", nullable = false) private Integer orderNum;
  @Column(name = "content_id") private Long contentId;
  @Column(name = "task_type", nullable = false) private String taskType;
  @Column(nullable = false) private String title;
  @Column(nullable = false) private Boolean required;
  @Column(name = "completed_at") private Instant completedAt;

  public Long getId() { return id; }
  public PathMilestone getMilestone() { return milestone; }
  public void setMilestone(PathMilestone milestone) { this.milestone = milestone; }
  public Integer getOrderNum() { return orderNum; }
  public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
  public Long getContentId() { return contentId; }
  public void setContentId(Long contentId) { this.contentId = contentId; }
  public String getTaskType() { return taskType; }
  public void setTaskType(String taskType) { this.taskType = taskType; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public Boolean getRequired() { return required; }
  public void setRequired(Boolean required) { this.required = required; }
  public Instant getCompletedAt() { return completedAt; }
  public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
