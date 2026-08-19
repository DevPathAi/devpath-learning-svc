package ai.devpath.learning.contentgen.content;

/** 레벨을 지정해 콘텐츠 초안을 받아오는 경로. 테스트가 가짜 구현으로 하버스터를 검증한다. */
public interface ContentDraftClient {

  String generate(String track, String level, int count, String prompt) throws Exception;
}
