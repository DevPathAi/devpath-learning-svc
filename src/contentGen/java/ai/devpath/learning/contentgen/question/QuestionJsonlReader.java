package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QuestionJsonlReader {

  private final ObjectMapper mapper = JsonMapper.builder().build();

  public List<ApprovedQuestion> read(Path path) throws IOException {
    var questions = new ArrayList<ApprovedQuestion>();
    try (var lines = Files.lines(path)) {
      var iterator = lines.iterator();
      int lineNo = 0;
      while (iterator.hasNext()) {
        lineNo++;
        var line = iterator.next();
        if (line.isBlank()) continue;
        try {
          questions.add(mapper.readValue(line, ApprovedQuestion.class));
        } catch (IOException e) {
          throw new IOException("Invalid JSONL at " + path + ":" + lineNo, e);
        }
      }
    }
    return questions;
  }
}
