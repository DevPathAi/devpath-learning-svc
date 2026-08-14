package ai.devpath.learning.contentgen.question;

import java.util.ArrayList;
import java.util.List;

/** Claude 검수용 프롬프트와 배치 분할. 네트워크를 모른다 — 순수 함수라 테스트가 쉽다. */
public final class QuestionReviewPrompt {

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
    var batches = new ArrayList<List<ApprovedQuestion>>();
    var current = new ArrayList<ApprovedQuestion>();
    int size = 0;
    for (ApprovedQuestion q : questions) {
      int cost = weight(q);
      if (!current.isEmpty() && size + cost > budget) {
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

  private static int weight(ApprovedQuestion q) {
    int cost = q.content() == null ? 0 : q.content().length();
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
