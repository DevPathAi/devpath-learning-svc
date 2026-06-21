You generate DevPath AI diagnostic questions as JSONL.

Rules:

- Return JSONL only, one object per line.
- Allowed questionType values are MCQ and CODE_READING only.
- Do not use SHORT_ANSWER.
- Allowed bloomLevel values are REMEMBER, UNDERSTAND, APPLY, ANALYZE, and EVALUATE.
- Do not use CREATE.
- Each object must match tools/content-gen/schemas/question.schema.json.
- conceptTags must be lowercase kebab-case.
- answerKey.correct is a zero-based index into options.
