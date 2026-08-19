package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * 실측한 실패를 테스트로 고정한다. 로컬 Ollama 가 같은 문항을 끝없이 되풀이해도(NODE_TYPESCRIPT
 * 355줄 중 고유 16건, PYTHON_BACKEND 1차 30줄 중 고유 6건) 하버스터는 중복을 승인본에 넣지
 * 않아야 하고, 채우지 못한 채로 무한히 매달려서도 안 된다.
 */
class QuestionHarvesterTest {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  private static String line(String track, String type, String bloom, double difficulty,
      String content) {
    // 승인본은 600건 전부 선택지 벌이 서로 다르다. 픽스처도 content 마다 다른 벌을 준다.
    var options = "[\"" + content + "-a\",\"" + content + "-b\",\"" + content + "-c\",\""
        + content + "-d\"]";
    return line(track, type, bloom, difficulty, content, options, "{\"correct\":0}");
  }

  private static String line(String track, String type, String bloom, double difficulty,
      String content, String options, String answerKey) {
    return "{\"track\":\"" + track + "\",\"questionType\":\"" + type + "\",\"content\":\"" + content
        + "\",\"options\":" + options + ",\"answerKey\":" + answerKey + ",\"bloomLevel\":\""
        + bloom + "\",\"difficulty\":" + difficulty + ",\"conceptTags\":[\"t\"],"
        + "\"explanation\":\"설명\"}";
  }

  private static double difficultyFor(String band) {
    return switch (band) {
      case "0.1-0.2" -> 0.1;
      case "0.3-0.4" -> 0.3;
      case "0.5-0.6" -> 0.5;
      case "0.7-0.8" -> 0.7;
      default -> 0.9;
    };
  }

  /** 셀이 요구하는 형태로, 매번 새로운 content 를 내주는 정상 모델. */
  private static QuestionDraftClient distinctClient(AtomicInteger calls) {
    return (track, count, prompt) -> {
      calls.incrementAndGet();
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      return java.util.stream.IntStream.range(0, count)
          .mapToObj(index -> line(track, cell.questionType(), cell.bloomLevel(),
              difficultyFor(cell.difficultyBand()), "문항-" + calls.get() + "-" + index))
          .collect(Collectors.joining("\n"));
    };
  }

  @Test
  void fillsEveryCellWithDistinctQuestions() throws Exception {
    var calls = new AtomicInteger();
    var harvested = new QuestionHarvester(distinctClient(calls), 3)
        .harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).hasSize(100);
    assertThat(harvested.stream().map(QuestionHarvesterTest::content).distinct().toList())
        .hasSize(100);
  }

  @Test
  void harvestedQuestionsMatchEveryQuotaMarginal() throws Exception {
    var harvested = new QuestionHarvester(distinctClient(new AtomicInteger()), 3)
        .harvest("NODE_TYPESCRIPT", "system");

    var types = harvested.stream().map(l -> field(l, "questionType"))
        .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
    assertThat(types.get("MCQ")).isEqualTo(70L);
    assertThat(types.get("CODE_READING")).isEqualTo(30L);
  }

  // ★이 테스트가 이번 결함을 직접 겨냥한다★ 같은 문항만 되풀이하는 모델을 주면,
  // 하버스터는 중복을 채워 넣는 대신 모자란 채로 끝내야 한다.
  @Test
  void loopingModelDoesNotProduceDuplicateQuestions() throws Exception {
    QuestionDraftClient looping = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      return line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), "언제나 같은 문항");
    };

    var harvested = new QuestionHarvester(looping, 3).harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested.stream().map(QuestionHarvesterTest::content).distinct().toList())
        .hasSize(1);
    assertThat(harvested).hasSize(1);
  }

  @Test
  void dropsRowsFromAnotherTrackAndUnparsableLines() throws Exception {
    QuestionDraftClient noisy = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      var rows = new ArrayList<String>();
      rows.add("```jsonl");
      rows.add(line("DEVOPS", cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), "다른 트랙"));
      rows.add("{\"track\":\"NODE_TYPESCRIPT\",\"content\":\"잘린 줄");
      rows.add(line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), "정상-" + cell.bloomLevel() + cell.difficultyBand()
              + cell.questionType()));
      return String.join("\n", rows);
    };

    var harvested = new QuestionHarvester(noisy, 1).harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).isNotEmpty();
    assertThat(harvested).allSatisfy(l -> assertThat(field(l, "track")).isEqualTo("NODE_TYPESCRIPT"));
    assertThat(harvested).allSatisfy(l -> assertThat(content(l)).doesNotContain("잘린"));
  }

  // 모델이 셀과 다른 형태를 내면 쿼터가 조용히 어긋난다. 형태가 맞는 것만 받아야 한다.
  @Test
  void dropsRowsThatDoNotMatchTheRequestedCell() throws Exception {
    QuestionDraftClient wrongShape = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      var wrongBloom = cell.bloomLevel().equals("APPLY") ? "REMEMBER" : "APPLY";
      return line(track, cell.questionType(), wrongBloom,
          difficultyFor(cell.difficultyBand()), "형태가 어긋난 문항-" + cell.bloomLevel());
    };

    var harvested = new QuestionHarvester(wrongShape, 2).harvest("NODE_TYPESCRIPT", "system");

    assertThat(harvested).isEmpty();
  }

  // 실측: CODE_READING 을 요구하면 모델이 선택지를 통째로 빠뜨린다(초안 100건 중 옵션 0개가
  // 22건). 게이트는 "options must contain at least two choices" 로 뒤늦게 막는다.
  @Test
  void dropsRowsWithoutEnoughOptions() throws Exception {
    QuestionDraftClient missingOptions = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      return line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), "선택지 없는 문항-" + cell.bloomLevel(),
          "[]", "{\"correct\":0}");
    };

    assertThat(new QuestionHarvester(missingOptions, 2).harvest("NODE_TYPESCRIPT", "system"))
        .isEmpty();
  }

  // 실측: 초안 100건 중 9건이 answerKey 를 빠뜨렸다. 정답 없는 문항은 진단에 쓸 수 없다.
  @Test
  void dropsRowsWhoseAnswerKeyIsMissingOrOutOfRange() throws Exception {
    QuestionDraftClient noKey = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      var base = "키 없는 문항-" + cell.bloomLevel() + cell.difficultyBand() + cell.questionType();
      var options = "[\"" + base + "-a\",\"" + base + "-b\",\"" + base + "-c\",\"" + base + "-d\"]";
      return line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), base, options, "null")
          + "\n"
          + line(track, cell.questionType(), cell.bloomLevel(),
              difficultyFor(cell.difficultyBand()), base + "-범위밖", options, "{\"correct\":9}");
    };

    assertThat(new QuestionHarvester(noKey, 2).harvest("NODE_TYPESCRIPT", "system")).isEmpty();
  }

  // 승인본 600건은 선택지 벌이 서로 겹치지 않는다. 같은 벌이 반복되면 다른 문항처럼 보여도
  // 사실상 같은 보기 묶음을 재사용한 것이다(운영에서 500문항이 한 벌을 공유한 사고가 있었다).
  @Test
  void dropsRepeatedOptionSets() throws Exception {
    var shared = "[\"공용-a\",\"공용-b\",\"공용-c\",\"공용-d\"]";
    QuestionDraftClient sharedOptions = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      return line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()),
          "서로 다른 질문-" + cell.bloomLevel() + cell.difficultyBand() + cell.questionType(),
          shared, "{\"correct\":0}");
    };

    assertThat(new QuestionHarvester(sharedOptions, 2).harvest("NODE_TYPESCRIPT", "system"))
        .hasSize(1);
  }

  // 실측: 보기를 문자열이 아니라 배열로 감싼 행이 나왔다
  // (["fetch(...)"], ["setImmediate(...)"] …). 개수만 세면 4개라 통과해 버린다.
  @Test
  void dropsRowsWhoseOptionsAreNotPlainText() throws Exception {
    QuestionDraftClient nested = (track, count, prompt) -> {
      var cell = QuestionHarvestPrompt.parseCell(prompt);
      var base = "중첩 보기-" + cell.bloomLevel() + cell.difficultyBand() + cell.questionType();
      var options = "[[\"" + base + "-a\"],[\"" + base + "-b\"],[\"" + base + "-c\"],[\""
          + base + "-d\"]]";
      return line(track, cell.questionType(), cell.bloomLevel(),
          difficultyFor(cell.difficultyBand()), base, options, "{\"correct\":0}");
    };

    assertThat(new QuestionHarvester(nested, 2).harvest("NODE_TYPESCRIPT", "system")).isEmpty();
  }

  @Test
  void stopsAfterTheAttemptBudgetInsteadOfLoopingForever() throws Exception {
    var calls = new AtomicInteger();
    QuestionDraftClient empty = (track, count, prompt) -> {
      calls.incrementAndGet();
      return "";
    };

    new QuestionHarvester(empty, 2).harvest("NODE_TYPESCRIPT", "system");

    assertThat(calls.get()).isEqualTo(QuestionHarvestPlan.cells().size() * 2);
  }

  private static String field(String jsonLine, String name) {
    try {
      return MAPPER.readTree(jsonLine).path(name).asText();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private static String content(String jsonLine) {
    return field(jsonLine, "content");
  }
}
