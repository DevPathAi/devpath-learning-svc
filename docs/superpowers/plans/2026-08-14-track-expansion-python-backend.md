# 트랙 확장 (게이트 + PYTHON_BACKEND) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 문항 생성 파이프라인에 품질 게이트 5종과 Claude 검수 단계를 세우고, 그 위에서 `PYTHON_BACKEND` 트랙을 문항 100·콘텐츠 30·임베딩·운영 적재까지 끝낸다.

**Architecture:** `QuestionValidator` 에 error 게이트를 더하고(중복 3종 · 정답키 편향 · 한국어), 자동 검증 **뒤**에 오프라인 Claude 검수 커맨드를 둔다. 트랙 값은 `devpath-shared` 마이그레이션으로 3값을 한 번에 열되 Quota 에는 파일럿 트랙만 넣는다. 운영 적재는 기존 5트랙을 건드리지 않는 **증분 INSERT** 다.

**Tech Stack:** Java 21 · Spring Boot 4.0.7 · Gradle(Kotlin DSL) · JUnit 5 + AssertJ · Flyway · Ollama(qwen2.5:14b · nomic-embed-text) · Flutter(프론트 카탈로그)

**Spec:** `devpath-learning-svc/docs/superpowers/specs/2026-08-14-track-expansion-python-backend-design.md`

## Global Constraints

- **모든 git/파일 명령에 절대경로 또는 `-C <레포 절대경로>` 를 쓴다.** `cd` 후 상대경로로 후속 명령을 내지 말 것 — 에이전트 스레드는 bash 호출 사이 cwd 가 리셋되어 조용히 다른 레포에서 실행될 수 있다. `gh` 에는 항상 `--repo DevPathAi/<레포명>` 을 명시한다.
- 레포 절대경로: learning-svc `D:\workspace\dpa\devpath-learning-svc` · shared `D:\workspace\dpa\devpath-shared` · frontend `D:\workspace\dpa\devpath-frontend`
- **`main` 직접 push 금지.** 작업 브랜치 → `develop` PR. learning-svc 작업 브랜치는 `docs/track-expansion-python-backend` 에서 이어간다.
- **트랙 값은 `VARCHAR(20)` 이하**: `PYTHON_BACKEND`(14) · `NODE_TYPESCRIPT`(15) · `DATA_AI`(7).
- **Gradle 은 `UP-TO-DATE` 로 테스트를 건너뛴다.** 항상 `cleanTest test` 로 돌리고, 결과 XML 건수로 실행을 확인한다: `Get-ChildItem <repo>\build\test-results\test -Filter *.xml` 의 `tests` 합.
- learning-svc 통합 테스트는 **로컬 Postgres(5432)·Redis(6379) 가 필요**하다. 꺼져 있으면 `docker start devpath-local-postgres-1 devpath-local-redis-1`.
- **CI 는 Ollama 도 Claude 도 부르지 않는다.** 생성·검수는 오프라인 수동 실행이고, CI 가 보는 것은 커밋된 JSONL·SQL·리포트뿐이다.
- 비밀값(Claude API 키)은 절대 커밋하지 않는다. 환경변수로 받는다.
- 커밋 메시지는 Conventional Commits, 본문은 한국어.

---

### Task 1: 검증기 픽스처를 현실적으로 만든다 (게이트의 선행조건)

`QuestionValidatorTest.validQuestions()` 는 500문항을 만들면서 **전부 `List.of("A","B","C","D")` 를 쓰고 content 에 한글이 하나도 없다.** 2026-08-13 운영 사고(500문항이 선택지 한 벌 공유)와 **똑같은 모양**이다. 게이트를 먼저 넣으면 기존 테스트 9개가 전부 red 가 되는데, 그것은 게이트의 문제가 아니라 픽스처의 문제다. 그래서 픽스처를 먼저 고친다.

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\test\java\ai\devpath\learning\contentgen\question\QuestionValidatorTest.java:122-137`

**Interfaces:**
- Consumes: `ApprovedQuestion(track, questionType, content, options, answerKey, bloomLevel, difficulty, conceptTags, explanation)` · `ApprovedQuestion.AnswerKey(Integer correct)`
- Produces: `QuestionValidatorTest.validQuestions()` — 트랙 5종 × 100문항. **선택지 벌이 문항마다 고유하고, content 에 한글이 있고, 정답키가 균등**한 픽스처. Task 2·3 의 테스트가 이것을 기반으로 훼손 사본을 만든다.

- [ ] **Step 1: 픽스처 생성 함수를 고친다**

`question(String track, int index)` 를 아래로 교체한다. 선택지는 인덱스를 섞어 넣어 벌이 고유해지고, content 에 한글이 들어간다.

```java
  private static ApprovedQuestion question(String track, int index) {
    var type = index < 70 ? "MCQ" : "CODE_READING";
    var bloom = bloom(index);
    var difficulty = difficulty(index);
    var tag = track.toLowerCase().replace('_', '-') + "-" + (index % 10);
    return new ApprovedQuestion(
        track,
        type,
        track + " 진단 문항 " + index + " — 무엇이 맞는가?",
        List.of(
            "보기 가 " + track + "-" + index,
            "보기 나 " + track + "-" + index,
            "보기 다 " + track + "-" + index,
            "보기 라 " + track + "-" + index),
        new ApprovedQuestion.AnswerKey(index % 4),
        bloom,
        difficulty,
        List.of(tag),
        "해설 " + index);
  }
```

- [ ] **Step 2: 기존 테스트가 그대로 통과하는지 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: PASS. XML 에서 `tests="9" failures="0"`.

확인: `$x=[xml](Get-Content D:\workspace\dpa\devpath-learning-svc\build\test-results\test\TEST-ai.devpath.learning.contentgen.question.QuestionValidatorTest.xml); "$($x.testsuite.tests) $($x.testsuite.failures)"`

- [ ] **Step 3: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add src/test/java/ai/devpath/learning/contentgen/question/QuestionValidatorTest.java
git -C D:/workspace/dpa/devpath-learning-svc commit -m "test(contentgen): 검증기 픽스처를 현실적인 문항으로 바꾼다

500문항이 전부 같은 선택지 한 벌을 쓰고 한글이 하나도 없었다 —
2026-08-13 운영 사고와 똑같은 모양이다. 곧 추가할 중복·한국어
게이트를 이 픽스처로는 시험할 수 없다."
```

---

### Task 2: 중복 게이트 3종

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\QuestionValidator.java`
- Test: `D:\workspace\dpa\devpath-learning-svc\src\test\java\ai\devpath\learning\contentgen\question\QuestionValidatorTest.java`

**Interfaces:**
- Consumes: Task 1 의 `validQuestions()`
- Produces: `QuestionValidator` 가 error 로 내는 문구 — `"duplicate option set"` · `"duplicate content"` · `"duplicate option inside question"`. Task 4 가 이 문구로 실측 결과를 센다.

- [ ] **Step 1: 실패하는 테스트 3개를 쓴다**

`QuestionValidatorTest` 에 추가한다.

```java
  @Test
  void rejectsDuplicateOptionSetInsideTrack() {
    var questions = validQuestions();
    questions.set(1, withOptions(questions.get(1), questions.get(0).options()));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate option set").contains("BACKEND_SPRING"));
  }

  @Test
  void rejectsDuplicateContent() {
    var questions = validQuestions();
    questions.set(1, withContent(questions.get(1), questions.get(0).content()));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate content"));
  }

  @Test
  void rejectsDuplicateOptionInsideQuestion() {
    var questions = validQuestions();
    var first = questions.get(0);
    questions.set(0, withOptions(first, List.of("같은 보기", "같은 보기", "다른 보기", "또 다른 보기")));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("duplicate option inside question"));
  }

  private static ApprovedQuestion withOptions(ApprovedQuestion q, List<String> options) {
    return new ApprovedQuestion(q.track(), q.questionType(), q.content(), options, q.answerKey(),
        q.bloomLevel(), q.difficulty(), q.conceptTags(), q.explanation());
  }

  private static ApprovedQuestion withContent(ApprovedQuestion q, String content) {
    return new ApprovedQuestion(q.track(), q.questionType(), content, q.options(), q.answerKey(),
        q.bloomLevel(), q.difficulty(), q.conceptTags(), q.explanation());
  }
```

- [ ] **Step 2: 테스트가 실패하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: FAIL — `12 tests completed, 3 failed`. 세 테스트 모두 "Expecting any element ... to satisfy" 로 실패한다.

- [ ] **Step 3: 게이트를 구현한다**

`QuestionValidator` 에 아래를 추가한다. import 는 `java.util.HashMap` · `java.util.HashSet` · `java.util.LinkedHashSet` 가 필요하다.

```java
  /** 앞뒤 공백 제거 + 연속 공백 1칸. 대소문자는 건드리지 않는다(코드 문항이 있다). */
  private static String normalize(String value) {
    return value == null ? "" : value.trim().replaceAll("\\s+", " ");
  }

  private static List<String> normalizeAll(List<String> values) {
    return values == null ? List.of() : values.stream().map(QuestionValidator::normalize).toList();
  }

  private void validateDuplicateOptionSets(List<ApprovedQuestion> questions, List<String> errors) {
    var byTrack = new HashMap<String, Map<List<String>, Integer>>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.track() == null || q.options() == null) continue;
      byTrack.computeIfAbsent(q.track(), t -> new HashMap<>())
          .merge(normalizeAll(q.options()), 1, Integer::sum);
    }
    for (var track : byTrack.entrySet()) {
      for (var set : track.getValue().entrySet()) {
        if (set.getValue() > 1) {
          errors.add(track.getKey() + ": duplicate option set shared by "
              + set.getValue() + " questions");
        }
      }
    }
  }

  private void validateDuplicateContents(List<ApprovedQuestion> questions, List<String> errors) {
    var counts = new HashMap<String, Integer>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.content() == null) continue;
      counts.merge(normalize(q.content()), 1, Integer::sum);
    }
    for (var entry : counts.entrySet()) {
      if (entry.getValue() > 1) {
        errors.add("duplicate content shared by " + entry.getValue() + " questions");
      }
    }
  }
```

`validateQuestion(int line, ApprovedQuestion q, List<String> errors)` 안, 기존 options 검사 바로 뒤에 보기 내부 중복을 넣는다.

```java
    if (q.options() != null) {
      var normalized = normalizeAll(q.options());
      if (new HashSet<>(normalized).size() != normalized.size()) {
        errors.add("line " + line + ": duplicate option inside question");
      }
    }
```

`validate(...)` 안 `validateTrackQuotas(questions, errors);` 다음 줄에 두 호출을 넣는다.

```java
    validateDuplicateOptionSets(questions, errors);
    validateDuplicateContents(questions, errors);
```

- [ ] **Step 4: 테스트가 통과하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: PASS — XML `tests="12" failures="0"`.

- [ ] **Step 5: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add src/contentGen/java/ai/devpath/learning/contentgen/question/QuestionValidator.java src/test/java/ai/devpath/learning/contentgen/question/QuestionValidatorTest.java
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(contentgen): 문항 중복 게이트 3종을 세운다

선택지 벌 중복(트랙 내)·content 중복(전체)·한 문항 안 보기 중복.
검증기에 「중복」이라는 개념이 아예 없어 500문항이 선택지 한 벌을
공유해도 통과했다 — 2026-08-13 운영 사고가 살아남은 경로다."
```

---

### Task 3: 정답키 편향·한국어 게이트

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\QuestionValidator.java`
- Test: `D:\workspace\dpa\devpath-learning-svc\src\test\java\ai\devpath\learning\contentgen\question\QuestionValidatorTest.java`

**Interfaces:**
- Consumes: Task 1 의 `validQuestions()`, Task 2 의 `withContent` · `normalize`
- Produces: error 문구 `"answer key bias"` · `"content must contain Korean"`

**임계값 근거(실측):** 기존 5트랙 500문항의 정답키 최대 쏠림은 트랙별 32~40% 였다(`FULLSTACK` 38 · `BACKEND_SPRING` 40 · `DEVOPS` 40 · `MOBILE_FLUTTER` 32 · `FRONTEND_REACT` 35). 임계 50% 는 이들을 통과시키면서 「눈에 띄는 쏠림」을 잡는다. 4지선다 균등은 25% 다.

- [ ] **Step 1: 실패하는 테스트 2개를 쓴다**

```java
  @Test
  void rejectsAnswerKeyBias() {
    var questions = validQuestions();
    for (int i = 0; i < questions.size(); i++) {
      if ("BACKEND_SPRING".equals(questions.get(i).track())) {
        questions.set(i, withCorrect(questions.get(i), 0));
      }
    }

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("answer key bias").contains("BACKEND_SPRING"));
  }

  @Test
  void rejectsContentWithoutKorean() {
    var questions = validQuestions();
    questions.set(0, withContent(questions.get(0), "What is the default bean scope?"));

    var report = validator.validate(questions);

    assertThat(report.errors()).anySatisfy(error ->
        assertThat(error).contains("content must contain Korean"));
  }
```

- [ ] **Step 2: 테스트가 실패하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: FAIL — `14 tests completed, 2 failed`.

- [ ] **Step 3: 게이트를 구현한다**

클래스 상단 상수에 추가한다.

```java
  private static final Pattern HANGUL = Pattern.compile("[\\uAC00-\\uD7A3]");
  private static final double MAX_ANSWER_KEY_SHARE = 0.5;
```

`validateQuestion(...)` 안 content 검사 뒤에 한국어 게이트를 넣는다.

```java
    if (!blank(q.content()) && !HANGUL.matcher(q.content()).find()) {
      errors.add("line " + line + ": content must contain Korean");
    }
```

편향 게이트를 추가하고 `validate(...)` 에서 부른다.

```java
  private void validateAnswerKeyBias(List<ApprovedQuestion> questions, List<String> errors) {
    var byTrack = new HashMap<String, Map<Integer, Integer>>();
    for (ApprovedQuestion q : questions) {
      if (q == null || q.track() == null || q.answerKey() == null
          || q.answerKey().correct() == null) continue;
      byTrack.computeIfAbsent(q.track(), t -> new HashMap<>())
          .merge(q.answerKey().correct(), 1, Integer::sum);
    }
    for (var track : byTrack.entrySet()) {
      int total = track.getValue().values().stream().mapToInt(Integer::intValue).sum();
      if (total == 0) continue;
      for (var position : track.getValue().entrySet()) {
        double share = (double) position.getValue() / total;
        if (share > MAX_ANSWER_KEY_SHARE) {
          errors.add(track.getKey() + ": answer key bias — position " + position.getKey()
              + " holds " + position.getValue() + "/" + total);
        }
      }
    }
  }
```

`validate(...)` 안 Task 2 의 두 호출 다음 줄:

```java
    validateAnswerKeyBias(questions, errors);
```

- [ ] **Step 4: 테스트가 통과하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: PASS — XML `tests="14" failures="0"`.

- [ ] **Step 5: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add src/contentGen/java/ai/devpath/learning/contentgen/question/QuestionValidator.java src/test/java/ai/devpath/learning/contentgen/question/QuestionValidatorTest.java
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(contentgen): 정답키 편향·한국어 게이트를 세운다

임계 50%는 실측으로 정했다 — 기존 5트랙의 정답키 최대 쏠림이
32~40%라 이들은 통과하고 눈에 띄는 쏠림만 잡는다.
한국어 게이트는 「로컬은 한국어, 운영만 영어」였던 구멍을 막는다."
```

---

### Task 4: 기존 5트랙에 게이트를 돌리고, 걸린 문항을 교정한다

게이트가 살아 있는지 **실제 데이터로** 확인한다. 계획 작성 시점의 시뮬레이션(jq)에서는 **선택지 벌 중복 4종 8문항**이 나왔고 나머지 4게이트는 0건이었다. 검증기 실행으로 이 숫자를 재확인하고 교정한다.

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\generated\approved\questions.jsonl` (걸린 문항의 선택지만)
- Modify: 재생성되는 시드 SQL 5개 (`makeQuestionSeedSql` 산출물)

**Interfaces:**
- Consumes: Task 2·3 의 게이트
- Produces: 새 게이트를 통과하는 `questions.jsonl` — Task 8 이 여기에 100문항을 덧붙인다.

- [ ] **Step 1: 승인 JSONL 에 검증기를 돌린다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc validateQuestions`
Expected: **FAIL**. `duplicate option set shared by 2 questions` 형태의 error 가 나온다(계획 시점 시뮬레이션 기준 4건). 출력 전문을 파일로 남긴다.

출력이 예상과 다르면(0건이거나 다른 게이트가 걸리면) **그 자체가 발견이다.** 숫자를 기록하고 Step 2 를 그에 맞춰 진행한다.

- [ ] **Step 2: 걸린 문항의 선택지를 교정한다**

각 중복 쌍에서 **뒤쪽 문항의 선택지를 그 문항 내용에 맞게 다시 쓴다.** 문항 자체(`content`)는 서로 다르므로 선택지만 고치면 된다. 정답 위치가 바뀌면 `answerKey.correct` 도 함께 고친다.

중복 쌍을 찾는 명령(개행 안전 — `content` 에 코드 스니펫 개행이 있어 `jq -r` 로 뽑으면 줄이 쪼개져 집계가 무효가 된다):

```bash
cd /d/workspace/dpa/devpath-learning-svc/tools/content-gen/generated/approved
printf '%s\n' '[.track, (.options|map(gsub("^[[:space:]]+|[[:space:]]+$";""))|tostring)]|@json' > /tmp/dup.jq
jq -r -f /tmp/dup.jq questions.jsonl | sort | uniq -c | awk '$1>1'
```

- [ ] **Step 3: 검증기가 통과하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc validateQuestions`
Expected: PASS (errors 없음. warning 은 허용).

- [ ] **Step 4: 게이트의 판별력을 훼손 사본으로 실증한다**

승인 JSONL 을 건드리지 않고 사본으로 시험한다.

```bash
cd /d/workspace/dpa/devpath-learning-svc/tools/content-gen/generated/approved
cp questions.jsonl /tmp/probe.jsonl
# 1번 문항의 선택지를 2번 문항에 복사해 중복을 만든다
jq -c --argjson opts "$(jq -c '.options' <(head -1 questions.jsonl))" \
   'if input_line_number == 2 then .options = $opts else . end' questions.jsonl > /tmp/probe.jsonl
```

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc validateQuestions --args="/tmp/probe.jsonl"`

`validateQuestions` 태스크는 인자가 고정돼 있으므로(`build.gradle.kts:115`), 이 단계에서는 태스크에 `args` 오버라이드가 되지 않으면 임시로 승인 파일을 백업·교체해 실행한 뒤 **반드시 원복**한다. 원복 확인: `git -C D:/workspace/dpa/devpath-learning-svc diff --stat -- tools/content-gen/generated/approved/questions.jsonl` 가 교정분 외에 변화가 없어야 한다.

Expected: FAIL — `duplicate option set` error.

- [ ] **Step 5: 시드 SQL 을 재생성한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc makeQuestionSeedSql`
Expected: 5개 파일이 갱신된다(`tools/.../question_bank_seed.sql` · `src/main/resources/db/seed/...` · `src/test/resources/seed/...` 3종).

- [ ] **Step 6: 전체 테스트를 돌린다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test`
Expected: PASS. 시드 회귀 테스트(`SeedSqlTest`·`QuestionBankKoreanSeedTest`)가 갱신된 SQL 로 통과해야 한다. 실패하면 그 테스트가 무엇을 단언하는지 읽고, 교정한 문항 때문인지 확인한다.

- [ ] **Step 7: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add tools/content-gen/generated src/main/resources/db/seed src/test/resources/seed
git -C D:/workspace/dpa/devpath-learning-svc commit -m "fix(contentgen): 새 게이트에 걸린 기존 문항의 선택지를 교정한다

게이트를 세우자 기존 5트랙에서 선택지 벌 중복이 실제로 걸렸다.
게이트가 살아 있다는 증거이자, 지금까지 아무도 몰랐다는 증거다."
```

---

### Task 5: 생성 커맨드에 트랙 인자를 넣고 Python 트랙 지침을 쓴다

`GenerateQuestionsCommand:17` 은 `QuestionQuota.TRACKS` **전체**를 순회한다. 파일럿 트랙 하나만 생성하려면 트랙을 인자로 받아야 한다. 그리고 Quota 에는 아직 `PYTHON_BACKEND` 를 넣지 않으므로(넣으면 검증기가 「100문항이어야 한다」로 즉시 실패한다), 커맨드는 **Quota 목록과 무관하게** 인자로 받은 트랙을 생성할 수 있어야 한다.

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\GenerateQuestionsCommand.java`
- Modify: `D:\workspace\dpa\devpath-learning-svc\build.gradle.kts:132-162` (`generateQuestionsLocal`)
- Create: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\prompts\tracks\python-backend.md`

**Interfaces:**
- Consumes: `OllamaQuestionDraftClient.generate(String track, int count, String prompt)`
- Produces: `generateQuestionsLocal -Ptrack=PYTHON_BACKEND` — 지정 트랙만 생성해 `tools/content-gen/generated/raw/questions.draft.jsonl` 에 쓴다. 인자가 없으면 기존대로 `QuestionQuota.TRACKS` 전체.

- [ ] **Step 1: 커맨드가 트랙 인자를 받게 한다**

`GenerateQuestionsCommand.main` 을 아래로 바꾼다. 트랙 슬러그로 지침 파일을 찾는 규칙(`slug()`)은 그대로 쓴다.

```java
  public static void main(String[] args) throws Exception {
    var model = args.length > 0 && !args[0].isBlank() ? args[0] : "qwen2.5:7b";
    var only = args.length > 1 && !args[1].isBlank() ? args[1] : null;
    var tracks = only == null ? QuestionQuota.TRACKS : List.of(only);
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var client = new OllamaQuestionDraftClient(baseUrl, model);
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/question-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/questions.draft.jsonl");
    Files.createDirectories(output.getParent());

    var draft = new StringBuilder();
    for (String track : tracks) {
      var trackPrompt = Files.readString(
          Path.of("tools/content-gen/prompts/tracks/" + slug(track) + ".md"));
      draft.append(client.generate(track, QuestionQuota.PER_TRACK, systemPrompt + "\n\n" + trackPrompt));
      if (!draft.toString().endsWith("\n")) {
        draft.append("\n");
      }
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft questions for " + tracks + " to " + output);
  }
```

`import java.util.List;` 를 추가한다. `QuestionQuota.PER_TRACK` 은 package-private 이므로 같은 패키지에서 접근 가능하다.

- [ ] **Step 2: gradle 태스크가 `-Ptrack` 을 넘기게 한다**

`build.gradle.kts` 의 `generateQuestionsLocal` 블록에서 `args(...)` 를 아래로 바꾼다. 기존 모델 인자 전달 방식(`-Pollama.model`)은 그대로 둔다.

```kotlin
	args(
		(project.findProperty("ollama.model") as String? ?: "qwen2.5:7b"),
		(project.findProperty("track") as String? ?: "")
	)
```

- [ ] **Step 3: 컴파일을 확인한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc compileContentGenJava`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Python 백엔드 출제 지침을 쓴다**

`tools/content-gen/prompts/tracks/python-backend.md` 를 만든다. 기존 5개(각 ~1.7KB)와 같은 구조 — 첫 줄에 트랙 선언, 「핵심 개념 목록」, 「CODE_READING 지침」. **데이터·ML 은 명시적으로 제외**한다(`DATA_AI` 트랙과 겹치지 않게).

```markdown
트랙: PYTHON_BACKEND (파이썬 웹 백엔드 — Django·FastAPI)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.
**데이터 분석·머신러닝·노트북은 이 트랙이 아니다**(DATA_AI 트랙 소관). pandas·모델 학습을 묻지 않는다.

## 핵심 개념 목록

- **파이썬 언어**: 가변/불변, 얕은·깊은 복사, 데코레이터, 컨텍스트 매니저, 제너레이터, GIL, 예외 계층, 타입 힌트.
- **비동기**: `async`/`await`, 이벤트 루프, 코루틴 vs 스레드, `asyncio.gather`, 블로킹 호출이 이벤트 루프를 멈추는 문제, ASGI vs WSGI.
- **Django**: ORM 쿼리셋 지연 평가, **N+1과 `select_related`/`prefetch_related`**, 마이그레이션, 미들웨어, 시그널, `settings` 분리, 트랜잭션(`atomic`).
- **FastAPI**: Pydantic 모델 검증, 의존성 주입(`Depends`), 응답 모델, 백그라운드 태스크, 미들웨어, OpenAPI 스키마 생성.
- **웹/HTTP API**: 상태 코드, 인증(세션·JWT), CORS, 페이지네이션, 멱등성, 파일 업로드.
- **영속성**: SQLAlchemy 세션 수명, 커넥션 풀, 인덱스, 트랜잭션 격리, 마이그레이션 도구(Alembic).
- **테스트·품질**: pytest 픽스처, 목·패치, 커버리지, 의존성 관리(`pip`·`poetry`·가상환경), 패키지 구조.
- **배포·운영**: gunicorn/uvicorn 워커, 프로세스 vs 스레드, 환경변수 설정, 로깅, 정적 파일, 헬스체크.

## CODE_READING 지침

짧은 파이썬 스니펫(5~15줄)을 `content` 에 `\n` 으로 넣고, 가변 기본 인자·얕은 복사·이벤트 루프를 막는 블로킹 호출·쿼리셋 N+1·`atomic` 밖 예외 처리·데코레이터 적용 순서 등 **위 개념의 미묘한 동작·버그**를 읽어내게 한다. `content` 는 코드만 두지 말고 **한국어 질문 문장을 함께** 넣는다.
```

- [ ] **Step 5: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add src/contentGen/java/ai/devpath/learning/contentgen/question/GenerateQuestionsCommand.java build.gradle.kts tools/content-gen/prompts/tracks/python-backend.md
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(contentgen): 트랙 단위 생성과 Python 백엔드 출제 지침

생성 커맨드가 Quota 목록 전체를 돌던 것을 -Ptrack 으로 좁힌다.
Quota 에 트랙을 넣는 것은 문항 100개가 준비된 뒤여야 한다 —
넣는 순간 검증기가 「100문항이어야 한다」로 실패하기 때문이다."
```

---

### Task 6: Claude 검수 커맨드

자동 검증기가 통과시킨 뒤 도는 오프라인 도구다. **승인 JSONL 을 덮어쓰지 않는다** — 리포트만 낸다.

**Files:**
- Create: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\ReviewQuestionsCommand.java`
- Create: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\QuestionReviewPrompt.java`
- Test: `D:\workspace\dpa\devpath-learning-svc\src\test\java\ai\devpath\learning\contentgen\question\QuestionReviewPromptTest.java`
- Modify: `D:\workspace\dpa\devpath-learning-svc\build.gradle.kts` (태스크 등록)

**Interfaces:**
- Consumes: `QuestionJsonlReader` (기존, 승인 JSONL 읽기) · `ApprovedQuestion`
- Produces: `QuestionReviewPrompt.build(String track, List<ApprovedQuestion> batch)` → `String` · `reviewQuestionsLocal -Ptrack=PYTHON_BACKEND` → `tools/content-gen/generated/review/<track>-review.json`

**배치 크기:** 문항 길이 편차가 크므로(CODE_READING 은 스니펫 포함) **고정 개수가 아니라 문자 예산**으로 자른다. 예산 40,000자, 한 문항이 예산을 넘으면 그 문항만 단독 배치.

- [ ] **Step 1: 프롬프트 빌더의 실패하는 테스트를 쓴다**

```java
package ai.devpath.learning.contentgen.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionReviewPromptTest {

  private static ApprovedQuestion question(String content) {
    return new ApprovedQuestion("PYTHON_BACKEND", "MCQ", content,
        List.of("보기 하나", "보기 둘", "보기 셋", "보기 넷"),
        new ApprovedQuestion.AnswerKey(0), "APPLY", 0.5, List.of("python-async"), "해설");
  }

  @Test
  void promptContainsTrackAndEveryQuestionWithIndex() {
    var prompt = QuestionReviewPrompt.build("PYTHON_BACKEND",
        List.of(question("첫 문항입니다"), question("둘째 문항입니다")));

    assertThat(prompt).contains("PYTHON_BACKEND");
    assertThat(prompt).contains("첫 문항입니다").contains("둘째 문항입니다");
    assertThat(prompt).contains("\"index\": 0").contains("\"index\": 1");
  }

  @Test
  void promptAsksForTheFourReviewAxes() {
    var prompt = QuestionReviewPrompt.build("PYTHON_BACKEND", List.of(question("문항")));

    assertThat(prompt).contains("사실오류").contains("정답키").contains("정답을 흘리는")
        .contains("한국어");
  }

  @Test
  void batchesSplitByCharacterBudget() {
    var big = question("가".repeat(30_000));
    var batches = QuestionReviewPrompt.batch(List.of(big, big, big), 40_000);

    assertThat(batches).hasSize(3);
    assertThat(batches.get(0)).hasSize(1);
  }
}
```

- [ ] **Step 2: 테스트가 실패하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionReviewPromptTest"`
Expected: FAIL — 컴파일 실패(`QuestionReviewPrompt` 없음).

- [ ] **Step 3: 프롬프트 빌더를 구현한다**

```java
package ai.devpath.learning.contentgen.question;

import java.util.ArrayList;
import java.util.List;

/** Claude 검수용 프롬프트와 배치 분할. 네트워크를 모른다 — 순수 함수라 테스트가 쉽다. */
public final class QuestionReviewPrompt {

  private QuestionReviewPrompt() {}

  public static String build(String track, List<ApprovedQuestion> batch) {
    var sb = new StringBuilder();
    sb.append("당신은 개발자 진단 문항을 검수한다. 트랙: ").append(track).append("\n\n");
    sb.append("각 문항에 대해 아래 네 가지를 본다.\n");
    sb.append("1. 사실오류 — 기술적으로 틀린 서술\n");
    sb.append("2. 정답키 오류 — answerKey.correct 가 가리키는 보기가 정답이 아님\n");
    sb.append("3. 정답을 흘리는 선택지 — 길이·구체성만으로 정답이 보이는 문항\n");
    sb.append("4. 어색한 한국어 — 번역투·비문\n\n");
    sb.append("문제가 없는 문항은 결과에 넣지 않는다. ");
    sb.append("결과는 JSON 배열만 출력한다(설명 금지): ");
    sb.append("[{\"index\": 0, \"axis\": \"정답키\", \"detail\": \"...\", \"suggestion\": \"...\"}]\n\n");
    for (int i = 0; i < batch.size(); i++) {
      var q = batch.get(i);
      sb.append("{\"index\": ").append(i)
          .append(", \"content\": ").append(quote(q.content()))
          .append(", \"options\": ").append(q.options())
          .append(", \"correct\": ").append(q.answerKey().correct())
          .append("}\n");
    }
    return sb.toString();
  }

  public static List<List<ApprovedQuestion>> batch(List<ApprovedQuestion> questions, int budget) {
    var batches = new ArrayList<List<ApprovedQuestion>>();
    var current = new ArrayList<ApprovedQuestion>();
    int size = 0;
    for (ApprovedQuestion q : questions) {
      int cost = weight(q);
      if (!current.isEmpty() && size + cost > budget) {
        batches.add(List.copyOf(current));
        current = new ArrayList<>();
        size = 0;
      }
      current.add(q);
      size += cost;
    }
    if (!current.isEmpty()) {
      batches.add(List.copyOf(current));
    }
    return List.copyOf(batches);
  }

  private static int weight(ApprovedQuestion q) {
    int cost = q.content() == null ? 0 : q.content().length();
    if (q.options() != null) {
      for (String option : q.options()) {
        cost += option == null ? 0 : option.length();
      }
    }
    return cost;
  }

  private static String quote(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
  }
}
```

- [ ] **Step 4: 테스트가 통과하는 것을 확인**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionReviewPromptTest"`
Expected: PASS — XML `tests="3" failures="0"`.

- [ ] **Step 5: 커맨드와 gradle 태스크를 만든다**

`ReviewQuestionsCommand` 를 만든다. API 키가 없으면 **프롬프트만 파일로 떨어뜨리고 종료**한다 — 키 없이도 사람이 수동 검수할 수 있고, CI 가 이 태스크를 잘못 부르더라도 네트워크를 타지 않는다.

```java
package ai.devpath.learning.contentgen.question;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class ReviewQuestionsCommand {

  private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";
  private static final String MODEL = "claude-opus-5";
  private static final int CHAR_BUDGET = 40_000;

  public static void main(String[] args) throws Exception {
    var approved = Path.of(args[0]);
    var track = args.length > 1 ? args[1] : "";
    var outDir = Path.of(args.length > 2 ? args[2] : "tools/content-gen/generated/review");
    if (track.isBlank()) {
      System.err.println("track is required: -Ptrack=PYTHON_BACKEND");
      System.exit(2);
    }
    Files.createDirectories(outDir);

    List<ApprovedQuestion> all = new QuestionJsonlReader().read(approved);
    var target = all.stream().filter(q -> track.equals(q.track())).toList();
    if (target.isEmpty()) {
      System.err.println("no questions for track " + track);
      System.exit(2);
    }
    var batches = QuestionReviewPrompt.batch(target, CHAR_BUDGET);
    System.out.println(track + ": " + target.size() + " questions in " + batches.size() + " batches");

    var apiKey = System.getenv("ANTHROPIC_API_KEY");
    if (apiKey == null || apiKey.isBlank()) {
      for (int i = 0; i < batches.size(); i++) {
        Files.writeString(outDir.resolve(track + "-prompt-" + i + ".txt"),
            QuestionReviewPrompt.build(track, batches.get(i)));
      }
      System.out.println("ANTHROPIC_API_KEY 가 없어 프롬프트만 썼다: " + outDir);
      return;
    }

    var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
    var merged = new StringBuilder("[\n");
    for (int i = 0; i < batches.size(); i++) {
      var prompt = QuestionReviewPrompt.build(track, batches.get(i));
      var body = "{\"model\":\"" + MODEL + "\",\"max_tokens\":8000,\"messages\":[{\"role\":\"user\","
          + "\"content\":" + jsonString(prompt) + "}]}";
      var request = HttpRequest.newBuilder(URI.create(ENDPOINT))
          .header("content-type", "application/json")
          .header("x-api-key", apiKey)
          .header("anthropic-version", "2023-06-01")
          .timeout(Duration.ofMinutes(10))
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new IllegalStateException("Claude " + response.statusCode() + ": " + response.body());
      }
      Files.writeString(outDir.resolve(track + "-raw-" + i + ".json"), response.body());
      merged.append("  {\"batch\": ").append(i).append(", \"raw\": ")
          .append(jsonString(response.body())).append("},\n");
      System.out.println("batch " + i + " reviewed");
    }
    merged.append("]\n");
    var report = outDir.resolve(track + "-review.json");
    Files.writeString(report, merged.toString());
    System.out.println("Wrote " + report);
  }

  private static String jsonString(String value) {
    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"")
        .replace("\n", "\\n").replace("\r", "").replace("\t", "\\t") + "\"";
  }
}
```

`QuestionJsonlReader.read(Path)` 의 시그니처는 실측으로 확인했다 — `public List<ApprovedQuestion> read(Path path) throws IOException`. 위 코드가 그대로 맞는다.

`build.gradle.kts` 에 `validateQuestions` 옆으로 등록한다.

```kotlin
tasks.register<JavaExec>("reviewQuestionsLocal") {
	group = "content generation"
	description = "Review approved questions with Claude. Do not run in CI."
	classpath = contentGenSourceSet.runtimeClasspath
	mainClass.set("ai.devpath.learning.contentgen.question.ReviewQuestionsCommand")
	args(
		"tools/content-gen/generated/approved/questions.jsonl",
		(project.findProperty("track") as String? ?: ""),
		"tools/content-gen/generated/review"
	)
}
```

- [ ] **Step 6: 키 없이 도는지 확인한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc reviewQuestionsLocal -Ptrack=BACKEND_SPRING`
Expected: BUILD SUCCESSFUL. `tools/content-gen/generated/review/` 에 프롬프트 파일이 생기고, 「ANTHROPIC_API_KEY 가 없어 프롬프트만 썼다」는 안내가 출력된다.

- [ ] **Step 7: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add src/contentGen/java/ai/devpath/learning/contentgen/question/ReviewQuestionsCommand.java src/contentGen/java/ai/devpath/learning/contentgen/question/QuestionReviewPrompt.java src/test/java/ai/devpath/learning/contentgen/question/QuestionReviewPromptTest.java build.gradle.kts
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(contentgen): Claude 검수 커맨드를 더한다

자동 검증기가 통과시킨 뒤 도는 오프라인 도구다. 승인 JSONL 을
덮어쓰지 않고 리포트만 낸다 — approved/ 는 이름 그대로
사람이 승인한 것이어야 한다."
```

---

### Task 7: shared 마이그레이션으로 트랙 3값을 연다

**별도 레포다.** `D:\workspace\dpa\devpath-shared` 에서 `develop` 분기 → PR.

**Files:**
- Create: `D:\workspace\dpa\devpath-shared\src\main\resources\db\migration\V202608141001__track_expansion_check.sql`

**Interfaces:**
- Produces: `question_bank`·`assessments`·`learning_paths`·`contents`·`user_profiles` 의 track CHECK 가 8값을 허용한다. Task 10 의 시드 INSERT 가 이것을 전제한다.

- [ ] **Step 1: 브랜치를 만든다**

```bash
git -C D:/workspace/dpa/devpath-shared checkout develop
git -C D:/workspace/dpa/devpath-shared pull --ff-only
git -C D:/workspace/dpa/devpath-shared checkout -b feat/track-expansion-check
```

- [ ] **Step 2: 마이그레이션을 쓴다**

기존 제약 이름은 실측했다 — `chk_qb_track`(`question_bank`) · `chk_assessments_track`(`assessments`) · `chk_lp_track`(`learning_paths`) · `chk_contents_track`(`contents`) · `chk_target_track`(`user_profiles`, `IS NULL OR` 를 유지해야 한다).

```sql
-- 트랙 3종 확장(PYTHON_BACKEND · NODE_TYPESCRIPT · DATA_AI)을 위해 track CHECK 를 넓힌다.
--
-- CHECK 는 값을 "허용"할 뿐이고 이용자에게 보이는 목록은 프론트 track_catalog.dart 가 정한다.
-- 그래서 세 값을 한 번에 열되, 실제 문항·콘텐츠는 트랙별로 따로 적재한다.
-- track 컬럼은 VARCHAR(20) 이라 세 값 모두 길이가 맞는다(14 · 15 · 7).

ALTER TABLE question_bank DROP CONSTRAINT chk_qb_track;
ALTER TABLE question_bank ADD CONSTRAINT chk_qb_track CHECK (
  track IN ('BACKEND_SPRING','FRONTEND_REACT','MOBILE_FLUTTER','DEVOPS','FULLSTACK',
            'PYTHON_BACKEND','NODE_TYPESCRIPT','DATA_AI'));

ALTER TABLE assessments DROP CONSTRAINT chk_assessments_track;
ALTER TABLE assessments ADD CONSTRAINT chk_assessments_track CHECK (
  track IN ('BACKEND_SPRING','FRONTEND_REACT','MOBILE_FLUTTER','DEVOPS','FULLSTACK',
            'PYTHON_BACKEND','NODE_TYPESCRIPT','DATA_AI'));

ALTER TABLE learning_paths DROP CONSTRAINT chk_lp_track;
ALTER TABLE learning_paths ADD CONSTRAINT chk_lp_track CHECK (
  track IN ('BACKEND_SPRING','FRONTEND_REACT','MOBILE_FLUTTER','DEVOPS','FULLSTACK',
            'PYTHON_BACKEND','NODE_TYPESCRIPT','DATA_AI'));

ALTER TABLE contents DROP CONSTRAINT chk_contents_track;
ALTER TABLE contents ADD CONSTRAINT chk_contents_track CHECK (
  track IN ('BACKEND_SPRING','FRONTEND_REACT','MOBILE_FLUTTER','DEVOPS','FULLSTACK',
            'PYTHON_BACKEND','NODE_TYPESCRIPT','DATA_AI'));

ALTER TABLE user_profiles DROP CONSTRAINT chk_target_track;
ALTER TABLE user_profiles ADD CONSTRAINT chk_target_track CHECK (
  target_track IS NULL OR target_track IN (
    'BACKEND_SPRING','FRONTEND_REACT','MOBILE_FLUTTER','DEVOPS','FULLSTACK',
    'PYTHON_BACKEND','NODE_TYPESCRIPT','DATA_AI'));
```

- [ ] **Step 3: 로컬 DB 에 적용해 확인한다**

learning-svc 테스트를 돌리면 Flyway 가 로컬 DB 에 적용한다.

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*LearningPathArchiveOnSwitchTest"`

**주의:** learning-svc 는 shared 를 **발행된 아티팩트**로 참조한다. 마이그레이션이 로컬에 반영되지 않으면 shared 를 로컬 발행하거나, 확인을 PR 머지 뒤로 미룬다. 확인 불가면 멈추고 `NEEDS_CONTEXT` 로 보고한다.

직접 확인(psql 이 있으면):
```bash
docker exec devpath-local-postgres-1 psql -U devpath -d devpath -c "\d+ question_bank" | grep chk_qb_track
```
Expected: 8값이 보인다.

- [ ] **Step 4: 커밋하고 PR 을 올린다**

```bash
git -C D:/workspace/dpa/devpath-shared add src/main/resources/db/migration/V202608141001__track_expansion_check.sql
git -C D:/workspace/dpa/devpath-shared commit -m "feat(schema): 트랙 CHECK 를 8값으로 넓힌다

PYTHON_BACKEND·NODE_TYPESCRIPT·DATA_AI 를 한 번에 연다.
CHECK 는 값을 허용할 뿐이고 이용자 노출은 프론트 카탈로그가
정하므로, 마이그레이션을 세 번 치는 것보다 안전하다."
git -C D:/workspace/dpa/devpath-shared push -u origin feat/track-expansion-check
gh pr create --repo DevPathAi/devpath-shared --base develop --title "feat(schema): 트랙 CHECK 8값 확장" --body "트랙 3종 확장의 스키마 몫. 설계: devpath-learning-svc/docs/superpowers/specs/2026-08-14-track-expansion-python-backend-design.md"
```

---

### Task 8: PYTHON_BACKEND 문항 100개 생성·검수·승인

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\generated\approved\questions.jsonl` (100줄 추가)
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\question\QuestionQuota.java:8-13`
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\schemas\question.schema.json:19`

**Interfaces:**
- Consumes: Task 5 의 `generateQuestionsLocal -Ptrack=` · Task 6 의 `reviewQuestionsLocal -Ptrack=` · Task 2·3 의 게이트
- Produces: 승인 JSONL 에 `PYTHON_BACKEND` 100문항(MCQ 70 · CODE_READING 30). Task 10 의 시드가 이것에서 나온다.

**순서가 중요하다:** Quota 에 트랙을 넣는 것은 **문항 100개가 승인 JSONL 에 들어간 뒤**다. 먼저 넣으면 검증기가 「100문항이어야 하는데 0개」로 실패한다.

- [ ] **Step 1: Ollama 로 초안을 생성한다**

모델은 `qwen2.5:14b` 를 쓴다 — 7b 는 루프와 오답키 사례가 있었다.

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc generateQuestionsLocal -Pollama.model=qwen2.5:14b -Ptrack=PYTHON_BACKEND`
Expected: `tools/content-gen/generated/raw/questions.draft.jsonl` 에 초안이 쓰인다. 소요 시간을 기록한다(다음 두 트랙의 계획 근거가 된다).

한 번에 100개가 안 나오면 **타깃 소배치 하버스트**로 모자란 유형만 다시 부른다(기존 5트랙도 그렇게 만들었다).

- [ ] **Step 2: 초안을 승인 JSONL 에 합친다**

초안을 검토해 형식이 맞는 줄만 `tools/content-gen/generated/approved/questions.jsonl` **끝에 덧붙인다.** 기존 500줄은 건드리지 않는다.

배분 목표: MCQ 70 · CODE_READING 30 / Bloom REMEMBER 10 · UNDERSTAND 25 · APPLY 30 · ANALYZE 25 · EVALUATE 10 / 난이도 대역 0.1-0.2:10 · 0.3-0.4:25 · 0.5-0.6:30 · 0.7-0.8:25 · 0.9:10.

- [ ] **Step 3: Quota 와 스키마에 트랙을 넣는다**

`QuestionQuota.TRACKS` 에 `"PYTHON_BACKEND"` 를 마지막에 추가한다.

```java
  public static final List<String> TRACKS = List.of(
      "BACKEND_SPRING",
      "FRONTEND_REACT",
      "MOBILE_FLUTTER",
      "DEVOPS",
      "FULLSTACK",
      "PYTHON_BACKEND");
```

`question.schema.json:19` 의 enum 에도 추가한다.

```json
    "track": {
      "enum": ["BACKEND_SPRING", "FRONTEND_REACT", "MOBILE_FLUTTER", "DEVOPS", "FULLSTACK", "PYTHON_BACKEND"]
    },
```

- [ ] **Step 4: 검증기를 돌린다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc validateQuestions`
Expected: PASS. 실패하면 게이트가 알려주는 대로 고친다 — 특히 `duplicate option set`(같은 벌을 재사용) · `content must contain Korean`(CODE_READING 에 코드만 넣은 경우) · `answer key bias`.

- [ ] **Step 5: Claude 검수를 돌리고 반영한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc reviewQuestionsLocal -Ptrack=PYTHON_BACKEND`

리포트(`generated/review/PYTHON_BACKEND-review.json`)를 읽고 **사람이 판단해** 승인 JSONL 을 고친다. 리포트는 커밋한다 — 무엇을 보고 무엇을 넘겼는지가 기록으로 남아야 한다.

반영 후 Step 4 를 다시 돌려 green 을 확인한다.

- [ ] **Step 6: 단위 테스트를 돌린다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test --tests "*QuestionValidatorTest"`
Expected: PASS — 픽스처가 `QuestionQuota.TRACKS` 를 순회하므로 트랙이 6개가 되어도 자동으로 600문항을 만든다.

- [ ] **Step 7: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add tools/content-gen/generated/approved/questions.jsonl tools/content-gen/generated/review src/contentGen/java/ai/devpath/learning/contentgen/question/QuestionQuota.java tools/content-gen/schemas/question.schema.json
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(content): PYTHON_BACKEND 진단 문항 100개를 승인한다

Ollama qwen2.5:14b 초안 → 자동 게이트 → Claude 검수 → 사람 승인.
검수 리포트를 함께 커밋한다 — 무엇을 보고 무엇을 넘겼는지가
남아야 다음 두 트랙에서 같은 판단을 반복할 수 있다."
```

---

### Task 9: PYTHON_BACKEND 학습 콘텐츠 30개 + 임베딩

**Files:**
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\generated\approved\contents.jsonl` (30줄 추가)
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\generated\approved\content_embeddings.jsonl`
- Modify: `D:\workspace\dpa\devpath-learning-svc\src\contentGen\java\ai\devpath\learning\contentgen\content\ContentQuota.java:10-15`
- Modify: `D:\workspace\dpa\devpath-learning-svc\tools\content-gen\schemas\content.schema.json`

**Interfaces:**
- Consumes: `generateContentsLocal` · `validateContents` · `embedContentsLocal`(nomic-embed-text)
- Produces: 승인 콘텐츠 30(INTRO 8 · INTERMEDIATE 14 · ADVANCED 8, 코드블록 10개 이상)과 그 임베딩. Task 11 의 재측정이 이 커버리지를 본다.

**임베딩이 비면 안 되는 이유:** `LearningPathGenerationService:48` 이 `matcher.match(diagnosis.track(), …)` 로 트랙별 유사검색을 한다. 임베딩이 없으면 `contentId = null` 로 **예외 없이 조용히 degrade** 해, 이 트랙을 고른 이용자의 학습 경로에 콘텐츠가 하나도 안 붙는다.

- [ ] **Step 1: 콘텐츠 생성 커맨드에 트랙 인자를 넣는다**

실측 확인: `GenerateContentsCommand:27` 이 `ContentQuota.TRACKS` 전체를 순회하고, `main` 은 모델 인자 하나만 받는다. 문항 쪽과 같은 방식으로 좁힌다.

`GenerateContentsCommand.main` 의 앞부분을 아래로 바꾼다(`generate(...)` 는 그대로 둔다).

```java
  public static void main(String[] args) throws Exception {
    var model = args.length > 0 && !args[0].isBlank() ? args[0] : "qwen2.5:7b";
    var only = args.length > 1 && !args[1].isBlank() ? args[1] : null;
    var tracks = only == null ? ContentQuota.TRACKS : List.of(only);
    var baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    var systemPrompt = Files.readString(Path.of("tools/content-gen/prompts/content-system.md"));
    var output = Path.of("tools/content-gen/generated/raw/contents.draft.jsonl");
    Files.createDirectories(output.getParent());

    var draft = new StringBuilder();
    for (String track : tracks) {
      draft.append(generate(baseUrl, model, track, systemPrompt));
      if (!draft.toString().endsWith("\n")) {
        draft.append("\n");
      }
    }
    Files.writeString(output, draft.toString());
    System.out.println("Wrote draft contents for " + tracks + " to " + output);
  }
```

`java.util.List` 는 이미 import 돼 있다(`GenerateContentsCommand:12`).

`build.gradle.kts` 의 `generateContentsLocal` 블록에서 `args(...)` 를 바꾼다.

```kotlin
	args(
		(project.findProperty("ollama.model") as String? ?: "qwen2.5:7b"),
		(project.findProperty("track") as String? ?: "")
	)
```

컴파일 확인: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc compileContentGenJava` → BUILD SUCCESSFUL.

- [ ] **Step 1b: 콘텐츠 초안을 생성한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc generateContentsLocal -Pollama.model=qwen2.5:14b -Ptrack=PYTHON_BACKEND`

- [ ] **Step 2: 승인 JSONL 에 합치고 Quota·스키마에 트랙을 넣는다**

`contents.jsonl` 끝에 30줄을 덧붙인다. `ContentQuota.TRACKS` 와 `content.schema.json` 의 track enum 에 `PYTHON_BACKEND` 를 추가한다.

slug 는 전체에서 유일해야 한다(`ContentValidator:42` 가 중복을 잡는다). `python-backend-` 접두를 붙여 충돌을 피한다.

- [ ] **Step 3: 콘텐츠 검증**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc validateContents`
Expected: PASS. 실패하면 레벨 분포(8/14/8)·코드블록 10개·slug 중복·닫히지 않은 코드펜스를 본다.

- [ ] **Step 4: 임베딩 생성**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc embedContentsLocal -Pollama.embedModel=nomic-embed-text`
Expected: `content_embeddings.jsonl` 이 갱신된다.

- [ ] **Step 5: 임베딩이 신규 콘텐츠를 모두 덮는지 확인**

```bash
cd /d/workspace/dpa/devpath-learning-svc/tools/content-gen/generated/approved
printf '%s\n' 'select(.track=="PYTHON_BACKEND")|.slug' > /tmp/c.jq
echo "콘텐츠: $(jq -r -f /tmp/c.jq contents.jsonl | sort -u | wc -l)"
echo "임베딩이 덮는 slug: $(jq -r '.slug' content_embeddings.jsonl | sort -u | grep -c '^python-backend-')"
```
Expected: 두 숫자가 같다(30). 다르면 임베딩을 다시 돌린다.

**주의:** 임베딩 JSONL 의 필드명이 `slug` 가 아닐 수 있다 — `EmbeddingRecord` 를 읽고 실제 필드로 맞춘다. 추측하지 않는다.

- [ ] **Step 6: 전체 테스트**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test`
Expected: PASS.

- [ ] **Step 7: 커밋**

```bash
git -C D:/workspace/dpa/devpath-learning-svc add tools/content-gen src/contentGen/java/ai/devpath/learning/contentgen/content/ContentQuota.java
git -C D:/workspace/dpa/devpath-learning-svc commit -m "feat(content): PYTHON_BACKEND 학습 콘텐츠 30개와 임베딩

임베딩이 비면 이 트랙을 고른 이용자의 학습 경로에 콘텐츠가
예외 없이 조용히 안 붙는다(LearningPathGenerationService:48)."
```

---

### Task 10: 시드 SQL · 운영 마이그레이션 · 프론트 카탈로그

**Files:**
- Modify: learning-svc 시드 산출물(`makeQuestionSeedSql`·`makeContentSeedSql` 재생성)
- Create: `D:\workspace\dpa\devpath-shared\src\main\resources\db\migration\V202608141002__seed_python_backend.sql`
- Create: `D:\workspace\dpa\devpath-shared\src\main\resources\db\migration\V202608141002__seed_python_backend.sql.conf`
- Modify: `D:\workspace\dpa\devpath-frontend\apps\web\lib\src\features\common\application\track_catalog.dart`
- Modify: `D:\workspace\dpa\devpath-frontend\apps\web\test\features\common\track_catalog_test.dart`

**Interfaces:**
- Consumes: Task 8·9 의 승인 JSONL · Task 7 의 CHECK 확장
- Produces: 운영 DB 에 `PYTHON_BACKEND` 문항 100·콘텐츠 30 · 이용자에게 보이는 6트랙

- [ ] **Step 1: 시드 SQL 을 재생성한다**

```
& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc makeQuestionSeedSql
& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc makeContentSeedSql
```

- [ ] **Step 2: 신규 트랙 행만 발췌해 운영 마이그레이션을 만든다**

**전량을 넣으면 기존 5트랙이 중복 삽입된다.** `PYTHON_BACKEND` 행만 뽑는다.

```bash
cd /d/workspace/dpa/devpath-learning-svc/src/main/resources/db/seed
grep "^('PYTHON_BACKEND'" question_bank_md2_seed.sql | wc -l   # 100 이어야 한다
grep "^('PYTHON_BACKEND'" content_md2_seed.sql | wc -l          # 30 이어야 한다
```

발췌한 행 앞에 `INSERT INTO question_bank (...) VALUES` 헤더를 붙이고, 마지막 행의 콤마를 세미콜론으로 바꾼다. 컬럼 목록은 기존 시드 파일 헤더에서 그대로 복사한다.

`.sql.conf` 를 짝으로 만든다 — 한 줄이다.

```
placeholderReplacement=false
```

없으면 SQL 안의 `${...}` 를 Flyway 가 치환하려다 깨진다(2026-08-13 실측).

마이그레이션 상단 주석에 **증분 INSERT 임을 명시**한다 — 어제의 재시드는 `DELETE` 후 전량 재삽입이었고, 이것은 다르다.

- [ ] **Step 3: 로컬에서 마이그레이션이 도는지 확인한다**

Run: `& D:\workspace\dpa\devpath-learning-svc\gradlew.bat -p D:\workspace\dpa\devpath-learning-svc cleanTest test`
Expected: PASS. Flyway 가 새 마이그레이션을 적용한다.

```bash
docker exec devpath-local-postgres-1 psql -U devpath -d devpath -c "SELECT track, count(*), count(DISTINCT options) FROM question_bank GROUP BY track ORDER BY track;"
```
Expected: `PYTHON_BACKEND | 100 | 100` 에 가까운 값(중복 벌이 없으면 100). `count(DISTINCT options) = 1` 이면 어제 사고의 재현이다.

- [ ] **Step 4: 프론트 카탈로그를 6키로 늘린다**

`track_catalog.dart` 에 한 줄 추가한다.

```dart
  'FULLSTACK': '풀스택',
  'PYTHON_BACKEND': 'Python 백엔드 (Django/FastAPI)',
};
```

`track_catalog_test.dart` 의 계약도 6키로 고친다.

```dart
    expect(trackLabels.keys, [
      'BACKEND_SPRING',
      'FRONTEND_REACT',
      'MOBILE_FLUTTER',
      'DEVOPS',
      'FULLSTACK',
      'PYTHON_BACKEND',
    ]);
```

- [ ] **Step 5: 프론트 테스트**

```
Set-Location D:\workspace\dpa\devpath-frontend
dart pub global run melos run analyze
dart pub global run melos run test
dart pub global run melos run format
```
Expected: analyze SUCCESS · test SUCCESS(devpath_web 은 405건 유지 — 키만 늘었다) · format `0 changed`.

- [ ] **Step 6: 세 레포에 커밋하고 PR 을 올린다**

learning-svc 는 기존 브랜치에 커밋한다. shared 는 Task 7 브랜치에 이어 커밋한다. frontend 는 `develop` 에서 새 브랜치를 딴다.

```bash
git -C D:/workspace/dpa/devpath-frontend checkout develop
git -C D:/workspace/dpa/devpath-frontend pull --ff-only
git -C D:/workspace/dpa/devpath-frontend checkout -b feat/track-catalog-python-backend
```

각 레포에서 PR 을 올린다(`gh pr create --repo DevPathAi/<레포> --base develop ...`).

---

### Task 11: 배포와 재측정

**Interfaces:**
- Consumes: Task 7·10 의 PR 3건
- Produces: 운영에서 이용자가 6트랙을 고를 수 있다

- [ ] **Step 1: CI 가 green 인지 확인하고 develop 에 머지한다**

`gh pr checks <번호> --repo DevPathAi/<레포>` — **`--repo` 를 반드시 명시한다.** cwd 만 믿고 부르면 다른 레포의 옛 PR 을 보게 된다(2026-08-13 실제 사고).

머지 순서: shared → learning-svc → frontend.

- [ ] **Step 2: 릴리스 PR(develop→main)을 올려 머지한다**

세 레포 모두 `main` 릴리스가 필요하다. frontend 는 `web-deploy` 가 **main 전용**이라 릴리스 없이는 앱에 반영되지 않는다.

릴리스 전 무엇이 함께 나가는지 실측한다:
```bash
git -C D:/workspace/dpa/devpath-shared fetch -q origin && git -C D:/workspace/dpa/devpath-shared rev-list --count origin/main..origin/develop
```

- [ ] **Step 3: 운영 DB 에서 재측정한다 (게이트)**

배포 후 실측한다. 「배포 워크플로 success」는 반영의 증거가 아니다.

- `SELECT count(*) FROM question_bank WHERE track='PYTHON_BACKEND'` = **100**
- `SELECT count(DISTINCT options) FROM question_bank WHERE track='PYTHON_BACKEND'` — **1 이면 어제 사고의 재현이다**
- `SELECT count(*) FROM contents WHERE track='PYTHON_BACKEND'` = **30**
- 신규 트랙 콘텐츠의 `content_embeddings` 커버리지 = **30/30**

- [ ] **Step 4: 프론트 반영을 번들로 실측한다**

```bash
curl.exe -4 -s --compressed https://app.leva.ai.kr/main.dart.js -o /tmp/bundle.js
grep -c "PYTHON_BACKEND" /tmp/bundle.js
```

**측정법의 유효성을 먼저 세운다.** 기존부터 있던 값(`FULLSTACK`)이 잡히는지 함께 확인한다 — 잡히지 않으면 측정법이 무효이고, 그 상태의 「0건」은 「반영 안 됨」의 증거가 되지 못한다(2026-08-14 실제로 이 함정에 빠졌다).

ArgoCD sync 에 2분 안팎이 걸린다. 캐시버스터를 붙여 여러 번 재서 혼재가 없는지 본다.

- [ ] **Step 5: 레저와 메모리를 갱신한다**

무엇이 실측됐고 무엇이 추정인지 구분해 적는다. 특히 이 머신에는 **운영 k3s 컨텍스트가 없어 백엔드 파드는 직접 볼 수 없다** — gitops 커밋까지가 확인 범위다.

---

## 다음 두 트랙 (이 계획 밖)

`NODE_TYPESCRIPT` · `DATA_AI` 는 Task 5 · 8 · 9 · 10 · 11 의 반복이다. Task 1~4·6·7 은 이미 끝나 있고, CHECK 도 Task 7 에서 함께 열렸다. 트랙당 남는 일은 **지침 md 1개 · 생성 · 검수 · 시드 발췌 · 카탈로그 1키**다. Task 8 Step 1 에서 기록한 생성 소요 시간이 그 계획의 근거가 된다.
