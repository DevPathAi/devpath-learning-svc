package ai.devpath.learning.seed;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class QuestionBankKoreanSeedTest {

  @Test
  void seedContainsKorean() throws Exception {
    String sql = Files.readString(Path.of("src/main/resources/db/seed/question_bank_md2_seed.sql"));
    assertTrue(Pattern.compile("[\\uAC00-\\uD7A3]").matcher(sql).find(),
        "question_bank 시드에 한글이 포함되어야 한다(영어 필러 회귀 방지)");
  }
}
