# 핸드오프 — 학습 문서 지식베이스 + 멘토 RAG 1단계, Task 0~4 완료

> 작성 2026-08-08
> 스펙: `docs/superpowers/specs/2026-08-08-knowledge-base-mentor-rag-design.md` (`7f17b2e`)
> 계획: `docs/superpowers/plans/2026-08-08-knowledge-base-mentor-rag.md` (`d39e7fd`)
> 원장: `.superpowers/sdd/2026-08-08-knowledge-base-mentor-rag/progress.md` (**git-ignored**, 세션 복구용)
> 이전 핸드오프: `devpath-frontend/docs/superpowers/handoff-2026-08-08-design-phase3b-complete-followups-open.md`

## 0. 지금 상태

**`develop-study-documents`의 학습 문서 743개를 지식베이스로 적재해 AI 멘토가 근거로 답하게 하는 작업.**
전체 9개 Task 중 **Task 0~4 완료, Task 5부터 남았다.**

| Task | 상태 | 결과 |
|---|---|---|
| 0. 문서 스냅샷 확정 (dsd) | ✅ | PR #9 머지(`89e2639`). AWS 환각 교정 21건 반영 |
| 1. 지식베이스 스키마 (shared) | ✅ | PR #55 머지(`6bfd17a`) + **발행 완료** |
| 2. `ContentChunker` 일반화 | ✅ | `98918a2` |
| 3. 배치 임베딩 클라이언트 | ✅ | `1fa9a9f` |
| 4. 문서 스캐너 | ✅ | `76598cf` + fix `b5cce2d` |
| **5. 청킹+배치 임베딩 커맨드** | ⬅ **다음** | 브리프 생성돼 있음 |
| 6. 적재 커맨드 | 대기 | |
| 7. 검색 API | 대기 | |
| 8. 멘토 프롬프트 주입 | 대기 | **목적 ①의 핵심** |
| 9. 실행·실증 | 대기 | |

### 레포별 브랜치 (전부 푸시 안 된 로컬 커밋 있음 — 주의)

| 레포 | 절대경로 | 브랜치 | 상태 |
|---|---|---|---|
| dsd (문서 소스) | `D:\workspace\dsd` | `master` | origin 동기. **스냅샷 SHA `89e263935d66d14f59e22f4d764f1906c8983811`** |
| devpath-shared | `D:\workspace\dpa\devpath-shared` | `develop` | origin 동기. 발행 완료 |
| **devpath-learning-svc** | `D:\workspace\dpa\devpath-learning-svc` | **`feat/knowledge-base-pipeline`** | **로컬 커밋 5개, 미푸시. PR 없음** |
| devpath-ai-svc | `D:\workspace\dpa\devpath-ai-svc` | `feat/mentor-ollama-fallback` | 이미 develop 머지된 옛 브랜치. Task 8에서 `origin/develop`에서 새로 분기할 것 |

`feat/knowledge-base-pipeline`에는 스펙·계획 문서까지 함께 담겨 있다(리뷰 시 함께 보게 하려는 의도).

## 1. ★실측이 뒤집은 것 — 이번 세션의 핵심 자산★

### 1.1 현재 멘토의 RAG는 "무늬만 RAG"다 (설계 단계 발견)

`MentorService.java:35-46`이 검색 결과를 이렇게 쓴다.

```java
List<SimilarContent> refs = referenceService.find(question, ctx.track());
emitter.send(SseEmitter.event().name("references")...);              // ① SSE로 목록만 전송
mentorClient.stream(new MentorInput(question, ctx.promptText()), ...); // ② refs가 프롬프트에 없다
```

검색 결과는 **SSE 이벤트로 나갈 뿐 LLM 프롬프트에 들어가지 않는다.** 게다가
`SimilarContent(contentId, slug, title)`에는 **본문이 없다.** 그래서 지식베이스만 추가했다면
"멘토는 여전히 문서 내용을 모르고, 화면엔 열 수도 없는 비공개 문서 제목만 뜨는" 결과가 됐을 것이다.
**Task 8이 이 구조를 바꾸는 것이 이 프로젝트의 핵심이다.**

### 1.2 배치 임베딩이 39배 (설계 단계 실측)

| 방식 | 청크당 | 19,109청크 환산 |
|---|---|---|
| 단건 `/api/embeddings`(구형) | 2,284ms | **12.1시간** |
| 배치 10개 | 247ms | 79분 |
| **배치 50개 `/api/embed`** | **58ms** | **약 19분** |

**배치는 선택이 아니라 전제다.** 청크 수도 추정 8~9천의 두 배인 **19,109개**였다.

### 1.3 ★CRLF 오염 — 리뷰어의 "낮은 가능성"을 실측이 뒤집었다★ (Task 4)

`dsd` 레포가 git `core.autocrlf`로 **CRLF로 체크아웃**돼 있었다. 스캐너가 원문을 그대로 담아
**743개 중 626개 문서의 `markdown`에 `\r\n`이 실렸고**, 총 문자 수가 16,803,741자로
LF 기준(16,372,359자)보다 **431,382자 부풀어** 있었다.

리뷰어도 이 위험을 잡아냈지만 **"Low probability"**로 판정하고 **해시 계산 시점만 정규화**하자고 제안했다.
둘 다 틀렸다:

- **가능성이 아니라 현실이었다** — 컨트롤러가 `documents.jsonl`을 직접 세어 626개를 확인했다.
- **해시만 고치면 부족하다** — 청킹은 여전히 CRLF 텍스트로 이뤄져 `MAX_CHARS=1200` 경계가
  왜곡되고, `chunk_text`에 `\r`이 실린 채 임베딩돼 **멘토 프롬프트까지 흘러간다.**

→ **읽는 시점에 정규화**하도록 수정(`b5cce2d`). 검증: 재스캔 총 문자 수가 **16,372,359로 LF
실측치와 정확히 일치**, CR 포함 문서 **626 → 0**, 문서 수 743 유지, 테스트 9건 통과.

**교훈: 리뷰어의 severity 판정은 근거가 "가능성"일 때 실측으로 확인해야 한다.**

### 1.4 계획 자체 검토가 빌드 실패를 미리 막았다

계획을 쓴 뒤 스스로 점검해 3건을 잡았다. 그중 하나는 실제 빌드를 깨뜨렸을 것이다.

- `knowledgeGen` 소스셋이 `contentGen`의 `ContentChunker`를 쓰는데 **클래스패스에 `contentGenSourceSet.output`이 없었다**
- `ApprovedContent`는 **10개 필드**인데 테스트 예제가 8인자였다
- Task 9에 서버 기동 대기가 없어 "검색 결과 없음"으로 오진할 수 있었다

## 2. ★다음 세션이 반드시 알아야 할 함정★

### 2.1 `MentorInput`은 record — 필드를 더하면 생성자가 깨진다 (Task 8)

`new MentorInput(q, ctx)` **2-인자 호출이 프로덕션 1곳 + 테스트 5곳**에 있다:
`MentorService.java:39` · `GoldenMentorInjectionEvalTest.java:40` · `FallbackMentorClientTest.java:32`
· `MentorPromptBuilderTest.java:22` · `MockMentorClientTest.java:16` · `OllamaMentorClientTest.java:37`

3-B에서 `ActivePathCompletions` 확장이 테스트 3곳을 컴파일 불가로 만든 것과 **같은 함정**이다.
계획 Task 8에 **2-인자 보조 생성자를 남기도록** 명시해 뒀다. 그대로 따를 것.

### 2.2 서브에이전트의 최종 메시지가 잘려서 전달된다

이 하네스에서 서브에이전트가 반환하는 마지막 메시지가 `"Idle."`, `"Approved."` 처럼 **잘려서 온다.**
보고서 파일은 정상적으로 쓰인다. 따라서:

- **모든 서브에이전트에게 "결과를 파일에 완전히 기록하라. 최종 회신은 잘린다"고 지시할 것**
- 리뷰어도 마찬가지 — 리뷰 결과 파일 경로를 지정해야 한다(안 했다가 Task 2 리뷰를 한 번 잃고
  `SendMessage`로 재요청했다)

### 2.3 gradle `BUILD SUCCESSFUL in 2s`는 실행됐다는 뜻이 아니다

- 테스트: 필터가 0건을 잡아도 성공한다. **`build/test-results/test/TEST-*.xml`의
  `tests="N" failures="0"`을 눈으로 볼 것.** 확실히 하려면 `--rerun-tasks`.
- `scanKnowledgeDocs` 같은 JavaExec도 up-to-date로 스킵될 수 있다. 실제 실행 여부는
  **산출물 타임스탬프**로 판별한다.

### 2.4 콘솔 한글이 깨진다

gradle/Java 출력의 한글이 `????`·`Ŭ����`로 깨져 보인다. 값 자체는 정상이다.
**`grep`으로 한글을 찾지 말고**, JSONL은 `py -c`로 파싱해 검증할 것(이번에 그렇게 확인했다).

### 2.5 로컬 테스트 전제

- **Redis가 없으면 3건 실패**한다(헬스체크 503). DB만으로는 부족하다.
- **전용 DB를 쓸 것**: `export DB_URL=jdbc:postgresql://localhost:5432/devpath_kbtest`.
  공유 `devpath` DB로 반복 실행하면 `FlywayMigrationTest`가 깨진다.
- 현재 로컬에 `devpath-local-postgres-1`(pgvector/pg17) · `devpath-local-redis-1` 가동 중.
- **`devpath_kbtest` DB에는 Task 1 마이그레이션이 수동 적용돼 있다**(스키마 검증용으로 컨트롤러가
  직접 넣음). Task 6 테스트가 이 DB를 쓴다.

## 3. 다음 세션 시작 방법

```bash
# 1) 상태 확인
git -C D:/workspace/dpa/devpath-learning-svc log --oneline -5     # b5cce2d가 HEAD
cat D:/workspace/dpa/devpath-learning-svc/.superpowers/sdd/2026-08-08-knowledge-base-mentor-rag/progress.md

# 2) 전제 확인
docker ps                                    # postgres·redis Up
curl -s http://localhost:11434/api/tags      # Ollama
```

**Task 5부터 이어간다.** 브리프가 이미 생성돼 있다:
`.superpowers/sdd/2026-08-08-knowledge-base-mentor-rag/task-5-brief.md`

실행 방식은 **subagent-driven-development**를 계속 쓴다(스킬 절차는 `superpowers:subagent-driven-development`).
Task별로 브리프 생성 → implementer dispatch → 컨트롤러 직접 검증 → 리뷰어 dispatch → 완료 기록.

```bash
SKILL="C:/Users/deepe/.claude/plugins/cache/claude-plugins-official/superpowers/6.2.0/skills/subagent-driven-development"
PLAN="/d/workspace/dpa/devpath-learning-svc/docs/superpowers/plans/2026-08-08-knowledge-base-mentor-rag.md"
cd /d/workspace/dpa/devpath-learning-svc && "$SKILL/scripts/task-brief" "$PLAN" 5
```

> **주의:** `task-brief`·`review-package`는 **cwd의 git root**에 파일을 쓴다. 반드시
> `cd /d/workspace/dpa/devpath-learning-svc`를 먼저 할 것(한 번 ai-svc 아래에 생겨 옮겼다).

### 3.1 Task 5 실행 시 쓸 값

```bash
./gradlew embedKnowledge -PsourceCommit=89e263935d66d14f59e22f4d764f1906c8983811
```

- 예상 청크 **19,109개**, 소요 **약 19분**(RTX 2080 Ti + `nomic-embed-text`)
- 산출물 `tools/knowledge-gen/generated/embeddings.jsonl` 약 **164MB** — gitignore돼 있다
- 중단되면 **같은 명령을 다시 실행**하면 `doc_hash` 체크포인트로 이어서 간다

### 3.2 남은 Task의 순서와 의존

```
Task 5 (임베딩) → Task 6 (적재) → Task 7 (검색 API) → Task 8 (멘토 주입) → Task 9 (실증)
```

Task 8만 `devpath-ai-svc` 레포다. **`origin/develop`에서 `feat/mentor-knowledge-injection`을 새로 분기**할 것
(현재 체크아웃된 `feat/mentor-ollama-fallback`은 이미 머지된 옛 브랜치다).

## 4. 미해결 · 이월

| 항목 | 내용 |
|---|---|
| **learning-svc PR 미생성** | `feat/knowledge-base-pipeline` 로컬 커밋 5개가 미푸시다. Task 7 끝에 PR을 만들도록 계획돼 있다 |
| Task 4 scoped re-review 미실시 | fix 후 형식상 재리뷰를 돌리지 않았다. 대신 컨트롤러가 직접 검증(테스트 9건·문자수 일치·CR 0). 다음 세션이 문제 삼지 않아도 된다 |
| minor (deferred) | `title` 500자 절단 경로에 테스트 없음 (Task 4 리뷰 Minor #1). 최종 whole-branch 리뷰에서 triage할 것 |
| Sample Codes 비중 | 전체 청크의 25%(4,853개). 개념 질문을 밀어낼 수 있으나 **가중치를 미리 넣지 않기로 결정**했다. Task 9 Step 8에서 실측 관찰 후 판단한다 |
| 2단계(③콘텐츠 생성) | 지식베이스를 재료로 학습 콘텐츠·문항을 생성하는 별도 스펙. 1단계 완료 후 착수 |

## 5. 검증 명령 모음

```bash
R=D:/workspace/dpa/devpath-learning-svc

# 테스트 (전용 DB + 강제 재실행 + 실제 건수 확인)
export DB_URL=jdbc:postgresql://localhost:5432/devpath_kbtest
cd $R && ./gradlew test --tests '*Knowledge*' --rerun-tasks
grep -o 'tests="[0-9]*" failures="[0-9]*"' $R/build/test-results/test/TEST-*.xml

# 스캔 결과 검증 (한글 깨짐 회피 — py로 파싱)
py -c "
import json
p=r'D:\workspace\dpa\devpath-learning-svc\tools\knowledge-gen\generated\documents.jsonl'
ds=[json.loads(l) for l in open(p,encoding='utf-8') if l.strip()]
print(f'문서 {len(ds)}개 / 문자 {sum(len(d[\"markdown\"]) for d in ds):,}')
print('CR 포함:', sum(1 for d in ds if chr(13) in d['markdown']))
"
# 기대: 문서 743개 / 문자 16,372,359 / CR 포함 0
```

## 6. 이번 세션 산출 커밋

| 레포 | 커밋 | 내용 |
|---|---|---|
| dsd | `222537a` · `1a99ca0` → merge `89e2639` | AWS 환각 교정 21파일 / 에이전트 설정 94파일 |
| devpath-shared | `7590800` → merge `6bfd17a` | `V202608081001__knowledge_base.sql` |
| learning-svc | `7f17b2e` | 설계 스펙 |
| learning-svc | `d39e7fd` | 구현 계획 |
| learning-svc | `98918a2` | ContentChunker 일반화 |
| learning-svc | `1fa9a9f` | 배치 임베딩 클라이언트 |
| learning-svc | `76598cf` | 문서 스캐너 |
| learning-svc | `b5cce2d` | CRLF → LF 정규화 fix |
