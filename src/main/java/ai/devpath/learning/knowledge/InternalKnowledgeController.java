package ai.devpath.learning.knowledge;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 서비스 간 내부 지식 검색(게이트웨이 미경유). body가 768벡터라 GET 아님 POST. */
@RestController
@RequestMapping("/internal/knowledge")
public class InternalKnowledgeController {

  private final InternalKnowledgeService service;

  public InternalKnowledgeController(InternalKnowledgeService service) {
    this.service = service;
  }

  @PostMapping("/similar")
  public List<KnowledgeChunk> similar(@RequestBody KnowledgeQuery query) {
    return service.search(query);
  }
}
