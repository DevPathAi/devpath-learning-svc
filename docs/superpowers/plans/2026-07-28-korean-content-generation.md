# 한국어 진단 콘텐츠 생성 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 로컬 Ollama(GPU)로 실제 의미 있는 한국어 진단 문항(500)·학습 콘텐츠(150)를 생성·검증하고, 로컬 DB에 재시드해 진단이 한국어 실물을 반환하게 한다(현재는 영어 템플릿 필러).

**Architecture:** 기존 `tools/content-gen` 파이프라인을 재사용한다. 유일한 실질 변경은 **프롬프트(영어·포맷규칙 → 한국어·고품질)**. gradle JavaExec 태스크로 생성→검증→시드SQL을 돌린다. 생성물(raw)을 큐레이션해 `approved/*.jsonl`로 확정하고, 검증 그린이 승인 게이트다. 시드 SQL은 `makeQuestionSeedSql`이 `src/main/resources/db/seed/*.sql`(dev 시더가 로드)에 기록한다.

**Tech Stack:** Java 21, Gradle(Kotlin DSL), Ollama(로컬, `qwen2.5:7b`·`nomic-embed-text`), PostgreSQL 17+pgvector(devpath-shared docker-compose), JSON Schema.

## Global Constraints

- **로컬 Ollama**: `OLLAMA_BASE_URL=http://localhost:11434`. GPU=RTX 2080 Ti(11GB). 모델 `qwen2.5:7b`(부족 시 `qwen2.5:14b`), 임베딩 `nomic-embed-text`.
- **파이프라인 명령**(learning-svc 루트에서): `./gradlew generateQuestionsLocal -Pollama.model=qwen2.5:7b` · `./gradlew validateQuestions` · `./gradlew makeQuestionSeedSql` · `./gradlew generateContentsLocal` · `./gradlew validateContents` · `./gradlew makeContentSeedSql` · `./gradlew embedContentsLocal`. (Windows는 `gradlew.bat` 또는 Git Bash `./gradlew`.)
- **생성기 입출력**: `generateQuestionsLocal` → `tools/content-gen/generated/raw/questions.draft.jsonl`. `validateQuestions`·`makeQuestionSeedSql`는 `tools/content-gen/generated/approved/questions.jsonl`을 소비. `makeQuestionSeedSql`은 시드 SQL을 `tools/content-gen/generated/seeds/question_bank_seed.sql`·`src/main/resources/db/seed/question_bank_md2_seed.sql`·`src/test/resources/seed/question_bank_md2_seed.sql`·`src/test/resources/seed/question_bank_seed.sql`에 기록.
- **문항 스키마**(`tools/content-gen/schemas/question.schema.json`, `additionalProperties:false`): `track`(enum: BACKEND_SPRING·FRONTEND_REACT·MOBILE_FLUTTER·DEVOPS·FULLSTACK), `questionType`(MCQ|CODE_READING), `content`(string≥1), `options`(array≥2 of string≥1), `answerKey`{`correct`:int≥0}, `bloomLevel`(REMEMBER|UNDERSTAND|APPLY|ANALYZE|EVALUATE), `difficulty`(0–1), `conceptTags`(array≥1, `^[a-z0-9]+(?:-[a-z0-9]+)*$`), `explanation`(required).
- **쿼터**(`QuestionQuota`, 검증기 강제): 5트랙 × **100** = 500. 타입 MCQ 70/CODE_READING 30. Bloom REMEMBER10/UNDERSTAND25/APPLY30/ANALYZE25/EVALUATE10. 난이도밴드 0.1–0.2:10 / 0.3–0.4:25 / 0.5–0.6:30 / 0.7–0.8:25 / 0.9:10.
- **로컬 Postgres**: `cd devpath-shared && docker compose up -d`(pgvector/pgvector:pg17, user `devpath`/pw `localdev`/db `devpath`, 5432).
- **브랜치**: `feat/korean-content-generation`(devpath-shared 아님, devpath-learning-svc). **main·develop 직접 커밋 금지.** Conventional Commits.
- **품질**: 제네릭 템플릿 금지. 실제 기술 개념을 묻는 자연스러운 한국어. 정답키(0-based) 정확.
- **쿼터 난점 fallback**: 정확 분포 충족이 과도하게 어려우면 `QuestionQuota` 상수(PER_TRACK/밴드)를 하향 조정하되, 조정 시 검증기 테스트도 함께 갱신(별도 커밋). 스펙 §3.4의 "축소 후퇴" 경로.

---

### Task 1: 로컬 Ollama 셋업 + 검증

**Files:** (환경 셋업 — 코드 변경 없음)

**Interfaces:**
- Produces: `localhost:11434`에서 서빙되는 Ollama + `qwen2.5:7b`·`nomic-embed-text` 모델(Task 3·6이 소비).

- [ ] **Step 1: Ollama 설치**

Run: `winget install --id Ollama.Ollama -e` (또는 https://ollama.com/download 인스톨러). 설치 후 새 셸에서 `ollama --version` 확인.

- [ ] **Step 2: 서빙 + 모델 pull**

Run: `ollama pull qwen2.5:7b` 및 `ollama pull nomic-embed-text` (Ollama 데스크톱이 자동 serve; 아니면 `ollama serve` 별도 실행).
Expected: `ollama list`에 두 모델 표시.

- [ ] **Step 3: GPU + 한국어 생성 스모크**

Run: `ollama run qwen2.5:7b "한 문장으로 자기소개해줘"` (응답이 한국어면 OK). GPU 사용 확인: 생성 중 `nvidia-smi`에 ollama 프로세스 VRAM 점유.
Expected: 빠른 한국어 응답(GPU). CPU-only면 느림 → GPU 드라이버/Ollama CUDA 확인.

- [ ] **Step 4: API 확인**

Run: `curl -s http://localhost:11434/api/tags` → `qwen2.5:7b`·`nomic-embed-text` 포함.

---

### Task 2: 문항 프롬프트 한국어·고품질 재작성 + 소표본 검증

**Files:**
- Modify: `tools/content-gen/prompts/question-system.md`
- Modify: `tools/content-gen/prompts/tracks/backend-spring.md`·`frontend-react.md`·`mobile-flutter.md`·`devops.md`·`fullstack.md`

**Interfaces:**
- Consumes: Task 1 Ollama.
- Produces: 한국어 고품질 문항을 유도하는 프롬프트(Task 3이 소비).

- [ ] **Step 1: `question-system.md` 재작성(한국어)**

기존 영어 포맷규칙을 유지하되 한국어로, 아래를 명시:
- "당신은 DevPath AI 실력진단 문항을 **한국어로** JSONL로 생성한다. 한 줄에 JSON 하나, JSONL만 출력(설명·마크다운·코드펜스 금지)."
- 스키마 필드 정확 명시(Global Constraints의 question.schema.json 필드·타입·enum 그대로). `additionalProperties` 없음 — 정의된 9개 필드만.
- 품질 규칙: "제네릭 템플릿('~에 가장 잘 적용되는 옵션은?') 금지. 실제 기술 개념·상황을 구체적으로 묻는다. 보기 4개는 그럴듯한 오답 포함, 정답은 하나. `answerKey.correct`는 정답 보기의 0-based 인덱스. `explanation`은 왜 정답인지 한국어 1–2문장."
- `conceptTags`는 소문자 kebab-case(영문 개념 슬러그 허용, 예 `spring-transaction`). `content`·`options`·`explanation`은 한국어.
- MCQ/CODE_READING·bloomLevel·difficulty(0–1) 규칙 유지. CODE_READING은 `content`에 코드 스니펫(마크다운 코드블록 대신 `\n` 포함 문자열) 포함하고 "이 코드의 동작/문제는?"류.

- [ ] **Step 2: 트랙 프롬프트 5개 재작성(한국어·구체 개념)**

각 `tracks/<track>.md`에 트랙별 **실제 개념 목록 + 예시 시나리오**를 한국어로. 예:
- `backend-spring.md`: "Spring Boot 자동설정, `@Transactional` 전파/격리, JPA 영속성 컨텍스트·N+1, Kafka 컨슈머 그룹/리밸런싱, 캐시 추상화, 동시성(락·원자성), PostgreSQL 인덱스/트랜잭션. CODE_READING은 위 주제의 짧은 Java/Kotlin 스니펫."
- 나머지 트랙(frontend-react/mobile-flutter/devops/fullstack)도 해당 도메인의 실제 핵심 개념으로 동일 형식.

- [ ] **Step 3: 소표본 생성(한 트랙)으로 프롬프트 검증**

Run: `OLLAMA_BASE_URL=http://localhost:11434 ./gradlew generateQuestionsLocal -Pollama.model=qwen2.5:7b`
(전체가 돌지만 시간 확인용 — 또는 임시로 한 트랙만 두고 실행) → `tools/content-gen/generated/raw/questions.draft.jsonl` 확인.
Expected: 각 줄이 유효 JSON, `content`/`options`/`explanation`이 **한국어**, 제네릭 템플릿이 아님. 아니면 Step 1–2 프롬프트 보정 후 재생성.

- [ ] **Step 4: 커밋(프롬프트)**

```bash
git add tools/content-gen/prompts/
git commit -m "feat(content): 문항 생성 프롬프트 한국어·고품질 재작성"
```

---

### Task 3: 전체 문항 생성 → 큐레이션(approved) → 검증 그린

**Files:**
- Create/Modify: `tools/content-gen/generated/approved/questions.jsonl` (승인본)

**Interfaces:**
- Consumes: Task 2 프롬프트, Task 1 Ollama.
- Produces: 쿼터·스키마 충족 `approved/questions.jsonl`(Task 4가 소비).

- [ ] **Step 1: 전체 생성**

Run: `OLLAMA_BASE_URL=http://localhost:11434 ./gradlew generateQuestionsLocal -Pollama.model=qwen2.5:7b`
→ `generated/raw/questions.draft.jsonl`. (부족·저품질 트랙은 프롬프트 보정 후 재실행.)

- [ ] **Step 2: raw → approved 큐레이션**

raw에서 유효 JSON 줄만 추출(모델이 프롬프트를 프롤로그로 붙일 수 있음):
Run(예): `grep -E '^\{' tools/content-gen/generated/raw/questions.draft.jsonl > tools/content-gen/generated/approved/questions.jsonl`
그다음 눈으로 훑어 명백한 오류(정답키 틀림·비한국어·깨진 JSON) 줄 제거/수정. 쿼터(트랙당 100, 타입/Bloom/난이도 분포)를 맞추도록 부족분은 프롬프트 조정 후 재생성해 채운다.

- [ ] **Step 3: 검증(승인 게이트)**

Run: `./gradlew validateQuestions`
Expected: BUILD SUCCESSFUL, `ERROR` 없음(경고는 허용). 실패 시 에러(쿼터 미달·스키마 위반·정답키 범위)를 보고 approved를 보정/재생성 후 재검증. **그린일 때만 다음.**

- [ ] **Step 4: 트랙별 스팟체크(사용자)**

각 트랙 3–5문항을 사람이 확인(정답 정확·개념 실재·한국어 자연). 문제 있으면 해당 줄 수정 후 `validateQuestions` 재실행.

- [ ] **Step 5: 커밋(승인 문항)**

```bash
git add tools/content-gen/generated/approved/questions.jsonl
git commit -m "feat(content): 한국어 진단 문항 500 생성·검증(approved)"
```

---

### Task 4: 문항 시드 SQL 생성 + 로컬 재시드 + 진단 한국어 실증

**Files:**
- Generate(도구 산출): `src/main/resources/db/seed/question_bank_md2_seed.sql` 외 4곳
- Test(add): `src/test/java/ai/devpath/learning/seed/QuestionBankKoreanSeedTest.java`

**Interfaces:**
- Consumes: Task 3 `approved/questions.jsonl`.
- Produces: 한국어 문항이 적재된 로컬 `question_bank`.

- [ ] **Step 1: 한국어 회귀 테스트 작성(실패 확인)**

`QuestionBankKoreanSeedTest.java` — 시드 SQL 파일에 한글이 포함되는지 단언:

```java
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
```

Run: `./gradlew test --tests "ai.devpath.learning.seed.QuestionBankKoreanSeedTest"`
Expected: FAIL(현재 시드는 영어) — 단, Task는 아직 시드를 재생성 안 했으므로 이 시점 FAIL이 정상.

- [ ] **Step 2: 시드 SQL 생성**

Run: `./gradlew makeQuestionSeedSql`
→ `src/main/resources/db/seed/question_bank_md2_seed.sql`(+ 4곳) 재생성.

- [ ] **Step 3: 한국어 회귀 테스트 통과 확인**

Run: `./gradlew test --tests "ai.devpath.learning.seed.QuestionBankKoreanSeedTest"`
Expected: PASS(시드에 한글 포함).

- [ ] **Step 4: 로컬 DB 재시드**

Run: `cd ../devpath-shared && docker compose up -d` (Postgres 기동). 그다음 새 시드 적재:
Run: `PGPASSWORD=localdev psql -h localhost -U devpath -d devpath -c "TRUNCATE question_bank RESTART IDENTITY CASCADE;" -f ../devpath-learning-svc/src/main/resources/db/seed/question_bank_md2_seed.sql`
(psql 미설치면 docker exec 경유: `docker exec -i <postgres_container> psql -U devpath -d devpath < ...`.)
Expected: `SELECT count(*) FROM question_bank;` = 500.

- [ ] **Step 5: 진단 한국어 실증**

learning-svc를 `dev` 프로파일로 로컬 기동(포트 8080; 의존 인프라 필요분만) 또는 게스트 진단 로직 테스트로 문항 반환 확인. 최소 검증: DB에서 표본 조회 —
Run: `PGPASSWORD=localdev psql -h localhost -U devpath -d devpath -tAc "SELECT content FROM question_bank WHERE track='BACKEND_SPRING' LIMIT 3;"`
Expected: 한국어 실제 문항 3개.

- [ ] **Step 6: 커밋**

```bash
git add src/main/resources/db/seed/question_bank_md2_seed.sql src/test/resources/seed/ tools/content-gen/generated/seeds/question_bank_seed.sql src/test/java/ai/devpath/learning/seed/QuestionBankKoreanSeedTest.java
git commit -m "feat(content): 한국어 문항 시드 SQL 재생성 + 한글 회귀 테스트"
```

---

### Task 5: 콘텐츠 프롬프트 재작성 + 생성 → 큐레이션 → 검증 그린

**Files:**
- Modify: `tools/content-gen/prompts/content-system.md` (+ 콘텐츠 트랙 프롬프트 존재 시)
- Create/Modify: `tools/content-gen/generated/approved/contents.jsonl`

**Interfaces:**
- Consumes: Task 1 Ollama.
- Produces: 검증 통과 `approved/contents.jsonl`(Task 6이 소비).

- [ ] **Step 1: 콘텐츠 스키마·검증 확인**

Run: `cat tools/content-gen/schemas/content.schema.json` 및 `ContentQuota` 확인(트랙당 30·필드: slug/title/track/content_md/estimated_minutes/difficulty/bloom_level/concept_tags/status 대응). 프롬프트 요구사항을 스키마에 정렬.

- [ ] **Step 2: `content-system.md` 재작성(한국어)**

학습 콘텐츠를 **한국어 `content_md`**(마크다운 본문)로 생성하도록: 실제 개념 설명·예시, 트랙·난이도·bloom·concept_tags(kebab-case), slug(영문 kebab), title(한국어). JSONL only.

- [ ] **Step 3: 생성**

Run: `OLLAMA_BASE_URL=http://localhost:11434 ./gradlew generateContentsLocal -Pollama.model=qwen2.5:7b`
→ raw 드래프트. (경로는 `GenerateContentsCommand` 산출 위치 확인.)

- [ ] **Step 4: 큐레이션 → approved + 검증**

raw 유효 줄 추출 → `tools/content-gen/generated/approved/contents.jsonl`. 그다음:
Run: `./gradlew validateContents`
Expected: BUILD SUCCESSFUL, ERROR 없음. 실패분 보정/재생성.

- [ ] **Step 5: 커밋**

```bash
git add tools/content-gen/prompts/content-system.md tools/content-gen/generated/approved/contents.jsonl
git commit -m "feat(content): 한국어 학습 콘텐츠 150 생성·검증(approved)"
```

---

### Task 6: 콘텐츠 시드 SQL + 임베딩 + 로컬 재시드

**Files:**
- Generate: `src/main/resources/db/seed/content_md2_seed.sql`
- Create/Modify: `tools/content-gen/generated/approved/content_embeddings.jsonl`

**Interfaces:**
- Consumes: Task 5 `approved/contents.jsonl`, Task 1 `nomic-embed-text`.
- Produces: 한국어 contents + content_embeddings(로컬).

- [ ] **Step 1: 임베딩 생성**

Run: `OLLAMA_BASE_URL=http://localhost:11434 ./gradlew embedContentsLocal -Pollama.embedModel=nomic-embed-text`
→ `tools/content-gen/generated/approved/content_embeddings.jsonl`.
Expected: `validateContents`류 임베딩 검증 통과(차원 일치). (`EmbeddingValidator` 존재.)

- [ ] **Step 2: 콘텐츠 시드 SQL 생성**

Run: `./gradlew makeContentSeedSql`
→ `src/main/resources/db/seed/content_md2_seed.sql`(+ 테스트 사본).

- [ ] **Step 3: 로컬 재시드 + 검증**

Run: `PGPASSWORD=localdev psql -h localhost -U devpath -d devpath -c "TRUNCATE contents, content_embeddings RESTART IDENTITY CASCADE;" -f src/main/resources/db/seed/content_md2_seed.sql`
Expected: `SELECT count(*) FROM contents;` = 150, `SELECT count(*) FROM content_embeddings;` > 0.
Run: `PGPASSWORD=localdev psql -h localhost -U devpath -d devpath -tAc "SELECT title FROM contents LIMIT 3;"` → 한국어 title.

- [ ] **Step 4: 전체 빌드·테스트**

Run: `./gradlew build`
Expected: BUILD SUCCESSFUL(한글 회귀 테스트 포함 그린).

- [ ] **Step 5: 커밋 + 브랜치 마무리**

```bash
git add src/main/resources/db/seed/content_md2_seed.sql src/test/resources/seed/ tools/content-gen/generated/
git commit -m "feat(content): 한국어 콘텐츠 시드 + 임베딩 재생성"
git push -u origin feat/korean-content-generation
```
그다음 `develop` PR 생성(운영 반영 마이그레이션은 GPU Phase B 재배포 때 별도).

---

## Self-Review

**1. Spec coverage:** 스펙 §3.1(셋업)=Task1, §3.2(프롬프트)=Task2·5, §3.3(생성→검증→승인→시드→콘텐츠+임베딩)=Task3·4·6, §3.4(모델/스코프/검토)=Global+Task3 Step4, §4(테스트: 검증게이트·한글단언·진단실증)=Task3 Step3·Task4 Step1/5, §6 순서 일치. ✅

**2. Placeholder scan:** 명령·경로·스키마·쿼터 전부 실측 값. 프롬프트 "본문 작성"은 요구사항을 구체 명시(제네릭 금지·한국어·정답키·스키마 9필드)했고 실제 문안은 구현 산출물. 쿼터 fallback 경로 명시. TBD 없음. ✅

**3. Type consistency:** gradle 태스크명(generateQuestionsLocal/validateQuestions/makeQuestionSeedSql/generateContentsLocal/validateContents/makeContentSeedSql/embedContentsLocal)·파일 경로·스키마 필드가 build.gradle.kts·schema 실측과 일치. 시드 SQL 경로(`src/main/resources/db/seed/question_bank_md2_seed.sql`)가 Task4·6과 QuestionBankSeeder 로드 경로 일치. ✅

**4. 리스크:** 소형모델 품질·쿼터 정확분포 난점을 Task3 큐레이션 루프 + Global fallback(쿼터 하향)으로 흡수. CODE_READING 코드 정확성은 스팟체크. ✅
