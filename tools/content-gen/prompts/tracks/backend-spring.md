트랙: BACKEND_SPRING (백엔드 — Spring/Java·Kotlin)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.

## 핵심 개념 목록

- **Spring Boot**: 자동설정(auto-configuration), `@ConditionalOn*`, 스타터, 프로파일, `application.yml` 바인딩, 액추에이터.
- **의존성 주입·빈**: 생성자 주입 vs 필드 주입, 빈 스코프(singleton/prototype), 순환 참조, `@Primary`/`@Qualifier`.
- **웹/HTTP API**: `@RestController`, 요청 매핑, `@RequestBody`/`@Valid` 검증, 예외 처리(`@ControllerAdvice`), 상태 코드, 콘텐츠 협상.
- **JPA/영속성**: 영속성 컨텍스트, 1차 캐시, 더티 체킹, 지연/즉시 로딩, **N+1 문제와 fetch join**, `@Entity` 연관관계, 낙관적/비관적 락.
- **트랜잭션**: `@Transactional` **전파(propagation)**·격리 수준, 롤백 규칙(checked vs unchecked), self-invocation 프록시 한계, readOnly.
- **보안**: Spring Security 필터 체인, 인증 vs 인가, JWT/세션, CSRF, 비밀번호 해싱.
- **Kafka**: 프로듀서/컨슈머, **컨슈머 그룹·파티션·리밸런싱**, 오프셋 커밋(자동/수동), 순서 보장, at-least-once.
- **캐시**: `@Cacheable`/`@CacheEvict`, 캐시 추상화, TTL, 캐시 스탬피드.
- **동시성**: 스레드 안전성, `synchronized`/`Atomic*`, 낙관적 락 vs 비관적 락, 데드락.
- **PostgreSQL**: 인덱스(B-tree·부분·복합), 실행 계획, 트랜잭션 격리(MVCC), 커넥션 풀(HikariCP).

## CODE_READING 지침

짧은 Java 또는 Kotlin/Spring 스니펫(5~15줄)을 `content`에 `\n`으로 넣고, 트랜잭션 전파·N+1·프록시 self-invocation·락·`@Transactional(readOnly)` 오작동 등 **위 개념의 미묘한 동작·버그**를 읽어내게 한다.
