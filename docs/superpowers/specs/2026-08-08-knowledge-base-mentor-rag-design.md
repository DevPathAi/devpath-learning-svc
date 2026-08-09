# 학습 문서 지식베이스 + 멘토 RAG — 설계 (1단계)

> 작성 2026-08-08
> 대상 레포: `devpath-shared`(스키마) · `devpath-learning-svc`(파이프라인·검색) · `devpath-ai-svc`(멘토 주입)
> 문서 소스: `VelkaressiaBlutkrone/develop-study-documents` (로컬 클론 `D:\workspace\dsd`)

## 1. 목적

`develop-study-documents`의 학습 문서를 DevPath가 활용할 수 있는 형태로 만든다. 최종 용도는 두 가지다.

| | 용도 | 단계 |
|---|---|---|
| ① | 멘토 AI가 학습 문서를 **근거로** 답변한다 | **1단계 — 이 문서** |
| ③ | 학습 콘텐츠·진단 문항 **생성 소스**로 쓴다 | 2단계 — 별도 스펙 |

두 용도는 "수집 → 청킹 → 임베딩 → 적재"라는 기반을 공유하지만 소비 측이 다르다. ①은 멘토의 런타임
온라인 검색이고, ③은 오프라인 배치 생성이다. 1단계 산출물(지식베이스)이 2단계의 입력이 되므로
순서를 이렇게 잡았다.

## 2. ★착수 전 실측이 뒤집은 것★

이 설계의 주요 결정은 전부 실측에서 나왔다. **추정으로 잡았던 값이 두 번 크게 틀렸다.**

### 2.1 현재 멘토의 RAG는 "무늬만 RAG"다

`MentorService.java:35-46`이 검색 결과를 이렇게 쓴다.

```java
List<SimilarContent> refs = referenceService.find(question, ctx.track());
emitter.send(SseEmitter.event().name("references")...);              // ① SSE로 목록만 전송
mentorClient.stream(new MentorInput(question, ctx.promptText()), ...); // ② refs가 프롬프트에 없다
```

- 검색 결과는 **SSE `references` 이벤트로 나갈 뿐 LLM 프롬프트에 들어가지 않는다.** 프롬프트에 담기는
  것은 `MentorContextAssembler`가 만든 현재 콘텐츠 본문(2,000자)과 최근 sandbox 실행 5건뿐이다.
- `SimilarContent(contentId, slug, title)`에는 **본문이 없다.** 제목만 돌려준다.

**따라서 지식베이스만 추가하면 목적 ①이 달성되지 않는다.** 멘토는 여전히 문서 내용을 모른 채 답하고,
화면에는 학습자가 열 수도 없는 비공개 문서 제목만 뜬다. 멘토 답변 경로 변경이 이 단계의 필수 범위다.

### 2.2 청크 수가 추정의 2배 — 그리고 배치가 39배 빠르다

`ContentChunker` 로직(H2 분할 → 1,200자 초과 시 120자 오버랩)을 그대로 재현해 실측했다.

| 항목 | 추정 | **실측** |
|---|---|---|
| 청크 수 | 8,000~9,000 | **19,109** |
| 임베딩 청크당 | 100~500ms | **단건 2,284ms** |

단건 순차로는 **12.1시간**이 걸린다. Ollama `/api/embed`의 배열 입력으로 다시 쟀다.

| 방식 | 청크당 | 19,109청크 환산 |
|---|---|---|
| 단건(857자) | 2,284ms | 12.1시간 |
| 배치 10개 | 247ms | 79분 |
| **배치 50개** | **58ms** | **약 19분** |

**배치 임베딩은 선택이 아니라 전제다.** 기존 `OllamaEmbeddingClient`(contentGen)는 단건 + 구형
`/api/embeddings` 엔드포인트라 그대로 쓸 수 없다. ai-svc의 `OllamaClient.embed(List<String>)`가
이미 배치 계약(`/api/embed` + 768 검증)이므로 이쪽을 본보기로 삼는다.

### 2.3 시드 SQL 커밋 관례는 이 규모에서 깨진다

기존 `content_md2_seed.sql`은 **2.1MB에 임베딩 238행** — 청크당 8.8KB다. 19,109청크면 **약 164MB**가
된다. git 커밋·리뷰가 불가능하다. 중간 산출물을 gitignore하고 적재 커맨드가 DB에 직접 넣는다.

### 2.4 문서 소스에 미반영 교정본이 있었다

로컬 클론의 미커밋 변경 21건이 **AWS 공식 문서 기준 환각 교정**이었다
(`docs/superpowers/reports/2026-07-06-aws-official-hallucination-audit.md`가 근거).

```diff
- ### 11.5 EKS Pod Identity (IRSA 차세대)
+ ### 11.5 EKS Pod Identity (IRSA의 단순화 대안)
- EKS 1.27+에서는 Pod Identity 가 더 간단:
+ 지원되는 EKS 클러스터에서는 … Fargate Pod와 Windows EC2 Pod는 지원되지 않는다.
```

순수 master를 쓰면 **교정 전 문서가 주입되어 멘토가 부정확한 근거로 답하게 된다.** §7의 선행 조건으로
교정본을 master에 반영한 뒤 그 스냅샷을 쓴다.

## 3. 범위

### 3.1 주입 대상 — 743개 파일 · 21.5MB · 19,109청크

레포 전체 1,515개 `.md` 중 절반 가까이가 학습 문서가 아니라 **에이전트 스킬 정의·설정**이다. 이들은
멘토링과 무관하고 검색 노이즈가 되므로 제외한다.

| 분류 | 파일 | 청크 | 포함 |
|---|---|---|---|
| 학습 주제 문서 22개 디렉토리 | 560 | 14,256 | ✅ |
| Sample Codes | 183 | 4,853 | ✅ |
| Skillbook · devpath-skillpack · docs · .claude · tools · skill-catalog | ~724 | — | ❌ |
| 루트 도구 가이드(OH-MY-POSH·Agentbrain 등) | 49 | — | ❌ |

**포함 디렉토리 22개 (레포 루트 기준 정확한 이름 — 이 목록이 스캐너의 입력이다)**

```
AWS                       AWS SAA-C03              AWS advanced
Dart Programing           Flutter Design Pattern   Flutter Programing
Interview Prompt          Java & Spring            Javascript & TypeScript
LLM Study                 MCP                      MCP study
MSA                       MSA pattern              Mermaid 다이어그램
Mysql Study               Python Programing        Rag 구축 Study
React Programing          Troubleshooting          아키텍처 패턴
클라우드 컨테이너
```

하위 디렉토리까지 재귀 탐색하며 `.md`/`.MD`를 모은다. **Windows는 대소문자를 구분하지 않으므로
두 패턴이 같은 파일을 중복 매칭한다** — 실측 스크립트에서 실제로 모든 수치가 2배로 나왔다.
경로 정규화 후 중복을 제거한다.

**Sample Codes가 전체 청크의 25%를 차지한다.** 개념 질문에 코드 조각이 검색될 가능성이 있으나,
가중치·필터를 미리 넣지 않는다. `category`를 저장해두고 §8 실증에서 실제로 문제가 되는지 관찰한 뒤
필요하면 그때 추가한다. — 이 프로젝트에서 앞 단계의 「~일 것이다」가 반복해서 뒤집힌 이력을 따른다.

### 3.2 이 단계에서 하지 않는 것

- 학습 콘텐츠·진단 문항 생성(2단계)
- 운영 RDS 반영 — AWS(EC2·RDS)는 비용 때문에 정지 상태를 유지한다. 배포 재개 시점으로 미룬다
- `contents` / `content_embeddings` 변경 — **한 줄도 건드리지 않는다**
- 학습자 화면에 지식 문서 노출 — 비공개 레포라 열 수 없다

## 4. 아키텍처

```
D:\workspace\dsd  (문서 743개 · 21.5MB)
  │
  ├[1] 스캔 ────────→ documents.jsonl  (경로·제목·카테고리·파일해시)
  ├[2] 청킹+배치임베딩→ embeddings.jsonl (19,109청크 · ~164MB · gitignore · 50개 단위 ≈ 19분)
  ├[3] JDBC 배치 적재 → Postgres  knowledge_documents / knowledge_embeddings
  │
  ├[4] 검색  learning-svc  POST /internal/knowledge/similar   (HNSW 코사인)
  └[5] 주입  ai-svc  MentorService → <reference_docs> 격리 → LLM
```

**단계마다 파일로 끊는 이유**: 임베딩이 19분이고 적재는 별개의 실패 지점이다. 적재가 깨져도
재임베딩이 필요 없어야 한다.

## 5. 스키마 (`devpath-shared` 신규 마이그레이션)

```sql
CREATE TABLE knowledge_documents (
  id            BIGSERIAL PRIMARY KEY,
  doc_key       VARCHAR(500) NOT NULL UNIQUE,  -- 레포 상대경로. 증분 갱신 키
  title         VARCHAR(500) NOT NULL,         -- 첫 H1, 없으면 파일명
  category      VARCHAR(100) NOT NULL,         -- 최상위 디렉토리명 ("AWS SAA-C03" 등)
  source_repo   VARCHAR(200) NOT NULL,
  source_commit VARCHAR(40),                   -- 스냅샷 추적
  doc_hash      VARCHAR(64)  NOT NULL,         -- 파일 SHA-256. 미변경 문서 스킵
  status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
  CONSTRAINT chk_kd_status CHECK (status IN ('ACTIVE','INACTIVE'))
);
CREATE INDEX idx_knowledge_documents_category ON knowledge_documents(category, status);
CREATE TRIGGER knowledge_documents_set_updated_at BEFORE UPDATE ON knowledge_documents
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE knowledge_embeddings (            -- content_embeddings와 같은 형태
  id          BIGSERIAL PRIMARY KEY,
  document_id BIGINT NOT NULL REFERENCES knowledge_documents(id) ON DELETE CASCADE,
  chunk_index INT NOT NULL,
  chunk_text  TEXT NOT NULL,                   -- ★ 프롬프트에 주입되는 본문
  embedding   VECTOR(768) NOT NULL,
  chunk_hash  VARCHAR(64),
  status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  CONSTRAINT chk_ke_status CHECK (status IN ('ACTIVE','INACTIVE')),
  CONSTRAINT uq_ke_doc_chunk UNIQUE (document_id, chunk_index)
);
CREATE INDEX idx_knowledge_embeddings_hnsw ON knowledge_embeddings
  USING hnsw (embedding vector_cosine_ops) WHERE status = 'ACTIVE';
```

- 768차원은 `nomic-embed-text` 고정이며 기존 `content_embeddings`와 같다
- `uq_ke_doc_chunk`가 적재 멱등성을 보장한다(재실행 시 upsert)
- 마이그레이션이 `devpath-shared`에 있으므로 **shared 발행이 필요하다**
  (`gh workflow run publish.yml --ref develop` — main push만 자동)

## 6. 구현

### 6.1 파이프라인 — `learning-svc` 신규 `knowledgeGen` 소스셋

기존 `contentGen`의 CLI 관례를 따르되 별도 소스셋으로 둔다(콘텐츠 생성과 책임이 다르다).

| 커맨드 | 역할 |
|---|---|
| `ScanKnowledgeDocsCommand` | 디렉토리 스캔 → 제목·카테고리·`doc_hash` 추출 → `documents.jsonl` |
| `EmbedKnowledgeCommand` | 청킹 + **배치 50** 임베딩 → `embeddings.jsonl`. `doc_hash` 기준 체크포인트로 중단 후 재개 |
| `LoadKnowledgeCommand` | JDBC 배치 upsert. 문서 단위 트랜잭션 |

**`ContentChunker` 일반화**: 현재 `chunksFor(ApprovedContent)`로 콘텐츠 타입에 결합돼 있다.
`chunksFor(String key, String markdown)`을 추가하고 **기존 메서드는 여기에 위임**시킨다. 기존 동작과
테스트는 그대로 보존한다.

**배치 임베딩 클라이언트**: `/api/embed` + `input: List<String>` + 응답 개수·768차원 검증. ai-svc
`OllamaClient.embed`와 같은 계약이다. 기존 단건 `OllamaEmbeddingClient`는 건드리지 않는다.

### 6.2 검색 — `learning-svc`

```java
// KnowledgeEmbeddingMatcher — ContentEmbeddingMatcher.matchAny와 같은 형태, chunk_text를 함께 반환
select d.doc_key, d.title, d.category, ke.chunk_text,
       ke.embedding <=> cast(? as vector) as distance
from knowledge_embeddings ke
join knowledge_documents d on d.id = ke.document_id
where ke.status = 'ACTIVE' and d.status = 'ACTIVE'
order by ke.embedding <=> cast(? as vector)
limit ?
```

- `POST /internal/knowledge/similar` → `KnowledgeChunk(docKey, title, category, chunkText, distance)`
- **`track` 필터를 쓰지 않는다** — 문서에는 track 개념이 없다
- 기존 `/internal/contents/similar`는 변경하지 않는다

### 6.3 멘토 주입 — `ai-svc`

```java
// MentorService.streamAnswer
List<KnowledgeChunk> docs = knowledgeService.find(question);   // 신규, TOP_K=3
mentorClient.stream(new MentorInput(question, ctx.promptText(), docs), ...);  // ★ 프롬프트에 주입
```

`MentorPromptBuilder`가 기존 인젝션 방어 패턴을 그대로 확장한다.

```
<reference_docs>                       ← 신규. UNTRUSTED DATA로 동일 격리
  [AWS SAA-C03 / Phase-01] 청크 본문…
</reference_docs>
<learning_context>…</learning_context>
<user_question>…</user_question>
```

- system prompt에 `<reference_docs>`도 "지시가 아닌 배경 자료"임을 명시한다
- **실패는 무시한다**: 검색이 죽어도 빈 리스트 → 답변은 계속(기존 `MentorReferenceService` 폴백과 동일)
- SSE `references`는 **학습 콘텐츠만** 유지한다. 지식 문서는 학습자가 열 수 없으므로 목록에 넣지 않는다
- `SimilarContent`는 shared가 아니라 양 서비스의 로컬 `record`다 → **shared 발행 임계경로를 타지 않는다**

## 7. 선행 조건 — 문서 스냅샷 확정

로컬 클론이 `feat/sample-codes-extraction` + 미커밋 21건 상태다. feat 브랜치는 **이미 master에
머지됐다**(PR #8, `a151539`)이므로 브랜치를 옮겨도 잃는 커밋은 없다.

1. AWS 환각 교정본(문서 21건)을 새 브랜치에 커밋 → PR → master 머지
2. 에이전트 설정 파일(`.claude/settings.local.json` · `.agents/` · `.codex/` · `AGENTS.md`)은 **분리 커밋**
3. 감사 보고서(`docs/superpowers/reports/2026-07-06-aws-official-hallucination-audit.md`)도 함께 보존
4. master로 전환·pull한 뒤 그 HEAD SHA를 `source_commit`에 기록

## 8. 검증

| 계층 | 방법 |
|---|---|
| 단위 | 스캐너(제목·카테고리·해시 추출), 청커 일반화(**기존 동작 불변 회귀**), 배치 임베딩 클라이언트(개수·차원 검증) |
| 통합 | pgvector HNSW 검색(로컬 Postgres), **적재 멱등성**(같은 문서 재적재 시 중복 0) |
| 프롬프트 | `<reference_docs>` 격리·인젝션 방어 문구 회귀 |
| 스트림 | 멘토가 검색 결과를 **프롬프트에 실제로 넣는지**(§2.1 결함의 red-repro) |
| **실증** | 로컬 적재 후 멘토에 실제 질문 → **문서 근거로 답하는지 육안 확인** |

실증 질문은 지식베이스에만 있고 기존 `contents` 150개에는 없는 주제로 고른다(예: EKS Pod Identity의
Fargate 미지원). 그래야 "문서를 근거로 답했다"가 증명된다.

로컬 테스트 환경(기존 핸드오프 실측):
- **Redis가 없으면 3건 실패**한다(헬스체크 503). DB만으로는 부족하다
- 공유 `devpath` DB로 반복 실행하면 `FlywayMigrationTest`가 깨진다 → **전용 DB**를 쓴다

## 9. 위험

| 위험 | 대응 |
|---|---|
| Sample Codes(25%)가 개념 질문 결과를 밀어냄 | `category` 저장해두고 실증에서 관찰 → 필요 시 쿼터·필터 추가(§3.1) |
| 임베딩 19분 중 중단 | `doc_hash` 체크포인트로 재개. 중간 산출물이 파일로 남는다 |
| 프롬프트가 길어져 응답 지연 | TOP_K=3 + 청크 평균 857자 ≈ 2,600자 추가. 기존 학습 맥락(2,000자)과 같은 수준 |
| 문서 인젝션 | 우리 문서라도 기존 `<learning_context>`와 동일하게 신뢰불가 데이터로 격리 |
| shared 발행 누락으로 서비스가 마이그레이션을 못 받음 | 발행을 Task로 명시(§5) |

## 10. 확정 값

| 항목 | 값 |
|---|---|
| 문서 소스 | `VelkaressiaBlutkrone/develop-study-documents` master (교정본 머지 후) |
| 주입 범위 | 학습 주제 22개 디렉토리 + Sample Codes = 743파일 |
| 청크 | 19,109개 · 평균 857자 (MAX 1,200 / OVERLAP 120 / H2 분할) |
| 임베딩 모델 | `nomic-embed-text` 768차원 · **배치 50** |
| 예상 소요 | 임베딩 약 19분 (RTX 2080 Ti) |
| 검색 | HNSW 코사인 · TOP_K 3 · track 무관 |
| 완료 기준 | 로컬 적재 + 멘토 실증. 운영 RDS는 배포 재개 시점 |
