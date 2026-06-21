package ai.devpath.learning.contentgen.question;

public interface QuestionDraftClient {

  String generate(String track, int count, String prompt) throws Exception;
}
