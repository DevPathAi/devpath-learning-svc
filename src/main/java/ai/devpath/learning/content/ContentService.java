package ai.devpath.learning.content;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@Service
public class ContentService {
  private static final String PUBLISHED = "PUBLISHED";
  private static final int DEFAULT_LIMIT = 50;
  private static final int MAX_LIMIT = 100;

  private final ContentRepository contents;
  private final ContentProgressRepository progress;
  private final ContentProgressProperties properties;
  private final JsonMapper jsonMapper;

  public ContentService(ContentRepository contents, ContentProgressRepository progress,
      ContentProgressProperties properties, JsonMapper jsonMapper) {
    this.contents = contents;
    this.progress = progress;
    this.properties = properties;
    this.jsonMapper = jsonMapper;
  }

  @Transactional(readOnly = true)
  public LearningContentView get(long userId, String idOrSlug) {
    Content content = resolvePublished(idOrSlug);
    ContentProgressView progressView = progress.find(userId, content.getId())
        .map(this::toView)
        .orElseGet(ContentProgressView::empty);
    return toContentView(content, progressView);
  }

  @Transactional
  public UpsertContentProgressResponse upsertProgress(
      long userId, String idOrSlug, UpsertContentProgressRequest request) {
    validate(request);
    Content content = resolvePublished(idOrSlug);
    var row = progress.upsert(userId, content.getId(), request.scrollPct(), request.dwellSec(),
        properties.scrollThreshold(), properties.minDwellSec());
    int taskCompletedCount = row.completed()
        ? progress.completeActivePathTasks(userId, content.getId())
        : 0;
    return new UpsertContentProgressResponse(
        content.getId(),
        row.scrollPct(),
        row.dwellSec(),
        row.completed(),
        row.completedAt(),
        taskCompletedCount);
  }

  @Transactional(readOnly = true)
  public ProgressListView myProgress(long userId, Boolean completed, String track, Integer limit) {
    return new ProgressListView(progress.list(userId, completed, track, clampLimit(limit)));
  }

  private Content resolvePublished(String idOrSlug) {
    if (idOrSlug != null && idOrSlug.matches("\\d+")) {
      try {
        return contents.findByIdAndStatus(Long.parseLong(idOrSlug), PUBLISHED)
            .orElseThrow(() -> new ContentNotFoundException(idOrSlug));
      } catch (NumberFormatException e) {
        throw new InvalidContentIdException(idOrSlug);
      }
    }
    return contents.findBySlugAndStatus(idOrSlug, PUBLISHED)
        .orElseThrow(() -> new ContentNotFoundException(idOrSlug));
  }

  private void validate(UpsertContentProgressRequest request) {
    if (request == null || request.scrollPct() == null || request.dwellSec() == null) {
      throw new InvalidProgressException("scrollPct and dwellSec are required");
    }
    if (request.scrollPct() < 0.0 || request.scrollPct() > 1.0) {
      throw new InvalidProgressException("scrollPct must be between 0.0 and 1.0");
    }
    if (request.dwellSec() < 0) {
      throw new InvalidProgressException("dwellSec must be zero or greater");
    }
  }

  private LearningContentView toContentView(Content content, ContentProgressView progressView) {
    return new LearningContentView(
        content.getId(),
        content.getSlug(),
        content.getTitle(),
        content.getTrack(),
        content.getContentMd(),
        content.getEstimatedMinutes(),
        content.getDifficulty(),
        content.getBloomLevel(),
        conceptTags(content.getConceptTags()),
        progressView);
  }

  private ContentProgressView toView(ContentProgressRepository.ProgressRow row) {
    return new ContentProgressView(
        row.scrollPct(),
        row.dwellSec(),
        row.completed(),
        row.completedAt());
  }

  private List<String> conceptTags(String json) {
    if (json == null || json.isBlank()) return List.of();
    try {
      return jsonMapper.readValue(json, new TypeReference<List<String>>() {});
    } catch (Exception e) {
      return List.of();
    }
  }

  private int clampLimit(Integer limit) {
    if (limit == null) return DEFAULT_LIMIT;
    if (limit < 1) return DEFAULT_LIMIT;
    return Math.min(limit, MAX_LIMIT);
  }
}
