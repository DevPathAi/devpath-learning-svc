# MD2 Question Content Generation

This directory contains offline-only tooling for approved diagnostic questions.
CI validates committed JSONL and SQL artifacts; it must not call Ollama.

Manual local flow:

```powershell
.\gradlew.bat generateQuestionsLocal -Pollama.model=qwen2.5:7b
.\gradlew.bat validateQuestions
.\gradlew.bat makeQuestionSeedSql
```

Artifacts:

- `generated/approved/questions.jsonl` is the reviewed source of truth.
- `generated/seeds/question_bank_seed.sql` is deterministic SQL generated from the approved JSONL.
- `src/main/resources/db/seed/question_bank_md2_seed.sql` is the classpath seed used by the dev profile seeder.
