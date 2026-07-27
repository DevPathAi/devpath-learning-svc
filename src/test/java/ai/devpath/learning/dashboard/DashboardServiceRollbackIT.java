package ai.devpath.learning.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * 회귀 방지: 활성 학습 경로가 없는 사용자의 대시보드 조회는 예외 없이 폴백 요약을 반환해야 한다.
 *
 * <p>기존 결함: {@code DashboardService.summary()}(readOnly 트랜잭션)가 내부 {@code @Transactional}
 * {@code LearningPathQueryService.current()}의 {@link java.util.NoSuchElementException}을 catch했지만,
 * 내부 트랜잭션이 던진 런타임 예외가 공유 트랜잭션을 rollback-only로 마킹 → 커밋 시
 * {@code UnexpectedRollbackException}(HTTP 500). 유닛 테스트(목)는 실제 트랜잭션 전파가 없어 이를 놓친다.
 * 실제 트랜잭션이 도는 {@code @SpringBootTest}에서만 재현되므로 통합 테스트로 고정한다.
 */
@SpringBootTest
@ActiveProfiles("test")
class DashboardServiceRollbackIT {

  // 커뮤니티 배지 조회는 외부 HTTP 호출이므로 목으로 대체(트랜잭션 오염과 무관).
  @MockitoBean
  CommunityBadgeClient badgeClient;

  @Autowired
  DashboardService dashboardService;

  @Test
  void summaryReturnsFallbackWhenNoActiveLearningPath() {
    when(badgeClient.badgeNamesOf(anyLong())).thenReturn(List.of());
    // 학습 경로·스트릭·진척이 전혀 없는 사용자 id.
    long userIdWithoutData = 999_999_999L;

    assertThatCode(() -> {
      DashboardSummary summary = dashboardService.summary(userIdWithoutData);
      assertThat(summary.progressPercent()).isZero();
      assertThat(summary.nextTaskTitle()).isNull();
      assertThat(summary.streakDays()).isZero();
    }).doesNotThrowAnyException();
  }
}
