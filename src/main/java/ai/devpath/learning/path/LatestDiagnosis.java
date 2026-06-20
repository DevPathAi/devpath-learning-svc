package ai.devpath.learning.path;

import java.util.List;

public record LatestDiagnosis(
    long assessmentId,
    String track,
    String diagnosedLevel,
    List<String> strengthConcepts,
    List<String> weaknessConcepts,
    Double confidenceWeight
) {
}
