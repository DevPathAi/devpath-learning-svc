package ai.devpath.learning.contentgen.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class EmbeddingValidator {

  public static final int DIMENSIONS = 768;
  private static final Pattern SHA256_HEX = Pattern.compile("[a-f0-9]{64}");

  public EmbeddingValidationReport validate(List<EmbeddingRecord> records) {
    var errors = new ArrayList<String>();
    if (records == null) {
      return new EmbeddingValidationReport(List.of("embeddings must not be null"));
    }

    var seenContentChunks = new HashSet<String>();
    for (int i = 0; i < records.size(); i++) {
      validateRecord(i + 1, records.get(i), seenContentChunks, errors);
    }
    return new EmbeddingValidationReport(List.copyOf(errors));
  }

  private void validateRecord(int line, EmbeddingRecord record, HashSet<String> seenContentChunks,
      List<String> errors) {
    if (record == null) {
      errors.add("line " + line + ": embedding record must not be null");
      return;
    }
    if (blank(record.slug())) {
      errors.add("line " + line + ": slug is required");
    }
    if (record.chunkIndex() == null || record.chunkIndex() < 0) {
      errors.add("line " + line + ": chunkIndex must be zero or greater");
    }
    if (blank(record.chunkText())) {
      errors.add("line " + line + ": chunkText is required");
    }
    if (blank(record.chunkHash()) || !SHA256_HEX.matcher(record.chunkHash()).matches()) {
      errors.add("line " + line + ": chunkHash must be sha256 hex");
    } else if (!blank(record.slug())) {
      var key = record.slug() + "|" + record.chunkHash();
      if (!seenContentChunks.add(key)) {
        errors.add("line " + line + ": duplicate content chunk hash for " + record.slug());
      }
    }
    if (!"ACTIVE".equals(record.status())) {
      errors.add("line " + line + ": status must be ACTIVE");
    }
    if (record.embedding() == null || record.embedding().size() != DIMENSIONS) {
      errors.add("line " + line + ": embedding must contain 768 dimensions");
    } else {
      for (Double value : record.embedding()) {
        if (value == null || value.isNaN() || value.isInfinite()) {
          errors.add("line " + line + ": embedding contains invalid value");
          break;
        }
      }
    }
  }

  private boolean blank(String value) {
    return value == null || value.isBlank();
  }
}
