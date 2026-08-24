package ai.devpath.learning.contentgen.question;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

/**
 * 문항 1건에 대한 사실 검증 판정(장부의 한 줄).
 *
 * <p>2026-08-22 문항 800 전수 검수의 후속 조치다: 구조·분포 게이트만 거친 트랙은
 * 결함율 21~47%였고, 사실 검증 루프를 거친 트랙(PYTHON_BACKEND)만 3%였다.
 * 이 장부가 없거나 낡으면 {@link QuestionValidator} 가 실패해, 리뷰 루프를
 * 건너뛴 문항이 시드로 흘러갈 수 없게 한다.
 *
 * <p>fingerprint 는 채점에 영향을 주는 필드(track·content·options·정답 인덱스)만
 * 요약한다 — bloom·difficulty·태그 조정은 재검증을 요구하지 않는다. 반대로
 * 채점 필드가 하나라도 바뀌면 fingerprint 가 어긋나 장부가 무효가 된다.
 */
public record QuestionVerification(
    String track,
    String fingerprint,
    String verdict,
    List<String> axes,
    String reviewer,
    String reviewedAt) {

  /** 판정이 반드시 다뤄야 하는 검증 축: 사실성 · 단일 정답 · 자립성. */
  public static final List<String> REQUIRED_AXES = List.of("FACT", "SINGLE_KEY", "SELF_CONTAINED");

  public static final String VERDICT_PASS = "PASS";

  /** 필드 구분자 — 본문에 나타날 수 없는 NUL. 공백 연결은 필드 경계가 섞일 수 있다. */
  private static final char SEPARATOR = (char) 0;

  public static String fingerprintOf(ApprovedQuestion question) {
    var sb = new StringBuilder();
    sb.append(nullSafe(question.track())).append(SEPARATOR);
    sb.append(nullSafe(question.content())).append(SEPARATOR);
    if (question.options() != null) {
      for (String option : question.options()) {
        sb.append(nullSafe(option)).append(SEPARATOR);
      }
    }
    sb.append(question.answerKey() == null ? "" : String.valueOf(question.answerKey().correct()));
    return sha256(sb.toString());
  }

  private static String nullSafe(String value) {
    return value == null ? "" : value;
  }

  private static String sha256(String value) {
    try {
      var digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 unavailable", e);
    }
  }
}
