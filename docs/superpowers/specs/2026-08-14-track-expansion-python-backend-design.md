# 트랙 확장 — 파이프라인 게이트 + Python 웹 백엔드 트랙 (설계)

2026-08-14. 진단·학습 트랙을 5종에서 8종으로 늘리는 작업의 **첫 사이클**이다.
이 스펙은 ①생성 파이프라인의 품질 게이트를 세우고 ②그 위에서 **Python 웹 백엔드** 한 트랙을
끝까지(문항·콘텐츠·임베딩·운영 적재) 만든다. 나머지 두 트랙은 같은 절차의 반복이므로 범위 밖이다.

관련: [진단 트랙 선택 배선](../../../devpath-frontend/docs/superpowers/specs/2026-08-13-diagnostic-track-selection-design.md) ·
[한국어 콘텐츠 생성](2026-07-28-korean-content-generation-design.md)

## 왜 트랙보다 게이트가 먼저인가

`QuestionValidator` 를 읽고 실측했다. 지금 잡는 것은 트랙 화이트리스트·`questionType`·`bloomLevel`·
난이도 범위·`options.size() >= 2`·정답키 인덱스 범위·태그 kebab-case, 그리고 트랙당 100문항과
MCQ 70 / CODE_READING 30 이다. **잡지 않는 것이 2026-08-13 운영 사고의 정확한 모양이다.**

| 결함 | 현재 판정 | 근거 |
|---|---|---|
| 500문항이 **선택지 한 벌을 공유** | 통과 | `QuestionValidator:57` 이 `size() >= 2` 만 본다 |
| 같은 문항이 여러 번 | 통과 | 문항 중복 검사가 없다 |
| 정답키가 한 위치에 쏠림 | 통과 | 인덱스 **범위**만 본다(`:62-65`) |
| 문항이 영어 | 통과 | 한국어 검사가 없다 — 「로컬은 한국어, 운영만 영어」였던 그 구멍 |
| 한 문항 안에 같은 보기 2개 | 통과 | 보기 내부 중복 검사가 없다 |

콘텐츠 쪽은 사정이 낫다 — `ContentValidator:42` 가 slug 중복을 잡는다. **문항 검증기에만
「중복」이라는 개념이 아예 없다.**

트랙을 3개 더 찍는 것은 이 구멍을 3배로 복제하는 일이다. 그래서 게이트를 먼저 세운다.

## 게이트

### 자동 검증기 — `QuestionValidator` 에 error 추가

| 게이트 | 실패 조건 |
|---|---|
| 선택지 벌 중복 | 정규화한 `options` 리스트가 같은 문항이 **트랙 안에서 2개 이상** |
| 문항 중복 | 정규화한 `content` 가 완전일치하는 문항이 2개 이상 |
| 보기 내부 중복 | 한 문항의 `options` 안에 같은 문자열이 2개 이상 |
| 정답키 편향 | 트랙 안에서 한 정답 위치의 비율이 **50%** 를 넘음 |
| 한국어 | `content` 에 한글(U+AC00–U+D7A3)이 하나도 없음 |

정규화 = 앞뒤 공백 제거 + 연속 공백 1칸 축약. 대소문자는 건드리지 않는다(코드 문항이 있다).

경계값 근거: 정답키 4지선다에서 균등이면 25%다. 50% 는 「눈에 띄게 쏠렸다」를 잡되
소량 표본의 자연 편차로 실패하지 않는 선이다. 트랙당 100문항이므로 표본은 충분하다.

### Claude 검수 — 신설, 자동 검증기 **뒤**

자동 검증기가 통과시킨 트랙 단위 JSONL 을 Claude 에 넘겨 아래를 리포트로 받는다.

1. 사실오류 2. 정답키 오류 3. 선택지가 정답을 흘리는 문항(길이·구체성으로 답이 보이는 것)
4. 어색한 한국어

**교정 제안은 승인 JSONL 을 덮어쓰지 않는다.** `generated/review/<track>-review.json` 으로 내고,
반영 여부는 사람이 정한다 — `generated/approved/` 가 이름 그대로 「승인된 것」이라는 기존 규약을 지킨다.

Claude 호출은 오프라인 도구다. `content-gen` README 의 원칙(**CI 는 Ollama 를 부르지 않는다**)을
그대로 적용해 **CI 는 Claude 도 부르지 않는다.** CI 가 보는 것은 커밋된 JSONL 과 리포트 파일뿐이다.

실행 형태: 기존 `generateQuestionsLocal` 과 같은 자리의 gradle 태스크
`reviewQuestionsLocal -Ptrack=PYTHON_BACKEND` 로 둔다. 트랙 단위로 도는 이유는 100문항이 한 요청에
들어가지 않기 때문이며, 배치 경계는 구현 계획에서 실측해 정한다(문항 길이 편차가 크다).
호출 주체는 `contentGen` 소스셋의 커맨드 클래스이고, 인증은 기존 오프라인 도구와 같이
환경변수로 받는다 — **키를 커밋하지 않는다.**

### 게이트가 살아 있는지 실증

새 게이트를 **기존 5트랙 500문항에 돌린다.** 나온 숫자가 「이 게이트는 무언가를 잡는다」의 증거다.
0건이면 게이트가 무의미한 것이 아니라 어제 재시드로 이미 고쳐졌다는 뜻이므로, **고의로 훼손한
사본**(선택지 한 벌 복제 · 정답키 전부 0 · 한국어를 영어로)으로 red 를 확인한다.

## 트랙 하나를 추가할 때 손대는 곳 (실측 10곳 + 신규 1)

| # | 위치 | 내용 |
|---|---|---|
| 1 | `devpath-shared` 신규 마이그레이션 | `chk_qb_track` (`question_bank`) |
| 2 | 〃 | `chk_assessments_track` (`assessments`) |
| 3 | 〃 | `chk_lp_track` (`learning_paths`) |
| 4 | 〃 | `chk_contents_track` (`contents`) |
| 5 | 〃 | `chk_target_track` (`user_profiles`) |
| 6 | `contentgen/question/QuestionQuota.TRACKS` | 트랙 목록 |
| 7 | `contentgen/content/ContentQuota.TRACKS` | 트랙 목록 |
| 8 | `tools/content-gen/schemas/question.schema.json` | 트랙 enum |
| 9 | `tools/content-gen/schemas/content.schema.json` | 트랙 enum |
| 10 | `devpath-frontend` `track_catalog.dart` | 이용자에게 보이는 카탈로그 |
| 신규 | `tools/content-gen/prompts/tracks/python-backend.md` | 출제 지침 (기존 5개는 각 ~1.7KB) |

`SeedSqlTest` · `ContentSeedSqlTest` 가 트랙 리터럴을 각 1건 들고 있으나 기존 트랙 값이라
이번 변경 대상이 아니다.

### 트랙 값

`track` 컬럼은 **`VARCHAR(20)`** 이다(`question_bank`·`assessments`·`learning_paths`·`contents`).
20자를 넘는 명칭은 쓸 수 없다.

| 값 | 길이 | 경계 |
|---|---|---|
| `PYTHON_BACKEND` | 14 | Django·FastAPI 중심의 웹 백엔드(ORM·비동기·인증·배포). **데이터·ML 은 제외** |
| `NODE_TYPESCRIPT` | 15 | Express·NestJS·Prisma·Node 런타임(이벤트 루프·스트림) — 후속 |
| `DATA_AI` | 7 | pandas·SQL·전처리·모델 평가·LLM 활용 — 후속 |

표시 라벨(`track_catalog.dart`)은 「Python 백엔드 (Django/FastAPI)」.

### 두 가지 설계 판단

- **CHECK 마이그레이션은 3값을 한 번에 연다.** 파일럿이어도 그렇다. CHECK 는 값을 허용할 뿐이고
  이용자 노출은 `track_catalog.dart` 가 정하므로, 마이그레이션을 세 번 치는 것보다 안전하다.
- **`QuestionQuota`/`ContentQuota` 에는 파일럿 트랙만 넣는다.** 이 목록에 값이 들어가면
  「그 트랙은 정확히 100문항·30콘텐츠」가 강제된다(`QuestionValidator:84`). 아직 만들지 않은 트랙을
  넣으면 검증기가 즉시 실패한다 — 이 강제는 유지한다.

## 운영 반영 경로

생성 파이프라인의 `makeQuestionSeedSql` 은 승인 JSONL **전량**을 SQL 로 재생성한다. 반면 운영 적재는
`devpath-shared` 의 Flyway 마이그레이션으로 나간다. 2026-08-13 사고는 정확히 이 접합부에서 났다 —
한국어 재생성 **이전**의 영어 파일에서 만든 SQL 이 운영에 적재됐고, 로컬은 한국어여서 발견이 늦었다.

그래서 새 트랙 마이그레이션은 **증분 INSERT 만** 한다.

- 어제의 `V202608131001__reseed_korean_seed_data.sql` 은 `DELETE` + 전량 재삽입이었다(이미 500행이
  있어 「비었을 때만 채움」 가드가 무력했기 때문). 이번은 **기존 5트랙 데이터를 건드리지 않는다.**
- `question_bank`·`contents` 는 id 를 지정하지 않고 INSERT 한다(SERIAL) — 기존 시드와 같은 방식.
- 파일 옆에 **`.sql.conf`(`placeholderReplacement=false`)** 를 짝으로 둔다. 없으면 SQL 안의 `${...}` 를
  Flyway 가 치환하려다 깨진다. 어제 이 한 줄이 해법이었다.
- 마이그레이션에 넣는 SQL 은 전량 재생성된 시드 파일에서 **신규 트랙 행만 발췌**한다. 전량을 넣으면
  기존 트랙이 중복 삽입된다.

### 배포 후 재측정 (게이트)

운영 DB 에서 신규 트랙에 대해 실측한다.

- `count(*)` = 100(문항) · 30(콘텐츠)
- `count(DISTINCT options)` — 1 이면 어제 사고의 재현이다
- `content_embeddings` 가 신규 트랙 콘텐츠를 **모두** 덮는지

임베딩이 비면 `LearningPathGenerationService:48` 의 트랙별 유사검색이 `contentId = null` 로
**예외 없이 조용히 degrade** 해, 그 트랙을 고른 이용자의 학습 경로에 콘텐츠가 하나도 안 붙는다.

## 테스트

- 새 게이트마다 **판별력을 실증**한다 — 고의로 훼손한 사본으로 red 를 확인한 뒤 통과시킨다.
  이 워크스트림에서 「green 인데 아무것도 검증하지 않는 테스트」가 세 번 나왔다.
- 게이트 단위 테스트는 기존 `QuestionValidator` 테스트 관례를 따른다(작은 픽스처 리스트 + 리포트 단언).
- `track_catalog_test.dart` 의 5키 계약을 6키로 갱신한다. 이 테스트는 **트랙 추가를 강제로
  알아차리게 하는 장치**이므로 키를 늘리는 것 자체가 절차의 일부다.
- 시드 SQL 회귀 테스트(`SeedSqlTest`·`ContentSeedSqlTest`)는 신규 트랙 행이 늘어난 만큼 갱신한다.

## 완료 조건

1. 새 게이트 5종이 `QuestionValidator` 에 있고, 각각 **훼손 사본으로 red 가 실증**됐다.
2. 기존 5트랙 500문항이 새 게이트를 통과한다(통과하지 못하면 그 자체가 발견이며, 교정 후 통과).
3. Claude 검수 단계가 트랙 단위로 동작하고 리포트가 `generated/review/` 에 남는다.
4. `PYTHON_BACKEND` 문항 100(MCQ 70 · CODE_READING 30) · 콘텐츠 30(INTRO 8 · INTERMEDIATE 14 ·
   ADVANCED 8, 코드블록 10개 이상) · 임베딩이 승인 JSONL 에 있고 검증기가 green 이다.
5. 운영에 적재되고 **재측정 게이트를 통과**한다(문항 100 · 콘텐츠 30 · `DISTINCT options` > 1 ·
   임베딩 커버리지 100%).
6. 이용자가 진단 시작 화면에서 **6트랙**을 고를 수 있다.

## 이 스펙 밖

- **`NODE_TYPESCRIPT` · `DATA_AI` 트랙 생성.** 같은 절차의 반복이다. CHECK 는 이번에 함께 열리므로
  남는 일은 지침 md · 생성 · 검수 · 시드 · 카탈로그 1키다.
- **기존 5트랙 문항의 전면 재생성.** 새 게이트가 잡는 것만 교정한다.
- **트랙별 학습 경로 프롬프트 튜닝.** 경로 생성은 트랙을 파라미터로 받을 뿐 프롬프트는 공통이다.
- **기존 이용자의 재진단 진입점.** 트랙 선택 스펙에서 이미 범위 밖으로 명시했다 — 온보딩을 마친
  이용자는 `/diagnostic` 에 도달할 수 없어, 새 트랙도 **신규 온보딩·게스트에게만** 열린다.
