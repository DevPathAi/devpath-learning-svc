package ai.devpath.learning.assessment.dto;

public record AnswerRequest(long questionId, String answer, boolean skipped, Integer timeSpentSec) {}
