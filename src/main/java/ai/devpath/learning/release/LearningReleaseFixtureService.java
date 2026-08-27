package ai.devpath.learning.release;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import ai.devpath.learning.path.LearningPath;
import ai.devpath.learning.path.LearningPathRepository;
import ai.devpath.learning.path.PathMilestone;
import ai.devpath.learning.path.PathWeeklyTask;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Creates the minimum real learning state needed by the workspace release journey. */
@Service
public class LearningReleaseFixtureService {
  private static final String TRACK = "BACKEND_SPRING";

  private final LearningPathRepository paths;
  private final ContentRepository contents;
  private final LearningReleaseRegistry release;

  public LearningReleaseFixtureService(
      LearningPathRepository paths,
      ContentRepository contents,
      LearningReleaseRegistry release) {
    this.paths = paths;
    this.contents = contents;
    this.release = release;
  }

  @Transactional
  public void prepare(String candidate, String runKey, long userId) {
    release.bindUser(candidate, runKey, userId);
    if (paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(userId, "ACTIVE").isPresent()) {
      return;
    }

    Content content = contents.findFirstByTrackAndStatusOrderByIdAsc(TRACK, "PUBLISHED")
        .orElseThrow(() -> new IllegalStateException(
            "published release fixture content is unavailable"));

    LearningPath path = new LearningPath();
    path.setUserId(userId);
    path.setGeneratedAt(Instant.now());
    path.setTrack(TRACK);
    path.setTotalWeeks(1);
    path.setGenPromptVersion("release-path-v1");
    path.setSourceEmbeddingVersion("release-fixture");
    path.setStatus("ACTIVE");
    path.setAiRationale("Deterministic workspace release fixture");

    PathMilestone milestone = new PathMilestone();
    milestone.setWeekNum(1);
    milestone.setTitle("Release workspace mission");
    milestone.setGoalDescription("Exercise the production workspace path");
    milestone.setTargetSkills("[\"Spring\"]");
    milestone.setEstimatedHours(1);
    milestone.setWhyThisOrder("Deterministic release ordering");
    milestone.setExpectedOutcome("Open one content-linked mission");

    PathWeeklyTask linkedTask = new PathWeeklyTask();
    linkedTask.setOrderNum(1);
    linkedTask.setContentId(content.getId());
    linkedTask.setTaskType("READ");
    linkedTask.setTitle(content.getTitle());
    linkedTask.setRequired(true);
    milestone.addTask(linkedTask);

    PathWeeklyTask contentlessTask = new PathWeeklyTask();
    contentlessTask.setOrderNum(2);
    contentlessTask.setTaskType("QUIZ");
    contentlessTask.setTitle("Release completion checkpoint");
    contentlessTask.setRequired(true);
    milestone.addTask(contentlessTask);
    path.addMilestone(milestone);
    paths.saveAndFlush(path);
  }
}
