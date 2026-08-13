package ai.devpath.learning.contentgen.content;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Pattern;

public class ContentChunker {

  private static final int MAX_CHARS = 1_200;
  private static final int OVERLAP_CHARS = 120;
  private static final Pattern H2 = Pattern.compile("(?m)^##\\s+.+$");

  public List<ContentChunk> chunksFor(ApprovedContent content) {
    return chunksFor(content.slug(), content.contentMd());
  }

  /** 청킹 일반형. key는 청크의 소속 식별자(콘텐츠 slug 또는 문서 doc_key). */
  public List<ContentChunk> chunksFor(String key, String markdown) {
    var sections = splitH2Sections(markdown);
    var chunks = new ArrayList<ContentChunk>();
    int index = 0;
    for (String section : sections) {
      for (String chunkText : splitLongSection(section)) {
        if (!chunkText.isBlank()) {
          chunks.add(new ContentChunk(key, index++, chunkText, normalizedSha256Hex(chunkText)));
        }
      }
    }
    return List.copyOf(chunks);
  }

  public String normalizedSha256Hex(String text) {
    return sha256Hex(normalize(text));
  }

  private List<String> splitH2Sections(String markdown) {
    if (markdown == null || markdown.isBlank()) {
      return List.of();
    }
    var matcher = H2.matcher(markdown);
    var starts = new ArrayList<Integer>();
    while (matcher.find()) {
      starts.add(matcher.start());
    }
    if (starts.isEmpty()) {
      return List.of(markdown.trim());
    }

    var sections = new ArrayList<String>();
    if (starts.get(0) > 0) {
      var intro = markdown.substring(0, starts.get(0)).trim();
      if (!intro.isBlank()) {
        sections.add(intro);
      }
    }
    for (int i = 0; i < starts.size(); i++) {
      int start = starts.get(i);
      int end = i + 1 < starts.size() ? starts.get(i + 1) : markdown.length();
      var section = markdown.substring(start, end).trim();
      if (!section.isBlank()) {
        sections.add(section);
      }
    }
    return sections;
  }

  private List<String> splitLongSection(String section) {
    if (section.length() <= MAX_CHARS) {
      return List.of(section.trim());
    }

    int chunkCount = Math.max(2,
        (int) Math.ceil((section.length() - OVERLAP_CHARS) / (double) (MAX_CHARS - OVERLAP_CHARS)));
    int chunkLength = (int) Math.ceil((section.length() + (chunkCount - 1) * OVERLAP_CHARS)
        / (double) chunkCount);

    var chunks = new ArrayList<String>();
    int start = 0;
    for (int i = 0; i < chunkCount && start < section.length(); i++) {
      int end = Math.min(section.length(), start + chunkLength);
      if (i == chunkCount - 1) {
        end = section.length();
      } else {
        end = safeSurrogateBoundary(section, end);
      }
      chunks.add(section.substring(start, end).trim());
      if (end == section.length()) {
        break;
      }
      start = safeSurrogateBoundary(section, Math.max(0, end - OVERLAP_CHARS));
    }
    return chunks;
  }

  /**
   * UTF-16 코드 유닛 인덱스 산술로 계산된 경계가 서로게이트 쌍(astral 문자, 예: 이모지) 중간에
   * 떨어지면, 그 지점에서 substring한 결과 String이 짝 없는 서로게이트를 갖게 되고 이를 UTF-8로
   * 쓰는 순간(BufferedWriter) MalformedInputException이 던져진다. index가 low surrogate이고
   * 그 직전이 high surrogate라면 쌍을 쪼개지 않도록 경계를 1 코드 유닛 앞으로 당긴다(쌍은 그 다음
   * 청크에 온전히 포함된다). index==0이거나 문자열 끝(section.length())은 쌍 중간일 수 없어
   * 그대로 반환한다.
   */
  private int safeSurrogateBoundary(String text, int index) {
    if (index <= 0 || index >= text.length()) {
      return index;
    }
    if (Character.isLowSurrogate(text.charAt(index)) && Character.isHighSurrogate(text.charAt(index - 1))) {
      return index - 1;
    }
    return index;
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().replaceAll("\\s+", " ");
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
