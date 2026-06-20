package ai.devpath.learning.path.ai;

import java.util.List;

public record PathGenerateCommand(
    String track,
    String diagnosedLevel,
    List<String> strengthConcepts,
    List<String> weaknessConcepts,
    String goal
) {
}
