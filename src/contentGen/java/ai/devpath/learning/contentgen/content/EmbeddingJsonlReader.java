package ai.devpath.learning.contentgen.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingJsonlReader {

  private final ObjectMapper mapper = JsonMapper.builder().build();

  public List<EmbeddingRecord> read(Path path) throws IOException {
    var records = new ArrayList<EmbeddingRecord>();
    try (var lines = Files.lines(path)) {
      var iterator = lines.iterator();
      int lineNo = 0;
      while (iterator.hasNext()) {
        lineNo++;
        var line = iterator.next();
        if (line.isBlank()) continue;
        try {
          records.add(mapper.readValue(line, EmbeddingRecord.class));
        } catch (IOException e) {
          throw new IOException("Invalid JSONL at " + path + ":" + lineNo, e);
        }
      }
    }
    return records;
  }
}
