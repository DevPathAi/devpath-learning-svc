트랙: NODE_TYPESCRIPT (Node.js 서버 — TypeScript)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.
**브라우저 UI·React 컴포넌트·상태관리는 이 트랙이 아니다**(FRONTEND_REACT 트랙 소관). JSX·훅·렌더링을 묻지 않는다.
**컨테이너·쿠버네티스·CI 파이프라인 자체도 이 트랙이 아니다**(DEVOPS 트랙 소관). 묻더라도 Node 프로세스 관점으로만 묻는다.
언어는 TypeScript 와 Node 런타임이다. Java·Kotlin·Python 코드를 쓰지 않는다.

## 핵심 개념 목록

- **TypeScript 타입 시스템**: 구조적 타이핑, 유니온·교차, 리터럴 타입과 좁히기(narrowing), 타입 가드와 `is`, `unknown` 대 `any`, `never`, 제네릭과 제약, 유틸리티 타입(`Partial`·`Pick`·`Record`·`ReturnType`), `readonly`, 선언 병합.
- **타입의 한계**: 타입 단언(`as`)이 런타임을 보장하지 않는 것, 외부 입력(JSON·환경변수)의 검증 필요성, `strict`·`strictNullChecks`가 잡는 것과 못 잡는 것, 구조적 타이핑이 의도치 않게 허용하는 대입.
- **Node 이벤트 루프**: timers·poll·check 단계, 매크로태스크 대 마이크로태스크, `process.nextTick` 과 `Promise.then` 의 우선순위, `setTimeout(0)` 대 `setImmediate`, **동기 CPU 작업이 루프를 막는 문제**.
- **비동기 제어**: `Promise.all`·`allSettled`·`race`·`any` 의 차이, 순차 `await` 로 생기는 불필요한 지연, 오류 전파와 `try/catch` 범위, unhandled rejection, `AbortController` 로 취소, 동시성 제한.
- **스트림과 버퍼**: 읽기·쓰기 스트림, `pipeline`, **백프레셔**, `Buffer` 와 인코딩, 대용량 파일을 메모리에 올리지 않기.
- **프로세스 모델**: 단일 스레드와 `worker_threads`, `cluster`, 자식 프로세스, 그레이스풀 셧다운(`SIGTERM`), 처리되지 않은 예외로 인한 프로세스 종료.
- **모듈 시스템**: CommonJS 대 ESM, `require` 와 `import` 의 해석 차이, `package.json` 의 `type`·`exports`·`main`·`types`, 순환 의존이 만드는 부분 초기화, 최상위 `await`.
- **웹 프레임워크**: Express·Fastify·NestJS — 미들웨어 실행 순서, 오류 처리 미들웨어가 인자 4개여야 하는 이유, 라우팅과 경로 우선순위, 요청 검증(zod 등 스키마 검증), NestJS 의존성 주입과 모듈 경계.
- **영속성**: Prisma·TypeORM 의 쿼리와 **N+1**, 관계 로딩 전략, 트랜잭션 경계, 커넥션 풀 고갈, 마이그레이션 관리.
- **HTTP·API**: 상태 코드, 인증(세션·JWT), CORS, 페이지네이션, 멱등성, 요청 타임아웃, 스트리밍 응답.
- **테스트·품질**: Jest·Vitest 의 목과 스파이, 비동기 테스트의 흔한 실수, 타입 수준 테스트, 커버리지가 보증하지 못하는 것.
- **도구·패키징**: `tsconfig` 의 `target`·`module`·`moduleResolution`, 컴파일(tsc)과 번들(esbuild) 의 차이, 소스맵, 잠금 파일과 재현 가능한 설치, 개발 의존성과 런타임 의존성의 구분.

## CODE_READING 지침

짧은 TypeScript 스니펫(5~15줄)을 `content` 에 `\n` 으로 넣고, **위 개념의 미묘한 동작·버그**를 읽어내게 한다.

겨냥할 만한 것: `process.nextTick`·`setTimeout`·`Promise` 가 섞인 출력 순서 · 순차 `await` 로 병렬 기회를 잃는 코드 · `Promise.all` 안의 거부가 나머지를 취소하지 않는 것 · 타입 가드가 좁히지 못하는 분기 · `as` 로 가린 런타임 불일치 · 화살표 함수와 `this` · 순환 import 로 `undefined` 를 읽는 초기화 · 스트림을 `await` 없이 종료해 데이터가 잘리는 것 · `catch` 밖에서 던져 unhandled rejection 이 되는 비동기 콜백.

`content` 는 코드만 두지 말고 **한국어 질문 문장을 함께** 넣는다.
