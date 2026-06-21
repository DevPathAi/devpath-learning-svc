package ai.devpath.learning.contentgen.question;

import java.util.List;

public record ApprovedQuestion(
    String track,
    String questionType,
    String content,
    List<String> options,
    AnswerKey answerKey,
    String bloomLevel,
    Double difficulty,
    List<String> conceptTags,
    String explanation) {

  public record AnswerKey(Integer correct) {}
}
