package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 셀 단위로 조금씩 요청해 트랙 하나의 쿼터를 채운다.
 *
 * <p>모델은 되풀이한다. 그래서 받은 줄을 그대로 쌓지 않고 (1) 파싱되는가 (2) 요청한 트랙인가
 * (3) 요청한 셀의 형태인가 (4) 이미 받은 content 가 아닌가 를 모두 통과한 것만 남긴다.
 * 채우지 못하면 시도 예산까지만 쓰고 **모자란 채로** 끝낸다 — 중복으로 숫자를 맞추지 않는다.
 */
public final class QuestionHarvester {

  private final ObjectMapper mapper = JsonMapper.builder().build();
  private final QuestionDraftClient client;
  private final int maxAttemptsPerCell;

  public QuestionHarvester(QuestionDraftClient client, int maxAttemptsPerCell) {
    this.client = client;
    this.maxAttemptsPerCell = maxAttemptsPerCell;
  }

  /** 승인본 600건은 전부 선택지가 4개 이상이다(4개 599 · 5개 1). */
  private static final int MIN_OPTIONS = 4;

  public List<String> harvest(String track, String basePrompt) throws Exception {
    var accepted = new ArrayList<String>();
    var seenContents = new LinkedHashSet<String>();
    var seenOptionSets = new LinkedHashSet<String>();

    for (QuestionCell cell : QuestionHarvestPlan.cells()) {
      var filled = 0;
      for (int attempt = 0; attempt < maxAttemptsPerCell && filled < cell.count(); attempt++) {
        var wanted = cell.count() - filled;
        var raw = client.generate(track, wanted, QuestionHarvestPrompt.build(basePrompt, cell));
        for (String line : raw.split("\\R")) {
          if (filled >= cell.count()) {
            break;
          }
          var row = parse(line);
          if (row == null || !matches(row, track, cell) || !isAnswerable(row)) {
            continue;
          }
          var content = row.path("content").asText("").strip();
          if (content.isEmpty() || !seenContents.add(content)) {
            continue;
          }
          if (!seenOptionSets.add(row.path("options").toString())) {
            continue;
          }
          accepted.add(line.strip());
          filled++;
        }
      }
    }
    return List.copyOf(accepted);
  }

  /**
   * 정답을 고를 수 있는 문항인가. 실측으로 초안 100건 중 선택지가 통째로 빠진 것이 22건,
   * answerKey 가 없는 것이 9건이었다 — 둘 다 그대로 두면 진단에 쓸 수 없다.
   */
  private boolean isAnswerable(JsonNode row) {
    var options = row.path("options");
    if (!options.isArray() || options.size() < MIN_OPTIONS) {
      return false;
    }
    // 개수만 세면 배열로 감싼 보기(["a"] 네 개)도 4개로 통과한다. 실제로 그런 행이 나왔다.
    for (JsonNode option : options) {
      if (!option.isTextual() || option.asText().isBlank()) {
        return false;
      }
    }
    var correct = row.path("answerKey").path("correct");
    return correct.isInt() && correct.asInt() >= 0 && correct.asInt() < options.size();
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

  private boolean matches(JsonNode row, String track, QuestionCell cell) {
    return track.equals(row.path("track").asText(null))
        && cell.questionType().equals(row.path("questionType").asText(null))
        && cell.bloomLevel().equals(row.path("bloomLevel").asText(null))
        && cell.difficultyBand().equals(bandOf(row.path("difficulty")));
  }

  private static String bandOf(JsonNode difficulty) {
    if (!difficulty.isNumber()) {
      return null;
    }
    var value = difficulty.asDouble();
    if (value >= 0.1 - 1e-9 && value <= 0.2 + 1e-9) {
      return "0.1-0.2";
    }
    if (value >= 0.3 - 1e-9 && value <= 0.4 + 1e-9) {
      return "0.3-0.4";
    }
    if (value >= 0.5 - 1e-9 && value <= 0.6 + 1e-9) {
      return "0.5-0.6";
    }
    if (value >= 0.7 - 1e-9 && value <= 0.8 + 1e-9) {
      return "0.7-0.8";
    }
    if (value >= 0.9 - 1e-9 && value <= 1.0 + 1e-9) {
      return "0.9";
    }
    return null;
  }
}
