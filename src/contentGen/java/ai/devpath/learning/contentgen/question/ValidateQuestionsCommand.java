package ai.devpath.learning.contentgen.question;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ValidateQuestionsCommand {

  public static void main(String[] args) throws Exception {
    var input = Path.of(args.length > 0 ? args[0]
        : "tools/content-gen/generated/approved/questions.jsonl");
    var manifest = Path.of(args.length > 1 ? args[1]
        : "tools/content-gen/generated/approved/question_verifications.jsonl");
    var questions = new QuestionJsonlReader().read(input);
    // 장부 파일 부재는 「빈 장부」로 취급한다 — 모든 문항이 missing 으로 실패해
    // 사실 검증 축을 조용히 건너뛸 수 없다.
    List<QuestionVerification> verifications = Files.exists(manifest)
        ? new QuestionVerificationJsonlReader().read(manifest)
        : List.of();
    var report = new QuestionValidator().validate(questions, verifications);
    report.warnings().forEach(warning -> System.err.println("WARN " + warning));
    if (!report.valid()) {
      report.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }
    System.out.println("Validated " + questions.size() + " approved questions ("
        + verifications.size() + " verifications)");
  }
}
