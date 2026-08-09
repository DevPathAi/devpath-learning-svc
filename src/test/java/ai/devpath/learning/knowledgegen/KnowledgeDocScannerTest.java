package ai.devpath.learning.knowledgegen;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class KnowledgeDocScannerTest {

  private final KnowledgeDocScanner scanner = new KnowledgeDocScanner();

  private void write(Path file, String content) throws Exception {
    Files.createDirectories(file.getParent());
    Files.writeString(file, content, StandardCharsets.UTF_8);
  }

  @Test
  void scansOnlyIncludedDirectories(@TempDir Path root) throws Exception {
    write(root.resolve("AWS/step-01.md"), "# AWS 개념\n\n## 본문\n내용\n");
    write(root.resolve("Skillbook/spring-setup/SKILL.md"), "# 스킬\n내용\n");
    write(root.resolve("docs/whatever.md"), "# 문서\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).extracting(KnowledgeDoc::docKey).containsExactly("AWS/step-01.md");
  }

  @Test
  void usesFirstH1AsTitleAndTopDirectoryAsCategory(@TempDir Path root) throws Exception {
    write(root.resolve("AWS SAA-C03/Phase-01.md"), "# 클라우드 기본 개념\n\n## 목표\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).singleElement().satisfies(d -> {
      assertThat(d.title()).isEqualTo("클라우드 기본 개념");
      assertThat(d.category()).isEqualTo("AWS SAA-C03");
      assertThat(d.docKey()).isEqualTo("AWS SAA-C03/Phase-01.md");
    });
  }

  @Test
  void fallsBackToFileNameWhenNoH1(@TempDir Path root) throws Exception {
    write(root.resolve("MSA/no-heading.md"), "## 섹션만 있다\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).singleElement()
        .extracting(KnowledgeDoc::title).isEqualTo("no-heading");
  }

  @Test
  void scansNestedDirectoriesRecursively(@TempDir Path root) throws Exception {
    write(root.resolve("Sample Codes/Spring/sample.md"), "# 샘플\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).singleElement().satisfies(d -> {
      assertThat(d.category()).isEqualTo("Sample Codes");
      assertThat(d.docKey()).isEqualTo("Sample Codes/Spring/sample.md");
    });
  }

  @Test
  void deduplicatesCaseInsensitiveGlobMatches(@TempDir Path root) throws Exception {
    // Windows는 대소문자를 구분하지 않아 *.md와 *.MD가 같은 파일을 중복 매칭한다.
    write(root.resolve("AWS/a.md"), "# A\n내용\n");
    write(root.resolve("AWS/b.MD"), "# B\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).hasSize(2);
    assertThat(docs).extracting(KnowledgeDoc::docKey).doesNotHaveDuplicates();
  }

  @Test
  void docHashChangesWhenContentChanges(@TempDir Path root) throws Exception {
    Path file = root.resolve("AWS/x.md");
    write(file, "# X\n원본\n");
    String before = scanner.scan(root).get(0).docHash();

    write(file, "# X\n수정본\n");
    String after = scanner.scan(root).get(0).docHash();

    assertThat(after).isNotEqualTo(before).hasSize(64);
  }

  @Test
  void docKeyUsesForwardSlashesOnAllPlatforms(@TempDir Path root) throws Exception {
    write(root.resolve("Sample Codes/Nested/deep/x.md"), "# X\n내용\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs.get(0).docKey()).isEqualTo("Sample Codes/Nested/deep/x.md");
  }

  @Test
  void normalizesCrlfLineEndings(@TempDir Path root) throws Exception {
    // dsd 레포는 CRLF로 체크아웃돼 있다. 정규화 없이 담으면 markdown에 '\r'이 섞여
    // 청킹 경계(MAX_CHARS)를 왜곡하고 해시가 체크아웃 설정에 의존하게 된다.
    write(root.resolve("AWS/crlf.md"), "# 제목\r\n\r\n## 섹션\r\n본문\r\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    assertThat(docs).singleElement()
        .satisfies(d -> assertThat(d.markdown()).doesNotContain("\r"));
  }

  @Test
  void docHashIsIndependentOfLineEndingStyle(@TempDir Path root) throws Exception {
    write(root.resolve("AWS/crlf.md"), "# X\r\n본문\r\n");
    write(root.resolve("MSA/lf.md"), "# X\n본문\n");

    List<KnowledgeDoc> docs = scanner.scan(root);

    String crlfHash = docs.stream()
        .filter(d -> d.docKey().equals("AWS/crlf.md")).findFirst().orElseThrow().docHash();
    String lfHash = docs.stream()
        .filter(d -> d.docKey().equals("MSA/lf.md")).findFirst().orElseThrow().docHash();

    assertThat(crlfHash).isEqualTo(lfHash);
  }
}
