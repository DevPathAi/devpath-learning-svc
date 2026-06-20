## Step 1: #2 진단

> **상태(2026-06-19)**: 진단 도메인은 부분 구현. `question_bank`, `assessments`, `assessment_items`, `assessment_results` Flyway와 member/guest assessment API, claim API, `AssessmentCompletedEvent` 발행이 코드에 존재한다. 프론트 실API E2E와 학습경로 스키마/엔진은 아직 목표 상태다.

### 1.1 진단 스키마·알고리즘
- [x] question_bank + Bloom 태깅 스키마
- [x] assessments·assessment_items·assessment_results 스키마
- [x] 적응형 진단 알고리즘(난이도 ±)
### 1.2 비회원 세션
- [x] 비회원 진단 세션(Redis 30분) + 결과 회원 이관
## Step 2: #3 학습경로 (1st Aha)
### 2.1 임베딩·경로
- [ ] 콘텐츠 임베딩(nomic-embed-text 768, 운영 provider 교체 가능) + HNSW 인덱스
- [ ] milestone.target_skills 매칭 + path_milestones·path_weekly_tasks
