package ai.devpath.learning.contentgen.content;

import java.nio.file.Path;

public class ValidateContentsCommand {

  public static void main(String[] args) throws Exception {
    var input = Path.of(args.length > 0 ? args[0]
        : "tools/content-gen/generated/approved/contents.jsonl");
    var contents = new ContentJsonlReader().read(input);
    var report = new ContentValidator().validate(contents);
    report.warnings().forEach(warning -> System.err.println("WARN " + warning));
    if (!report.valid()) {
      report.errors().forEach(error -> System.err.println("ERROR " + error));
      System.exit(1);
    }
    System.out.println("Validated " + contents.size() + " approved contents");
  }
}
