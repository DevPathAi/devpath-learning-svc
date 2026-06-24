package ai.devpath.learning.content;

import ai.devpath.learning.path.Content;
import ai.devpath.learning.path.ContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** ai-svc 멘토가 사용자 JWT 없이 콘텐츠 본문을 가져오는 내부 조회(게이트웨이 미경유, 슬라이스 #7). */
@Service
public class InternalContentService {
  private static final String PUBLISHED = "PUBLISHED";
  static final int MAX_BODY_CHARS = 4000;

  private final ContentRepository contents;

  public InternalContentService(ContentRepository contents) {
    this.contents = contents;
  }

  @Transactional(readOnly = true)
  public InternalContentView get(long id) {
    Content content = contents.findByIdAndStatus(id, PUBLISHED)
        .orElseThrow(() -> new ContentNotFoundException(String.valueOf(id)));
    return new InternalContentView(
        content.getId(),
        content.getSlug(),
        content.getTitle(),
        content.getTrack(),
        truncate(content.getContentMd()));
  }

  private String truncate(String body) {
    if (body == null) return "";
    if (body.length() <= MAX_BODY_CHARS) return body;
    return body.substring(0, MAX_BODY_CHARS) + "…";
  }
}
