package ai.devpath.learning.knowledgegen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 학습 문서 스캐너. 에이전트 스킬 정의(Skillbook·devpath-skillpack·docs·.claude·tools)는
 * 멘토링과 무관하고 검색 노이즈가 되므로 제외한다.
 */
public class KnowledgeDocScanner {

  /** 주입 대상 디렉토리. 설계 §3.1의 확정 목록이다. */
  public static final List<String> INCLUDED_DIRECTORIES = List.of(
      "AWS", "AWS SAA-C03", "AWS advanced",
      "Dart Programing", "Flutter Design Pattern", "Flutter Programing",
      "Interview Prompt", "Java & Spring", "Javascript & TypeScript",
      "LLM Study", "MCP", "MCP study",
      "MSA", "MSA pattern", "Mermaid 다이어그램",
      "Mysql Study", "Python Programing", "Rag 구축 Study",
      "React Programing", "Troubleshooting", "아키텍처 패턴",
      "클라우드 컨테이너", "Sample Codes");

  private static final Pattern H1 = Pattern.compile("(?m)^#\\s+(.+)$");

  public List<KnowledgeDoc> scan(Path root) throws IOException {
    var docs = new ArrayList<KnowledgeDoc>();
    for (String dir : INCLUDED_DIRECTORIES) {
      Path base = root.resolve(dir);
      if (!Files.isDirectory(base)) continue;
      for (Path file : markdownFilesUnder(base)) {
        String markdown = normalizeLineEndings(Files.readString(file, StandardCharsets.UTF_8));
        docs.add(new KnowledgeDoc(
            toDocKey(root, file),
            titleOf(markdown, file),
            dir,
            sha256Hex(markdown),
            markdown));
      }
    }
    return List.copyOf(docs);
  }

  /**
   * Windows는 파일명 대소문자를 구분하지 않아 *.md와 *.MD가 같은 파일을 중복 매칭한다.
   * 정규화된 절대경로 Set으로 제거한다(실측에서 모든 수치가 정확히 2배로 나온 사고).
   */
  private List<Path> markdownFilesUnder(Path base) throws IOException {
    var unique = new LinkedHashSet<Path>();
    try (Stream<Path> walk = Files.walk(base)) {
      walk.filter(Files::isRegularFile)
          .filter(p -> {
            String name = p.getFileName().toString().toLowerCase();
            return name.endsWith(".md");
          })
          .map(Path::toAbsolutePath)
          .map(Path::normalize)
          .forEach(unique::add);
    }
    var sorted = new ArrayList<>(unique);
    sorted.sort(Path::compareTo);
    return sorted;
  }

  /**
   * 개행을 LF로 통일한다. dsd 레포는 CRLF로 체크아웃돼 있어(git core.autocrlf), 정규화 없이
   * markdown·docHash를 산출하면 Task 5 청킹(MAX_CHARS)이 '\r'만큼 왜곡되고, 해시가 체크아웃
   * 설정(CRLF/LF)에 의존해 플랫폼이 바뀌면 전량 재해시된다. 단독 '\r'(구 Mac)도 처리한다.
   */
  private String normalizeLineEndings(String value) {
    return value.replace("\r\n", "\n").replace("\r", "\n");
  }

  private String toDocKey(Path root, Path file) {
    return root.toAbsolutePath().normalize()
        .relativize(file.toAbsolutePath().normalize())
        .toString()
        .replace('\\', '/');
  }

  private String titleOf(String markdown, Path file) {
    Matcher m = H1.matcher(markdown);
    if (m.find()) {
      String title = m.group(1).trim();
      if (!title.isBlank()) {
        return title.length() > 500 ? title.substring(0, 500) : title;
      }
    }
    String name = file.getFileName().toString();
    int dot = name.lastIndexOf('.');
    return dot > 0 ? name.substring(0, dot) : name;
  }

  private String sha256Hex(String value) {
    try {
      var digest = MessageDigest.getInstance("SHA-256")
          .digest(value.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 unavailable", e);
    }
  }
}
