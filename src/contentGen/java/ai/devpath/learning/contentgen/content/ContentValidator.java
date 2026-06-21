package ai.devpath.learning.contentgen.content;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContentValidator {

  private static final Pattern KEBAB = Pattern.compile("[a-z0-9]+(?:-[a-z0-9]+)*");
  private static final Pattern HTML_TAG = Pattern.compile("<[A-Za-z][A-Za-z0-9:-]*(?:\\s[^>]*)?/?>");
  private static final Pattern FENCE_LINE = Pattern.compile("^\\s*```.*$");
  private static final List<String> ALLOWED_BLOOM = List.of(
      "REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE");

  public ContentValidationReport validate(List<ApprovedContent> contents) {
    var errors = new ArrayList<String>();
    var warnings = new ArrayList<String>();
    if (contents == null) {
      return new ContentValidationReport(List.of("contents must not be null"), warnings);
    }

    var seenSlugs = new HashSet<String>();
    for (int i = 0; i < contents.size(); i++) {
      validateContent(i + 1, contents.get(i), seenSlugs, errors);
    }
    validateTrackQuotas(contents, errors);
    validateCodeBlockQuotas(contents, errors);
    return new ContentValidationReport(List.copyOf(errors), List.copyOf(warnings));
  }

  private void validateContent(int line, ApprovedContent c, HashSet<String> seenSlugs,
      List<String> errors) {
    if (c == null) {
      errors.add("line " + line + ": content must not be null");
      return;
    }
    if (blank(c.slug()) || !KEBAB.matcher(c.slug()).matches()) {
      errors.add("line " + line + ": slug must be lowercase kebab-case");
    } else if (!seenSlugs.add(c.slug())) {
      errors.add("line " + line + ": duplicate slug " + c.slug());
    }
    if (blank(c.title())) {
      errors.add("line " + line + ": title is required");
    }
    if (!ContentQuota.TRACKS.contains(c.track())) {
      errors.add("line " + line + ": unsupported track " + c.track());
    }
    if (!ContentQuota.LEVEL_TARGETS.containsKey(c.level())) {
      errors.add("line " + line + ": unsupported level " + c.level());
    }
    if (!"PUBLISHED".equals(c.status())) {
      errors.add("line " + line + ": status must be PUBLISHED");
    }
    if (c.estimatedMinutes() == null || c.estimatedMinutes() <= 0) {
      errors.add("line " + line + ": estimatedMinutes must be positive");
    }
    if (c.difficulty() == null || c.difficulty() < 0.0 || c.difficulty() > 1.0) {
      errors.add("line " + line + ": difficulty must be between 0.0 and 1.0");
    }
    if (!ALLOWED_BLOOM.contains(c.bloomLevel())) {
      errors.add("line " + line + ": unsupported bloomLevel " + c.bloomLevel());
    }
    if (blank(c.contentMd())) {
      errors.add("line " + line + ": contentMd is required");
    } else {
      validateMarkdown(line, c.contentMd(), errors);
    }
    if (c.conceptTags() == null || c.conceptTags().isEmpty()) {
      errors.add("line " + line + ": conceptTags must not be empty");
    } else {
      for (String tag : c.conceptTags()) {
        if (blank(tag) || !KEBAB.matcher(tag).matches()) {
          errors.add("line " + line + ": conceptTags must be kebab-case");
          break;
        }
      }
    }
  }

  private void validateMarkdown(int line, String markdown, List<String> errors) {
    if (hasUnclosedFence(markdown)) {
      errors.add("line " + line + ": fenced code block must be closed");
    }
    var withoutCodeBlocks = removeFencedCodeBlocks(markdown);
    if (HTML_TAG.matcher(withoutCodeBlocks).find()) {
      errors.add("line " + line + ": raw HTML is not allowed");
    }
  }

  private boolean hasUnclosedFence(String markdown) {
    boolean open = false;
    for (String line : markdown.split("\\R", -1)) {
      if (FENCE_LINE.matcher(line).matches()) {
        open = !open;
      }
    }
    return open;
  }

  private String removeFencedCodeBlocks(String markdown) {
    var sb = new StringBuilder();
    boolean inFence = false;
    for (String line : markdown.split("\\R", -1)) {
      if (FENCE_LINE.matcher(line).matches()) {
        inFence = !inFence;
        continue;
      }
      if (!inFence) {
        sb.append(line).append('\n');
      }
    }
    return sb.toString();
  }

  private void validateTrackQuotas(List<ApprovedContent> contents, List<String> errors) {
    var byTrack = contents.stream()
        .filter(c -> c != null && c.track() != null)
        .collect(Collectors.groupingBy(ApprovedContent::track));
    for (String track : ContentQuota.TRACKS) {
      var trackContents = byTrack.getOrDefault(track, List.of());
      if (trackContents.size() != ContentQuota.PER_TRACK) {
        errors.add(track + " must contain exactly 30 contents but was " + trackContents.size());
      }
      for (var target : ContentQuota.LEVEL_TARGETS.entrySet()) {
        long actual = trackContents.stream()
            .filter(c -> target.getKey().equals(c.level()))
            .count();
        if (actual != target.getValue()) {
          errors.add(track + " must contain " + target.getValue() + " "
              + target.getKey() + " contents but was " + actual);
        }
      }
    }
  }

  private void validateCodeBlockQuotas(List<ApprovedContent> contents, List<String> errors) {
    for (String track : ContentQuota.TRACKS) {
      long withCode = contents.stream()
          .filter(c -> c != null && track.equals(c.track()))
          .filter(c -> containsCodeBlock(c.contentMd()))
          .count();
      if (withCode < ContentQuota.CODE_BLOCK_MIN_PER_TRACK) {
        errors.add(track + " must contain at least 10 contents with fenced code blocks but was "
            + withCode);
      }
    }
  }

  private boolean containsCodeBlock(String markdown) {
    if (markdown == null) return false;
    int fenceCount = 0;
    for (String line : markdown.split("\\R", -1)) {
      if (FENCE_LINE.matcher(line).matches()) {
        fenceCount++;
      }
    }
    return fenceCount >= 2 && fenceCount % 2 == 0;
  }

  private boolean blank(String value) {
    return value == null || value.isBlank();
  }
}
