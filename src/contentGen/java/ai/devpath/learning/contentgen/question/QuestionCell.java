package ai.devpath.learning.contentgen.question;

/** 한 번의 생성 요청이 겨냥하는 쿼터 셀. count 는 이 요청에서 받아낼 문항 수다. */
public record QuestionCell(
    String questionType,
    String bloomLevel,
    String difficultyBand,
    int count) {}
