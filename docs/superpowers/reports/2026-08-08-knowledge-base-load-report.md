# 지식베이스 적재 · 멘토 실증 결과

> 실행 2026-08-09 (로컬)
> 스펙: `docs/superpowers/specs/2026-08-08-knowledge-base-mentor-rag-design.md`
> 계획: `docs/superpowers/plans/2026-08-08-knowledge-base-mentor-rag.md` Task 9
> 문서 스냅샷: `develop-study-documents` master `89e263935d66d14f59e22f4d764f1906c8983811`

## 1. 결론

**멘토가 학습 문서를 근거로 답한다.** 대조 실증으로 주입 효과를 분리해 확인했다(§5).

파이프라인 전 구간이 실제 데이터로 통과했다: 스캔 743개 → 청킹·임베딩 19,115청크 → 적재 → 검색 API → 멘토 프롬프트 주입.

## 2. 실행 수치

| 단계 | 결과 |
|---|---|
| 스캔 | 문서 **743개** · 16,372,359자 · CR 포함 0 · 고유 `doc_hash` 743 · 카테고리 23 |
| 청킹 + 배치 임베딩 | **19,115청크** · 768차원 단일 · `source_commit` 전부 기록 · 파싱 오류 0 |
| 임베딩 소요 | **3분 6초** (배치 50, `nomic-embed-text`, RTX 2080 Ti) |
| 산출물 | `embeddings.jsonl` 213,678,986바이트 |
| 적재 | `knowledge_documents` **743** · `knowledge_embeddings` **19,115** · INACTIVE 0 |
| 멱등성 | 같은 적재 재실행 후에도 743 / 19,115 **불변** |

청크 수가 설계 추정치(19,109)보다 6개 많다. §4의 서로게이트 경계 수정으로 경계가 최대 1 코드 유닛 앞으로 당겨지면서 생긴 차이다.

**임베딩 소요가 설계 추정(약 19분)의 6분의 1이다.** 설계 단계 실측(배치 50에서 58ms/청크)보다 실제가 빨랐다. 배치가 전제라는 결론 자체는 바뀌지 않는다 — 단건 방식이었다면 12시간이었다.

## 3. 검색 API 실증

learning-svc를 `:8081`로 띄우고 `POST /internal/knowledge/similar`를 직접 호출했다.

질문 「EKS Pod Identity는 Fargate Pod를 지원하나요?」 → 상위 3건:

| 순위 | distance | docKey |
|---|---|---|
| 1 | 0.1648 | `AWS/step-03-iam-deep.md` |
| 2 | 0.2432 | `AWS/step-14-eks-ops.md` |
| 3 | 0.2504 | `AWS/step-03-iam-deep.md` |

1위 `chunkText` 발췌:

> …드 전제(Fargate 미지원)**: Pod Identity 에이전트는 노드에 DaemonSet으로 뜨므로 **EC2 노드(또는 하이브리드 노드)** 가 전제다. 노드에 에이전트를 띄울 수 없는 **EKS Fargate** Pod에는 적용되지 않아, Fargate에서는 **IRSA**를 써야 한다.

**Task 0의 AWS 환각 교정본이 그대로 근거로 반환된다.** 이 API의 존재 이유(제목이 아니라 본문을 돌려주는 것)가 실증됐다 — 기존 `SimilarContent(contentId, slug, title)`에는 본문이 없어 멘토가 문서 내용을 알 수 없었다.

## 4. 실행이 드러낸 결함 — 청크 경계가 서로게이트 쌍을 쪼갠다

첫 실행이 **8,955청크에서 죽었다.**

```
java.nio.charset.MalformedInputException: Input length = 1
	at java.base/sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:384)
	...
	at ai.devpath.learning.knowledgegen.EmbedKnowledgeCommand.writeBatch(EmbedKnowledgeCommand.java:128)
```

**예외가 읽기가 아니라 쓰기(인코딩)에서 났다.** Java `String`이 UTF-8 인코딩에 실패하는 경우는 짝이 깨진 서로게이트뿐이다.

원인은 `ContentChunker`가 청크를 UTF-16 코드 유닛 인덱스로 자르는 데 있었다. `end`가 `MAX_CHARS`/`OVERLAP_CHARS` 산술로 계산되므로 서로게이트 쌍(이모지 등 astral 문자) 한가운데 떨어질 수 있다.

### 영향 범위 (실측)

```
docs_total=743  docs_with_astral=562  astral_char_count=5246
```

**743개 문서 중 562개**에 astral 문자가 5,246자 있다. 문서 카테고리 이름 자체가 `🐤 Spring_Boot_4_*` 형태다 — 드문 엣지 케이스가 아니라 코퍼스의 대부분이 해당된다.

### 수정

`ContentChunker.safeSurrogateBoundary(text, index)` — 경계가 low surrogate이고 그 직전이 high surrogate일 때만 1 코드 유닛 앞으로 당긴다. astral 문자가 없는 입력에는 경계가 **완전히 동일**해 기존 콘텐츠 시드 파이프라인에 영향이 없다. `MAX_CHARS=1200` · `OVERLAP_CHARS=120` · H2 정규식은 불변이다. 같은 결함이 `KnowledgeDocScanner`의 제목 500자 절단에도 있어 함께 고쳤다.

### 파생 관찰

- 8,955번째 줄이 잘린 것은 프로세스가 죽어서가 아니라 **인코더가 줄 중간에서 던진 결과**였다.
- 그 잘린 줄 덕분에 **재개 방어(최종 리뷰 I3)가 실제로 값을 했다.** 리뷰어는 "한 줄이 11~13KB로 `BufferedWriter` 8KB 버퍼보다 커서 줄 쓰는 도중에도 잘릴 수 있다"며 Minor를 Important로 승격시켰는데, 예측한 메커니즘(프로세스 죽음)은 달랐지만 방어는 정확히 필요한 자리였다. 이 방어가 없었다면 재실행이 손상된 줄에서 계속 죽어 **230MB 파일을 손으로 편집**해야 했다.
- 인코더를 `CodingErrorAction.REPLACE`로 관대하게 만들어 회피하는 길도 있었으나 택하지 않았다. 예외는 사라지지만 본문이 조용히 손상되고, 지식베이스는 멘토 답변의 근거다.

## 5. ★멘토 실증 — 주입 전후 비교★

ai-svc를 `MENTOR_PROVIDER=ollama` · `MENTOR_OLLAMA_MODEL=qwen2.5:14b`로 띄우고 `POST /ai-mentor/sessions`(SSE)를 호출했다.

대조군은 `knowledge_embeddings.status`를 일시적으로 `INACTIVE`로 내려 검색이 0건을 반환하게 만들어 구성했다(검색 0건을 먼저 확인한 뒤 질문했고, 실험 후 전량 `ACTIVE`로 복구했다).

### 5.1 판별력이 없었던 첫 질문

「EKS Pod Identity를 Fargate Pod에서 쓸 수 있나요?」

| 조건 | 답변 |
|---|---|
| 근거 있음 | EKS Pod Identity는 Fargate Pod에서는 사용할 수 없습니다. 대신 Fargate Pod에서는 IRSA를 사용해야 합니다. |
| 근거 없음 | 아니요, … 현재 Fargate **프로드(프로덕션 옵티마이즈드 도커 컨테이너)** 에서는 지원되지 않습니다. … STS를 통해 임시 자격 증명을 획득하거나 IRSA를 사용… |

**모델이 이미 아는 사실이라 결론이 같았다.** 다만 근거가 없을 때 「Fargate 프로드(프로덕션 옵티마이즈드 도커 컨테이너)」라는 없는 용어를 지어냈다.

이 질문 하나로 끝냈다면 "지식베이스가 동작한다"는 결론을 **근거 없이** 내릴 뻔했다. 계획이 요구한 대조 실증이 이 함정을 막았다.

### 5.2 판별력 있는 질문 — 코퍼스에만 있는 사실

`MCP study/MCP-Phase5-Step24-Skill워크플로우결합.md`의 프로젝트 고유 규칙을 골랐다.

> 쓰기 도구(registerInbound, decrement_stock)는 반드시 사용자 승인 후 실행한다.
> 재고 차감은 가용 재고의 50%를 초과하면 먼저 사용자에게 확인한다.

질문: 「StockPilot MCP 서버에서 재고를 차감할 때 사용자 확인이 필요한 기준은 무엇인가요? 쓰기 도구 이름도 알려주세요.」

| 조건 | 답변 |
|---|---|
| **근거 있음** | 재고 차감을 할 때, **가용 재고의 50%**를 초과하는 경우 사용자 승인이 필요합니다. 이때 사용되는 쓰기 도구는 **`decrement_stock`**입니다. |
| **근거 없음** | …두 가지입니다: 1. 재고 수량이 **일정 수준 이하**로 떨어질 경우… 2. **특정 금액 이상**의 거래… 일반적으로 **"Stock Deduction Confirmation"** 또는 **"Inventory Adjustment Request"** 와 같은 쓰기 도구를 사용합니다. |

**근거가 있을 때는 문서와 정확히 일치하고, 없을 때는 기준도 도구명도 전부 지어낸다.** 이것이 이 프로젝트의 목적을 증명하는 결정적 증거다.

## 6. Sample Codes 비중 관찰 (설계 §3.1 미결)

Sample Codes가 전체 청크의 25%(4,853개)라 개념 질문을 밀어낼 수 있다는 우려로 가중치 도입을 보류해 뒀다. 개념 질문 3개 × 상위 5건으로 관찰했다.

| 질문 | 상위 5건 카테고리 |
|---|---|
| 의존성 주입이 뭔가요 | Mysql Study, AWS SAA-C03 ×2, Interview Prompt ×2 |
| MSA에서 Saga 패턴은 언제 쓰나요 | MCP study, **MSA pattern ×2**, MSA ×2 |
| React 상태관리 전략 | **React Programing ×5** |

**15건 중 Sample Codes 0건.** 우려는 현실이 아니었다 — **가중치·필터가 불필요하다.**

### 별건 관찰 (이월)

「의존성 주입이 뭔가요」의 1위가 `Mysql Study/mysql-step-01.md`(distance 0.1361)로 관련성이 낮다. 「MSA Saga」·「React 상태관리」는 정확한 문서를 찾았으므로 파이프라인 문제는 아니고, `nomic-embed-text`가 짧은 한국어 개념 질문에 약할 가능성이 있다. **2단계(콘텐츠 생성) 착수 전에 재확인할 것.**

## 7. 다음 세션이 알아야 할 실행 함정

1. **★`MENTOR_PROVIDER` 기본값이 `mock`이다★** — 그냥 `bootRun`하면 멘토가 `MockMentorClient`의 고정 문구(「그 질문에 답하면, 비동기는 Future/async/await로 다룹니다」)를 반환한다. **주입 실패로 오진하기 딱 좋다.** 실증에는 `MENTOR_PROVIDER=ollama MENTOR_OLLAMA_MODEL=qwen2.5:14b`가 필수다. `MENTOR_FALLBACK`은 비워 둔다 — mock 폴백이 붙으면 실패가 조용히 가려진다.
2. **★백그라운드 태스크 종료가 java 프로세스를 남긴다★** — 재기동이 `Port 8080 was already in use`로 죽었는데도 헬스체크는 200을 반환했다(옛 프로세스가 응답). 그 상태로 멘토를 호출해 또 mock 답변을 받고 「주입 실패」로 오진할 뻔했다. 판별법은 `netstat -ano | grep ":8080" | grep LISTEN`으로 **PID가 바뀌었는지 확인**하는 것이다.
3. **포트** — learning-svc·ai-svc 둘 다 `application.yml` 기본이 8080이다. 동시 기동 시 learning-svc를 `--args='--server.port=8081'`로 띄운다. `KnowledgeClient`의 기본 base-url이 `http://localhost:8081`이라 그대로 붙는다.
4. **멘토 호출** — `POST /ai-mentor/sessions`는 JWT가 필요하다(`sub`=userId). ai-svc의 JWT는 HS256 대칭키에 로컬 기본값이 있고 issuer·audience 검증이 없어 직접 발급할 수 있다.
5. **적재 자격증명** — `LoadKnowledgeCommand`는 `DB_USERNAME` → `DB_USER` → 기본값 순으로 읽고 비밀번호 기본값은 `localdev`다(레포 관례와 일치). 최종 리뷰 전에는 `DB_USERNAME`만 읽고 기본값이 `devpath`라 **계획대로 실행하면 인증 실패**했을 상태였다.
