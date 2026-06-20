package ai.devpath.learning.assessment.dto;

public record AssessmentResultView(String diagnosedLevel, String conceptScores, String strengthConcepts, String weaknessConcepts, Double confidenceWeight) {}
