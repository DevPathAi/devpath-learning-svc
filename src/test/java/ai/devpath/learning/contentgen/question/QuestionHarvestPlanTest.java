package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * 생성기가 "100개를 한 번에" 요구하면 모델이 한 형태로 붕괴한다(실측: NODE_TYPESCRIPT 355줄 중
 * 고유 16건, 전부 MCQ·REMEMBER·난이도 0.1-0.2). 셀 단위로 쪼개 요청하려면 먼저 쿼터를 만족하는
 * 셀 계획이 있어야 한다.
 */
class QuestionHarvestPlanTest {

  private static Map<String, Integer> tally(List<QuestionCell> cells,
      Function<QuestionCell, String> key) {
    return cells.stream().collect(Collectors.groupingBy(key,
        LinkedHashMap::new, Collectors.summingInt(QuestionCell::count)));
  }

  @Test
  void planCoversExactlyOneTrackQuota() {
    assertThat(QuestionHarvestPlan.cells().stream().mapToInt(QuestionCell::count).sum())
        .isEqualTo(100);
  }

  @Test
  void planMatchesEveryQuotaMarginal() {
    var cells = QuestionHarvestPlan.cells();

    assertThat(tally(cells, QuestionCell::questionType))
        .containsExactlyInAnyOrderEntriesOf(QuestionQuota.TYPE_TARGETS);
    assertThat(tally(cells, QuestionCell::bloomLevel))
        .containsExactlyInAnyOrderEntriesOf(QuestionQuota.BLOOM_TARGETS);
    assertThat(tally(cells, QuestionCell::difficultyBand))
        .containsExactlyInAnyOrderEntriesOf(QuestionQuota.DIFFICULTY_BAND_TARGETS);
  }

  // 주변부만 맞추면 CODE_READING 30개가 전부 최고 난이도로 몰릴 수 있다. 그러면 쿼터는
  // 통과하지만 진단은 "코드 읽기 = 어려운 문제"라는 엉뚱한 신호를 준다.
  @Test
  void codeReadingIsNotConfinedToOneDifficultyBand() {
    var bands = QuestionHarvestPlan.cells().stream()
        .filter(cell -> cell.questionType().equals("CODE_READING"))
        .filter(cell -> cell.count() > 0)
        .map(QuestionCell::difficultyBand)
        .distinct()
        .toList();

    assertThat(bands).hasSizeGreaterThanOrEqualTo(3);
  }

  @Test
  void everyCellAsksForAtLeastOneQuestion() {
    assertThat(QuestionHarvestPlan.cells()).allSatisfy(cell ->
        assertThat(cell.count()).isPositive());
  }

  // 셀 하나가 지나치게 크면 그 안에서 다시 루프가 난다. 붕괴를 부른 원인이 "한 번에 많이"였다.
  @Test
  void noCellAsksForMoreThanASmallBatch() {
    assertThat(QuestionHarvestPlan.cells()).allSatisfy(cell ->
        assertThat(cell.count()).isLessThanOrEqualTo(6));
  }
}
