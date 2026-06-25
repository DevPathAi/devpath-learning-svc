package ai.devpath.learning.content;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 서비스 간 내부 유사검색(게이트웨이 미경유). body가 768벡터라 GET 아님 POST. */
@RestController
@RequestMapping("/internal/contents")
public class InternalSimilarController {
  private final InternalSimilarService service;

  public InternalSimilarController(InternalSimilarService service) {
    this.service = service;
  }

  @PostMapping("/similar")
  public List<SimilarContent> similar(@RequestBody SimilarQuery query) {
    return service.search(query);
  }
}
