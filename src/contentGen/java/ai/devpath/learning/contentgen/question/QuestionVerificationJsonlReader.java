package ai.devpath.learning.contentgen.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QuestionVerificationJsonlReader {

  private final ObjectMapper mapper = JsonMapper.builder().build();

  public List<QuestionVerification> read(Path path) throws IOException {
    var verifications = new ArrayList<QuestionVerification>();
    try (var lines = Files.lines(path)) {
      var iterator = lines.iterator();
      int lineNo = 0;
      while (iterator.hasNext()) {
        lineNo++;
        var line = iterator.next();
        if (line.isBlank()) continue;
        try {
          verifications.add(mapper.readValue(line, QuestionVerification.class));
        } catch (IOException e) {
          throw new IOException("Invalid verification JSONL at " + path + ":" + lineNo, e);
        }
      }
    }
    return verifications;
  }
}
