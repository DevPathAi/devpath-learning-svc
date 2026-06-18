package ai.devpath.learning.assessment.guest;

import java.util.List;

public record GuestSession(
    String guestId,
    String track,
    double currentDifficulty,
    Long pendingQuestionId,
    List<Presented> presented,
    boolean completed,
    String diagnosedLevel) {

  public record Presented(long questionId, double difficulty, Boolean correct,
                          boolean skipped, String answer, Integer timeSpentSec) {}
}
