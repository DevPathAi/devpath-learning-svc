package ai.devpath.learning.assessment.dto;

public record QuestionView(long id, String type, String content, String options, String bloomLevel, double difficulty) {}
