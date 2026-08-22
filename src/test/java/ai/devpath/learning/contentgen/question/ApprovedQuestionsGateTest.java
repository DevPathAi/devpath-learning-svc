package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * 승인 JSONL 게이트를 CI 로 강제한다. 지금까지 validateQuestions 는 수동 gradle
 * 태스크뿐이라 「CI validates committed JSONL」(README) 은 선언만 있고 실제가 없었다 —
 * 그 틈으로 검증 안 된 문항 800건 중 237건(29.6%)이 결함인 채 운영에 갔다(2026-08-22 검수).
 *
 * <p>이 테스트가 지키는 두 계약:
 * <ol>
 *   <li>구조·분포·**사실 검증 장부** 게이트 전부 통과 — 리뷰 루프 없이 문항을
 *       추가·수정하면 fingerprint 불일치로 실패한다.</li>
 *   <li>커밋된 시드 SQL(4곳)은 승인 JSONL 에서 결정적으로 재생성한 것과 일치 —
 *       승인 JSONL 을 우회한 시드 직접 편집(또는 그 반대)이 표류를 만들 수 없다.</li>
 * </ol>
 */
class ApprovedQuestionsGateTest {

  private static final Path APPROVED =
      Path.of("tools/content-gen/generated/approved/questions.jsonl");
  private static final Path MANIFEST =
      Path.of("tools/content-gen/generated/approved/question_verifications.jsonl");

  @Test
  void approvedQuestionsPassAllGatesIncludingFactVerification() throws Exception {
    var questions = new QuestionJsonlReader().read(APPROVED);
    var verifications = new QuestionVerificationJsonlReader().read(MANIFEST);

    var report = new QuestionValidator().validate(questions, verifications);

    assertThat(report.errors()).isEmpty();
  }

  @Test
  void committedSeedSqlMatchesDeterministicRegeneration() throws Exception {
    var questions = new QuestionJsonlReader().read(APPROVED);
    var expected = new QuestionSeedSqlWriter().toSql(questions);

    for (String committed : new String[] {
        "tools/content-gen/generated/seeds/question_bank_seed.sql",
        "src/main/resources/db/seed/question_bank_md2_seed.sql",
        "src/test/resources/seed/question_bank_md2_seed.sql",
        "src/test/resources/seed/question_bank_seed.sql"}) {
      var actual = Files.readString(Path.of(committed));
      assertThat(normalize(actual))
          .as("%s 는 승인 JSONL 재생성본과 일치해야 한다 (makeQuestionSeedSql 로 갱신하라)",
              committed)
          .isEqualTo(normalize(expected));
    }
  }

  private static String normalize(String sql) {
    return sql.replace("\r\n", "\n");
  }
}
