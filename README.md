# devpath-learning-svc

**DevPath AI** 학습 서비스 — 온보딩 진단, 학습 경로 엔진, 콘텐츠, AI 멘토를 담당합니다.

## 담당 도메인

| 모듈 | 역할 |
|------|------|
| onboarding | 적응형 진단 테스트 |
| learning | 학습 경로 엔진 + 진척도 (1st Aha 핵심) |
| content | 콘텐츠 CRUD + 임베딩(pgvector) |
| mentor | **ai-svc 소관**(`/ai-mentor/**`, 슬라이스 #7) — 1st Aha 초기 계획 잔재로 learning-svc에는 미구현 |

**SLO**: 학습 경로 생성 p95 < 8초.

## 구성

- Spring Boot 4.0.x · Java 21 · Gradle (Kotlin DSL)
- [devpath-svc-template](https://github.com/DevPathAi/devpath-svc-template) 기반
- DB 의존성(JPA + PostgreSQL, Redis)은 `build.gradle.kts` 주석 해제로 활성화
- Claude API 호출은 [devpath-ai-svc](https://github.com/DevPathAi/devpath-ai-svc)를 경유

## 빌드 / 실행

```bash
./gradlew build
./gradlew bootRun    # 기본 포트 8080
```

로컬 인프라는 [devpath-shared](https://github.com/DevPathAi/devpath-shared)의 docker-compose를 사용합니다.

## 개발 규칙

- Git 규칙: [documents/09_Git_규칙_정의서](https://github.com/DevPathAi/documents/blob/main/09_Git_규칙_정의서.md)
- 워크플로우 현황: `docs/project-management/` → [workflow-dashboard](https://devpathai.github.io/workflow-dashboard/)
