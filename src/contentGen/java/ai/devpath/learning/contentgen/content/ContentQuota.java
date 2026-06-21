package ai.devpath.learning.contentgen.content;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public final class ContentQuota {

  public static final List<String> TRACKS = List.of(
      "BACKEND_SPRING",
      "FRONTEND_REACT",
      "MOBILE_FLUTTER",
      "DEVOPS",
      "FULLSTACK");

  public static final Map<String, Integer> LEVEL_TARGETS = levelTargets();

  public static final int PER_TRACK = 30;
  public static final int CODE_BLOCK_MIN_PER_TRACK = 10;

  private ContentQuota() {}

  private static Map<String, Integer> levelTargets() {
    var targets = new LinkedHashMap<String, Integer>();
    targets.put("INTRO", 8);
    targets.put("INTERMEDIATE", 14);
    targets.put("ADVANCED", 8);
    return Collections.unmodifiableMap(targets);
  }
}
