# 핸드오프 — 학습 문서 지식베이스 + 멘토 RAG 1단계 **전 과정 완결**

> 작성 2026-08-09
> 스펙: `docs/superpowers/specs/2026-08-08-knowledge-base-mentor-rag-design.md`
> 계획: `docs/superpowers/plans/2026-08-08-knowledge-base-mentor-rag.md`
> 실증 보고서: `docs/superpowers/reports/2026-08-08-knowledge-base-load-report.md`
> 이전 핸드오프: `handoff-2026-08-08-knowledge-base-task0-4-done.md`

## 0. 지금 상태

**Task 0~9 전부 완료. PR 3건 전부 develop 머지.**

| Task | 상태 | 결과 |
|---|---|---|
| 0 문서 스냅샷(dsd) | ✅ | PR #9 머지 `89e2639` |
| 1 스키마(shared) | ✅ | PR #55 머지 + 발행 |
| 2~7 파이프라인·검색 API | ✅ | **learning-svc PR #44 머지** `658c7d0` |
| — 서로게이트 결함 수정 + 실증 보고서 | ✅ | **learning-svc PR #45 머지** `03d602d` |
| 8 멘토 프롬프트 주입 | ✅ | **ai-svc PR #34 머지** `47031c0` |
| 9 실행·실증 | ✅ | 아래 §2 |

로컬 `develop`은 두 레포 모두 origin과 동기. 실행에 쓴 서버는 전부 종료했다.

**목적 ①(멘토가 문서를 근거로 답한다)이 실증으로 확인됐다.** 다음은 2단계(③콘텐츠 생성) 또는 다른 영역이다.

## 1. 무엇이 만들어졌나

```
dsd 문서 743개
  → KnowledgeDocScanner        → documents.jsonl (16,372,359자, LF 정규화)
  → ContentChunker + 배치 임베딩 → embeddings.jsonl (19,115청크 × 768차원, 213MB)
  → KnowledgeLoader            → knowledge_documents 743 / knowledge_embeddings 19,115
  → POST /internal/knowledge/similar (chunkText 반환)
  → ai-svc KnowledgeClient → MentorInput.referenceDocs → <reference_docs> 프롬프트 블록
```

`src/knowledgeGen/`은 별도 gradle 소스셋(오프라인 배치 도구)이고, 런타임 서비스 코드는 `src/main/java/ai/devpath/learning/knowledge/`뿐이다.

**불변 계약 지켜짐:** `contents` · `content_embeddings` · `ContentEmbeddingMatcher` · `InternalSimilarService` · `/internal/contents/similar` 전부 변경 0.

## 2. ★실증 결과 — 대조군으로 주입 효과를 분리했다★

세부는 실증 보고서 §5에 있다. 핵심만:

첫 질문(EKS Pod Identity + Fargate)은 **모델이 이미 아는 사실이라 판별력이 없었다.** 근거 유무와 무관하게 같은 결론이 나왔다(다만 근거가 없을 때 「Fargate 프로드(프로덕션 옵티마이즈드 도커 컨테이너)」라는 없는 용어를 지어냈다). **그 질문 하나로 끝냈다면 "동작한다"는 결론을 근거 없이 내릴 뻔했다.**

코퍼스에만 있는 사실로 다시 물었다 — `MCP study` 문서의 프로젝트 고유 규칙:

| 조건 | 답변 |
|---|---|
| **근거 있음** | 가용 재고의 **50%**를 초과하면 승인 필요, 도구는 **`decrement_stock`** — 문서와 정확히 일치 |
| **근거 없음**(청크 INACTIVE) | "재고 수량이 일정 수준 이하로", "특정 금액 이상의 거래", 도구명 "Stock Deduction Confirmation"·"Inventory Adjustment Request" — **전부 환각** |

대조군은 `knowledge_embeddings.status`를 일시 `INACTIVE`로 내려 만들었고(검색 0건 확인), 실험 후 **19,115건 전량 `ACTIVE` 복구를 확인**했다.

## 3. ★실행이 드러낸 결함 — 계획도 리뷰도 놓쳤다★

### 3.1 청크 경계가 서로게이트 쌍을 쪼갠다 (PR #45)

첫 실행이 8,955청크에서 `MalformedInputException`으로 죽었다. **예외가 읽기가 아니라 쓰기(인코딩)에서 났다** — Java `String`이 UTF-8 인코딩에 실패하는 경우는 짝 깨진 서로게이트뿐이다.

`ContentChunker`가 UTF-16 코드 유닛 인덱스로 자르는데 `end`가 산술로 계산돼 이모지 한가운데 떨어졌다.

**실측 blast radius: 743문서 중 562개 · astral 문자 5,246자.** 문서 카테고리명 자체가 `🐤 Spring_Boot_4_*`다 — 드문 엣지 케이스가 아니라 코퍼스 대부분이다.

이전 세션의 CRLF 사건(743중 626개 오염)과 **같은 계열**이다: 텍스트 전처리의 "설마" 가정이 실측에서 대부분 뒤집혔다.

### 3.2 최종 리뷰의 두 지적이 실제로 값을 했다

- **I3(잘린 줄 방어)** — 리뷰어는 "한 줄이 11~13KB로 `BufferedWriter` 8KB 버퍼보다 커서 줄 쓰는 도중에도 잘릴 수 있다"며 Minor를 Important로 승격시켰다. **예측한 메커니즘(프로세스 죽음)은 틀렸지만 방어는 정확히 필요한 자리였다.** 이게 없었다면 재실행이 손상된 줄에서 계속 죽어 213MB 파일을 손으로 편집해야 했다.
- **I1(DB 자격증명)** — `LoadKnowledgeCommand`가 `DB_USERNAME`/기본 비번 `devpath`를 쓰는데 레포 관례는 `DB_USER`/`localdev`였다. **계획대로 실행했다면 19분 임베딩 직후 인증 실패로 막혔다.**
- **I4(`sourceCommit` fail-fast)** — 없었다면 19,115행이 전부 `"unknown"`이 되고, **정상 종료라 눈치채지 못했을 것이다.**

### 3.3 그 I4 수정이 다시 `gradle tasks`를 깼다

`tasks.register {}`의 구성 람다는 **realize 시점에 통째로 평가**된다. `?: throw GradleException(...)`을 람다 안에 두니 `./gradlew tasks`가 `Could not create task ':embedKnowledge'`로 실패했다. 재리뷰어는 "지연 구성이라 무관한 명령엔 영향 없다"고 **코드를 읽고** ADDRESSED 판정했지만, **명령 한 줄 실행이 그것을 뒤집었다.** → `doFirst {}`로 이동.

**교훈: 리뷰어의 근거가 「~하므로 ~일 것이다」일 때, 명령 하나로 확인할 수 있으면 확인한다.**

## 4. 다음 세션이 반드시 알아야 할 실행 함정

1. **★`MENTOR_PROVIDER` 기본값이 `mock`이다★** — 그냥 `bootRun`하면 멘토가 `MockMentorClient`의 고정 문구(「그 질문에 답하면, 비동기는 Future/async/await로 다룹니다」)를 반환한다. **주입 실패로 오진하기 딱 좋다.** 실증에는 `MENTOR_PROVIDER=ollama MENTOR_OLLAMA_MODEL=qwen2.5:14b` 필수. `MENTOR_FALLBACK`은 비워 둘 것 — mock 폴백이 붙으면 실패가 조용히 가려진다.
2. **★서버 종료가 java 프로세스를 남긴다★** — 재기동이 `Port 8080 was already in use`로 죽었는데도 헬스체크는 200을 반환했다(옛 프로세스가 응답). 그 상태로 멘토를 호출해 또 mock 답변을 받고 「주입 실패」로 오진할 뻔했다. 판별법=`netstat -ano | grep ":8080" | grep LISTEN`으로 **PID가 바뀌었는지 확인**. 종료는 `Stop-Process -Id <PID> -Force`.
3. **★gradle 명령에도 `cd <절대경로> &&`를 붙일 것★** — 세션 cwd가 이전 명령의 `cd`로 다른 레포에 남아 `scanKnowledgeDocs`가 ai-svc에서 실행됐다(다행히 `Task not found in root project 'devpath-ai-svc'`로 명시적 실패). 서브에이전트에게 반복 경고해 온 함정에 컨트롤러가 걸렸다.
4. **포트** — learning-svc·ai-svc 둘 다 `application.yml` 기본이 8080. 동시 기동 시 learning-svc를 `--args='--server.port=8081'`로. `KnowledgeClient` 기본 base-url이 `http://localhost:8081`이라 그대로 붙는다.
5. **멘토 호출** — `POST /ai-mentor/sessions`, JWT 필수(`sub`=userId). ai-svc JWT는 HS256 대칭키에 로컬 기본값이 있고 issuer·audience 검증이 없어 직접 발급 가능.
6. **로컬 테스트 전제** — Redis 없으면 헬스체크 503으로 3건 실패. 전용 DB를 쓸 것(`DB_URL=jdbc:postgresql://localhost:5432/devpath_kbtest`). `devpath_kbtest`는 이제 **Flyway 40개 마이그레이션이 정상 적용된 상태**다(이전 핸드오프의 「수동 적용」 서술은 `@SpringBootTest`에 불충분했다).
7. **콘솔 한글이 깨진다** — 값 자체는 정상. `grep`으로 한글을 찾지 말고 `py -c`로 파싱하되, Windows 콘솔이 cp949라 `PYTHONIOENCODING=utf-8`을 붙이고 ASCII로 출력할 것.

## 5. 이월 · 미결

| 항목 | 내용 |
|---|---|
| **임베딩 모델 한국어 약점(신규)** | 「의존성 주입이 뭔가요」의 1위가 `Mysql Study/mysql-step-01.md`(distance 0.1361)로 관련성이 낮다. 「MSA Saga」·「React 상태관리」는 정확했으므로 파이프라인 문제는 아니고 `nomic-embed-text`가 짧은 한국어 개념 질문에 약할 가능성. **2단계 착수 전 재확인** |
| **삭제 문서 sweep 부재** | `status`를 `'INACTIVE'`로 내리는 경로가 코드에 없다. dsd에서 파일이 지워져도 그 문서·청크는 영원히 `ACTIVE`로 남아 계속 검색된다. 스펙도 다루지 않은 미결 — **2단계 착수 전 결정 필요** |
| Sample Codes 비중 | **우려 기각.** 개념 질문 3개 × 상위 5건 = 15건 중 0건. 가중치·필터 불필요 |
| 성능(deferred minor) | `KnowledgeLoader`가 건별 `jdbc.update` 19,115회(`batchUpdate` 미사용). 문서 단위 트랜잭션 안이라 커넥션은 재사용되고 실측 적재가 1분 6초라 수용 가능 |
| `/internal/**` 노출 | 게이트웨이에 `/internal` 라우트가 **하나도 없음**을 실측 확인해 외부 도달은 배제됐다. 다만 반환 데이터 민감도가 올라갔다(제목 → 본문). **AWS 재개 시 NetworkPolicy 또는 서비스 간 토큰 검토** |
| HNSW 인덱스 실사용 | 정렬 tiebreak `d.id desc`가 인덱스 경로를 막을 수 있다는 지적이 있었다. 데이터가 적재된 지금 `EXPLAIN (ANALYZE)`로 1회 확인할 수 있다(19,115행이라 seq scan이어도 수 ms) |
| 2단계(③콘텐츠 생성) | 지식베이스를 재료로 학습 콘텐츠·문항을 생성하는 별도 스펙 |
| **광고 → Google AdSense 전환** | 사용자 신규 요구(2026-08-09). **지식베이스 1단계 완료 후 착수**하기로 했으므로 지금부터 가능. 기존 자체 광고(하우스/스폰서)는 제거하지 않고 **유지 + 병행**. 착수 시 브레인스토밍 선행. 선결 확인 2건 = ①Flutter Web(캔버스 렌더)에서 AdSense DOM 삽입이 실제로 되는지 PROBE ②애드센스 심사는 라이브 사이트 전제인데 현재 AWS 정지로 `leva.ai.kr` 다운 |

## 6. 재현 명령

```bash
# 전제
docker start devpath-local-postgres-1 devpath-local-redis-1
curl -s http://localhost:11434/api/tags        # Ollama

# 파이프라인 (learning-svc)
cd /d/workspace/dpa/devpath-learning-svc
./gradlew scanKnowledgeDocs                                     # 743개
./gradlew embedKnowledge -PsourceCommit=$(git -C /d/workspace/dsd rev-parse HEAD)   # 19,115청크·약 3분
export DB_URL=jdbc:postgresql://localhost:5432/devpath
./gradlew loadKnowledge                                          # 멱등

# 검색 실증
cd /d/workspace/dpa/devpath-learning-svc && ./gradlew bootRun --args='--server.port=8081'
# POST /internal/knowledge/similar  {"embedding":[768개], "limit":3}

# 멘토 실증 (★provider 지정 필수★)
cd /d/workspace/dpa/devpath-ai-svc
MENTOR_PROVIDER=ollama MENTOR_OLLAMA_MODEL=qwen2.5:14b MENTOR_TIMEOUT=PT180S \
  DB_URL=jdbc:postgresql://localhost:5432/devpath ./gradlew bootRun
# POST /ai-mentor/sessions  (JWT 필요, SSE)
```

## 7. 이번 세션 산출 커밋

| 레포 | PR | merge |
|---|---|---|
| devpath-learning-svc | #44 (Task 2~7) | `658c7d0` |
| devpath-learning-svc | #45 (서로게이트 수정 + 실증 보고서) | `03d602d` |
| devpath-ai-svc | #34 (Task 8 멘토 주입) | `47031c0` |
