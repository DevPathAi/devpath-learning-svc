package ai.devpath.learning.content;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 서비스 간 내부 조회(게이트웨이 미경유). ai-svc 멘토가 콘텐츠 본문을 context로 가져온다. */
@RestController
@RequestMapping("/internal/contents")
public class InternalContentController {
  private final InternalContentService service;

  public InternalContentController(InternalContentService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public InternalContentView get(@PathVariable long id) {
    return service.get(id);
  }
}
