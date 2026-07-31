package ai.devpath.learning.dashboard;

/** 진행률 추이 1점. [date] KST 기준 ISO 날짜(yyyy-MM-dd), [percent] 0~100 누적 완료율. */
public record ProgressPoint(String date, int percent) {}
