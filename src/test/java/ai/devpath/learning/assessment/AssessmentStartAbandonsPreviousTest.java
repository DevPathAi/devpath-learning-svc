package ai.devpath.learning.assessment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 진단 시작이 그 이용자의 옛 IN_PROGRESS 세션을 정리하는지 본다.
 *
 * <p>이전에는 start() 가 아무 확인 없이 새 행을 만들어, 시작 버튼을 누를 때마다
 * 옛 세션이 IN_PROGRESS 로 남았다 — 운영 실측 10건 중 7건이 그 상태였다.
 * 트랙 선택이 생기면 「고르다 다시 시작」이 늘어 더 쌓인다.
 */
@SpringBootTest
@ActiveProfiles("test")
class AssessmentStartAbandonsPreviousTest {

  @Autowired AssessmentService service;
  @Autowired AssessmentRepository assessments;

  @Test
  void startAbandonsPreviousInProgressOfSameUser() {
    long userId = System.nanoTime();

    long first = service.start(userId, "BACKEND_SPRING");
    long second = service.start(userId, "DEVOPS");

    assertThat(assessments.findById(first).orElseThrow().getStatus()).isEqualTo("ABANDONED");
    assertThat(assessments.findById(second).orElseThrow().getStatus()).isEqualTo("IN_PROGRESS");
  }

  @Test
  void startDoesNotTouchOtherUsers() {
    long mine = System.nanoTime();
    long other = mine + 1;

    long othersAssessment = service.start(other, "BACKEND_SPRING");
    service.start(mine, "DEVOPS");

    assertThat(assessments.findById(othersAssessment).orElseThrow().getStatus())
        .isEqualTo("IN_PROGRESS");
  }
}
