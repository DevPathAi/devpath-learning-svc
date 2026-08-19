package ai.devpath.learning.contentgen.question;

import java.util.regex.Pattern;

/** 셀 지시를 프롬프트에 싣고 다시 읽어낸다. 지시가 프롬프트 안에 있어야 모델이 셀을 지킨다. */
public final class QuestionHarvestPrompt {

  private static final Pattern CELL = Pattern.compile(
      "CELL questionType=(\\S+) bloomLevel=(\\S+) difficultyBand=(\\S+)");

  private QuestionHarvestPrompt() {}

  public static String build(String basePrompt, QuestionCell cell) {
    return basePrompt + "\n\n## 이번 요청의 좁은 목표\n\n"
        + "CELL questionType=" + cell.questionType()
        + " bloomLevel=" + cell.bloomLevel()
        + " difficultyBand=" + cell.difficultyBand() + "\n\n"
        + "이번에는 **정확히 이 형태의 문항만** 낸다. questionType 은 `" + cell.questionType()
        + "`, bloomLevel 은 `" + cell.bloomLevel() + "`, difficulty 는 " + cell.difficultyBand()
        + " 범위여야 한다.\n"
        + "앞서 낸 문항과 **같은 개념을 되풀이하지 않는다.** 서로 다른 개념을 겨냥하고,"
        + " 문장을 바꿔 쓴 같은 질문을 다시 내지 않는다.\n"
        + "**CODE_READING 도 객관식이다.** 어떤 questionType 이든 `options` 에 서로 다른 보기를"
        + " 정확히 4개 담고, `answerKey` 는 `{\"correct\": <0-3 정수>}` 로 반드시 채운다."
        + " 보기 묶음은 문항마다 새로 쓴다 — 앞 문항의 보기를 재사용하지 않는다.\n"
        + "요청한 개수만큼만 JSONL 로 출력하고 다른 말을 덧붙이지 않는다.";
  }

  /** 프롬프트에 실린 셀을 되읽는다. 테스트가 이 경로로 셀 준수를 확인한다. */
  public static QuestionCell parseCell(String prompt) {
    var matcher = CELL.matcher(prompt);
    if (!matcher.find()) {
      throw new IllegalArgumentException("프롬프트에 CELL 지시가 없다");
    }
    return new QuestionCell(matcher.group(1), matcher.group(2), matcher.group(3), 0);
  }
}
