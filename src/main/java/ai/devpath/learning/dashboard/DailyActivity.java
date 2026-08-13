package ai.devpath.learning.dashboard;

/** 주간 학습량 1일치. [date] KST 기준 ISO 날짜(yyyy-MM-dd), [completedCount] 그 날 완료 콘텐츠 수. */
public record DailyActivity(String date, int completedCount) {}
