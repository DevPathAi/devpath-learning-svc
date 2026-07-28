# 한국어 진단 콘텐츠 생성 설계 (콘텐츠 한국어화)

- 날짜: 2026-07-28
- 상태: **사용자 승인** (구현 전 — 이 spec 리뷰 후 플랜 작성)
- 대상 레포: `devpath-learning-svc`(`tools/content-gen` 툴체인·프롬프트) · (후속) `devpath-shared`(운영 마이그레이션)
- 우선순위: 로컬 4이슈 워크스트림 **①** (다음: 멘토 Ollama+커뮤니티 → UI/UX)

## 1. 배경 — 콘텐츠가 "영어"가 아니라 "가짜 필러" (코드 실측)

진단 문항/응답이 영어로 나온다는 지적의 실체는 **언어 문제가 아니라 무의미한 템플릿 필러**다.

- 운영/시드 소스 `tools/content-gen/generated/approved/questions.jsonl`(500행) 첫 레코드:
  `"BACKEND_SPRING 001: Which option best applies spring-core in a DevPath diagnostic scenario?"` + 제네릭 보기 4개(`"Apply the primary concept deliberately"` 등) + 템플릿 explanation. `questions.fixture.jsonl`(10행)과 **동일 패턴** → 실제 문항이 아님.
- 원인: 생성 프롬프트(`tools/content-gen/prompts/question-system.md`·`tracks/*.md`)가 **영어 + 최소 포맷 규칙만** 담고 있어(예: backend-spring.md = 주제 한 줄), 실제 고품질 문항 생성 지시가 없음.

**그러나 생성 파이프라인은 완비돼 있다**(재사용 가능):
- 질문: `GenerateQuestionsCommand`(Ollama `/api/chat`, `OllamaQuestionDraftClient`) → `ValidateQuestionsCommand`(`QuestionValidator`) → `MakeQuestionSeedSqlCommand`(`QuestionSeedSqlWriter`).
- 콘텐츠: `GenerateContentsCommand` → `ValidateContentsCommand` → `MakeContentSeedSqlCommand`, 임베딩 `EmbedContentsCommand`(`OllamaEmbeddingClient`).
- 쿼터(`QuestionQuota`): 5트랙(BACKEND_SPRING·FRONTEND_REACT·MOBILE_FLUTTER·DEVOPS·FULLSTACK) × **PER_TRACK 100 = 500**. 타입 MCQ 70/CODE_READING 30, Bloom(REMEMBER10·UNDERSTAND25·APPLY30·ANALYZE25·EVALUATE10), 난이도밴드(0.1–0.2:10 … 0.9:10).

**로컬 환경**: 이 PC에 **NVIDIA RTX 2080 Ti(11GB VRAM)** 존재 → 로컬 Ollama가 GPU로 빠름(클라우드 CPU 대비). **Ollama는 미설치**(설치 필요).

## 2. 목표 / 비목표

**목표**
- 실제로 의미 있는 **한국어 진단 문항 + 학습 콘텐츠**를 로컬 Ollama로 생성 → 검증 → 로컬 DB 재시드까지 완료. 진단이 실물이 되게 한다.
- 기존 `tools/content-gen` 파이프라인·검증·쿼터를 **재사용**(생성기 재작성 아님). 바꾸는 것은 **프롬프트(한국어·고품질)**.
- 절대조건 준수: 추측 금지·검증 우선·자화자찬 금지·작업 브랜치.

**비목표 (후속/별도)**
- 운영 DB 반영(영어→한국어 교체 마이그레이션) — AWS 정지·GPU Phase B 재배포 때. 이 워크스트림은 **로컬 검증까지**.
- 멘토 provider 전환·커뮤니티 보드·UI/UX — 후속 워크스트림.
- 문항의 전문가(SME) 감수 — 베타는 자동 검증 + 표본 스팟체크로 갈음.

## 3. 설계

### 3.1 로컬 셋업 (선행)
- Ollama 설치(Windows, `winget install Ollama.Ollama` 또는 공식 인스톨러) → `ollama serve`(기본 `localhost:11434`).
- 모델 pull: **`qwen2.5:7b`**(~4.7GB, 11GB VRAM에 여유, 한국어 무난) 시작. 품질 부족 시 **`qwen2.5:14b`**(q4, ~9GB, 11GB 적재 가능) 상향.
- 임베딩 모델 `nomic-embed-text` pull(콘텐츠 임베딩용).

### 3.2 프롬프트 재작성 (핵심 변경)
- `prompts/question-system.md`: 한국어로 재작성. 요구 = **실제 기술 개념을 묻는 문항**(제네릭 템플릿 금지), 자연스러운 한국어 지문·보기·해설, 정답키(0-based index) 정확, `question.schema.json` 준수, MCQ/CODE_READING·Bloom·난이도밴드·conceptTags(kebab-case) 규칙 유지.
- `prompts/tracks/<track>.md` 5개: 각 트랙의 **구체 개념 목록·예시 시나리오**를 한국어로 확장(예: BACKEND_SPRING = 트랜잭션 전파, JPA N+1, Kafka 컨슈머 리밸런싱 등 실제 주제 + CODE_READING용 코드 스니펫 지침).
- `prompts/content-system.md` + 콘텐츠 트랙 프롬프트: 학습 콘텐츠(`content_md`)를 한국어로 생성하도록 재작성.

### 3.3 생성 → 검증 → 승인 → 시드 (기존 파이프라인)
1. **생성**: `GenerateQuestionsCommand qwen2.5:7b` (env `OLLAMA_BASE_URL=http://localhost:11434`) → `generated/raw/questions.draft.jsonl`. 출력 품질/유효성 볼 때까지 **프롬프트 반복**.
2. **검증**: `ValidateQuestionsCommand` → 쿼터·스키마·정답키·중복 검사. 실패/부족분은 프롬프트 보정 후 재생성(부분 재생성 포함).
3. **승인**: 검증 통과분을 `generated/approved/questions.jsonl`로 확정(영어 필러 대체).
4. **SQL**: `MakeQuestionSeedSqlCommand approved/questions.jsonl <seed>.sql` → 새 시드 SQL.
5. **로컬 재시드**: 로컬 Postgres에 새 시드 적용(빈 DB 재적재 또는 `TRUNCATE question_bank; \i <seed>.sql`). 진단이 한국어 문항을 반환하는지 로컬 실증.
6. **콘텐츠+임베딩**: contents 동일 흐름 → `EmbedContentsCommand`(nomic-embed-text)로 `content_embeddings` 생성.

### 3.4 모델/스코프/검토 결정 (승인됨)
- 모델: `qwen2.5:7b` 시작, 필요 시 `qwen2.5:14b`.
- 스코프: **쿼터 500 유지**(GPU라 속도 무관·적응형 풀 풍부·검증기가 품질 게이트). 품질 반복이 과하면 트랙당 축소로 후퇴 가능.
- 검토: 자동 검증 + **트랙당 표본 스팟체크(사용자)**. 전수 감수 아님.

## 4. 테스트 (Test-First)

- 생성기/검증기 자체는 기존 테스트 존재(`QuestionSeedSqlWriterTest`·`ContentSeederTest` 등). 프롬프트 변경은 **산출물 검증**으로 담보한다(절대조건 2의 문서/데이터 버전).
- **검증 게이트 = `ValidateQuestionsCommand`가 그린**(쿼터·스키마·정답키 전부 통과)일 때만 승인.
- 승인 jsonl에 **한글 포함 단언**(예: content 필드에 `[가-힣]` 존재) 회귀 체크 추가 — "영어 필러로 회귀" 방지.
- 로컬 재시드 후 게스트 진단(`POST /onboarding/assessments/guest`→`/next`)이 **한국어 문항**을 반환하는지 실측.

## 5. 리스크

- **소형 모델 품질**: qwen2.5:7b가 틀린 문항·모호한 보기·잘못된 정답키 생성 가능 → 검증기 + 스팟체크로 걸러내고, 부족 시 14b·프롬프트 강화.
- **malformed JSONL**: LLM이 스키마 이탈 → 검증기가 차단, 재생성. 프롬프트에 "JSONL only" 강제 유지.
- **쿼터 미충족**(밴드/타입 분포): 검증기가 잡음 → 부분 재생성으로 채움.
- **CODE_READING 코드 정확성**: 코드 스니펫이 실제로 동작/의미가 맞는지 — 스팟체크 대상.

## 6. 순서 / 롤아웃

1. Ollama 로컬 셋업(설치·pull).
2. 질문 프롬프트 재작성 → 생성 → 검증 → 승인 → 로컬 재시드 → 진단 실증.
3. 콘텐츠 프롬프트 → 생성 → 검증 → 승인 → 임베딩 → 로컬 반영.
4. (후속) 운영 반영 마이그레이션은 GPU Phase B 재배포 때 별도.

## 7. 영향 범위

| 레포 | 변경 |
|---|---|
| devpath-learning-svc | `tools/content-gen/prompts/*` 재작성(주 변경) · `generated/approved/*.jsonl` 재생성 · (필요 시) 쿼터/검증 미세조정 |
| devpath-shared | (후속) 한국어 시드 마이그레이션 — 이 워크스트림 밖 |
