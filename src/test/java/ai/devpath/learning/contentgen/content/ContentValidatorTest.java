package ai.devpath.learning.contentgen.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContentValidatorTest {

  private final ContentValidator validator = new ContentValidator();

  @Test
  void validContentSetPassesWithoutErrors() {
    var report = validator.validate(validContents());

    assertThat(report.errors()).isEmpty();
  }

  @Test
  void rejectsTrackCountOtherThanThirty() {
    var contents = validContents();
    contents.remove(0);

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("BACKEND_SPRING").contains("30"));
  }

  @Test
  void rejectsDuplicateSlug() {
    var contents = validContents();
    contents.set(1, withSlug(contents.get(1), contents.get(0).slug()));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate slug"));
  }

  @Test
  void rejectsNonLowercaseKebabSlug() {
    var contents = validContents();
    contents.set(0, withSlug(contents.get(0), "Backend_Spring_Intro"));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("slug").contains("kebab-case"));
  }

  @Test
  void rejectsNonPublishedStatus() {
    var contents = validContents();
    contents.set(0, withStatus(contents.get(0), "DRAFT"));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("PUBLISHED"));
  }

  @Test
  void rejectsRawHtmlOutsideCodeFence() {
    var contents = validContents();
    contents.set(0, withMarkdown(contents.get(0), "## Goal\n<div>raw</div>"));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("raw HTML"));
  }

  @Test
  void allowsHtmlInsideCodeFenceButRejectsUnclosedFence() {
    var withHtmlCode = validContents();
    withHtmlCode.set(0, withMarkdown(withHtmlCode.get(0),
        "## Example\n```html\n<div>inside code</div>\n```"));
    var unclosed = validContents();
    unclosed.set(0, withMarkdown(unclosed.get(0), "## Example\n```java\nreturn 1;"));

    assertThat(validator.validate(withHtmlCode).errors()).isEmpty();
    assertThat(validator.validate(unclosed).errors()).anySatisfy(error ->
        assertThat(error).contains("fenced code block"));
  }

  @Test
  void rejectsDifficultyOutsideZeroToOne() {
    var contents = validContents();
    contents.set(0, withDifficulty(contents.get(0), 1.1));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("difficulty"));
  }

  @Test
  void rejectsWrongLevelQuota() {
    var contents = validContents();
    contents.set(0, withLevel(contents.get(0), "INTERMEDIATE"));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("INTRO").contains("8"));
    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("INTERMEDIATE").contains("14"));
  }

  @Test
  void rejectsTrackWithFewerThanTenCodeBlockContents() {
    var contents = validContents();
    contents.set(0, withMarkdown(contents.get(0), "## Goal\nNo code in this one."));

    var report = validator.validate(contents);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("fenced code blocks").contains("9"));
  }

  @Test
  void rejectsMissingOrNonKebabConceptTags() {
    var missing = validContents();
    missing.set(0, withTags(missing.get(0), List.of()));
    var nonKebab = validContents();
    nonKebab.set(0, withTags(nonKebab.get(0), List.of("Spring Core")));

    assertThat(validator.validate(missing).errors()).anySatisfy(error ->
        assertThat(error).contains("conceptTags"));
    assertThat(validator.validate(nonKebab).errors()).anySatisfy(error ->
        assertThat(error).contains("kebab-case"));
  }

  static List<ApprovedContent> validContents() {
    var contents = new ArrayList<ApprovedContent>();
    for (String track : ContentQuota.TRACKS) {
      for (int i = 0; i < ContentQuota.PER_TRACK; i++) {
        contents.add(content(track, i));
      }
    }
    return contents;
  }

  private static ApprovedContent content(String track, int index) {
    var trackSlug = track.toLowerCase().replace('_', '-');
    var level = index < 8 ? "INTRO" : index < 22 ? "INTERMEDIATE" : "ADVANCED";
    var bloom = index < 8 ? "UNDERSTAND" : index < 22 ? "APPLY" : "ANALYZE";
    var markdown = "## Learning Goal\n"
        + "Practice " + trackSlug + " concept " + index + " with a focused implementation note.\n\n"
        + "## Checklist\n"
        + "- Identify the main responsibility.\n"
        + "- Apply the concept in a small task.\n";
    if (index < 10) {
      markdown += "\n```java\n"
          + "String concept = \"" + trackSlug + "-" + index + "\";\n"
          + "System.out.println(concept);\n"
          + "```\n";
    }
    return new ApprovedContent(
        trackSlug + "-content-" + String.format("%02d", index + 1),
        track + " content " + (index + 1),
        track,
        level,
        markdown,
        15,
        index < 8 ? 0.2 : index < 22 ? 0.55 : 0.85,
        bloom,
        List.of(trackSlug + "-concept-" + (index % 10)),
        "PUBLISHED");
  }

  private static ApprovedContent withSlug(ApprovedContent c, String slug) {
    return new ApprovedContent(slug, c.title(), c.track(), c.level(), c.contentMd(),
        c.estimatedMinutes(), c.difficulty(), c.bloomLevel(), c.conceptTags(), c.status());
  }

  private static ApprovedContent withStatus(ApprovedContent c, String status) {
    return new ApprovedContent(c.slug(), c.title(), c.track(), c.level(), c.contentMd(),
        c.estimatedMinutes(), c.difficulty(), c.bloomLevel(), c.conceptTags(), status);
  }

  private static ApprovedContent withMarkdown(ApprovedContent c, String markdown) {
    return new ApprovedContent(c.slug(), c.title(), c.track(), c.level(), markdown,
        c.estimatedMinutes(), c.difficulty(), c.bloomLevel(), c.conceptTags(), c.status());
  }

  private static ApprovedContent withDifficulty(ApprovedContent c, double difficulty) {
    return new ApprovedContent(c.slug(), c.title(), c.track(), c.level(), c.contentMd(),
        c.estimatedMinutes(), difficulty, c.bloomLevel(), c.conceptTags(), c.status());
  }

  private static ApprovedContent withLevel(ApprovedContent c, String level) {
    return new ApprovedContent(c.slug(), c.title(), c.track(), level, c.contentMd(),
        c.estimatedMinutes(), c.difficulty(), c.bloomLevel(), c.conceptTags(), c.status());
  }

  private static ApprovedContent withTags(ApprovedContent c, List<String> tags) {
    return new ApprovedContent(c.slug(), c.title(), c.track(), c.level(), c.contentMd(),
        c.estimatedMinutes(), c.difficulty(), c.bloomLevel(), tags, c.status());
  }
}
