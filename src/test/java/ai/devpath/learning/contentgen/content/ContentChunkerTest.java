package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ContentChunkerTest {

  private final ContentChunker chunker = new ContentChunker();

  @Test
  void splitsByH2AndAssignsSequentialIndexesAndHashes() {
    var content = content("## First\nRead this section.\n\n## Second\nPractice this section.");

    var chunks = chunker.chunksFor(content);

    assertThat(chunks).hasSize(2);
    assertThat(chunks).extracting(ContentChunk::chunkIndex).containsExactly(0, 1);
    assertThat(chunks.get(0).chunkText()).startsWith("## First");
    assertThat(chunks.get(0).chunkHash()).matches("[a-f0-9]{64}");
    assertThat(chunks.get(0).chunkHash())
        .isEqualTo(chunker.normalizedSha256Hex("## First\nRead this section."));
  }

  @Test
  void splitsLongSectionsIntoBoundedChunksWithOverlap() {
    var repeated = "0123456789".repeat(320);
    var content = content("## Long\n" + repeated);

    var chunks = chunker.chunksFor(content);

    assertThat(chunks).hasSizeGreaterThan(1);
    assertThat(chunks).allSatisfy(chunk ->
        assertThat(chunk.chunkText().length()).isLessThanOrEqualTo(1_200));
    assertThat(chunks).extracting(ContentChunk::chunkIndex)
        .containsExactlyElementsOf(java.util.stream.IntStream.range(0, chunks.size()).boxed().toList());
    var first = chunks.get(0).chunkText();
    var second = chunks.get(1).chunkText();
    assertThat(second).startsWith(first.substring(first.length() - 120));
  }

  private static ApprovedContent content(String markdown) {
    return new ApprovedContent(
        "backend-spring-test",
        "Backend Spring Test",
        "BACKEND_SPRING",
        "INTRO",
        markdown,
        15,
        0.2,
        "UNDERSTAND",
        List.of("backend-spring-test"),
        "PUBLISHED");
  }
}
