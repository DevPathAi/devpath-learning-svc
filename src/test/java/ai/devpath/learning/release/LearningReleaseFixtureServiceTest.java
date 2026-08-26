package ai.devpath.learning.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import ai.devpath.learning.path.LearningPath;
import ai.devpath.learning.path.LearningPathRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class LearningReleaseFixtureServiceTest {
  private static final String CANDIDATE = "a".repeat(64);
  private static final String RUN = "R".repeat(43);

  @Test
  void createsOneContentLinkedActiveMissionForTheBoundWorkspaceUser() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    ContentRepository contents = mock(ContentRepository.class);
    LearningReleaseRegistry registry = mock(LearningReleaseRegistry.class);
    Content content = mock(Content.class);
    when(content.getId()).thenReturn(73L);
    when(content.getTitle()).thenReturn("Spring transaction boundaries");
    when(paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(42L, "ACTIVE"))
        .thenReturn(Optional.empty());
    when(contents.findFirstByTrackAndStatusOrderByIdAsc(
        "BACKEND_SPRING", "PUBLISHED")).thenReturn(Optional.of(content));

    LearningReleaseFixtureService service =
        new LearningReleaseFixtureService(paths, contents, registry);
    service.prepare(CANDIDATE, RUN, 42L);

    verify(registry).bindUser(CANDIDATE, RUN, 42L);
    ArgumentCaptor<LearningPath> saved = ArgumentCaptor.forClass(LearningPath.class);
    verify(paths).saveAndFlush(saved.capture());
    LearningPath path = saved.getValue();
    assertThat(path.getUserId()).isEqualTo(42L);
    assertThat(path.getTrack()).isEqualTo("BACKEND_SPRING");
    assertThat(path.getStatus()).isEqualTo("ACTIVE");
    assertThat(path.getMilestones()).hasSize(1);
    assertThat(path.getMilestones().getFirst().getTasks()).hasSize(1);
    assertThat(path.getMilestones().getFirst().getTasks().getFirst().getContentId())
        .isEqualTo(73L);
  }

  @Test
  void reusesAnExistingActiveFixturePathWithoutCreatingAnother() {
    LearningPathRepository paths = mock(LearningPathRepository.class);
    ContentRepository contents = mock(ContentRepository.class);
    LearningReleaseRegistry registry = mock(LearningReleaseRegistry.class);
    LearningPath existing = new LearningPath();
    when(paths.findFirstByUserIdAndStatusOrderByGeneratedAtDesc(42L, "ACTIVE"))
        .thenReturn(Optional.of(existing));

    new LearningReleaseFixtureService(paths, contents, registry)
        .prepare(CANDIDATE, RUN, 42L);

    verify(registry).bindUser(CANDIDATE, RUN, 42L);
    verify(contents, never()).findFirstByTrackAndStatusOrderByIdAsc(
        "BACKEND_SPRING", "PUBLISHED");
    verify(paths, never()).saveAndFlush(org.mockito.ArgumentMatchers.any());
  }
}
