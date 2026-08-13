package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ContentChunkerGeneralizationTest {

  private final ContentChunker chunker = new ContentChunker();

  @Test
  void chunksForKeyAndMarkdownSplitsByH2() {
    String md = "# 제목\n\n인트로 문단.\n\n## 첫 섹션\n본문 1.\n\n## 둘째 섹션\n본문 2.\n";

    List<ContentChunk> chunks = chunker.chunksFor("AWS/step-01.md", md);

    assertThat(chunks).hasSize(3);
    assertThat(chunks).extracting(ContentChunk::slug)
        .containsOnly("AWS/step-01.md");
    assertThat(chunks).extracting(ContentChunk::chunkIndex)
        .containsExactly(0, 1, 2);
    assertThat(chunks.get(1).chunkText()).startsWith("## 첫 섹션");
  }

  @Test
  void approvedContentOverloadDelegatesToTheGeneralOne() {
    String md = "인트로.\n\n## 섹션 A\n내용 A.\n";
    // ApprovedContent는 10개 필드다:
    // (slug, title, track, level, contentMd, estimatedMinutes, difficulty, bloomLevel, conceptTags, status)
    var content = new ApprovedContent(
        "slug-1", "제목", "BACKEND_SPRING", "BEGINNER", md,
        10, 0.4, "APPLY", List.of("tag"), "PUBLISHED");

    List<ContentChunk> viaContent = chunker.chunksFor(content);
    List<ContentChunk> viaGeneral = chunker.chunksFor("slug-1", md);

    assertThat(viaContent).usingRecursiveComparison().isEqualTo(viaGeneral);
  }

  @Test
  void longSectionIsSplitWithOverlap() {
    String body = "가".repeat(3000);
    String md = "## 긴 섹션\n" + body;

    List<ContentChunk> chunks = chunker.chunksFor("k", md);

    assertThat(chunks.size()).isGreaterThan(1);
    assertThat(chunks).allSatisfy(c -> assertThat(c.chunkText().length()).isLessThanOrEqualTo(1200));
  }
}
