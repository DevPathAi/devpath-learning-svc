package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.ArrayList;
import java.util.List;

/** Claude 검수용 프롬프트와 배치 분할. 네트워크를 모른다 — 순수 함수라 테스트가 쉽다. */
public final class QuestionReviewPrompt {

  /**
   * 문항 1건을 JSON 스캐폴딩({"index": N, "content": ..., "options": ..., "correct": N}\n)으로
   * 감쌀 때 content·options 원문 글자 수 외에 추가로 드는 글자 수. 실측(BACKEND_SPRING 100문항,
   * options 4개 기준): 문항당 평균 66.98자(스캐폴딩 리터럴 51자 + index/correct 숫자 + quote()
   * 이스케이프 + options 의 List.toString() 대괄호·콤마 포맷 8~14자). 이스케이프가 많은 문항을
   * 대비해 여유를 둔다.
   */
  private static final int PER_QUESTION_OVERHEAD = 90;

  /**
   * 배치 예산에서 문항 앞에 미리 차감할 프리앰블(안내문+트랙 헤더) 크기. batch() 는 트랙명을
   * 받지 않아 정확한 값을 실측할 수 없으므로 상수로 둔다. 실측(트랙명 14자, 예: BACKEND_SPRING):
   * build(track, List.of()).length() == 310. 더 긴 트랙명·여유를 대비해 40자를 더한다.
   */
  private static final int PREAMBLE_BUDGET = 350;

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private QuestionReviewPrompt() {}

  public static String build(String track, List<ApprovedQuestion> batch) {
    var sb = new StringBuilder();
    sb.append("당신은 개발자 진단 문항을 검수한다. 트랙: ").append(track).append("\n\n");
    sb.append("각 문항에 대해 아래 네 가지를 본다.\n");
    sb.append("1. 사실오류 — 기술적으로 틀린 서술\n");
    sb.append("2. 정답키 오류 — answerKey.correct 가 가리키는 보기가 정답이 아님\n");
    sb.append("3. 정답을 흘리는 선택지 — 길이·구체성만으로 정답이 보이는 문항\n");
    sb.append("4. 어색한 한국어 — 번역투·비문\n\n");
    sb.append("문제가 없는 문항은 결과에 넣지 않는다. ");
    sb.append("결과는 JSON 배열만 출력한다(설명 금지): ");
    sb.append("[{\"index\": 0, \"axis\": \"정답키\", \"detail\": \"...\", \"suggestion\": \"...\"}]\n\n");
    for (int i = 0; i < batch.size(); i++) {
      var q = batch.get(i);
      sb.append("{\"index\": ").append(i)
          .append(", \"content\": ").append(quote(q.content()))
          .append(", \"options\": ").append(q.options())
          .append(", \"correct\": ").append(q.answerKey().correct())
          .append("}\n");
    }
    return sb.toString();
  }

  public static List<List<ApprovedQuestion>> batch(List<ApprovedQuestion> questions, int budget) {
    int effectiveBudget = Math.max(budget - PREAMBLE_BUDGET, 0);
    var batches = new ArrayList<List<ApprovedQuestion>>();
    var current = new ArrayList<ApprovedQuestion>();
    int size = 0;
    for (ApprovedQuestion q : questions) {
      int cost = weight(q);
      if (!current.isEmpty() && size + cost > effectiveBudget) {
        batches.add(List.copyOf(current));
        current = new ArrayList<>();
        size = 0;
      }
      current.add(q);
      size += cost;
    }
    if (!current.isEmpty()) {
      batches.add(List.copyOf(current));
    }
    return List.copyOf(batches);
  }

  /**
   * 배치별 Claude 원본 응답을 하나의 유효한 JSON 배열로 합친다. 순서를 보존하고 각 원소에
   * batch 인덱스를 붙인다. Jackson 으로 직접 노드를 구성해 트레일링 콤마 등 수기 문자열 접합이
   * 낼 수 있는 문법 오류를 원천적으로 없앤다.
   */
  public static String mergeReports(List<String> rawBodies) {
    ArrayNode array = MAPPER.createArrayNode();
    for (int i = 0; i < rawBodies.size(); i++) {
      var entry = array.addObject();
      entry.put("batch", i);
      entry.put("raw", rawBodies.get(i));
    }
    try {
      return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(array) + "\n";
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("검수 리포트 병합에 실패했다", e);
    }
  }

  private static int weight(ApprovedQuestion q) {
    int cost = PER_QUESTION_OVERHEAD;
    cost += q.content() == null ? 0 : q.content().length();
    if (q.options() != null) {
      for (String option : q.options()) {
        cost += option == null ? 0 : option.length();
      }
    }
    return cost;
  }

  private static String quote(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
  }
}
