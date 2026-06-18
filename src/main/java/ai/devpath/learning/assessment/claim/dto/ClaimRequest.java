package ai.devpath.learning.assessment.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClaimRequest(@JsonProperty("guest_assessment_id") String guestAssessmentId) {}
