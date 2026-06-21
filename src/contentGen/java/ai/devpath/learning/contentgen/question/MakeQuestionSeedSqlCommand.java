package ai.devpath.learning.contentgen.question;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MakeQuestionSeedSqlCommand {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: MakeQuestionSeedSqlCommand <questions.jsonl> <output.sql> [copy.sql...]");
      System.exit(2);
    }

    var input = Path.of(args[0]);
    var outputs = List.of(args).subList(1, args.length).stream().map(Path::of).toList();
    var questions = new QuestionJsonlReader().read(input);
    var report = new QuestionValidator().validate(questions);
    report.warnings().forEach(warning -> System.err.println("WARN " + warning));
    if (!report.valid()) {
      report.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }

    var sql = new QuestionSeedSqlWriter().toSql(questions);
    for (Path output : outputs) {
      if (output.getParent() != null) {
        Files.createDirectories(output.getParent());
      }
      Files.writeString(output, sql);
      System.out.println("Wrote " + output);
    }
  }
}
