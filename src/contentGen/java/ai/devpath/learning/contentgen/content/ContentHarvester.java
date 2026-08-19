package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 레벨별로 조금씩 요청해 트랙 하나의 콘텐츠 쿼터를 채운다.
 *
 * <p>받은 줄을 그대로 쌓지 않고 게이트가 나중에 막을 것들을 **받는 자리에서** 거른다 —
 * 요청한 레벨인가 · raw HTML 이 없는가 · 코드 펜스가 닫혔는가 · slug 가 kebab-case 이고
 * 처음 보는 것인가. 채우지 못하면 모자란 채로 끝낸다.
 */
public final class ContentHarvester {

  private static final Pattern KEBAB = Pattern.compile("[a-z0-9]+(?:-[a-z0-9]+)*");
  private static final Pattern RAW_HTML = Pattern.compile("<[a-zA-Z/][^>]*>");

  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final ContentDraftClient client;
  private final int maxAttemptsPerCell;

  public ContentHarvester(ContentDraftClient client, int maxAttemptsPerCell) {
    this.client = client;
    this.maxAttemptsPerCell = maxAttemptsPerCell;
  }

  public List<String> harvest(String track, String basePrompt) throws Exception {
    var accepted = new ArrayList<String>();
    var seenSlugs = new LinkedHashSet<String>();
    // 승인본 180건은 slug 뿐 아니라 title 도 전부 서로 다르다. 제목이 겹치면 같은 주제를
    // 두 번 가르치며 쿼터 한 칸을 낭비한다.
    var seenTitles = new LinkedHashSet<String>();

    for (ContentCell cell : ContentHarvestPlan.cells()) {
      var filled = 0;
      for (int attempt = 0; attempt < maxAttemptsPerCell && filled < cell.count(); attempt++) {
        var wanted = cell.count() - filled;
        var raw = client.generate(track, cell.level(), wanted, basePrompt);
        for (String line : raw.split("\\R")) {
          if (filled >= cell.count()) {
            break;
          }
          var row = parse(line);
          if (row == null || !isUsable(row, track, cell)) {
            continue;
          }
          if (!seenSlugs.add(row.path("slug").asText())
              || !seenTitles.add(row.path("title").asText().strip())) {
            continue;
          }
          accepted.add(line.strip());
          filled++;
        }
      }
    }
    return List.copyOf(accepted);
  }

  private boolean isUsable(JsonNode row, String track, ContentCell cell) {
    if (!track.equals(row.path("track").asText(null))
        || !cell.level().equals(row.path("level").asText(null))
        || !"PUBLISHED".equals(row.path("status").asText(null))) {
      return false;
    }
    var slug = row.path("slug").asText("");
    if (!KEBAB.matcher(slug).matches() || row.path("title").asText("").isBlank()) {
      return false;
    }
    var tags = row.path("conceptTags");
    if (!tags.isArray() || tags.isEmpty()) {
      return false;
    }
    // 게이트가 'conceptTags must be kebab-case' 로 막는다(실측: AbortController·node-worker_threads).
    for (JsonNode tag : tags) {
      if (!tag.isTextual() || !KEBAB.matcher(tag.asText()).matches()) {
        return false;
      }
    }
    var markdown = row.path("contentMd").asText("");
    if (markdown.isBlank() || RAW_HTML.matcher(markdown).find()) {
      return false;
    }
    // 펜스가 홀수면 닫히지 않은 코드블록이다.
    return countFences(markdown) % 2 == 0;
  }

  private static int countFences(String markdown) {
    var fences = 0;
    var index = markdown.indexOf("```");
    while (index >= 0) {
      fences++;
      index = markdown.indexOf("```", index + 3);
    }
    return fences;
  }

  private JsonNode parse(String line) {
    var trimmed = line.strip();
    if (!trimmed.startsWith("{")) {
      return null;
    }
    try {
      return mapper.readTree(trimmed);
    } catch (Exception ignored) {
      return null;
    }
  }
}
