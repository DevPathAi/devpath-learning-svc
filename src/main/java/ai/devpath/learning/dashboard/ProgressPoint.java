package ai.devpath.learning.dashboard;

import java.util.Map;

/**
 * 진행률 추이 1점. [date] KST 기준 ISO 날짜(yyyy-MM-dd), [percent] 0~100 전체 누적 완료율.
 * [byType] 과제 유형(READ·PRACTICE·QUIZ)별 누적 완료율. 해당 유형의 과제가 0개면 키가 없다.
 */
public record ProgressPoint(String date, int percent, Map<String, Integer> byType) {}
