package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmbeddingValidatorTest {

  private static final String HASH_A = "a".repeat(64);
  private static final String HASH_B = "b".repeat(64);

  private final EmbeddingValidator validator = new EmbeddingValidator();

  @Test
  void validEmbeddingPasses() {
    var report = validator.validate(List.of(record("slug-a", 0, HASH_A, 768, "ACTIVE")));

    assertThat(report.errors()).isEmpty();
  }

  @Test
  void rejectsEmbeddingLengthOtherThanSevenHundredSixtyEight() {
    var shortRecord = record("slug-a", 0, HASH_A, 767, "ACTIVE");
    var longRecord = record("slug-b", 0, HASH_B, 769, "ACTIVE");

    assertThat(validator.validate(List.of(shortRecord)).errors()).anySatisfy(error ->
        assertThat(error).contains("768"));
    assertThat(validator.validate(List.of(longRecord)).errors()).anySatisfy(error ->
        assertThat(error).contains("768"));
  }

  @Test
  void rejectsDuplicateContentAndChunkHash() {
    var report = validator.validate(List.of(
        record("same-slug", 0, HASH_A, 768, "ACTIVE"),
        record("same-slug", 1, HASH_A, 768, "ACTIVE")));

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate content chunk hash"));
  }

  @Test
  void rejectsNonActiveStatus() {
    var report = validator.validate(List.of(record("slug-a", 0, HASH_A, 768, "INACTIVE")));

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("ACTIVE"));
  }

  @Test
  void fakeEmbeddingClientBoundaryProducesDeterministicFixtureVector() throws Exception {
    EmbeddingClient client = text -> Collections.nCopies(EmbeddingValidator.DIMENSIONS, 0.125);

    assertThat(client.embed("chunk text")).hasSize(768).containsOnly(0.125);
  }

  private static EmbeddingRecord record(
      String slug, int index, String hash, int dimensions, String status) {
    return new EmbeddingRecord(
        slug,
        index,
        "chunk " + index,
        Collections.nCopies(dimensions, 0.25),
        hash,
        status);
  }
}
