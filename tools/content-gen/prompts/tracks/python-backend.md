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
- **Django REST Framework**: Serializer 검증·`ModelSerializer`, ViewSet·라우터, 권한 클래스, 페이지네이션, N+1 을 부르는 중첩 Serializer.
- **작업 큐**: Celery 워커·브로커(Redis/RabbitMQ), 태스크 재시도·멱등성, 결과 백엔드, 주기 작업(beat), 웹 요청과 분리해야 하는 작업.
- **캐시**: Redis 캐시 패턴(cache-aside), TTL, 키 설계, 캐시 무효화, 캐시 스탬피드.

## CODE_READING 지침

짧은 파이썬 스니펫(5~15줄)을 `content` 에 `\n` 으로 넣고, 가변 기본 인자·얕은 복사·이벤트 루프를 막는 블로킹 호출·쿼리셋 N+1·`atomic` 밖 예외 처리·데코레이터 적용 순서 등 **위 개념의 미묘한 동작·버그**를 읽어내게 한다. `content` 는 코드만 두지 말고 **한국어 질문 문장을 함께** 넣는다.
