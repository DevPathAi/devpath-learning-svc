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
    var sections = splitH2Sections(content.contentMd());
    var chunks = new ArrayList<ContentChunk>();
    int index = 0;
    for (String section : sections) {
      for (String chunkText : splitLongSection(section)) {
        if (!chunkText.isBlank()) {
          chunks.add(new ContentChunk(
              content.slug(),
              index++,
              chunkText,
              normalizedSha256Hex(chunkText)));
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
      }
      chunks.add(section.substring(start, end).trim());
      if (end == section.length()) {
        break;
      }
      start = Math.max(0, end - OVERLAP_CHARS);
    }
    return chunks;
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
