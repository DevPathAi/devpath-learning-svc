package ai.devpath.learning.path;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "path_milestones")
public class PathMilestone {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "path_id")
  private LearningPath path;
  @Column(name = "week_num", nullable = false) private Integer weekNum;
  @Column(nullable = false) private String title;
  @Column(name = "goal_description") private String goalDescription;
  @JdbcTypeCode(SqlTypes.JSON) @Column(name = "target_skills") private String targetSkills;
  @Column(name = "estimated_hours") private Integer estimatedHours;
  @Column(name = "why_this_order") private String whyThisOrder;
  @Column(name = "expected_outcome") private String expectedOutcome;
  @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("orderNum ASC")
  private List<PathWeeklyTask> tasks = new ArrayList<>();

  public Long getId() { return id; }
  public LearningPath getPath() { return path; }
  public void setPath(LearningPath path) { this.path = path; }
  public Integer getWeekNum() { return weekNum; }
  public void setWeekNum(Integer weekNum) { this.weekNum = weekNum; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getGoalDescription() { return goalDescription; }
  public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }
  public String getTargetSkills() { return targetSkills; }
  public void setTargetSkills(String targetSkills) { this.targetSkills = targetSkills; }
  public Integer getEstimatedHours() { return estimatedHours; }
  public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }
  public String getWhyThisOrder() { return whyThisOrder; }
  public void setWhyThisOrder(String whyThisOrder) { this.whyThisOrder = whyThisOrder; }
  public String getExpectedOutcome() { return expectedOutcome; }
  public void setExpectedOutcome(String expectedOutcome) { this.expectedOutcome = expectedOutcome; }
  public List<PathWeeklyTask> getTasks() { return tasks; }

  public void addTask(PathWeeklyTask task) {
    task.setMilestone(this);
    tasks.add(task);
  }
}
