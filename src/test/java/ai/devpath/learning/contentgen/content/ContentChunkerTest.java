package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
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

  @Test
  void neverSplitsAstralSurrogatePairsAcrossChunkBoundaries() {
    // 프로덕션 장애 재현: 청크 경계(산술로 계산된 substring 인덱스)가 UTF-16 서로게이트
    // 쌍(astral 문자, 예: 이모지) 중간에 떨어지면 결과 String이 짝 없는 서로게이트를 갖게 되고,
    // 이를 UTF-8로 쓰는 순간(BufferedWriter) MalformedInputException이 던져진다
    // (8,955번째 청크에서 실제로 발생). section 길이·이모지 삽입 위치를 전수 스윕해
    // chunkCount=2와 chunkCount=3 두 경우 모두에서 경계가 반드시 한 번은 쌍 한가운데를
    // 지나가도록 강제한다.
    String emoji = "🐤"; // 🐤 U+1F424 BABY CHICK (study-documents 카테고리 접두어와 동일 astral 문자)

    // chunkCount=2가 되는 길이(기존 splitsLongSectionsIntoBoundedChunksWithOverlap과 동일 크기대)
    assertNeverSplitsSurrogatePairForEveryPosition(emoji, 1250);
    // chunkCount=3이 되는 길이(기존 ContentChunkerGeneralizationTest.longSectionIsSplitWithOverlap과 동일 크기대)
    assertNeverSplitsSurrogatePairForEveryPosition(emoji, 3000);
  }

  private void assertNeverSplitsSurrogatePairForEveryPosition(String emoji, int baseLength) {
    String base = "0123456789".repeat((baseLength / 10) + 1).substring(0, baseLength);
    for (int pos = 0; pos <= baseLength; pos++) {
      String withEmoji = base.substring(0, pos) + emoji + base.substring(pos);
      String md = "## Long\n" + withEmoji;

      List<ContentChunk> chunks = chunker.chunksFor("k", md);

      boolean emojiFoundIntact = false;
      for (ContentChunk chunk : chunks) {
        assertEncodableAsUtf8(chunk.chunkText(), baseLength, pos);
        if (chunk.chunkText().contains(emoji)) {
          emojiFoundIntact = true;
        }
      }
      assertThat(emojiFoundIntact)
          .as("astral 문자가 최소 한 청크에는 원형 그대로 남아 있어야 한다 (len=%d, pos=%d)", baseLength, pos)
          .isTrue();
    }
  }

  private static void assertEncodableAsUtf8(String text, int baseLength, int pos) {
    var encoder = StandardCharsets.UTF_8.newEncoder()
        .onMalformedInput(CodingErrorAction.REPORT)
        .onUnmappableCharacter(CodingErrorAction.REPORT);
    try {
      encoder.encode(CharBuffer.wrap(text));
    } catch (CharacterCodingException e) {
      throw new AssertionError(
          "chunk에 짝 없는 서로게이트가 있어 UTF-8 인코딩이 불가능하다 (len=%d, pos=%d): %s"
              .formatted(baseLength, pos, e.getMessage()),
          e);
    }
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
