# MD2 Offline Content Generation

This directory contains offline-only tooling for approved diagnostic questions and learning contents.
CI validates committed JSONL and SQL artifacts; it must not call Ollama.

Manual local question flow:

```powershell
.\gradlew.bat generateQuestionsLocal -Pollama.model=qwen2.5:7b
.\gradlew.bat reviewQuestionsLocal -Ptrack=<TRACK>          # 사실 검증 루프 (Claude, 반박 우선)
# 리뷰 지적을 questions.jsonl 에 반영한 뒤:
.\gradlew.bat stampQuestionVerifications -Ptrack=<TRACK> -Previewer=<이름/근거>
.\gradlew.bat validateQuestions
.\gradlew.bat makeQuestionSeedSql
```

Fact-verification gate (2026-08-22 문항 800 검수 후속): 모든 승인 문항은
`question_verifications.jsonl` 에 fingerprint 가 일치하는 PASS 판정을 가져야 한다.
채점 필드(content·options·정답)가 바뀌면 fingerprint 가 어긋나므로, 리뷰 루프를
건너뛴 추가·수정은 `validateQuestions` 와 CI(`ApprovedQuestionsGateTest`)가 막는다 —
구조·분포 게이트만 거친 트랙은 결함율 21~47%, 검증 루프를 거친 트랙은 3%였다.
CI 는 또한 커밋된 시드 SQL 4곳이 승인 JSONL 재생성본과 일치함을 강제한다(우회 편집 금지).

Manual local content and embedding flow:

```powershell
.\gradlew.bat generateContentsLocal -Pollama.model=qwen2.5:7b
.\gradlew.bat validateContents
.\gradlew.bat embedContentsLocal -Pollama.embedModel=nomic-embed-text
.\gradlew.bat makeContentSeedSql
```

Artifacts:

- `generated/approved/questions.jsonl` is the reviewed source of truth.
- `generated/seeds/question_bank_seed.sql` is deterministic SQL generated from the approved JSONL.
- `src/main/resources/db/seed/question_bank_md2_seed.sql` is the classpath seed used by the dev profile seeder.
- `generated/approved/contents.jsonl` is the reviewed learning content source of truth.
- `generated/approved/content_embeddings.jsonl` is the reviewed embedding fixture source of truth.
- `generated/seeds/content_seed.sql` is deterministic SQL generated from the approved content and embedding JSONL.
- `src/main/resources/db/seed/content_md2_seed.sql` is the classpath content seed for local/dev loading.
