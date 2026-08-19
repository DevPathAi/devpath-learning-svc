package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * 콘텐츠 생성은 문항처럼 루프에 빠지지는 않았지만 **레벨 분포를 지키지 않는다**.
 * 실측: NODE_TYPESCRIPT 34건이 INTRO 6 · INTERMEDIATE 14 · ADVANCED 14 로 나와
 * 게이트가 "must contain 8 INTRO"·"must contain 8 ADVANCED" 로 막았다.
 */
class ContentHarvesterTest {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  private static String line(String track, String level, String slug, String markdown) {
    return "{\"track\":\"" + track + "\",\"level\":\"" + level + "\",\"slug\":\"" + slug
        + "\",\"title\":\"제목 " + slug + "\",\"status\":\"PUBLISHED\",\"estimatedMinutes\":10,"
        + "\"difficulty\":0.4,\"bloomLevel\":\"UNDERSTAND\",\"conceptTags\":[\"node-basics\"],"
        + "\"contentMd\":\"" + markdown + "\"}";
  }

  private static String body(String slug) {
    return "설명 " + slug + "\\n\\n```ts\\nconst a = 1;\\n```\\n";
  }

  private static ContentDraftClient distinctClient(AtomicInteger calls) {
    return (track, level, count, prompt) -> {
      var index = calls.incrementAndGet();
      return IntStream.range(0, count)
          .mapToObj(i -> {
            var slug = "node-" + level.toLowerCase() + "-" + index + "-" + i;
            return line(track, level, slug, body(slug));
          })
          .collect(Collectors.joining("\n"));
    };
  }

  @Test
  void fillsEveryLevelToItsQuota() throws Exception {
    var harvested = new ContentHarvester(distinctClient(new AtomicInteger()), 3)
        .harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).hasSize(30);
    var byLevel = harvested.stream().collect(Collectors.groupingBy(
        l -> field(l, "level"), LinkedHashMap::new, Collectors.counting()));
    assertThat(byLevel.get("INTRO")).isEqualTo(8L);
    assertThat(byLevel.get("INTERMEDIATE")).isEqualTo(14L);
    assertThat(byLevel.get("ADVANCED")).isEqualTo(8L);
  }

  // ★이 테스트가 실측한 결함을 겨냥한다★ 모델이 요청한 레벨과 다른 레벨을 내면 분포가
  // 조용히 어긋난다. 요청한 레벨이 아닌 행은 받지 않는다.
  @Test
  void dropsRowsWhoseLevelDoesNotMatchTheRequest() throws Exception {
    // slug 가 겹치면 중복 제거에 먼저 걸려 레벨 가드가 없어도 결과가 같아진다
    // (실측으로 이 테스트가 가드를 지워도 통과했다). 호출마다 새 slug 를 준다.
    var calls = new AtomicInteger();
    ContentDraftClient alwaysAdvanced = (track, level, count, prompt) -> {
      var index = calls.incrementAndGet();
      return IntStream.range(0, count)
          .mapToObj(i -> {
            var slug = "node-always-advanced-" + index + "-" + i;
            return line(track, "ADVANCED", slug, body(slug));
          })
          .collect(Collectors.joining("\n"));
    };

    var harvested = new ContentHarvester(alwaysAdvanced, 2).harvest("NODE_TYPESCRIPT", "system");

    // ADVANCED 셀(8개)만 채워져야 한다. 가드가 없으면 INTRO·INTERMEDIATE 셀도
    // ADVANCED 행을 받아 30건이 된다.
    assertThat(harvested).hasSize(8);
    assertThat(harvested).allSatisfy(l -> assertThat(field(l, "level")).isEqualTo("ADVANCED"));
  }

  // 실측: 초안 34건 중 1건이 raw HTML 을 담아 게이트에 걸렸다.
  @Test
  void dropsRowsWithRawHtml() throws Exception {
    ContentDraftClient html = (track, level, count, prompt) ->
        line(track, level, "node-html-" + level.toLowerCase(),
            "설명<br/>\\n\\n```ts\\nconst a = 1;\\n```\\n");

    assertThat(new ContentHarvester(html, 2).harvest("NODE_TYPESCRIPT", "system")).isEmpty();
  }

  @Test
  void dropsDuplicateSlugsAndNonKebabSlugs() throws Exception {
    ContentDraftClient sameSlug = (track, level, count, prompt) ->
        line(track, level, "Node_Bad_Slug", body("bad")) + "\n"
            + line(track, level, "node-shared-slug", body("shared"));

    var harvested = new ContentHarvester(sameSlug, 2).harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).hasSize(1);
    assertThat(field(harvested.get(0), "slug")).isEqualTo("node-shared-slug");
  }

  // 트랙당 코드블록이 10개 이상이어야 한다. 닫히지 않은 펜스는 게이트가 막는다.
  @Test
  void dropsRowsWithUnclosedCodeFence() throws Exception {
    ContentDraftClient unclosed = (track, level, count, prompt) ->
        line(track, level, "node-unclosed-" + level.toLowerCase(),
            "설명\\n\\n```ts\\nconst a = 1;\\n");

    assertThat(new ContentHarvester(unclosed, 2).harvest("NODE_TYPESCRIPT", "system")).isEmpty();
  }

  // 실측: 게이트가 'conceptTags must be kebab-case' 로 2건을 막았다
  // (node-worker_threads · AbortController). 승인본 180건에는 위반이 하나도 없다.
  @Test
  void dropsRowsWhoseConceptTagsAreNotKebabCase() throws Exception {
    ContentDraftClient badTags = (track, level, count, prompt) -> {
      var slug = "node-bad-tag-" + level.toLowerCase();
      return line(track, level, slug, body(slug))
          .replace("[\"node-basics\"]", "[\"AbortController\",\"node-worker_threads\"]");
    };

    assertThat(new ContentHarvester(badTags, 2).harvest("NODE_TYPESCRIPT", "system")).isEmpty();
  }

  // 승인본 180건은 title 도 전부 서로 다르다. slug 만 다르고 제목이 같으면 같은 주제를
  // 두 번 가르치며 쿼터 한 칸을 낭비한다(실측: event-loop 항목이 슬러그만 달리해 두 번 나왔다).
  @Test
  void dropsRowsRepeatingAnAlreadyHarvestedTitle() throws Exception {
    var calls = new AtomicInteger();
    ContentDraftClient sameTitle = (track, level, count, prompt) -> {
      var index = calls.incrementAndGet();
      return IntStream.range(0, count)
          .mapToObj(i -> {
            var slug = "node-slug-" + index + "-" + i;
            return line(track, level, slug, body(slug)).replace("제목 " + slug, "같은 제목");
          })
          .collect(Collectors.joining("\n"));
    };

    assertThat(new ContentHarvester(sameTitle, 2).harvest("NODE_TYPESCRIPT", "system")).hasSize(1);
  }

  @Test
  void stopsAfterTheAttemptBudget() throws Exception {
    var calls = new AtomicInteger();
    ContentDraftClient empty = (track, level, count, prompt) -> {
      calls.incrementAndGet();
      return "";
    };

    new ContentHarvester(empty, 2).harvest("NODE_TYPESCRIPT", "system");

    assertThat(calls.get()).isEqualTo(ContentHarvestPlan.cells().size() * 2);
  }

  private static String field(String jsonLine, String name) {
    try {
      return MAPPER.readTree(jsonLine).path(name).asText();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Test
  void planMatchesLevelQuotaAndUsesSmallBatches() {
    var cells = ContentHarvestPlan.cells();

    assertThat(cells.stream().mapToInt(ContentCell::count).sum()).isEqualTo(30);
    var byLevel = cells.stream().collect(Collectors.groupingBy(ContentCell::level,
        LinkedHashMap::new, Collectors.summingInt(ContentCell::count)));
    assertThat(byLevel).containsExactlyInAnyOrderEntriesOf(ContentQuota.LEVEL_TARGETS);
    assertThat(cells).allSatisfy(cell -> assertThat(cell.count()).isBetween(1, 6));
  }

  @Test
  void keepsTracksIntact() throws Exception {
    List<String> harvested = new ContentHarvester(distinctClient(new AtomicInteger()), 2)
        .harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).allSatisfy(l ->
        assertThat(field(l, "track")).isEqualTo("NODE_TYPESCRIPT"));
  }
}
