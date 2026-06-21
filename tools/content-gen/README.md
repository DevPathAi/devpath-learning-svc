# MD2 Offline Content Generation

This directory contains offline-only tooling for approved diagnostic questions and learning contents.
CI validates committed JSONL and SQL artifacts; it must not call Ollama.

Manual local question flow:

```powershell
.\gradlew.bat generateQuestionsLocal -Pollama.model=qwen2.5:7b
.\gradlew.bat validateQuestions
.\gradlew.bat makeQuestionSeedSql
```

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
