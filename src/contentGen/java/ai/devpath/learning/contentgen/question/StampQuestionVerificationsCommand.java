package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 리뷰 루프(reviewQuestionsLocal → 지적 반영)를 **마친 뒤** 트랙의 검증 장부를 갱신한다.
 * 자동 실행 금지 — 스탬프는 「사실 검증을 끝냈다」는 사람의 선언이고, git diff 로 감사된다.
 * 장부 없이 문항을 추가·수정하면 validateQuestions / ApprovedQuestionsGateTest 가 실패한다.
 */
public class StampQuestionVerificationsCommand {

  private static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static void main(String[] args) throws Exception {
    if (args.length < 4 || args[2].isBlank() || args[3].isBlank()) {
      System.err.println("usage: <questions.jsonl> <verifications.jsonl> <track|ALL> <reviewer>");
      System.exit(2);
    }
    var questionsPath = Path.of(args[0]);
    var manifestPath = Path.of(args[1]);
    var track = args[2];
    var reviewer = args[3];
    var reviewedAt = args.length > 4 ? args[4] : java.time.LocalDate.now().toString();

    var questions = new QuestionJsonlReader().read(questionsPath);
    var target = "ALL".equals(track)
        ? questions
        : questions.stream().filter(q -> track.equals(q.track())).toList();
    if (target.isEmpty()) {
      System.err.println("no questions for track " + track);
      System.exit(2);
    }

    var kept = new ArrayList<QuestionVerification>();
    if (Files.exists(manifestPath)) {
      for (QuestionVerification v : new QuestionVerificationJsonlReader().read(manifestPath)) {
        if (!"ALL".equals(track) && !track.equals(v.track())) {
          kept.add(v);
        }
      }
    }
    for (ApprovedQuestion q : target) {
      kept.add(new QuestionVerification(q.track(), QuestionVerification.fingerprintOf(q),
          QuestionVerification.VERDICT_PASS, QuestionVerification.REQUIRED_AXES,
          reviewer, reviewedAt));
    }
    kept.sort(Comparator.comparing(QuestionVerification::track)
        .thenComparing(QuestionVerification::fingerprint));

    var sb = new StringBuilder();
    for (QuestionVerification v : kept) {
      sb.append(MAPPER.writeValueAsString(v)).append('\n');
    }
    Files.createDirectories(manifestPath.toAbsolutePath().getParent());
    Files.writeString(manifestPath, sb.toString());
    System.out.println("Stamped " + target.size() + " verifications for " + track
        + " by " + reviewer + " -> " + manifestPath + " (" + kept.size() + " total)");
  }
}
