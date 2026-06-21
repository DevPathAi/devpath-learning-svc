package ai.devpath.learning.contentgen.question;

import java.nio.file.Path;

public class ValidateQuestionsCommand {

  public static void main(String[] args) throws Exception {
    var input = Path.of(args.length > 0 ? args[0]
        : "tools/content-gen/generated/approved/questions.jsonl");
    var questions = new QuestionJsonlReader().read(input);
    var report = new QuestionValidator().validate(questions);
    report.warnings().forEach(warning -> System.err.println("WARN " + warning));
    if (!report.valid()) {
      report.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }
    System.out.println("Validated " + questions.size() + " approved questions");
  }
}
