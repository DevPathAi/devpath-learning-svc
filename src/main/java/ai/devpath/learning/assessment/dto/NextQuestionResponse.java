package ai.devpath.learning.assessment.dto;

public record NextQuestionResponse(QuestionView question, int index, int total) {}
