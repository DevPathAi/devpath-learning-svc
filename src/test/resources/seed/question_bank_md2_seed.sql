INSERT INTO question_bank (track, question_type, content, options, answer_key, bloom_level, difficulty, concept_tags) VALUES
('BACKEND_SPRING','MCQ','Spring의 @Transactional 어노테이션에서 전파(propagation) 타입으로 REQUIRED가 설정된 경우, 트랜잭션이 이미 시작되어 있으면 어떻게 동작하는가?','["기존 트랜잭션과 병행 실행","새로운 트랜잭션을 생성","현재 메서드만 트랜잭션으로 감싸지 않음","기존 트랜잭션에 참여"]','{"correct":3}','APPLY',0.2,'["spring-transaction-propagation"]'),
('BACKEND_SPRING','MCQ','스프링 트랜잭션에서 @Transactional(readOnly = true) 어노테이션이 적용된 메소드에서는 어떤 작업이 금지되는가?','["데이터베이스 쿼리를 실행하는 것","데이터베이스에 데이터를 삽입하거나 수정하는 것","트랜잭션을 시작하는 것","@Transactional을 사용하지 않은 다른 메서드 호출"]','{"correct":1}','EVALUATE',0.2,'["spring-transaction-read-only"]'),
('BACKEND_SPRING','MCQ','JPA에서 N+1 문제와 관련하여, fetch join을 사용할 때 어떤 이점이 있는가?','["데이터베이스 쿼리의 성능을 크게 향상시킬 수 있다.","모든 연관된 엔티티를 한 번에 로드하지 않고 지연로딩으로 처리한다.","데이터베이스에서 모든 데이터를 가져와 메모리에 저장한다.","N+1 문제 해결과 동시에 캐싱 기능을 제공한다."]','{"correct":0}','EVALUATE',0.2,'["jpa-fetch-join"]'),
('BACKEND_SPRING','MCQ','Kafka에서 컨슈머 그룹의 역할은 무엇인가?','["동일한 토픽에 대한 모든 메시지를 독점적으로 소비한다.","같은 그룹 내에서 각 컨슈머는 다른 파티션을 소비한다.","컨슈머 그룹은 여러 토픽의 메시지를 동시에 소비할 수 있다.","두 가지 모두 올바르지 않다."]','{"correct":1}','UNDERSTAND',0.2,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 @ConditionalOnProperty 어노테이션을 사용하면 무엇을 할 수 있나?','["프로필 기반으로 빈 등록 여부를 결정한다.","프로퍼티가 설정된 경우에만 스프링 빈을 등록한다.","두 가지 모두 가능하다.","빈의 스코프를 조절할 수 있다."]','{"correct":1}','UNDERSTAND',0.2,'["spring-boot"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 프로필(profile) 기반 설정을 활성화하려면 application.yml 파일에서 어떤 키워드를 사용해야 하는가?','["spring.profiles.include","spring.profiles.active","spring.profiles.default","spring.profiles.actives"]','{"correct":1}','REMEMBER',0.2,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증을 성공적으로 마친 사용자에게 권한 정보를 제공하는 역할은 무엇인가?','["AuthenticationManager","UserDetailsService","AccessDecisionManager","AuthorizationService"]','{"correct":1}','APPLY',0.2,'["spring-security-authorization"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 애플리케이션 설정을 위한 기본 파일로 사용되는 것은 무엇인가?','["application.properties","config.yml","bootstrap.yaml","environment.conf"]','{"correct":0}','REMEMBER',0.2,'["spring-boot-configuration"]'),
('BACKEND_SPRING','MCQ','Kafka 컨슈머 그룹이 레코드 처리 중 오류가 발생하면, Kafka는 어떻게 동작하나?','["오류 발생한 파티션만 다시 처리한다.","전체 토픽에서 재시작한다.","해당 그룹의 모든 파티션이 다시 처리된다.","위치를 기억하고 이후에 다시 처리한다."]','{"correct":0}','APPLY',0.2,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 application.yml 파일을 사용하면 무엇을 할 수 있나?','["애플리케이션의 환경 변수를 설정한다.","스프링 빈들의 속성을 바인딩한다.","두 가지 모두 가능하다.","환경 프로파일에 따른 세부 설정을 적용할 수 있다."]','{"correct":2}','UNDERSTAND',0.4,'["spring-boot-configuration"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 application.yml 파일을 통해 설정할 수 있는 환경 변수는 어떤 것이 있나?','["application.properties","bootstrap.yml","config/application.yaml","logback.xml"]','{"correct":2}','APPLY',0.4,'["spring-boot-configuration"]'),
('BACKEND_SPRING','MCQ','Kafka의 컨슈머 그룹에서 메시지를 처리하는 방법은 여러 가지가 있습니다. 다음 중 올바른 방법을 선택하세요.','["메시지를 처리한 후에만 오프셋을 커밋합니다.","메시지 처리를 완료하기 전에도 오프셋을 자동으로 커밋합니다.","Kafka 컨슈머는 메시지 처리 과정에서 오프셋을 무시하고 진행됩니다.","컨슈머 그룹은 하나의 파티션에 대해 여러 개의 컨슈머가 동시에 작업할 수 있습니다."]','{"correct":0}','ANALYZE',0.4,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인가 정보를 검사하는 필터는 무엇인가요?','["AccessDecisionFilter","AuthorizationFilter","AccessDeniedHandler","SecurityMetadataSource"]','{"correct":1}','REMEMBER',0.4,'["spring-security-authorization-filters"]'),
('BACKEND_SPRING','MCQ','Kafka 컨슈머 그룹에서 파티션 리밸런싱이 발생할 때, 어떤 상황에서는 데이터 순서 보장이 어려워진다. 이 문제를 해결하기 위한 방법은 무엇인가?','["자동 오프셋 커밋을 사용한다.","하드 코딩된 그룹 ID를 사용한다.","사용자 정의 파티션 할당기를 구현한다.","커넥션 풀링을 적용한다."]','{"correct":2}','EVALUATE',0.4,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 CSRF 보호를 설정할 때 주의해야 할 점은 무엇인가요?','["CSRF 보호는 모든 HTTP 요청에 대해 활성화되어야 합니다.","CSRF 토큰이 필요한 경우만 CSRF 보호를 적용하면 됩니다.","CSRF 보호를 비활성화하는 것이 더 안전합니다.","Spring Security에서 CSRF 보호를 설정할 수 없습니다."]','{"correct":1}','ANALYZE',0.4,'["spring-security-csrf"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 프로파일 기반 설정을 사용할 때, 활성화된 프로필을 결정하는 방식은 무엇인가?','["application.yml 파일에 직접적으로 활성화할 프로필 이름을 지정한다.","@ActiveProfiles 어노테이션으로 실행 시점에 프로필을 선택한다.","@Profile 어노테이션이 적용된 설정 클래스를 사용하여 프로파일별로 구분한다.","프로젝트 빌드 시 설정 파일에서 프로필을 지정해야 한다."]','{"correct":2}','EVALUATE',0.4,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 프로파일을 사용하여 환경별 설정을 구분하는 방법은 무엇인가?','["application.properties 파일 내부에 특정 환경을 위한 설정을 분할하고, 활성화하려는 프로필 이름으로 해당 파일을 명시한다.","스프링 부트 애플리케이션 프로퍼티를 사용하여 활성화하려는 프로파일을 명시하며, 이를 통해 특정 환경에 대한 설정만 로드된다.","application.yml 파일 내부에서 @Profile 어노테이션으로 분할된 설정을 정의하고, 필요한 프로필 이름을 지정한다.","환경 변수를 사용하여 활성화하려는 프로파일을 명시하며, 이 환경 변수에 해당하는 설정만 로드된다."]','{"correct":1}','UNDERSTAND',0.4,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring의 @Transactional 어노테이션에서 readOnly 속성을 사용하는 경우 주의해야 할 점은 무엇인가요?','["readOnly는 트랜잭션이 읽기 전용으로 설정되면 변경사항을 롤백합니다.","readOnly는 데이터베이스의 쓰기 작업을 비활성화하지만, 메모리에서의 변경은 허용됩니다.","readOnly 속성을 사용하면 트랜잭션 관리가 완전히 비활성화됩니다.","Spring에서는 readOnly 속성을 지원하지 않습니다."]','{"correct":1}','ANALYZE',0.4,'["spring-transaction-readonly"]'),
('BACKEND_SPRING','MCQ','@Cacheable 어노테이션의 기본 캐시 이름은 무엇인가요?','["default","main","primary","none"]','{"correct":3}','REMEMBER',0.4,'["spring-cache-annotation"]'),
('BACKEND_SPRING','MCQ','Kafka에서 메시지를 토픽에 전송하기 위해 사용되는 클래스는 무엇인가?','["ConsumerRecord","ProducerRecord","TopicPartition","MessageSet"]','{"correct":1}','REMEMBER',0.4,'["kafka-producer"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 @Profile 어노테이션과 application.yml 파일을 사용하여 환경 설정을 변경할 때 주의해야 할 사항은 무엇인가요?','["환경 변수를 통해 프로파일을 동적으로 변경하는 것이 가능합니다.","application.yml 파일 내에서 모든 프로필 설정을 한 번에 적용할 수 있습니다.","프로필 기반 설정이 application.yml 파일보다 우선 순위가 높습니다.","Spring Boot는 프로필 기반 설정을 지원하지 않습니다."]','{"correct":0}','ANALYZE',0.4,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증(authentication)과 인가(authorization)를 구분하는 주요 차이는 무엇인가?','["인증은 사용자 계정의 정합성을 확인하고, 인가는 권한을 부여합니다.","인증은 암호화를 처리하고, 인가는 세션 관리를 수행합니다.","인증은 요청 URI에 대한 접근 제어를 결정하고, 인가는 사용자의 정보를 검증합니다.","인증과 인가는 서로 동일한 개념입니다."]','{"correct":0}','REMEMBER',0.4,'["spring-security-authentication-authorization"]'),
('BACKEND_SPRING','MCQ','Kafka에서 컨슈머 그룹과 파티션의 관계에 대해 설명해 주세요.','["컨슈머 그룹은 한 개의 파티션을 공유할 수 있습니다.","컨슈머 그룹은 여러 파티션을 분산하여 처리할 수 있습니다.","컨슈머 그룹은 하나의 메시지만 처리합니다.","Kafka에서는 컨슈머 그룹 개념이 없습니다."]','{"correct":1}','ANALYZE',0.4,'["kafka-consumer-group-partition"]'),
('BACKEND_SPRING','MCQ','Spring Data JPA에서 N+1 문제를 해결하기 위해 사용할 수 있는 방법은 여러 가지가 있습니다. 다음 중 올바른 방법을 선택하세요.','["지연 로딩을 활성화하여 모든 관련 엔티티를 즉시 가져옵니다.","지연 로딩 대신 전환(join)을 통해 필요한 데이터만 한 번에 가져오도록 설정합니다.","엔티티 매니저의 flush() 메서드를 사용하여 쿼리 실행 횟수를 줄입니다.","기존 엔티티 매니저를 재사용하지 않고 새로운 인스턴스를 계속 생성합니다."]','{"correct":1}','ANALYZE',0.4,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 CSRF 보호를 활성화하기 위해 필요한 설정은 무엇인가?','["@EnableWebSecurity 어노테이션을 사용한다.","@Secured 어노테이션을 사용하여 컨트롤러 메서드에 적용한다.","@Csrf 테스트 어노테이션을 사용하여 요청 매핑에 적용한다.","@EnableWebMvc 및 @EnableSpringDataWebSupport를 사용해 설정 클래스에서 활성화한다."]','{"correct":0}','EVALUATE',0.4,'["spring-security-csrf"]'),
('BACKEND_SPRING','MCQ','Spring Security를 사용하여 인증 정보를 검사하는 필터는 무엇인가요?','["AuthenticationFilter","AuthenticatingFilter","UsernamePasswordAuthenticationFilter","AuthenticationProcessingFilter"]','{"correct":2}','REMEMBER',0.4,'["spring-security-authentication-filters"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증이 성공했음에도 불구하고 접근 권한이 없는 경로에 접근하려고 할 때 발생하는 예외는 무엇인가?','["AccessDeniedException","AuthenticationCredentialsNotFoundException","BadCredentialsException","InsufficientAuthenticationException"]','{"correct":0}','APPLY',0.4,'["spring-security-authentication-authorization"]'),
('BACKEND_SPRING','MCQ','Spring에서 `@Transactional` 어노테이션의 레벨(read-only, propagation 등) 설정은 중요합니다. 다음 중 `@Transactional(readOnly = true)`가 올바르게 사용되는 상황을 선택하세요.','["트랜잭션이 변경사항을 허용할 때","데이터베이스에 대한 쿼리만 실행하고 데이터를 수정하지 않을 때","트랜잭션에서 예외 발생 시 롤백되지 않는 경우","사용자 요청에 대한 모든 메서드 호출에서 사용될 때."]','{"correct":1}','ANALYZE',0.4,'["spring-transaction-propagation"]'),
('BACKEND_SPRING','MCQ','Spring Data JPA에서 엔티티(Entity) 간의 관계를 표현하기 위해 사용하는 어노테이션은 무엇인가?','["@Entity","@Table","@JoinColumn","@OneToOne","@OneToMany"]','{"correct":4}','REMEMBER',0.4,'["jpa-entity-relationship"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 @ConditionalOnProperty 어노테이션은 어떤 용도로 사용됩니까?','["프로필 기반 빈 생성을 조건부로 설정합니다.","환경 변수를 바탕으로 빈 생성을 조건부로 설정합니다.","스피링 부트의 자동 설정 클래스에서 특정 속성에 따른 빈 생성을 조건부로 설정합니다.","스피링 부트 애플리케이션 프로퍼티를 바탕으로 스프링 컨텍스트 초기화를 조건부로 설정합니다."]','{"correct":2}','REMEMBER',0.4,'["spring-boot-auto-configuration"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 프로파일을 활성화하는 방법 중 하나는?','["application.yml 파일의 spring.profiles.active 속성을 설정한다.","@Profile 어노테이션을 사용하여 빈을 정의한다.","application.properties 파일에 profiles.default를 설정한다.","spring:profiles.active 속성을 환경 변수로 설정한다."]','{"correct":0}','UNDERSTAND',0.6,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 JWT를 사용하여 인증을 처리하는 방법은 무엇인가?','["Spring Security의 @EnableWebSecurity 설정 클래스에서 HttpSecurity를 통해 JWT 필터를 등록한다.","application.yml 파일에 jwt 관련 설정만 추가하면 자동으로 인증이 처리된다.","JWT는 Spring Security와 함께 기본적으로 제공되므로 따로 설정할 필요 없다.","Spring Security의 UserDetailsService를 구현하여 JWT 토큰을 검증한다."]','{"correct":0}','EVALUATE',0.6,'["spring-security-jwt"]'),
('BACKEND_SPRING','MCQ','Spring Transaction에서 @Transactional(readOnly = true) 설정을 사용할 때 주의해야 할 점은 무엇인가?','["readOnly=true로 설정하면 트랜잭션 동작이 비활성화된다.","readOnly=true에서는 엔티티의 변경사항이 영속성 컨텍스트에 반영되지 않는다.","readOnly=true는 데이터베이스 쿼리 성능을 저하시킨다.","readOnly=true는 N+1 문제를 해결한다."]','{"correct":1}','EVALUATE',0.6,'["spring-transaction-read-only"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증 정보를 검증하는 주요 인터페이스는 무엇인가?','["UserDetailsService","AuthenticationProvider","PasswordEncoder","AccessDecisionVoter"]','{"correct":1}','REMEMBER',0.6,'["spring-security-authentication-provider"]'),
('BACKEND_SPRING','MCQ','Spring AOP에서 advice의 종류는 몇 가지가 있으며, 각각이 어떤 역할을 하는지 설명해보자.','["전 처리(before), 후 처리(after) 두 가지로 전처리는 메소드 호출 이전에 작업을 수행하고, 후처리는 메소드 호출 이후에 작업을 수행한다.","전 처리(before), 후 처리(after), 예외 처리(after-throwing) 세 가지로 전처리는 메소드 호출 이전에 작업을 수행하고, 후처리와 예외 처리는 각각 메소드 호출 직후 및 예외 발생 시 작업을 수행한다.","예외 처리(around), 반환 처리(return) 두 가지로 예외 처리는 메소드 실행의 정상적인 흐름과 예외 상황 모두에 반응하며, 반환 처리는 메소드의 결과를 조작한다.","위치(pointcut)와 advice 두 가지로 위치는 특정 메소드가 호출되는 지점을 지정하고, advice는 해당 위치에서 수행할 작업을 정의한다."]','{"correct":1}','UNDERSTAND',0.6,'["spring-aop-advice"]'),
('BACKEND_SPRING','MCQ','Spring Boot Actuator의 기본 엔드포인트 중 하나인 ''health'' 엔드포인트는 어떤 정보를 제공하는가?','["애플리케이션의 전체 로그 데이터","애플리케이션의 현재 상태 및 건강도","애플리케이션의 메모리 사용량","애플리케이션의 트랜잭션 로그"]','{"correct":1}','REMEMBER',0.6,'["spring-boot-actuator-health-endpoint"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 JPA 엔티티와 연관 관계를 정의할 때 @JoinColumn 어노테이션이 어떤 역할을 하는가?','["외래 키 컬럼을 지정한다.","엔티티 클래스 간 관계를 설정하지 않는다.","엔티티 클래스의 기본 키를 설정한다.","데이터베이스 인덱스를 생성한다."]','{"correct":0}','UNDERSTAND',0.6,'["jpa-join-column"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 사용자가 인증(authentication)되었는지를 확인하는 방법은 여러 가지가 있습니다. 다음 중 Spring Security의 `UserDetails` 객체를 통해 사용자의 권한을 검사할 수 있는 올바른 코드 조각은 무엇인가요?','["authentication.getPrincipal().getAuthorities()","SecurityContextHolder.getContext().getAuthentication().getName()","securityContext.getAuthentication().isAuthenticated()","authentication.getPrincipal().toString()"]','{"correct":0}','ANALYZE',0.6,'["spring-security-authentication"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증과 인가를 구분하는 것은 무엇인가?','["인가는 사용자의 권한을 확인하고 인증은 로그인 여부를 확인한다.","인증은 사용자의 권한을 확인하고 인가는 로그인 여부를 확인한다.","인증은 사용자의 신원을 확인하고 인가는 접근 허용 여부를 확인한다.","두 가지 모두 올바르지 않다."]','{"correct":2}','UNDERSTAND',0.6,'["spring-security"]'),
('BACKEND_SPRING','MCQ','JPA에서 N+1 문제를 해결하기 위해 fetch join을 사용할 때 주의해야 할 점이 무엇인가요?','["fetch join은 모든 연관 엔티티를 즉시 로딩하며, 성능에 영향을 미칠 수 있습니다.","fetch join은 지연 로딩된 엔티티만 적용할 수 있습니다.","fetch join은 N+1 문제를 완전히 해결하지 못합니다.","N+1 문제는 JPA에서 발생하지 않습니다."]','{"correct":0}','ANALYZE',0.6,'["jpa-fetch-join"]'),
('BACKEND_SPRING','MCQ','Spring Security의 인증과 인가는 각각 어떤 역할을 하는가?','["인증은 사용자 권한 확인, 인가는 로그인 여부 판단.","인증은 로그인 여부 판단, 인가는 사용자 권한 확인.","인증은 비밀번호 검사, 인가는 URL 접근 제어.","인증은 URL 접근 제어, 인가는 비밀번호 검사."]','{"correct":1}','UNDERSTAND',0.6,'["spring-security-authentication-authorization"]'),
('BACKEND_SPRING','MCQ','Spring 프로젝트에서 @ConditionalOn* 어노테이션의 주요 역할은 무엇인가?','["프로젝트의 의존성 설정을 조정한다.","특정 조건 하에 빈을 등록하거나 초기화하는 것을 제어한다.","스프링 부트 애플리케이션 프로파일 설정을 변경한다.","환경 변수를 바탕으로 특정 클래스가 로딩되는 것을 결정한다."]','{"correct":1}','UNDERSTAND',0.6,'["spring-conditional-on"]'),
('BACKEND_SPRING','MCQ','JPA에서 N+1 문제를 해결하는 가장 효과적인 방법은 무엇인가?','["fetch join 사용","EntityGraph 사용","N+1 쿼리에 대한 최적화 전략 설정","EAGER 로딩을 모든 관계에 적용한다."]','{"correct":0}','EVALUATE',0.6,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 `@ConditionalOnProperty` 어노테이션을 사용하여 특정 속성을 조건부로 활성화시키는 방법은 여러 가지가 있습니다. 다음 중 올바른 방법을 선택하세요.','["application.yml 파일에 설정된 프로퍼티를 기반으로 클래스 또는 빈을 활성화합니다.","환경 변수만이 `@ConditionalOnProperty`의 조건을 충족시킬 수 있습니다.","모든 속성이 비어있거나 null인 경우에도 클래스가 활성화됩니다.","프로파일 설정과 상관없이 항상 동작합니다."]','{"correct":0}','ANALYZE',0.6,'["spring-boot-autoconfiguration"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증(Authentication)과 인가(Authorization)의 차이점은 무엇인가?','["인증은 사용자가 정상적인 계정인지 확인하고, 인가는 특정 리소스에 대한 접근 권한을 검사한다.","인증은 특정 리소스에 대한 접근 권한을 확인하고, 인가는 사용자의 정당성을 검사한다.","인증과 인가는 같은 개념으로 구분되지 않는다.","인증은 세션 생성이고, 인가는 쿠키 설정이다."]','{"correct":0}','UNDERSTAND',0.6,'["spring-security-authentication-authorization"]'),
('BACKEND_SPRING','MCQ','Spring Data JPA에서 N+1 문제를 해결하기 위한 방법 중 하나는 무엇인가?','["지연 로딩(lazy loading)을 사용한다.","즉시 로딩(fetch join)을 사용한다.","@Transactional 어노테이션을 제거한다.","싱글 테니스(Single Table) 구조로 설계한다."]','{"correct":1}','UNDERSTAND',0.6,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','MCQ','Spring Transaction의 @Transactional(readOnly = true)는 어떤 동작을 수행하는가?','["트랜잭션을 읽기 전용 모드로 설정한다.","트랜잭션이 롤백되지 않는다.","트랜잭션은 쓰기 가능하지만 읽기 전용으로 제한된다.","트랜잭션의 격리 수준을 변경한다."]','{"correct":0}','UNDERSTAND',0.6,'["spring-transaction-read-only"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 CSRF(Cross-Site Request Forgery) 공격에 대비하기 위해 필요한 설정은 무엇인가?','["application.yml 파일에 csrf.enabled=false로 설정한다.","@EnableWebSecurity 설정 클래스 내에서 HttpSecurity를 통해 csrf().disable()을 호출한다.","spring.security.csrf.enable=true로 프로퍼티 파일에 활성화한다.","스프링 시큐리티 필터 체인에서 CSRF 토큰 검증 필터를 제거해야 한다."]','{"correct":1}','EVALUATE',0.8,'["spring-security-csrf"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 Redis를 사용하여 캐싱 기능을 구현하려고 할 때, 캐시 명령어를 처리하는 주요 인터페이스는 무엇인가?','["CacheManager","RedisTemplate","JedisClient","CachingConfigurer"]','{"correct":1}','APPLY',0.8,'["spring-redis-cache"]'),
('BACKEND_SPRING','MCQ','Spring Boot의 자동 설정 클래스가 정의된 위치는 어디인가요?','["org.springframework.boot.autoconfigure","org.springframework.context.annotation","org.springframework.beans.factory","org.springframework.transaction.annotation"]','{"correct":0}','REMEMBER',0.8,'["spring-boot-auto-configuration"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 프로파일 기반 설정을 사용하려고 할 때, 특정 환경에 맞는 설정 파일은 어디서 찾을 수 있나?','["application.yml","application-{profile}.yml","src/main/resources/application.properties","application-dev.yml"]','{"correct":1}','APPLY',0.8,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','Spring AOP에서 @Transactional 어노테이션이 적용된 메서드에서 프록시를 통한 자기 호출(self-invocation)은 어떻게 동작하나?','["정상적으로 트랜잭션 컨텍스트가 유지된다.","트랜잭션 컨텍스트가 유지되지 않는다.","트랜잭션이 롤백된다.","트랜잭션이 강제로 커밋된다."]','{"correct":1}','APPLY',0.8,'["spring-aop-proxy"]'),
('BACKEND_SPRING','MCQ','Spring에서 비동기 작업을 처리할 때 `@Async` 어노테이션의 사용은 중요합니다. 다음 중 올바른 방법을 선택하세요.','["비동기 메서드는 항상 새로운 쓰레드를 생성하여 실행됩니다.","스프링 컨텍스트가 모든 비동기 메서드 호출에 대해 결과를 기다리는 동안 블로킹합니다.","비동기 메서드에서 발생한 예외는 자동으로 처리되지 않습니다.","비동기 메서드의 결과를 받아오는 방법은 반드시 Future 객체를 사용해야 합니다."]','{"correct":3}','ANALYZE',0.8,'["spring-async"]'),
('BACKEND_SPRING','MCQ','Spring Boot 애플리케이션에서 @ConditionalOnWebApplication 어노테이션이 어떤 역할을 하는가?','["웹 애플리케이션에서만 빈을 생성한다.","스프링 부팅 설정 파일에 대한 조건식을 만든다.","일반적인 스프링 애플리케이션에서만 빈을 생성한다.","@WebApplicationType 어노테이션과 동일한 역할을 한다."]','{"correct":0}','UNDERSTAND',0.8,'["spring-boot"]'),
('BACKEND_SPRING','MCQ','Spring Boot는 자동 설정(auto-configuration) 기능을 제공합니다. 이 기능은 애플리케이션의 의존성과 활성화 프로파일에 따라 빈들을 자동으로 구성하는 역할을 합니다.','["자동으로 필요한 빈을 추가하지 않음","특정 설정 클래스가 활성화되지 않을 경우 오류를 발생시킴","설정 파일(application.yml)에 명시적으로 작성된 내용만 사용함","프로파일과 의존성에 따라 자동으로 설정 클래스를 활성화함"]','{"correct":3}','REMEMBER',0.8,'["spring-boot-auto-configuration"]'),
('BACKEND_SPRING','MCQ','Kafka에서 Consumer Group의 역할과 파티션 배정은 어떻게 이루어지는지 설명해보자.','["Consumer Group은 메시지를 처리하는 여러 컨슈머를 모아 하나의 단위로 취급하며, 각 파티션이 특정 컨슈머에게 고유하게 할당된다.","Consumer Group은 동일한 주제에 대한 모든 메시지를 읽기 위해 독립적으로 작동하는 컨슈머들의 집합이며, 각 파티션은 임의로 선택된 컨슈머에게 할당된다.","Consumer Group은 Kafka 클러스터 내에서 중복된 메시지 처리를 방지하기 위한 식별자로서 동작하며, 모든 파티션이 단일 Consumer Group에 속한다.","각 파티션은 특정 소비그룹의 컨슈머에게 고유하게 할당되며, 리밸런싱 시점에서 자동적으로 파티션이 재할당된다."]','{"correct":3}','UNDERSTAND',0.8,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Kafka에서 메시지를 보내기 위한 프로듀서 인터페이스는 무엇인가요?','["Producer","MessagePublisher","KafkaProducer","TopicPublisher"]','{"correct":2}','REMEMBER',0.8,'["kafka-producer"]'),
('BACKEND_SPRING','MCQ','Kafka에서 같은 토픽의 메시지를 여러 개의 컨슈머 그룹이 처리할 수 있음에도 불구하고, 하나의 그룹 내에서는 각 파티션에 대해 한 명의 컨슈머만이 메시지를 받을 수 있는 이유는 무엇인가?','["컨슈머 그룹 고유성 때문","파티션이 독립적이라서","메시지 처리 우선순위 때문","Kafka 클러스터 설정 때문"]','{"correct":0}','APPLY',0.8,'["kafka-consumer-group"]'),
('BACKEND_SPRING','MCQ','Spring Data JPA의 N+1 문제는 어떻게 해결할 수 있는가?','["모든 쿼리를 단일 쿼리로 통합한다.","@ManyToOne 관계에서 fetch join을 사용한다.","JPA 엔티티에 @Transient 어노테이션을 적용한다.","Entity Manager를 직접 사용하여 데이터를 가져온다."]','{"correct":1}','UNDERSTAND',0.8,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 application.yml 파일을 사용하여 프로필 설정을 변경할 때, 활성화되지 않는 이유는 무엇인가?','["application.yml 파일 경로가 잘못되었다.","프로파일 이름이 설정되지 않았다.","application.yml 파일 내부에서 프로필 이름이 지정되어 있지 않다.","스프링 부트 애플리케이션이 로드할 때 프로필을 활성화하지 못한다."]','{"correct":2}','EVALUATE',0.8,'["spring-boot-profiles"]'),
('BACKEND_SPRING','MCQ','@Transactional 어노테이션으로 지정된 메서드가 예외를 던지면, 다음 중 어느 경우 해당 트랜잭션이 롤백되나?','["checked exception을 던질 때","unchecked exception을 던질 때","final block에서 checked exception을 던질 때","모든 경우"]','{"correct":1}','APPLY',0.8,'["spring-transaction-propagation"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 인증(Authentication)과 인가(Authorization)의 차이점을 설명해 주세요.','["인증은 사용자의 신원을 확인하고, 인가는 권한을 부여하는 과정입니다.","인증은 권한을 부여하고, 인가는 사용자의 신원을 확인하는 과정입니다.","인증과 인가는 동일한 개념으로, 모두 사용자에게 권한을 부여하는 것을 의미합니다.","Spring Security에서는 인증과 인가를 구분하지 않습니다."]','{"correct":0}','ANALYZE',0.8,'["spring-security-authentication-authorization"]'),
('BACKEND_SPRING','MCQ','JPA에서 지연 로딩(lazy loading)과 즉시 로딩(eager loading)의 주요 차이점은 무엇인가?','["지연 로딩은 데이터베이스 쿼리를 최소화하지만 즉시 로딩은 그렇지 않다.","즉시 로딩은 객체 그래프를 한 번에 로드하지만 지연 로딩은 각각 별도로 불러온다.","지연 로딩은 모든 연관된 엔티티를 미리 가져오지만 즉시 로딩은 필요할 때마다 가져온다.","두 가지 모두 올바르지 않다."]','{"correct":1}','UNDERSTAND',0.8,'["jpa-lazy-loading"]'),
('BACKEND_SPRING','MCQ','JPA에서 N+1 문제를 해결하기 위해 사용할 수 있는 방법 중 하나는 무엇인가?','["fetch join 사용","Entity Graph 사용","Lazy Loading 사용","Eager Loading 사용"]','{"correct":0}','APPLY',0.8,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','MCQ','Kafka에서 메시지 발행을 담당하는 KafkaProducer 클래스가 속한 패키지는 무엇인가?','["org.springframework.kafka.core","org.apache.kafka.clients.consumer","org.springframework.kafka.listener","org.apache.kafka.connect.runtime"]','{"correct":0}','REMEMBER',0.9,'["kafka-producer-consumer"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 사용자의 인증 정보를 저장하기 위해 사용되는 객체는 무엇인가?','["AuthenticationManager","UserDetailsService","SecurityContextHolder","AccessDecisionManager"]','{"correct":2}','REMEMBER',0.9,'["spring-security-authentication"]'),
('BACKEND_SPRING','MCQ','Spring Security에서 JWT를 사용할 때, 액세스 토큰이 만료되었을 때 어떻게 처리해야 하는가?','["JWT 토큰의 만료 시간을 늘린다.","리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급한다.","사용자의 인증 정보를 재입력 받아서 새 토큰을 생성한다.","JWT 토큰의 시크릿 키를 변경한다."]','{"correct":1}','EVALUATE',0.9,'["spring-security-jwt"]'),
('BACKEND_SPRING','MCQ','JPA에서 엔티티 간의 연관 관계 중 ''일대다'' 관계를 정의하기 위해 사용되는 애노테이션은 무엇인가?','["@OneToOne","@OneToMany","@ManyToOne","@ManyToMany"]','{"correct":1}','REMEMBER',0.9,'["jpa-entity-relationship"]'),
('BACKEND_SPRING','MCQ','Spring의 @Transactional 어노테이션은 어떤 동작을 수행하나?','["트랜잭션이 시작될 때마다 새로운 커넥션을 생성한다.","@Transactional이 적용된 메서드는 자동으로 롤백된다.","메서드 실행 중 발생한 모든 예외가 트랜잭션을 롤백시킨다.","트랜잭션 범위 내에서 메서드 호출을 감싸며, 커밋 또는 롤백을 결정한다."]','{"correct":3}','UNDERSTAND',0.9,'["spring-transaction"]'),
('BACKEND_SPRING','MCQ','Spring Boot에서 자동 설정(auto-configuration)을 활성화하기 위해 사용하는 어노테이션은 무엇인가?','["@Configuration","@EnableAutoConfiguration","@ComponentScan","@Profile"]','{"correct":1}','REMEMBER',0.9,'["spring-boot-auto-configuration"]'),
('BACKEND_SPRING','CODE_READING','다음 코드 스니펫에서 @Transactional(readOnly = true)가 적용된 메서드의 동작을 설명해주세요.

@Transactional(readOnly = true)
public List<User> getAllUsers() {
    return repository.findAll();
}
','["DB 조회를 수행하지 않고 캐시에서만 데이터를 가져옵니다.","DB 조회를 수행하고, 트랜잭션은 읽기 전용으로 설정됩니다.","DB에 새로운 사용자를 삽입할 수 있습니다.","트랜잭션이 읽기 전용이므로 DB 조회를 수행하지 않습니다."]','{"correct":1}','UNDERSTAND',0.2,'["spring-transaction-read-only"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Spring Security에서 사용되는 FilterChainProxy의 일부입니다.

public void doFilter(ServletRequest request, ServletResponse response)
    throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response,
            ((HttpServletRequest) request).getServletPath());
        invoke(fi);
}

fi.invoke()을 호출하는 동작에 대해 설명하면 다음과 같습니다.
','["필터 체인의 첫 번째 필터를 실행합니다.","현재 필터 체인이 정의된 순서대로 모든 필터를 실행합니다.","해당 필터만 실행하고 다른 필터는 무시합니다.","필터 체인을 중단시키고 다음 요청에 대한 처리를 종료합니다."]','{"correct":1}','APPLY',0.4,'["spring-security-filter-chain"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 @Transactional(readOnly=true)로 설정되어 있습니다.

@Transactional(readOnly = true)
public void updateOrderStatus(Order order, OrderStatus newStatus){
    // 주문 상태 업데이트 로직
}

이 메소드에서 발생할 수 있는 가장 큰 문제점은 무엇인가요?','["주문 상태 업데이트 로직이 성공적으로 실행됩니다.","read-only 트랜잭션에서는 데이터 변경이 불가능하여 예외가 발생합니다.","트랜잭션이 읽기 전용으로 설정되어도 데이터는 변경될 수 있습니다.","DB 연결이 되지 않습니다."]','{"correct":1}','APPLY',0.4,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드 스니펫에서 @Cacheable 어노테이션이 적용된 메서드는 어떠한 동작을 수행하는지 설명해주세요.

@Cacheable(value = "userCache", key = "{#userId}")
public User getUserById(Long userId) {
    return repository.findById(userId);
}
','["사용자가 이미 캐시에 존재하면 DB 조회 없이 캐시에서 값을 가져옵니다.","DB에서 사용자를 항상 새로 조회하고 캐시에는 저장하지 않습니다.","DB에서 사용자를 조회한 후 캐시에도 항상 저장합니다.","DB에서 사용자 정보를 얻어와서 캐시에 저장하지 않고 바로 반환합니다."]','{"correct":0}','UNDERSTAND',0.4,'["spring-cache-usage"]'),
('BACKEND_SPRING','CODE_READING','다음 JPA 쿼리는 페이징을 사용하여 데이터베이스로부터 레코드를 가져옵니다.

public Page<User> findUsersByAge(Integer age, Pageable pageable) {
    return userRepository.findByAge(age, pageable);
}

public List<User> findByAge(Integer age, Pageable pageable) {
    String jpql = "SELECT u FROM User u WHERE u.age = :age";
    TypedQuery<User> query = em.createQuery(jpql, User.class);
    query.setParameter("age", age);
    query.setFirstResult(pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    return query.getResultList();
}
','["페이징 처리가 제대로 이루어져 정상적으로 데이터를 가져옵니다.","데이터베이스에서 모든 레코드를 불러오고 페이징을 적용합니다.","findUsersByAge 메서드에서 페이지 당 결과 수를 잘못 설정합니다.","해당 코드는 N+1 쿼리 문제를 일으킵니다."]','{"correct":0}','ANALYZE',0.4,'["jpa-n-plus-one"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(propagation = Propagation.REQUIRES_NEW) 설정은 어떤 역할을 하는가?

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void processPayment() {
    paymentService.createTransaction();
}
','["기존 트랜잭션의 범위 내에서 작업을 수행한다.","새로운 트랜잭션을 시작하여 기존 트랜잭션과 분리된다.","기존 트랜잭션이 실패하면 새 트랜잭션을 시도한다.","기존 트랜잭션을 롤백하고 새로운 트랜잭션을 만든다."]','{"correct":1}','EVALUATE',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 Kafka 소비자의 동작을 설명해주세요.

public class MyKafkaConsumer implements Consumer<KafkaMessage> {
    
    @Override
    public void onMessage(KafkaMessage message) {
        // Process the received message
        System.out.println("Received message: " + message.getBody());
    }
}
','["메시지를 처리하지 않고 무시합니다.","Kafka 토픽에서 메시지가 도착하면 해당 메시지를 처리하고 출력합니다.","토픽에 대한 메시지는 Kafka 컨슈머 그룹이 아닌 다른 소비자에게 넘겨줍니다.","받은 메시지의 내용을 로그로 기록하지 않습니다."]','{"correct":1}','UNDERSTAND',0.6,'["kafka-consumer"]'),
('BACKEND_SPRING','CODE_READING','다음 코드의 동작 결과를 선택하시오.

@Cacheable(value = "cacheName", key = "{#id}")
public User getUser(Long id){
    return repository.findById(id).orElse(null);
}

@CacheEvict(value = "cacheName", key = "{#id}", beforeInvocation = true)
public void deleteUser(Long id){
    repository.deleteById(id);
}
','["삭제 이전에 캐시에서 해당 엔티티를 삭제한다.","DB에서 엔티티를 먼저 삭제한 후 캐시에서 해당 ID로 값을 찾는다.","엔티티가 DB에서 제거되었지만 캐시에서는 여전히 존재한다.","캐시에서 값이 삭제되지 않고 DB만 업데이트된다."]','{"correct":0}','REMEMBER',0.6,'["spring-cache"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Spring Boot 애플리케이션에서 프로파일 기반 설정을 사용하는 예입니다.

@Configuration
@Profile("prod")
public class ProductionConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
','["production 환경에서만 데이터 소스를 설정합니다.","개발 환경에서 자동으로 데이터 소스가 생성됩니다.","prod 프로필이 활성화되지 않은 경우 데이터 소스가 설정됩니다.","application.yml에서 설정 값을 무시하고 프로파일을 사용합니다."]','{"correct":0}','ANALYZE',0.6,'["spring-boot-profiles"]'),
('BACKEND_SPRING','CODE_READING','다음 코드 스니펫에서 @ControllerAdvice와 함께 사용되는 @ExceptionHandler 어노테이션의 동작을 설명해주세요.

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
','["특정 예외 발생 시에만 응답 메시지를 반환합니다.","전체 애플리케이션에서 모든 예외 처리를 수행합니다.","주어진 예외가 발생했을 때 HTTP 상태 코드 404와 함께 메시지를 반환합니다.","트랜잭션 롤백을 강제합니다."]','{"correct":2}','UNDERSTAND',0.6,'["spring-exception-handling"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 @Transactional(propagation = Propagation.REQUIRES_NEW)으로 설정되어 있다. 이 때, innerMethod()에서 예외가 발생하면 어떻게 처리되나?

@Transactional
public void outerMethod(){
    try{
        innerMethod();
    } catch(Exception e){
        System.out.println("outer rollback");
    }
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
private void innerMethod() throws Exception {
    // some code that throw exception
}
','["innerMethod()에서 발생한 예외는 outerMethod()의 트랜잭션에 영향을 미치지 않는다.","outerMethod()의 트랜잭션이 롤백되고, innerMethod()의 트랜잭션은 커밋된다.","innerMethod()와 outerMethod()의 트랜잭션 모두 롤백된다.","예외 발생 시 트랜잭션은 유지되며 예외 처리가 수행되지 않는다."]','{"correct":0}','REMEMBER',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Spring Boot 애플리케이션에서 @Transactional 어노테이션이 적용된 save() 메서드입니다. 이 코드가 저장소에 데이터를 삽입할 때 트랜잭션의 격리를 어떻게 설정하는지 설명해주세요.

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void save(User user) {
    repository.save(user);
}
','["트랜잭션의 격리 수준을 REPEATABLE_READ로 설정합니다.","트랜잭션의 격리를 새로운 독립 트랜잭션으로 시작합니다.","트랜잭션의 격리 수준을 READ_COMMITTED로 설정합니다.","트랜잭션의 격리를 현재 트랜잭션과 동일하게 유지합니다."]','{"correct":1}','UNDERSTAND',0.6,'["spring-transaction-propagation"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 @Cacheable 어노테이션을 사용하여 데이터베이스 쿼리를 캐시합니다.

@Cacheable("userCache")
public User findUserById(Integer id) {
    return userRepository.findById(id).orElse(null);
}
','["데이터를 성공적으로 캐싱하고 요청 시 복잡성을 줄입니다.","캐시에 저장된 데이터가 업데이트 되지 않습니다.","findUserById 메서드는 캐시에 저장된 데이터만 반환합니다.","이 코드는 퍼포먼스 저하를 일으킵니다."]','{"correct":0}','ANALYZE',0.6,'["spring-cache"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 @Transactional을 사용하여 데이터베이스 트랜잭션을 처리합니다.

public void transferMoney(Account fromAccount, Account toAccount, int amount) {
    if (fromAccount.getBalance() < amount)
        throw new InsufficientFundsException("Insufficient funds");
    debit(fromAccount, amount);
    credit(toAccount, amount);
}

private void debit(Account account, int amount) throws SQLException {
    // 비즈니스 로직...
    updateBalance(account, -amount);
}

private void credit(Account account, int amount) throws SQLException {
    // 비즈니스 로직...
    updateBalance(account, amount);
}
','["동작은 정상적으로 이루어지며 두 계좌의 잔액이 변경됩니다.","fromAccount에서 금액을 빼고 toAccount에 추가하려고 시도했지만 비정상적인 트랜잭션으로 실패합니다.","transferMoney 메소드가 실행될 때 발생하는 예외는 롤백하지 않습니다.","위 코드에서는 @Transactional 어노테이션이 적용되지 않았습니다."]','{"correct":0}','ANALYZE',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(readOnly = true) 설정이 주어진 상황에서, self-invocation 문제는 어떻게 해결할 수 있는가?

@Transactional(readOnly = true)
public void readOnlyMethod() {
    readOnlyMethod();
}
','["메서드를 동기화 처리한다.","스프링 빈 프록시를 직접 호출하여 트랜잭션을 생성한다.","인터페이스 메서드를 통해 메서드를 호출한다.","@Transactional 어노테이션을 사용하지 않고 별도로 트랜잭션을 시작한다."]','{"correct":2}','EVALUATE',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드의 동작 결과를 선택하시오.

@Transactional(readOnly = false)
public void updateMethod(){
    try{
        repository.save(entity);
        throw new Exception("Exception occurred");
    } catch(Exception e){
        System.out.println("caught exception");
    }
}
','["예외가 catch 블록에서 처리되어 트랜잭션이 정상 커밋되고 엔티티가 저장된다.","checked exception이므로 Spring이 자동으로 트랜잭션을 롤백하여 엔티티가 저장되지 않는다.","catch 블록에서 예외를 처리했더라도 Spring은 트랜잭션을 강제로 롤백한다.","예외가 발생하는 즉시 트랜잭션이 종료되어 메서드 실행이 중단된다."]','{"correct":0}','REMEMBER',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 @Transactional(readOnly = true)로 설정된 메서드에서 save()를 호출할 때 어떤 일이 발생하나?

@Transactional(readOnly = true)
public void readOnlyMethod(){
    repository.save(entity);
}
','["실행 중 예외가 발생한다.","정상적으로 데이터베이스에 엔티티가 저장된다.","데이터베이스 연결이 열리지만 아무 작업도 수행되지 않는다.","엔티티는 영속성 컨텍스트에만 들어간다."]','{"correct":0}','REMEMBER',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(propagation = Propagation.REQUIRES_NEW) 설정이 주어진 상황에서, 기존 트랜잭션의 영향은 무엇인가?

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void processPayment() {
    paymentService.createTransaction();
}
','["기존 트랜잭션이 롤백된다.","기존 트랜잭션이 커밋된다.","기존 트랜잭션은 유지되지만 새 트랜잭션을 추가한다.","기존 트랜잭션과 새 트랜잭션이 병렬로 실행된다."]','{"correct":2}','EVALUATE',0.6,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드의 동작 결과를 선택하시오.

@Transactional(readOnly = false)
public void updateMethod(){
    repository.save(entity);
    throw new RuntimeException("Exception occurred");
}
','["트랜잭션이 유지되고 엔티티가 저장된다.","트랜잭션은 롤백되지만 엔티티는 저장되지 않는다.","엔티티는 저장되지만 트랜잭션이 예외로 인해 롤백된다.","예외 처리 없이 프로그램이 종료된다."]','{"correct":2}','REMEMBER',0.8,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Spring Security에서 인증을 처리하는 예입니다.

@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/public/**").permitAll()
        .anyRequest().authenticated();
}
','["인증된 사용자만 비공개 경로에 대한 요청을 처리합니다.","비밀번호 해싱이 적용됩니다.","로그인 시 CSRF 보호를 무시합니다.","모든 요청은 인가 단계에서 허용됩니다."]','{"correct":0}','ANALYZE',0.8,'["spring-security-authentication"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(readOnly = true) 설정은 어떤 역할을 하는가?

@Transactional(readOnly = true)
public void readData() {
    List<User> users = userRepository.findAll();
}
','["트랜잭션을 시작하지 않고 데이터를 읽는다.","데이터를 읽지만 변경사항을 커밋할 수 없다.","데이터베이스에 대한 모든 쓰기 작업을 금지한다.","데이터베이스에서 읽은 내용만 메모리에 저장한다."]','{"correct":2}','EVALUATE',0.8,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드의 @Cacheable 동작을 선택하시오.

@Cacheable(value = "cacheName", key = "{#id}")
public User getUser(Long id){
    return repository.findById(id).orElse(null);
}
','["메서드 호출 시마다 캐시에 저장된다.","캐시에서 값을 찾지 못하면 DB에서 가져와 캐시에 저장한다.","DB에서 값을 가져오지 않고 항상 캐시만 참조한다.","캐시를 무시하고 매번 DB에서 새로 조회한다."]','{"correct":1}','REMEMBER',0.8,'["spring-cache"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(readOnly = true) 설정이 주어진 상황에서, readOnlyMethod() 메서드의 호출은 어떻게 작동하는가?

@Transactional(readOnly = true)
public void readOnlyMethod() {
    repository.delete(entity);
}
','["데이터베이스에서 엔티티를 삭제한다.","엔티티의 삭제 요청만 저장하여 커밋 시 처리된다.","readOnly 설정으로 인해 예외가 발생한다.","메서드 호출은 무시되고 아무런 동작도 수행하지 않는다."]','{"correct":2}','EVALUATE',0.8,'["spring-transaction"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Kafka의 컨슈머 그룹을 사용하여 메시지를 처리하는 예입니다.

public class OrderConsumer implements Consumer<String, String> {
    @Override
    public void consume(String message) {
        // 주문 정보 처리 로직
    }
}

이렇게 작성한 컨슈머에서 가장 중요한 것은 무엇인가요?','["메시지의 순서를 보장해야 합니다.","메시지를 무결하게 처리하고 오프셋을 커밋해야 합니다.","컨슈머 그룹 내부에서 동일한 메시지는 중복으로 처리되어야 합니다.","Kafka 클러스터에 대한 연결 상태를 관리해야 합니다."]','{"correct":1}','APPLY',0.8,'["kafka-consumer-group"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Kafka 프로듀서에서 메시지를 발행하는 예입니다.

Producer<String, String> producer = new DefaultKafkaProducerFactory<>(producerConfigs).createProducer();

public void sendMessage(String topic, String key, String message) {
    ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
    Future<RecordMetadata> sendFuture = producer.send(record);
}
','["메시지 발행이 성공적으로 이루어지고 ack를 기다립니다.","발행한 메시지는 즉시 데이터베이스에 저장됩니다.","sendMessage 메서드는 비동기 호출을 지원하지 않습니다.","sendFuture의 결과가 항상 성공적입니다."]','{"correct":0}','ANALYZE',0.8,'["kafka-producer"]'),
('BACKEND_SPRING','CODE_READING','다음 Spring Security 필터 체인의 동작을 설명해주세요. 

public class CustomSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
            .and()
            .csrf().disable();
    }
}
','["CSRF 보호를 비활성화하고, /admin 경로에 대한 요청은 ADMIN 역할이 있어야 합니다.","모든 경로에 대해 CSRF 보호가 적용됩니다.","/admin 경로에 대한 요청은 ADMIN 역할이 없어도 접근할 수 있습니다.","Spring Security 필터 체인을 사용하지 않습니다."]','{"correct":0}','UNDERSTAND',0.8,'["spring-security-csrf"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Spring Boot에서 @ConditionalOnProperty를 사용해 설정된 프로파일에 따라 빈을 등록하는 예입니다.

@Configuration
@ConditionalOnProperty(name = "myapp.enabled", havingValue = "true")
public class MyAppConfig {
    // 빈 정의 로직
}

이렇게 설정했을 때, myapp.enabled가 false인 경우 AppConfig는 빈으로 등록되지 않습니다.','["AppConfig 클래스는 항상 빈으로 등록됩니다.","myapp.enabled가 true일 때만 AppConfig 클래스가 빈으로 등록됩니다.","myapp.enabled 값과 상관없이 AppConfig 클래스는 빈으로 등록됩니다.","AppConfig 클래스는 항상 빈으로 등록되지 않습니다."]','{"correct":1}','APPLY',0.9,'["spring-boot-conditional"]'),
('BACKEND_SPRING','CODE_READING','다음 코드는 Kafka 컨슈머에서 메시지를 처리하는 예입니다.

public class OrderConsumer implements Consumer<String, String> {
    @Override
    public void consume(String message) {
        try {
            processOrder(message);
            commitOffset();
        } catch (Exception e) { 
            rollbackTransaction();
        }
    }
}

이 코드에서 메시지 처리 중 예외가 발생했을 때의 동작은 무엇인가요?','["예외가 발생하면 오프셋 커밋이 무시됩니다.","예외가 발생하면 트랜잭션이 롤백되고 오프셋 커밋도 되지 않습니다.","예외가 발생해도 트랜잭션은 유지되며 오프셋 커밋만 무시됩니다.","메시지는 다시 처리되지 않고 예외 상황이 완전히 무시됩니다."]','{"correct":1}','APPLY',0.9,'["kafka-transactional"]'),
('BACKEND_SPRING','CODE_READING','다음 JPA 엔티티 코드는 고객 주문 정보를 표현하는 Order 클래스입니다.

@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
}

이렇게 설정된 경우, 주문 엔티티와 관련된 고객 엔티티의 데이터가 실제로 로딩되는 시점은 언제인가요?','["주문 엔티티를 조회할 때 즉시 로딩됩니다.","주문 엔티티에 대한 모든 쿼리가 실행될 때 로딩됩니다.","Lazy로 설정되어 있어 고객 정보는 직접적으로 접근하지 않으면 로딩되지 않습니다.","customer 객체를 강제로 초기화해야만 데이터가 로딩됩니다."]','{"correct":2}','APPLY',0.9,'["jpa-lazy-loading"]'),
('BACKEND_SPRING','CODE_READING','다음 코드에서 @Transactional(readOnly = true) 설정이 주어진 상황에서, readOnlyMethod()가 호출된 결과는 무엇인가?

@Transactional(readOnly = true)
public void readOnlyMethod() {
    repository.save(entity);
}
','["데이터베이스에 엔티티를 저장한다.","엔티티의 변경사항을 커밋하지 않는다.","readOnly 설정으로 인해 예외가 발생한다.","데이터베이스에서 읽은 내용만 메모리에 저장한다."]','{"correct":2}','EVALUATE',0.9,'["spring-transaction"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 비동기 데이터를 로딩 중인 상태를 관리하기 위한 올바른 훅 사용 방법은?','["const [loading, setLoading] = useState(true);\nuseEffect(() => { fetch(apiUrl).then(data => data.json()).finally(setLoading(false)); }, []);","const [data, setData] = useState(null);\nuseEffect(() => { fetch(apiUrl).then(response => response.json()).then(setData); }, []);","const [loading, setLoading] = useState(true);\nfetch(apiUrl)\n.then(data => data.json())\n.then(res => { setLoading(false) });","<LoadingSpinner isLoading={loading} />"]','{"correct":0}','APPLY',0.2,'["hooks-api-loading-status"]'),
('FRONTEND_REACT','MCQ','React.memo와 useCallback 훅의 주요 차이점은 무엇인가요?','["비교 로직 제공 vs 참조 동일성 반환","DOM 업데이트 최적화 vs 상태 관리","렌더링 성능 개선 vs 함수 메모이제이션","동적으로 생성된 컴포넌트 관리 vs 외부 의존성 처리"]','{"correct":2}','ANALYZE',0.2,'["react-memo","react-hooks"]'),
('FRONTEND_REACT','MCQ','리액트에서 `React.memo` 훅을 사용하여 성능 최적화를 적용할 때, 컴포넌트가 재렌더링되지 않도록 하려면 어떤 조건이 필요합니다.','["props가 변경될 때마다 새로운 객체를 반환해야 합니다.","키 값을 고유하게 유지해야 합니다.","키 값은 변경되면 안 됩니다.","컴포넌트는 무효화되어야 합니다."]','{"correct":1}','APPLY',0.2,'["rendering-performance"]'),
('FRONTEND_REACT','MCQ','Redux 스토어와 Context API를 비교할 때, 어떤 상황에서 Redux가 더 적합한가?','["전역 상태 관리가 필요할 때","특정 컴포넌트 범위 내의 상태만 관리할 때","상태 변화에 따른 렌더링 최적화를 원할 때","컴포넌트 트리를 쉽게 테스트하고 싶을 때"]','{"correct":0}','EVALUATE',0.2,'["state-management","redux-context-api"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 키보드 이벤트를 처리하기 위해 사용되는 이벤트 핸들러는 어떤 이름을 갖습니까?','["onKeyDown","onClick","onChange","onSubmit"]','{"correct":0}','REMEMBER',0.2,'["react-events-handling"]'),
('FRONTEND_REACT','MCQ','useContext 훅은 어떤 상황에서 주로 활용되나요?','["전역 상태 관리 시","비동기 로딩 데이터 처리 시","렌더링 성능 최적화 시","컴포넌트 간 통신 시"]','{"correct":0}','ANALYZE',0.2,'["react-hooks"]'),
('FRONTEND_REACT','MCQ','React Testing Library에서 렌더링된 컴포넌트의 특정 요소를 검증할 때, 어떤 방법으로 해당 요소가 존재하는지 확인할 수 있나요?','["findDOMNode 메서드 사용","document.querySelector로 직접 선택","screen.queryByRole로 요소 선택","useRef 훅을 통해 접근"]','{"correct":2}','APPLY',0.2,'["testing-react-testing-library"]'),
('FRONTEND_REACT','MCQ','리액트 컴포넌트에서 `useEffect` 훅을 사용하여 API 요청을 할 때, 상태 업데이트와 이펙트 사이에 경쟁 상태(race condition)를 피하기 위한 방법은 무엇인가요?','["API 요청 후 상태를 업데이트합니다.","API 요청 전 상태를 업데이트합니다.","API 요청과 동시에 상태를 업데이트합니다.","API 요청 대신 콜백을 사용하여 상태를 업데이트합니다."]','{"correct":1}','APPLY',0.4,'["async-data"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 `useState` 훅을 사용할 때 초기 상태 값을 설정하는 올바른 방법은?','["const [count, setCount] = useState(0);","const [count, setCount] = useState(() => 1 + Math.random());","const [count, setCount] = useState(() => (Math.random() < 0.5) ? 0 : 1);","const [count, setCount] = useState(Math.floor(Math.random() * 2));"]','{"correct":0}','APPLY',0.4,'["hooks-state-management"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 props를 사용할 때, 아래 중 가장 중요한 특징은 무엇인가요?','["props는 반드시 객체 형태여야 한다.","props는 부모 컴포넌트에서 자식 컴포넌트로 전달된다.","props는 동적으로 변경될 수 있다.","props는 항상 불변해야 한다."]','{"correct":1}','UNDERSTAND',0.4,'["jsx-props"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 비제어 컴포넌트를 사용하는 장점은 무엇인가요?','["더 빠른 업데이트","더 쉬운 상태 관리","덜 자주 리렌더링","더 적은 메모리 사용"]','{"correct":1}','ANALYZE',0.4,'["react-forms"]'),
('FRONTEND_REACT','MCQ','React의 JSX에서 속성을 나타낼 때, 문자열 값은 반드시 큰따옴표로 감싸야 합니다. 예외는 어떤 경우인가요?','["true와 같은 Boolean 값은 따옴표 없이 사용 가능하다.","null과 undefined는 반드시 큰따옴표로 감싸야 한다.","리터럴 숫자는 큰따옴표로 감싸서 사용해야 한다.","모든 속성 값은 항상 큰따옴표로 감싸야 한다."]','{"correct":0}','REMEMBER',0.4,'["jsx-syntax"]'),
('FRONTEND_REACT','MCQ','useEffect 내부에서 상태 업데이트를 수행하면 어떤 일이 발생할까요?','["컴포넌트가 다시 렌더링되지 않음","컴포넌트가 무한 재렌더링 되는 상황이 발생할 수 있음","상태 변화에 따른 의존성 배열에 해당 상태가 자동으로 추가됨","useEffect 내부에서만 상태 업데이트가 적용됩니다"]','{"correct":1}','ANALYZE',0.4,'["hooks-effect-dependency-array"]'),
('FRONTEND_REACT','MCQ','React의 Context API는 무엇을 해결하기 위해 사용됩니까?','["컴포넌트 간 공유 데이터 전달","컴포넌트 성능 최적화","DOM 레이아웃 업데이트","렌더링 사이클 단순화"]','{"correct":0}','UNDERSTAND',0.4,'["context-api"]'),
('FRONTEND_REACT','MCQ','리액트에서 `useEffect` 훅을 사용하여 특정 이벤트가 발생할 때마다 상태를 업데이트하려면, 어떤 코드 구조를 사용해야 합니까?','["이벤트 리스너를 useEffect 내부에 등록합니다.","이벤트 리스너를 컴포넌트 생성 시점에서 등록합니다.","이벤트 리스너를 컴포넌트의 메서드로 정의합니다.","이벤트 리스너를 고유한 키 값으로 등록합니다."]','{"correct":0}','APPLY',0.4,'["hooks-lifecycle"]'),
('FRONTEND_REACT','MCQ','React Suspense와 코드 스플리팅을 함께 사용할 때, 어떤 문제점이 발생할 수 있나?','["무한 루프 생성","중복 데이터 로드","순차적인 컴포넌트 렌더링","비동기 상태 처리 실패"]','{"correct":2}','EVALUATE',0.4,'["react-suspense","code-splitting"]'),
('FRONTEND_REACT','MCQ','useMemo와 useCallback 훅의 주된 차이점은 무엇인가?','["useCallback는 불변성 유지, useMemo는 성능 최적화","useMemo는 함수를 메모라이즈하고 useCallback은 객체 참조를 메모라이즈한다","useCallback은 함수 호출 시점에 대한 메모리 저장을 제공하며, useMemo는 값의 변형 여부와 상관없이 그 결과를 반환한다","useMemo는 불변성 유지, useCallback은 성능 최적화"]','{"correct":2}','ANALYZE',0.4,'["hooks-memo-callback"]'),
('FRONTEND_REACT','MCQ','React의 함수형 컴포넌트에서는 상태(state)를 관리하기 위해 사용하는 훅은 무엇인가요?','["useReducer()","useState()","useCallback()","useMemo()"]','{"correct":1}','UNDERSTAND',0.4,'["hooks-usestate"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 비제어 상태와 제어 상태의 차이점을 설명하고, 각각 어떤 상황에서 사용해야 하는지 선택하라.','["비제어 상태는 DOM에 직접 접근하여 상태를 변경하며, 제어 상태는 React.setState()로 상태를 업데이트","비제어 상태는 항상 최상위 컴포넌트에서만 작동하며, 제어 상태는 하위 컴포넌트에서도 사용 가능","비제어 상태는 성능 향상을 위해 사용되며, 제어 상태는 보안을 위한 암호화를 제공","비제어 상태는 키보드 접근성을 개선하고, 제어 상태는 DOM 변경사항을 추적하기 위함"]','{"correct":0}','EVALUATE',0.4,'["controlled-vs-uncontrolled-components","form-handling"]'),
('FRONTEND_REACT','MCQ','React에서 `useEffect` 훅으로 특정 API를 요청할 때 콜백 함수 내부에서 에러 처리를 하는 방법은?','["fetch(apiUrl)\n.then(response => response.json())\n.catch(error => console.error(''Error:'', error));","fetch(apiUrl).then(response => { if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`); return response.json(); }).catch(error => console.error(''Error:'', error));","fetch(apiUrl).then(data => data.json()).catch(console.log);","const response = await fetch(apiUrl);\nconsole.log(await response.text());"]','{"correct":1}','APPLY',0.4,'["hooks-api-fetching"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 상태 업데이트가 여러 번 발생할 때, 어떤 방법을 사용하여 성능 최적화를 할 수 있나?','["useEffect()를 이용한 배치 업데이트","useState()를 두 번 호출","setState() 메서드를 직접 사용","React.memo()와 shouldComponentUpdate() 메소드를 함께 사용"]','{"correct":0}','EVALUATE',0.4,'["react-hooks","state-management"]'),
('FRONTEND_REACT','MCQ','React Testing Library에서 act 함수는 무엇을 목적으로 사용하나요?','["렌더링 성능 향상","동적 데이터 처리","비동기 동작 처리","DOM 접근성 테스트"]','{"correct":2}','UNDERSTAND',0.4,'["testing-library-act-function"]'),
('FRONTEND_REACT','MCQ','useEffect 훅에서 의존성 배열에 포함되지 않은 변수를 참조하면 어떤 문제가 발생하나요?','["무한 루프 생성","배치 업데이트 발생","상태 누락","렌더링 성능 저하"]','{"correct":0}','ANALYZE',0.4,'["react-hooks"]'),
('FRONTEND_REACT','MCQ','useContext 훅을 사용하여 전역 상태를 관리할 때 주의해야 할 점은 무엇인가?','["상태 값을 무한히 참조하는 것만으로도 충분하다","상태 값이 변경될 때마다 컴포넌트 전체를 재렌더링하게 됨","컴포넌트에서 상태 값에 접근할 수 없게 된다","상위 컴포넌트의 상태가 하위 컴포넌트로 전달되지 않음"]','{"correct":1}','ANALYZE',0.4,'["context-api-state-management"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 상태를 관리하기 위해 가장 일반적으로 사용되는 훅은 무엇인가요?','["useState","useEffect","useContext","useReducer"]','{"correct":0}','REMEMBER',0.4,'["hooks-state-management"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 상태 업데이트는 언제 이루어집니까?','["렌더링 직후","렌더링 중","렌더링 전","렌더링 후"]','{"correct":0}','REMEMBER',0.4,'["react-state-updates"]'),
('FRONTEND_REACT','MCQ','React에서 렌더링 성능을 개선하기 위해 사용하는 HOC(Higher-Order Component)와 Hooks를 대체할 수 있는 개념은 무엇인가요?','["클래스 컴포넌트","Redux","Context API","커스텀 훅"]','{"correct":3}','UNDERSTAND',0.4,'["hooks-custom-hooks"]'),
('FRONTEND_REACT','MCQ','useEffect 내부에서 useState를 호출하여 상태 업데이트를 수행할 때, 어떤 문제점이 발생할 수 있습니까?','["배치 업데이트가 일어나지 않음","무한 루프로 인해 성능 저하","렌더링 순서가 변경됨","상태 업데이트가 즉시 이루어짐"]','{"correct":1}','EVALUATE',0.6,'["useeffect-dependency-array","state-update-batch"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 비동기 로직을 처리하고 있는 `useEffect` 훅 내부에서 API 요청을 취소하는 방법은?','["const controller = new AbortController();\nfetch(apiUrl, { signal: controller.signal }).then(response => response.json());","await fetch(apiUrl);","abort();","return () => {}"]','{"correct":0}','APPLY',0.6,'["hooks-api-cancel"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 props를 사용할 때, 다음 중 무엇이 올바르게 사용되는 방법은 아닙니다.','["props의 값에 직접 접근하여 수정한다.","props를 복사한 후 변경하고 싶은 값을 바꾼다.","PropTypes를 사용하여 props 타입을 정의한다.","렌더 메서드에서 props를 사용하여 JSX를 렌더링한다."]','{"correct":0}','UNDERSTAND',0.6,'["props"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 비제어 컴포넌트를 사용할 때, state를 관리하는 올바른 방법은?','["function MyComponent() { const [value, setValue] = useState(''''); return <input value={value} onChange={(e) => setValue(e.target.value)} />; }","function MyComponent() { let value = ''''; return <input value={value} onChange={(e) => value = e.target.value} />; }","function MyComponent() { const value = ''''; return <input value={value} onChange={(e) => value = e.target.value} />; }","function MyComponent() { let [value, setValue] = useState(''''); return <input value={value} onChange={(e) => value = e.target.value} />; }"]','{"correct":0}','REMEMBER',0.6,'["controlled-components"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 렌더링 성능을 개선하기 위해 `React.memo`를 사용할 때, 재렌더링 방지를 위한 올바른 조건은?','["const MemoComponent = React.memo(Component);","const MemoComponent = React.memo((props) => <Component {...props} />);\nreturn <MemoComponent key={key} {...otherProps} />;","<Component key={key} {...otherProps} />","const MemoComponent = React.memo(Component, (prevProps, nextProps) => prevProps.count === nextProps.count);"]','{"correct":3}','APPLY',0.6,'["hooks-render-memoization"]'),
('FRONTEND_REACT','MCQ','useEffect 훅을 사용하여 특정 이벤트에 대한 리렌더링을 제어할 때, 의존성 배열에서 주의해야 하는 점은 무엇인가요?','["이벤트 핸들러를 의존성으로 추가하지 않아야 함","리액트 인스턴스 상태를 의존성으로 추가해야 함","변수 값이 변경되었는지 확인해야 함","모든 상태 변화에 반응하도록 무한 루프 생성"]','{"correct":0}','APPLY',0.6,'["hooks-useeffect"]'),
('FRONTEND_REACT','MCQ','useEffect 내부에서 의존성 배열에 포함되지 않은 상태 값이 변경되었을 때, 어떤 문제가 발생할 수 있습니까?','["이전 렌더링 시점의 오래된(stale) 상태 값을 참조하여 예상과 다르게 동작한다.","컴포넌트가 언마운트된 후에도 useEffect가 계속 실행된다.","상태 값이 변경될 때마다 컴포넌트 트리 전체가 강제로 리렌더링된다.","의존성 배열이 비어있어 useEffect 자체가 한 번도 실행되지 않는다."]','{"correct":0}','EVALUATE',0.6,'["useeffect-dependency-array","stale-closure"]'),
('FRONTEND_REACT','MCQ','React Testing Library에서 act 경고를 피하기 위해 무엇을 해야 합니까?','["비동기 콜백을 사용하지 않음","렌더링 후 즉시 업데이트 상태","이벤트 핸들러를 직접 호출","비동기 동작을 처리하는 방법 변경"]','{"correct":1}','EVALUATE',0.6,'["react-testing-library","act-warnings"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 props를 전달받아 사용할 때, 다음 중 올바르게 작성된 코드는?','["function MyComponent({ name }) { return <div>Hello, {name}</div>; }","function MyComponent(props) { return <div>Hello, {props.name}</div>; }","function MyComponent() { return <div>Hello, props.name</div>; }","function MyComponent({ name: ''John'' }) { return <div>Hello, {name}</div>; }"]','{"correct":1}','REMEMBER',0.6,'["jsx-props"]'),
('FRONTEND_REACT','MCQ','리스트 컴포넌트에서 item의 key 속성을 정확히 설정해야 하는 이유는 무엇인가요?','["렌더링 성능을 최적화하기 위함","뷰를 중복해서 사용하는 것을 방지하기 위함","비동기 데이터 로드 시 동작을 제어하기 위함","리액트 엘리먼트 트리를 식별하기 위함"]','{"correct":3}','APPLY',0.6,'["jsx-key-concepts"]'),
('FRONTEND_REACT','MCQ','리덕스 스토어의 액션 생성 함수에서 비동기 작업을 처리할 때, 가장 좋은 방법은 무엇일까요?','["비동기 작업을 동기적으로 실행","액션 생성 함수 내부에서 then/catch를 사용하여 비동기 작업을 관리","Thunk 미들웨어를 활용하여 비동기 작업을 분리","비동기 작업의 결과를 바로 액션으로 반환"]','{"correct":2}','EVALUATE',0.6,'["redux-thunk","async-action-handling"]'),
('FRONTEND_REACT','MCQ','React에서 컴포넌트가 렌더링될 때, 조건부 렌더링을 적용할 수 있는 방법은?','["const isLoggedIn = true; <div>{isLoggedIn && ''Welcome!''}</div>","<div>{if (isLoggedIn) { return ''Welcome!''; }}</div>","<div>{isLoggedIn : ''Welcome!'', null}</div>","const isLoggedIn = false; <div>Welcome!</div>;"]','{"correct":0}','REMEMBER',0.6,'["jsx-conditional-rendering"]'),
('FRONTEND_REACT','MCQ','React 테스트에서 React Testing Library의 act 함수를 사용하는 이유는 무엇인가?','["렌더링 순서를 정리하기 위함","비동기 코드를 동기화하기 위함","상태 변경을 추적하기 위함","UI 요소에 액세스하기 위함"]','{"correct":1}','REMEMBER',0.6,'["testing-react-act"]'),
('FRONTEND_REACT','MCQ','React Testing Library를 사용하여 컴포넌트 테스트를 할 때, act 경고를 제거하려면 어떻게 해야 하나?','["비동기 코드 실행을 동기화","렌더링 직후에 테스트 쿼리를 수행","act 래퍼를 모든 비동기 테스트 주변에 감싸","DOM 상태 변경을 강제로 적용"]','{"correct":2}','EVALUATE',0.6,'["react-testing-library","testing-hooks"]'),
('FRONTEND_REACT','MCQ','리스트를 렌더링할 때, 각 항목에 고유한 ''key'' 속성을 부여해야 하는 이유는?','["리스트의 순서를 정렬하기 위함","컴포넌트가 재렌더링 될 때 리스트 항목을 식별하기 위함","이벤트 핸들러를 연결하기 위함","스타일 지정을 용이하게 하기 위함"]','{"correct":1}','REMEMBER',0.6,'["jsx-key-rule"]'),
('FRONTEND_REACT','MCQ','React에서 JSX 요소의 고유 식별자를 제공하기 위해 사용되는 속성은?','["id","class","key","data-key"]','{"correct":2}','REMEMBER',0.6,'["jsx-key"]'),
('FRONTEND_REACT','MCQ','리액트에서 `useContext` 훅을 사용하여 컨텍스트의 변경 사항을 감지하고 업데이트하려면, 어떤 방법이 필요합니까?','["상태를 직접 업데이트합니다.","setState 메서드를 호출합니다.","contextType 속성을 설정합니다.","Provider 컴포넌트에서 value를 업데이트합니다."]','{"correct":3}','APPLY',0.6,'["state-management"]'),
('FRONTEND_REACT','MCQ','React에서 useEffect 훅을 이용하여 API 요청을 할 때, 아래 중 가장 중요한 특징은 무엇인가요?','["API 요청 결과를 상태(state)로 업데이트한다.","API 요청이 완료될 때마다 실행된다.","API 요청 시 의존성 배열에 포함된 props 또는 state의 변경으로 인해 재실행됩니다.","API 요청 전에 클린업 함수를 호출해야 한다."]','{"correct":2}','UNDERSTAND',0.6,'["hooks-useeffect"]'),
('FRONTEND_REACT','MCQ','비제어 컴포넌트를 사용하여 입력 폼의 값을 관리할 때, 어떤 방법으로 상태 변경을 감지하고 업데이트해야 하나요?','["onChange 이벤트 핸들러에서 useState 훅 사용","value 속성을 props로 전달","defaultValue 속성 설정","useContext 훅 사용"]','{"correct":0}','APPLY',0.6,'["controlled-vs-uncontrolled-components"]'),
('FRONTEND_REACT','MCQ','React의 Context API를 사용할 때, Provider 컴포넌트는 어떤 역할을 수행하나요?','["상태 값 설정 및 전달","렌더링 최적화","컴포넌트 재사용 가능하게 함","이벤트 핸들러 전달"]','{"correct":0}','UNDERSTAND',0.8,'["context-api-provider"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 `useContext` 훅을 사용하여 전역 컨텍스트의 값을 가져올 때, 초기값(initialValue)을 설정하는 올바른 방법은?','["const context = useContext(ThemeContext);\nconst defaultTheme = ''light'';\nreturn context || defaultTheme;","const ThemeContext = React.createContext();\nconst [theme, setTheme] = useState(''light'');\nuseEffect(() => ThemeContext.Provider({ theme }), []);","<ThemeContext.Provider value={theme}>{children}</ThemeContext.Provider>","const ThemeContext = React.createContext(''light'');"]','{"correct":3}','APPLY',0.8,'["hooks-context-api"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 state를 관리할 때, 상태 값을 업데이트하려면 어떤 방법을 사용해야 하나요?','["props로 전달","setState 메서드를 직접 호출","useState 훅을 사용","클래스의 this.state를 변경"]','{"correct":2}','APPLY',0.8,'["hooks-usestate"]'),
('FRONTEND_REACT','MCQ','React에서 상태(state)를 업데이트할 때, 어떤 방식이 가장 효율적일까요?','["배치 업데이트 사용하기","자주 업데이트하는 변수만 state로 관리하기","props로 데이터 전달하기","렌더링될 때마다 state 초기화하기"]','{"correct":0}','UNDERSTAND',0.8,'["state-management"]'),
('FRONTEND_REACT','MCQ','React에서 Context API를 사용하여 전역 상태를 관리할 때 필요한 요소는?','["Provider 컴포넌트","Consumer 컴포넌트","store 객체","redux의 connect"]','{"correct":0}','REMEMBER',0.8,'["context-api"]'),
('FRONTEND_REACT','MCQ','useContext 훅은 어떤 상황에서 사용되어야 하는가?','["상태 관리에만 사용해야 함","상위 컴포넌트의 상태를 하위 컴포넌트로 전달할 때 사용","동일한 컴포넌트 내에서 여러 상태를 공유할 때 사용","렌더링 성능을 개선하기 위해 사용"]','{"correct":1}','ANALYZE',0.8,'["context-api"]'),
('FRONTEND_REACT','MCQ','리액트 컴포넌트에서 동적으로 생성된 요소를 구분하기 위해 key 속성을 사용하는 이유는 무엇인가요?','["렌더링 성능 최적화","동일한 렌더링 결과 보장","DOM 업데이트 효율성 향상","리액트 내부에서의 상태 관리"]','{"correct":2}','ANALYZE',0.8,'["react-key-prop"]'),
('FRONTEND_REACT','MCQ','비동기 데이터 요청에서 AbortController를 사용하는 주된 이유는 무엇인가요?','["데이터를 더 빠르게 가져오기","API 호출이 실패했을 때 대체 솔루션 제공","비동기 작업의 취소 가능성을 지원","응답을 캐싱하기"]','{"correct":2}','EVALUATE',0.8,'["abortcontroller","async-data-fetching"]'),
('FRONTEND_REACT','MCQ','React의 컨텍스트(Context API)에서 값을 전달하는 방법으로 사용되는 훅은 어떤 이름을 갖습니까?','["useContext","useState","useReducer","useCallback"]','{"correct":0}','REMEMBER',0.8,'["context-api"]'),
('FRONTEND_REACT','MCQ','React에서 JSX의 key 프로퍼티를 사용하는 주된 이유는 무엇인가요?','["컴포넌트 렌더링 성능 향상","리스트 컴포넌트 아이템 식별","DOM 업데이트 최적화","렌더링 순서 제어"]','{"correct":1}','UNDERSTAND',0.8,'["jsx-key"]'),
('FRONTEND_REACT','MCQ','next/router에서 useNavigate를 사용할 때, 페이지 이동을 막고 데이터만 전송하려면 어떻게 해야 하나요?','["useNavigate({ replace: true })","useNavigate({ state: { data } })","useNavigate({ preventDefault: true })","useNavigate({ navigateOptions: { preventBrowserNavigation: true } })"]','{"correct":1}','ANALYZE',0.8,'["react-routing"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 상태(state)를 초기화할 때, useState 훅을 사용하여 정확하게 작성된 코드는?','["const [count] = useState(0);","let count = useState(0);","function MyComponent() { const count = useState(0); }","function MyComponent() { const [count, setCount] = useState(0); }"]','{"correct":3}','REMEMBER',0.8,'["state-management"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 훅(Hook)을 사용할 때, 다음 중 올바른 순서로 작성된 코드는?','["function MyComponent() { useEffect(() => {}, []); return <div>Content</div>; }","function MyComponent() { useState(0); useEffect(() => {}, []); return <div>Content</div>; }","function MyComponent() { const [count, setCount] = useState(0); useEffect(() => {}, []); return <div>Content</div>; }","function MyComponent() { useEffect(() => {}, []); useState(0); return <div>Content</div>; }"]','{"correct":2}','REMEMBER',0.8,'["hooks-rules"]'),
('FRONTEND_REACT','MCQ','리액트에서 `useMemo` 훅을 사용하여 비용이 많이 드는 계산을 메모이제이션하려고 할 때, 의존성 배열에 어떤 항목들을 포함시켜야 합니다.','["계산 함수만 포함합니다.","주요 상태 변수만 포함합니다.","계산에 영향을 주는 모든 상태 변수를 포함합니다.","상태가 아닌 다른 값들입니다."]','{"correct":2}','APPLY',0.8,'["hooks-memoization"]'),
('FRONTEND_REACT','MCQ','React.memo는 어떤 상황에서 사용되어야 하는가?','["렌더링 성능이 저하되는 모든 컴포넌트에 적용해야 함","props 변경 시 컴포넌트를 재렌더링하지 않도록 하기 위해 사용","컴포넌트의 상태 관리에 필요한 최소한의 정보만 보존하도록 사용","상태 변화 없이도 컴포넌트가 렌더링되는 경우에만 사용"]','{"correct":1}','ANALYZE',0.8,'["memoization-react-memo"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 `useContext`와 `Provider`를 사용하여 전역 상태 관리를 할 때 주의해야 할 사항은?','["상태가 변경될 때마다 모든 컴포넌트를 재렌더링한다.","상태 변화에 반응하지 않는 컴포넌트도 항상 업데이트된다.","특정 범위 내에서만 상태가 변경되는 경우, 해당 범위 외의 컴포넌트는 재렌더링되지 않는다.","상태 관리가 불필요한 모든 컴포넌트를 무시한다."]','{"correct":2}','ANALYZE',0.8,'["context-api"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 훅을 호출하는 올바른 위치는 어디인가?','["렌더링의 가장 위쪽","렌더링 블록 내부","조건문 안에서","리턴 문 다음"]','{"correct":0}','REMEMBER',0.8,'["hooks-rules"]'),
('FRONTEND_REACT','MCQ','React 함수형 컴포넌트에서 상태(state)를 관리하기 위해 사용되는 훅은?','["useEffect","useState","useCallback","useMemo"]','{"correct":1}','REMEMBER',0.8,'["hooks-state-management"]'),
('FRONTEND_REACT','MCQ','JSX에서 배열 내부에서 맵(map) 함수를 사용하여 각 요소에 대해 컴포넌트를 렌더링할 때, `key` 속성은 어디에 위치해야 합니다?','["배열의 첫 번째 요소 위에","각 요소 아래","각 요소 위에","배열의 마지막 요소 아래"]','{"correct":2}','REMEMBER',0.9,'["jsx-key-props"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 Context API를 사용하여 상태 공유를 수행했을 때, 컨텍스트의 값을 변경하면 어떤 일이 발생합니까?','["모든 렌더링이 중단됨","변경된 값만 재렌더링 됨","상태값에 따라 특정 컴포넌트만 리렌더링 됨","전체 애플리케이션을 다시 빌드함"]','{"correct":2}','EVALUATE',0.9,'["context-api","state-sharing"]'),
('FRONTEND_REACT','MCQ','useEffect 훅의 의존성 배열에 상태가 포함되지 않은 경우 어떤 일이 발생할까요?','["상태 변화에도 불구하고 컴포넌트는 무한 재렌더링을 수행하지 않음","컴포넌트는 상태 변경 이벤트를 감지하지 못하고 업데이트되지 않음","의존성 배열에 상태가 포함되어야 하므로 컴포넌트가 오류 발생","상태 변화에도 불구하고 useEffect 내부에서만 상태 업데이트가 적용됩니다"]','{"correct":1}','ANALYZE',0.9,'["hooks-effect-dependency-array"]'),
('FRONTEND_REACT','MCQ','React에서 useState를 사용할 때 초기 상태값을 변경하려면 어떻게 해야 하나요?','["useState에 새로운 값으로 설정","useEffect 내부에서 setState 호출","initialState 매개변수로 전달","props 통해 전달"]','{"correct":0}','UNDERSTAND',0.9,'["state-management-use-state"]'),
('FRONTEND_REACT','MCQ','useMemo 훅을 사용하여 메모이제이션을 적용할 때, 어떤 상황에서 이를 효과적으로 활용할 수 있나요?','["렌더링 성능 최적화를 위한 컴포넌트 로직 재사용","컴포넌트의 모든 상태 값 업데이트 시마다 리렌더링 방지","이벤트 핸들러 함수를 메모이제이션","리액트 인스턴스 상태 변경 감지"]','{"correct":0}','APPLY',0.9,'["hooks-usememo"]'),
('FRONTEND_REACT','MCQ','React 컴포넌트에서 props를 받는 방식은 무엇인가?','["props를 직접 전달","state를 사용하여 전달","context API를 사용","redux 스토어를 사용"]','{"correct":0}','REMEMBER',0.9,'["component-props"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서는 useEffect 훅이 배열에 의존성을 포함하지 않은 경우, 엘리먼트의 마운팅과 언마운팅 시점에서 무엇이 발생하나?

useEffect(() => {
  console.log(''mounting'');
}, []);

// 컴포넌트 내용','["마운팅 시에만 로그 출력","언마운팅 시에만 로그 출력","렌더링할 때마다 로그 출력","마운팅과 언마운팅 모두에서 로그 출력"]','{"correct":0}','ANALYZE',0.2,'["useeffect-mount-unmount"]'),
('FRONTEND_REACT','CODE_READING','다음 코드의 동작으로 옳은 것은 무엇인가요?

import React, { useState } from ''react'';
function Counter() {
  const [count, setCount] = useState(0);
  function handleClick() {
    console.log(''Button clicked!'');
    setTimeout(() => {
      console.log(count); // 1
      setCount(count + 1);
    }, 2000);
  }
  return <button onClick={handleClick}>Increment</button>;
}
','["두 개의 콘솔 로그가 모두 0으로 출력된다.","두 개의 콘솔 로그가 모두 최신 상태인 count로 출력된다.","setTimeout 내부에서 첫 번째 콘솔 로그는 이전 상태, 두 번째 콘솔 로그는 업데이트된 상태를 출력한다.","버튼 클릭 시 컴포넌트가 재렌더링되지 않는다."]','{"correct":2}','REMEMBER',0.2,'["react-state-management"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useReducer와 useSelector을 사용하여 Redux 상태를 관리하는 방식은 무엇인가?

import React, { useEffect } from ''react'';
import { useDispatch, useSelector } from ''react-redux'';
import { incrementCount } from ''./counterActions'';
const Counter = () => {
  const dispatch = useDispatch();
  const count = useSelector(state => state.counter.count);

  useEffect(() => {
    const intervalId = setInterval(() => {
      dispatch(incrementCount());
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);

  return <div>{count}</div>;
};','["useSelector와 useReducer를 동시에 사용하면 Redux 상태 관리가 가능하다.","dispatch를 통해 액션을 보내는 것이 중요하며, useSelector로 상태를 가져온다.","useDispatch 없이도 상태 변경이 가능하다.","React 컴포넌트에서 Redux 상태 관리는 불필요하다."]','{"correct":1}','EVALUATE',0.2,'["redux-state-management"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useReducer 훅과 함께 reducer 함수를 사용할 때, 액션 타입이 변경되었을 경우 어떻게 처리되나?

const [state, dispatch] = useReducer(reducer, initialState);
dispatch({type: ''NEW_ACTION''});','["기존의 상태 업데이트 로직으로 처리됨","새로운 액션 타입에 대한 처리 로직이 실행됨","예외를 발생시키며 실행 중단","렌더링 단계에서 에러 발생"]','{"correct":1}','ANALYZE',0.4,'["usereducer-new-action-type"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 React.memo와 shouldComponentUpdate 메소드를 사용한 이유는 무엇인가?

import React, { Component } from ''react'';
const Example = (props) => {
  return <Child {...props} />;
};
class Child extends Component {
  render() {
    console.log(''Rendering Child component'');
    return <div>{this.props.name}</div>;
  }
}
export default React.memo(Example);

// 또는 
class CustomComponent extends Component {
  shouldComponentUpdate(nextProps) {
    return nextProps.count !== this.props.count;
  }
  render() {
    console.log(''Rendering Custom component'');
    return <div>{this.props.count}</div>;
  }
}','["React.memo와 shouldComponentUpdate는 필요하지 않다.","React.memo를 사용하면 렌더링 성능을 향상시킬 수 있다.","shouldComponentUpdate 메소드는 항상 사용해야 한다.","React.memo와 shouldComponentUpdate 모두 성능 최적화에 효과적이지만, React.memo가 더 추천된다."]','{"correct":3}','EVALUATE',0.4,'["rendering-performance"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useEffect가 무한 루프를 생성하는 이유는 무엇인가?

import React, { useState, useEffect } from ''react'';
const Counter = () => {
  const [count, setCount] = useState(0);
  useEffect(() => {
    console.log(`Count is: ${count}`);
    const intervalId = setInterval(() => {
      setCount(prevCount => prevCount + 1);
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);

  return <div>{count}</div>;
};','["useEffect 의 의존성 배열에 count를 추가해야 한다.","setInterval 대신 setTimeout을 사용하면 무한 루프가 해결된다.","무한 루프는 생성되지 않는다. setCount의 호출은 즉시 리렌더를 일으키지 않기 때문에 useEffect가 무한 재호출되는 문제가 없다.","useEffect 의 의존성 배열에서 window 객체를 추가해야 한다."]','{"correct":0}','EVALUATE',0.4,'["hooks-useeffect"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 문제가 무엇인가?

import React from ''react'';
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  handleClick() {
    this.setState({ count: this.state.count + 1 }); // 문제점이 있는 코드
  }
  render() {
    return (
      <div>
        <p>카운트: {this.state.count}</p>
        <button onClick={this.handleClick}>증가</button>
      </div>
    );
  }
}
export default App;','["클래스 컴포넌트에서 setState를 사용할 때 this.setState가 아닌 다른 메서드로 상태 업데이트를 시도함.","setState 내부에서 this.state.count를 직접 참조하여 stale closure 문제가 발생합니다.","클릭 이벤트 핸들러 handleClick이 정의되지 않았습니다.","클래스 컴포넌트는 함수형 컴포넌트보다 성능이 떨어집니다."]','{"correct":1}','REMEMBER',0.4,'["state-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드는 상태를 업데이트하는 useEffect 콜백에서 의존성 배열을 잘못 설정한 경우입니다.

import React, { useState, useEffect } from ''react'';
const App = () => {
  const [count, setCount] = useState(0);
  useEffect(() => {
    const intervalId = setInterval(() => {
      setCount(count + 1);
    }, 1000);
    return () => clearInterval(intervalId);
  });
  return <div>{count}</div>;
};','["count가 무한히 증가하지만, 컴포넌트 언마운트 시 정상적으로 인터벌을 제거합니다.","count가 1씩 증가하지만, 의존성 배열에 count가 없기 때문에 stale closure 문제를 일으킵니다.","count가 정확하게 1씩 증가하며 의존성 배열은 정상적으로 작동합니다.","count가 초기화되지 않고 무한히 증가하는 버그가 발생합니다."]','{"correct":1}','APPLY',0.4,'["useeffect-dependency-array"]'),
('FRONTEND_REACT','CODE_READING','다음 코드가 실행될 때 발생하는 문제점을 설명하세요.

import React, { useState } from ''react'';
class MyComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  render() {
    return (
      <button onClick={() => this.setState({ count: this.state.count + 1 })}>Click me</button>
    );
  }
}
','["클래스 컴포넌트에서 상태 업데이트 메서드를 직접 사용할 수 없다.","this.setState를 호출하면 클래스 내부의 다른 함수에서도 이 상태값을 참조해야 한다.","이 코드는 정상적으로 동작한다.","클릭 시 클래스 인스턴스가 새롭게 생성된다."]','{"correct":0}','REMEMBER',0.6,'["react-state-management"]'),
('FRONTEND_REACT','CODE_READING','다음 코드는 useState 훅을 사용하여 상태를 업데이트하는 함수형 컴포넌트입니다.

import React, { useState } from ''react'';
const Counter = () => {
  const [count, setCount] = useState(0);
  const increment = (step) => {
    setCount(count + step);
  }
  return <button onClick={() => increment(1)}>Increment</button>;
};','["버튼 클릭 시 count 상태가 1씩 증가합니다.","버튼 클릭 시 count 상태가 계속해서 무한히 증가합니다.","버튼 클릭 시 아무런 동작이 일어나지 않습니다.","버튼 클릭 시 컴포넌트가 언마운트됩니다."]','{"correct":2}','APPLY',0.6,'["usestate-batch-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드의 문제점을 설명하세요.

import React from ''react'';
class Example extends React.Component {
  constructor(props) {
    super(props);
    this.state = { value: '''' };
  }
  handleChange(event) {
    this.setState({ value: event.target.value });
  }
  render() {
    return (
      <input type=''text'' value={this.state.value} onChange={this.handleChange.bind(this)} />
    );
  }
}
','["이 코드는 정상적으로 동작한다.","handleChange 메서드에서 this.setState를 호출하면 무한 루프가 발생할 수 있다.","클래스 컴포넌트에서는 value 속성을 사용하지 않고, onChange 이벤트 핸들러만 사용해야 한다.","인풋 필드의 값이 변경될 때마다 컴포넌트가 재렌더링되지 않는다."]','{"correct":1}','REMEMBER',0.6,'["react-state-management"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useEffect 훅이 몇 번 실행되는가?

const [count, setCount] = useState(0);
useEffect(() => {
  console.log(''effect'', count);
}, []);','["무한 루프로 계속 실행","결과에 영향을 주지 않는 상태 변경에도 실행","componentDidMount와 동일하게 한 번만 실행","렌더링할 때마다 실행"]','{"correct":2}','ANALYZE',0.6,'["useeffect-dependency-array"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `useCallback` 훅이 어떻게 작동하는지를 설명해 주세요.

function App() {
  const [count, setCount] = useState(0);
  const increment = useCallback(() => setCount(prev => prev + 1), []);

  return (
    <button onClick={increment}>Increment</button>
  );
}','["useCallback은 매 렌더링마다 새로운 함수를 생성한다.","useCallback은 이전 렌더링과 동일한 함수 객체를 반환한다.","useCallback는 상태가 변경될 때마다 새로운 함수를 생성하고 반환한다.","함수의 랜덤성 때문에 useCallback이 항상 새로운 함수를 반환한다."]','{"correct":1}','UNDERSTAND',0.6,'["hooks-usecallback"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 배열의 첫 번째 아이템이 렌더링될 때마다 새로 생성되는 엘리먼트가 있나?

const [items, setItems] = useState([{id: 1}]);
return (
  <div key={items[0].id}>{items[0]}</div>
);','["렌더링할 때마다 새로운 엘리먼트 생성","렌더링할 때마다 기존 엘리먼트 재사용","항상 동일한 엘리먼트를 참조","변경된 props로 인해 업데이트"]','{"correct":1}','ANALYZE',0.6,'["key-referential-integrity"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `React.memo`를 사용했지만, 컴포넌트가 계속해서 렌더링되는 이유는 무엇인가요?

const MyComponent = React.memo(({ count }) => {
  console.log(''MyComponent rendered'');
  return <div>{count}</div>;
});

function App() {
  const [count, setCount] = useState(0);
  useEffect(() => {
    setInterval(() => setCount(prev => prev + 1), 1000);
  }, []);

  return <MyComponent count={count} />;
}','["React.memo는 props 변경에 따라 항상 컴포넌트를 렌더링한다.","React.memo는 이벤트 핸들러의 변경사항을 감지하지 못한다.","props 값이 바뀌면 React.memo가 무시되고 컴포넌트가 계속 렌더링된다.","setInterval로 props 값이 자주 변경되므로 React.memo가 동작하지 않는다."]','{"correct":3}','UNDERSTAND',0.6,'["react-memo"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useEffect 내부에서 배열을 변경하면 어떤 일이 벌어질까요?

const [items, setItems] = useState([]);

useEffect(() => {
  items.push(''item1'');
}, []);','["배열에 ''item1''이 정상적으로 추가됩니다.","배열은 변하지 않고 아무 일도 발생하지 않습니다.","배열은 변하지만 컴포넌트는 리렌더되지 않습니다.","컴포넌트가 무한 루프로 리렌더됩니다."]','{"correct":3}','UNDERSTAND',0.6,'["hooks-effect"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `React.memo`를 사용했지만, 컴포넌트가 계속해서 렌더링되는 이유는 무엇인가요?

const MemoComponent = React.memo(function Component({ count }) {
  console.log(''MemoComponent rendered'');
  return <div>{count}</div>;
});

function App() {
  const [count, setCount] = useState(0);
  useEffect(() => {
    setInterval(() => setCount(prev => prev + 1), 1000);
  }, []);

  return <MemoComponent count={count} />;
}','["MemoComponent는 클래스형 컴포넌트라서 React.memo 최적화 대상이 아니다.","console.log 호출 때문에 리렌더링 여부와 무관하게 항상 화면이 다시 그려진다.","빈 의존성 배열 때문에 setInterval이 렌더링마다 중복으로 등록되어 성능이 저하된다.","setInterval로 count prop 값이 계속 바뀌기 때문에 React.memo가 변경을 감지하고 다시 렌더링한다."]','{"correct":3}','UNDERSTAND',0.6,'["react-memo"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useCallback 훅이 사용된 이유는 무엇인가?

import React, { useState, useCallback } from ''react'';
const Example = () => {
  const [count, setCount] = useState(0);
  const incrementCount = useCallback(() => {
    setCount(prevCount => prevCount + 1);
  }, []);

  return <button onClick={incrementCount}>Increment</button>;
};','["useCallback은 사용하지 않아도 된다.","incrementCount 함수의 렌더링 성능을 개선하기 위해 useCallback이 사용되었다.","useCallback 훅은 항상 필요하다.","count 상태를 바꾸는 메서드를 만들 때, useReducer가 더 적합하다."]','{"correct":1}','EVALUATE',0.6,'["hooks-usecallback"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `React.memo`와 `PureComponent`의 차이점은 무엇인가요?

import React, { PureComponent } from ''react'';

const MemoizedComponent = React.memo(function Component({ count }) {
  console.log(''MemoizedComponent rendered'');
  return <div>{count}</div>;
});

class PureComponent extends PureComponent {
  render() {
    console.log(''PureComponent rendered'');
    return <div>{this.props.count}</div>;
  }
}','["React.memo는 클래스 컴포넌트에만 적용할 수 있다.","PureComponent는 props가 변경되지 않아도 항상 렌더링된다.","React.memo는 props의 참조 동일성을 사용하여 렌더링을 제어하고, PureComponent는 업데이트된 상태를 고려한다.","React.memo와 PureComponent는 모두 props의 참조 동일성만으로 렌더링을 제어한다."]','{"correct":2}','UNDERSTAND',0.6,'["react-memo","purecomponent"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useLayoutEffect 훅이 useEffect와 어떤 방식으로 동작하는가?

useLayoutEffect(() => {
  console.log(''layout effect'');
}, []);

// 컴포넌트 내용','["렌더링 후 즉시 실행","렌더링 전에 실행되며 블록킹 가능","렌더링 중 단계에서 무시됨","렌더링 시점과 독립적으로 실행"]','{"correct":1}','ANALYZE',0.8,'["uselayouteffect-rendering-order"]'),
('FRONTEND_REACT','CODE_READING','다음 코드가 실행될 때 어떤 문제점이 발생할까요?

import React from ''react'';
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  handleClick() {
    setTimeout(() => {
      const latestCount = this.state.count; // 문제점이 있는 코드
      this.setState({ count: latestCount + 1 });
    }, 500); 
  }
  render() {
    return (
      <div>
        <p>카운트: {this.state.count}</p>
        <button onClick={this.handleClick}>증가</button>
      </div>
    );
  }
}
export default Counter;','["setTimeout 내부에서 setState를 사용하는 것은 문제 없습니다.","setState 메서드는 setTimeout 내부에서는 동작하지 않습니다.","setTimeout 내부에서 this.state.count를 참조하면 stale closure 문제가 발생합니다.","클래스 컴포넌트에서는 setTimeout을 사용할 수 없습니다."]','{"correct":2}','REMEMBER',0.8,'["state-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드는 useEffect 훅을 이용해 API 호출 및 데이터 로딩을 처리하는 컴포넌트입니다.

import React, { useState, useEffect } from ''react'';
const DataFetcher = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    fetch(''https://api.example.com/data'')
      .then(res => res.json())
      .then(data => {
        setData(data);
        setLoading(false);
      });
  }, []);
  if (loading) return <div>Loading...</div>;
  return (
    <pre>{JSON.stringify(data, null, 2)}</pre>
  );
};','["데이터가 로드될 때까지 ''Loading...'' 메시지가 표시되며, 데이터 로드 후에는 JSON 문자열이 표시됩니다.","컴포넌트가 로딩 중일 때도 JSON 문자열을 출력합니다.","API 호출에 실패하면 컴포넌트가 비정상적으로 작동합니다.","데이터 로딩 과정에서 API 호출이 여러 번 발생합니다."]','{"correct":0}','APPLY',0.8,'["useeffect-fetch-api"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 React.memo와 key props를 사용한 이유는 무엇인가?

import React, { useState } from ''react'';
const Example = () => {
  const [items, setItems] = useState([{ id: 1 }, { id: 2 }]);
  return (
    <ul>
      {items.map(item => (
        <li key={item.id}>{item.name}</li>
      ))}
    </ul>
  );
};','["key props는 필요하지 않다.","React.memo를 사용하면 렌더링 성능이 향상된다.","리스트의 각 아이템에 고유한 key prop을 지정해야 한다.","React.memo와 key props 모두 성능 최적화에 효과적이지만, React.memo가 더 중요하다."]','{"correct":2}','EVALUATE',0.8,'["rendering-performance"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useLayoutEffect 훅이 사용된 이유는 무엇인가?

import React, { useLayoutEffect } from ''react'';
const Example = () => {
  useLayoutEffect(() => {
    console.log(''useLayoutEffect called'');
    document.body.style.backgroundColor = ''#fff'';
  }, []);

  return <div>Example</div>;
};','["useLayoutEffect는 필요하지 않다.","렌더링 후 DOM 업데이트 전에 작업을 수행하기 위해 useLayoutEffect가 사용되었다.","렌더링 중인 컴포넌트의 상태를 변경해야 할 때, useLayoutEffect가 사용된다.","useLayoutEffect와 useEffect는 같은 역할을 한다."]','{"correct":1}','EVALUATE',0.8,'["hooks-uselayouteffect"]'),
('FRONTEND_REACT','CODE_READING','다음 코드가 실행될 때 어떤 문제가 발생할까요?

import React, { useState } from ''react'';
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  handleClick() {
    setTimeout(() => {
      const latestCount = this.state.count; // 문제점이 있는 코드
      console.log(latestCount); // 최신 상태 값을 확인하는 로그
      this.setState({ count: latestCount + 1 });
    }, 500); 
  }
  render() {
    return (
      <div>
        <p>카운트: {this.state.count}</p>
        <button onClick={this.handleClick}>증가</button>
      </div>
    );
  }
}
export default Counter;','["setTimeout 내부에서 this.setState를 사용하는 것은 문제가 없습니다.","setState 메서드는 setTimeout 내부에서는 동작하지 않습니다.","setTimeout 내부에서 this.state.count를 참조하면 stale closure 문제가 발생합니다.","클래스 컴포넌트에서는 setTimeout을 사용할 수 없습니다."]','{"correct":2}','REMEMBER',0.8,'["state-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드가 실행될 때 발생하는 문제점은 무엇인가?

import React, { useState } from ''react'';
const Counter = () => {
  const [count, setCount] = useState(0);
  const incrementAsync = async () => {
    setTimeout(() => {
      setCount(count + 1); // 문제점이 있는 코드
    }, 1000);
  };
  return (
    <div>
      <p>카운트: {count}</p>
      <button onClick={incrementAsync}>증가</button>
    </div>
  );
};','["setTimeout 내부에서 count를 직접 사용하여 상태 업데이트가 잘못됨.","setTimeout 외부에서 setCount 함수를 호출하지 않아 카운터가 증가하지 않음.","count 값이 정의되지 않았기 때문에 컴포넌트가 오류로 렌더링 됨.","setTimeout 내부에서 React 엔진이 콜백을 인식하지 못해 동작하지 않음."]','{"correct":0}','REMEMBER',0.8,'["state-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 useReducer 훅이 사용된 경우, 액션 타입을 변경하면 어떻게 되나?

const [state, dispatch] = useReducer(reducer, initialState);
dispatch({type: ''INCREMENT''});','["액션 타입에 따라 상태가 업데이트됨","기존의 액션 타입이 무시됨","예외를 발생시키며 실행 중단","렌더링 단계에서 에러 발생"]','{"correct":0}','ANALYZE',0.9,'["usereducer-action-type"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `useEffect`가 호출될 때마다 새로운 이벤트 리스너가 생성되고 기존 리스너는 제거되지 않습니다. 왜 이러한 문제가 발생하는지 설명해 주세요.

function Example() {
  useEffect(() => {
    const handleResize = () => console.log(''window resized'');
    window.addEventListener(''resize'', handleResize);
  });

  return <div>Example Component</div>
}','["이벤트 리스너가 여러번 추가되어 메모리 누수를 일으킨다.","이벤트 리스너가 제거되지 않아서 이전 렌더링의 상태가 유지된다.","useEffect 내부에서 사용된 이벤트 핸들러 함수가 항상 새로운 주소로 생성되기 때문에 기존 리스너가 무시된다.","이 코드는 문제가 없고, 정상적으로 동작한다."]','{"correct":2}','UNDERSTAND',0.9,'["hooks-useeffect"]'),
('FRONTEND_REACT','CODE_READING','다음 코드의 문제점은 무엇인가?

import React from ''react'';
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  handleClick() {
    setTimeout(() => {
      this.setState({ count: this.state.count + 1 }); // 문제점이 있는 코드
    }, 500); 
  }
  render() {
    return (
      <div>
        <p>카운트: {this.state.count}</p>
        <button onClick={this.handleClick}>증가</button>
      </div>
    );
  }
}
export default Counter;','["setTimeout 내부에서 this.setState를 사용하여 상태 업데이트 시 stale closure 문제가 발생합니다.","클래스 컴포넌트에서는 setTimeout을 사용할 수 없습니다.","setState 메서드가 정의되지 않았습니다.","카운터 값이 증가하지 않습니다."]','{"correct":0}','REMEMBER',0.9,'["state-update"]'),
('FRONTEND_REACT','CODE_READING','다음 코드에서 `useContext` 훅이 어떻게 작동하는지를 설명해 주세요.

const ThemeContext = React.createContext(''light'');

function App() {
  const [theme, setTheme] = useState(''dark'');
  return (
    <ThemeContext.Provider value={theme}>
      <Child />
    </ThemeContext.Provider>
  );
}

function Child() {
  const theme = useContext(ThemeContext);
  useEffect(() => {
    console.log(`Current Theme: ${theme}`);
  }, [theme]);
  return null;
}','["useContext는 컴포넌트 트리에서 가장 가까운 컨텍스트 값을 가져온다.","useContext 훅은 직접 제공된 value값을 무시하고, 최상위 Provider의 값만 사용한다.","useEffect 내부에서 useContext 훅이 호출되면, 이 컴포넌트는 렌더링되지 않는다.","ThemeContext.Provider가 없는 상황에서도 useContext가 정상적으로 작동한다."]','{"correct":0}','UNDERSTAND',0.9,'["hooks-usecontext"]'),
('MOBILE_FLUTTER','MCQ','InheritedWidget을 사용하는 이유는 무엇인가?','["상태를 변경하는 위젯을 제공한다.","다른 위젯에게 값을 공유하고 전달하기 위해 사용된다.","프로바이더 패턴을 구현하는데 필요한 클래스이다.","위젯 트리에 존재하지 않는 위젯을 만드는 데 사용된다."]','{"correct":1}','REMEMBER',0.2,'["inherited-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 Column 위젯은 자식 위젯들의 크기를 어떻게 결정하는가?','["자식 위젯의 최대 크기로 설정","제약 조건(constraints)에 따라 결정","하위 위젯 사이즈 합으로 결정","화면 전체 크기에 맞춤"]','{"correct":1}','REMEMBER',0.2,'["layout-constraint"]'),
('MOBILE_FLUTTER','MCQ','다음 중 Flutter 위젯 트리에서 제약(constraints)이 어떻게 전파되는 방식을 올바르게 설명한 것은?','["제약은 위로, 크기는 아래로 전파된다.","크기는 위로, 제약은 아래로 전파된다.","제약과 크기 모두 위로 전파된다.","제약과 크기 모두 아래로 전파된다."]','{"correct":0}','ANALYZE',0.2,'["layout-constraints"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 플랫폼별 차이점을 처리하기 위해 PlatformChannel을 사용할 때, Android와 iOS에서 서로 다른 메서드를 호출하려면 어떤 작업을 해야 할까요?','["Android와 iOS의 각각 메서드 이름만 다르게 지정하면 됩니다.","메소드 명세를 플랫폼별로 구분하여 정의해야 합니다.","Platform.isAndroid 와 Platform.isIOS를 사용해 조건부로 메소드 호출하도록 해야 합니다.","플랫폼 별로 분기 없이 동일한 메서드를 호출하면 됩니다."]','{"correct":2}','UNDERSTAND',0.2,'["platform-channel-platform-specific-methods"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 Navigator.push() 메서드는 무엇을 수행하나?','["기존 화면을 제거하고 새로운 화면으로 이동한다.","새로운 화면을 스택에 추가하며 현재 화면은 보인다.","화면을 왼쪽으로 밀어내고 새로운 화면이 나타난다.","현재 화면과 함께 새로운 화면을 동시에 표시한다."]','{"correct":1}','REMEMBER',0.2,'["navigator-push"]'),
('MOBILE_FLUTTER','MCQ','Dart에서 Future를 사용하여 비동기 작업을 처리할 때, 다음 중 올바른 사용법은?','["await 키워드 없이 Future 반환 함수 호출하기","Future 객체가 반환되는 함수 안에서 await 키워드를 사용하여 다른 Future 기다리기","비동기 함수에서 return 키워드만으로 값을 반환하기","Future.delayed로 지연된 작업을 실행하지 않고 바로 반환"]','{"correct":1}','UNDERSTAND',0.4,'["future-async-await"]'),
('MOBILE_FLUTTER','MCQ','다음 코드 조각에서, `Provider.of<T>()` 호출이 발생할 때마다 위젯 트리가 어떤 방식으로 변경되는지 설명하십시오.

```
class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(''Provider Example'')),
      body: Center(child: ValueCounter()),
    );
  }
}

class ValueCounter extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final int value = Provider.of<int>(context); // Provider 사용
    return Text(''Value: $value'');
  }
}
```','["BuildContext 변경 시 rebuild","Provider 상태가 변경될 때 rebuild","위젯 트리 전체 rebuild","특정 위젯만 rebuild"]','{"correct":3}','ANALYZE',0.4,'["flutter-provider","state-management"]'),
('MOBILE_FLUTTER','MCQ','다음 중 StatefulWidget에서 build 메서드는 언제 호출되지 않는가?','["setState() 호출시","dispose() 호출시","initState() 호출시","didUpdateWidget() 호출시"]','{"correct":1}','UNDERSTAND',0.4,'["stateful-widget-lifecycle"]'),
('MOBILE_FLUTTER','MCQ','다음 중 Flutter 앱 성능 최적화에 사용할 수 없는 전략은?','["불필요한 build 호출 제거","ListView.builder를 사용하여 지연 로딩","RepaintBoundary 위젯을 과도하게 사용","위젯 테스트를 통한 이슈 식별"]','{"correct":2}','UNDERSTAND',0.4,'["performance-optimization-concepts"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 비동기 처리와 관련된 다음 설명 중 가장 올바른 것은 무엇인가요?','["FutureBuilder 위젯은 Future 객체를 반환하는 함수를 받아들입니다.","StreamBuilder 위젯은 Stream 객체를 반환하지 않습니다.","async/await 키워드는 동기적인 코드로 실행됩니다.","dispose 메서드는 StatefulWidget에서 항상 호출되지 않습니다."]','{"correct":0}','EVALUATE',0.4,'["async-dispose"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 InheritedWidget의 주요 역할은 무엇인가?','["상태 관리를 위한 컨테이너","위젯 트리의 레벨을 제한","UI 업데이트를 최적화한다.","네이티브 코드와 통신한다."]','{"correct":0}','REMEMBER',0.4,'["inheritedwidget"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 InheritedWidget을 사용하여 위젯 트리 내의 데이터 공유가 어떻게 이루어지는지 설명하십시오.','["InheritedWidget은 위젯 트리를 통해 동일한 인스턴스를 전파하며, 자식 위젯들은 필요시 해당 인스턴스를 참조한다.","InheritedWidget은 각각 다른 인스턴스로 재사용 가능한 상태를 제공하고, 위젯 트리에서 별도의 데이터 공유를 지원하지 않는다.","InheritedWidget은 모든 위젯이 동일한 상태를 유지하도록 강제하며, 이를 통해 효율적인 데이터 공유가 이루어진다.","InheritedWidget는 위젯 트리를 통한 직접 데이터 전달을 방지하고, 대신 인스턴스 변수만을 사용하여 상태를 공유한다."]','{"correct":0}','ANALYZE',0.4,'["state-management-inheritedwidget"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 StatefulWidget의 생명주기 메서드 중 initState와 dispose가 각각 어떤 역할을 하는가?','["initState는 위젯이 생성될 때 호출되고, dispose는 위젯이 더 이상 사용되지 않을 때 호출된다.","initState는 위젯이 화면에 보일 때 호출되며, dispose는 위젯이 더 이상 보여지지 않을 때 호출된다.","initState는 위젯의 상태를 초기화하고, dispose는 리소스를 해제한다.","initState와 dispose 모두 위젯이 생성될 때 호출되며, 상태 관리를 위한 초기 설정을 수행한다."]','{"correct":0}','ANALYZE',0.4,'["lifecycle-init-dispose"]'),
('MOBILE_FLUTTER','MCQ','initState() 메서드의 주요 역할은 무엇인가?','["UI를 최초로 렌더링할 때 호출된다.","화면이 종료될 때 상태를 초기화한다.","build 메서드 실행 전에 필요한 초기 작업을 수행한다.","상태 변경 시 rebuild를 트리거한다."]','{"correct":2}','APPLY',0.4,'["lifecycle-methods"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 Row와 Column 위젯을 사용하여 레이아웃을 정의하려고 할 때 주요 차이점은 무엇인가?','["Row는 수평 방향으로 아이템을 배치하고, Column은 수직 방향으로 아이템을 배치한다.","Row와 Column 모두 수직 방향으로 아이템을 배치한다.","Row와 Column 모두 수평 방향으로 아이템을 배치한다.","Row는 수직 방향으로 아이템을 배치하고, Column은 수평 방향으로 아이템을 배치한다."]','{"correct":0}','UNDERSTAND',0.4,'["row-widget","column-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 비동기 처리를 위해 사용되는 주요 키워드들은 무엇인가?','["async, await, FutureBuilder","yield, nextTick, setState","callback, debounce, throttle","Stream, Queue, isSync"]','{"correct":0}','APPLY',0.4,'["asynchronous-programming"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 Row 위젯을 사용하여 가로 정렬된 컨텐츠를 만들 때, 자식 위젯들이 고유한 너비와 높이를 갖는 경우 어떤 속성을 설정해야 할까요?','["children 속성에 Expanded 객체 추가하기","mainAxisAlignment 속성에 MainAxisAlignment.spaceAround 설정하기","crossAxisAlignment 속성에 CrossAxisAlignment.start 설정하기","flex 속성 값을 변경하여 자식 위젯 사이 간격 조절"]','{"correct":0}','UNDERSTAND',0.4,'["row-widget-layout"]'),
('MOBILE_FLUTTER','MCQ','다음 코드가 위젯 트리의 어떤 부분에 영향을 미치게 될까요?

void _incrementCounter() {
  setState(() {
    _counter++;
  });
}','["_incrementCounter를 호출하는 위젯의 build 메소드만 재호출됨.","_incrementCounter가 정의된 모든 위젯들의 build 메소드가 재호출됨.","상태가 변경되는 특정 위젯과 그 자식 위젯들만이 rebuild 됨.","모든 StatefulWidget 위젯들이 재생성됩니다."]','{"correct":2}','ANALYZE',0.4,'["state-management"]'),
('MOBILE_FLUTTER','MCQ','플랫폼 통합에서 Flutter 앱이 안드로이드의 권한을 체크하는 방법은?','["Flutter 앱 내부에서 직접 권한을 요청한다.","AndroidManifest.xml 파일에서 권한을 설정하고, 플러그인을 사용해 확인한다.","iOS와 안드로이드 모두 Flutter 코드에서 권한을 동일하게 처리한다.","플랫폼별 특수 위젯을 사용하여 권한을 체크한다."]','{"correct":1}','REMEMBER',0.4,'["platform-integration"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Navigator 클래스를 이용해 화면을 전환할 때 주요 특징은 무엇인가?','["Navigator는 새로운 페이지로 이동할 때 백스택에 추가하지 않는다.","Navigator.push 메서드는 새 화면으로 바로 이동한다.","Navigator.pop 메서드는 현재 화면을 유지한 채로 이전 화면으로 돌아간다.","Navigator 클래스를 사용하면 화면 전환 시 애니메이션을 적용할 수 없다."]','{"correct":1}','UNDERSTAND',0.4,'["navigator-class"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 StatelessWidget과 StatefulWidget을 사용할 때, 각각 어떤 상황에 적합한가?','["상태를 변경해야 하는 경우 StatelessWidget을 사용한다.","상태를 변경하지 않는 경우 StatefulWidget을 사용한다.","상태를 변경해야 하는 경우 StatefulWidget을 사용한다.","상태를 변경하지 않는 경우 StatelessWidget을 사용한다."]','{"correct":3}','APPLY',0.4,'["stateless-widget-vs-stateful-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 FutureBuilder 위젯을 사용할 때, 어떤 상황에서 이를 적용해야 하는가?','["비동기 작업의 결과를 기반으로 UI를 동적으로 변경해야 할 때 FutureBuilder를 사용한다.","FutureBuilder는 네이티브 코드와 직접 통신하는 데 사용된다.","모든 Flutter 앱은 항상 FutureBuilder를 기본으로 사용해야 한다.","FutureBuilder는 데이터 상태 관리를 돕는다."]','{"correct":0}','ANALYZE',0.4,'["future-builder"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Stack과 Positioned 위젯을 사용하여 화면에 아이콘을 표시하려고 한다. 아이콘이 항상 화면의 중앙에 위치하도록 설정해야 하는데, 어떻게 해야 할까?','["Stack 위젯 내부에 Positioned 위젯을 사용하고, top과 left 속성을 0으로 설정한다.","Stack 위젯 내부에 Positioned 위젯을 사용하고, top와 left 속성을 화면 크기의 절반으로 설정한다.","Row 위젯과 Column 위젯을 이용하여 아이콘을 중앙 위치로 배치한다.","Expanded 위젯을 사용하여 아이콘을 화면 중앙에 위치시킨다."]','{"correct":1}','APPLY',0.6,'["stack-positioned"]'),
('MOBILE_FLUTTER','MCQ','ListView.builder를 사용하여 성능 최적화를 어떻게 수행할 수 있나?','["항목의 개수를 미리 계산한다.","건너뛰기(index)로 특정 항목만 빌드한다.","항목을 모두 미리 생성해 놓는다.","UI가 필요할 때마다 즉시 생성한다."]','{"correct":1}','APPLY',0.6,'["performance-optimization"]'),
('MOBILE_FLUTTER','MCQ','Dart에서 const 키워드가 사용되는 경우, Flutter 앱에서는 어떤 이점이 있는가?','["동적 타입 검사 가능","빌드 시간 최적화","메모리 사용량 증가","상수 값 변경 가능"]','{"correct":1}','REMEMBER',0.6,'["const-widget-optimization"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 FutureBuilder를 이용하여 비동기 작업의 결과를 보여주려고 한다. 아래 코드 중 어떤 옵션이 가장 적절한가?','["FutureBuilder는 기본적으로 null 값이 반환될 때까지 기다린다.","FutureBuilder는 주어진 future에 따라 위젯 트리를 동적으로 변환한다.","FutureBuilder는 비동기 작업의 결과를 즉시 화면에 표시한다.","FutureBuilder는 Future가 완료되기 전까지 아무런 처리도 하지 않는다."]','{"correct":1}','APPLY',0.6,'["future-builder"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 상태 관리를 위한 InheritedWidget을 사용할 때, 자식 위젯이 부모의 변경된 상태를 알 수 있는 가장 효과적인 방법은?','["자식 위젯에서 직접 부모 상태 값을 읽기","InheritedWidget 내부에 didUpdateWidget 메서드 구현하기","BuildContext를 통해 InheritedModel.of()로 상태 값 가져오기","setState()를 사용하여 자식 위젯의 상태 업데이트 하기"]','{"correct":2}','UNDERSTAND',0.6,'["inheritedwidget-state-management"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Navigator를 사용하여 페이지 이동을 구현하고 있다. 아래 코드 중 어떤 옵션이 가장 적절한가?','["Navigator의 push 메서드는 현재 스택에 있는 모든 위젯을 제거한다.","Navigator의 pop 메서드는 현재 활성화된 스택 위젯을 화면에서 제거한다.","Navigator의 push 메서드는 새로운 페이지를 스택에 추가하고, 이전 페이지로 돌아가는 버튼을 제공한다.","Navigator의 pop 메서드는 가장 상위 레벨 페이지로 즉시 이동한다."]','{"correct":2}','APPLY',0.6,'["navigator-push-pop"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 성능 최적화와 관련된 다음 설명 중 가장 적절한 것은 무엇인가요?','["ListView.builder는 모든 아이템을 미리 빌드합니다.","RepaintBoundary 위젯은 화면 갱신을 방지하지 않습니다.","불필요한 rebuild를 줄이기 위해 상태 관리를 최소화해야 합니다.","플랫폼 통합 시 Dart 코드가 항상 성능 이점을 제공합니다."]','{"correct":2}','EVALUATE',0.6,'["performance-optimization"]'),
('MOBILE_FLUTTER','MCQ','Dart 언어에서 다음과 같은 코드가 실행될 때, 어떤 타입의 객체가 생성되는가?

final list = List.generate(10, (index) => index * 2);','["List<int>","List<String>","int[]","String[]"]','{"correct":0}','ANALYZE',0.6,'["dart-lists"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Platform Channel을 이용하여 네이티브 코드와 상호작용할 때, 어떤 경우에 이를 사용해야 하는가?','["네이티브 플러그인의 기능을 사용하려면 반드시 Flutter 위젯을 사용해야 한다.","플랫폼 고유의 권한을 요청하거나 특정 API를 호출하기 위해 Platform Channel을 사용한다.","모든 Flutter 앱은 항상 Platform Channel을 이용해야 한다.","Platform Channel은 네이티브 코드에서만 작동한다."]','{"correct":1}','ANALYZE',0.6,'["platform-channel"]'),
('MOBILE_FLUTTER','MCQ','다음 Flutter 코드에서 가장 적절한 접근성 향상 방법은 무엇인가요?','["Semantics 정보를 직접 위젯에 추가합니다.","위젯의 Semantics 인터페이스를 무시하고 개발자 정의로 대체합니다.","각 위젯을 별도의 SemanticNode로 분리하여 사용합니다.","위젯 테스트에서 골든 테스트를 수행합니다."]','{"correct":0}','EVALUATE',0.6,'["semantics-accessibility"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 StatefulWidget과 StatelessWidget을 사용할 때 주의해야 할 사항은 무엇인가요?','["StatelessWidget은 상태를 변경할 수 있습니다.","StatefulWidget은 위젯 트리에서 한번만 빌드됩니다.","StatefulWidget은 상태 변화에 따라 build 메서드가 호출될 수 있습니다.","StatelessWidget은 항상 상수(const)로 선언되어야 합니다."]','{"correct":2}','EVALUATE',0.6,'["stateful-widget"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 InheritedWidget의 역할은 무엇인가?

final theme = Theme.of(context);
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: ''Flutter Demo'',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: ''Flutter Demo Home Page''),
    );
  }
}
class MyHomePage extends StatelessWidget {
  final String title;

  MyHomePage({Key? key, required this.title}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(title),
      ),
      body: Center(
        child: Text("Hello World"),
      ),
    );
  }
}
','["UI 요소의 상태를 관리한다.","하위 위젯에 데이터를 공유한다.","위젯 트리를 렌더링한다.","이벤트 핸들러를 정의한다."]','{"correct":1}','APPLY',0.6,'["inherited-widget"]'),
('MOBILE_FLUTTER','MCQ','Provider 패턴을 사용하여 상태를 관리할 때 주요 특징은 무엇인가?','["위젯 트리를 통해 데이터 공유가 이루어지며, 필요한 곳에서만 rebuild된다.","기존의 StatefulWidget을 이용해 상태를 관리한다.","모든 위젯이 데이터에 대한 직접적인 접근을 가질 수 있다.","setState 메서드를 사용하여 상태를 업데이트한다."]','{"correct":0}','UNDERSTAND',0.6,'["provider-pattern"]'),
('MOBILE_FLUTTER','MCQ','Provider 패키지를 사용하여 상태 관리를 할 때, 어떤 객체는 어떤 역할을 수행하나?','["InheritedWidget의 서브클래스를 제공한다.","상태 변경 시 build 메서드를 호출한다.","데이터 흐름을 관리하는 역할을 한다.","UI와 비즈니스 로직 사이에 통로를 만든다."]','{"correct":2}','APPLY',0.6,'["provider-state-management"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 `Positioned` 위젯은 어떤 좌표계를 기준으로 위치를 지정하는가?

```
Stack(
  children: [
    Positioned(left: 50, top: 30, child: Container(color: Colors.red)),
  ],
)
```','["Container 위젯의 크기","Stack 위젯의 크기","화면 전체의 크기","자신의 부모 위젯의 크기"]','{"correct":1}','ANALYZE',0.6,'["flutter-positioned","layout-concepts"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 레이아웃 시스템에서 제약(constraints)을 정확히 이해하는 것이 중요합니다. 다음 설명 중 잘못된 것은 무엇인가요?','["Constraints는 위젯 트리에서 아래로 전파됩니다.","Sizes는 위젯 트리에서 위로 전파됩니다.","Expanded 위젯은 항상 Constraints를 무시하고 지정한 크기를 가지려고 합니다.","Flexible 위젯은 가능한 최대 크기에 따라 사이즈가 결정됩니다."]','{"correct":2}','EVALUATE',0.6,'["layout-constraints"]'),
('MOBILE_FLUTTER','MCQ','Dart 언어에서 Future와 async/await를 사용할 때, 비동기 작업이 완료될 때까지 기다리는 방법은?','["Future 객체의 then() 메서드를 호출하여 콜백 함수를 등록한다.","async 키워드만 사용하면 자동으로 비동기 처리가 된다.","await 키워드를 사용하여 비동기 작업이 완료될 때까지 대기한다.","Future 객체는 직접 비동기 작업을 수행하지 않고, 동기적으로 실행된다."]','{"correct":2}','REMEMBER',0.6,'["async-await"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 플랫폼 통합을 수행할 때, platform channel을 사용하여 안드로이드와 iOS 간에 메시지를 주고받는 방법은 무엇인가?','["Platform-specific code를 직접 작성해 메시지를 전달한다.","MethodChannel을 이용해 메시지를 주고받는다.","EventChannel을 이용해 이벤트를 수신하고 메시지를 전송한다.","ServiceChannel을 이용해 서비스와 통신한다."]','{"correct":1}','APPLY',0.6,'["platform-channel"]'),
('MOBILE_FLUTTER','MCQ','const 위젯을 사용함으로써 Flutter에서 어떤 이점을 얻을 수 있나?','["상태 변경 시 build 메서드 재호출 가능","빌드 최적화를 통해 성능 향상","UI가 동적으로 변경되는 경우에 유용","widget 트리의 변화를 감지하는 데 사용된다."]','{"correct":1}','APPLY',0.6,'["const-widget-optimization"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 StatelessWidget과 StatefulWidget의 주요 차이점은 무엇인가?','["StatelessWidget은 상태를 가질 수 있다.","StatefulWidget은 위젯 트리에 한 번만 빌드된다.","StatefulWidget은 상태 변경 시 build 메서드를 다시 호출할 수 있다.","StatelessWidget은 위젯의 생명주기를 관리한다."]','{"correct":2}','REMEMBER',0.6,'["stateless-widget-vs-stateful-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 setState 메서드를 사용할 때 주의해야 할 사항은 무엇인가?','["setState는 비동기적으로 실행된다.","setState는 항상 위젯 트리를 전체 재구성한다.","setState는 build 메서드를 호출하지 않는다.","setState는 현재 위젯의 상태를 업데이트하는 데 사용된다."]','{"correct":3}','UNDERSTAND',0.6,'["state-management"]'),
('MOBILE_FLUTTER','MCQ','Dart에서 비동기 코드를 작성할 때, 이벤트 루프와 isolate의 개념을 이해하고 적절히 활용하는 것이 중요하다. 다음 중 이벤트 루프와 isolate에 대한 올바른 설명은 무엇인가?','["isolate는 Dart VM에서 별도의 실행 컨텍스트를 제공하며, 메인 isolate와 통신하려면 MessageChannel을 사용한다.","이벤트 루프는 비동기 작업이 완료될 때마다 이벤트를 처리하지만, 동기적으로 코드가 실행되는 동안에도 계속 작동한다.","isolate는 프로그램의 전체 생명주기를 관리하며, 메인 isolate에서 생성된 모든 isolate는 자동으로 종료된다.","이벤트 루프는 비동기 작업을 큐에 넣어 처리하지만, 이벤트가 발생하지 않으면 이벤트 루프도 중단된다."]','{"correct":0}','EVALUATE',0.8,'["flutter-async-concepts"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 BuildContext의 주요 역할은 무엇인가?','["위젯 트리의 위치 정보를 제공한다.","플랫폼 관련 기능을 호출한다.","UI 업데이트 요청을 처리한다.","네이티브 코드와 통신한다."]','{"correct":0}','REMEMBER',0.8,'["buildcontext"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 FutureBuilder 위젯을 사용할 때 주요 특징은 무엇인가?','["FutureBuilder는 비동기 작업의 결과를 렌더링하는 데 사용된다.","FutureBuilder는 항상 sync 메서드로 동작한다.","FutureBuilder는 앱 상태 관리를 위한 패턴이다.","FutureBuilder는 네트워크 요청을 자동으로 처리한다."]','{"correct":0}','UNDERSTAND',0.8,'["futurebuilder-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 FutureBuilder 위젯의 주요 역할은?','["위젯 트리에 상태 공유","비동기 작업 결과를 렌더링","데이터 변경 감지","UI 레이아웃 조정"]','{"correct":1}','REMEMBER',0.8,'["lifecycle-futurebuilder"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 InheritedWidget의 주요 역할은?','["상태를 변경하는 위젯","위젯 트리에 상태를 공유","UI 레이아웃을 변경","데이터를 불러오는 위젯"]','{"correct":1}','REMEMBER',0.8,'["state-management-inheritedwidget"]'),
('MOBILE_FLUTTER','MCQ','다음 중 Flutter 앱에서 상태 관리에 사용되는 패턴이 아닌 것은?','["Provider","InheritedWidget","Bloc Pattern","LiveData"]','{"correct":3}','UNDERSTAND',0.8,'["state-management-concepts"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 뷰 테스트를 수행할 때 위젯의 동작과 상태를 정확히 확인하는 것이 중요하다. 다음 중 testWidgets 함수와 관련된 올바른 설명은 무엇인가?','["testWidgets는 Flutter 앱을 실제 디바이스에서 실행하여 위젯의 동작을 테스트한다.","testWidgets는 위젯 트리의 상태를 변경하고, 그 변화에 따른 렌더링과 비동기 작업을 테스트할 수 있다.","testWidgets는 위젯을 단순히 빌드하지만, 실제 이벤트나 동작은 테스트하지 못한다.","testWidgets는 Flutter 앱의 UI를 자동으로 최적화하여 개발자가 별도로 처리할 필요가 없다."]','{"correct":1}','EVALUATE',0.8,'["flutter-widget-test"]'),
('MOBILE_FLUTTER','MCQ','다음 코드 조각에서, `FutureBuilder` 위젯은 어떤 타이밍에 rebuild 되는가?

```
class MyPage extends StatefulWidget {
  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  Future<int>? future;

  @override
  void initState() {
    super.initState();
    future = fetchUserCount();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: future,
      builder: (context, snapshot) {
        // rebuild logic here
      },
    );
  }
}
```','["Future가 완료될 때","initState 메서드 호출 시","build 메서드 실행 시","dispose 메서드 호출 시"]','{"correct":0}','ANALYZE',0.8,'["flutter-futurebuilder","async-concepts"]'),
('MOBILE_FLUTTER','MCQ','다음 중 Flutter 앱에서 사용할 수 없는 레이아웃 위젯은?','["Row","Flex","Grid","Wrap"]','{"correct":2}','UNDERSTAND',0.8,'["layout-widget-concepts"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 ListView.builder와 지연 생성을 이용하여 성능 최적화를 실시할 때, 어떤 상황에서 이를 적용해야 하는가?','["ListView.builder는 모든 아이템을 미리 생성하므로 성능이 저하된다.","성능 최적화를 위해 필요한 경우에만 ListView.builder를 사용하고 지연 생성 기법을 적용한다.","모든 Flutter 앱은 항상 ListView.builder를 기본으로 사용해야 한다.","지연 생성은 데이터 상태 관리를 돕는다."]','{"correct":1}','ANALYZE',0.8,'["listview-builder"]'),
('MOBILE_FLUTTER','MCQ','다음 중 플랫폼 통합 시 권한 요청을 처리하는 방법이 아닌 것은?','["Platform Channel","PermissionHandler 라이브러리","InAppWebViewController","Firebase Cloud Messaging"]','{"correct":3}','UNDERSTAND',0.8,'["platform-integration-concepts"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 dispose 메소드가 사용되는 시점과 그 목적은 무엇인가?','["dispose는 위젯이 화면에 보일 때 호출되며, 이벤트 리스너를 등록한다.","dispose는 위젯의 상태를 초기화하고 다시 생성할 준비를 한다.","dispose는 위젯을 더 이상 사용하지 않을 때 호출되어 메모리 누수를 방지하기 위해 리소스를 해제한다.","dispose는 위젯이 화면에 보일 때 자식 위젯들의 상태를 초기화한다."]','{"correct":2}','ANALYZE',0.8,'["lifecycle-dispose"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Stack 위젯의 주요 특징 중 하나는 무엇인가?','["자식 위젯들의 크기를 제한한다.","위젯 트리의 레벨을 제한한다.","자식 위젯들을 겹치게 배치할 수 있다.","비교적 단순한 레이아웃 구성을 가능하게 한다."]','{"correct":2}','REMEMBER',0.8,'["stack-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 StatelessWidget과 StatefulWidget을 구분하는 주요 특징은 무엇인가?','["StatefulWidget는 상태를 가지지만, StatelessWidget은 그렇지 않다.","StatelessWidget은 재렌더링이 항상 발생하지만, StatefulWidget은 그렇지 않다.","StatefulWidget은 build 메서드가 자주 호출되지만, Stateless 위젯은 그렇지 않다.","Flutter에서 StatelessWidget은 최적화된 렌더링을 제공한다."]','{"correct":0}','UNDERSTAND',0.8,'["stateless-widget","stateful-widget"]'),
('MOBILE_FLUTTER','MCQ','다음 Flutter 코드에서 가장 효율적인 상태 관리 방식은 무엇인가요?','["Provider 패키지를 사용하여 상태를 공유합니다.","각 위젯 내부에 상태를 직접 관리합니다.","InheritedWidget을 사용하여 위젯 트리 전체에 상태를 전달합니다.","Riverpod 패키지를 사용하여 컴포지션 레이어에서 상태를 관리합니다."]','{"correct":3}','EVALUATE',0.8,'["provider-riverpod"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 FutureBuilder를 사용하여 비동기 작업의 결과를 표시하는 방법 중 올바른 것은?','["FutureBuilder는 비동기 작업의 결과를 직접적으로 반환한다.","FutureBuilder는 항상 초기 상태인 ''data''를 보여주고, 비동기 작업이 완료되면 그 결과를 업데이트한다.","FutureBuilder는 비동기 작업이 시작될 때 ''initialData''로 표시된 데이터를 보여주며, 작업이 완료되면 실제 결과를 업데이트한다.","FutureBuilder는 비동기 작업이 실패할 경우 오류 메세지만을 표시하고 성공 시 아무 것도 보여주지 않는다."]','{"correct":2}','ANALYZE',0.8,'["async-future-builder"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 const 키워드를 사용하여 위젯을 선언하는 주요 목적은?','["실행 시점에만 생성","빌드 과정에서 변경 가능","컴파일 시점에 최적화","상태 변화에 따라 재렌더링"]','{"correct":2}','REMEMBER',0.8,'["widget-const-optimize"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Stack 위젯은 자식 위젯들의 위치를 어떻게 결정하는가?','["Row 위젯 기반","Column 위젯 기반","Flexibility 기반","위치 지정 기반"]','{"correct":3}','REMEMBER',0.8,'["layout-stack-positioned"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 StatelessWidget을 사용할 때 재렌더링이 일어나는 시점은?','["데이터 변경 시","UI 상태가 변경될 때","호출된 setState 함수 호출 후","화면 회전이나 다른 위젯의 변경으로 인해"]','{"correct":3}','REMEMBER',0.8,'["widget-rendering"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 Navigator를 사용해 페이지 이동을 구현할 때, 새로운 라우트로 이동하기 위한 pushNamed 메서드는 어떤 정보를 필요로 합니다.','["하나의 경로(String)만 필요합니다.","이전 화면으로 돌아가는 방법과 함께 두 개 이상의 경로가 필요합니다.","이동할 페이지에서 전달할 인자와 함께 하나의 경로가 필요합니다.","새로운 라우트를 생성하기 위한 클래스 정의가 필요합니다."]','{"correct":2}','UNDERSTAND',0.9,'["navigator-named-routes"]'),
('MOBILE_FLUTTER','MCQ','Flutter의 Row와 Column 위젯 중, 자식 위젯이 부모의 크기를 완전히 사용할 수 있도록 하는 속성이 무엇인가?','["flex","mainAxisAlignment","crossAxisAlignment","expanded"]','{"correct":3}','REMEMBER',0.9,'["row-column-layout"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 레이아웃을 구성할 때 Row와 Column 위젯은 어떻게 사용되는가?','["Row는 수평으로 아이템을 배치하며, Column은 수직으로 아이템을 배치한다.","Row와 Column 모두 수평으로 아이템을 배치한다.","Row와 Column 모두 수직으로 아이템을 배치한다.","Row는 수직으로 아이템을 배치하며, Column은 수평으로 아이템를 배치한다."]','{"correct":0}','REMEMBER',0.9,'["row-column-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 Row와 Column 위젯은 어떠한 역할을 하는가?','["Row와 Column 위젯은 UI 요소의 수평/수직 배치를 위한 레이아웃 위젯이다.","Row와 Column 위젯은 데이터 상태 관리에 사용된다.","모든 Flutter 앱은 Row와 Column을 기본으로 사용해야 한다.","Row와 Column은 네이티브 코드와 상호작용을 돕는다."]','{"correct":0}','ANALYZE',0.9,'["row-column"]'),
('MOBILE_FLUTTER','MCQ','Flutter 앱에서 StatefulWidget을 사용할 때, 상태 변경에 따라 화면을 업데이트하려면 어떤 메서드를 호출해야 할까요?','["initState() 메서드","dispose() 메서드","setState() 메서드","didChangeDependencies() 메서드"]','{"correct":2}','UNDERSTAND',0.9,'["statefulwidget-state-management"]'),
('MOBILE_FLUTTER','MCQ','다음 코드에서 상태가 변경되었을 때, 위젯 트리에 영향을 미치지 않는 부분은 어디인가?

```
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  int counter = 0;

  void incrementCounter() {
    setState(() {
      counter += 1;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(child: Text(''Hello World'')), // Counter 위젯이 아님
    );
  }
}
```','["Text 위젯의 내용","Scaffold 위젯의 자식","_MyAppState 클래스의 counter 변수","incrementCounter 메서드"]','{"correct":0}','ANALYZE',0.9,'["flutter-build-method","state-management"]'),
('MOBILE_FLUTTER','MCQ','다음 코드 조각에서, 위젯 트리가 다시 빌드되는 주요 이유는 무엇인가?

```
class MyWidget extends StatelessWidget {
  final int value;

  MyWidget({required this.value});

  @override
  Widget build(BuildContext context) {
    return Text(''Value: $value'');
  }
}
```','["final 변수 value가 변경될 때","Text 위젯이 변경될 때","MyWidget 클래스의 인스턴스가 새로 생성될 때","BuildContext 객체가 변경될 때"]','{"correct":2}','ANALYZE',0.9,'["flutter-widget-lifecycle","stateless-widget"]'),
('MOBILE_FLUTTER','MCQ','Flutter에서 InheritedWidget을 사용할 때, 어떤 상황에서 이를 적용해야 하는가?','["상태 관리에 필요한 클래스는 항상 StatefulWidget을 사용해야 한다.","하위 위젯 간 공유 상태를 효율적으로 관리하기 위해 InheritedWidget을 사용한다.","모든 위젯은 StatelessWidget만 사용하면 된다.","InheritedWidget은 앱의 모든 상태를 관리할 수 있다."]','{"correct":1}','ANALYZE',0.9,'["inheritedwidget"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `Navigator`가 어떤 동작을 하는가?

void _navigateToProfile() {
  Navigator.push(
    context,
    MaterialPageRoute(builder: (context) => ProfilePage()),
  );
}
','["현재 페이지를 보여주는 역할","새로운 페이지로 이동시키는 역할","페이지의 상태 관리를 위한 역할","위젯 트리 구조를 구성하는 역할"]','{"correct":1}','UNDERSTAND',0.2,'["navigation-concepts"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 Flutter의 플랫폼 채널을 사용하여 네이티브 액션을 트리거하는 동작에 대한 설명을 제공하세요.

import ''dart:io'' show Platform;
class NativeActionHandler {
init() {} 
invokeNativeFunction(String actionName) async {
if (Platform.isAndroid) {
// Android 플랫폼에서 액션 호출
} else if (Platform.isIOS) {
// iOS 플랫폼에서 액션 호출
}
}
}
','["invokeNativeFunction 메서드는 Platform.isAndroid 또는 Platform.isIOS를 사용하여 특정 플랫폼에 맞는 네이티브 함수를 호출합니다.","invokeNativeFunction 메서드는 모든 플랫폼에서 동일한 네이티브 함수를 호출합니다.","invokeNativeFunction 메서드는 Flutter 앱 내부에서만 동작하며, 네이티브 코드와 상호 작용하지 않습니다.","invokeNativeFunction 메서드는 항상 Android 플랫폼에서만 동작합니다."]','{"correct":0}','EVALUATE',0.2,'["platform-channel"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 Flutter 앱에서 플랫폼 채널을 사용하여 네이티브 기능에 액세스합니다.

Future<void> _getBatteryLevel() async {
  String batteryLevel = await BatteryInfoPlugin.getBatteryLevel();
  print(''Battery level is $batteryLevel'');
}
class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}
class _MyHomePageState extends State<MyHomePage> {
  void initState() {
    super.initState();
    _getBatteryLevel();
  }
  // 나머지 코드...
}
','["플랫폼 채널을 사용하여 배터리 수준 정보를 얻고 콘솔에 출력합니다.","플랫폼 채널은 배터리 수준 정보를 얻는데 사용되지 않습니다.","배터리 수준 정보는 플랫폼 채널을 통해 얻어지지만, 사용자가 볼 수 없습니다.","플랫폼 채널을 사용하면 앱이 네이티브 기능에만 접근할 수 있습니다."]','{"correct":0}','ANALYZE',0.2,'["platform-channel"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `Expanded` 위젯의 크기는 어떻게 결정되는가?

Column(
  children: [
    Expanded(
      child: Container(
        color: Colors.red,
      ),
    ),
    Text(''Hello World''),
  ],
)','["하위 자식들의 요구(constraints)를 기반으로 결정","상위 부모의 크기를 그대로 상속받음","하위 자식의 크기와 무관하게 고정된 값","상위 부모의 크기에 비례하여 확장"]','{"correct":0}','UNDERSTAND',0.2,'["layout-concepts"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 FutureBuilder 위젯이 정상적으로 데이터를 로드하지 못할 때, 어떤 동작을 취해야 하는가?

FutureBuilder(
builder: (context, snapshot) {
if (snapshot.hasError) {
return Text(''에러 발생'');
}
else if (!snapshot.hasData) {
return CircularProgressIndicator();
}
else {
return Text(snapshot.data.toString());
}
},
future: fetchUserData(),
)','["CircularProgressIndicator()를 계속 표시한다.","''데이터 로드 중...'' 텍스트를 표시한다.","''에러 발생'' 텍스트를 표시한다.","snapshot.hasData가 false일 때만 CircularProgressIndicator()를 표시한다."]','{"correct":2}','APPLY',0.2,'["futurebuilder"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `FutureBuilder`가 어떤 동작을 하는가?

@override
Widget build(BuildContext context) {
  return FutureBuilder(
    future: fetchUserData(),
    builder: (context, snapshot) {
      if (snapshot.hasData) {
        return Text(''User name: ${snapshot.data?.name}'');
      } else if (snapshot.hasError) {
        return Text(''${snapshot.error}'');
      }
      // By default, show a loading spinner.
      return CircularProgressIndicator();
    },
  );
}
','["비동기 작업의 결과를 UI에 동기화하는 역할","페이지 라우팅을 담당하는 역할","위젯 상태 관리를 위한 역할","리스트 데이터를 렌더링하는 역할"]','{"correct":0}','UNDERSTAND',0.4,'["async-programming"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 Column 위젯을 사용하여 여러 Text 위젯을 수직으로 배치하는 예제입니다.

return Column(
  children: [
    Text(''Hello''),
    Text(''World''),
    Text(''Flutter'')
  ],
);','["Text 위젯들이 수평으로 배치됩니다.","Text 위젯들의 간격이 동일하게 설정됩니다.","Text 위젯들이 수직으로 배치됩니다.","Column 위젯은 사용할 수 없습니다."]','{"correct":2}','REMEMBER',0.4,'["column"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 Stack 위젯과 Positioned 위젯을 사용한 레이아웃에 대한 설명을 제공하세요.

Stack(
alignment: Alignment.topLeft,
children: [
Positioned(
top: 10.0,
left: 20.0,
child: Container(color: Colors.red, width: 80.0, height: 50.0),
),
Positioned(
top: 30.0,
right: 40.0,
child: Container(color: Colors.blue, width: 120.0, height: 70.0),
)
],
)','["위 코드는 Stack과 Positioned를 사용하여 두 개의 컨테이너를 특정 위치에 배치합니다.","위 코드는 Stack을 사용하지 않고 단순히 두 개의 컨테이너 위젯만 반환합니다.","위 코드는 Stack을 사용하여 모든 자식 위젯을 중앙에 정렬합니다.","위 코드는 Positioned 위젯을 사용하여 레이아웃을 동적으로 조정합니다."]','{"correct":0}','EVALUATE',0.4,'["stack-positioned"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 Row 위젯을 사용하여 여러 자식 위젯들을 배치하는 예제입니다.

return Row(
  children: [
    Expanded(child: Text(''Hello'')), 
    Flexible(child: Text(''World''))
  ],
);','["Text 위젯들이 수평으로 배치됩니다.","Flexible 위젯은 사용할 수 없습니다.","Text 위젯들의 크기가 동일하게 설정됩니다.","Row 위젯은 사용할 수 없습니다."]','{"correct":0}','REMEMBER',0.4,'["row-flexible"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 InheritedWidget을 사용하여 앱 전체에서 공유할 수 있는 데이터를 관리하려 합니다.

class AppData extends InheritedWidget {
  final String data;
  const AppData({required this.data, required Widget child}) : super(child: child);
  static AppData of(BuildContext context) =>
      (context.dependOnInheritedWidgetOfExactType<AppData>() as AppData);
}
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text(''App'')), 
        body: Center(child: DataConsumer()),
      ),
    );
  }
}
class DataConsumer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final data = AppData.of(context).data;
    return Text(data);
  }
}
','["InheritedWidget은 위젯 트리에 한 번만 생성되며, 모든 자식 위젯들이 접근할 수 있습니다.","InheritedWidget은 앱 내에서 특정 위젯들만 접근 가능하며, 다른 위젯들은 접근할 수 없습니다.","DataConsumer 위젯이 생성될 때마다 새로운 AppData 인스턴스가 생성됩니다.","위젯 트리에 InheritedWidget이 여러 번 생성되며, 각각의 인스턴스는 독립적으로 작동합니다."]','{"correct":0}','ANALYZE',0.4,'["inherited-widget"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 ListView.builder를 사용하여 많은 항목을 효율적으로 표시하려 합니다.

ListView.builder(
  itemCount: 100,
  itemBuilder: (context, index) {
    return ListTile(title: Text(''Item $index''));
  },
)','["리스트의 모든 항목이 미리 생성되고 화면에 표시됩니다.","리스트 빌더가 호출될 때마다 각 항목이 즉시 생성됩니다.","ListView.builder는 화면 내부에서 보이는 항목만 동적으로 생성하고 렌더링합니다.","리스트 빌더가 호출되면 모든 항목의 타일이 동시에 생성됩니다."]','{"correct":2}','ANALYZE',0.4,'["listview-builder"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `build` 메서드가 호출될 때마다 상태(state)를 변경하는 것이 옳은 것은?

void initState() {
  super.initState();
}

@override
Widget build(BuildContext context) {
  return Scaffold(
    body: Center(
      child: Text(_counter.toString()),
    ),
  );
}

void _incrementCounter() {
  setState(() {
    _counter++;
  });
}
','["build 메서드에서 setState 호출","_incrementCounter 메서드에서 setState 호출","initState 메서드에서 setState 호출","dispose 메서드에서 setState 호출"]','{"correct":1}','UNDERSTAND',0.4,'["state-management"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 InheritedWidget을 사용한 상태 공유와 rebuild 최소화에 대한 설명을 제공하세요.

class ThemeProvider extends InheritedWidget {
final ThemeData theme;
ThemeProvider({Key key, @required this.theme, Widget child}) : super(key: key, child: child);
static ThemeProvider of(BuildContext context) =>
castToInheritedElement(context.inheritFromElement());
init(): super();
}
class MyApp extends StatelessWidget {
@override
Widget build(BuildContext context) {
return ThemeProvider(
theme: ThemeData.light(),
child: MaterialApp(title: ''Flutter Demo'', theme: ThemeData()),
);
}
}
','["ThemeProvider는 InheritedWidget을 사용하여 위젯 트리 전체에 주제 설정을 공유합니다.","ThemeProvider는 StatelessWidget을 사용하며, rebuild를 최소화하지 않습니다.","ThemeProvider는 InheritedWidget을 사용하지 않고 각각의 위젯에 개별적으로 주제를 전달합니다.","ThemeProvider는 MaterialApp에서 직접 ThemeData를 사용하여 상태를 관리합니다."]','{"correct":0}','EVALUATE',0.4,'["inheritedwidget-state-management"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `Stack` 위젯이 어떤 동작을 하는가?

@override
Widget build(BuildContext context) {
  return Stack(
    children: <Widget>[
      Positioned(top: 10.0, left: 15.0, child: Text(''Top Left'')),
      Positioned(bottom: 20.0, right: 40.0, child: Text(''Bottom Right''))
    ],
  );
}
','["위젯들의 크기를 자동으로 계산하는 역할","위젯들을 상대적인 위치에 배치하는 역할","위젯 트리를 구성하는 역할","데이터 전달을 위한 역할"]','{"correct":1}','UNDERSTAND',0.6,'["layout-concepts"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 FutureBuilder 위젯이 비동기 작업을 완료하지 못했을 때, 어떻게 에러 처리를 할 수 있을까요?

FutureBuilder(
future: fetchData(),
builder: (context, snapshot) {
if (snapshot.hasError)
return Text(''에러 발생'');
else if (!snapshot.hasData)
return CircularProgressIndicator();
else
return Text(snapshot.data.toString());
},
)','["async catch block을 사용하여 에러를 처리한다.","try-catch 블록을 사용하여 에러를 처리한다.","future: null 로 설정하여 에러 발생 시 초기화한다.","snapshot.hasError가 true일 때만 에러 메시지를 표시한다."]','{"correct":3}','APPLY',0.6,'["futurebuilder-async"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 StatelessWidget 위젯을 정의합니다. 이 위젯은 build 메서드에서 FutureBuilder를 사용하여 비동기로 데이터를 로드하고, FutureBuilder가 성공하면 Text 위젯을 반환합니다.

return FutureBuilder(
  future: getData(),
  builder: (context, snapshot) {
    if (snapshot.connectionState == ConnectionState.done) {
      return Text(snapshot.data.toString());
    } else {
      return CircularProgressIndicator();
    }
  },
);','["Text 위젯이 항상 화면에 표시됩니다.","CircularProgressIndicator가 로딩 중일 때만 화면에 표시됩니다.","getData() 함수가 실행되지 않습니다.","build 메서드에서 FutureBuilder를 사용하지 않아도 됩니다."]','{"correct":1}','REMEMBER',0.6,'["future-builder"]'),
('MOBILE_FLUTTER','CODE_READING','다음 StatefulWidget은 dispose() 메서드를 잘못 사용하고 있습니다. 코드에서 누수가 발생할 수 있는 부분을 고치려면 어떻게 해야 할까요?

@override
void dispose() {
super.dispose();
closeSocketConnection(); // 소켓 연결 종료
fetchUserData(); // 비동기 함수 호출
}
','["closeSocketConnection(); 뒤에 super.dispose();를 추가한다.","super.dispose(); 뒤에 closeSocketConnection();를 추가한다.","코드에서 fetchUserData()를 제거한다.","코드를 수정하지 않고도 누수가 발생하지 않는다."]','{"correct":1}','APPLY',0.6,'["statefulwidget-dispose"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 `InheritedWidget`이 어떤 역할을 하는가?

class MyInherited extends InheritedWidget {
  final int counter;
  MyInherited({required this.counter, required Widget child});
}

@override
Widget build(BuildContext context) {
  return MyInherited(
    counter: 0,
    child: Text(''Counter: ${context.inheritFrom<MyInherited>()?.counter ?? 0}''),
  );
}
','["상태(state)를 관리하고 전달하는 역할","위젯 트리를 구성하는 역할","디스패치 이벤트를 처리하는 역할","페이지 라우팅을 담당하는 역할"]','{"correct":0}','UNDERSTAND',0.6,'["state-management"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 FutureBuilder가 Future를 비동기적으로 처리하는 동작을 설명하세요.

FutureBuilder(
builder: (context, snapshot) {
if (snapshot.connectionState == ConnectionState.done) {
return Text(snapshot.data.toString());
} else if (snapshot.hasError) {
return Text(''에러 발생'');
} else {
return CircularProgressIndicator();
}
},
future: fetchUserData(),
)','["코드는 FutureBuilder를 사용하여 비동기적으로 데이터를 로드하고, ConnectionState가 done이 될 때까지 진행 상황을 표시합니다.","코드는 FutureBuilder를 사용하여 동기적으로 데이터를 로드하고, 상태 변경 없이 단일 텍스트를 표시합니다.","코드는 FutureBuilder를 사용하지 않고, 즉시 데이터를 로드한 후 Text 위젯만 반환합니다.","코드는 FutureBuilder를 사용하여 비동기적으로 데이터를 로드하지만, 에러가 발생하면 무한 회전 중인 동그라미를 표시합니다."]','{"correct":0}','EVALUATE',0.6,'["futurebuilder-async"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 InheritedWidget이 상태 변경을 감지하지 못하고 있습니다. 이를 해결하기 위해 어떤 작업을 해야 할까요?

MyInheritedWidget.of(context).updateData(data);

@override
void didUpdateWidget(MyOldWidget oldWidget) {
super.didUpdateWidget(oldWidget);
if (widget.data != oldWidget.data) {
MyInheritedWidget.of(context).updateData(widget.data);
}
}
','["didUpdateWidget 메서드에서 MyInheritedWidget의 rebuild 메소드 호출.","didUpdateWidget 메서드에서 MyInheritedWidget.of(context).notifyListeners();를 추가.","build 메서드에서 MyInheritedWidget.of(context).rebuild()를 호출.","didUpdateWidget 메서드에서 super.didUpdateWidget(oldWidget)을 먼저 호출하라."]','{"correct":1}','APPLY',0.6,'["inheritedwidget-updates"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 StatefulWidget을 사용하여 상태를 관리하는 예제입니다.

class MyStatefulWidget extends StatefulWidget {
  @override
  _MyStatefulWidgetState createState() => _MyStatefulWidgetState();
}

class _MyStatefulWidgetState extends State<MyStatefulWidget> {
  int count = 0;

  void incrementCount() {
    setState(() {
      count++;
    });
  }
}','["incrementCount 메서드는 호출되지 않습니다.","count 변수의 값은 변경되지 않습니다.","setState를 사용하여 위젯이 재렌더링됩니다.","MyStatefulWidget은 StatelessWidget과 같습니다."]','{"correct":2}','REMEMBER',0.6,'["stateful-widget"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 dispose 메서드의 사용과 생명주기 관리를 설명하세요.

class MyHomePage extends StatefulWidget {
@override
_MyHomePageState createState() => _MyHomePageState();
}
class _MyHomePageState extends State<MyHomePage> {
init(): super();
dispose() {
super.dispose();
// 리소스 해제 코드
}
Widget build(BuildContext context) {
return Text(''Hello, World!'');
}
}
','["dispose 메서드는 위젯의 생명주기에서 활성화 상태를 해지할 때 호출됩니다.","dispose 메서드는 위젯이 생성될 때 즉시 호출됩니다.","dispose 메서드는 build 메서드가 호출될 때마다 실행됩니다.","dispose 메서드는 위젯을 다시 빌드할 때 사용됩니다."]','{"correct":0}','EVALUATE',0.6,'["statefulwidget-dispose"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 스택 위젯(Stack)의 자식 위젯이 위치를 잘못 설정하고 있습니다. 어떤 위젯을 사용하여 문제를 해결할 수 있을까요?

Stack(
children: [
Positioned(
top: 10,
left: 50,
child: Container(width: 20, height: 20, color: Colors.red),
)
],
)','["Expanded 위젯","Flexible 위젯","Container 위젯","Positioned 위젯"]','{"correct":3}','APPLY',0.8,'["stack-positioned-widget"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 Stack 위젯을 사용하여 여러 자식 위젯들을 배치하는 예제입니다.

return Stack(
  children: [
    Positioned(top: 10, left: 20, child: Text(''Top Left'')), 
    Positioned(bottom: 10, right: 20, child: Text(''Bottom Right''))
  ],
);','["Text 위젯들이 수평으로 배치됩니다.","Positioned 위젯은 Stack 위젯의 자식이 될 수 없습니다.","Text 위젯들의 위치가 정확하게 설정됩니다.","Stack 위젯은 사용할 수 없습니다."]','{"correct":2}','REMEMBER',0.8,'["stack-positioned"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 Stack과 Positioned 위젯을 사용하여 특정 위치의 이미지를 표시합니다.

Stack(
  children: [
    Image.asset(''assets/image.png''),
    Positioned( 
      left: 50,
      top: 100,
      child: Container(color: Colors.red, width: 20, height: 20),
    ),
  ],
)','["위젯 트리는 Stack 위젯의 자식으로 정확히 위치 지정된 이미지와 빨간색 컨테이너가 포함됩니다.","위젯 트리에서 이미지는 항상 화면 중앙에 위치하며, 컨테이너는 왼쪽 상단 50px, 100px에 위치합니다.","위젯 트리는 정확히 지정된 위치에 빨간색 컨테이너만을 포함합니다.","위젯 트리에서 이미지는 화면 전체를 차지하고 컨테이너는 왼쪽 상단 50px, 100px에 위치합니다."]','{"correct":3}','ANALYZE',0.8,'["stack-positioned"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 StatelessWidget을 사용하여 상수(const) 위젯을 정의합니다.

return const MyWidget();

class MyWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text(''Hello World'');
  }
}','["MyWidget은 상태가 변경될 수 있습니다.","MyWidget은 상수(const)로 정의되었기 때문에 불변합니다.","build 메서드는 항상 호출됩니다.","const 키워드를 사용하면 위젯이 재렌더링되지 않습니다."]','{"correct":1}','REMEMBER',0.8,'["const-widget"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 ListView.builder가 정상적으로 동작하지 않도록 설계되었습니다. 이 문제를 해결하기 위해 어떤 수정이 필요할까요?

ListView.builder(
itemBuilder: (context, index) {
return Container(height: 50);
},
count: 100,
)
','["Container 위젯에 height 속성을 제거한다.","ListTile 위젯을 사용하여 itemBuilder 메소드를 수정한다.","ListView.builder에서 itemCount 대신 count를 사용한다.","itemBuilder 메서드에서 setState 호출을 제거한다."]','{"correct":2}','APPLY',0.8,'["listview-builder"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 StatefulWidget의 init와 dispose를 사용하여 위젯 생명 주기를 관리합니다.

class MyWidget extends StatefulWidget {
  @override
  _MyWidgetState createState() => _MyWidgetState();
}
class _MyWidgetState extends State<MyWidget> {
  void initState() {
    super.initState();
    print(''init called'');
  }
  void dispose() {
    super.dispose();
    print(''dispose called'');
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Text(''Hello, World!''),
    );
  }
}
','["initState 메서드가 호출되면 ''dispose called''라는 텍스트가 콘솔에 출력됩니다.","dispose 메서드가 호출되면 ''init called''라는 텍스트가 콘솔에 출력됩니다.","dispose 메서드가 호출되면 ''dispose called''라는 텍스트가 콘솔에 출력됩니다.","위젯이 생성될 때마다 ''Hello, World!''라는 텍스트가 빌더에서 중복으로 호출됩니다."]','{"correct":2}','ANALYZE',0.8,'["widget-lifecycle"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드에서 setState 메서드의 사용과 상태 관리에 대한 설명을 제공하세요.

class Counter extends StatefulWidget {
final int initialCount;
Counter({Key key, this.initialCount}) : super(key: key);
@override
_CounterState createState() => _CounterState();
}
class _CounterState extends State<Counter> {
init(): count = widget.initialCount;
void increment() { setState(() { count++; }); }
Widget build(BuildContext context) {
return Text(count.toString());
}
}
','["setState를 사용하면 Counter의 상태가 변경될 때마다 위젯 트리가 재구축됩니다.","setState를 사용하지 않으면 Counter의 상태가 변경되었을 때도 위젯 트리는 그대로 유지됩니다.","setState는 Counter의 상태를 변경하지만, build 메서드에서 반환되는 위젯은 변하지 않습니다.","setState를 사용하면 Counter의 상태가 변경될 때마다 Flutter 프레임워크에 이벤트를 전달합니다."]','{"correct":0}','EVALUATE',0.9,'["statefulwidget-setstate"]'),
('MOBILE_FLUTTER','CODE_READING','다음 코드는 FutureBuilder를 사용하여 비동기 작업의 결과를 화면에 표시하려고 합니다.

FutureBuilder<String>(
  future: _fetchData(), // 비동기 작업
  builder: (context, snapshot) {
    if (snapshot.hasError) return Text(''Error: ${snapshot.error}'');
    switch (snapshot.connectionState) {
      case ConnectionState.none:
        return Text(''Awaiting connection...'');
      case ConnectionState.active:
        return Text(''Connection active'');
      case ConnectionState.waiting:
        return Text(''Loading...'');
      case ConnectionState.done:
        if (snapshot.hasData) {
          return Text(snapshot.data!);
        } else {
          return Text(''No data available'');
        }
    }
  },
)','["비동기 작업이 완료되면 ''Loading...''이라는 텍스트가 계속 표시됩니다.","비동기 작업 중 오류 발생 시 에러 메시지가 화면에 표시됩니다.","비동기 작업의 결과를 정상적으로 화면에 표시합니다.","비동기 작업이 완료되지 않으면 ''Awaiting connection...''이라는 텍스트만 표시됩니다."]','{"correct":1}','ANALYZE',0.9,'["future-builder"]'),
('DEVOPS','MCQ','Kubernetes에서 Secret을 사용하여 비밀 정보를 관리하면 어떤 장점이 있나?','["비밀 정보를 암호화 없이 공개적으로 저장할 수 있다.","클러스터 외부에서 접근 가능한 위치에 비밀 정보를 안전하게 보관한다.","서비스 간의 비밀 정보 공유가 용이해진다.","비밀 정보의 관리와 업데이트를 쉽게 할 수 있다."]','{"correct":3}','UNDERSTAND',0.2,'["kubernetes-secret"]'),
('DEVOPS','MCQ','다음은 Kubernetes에서 Pod를 정의하는 YAML 파일입니다. 이 설정의 문제점은 무엇인가요?','["Pod는 항상 동작하지 않을 것이다.","Pod는 여러 복제본이 필요하다.","Pod는 특정 네트워크 이름을 사용해야 한다.","Pod는 스펙에서 정의된 리소스를 지원하지 못한다."]','{"correct":0}','EVALUATE',0.2,'["kubernetes-pod"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 아티팩트란 무엇을 의미하는가?','["소스 코드 저장소에 있는 모든 파일들.","코드 리뷰 과정에서 발생한 모든 변경사항.","빌드 및 테스트 단계에서 생성된 실행 가능한 소프트웨어 패키지.","프로젝트의 문서와 라이선스 파일."]','{"correct":2}','REMEMBER',0.2,'["ci-cd-artifact"]'),
('DEVOPS','MCQ','Kubernetes에서 Secret을 사용하여 암호화된 데이터를 안전하게 관리하는 방법은?','["ConfigMap","Secret","Downward API","Projected Volume"]','{"correct":1}','APPLY',0.2,'["kubernetes-secret-management"]'),
('DEVOPS','MCQ','Docker 이미지를 최적화하기 위한 방법 중 하나는?

- Dockerfile에서 불필요한 레이어를 제거하고,','["불필요한 명령을 줄인다.",".dockerignore 파일 사용","빌드 타임 캐시 활성화","Docker Hub 이용"]','{"correct":0}','REMEMBER',0.2,'["docker-layer-caching"]'),
('DEVOPS','MCQ','Docker 이미지 빌드 시에 .dockerignore 파일이 어떤 역할을 하는가?','["빌드 시 무시해야 할 파일/폴더를 정의한다.","빌드된 이미지를 압축한다.","이미지 사이즈를 줄이는 것을 돕는다.","Dockerfile과 함께 실행되는 스크립트"]','{"correct":0}','UNDERSTAND',0.2,'["dockerignore"]'),
('DEVOPS','MCQ','Kubernetes에서 pod 선택에 사용되는 방법은?

- label과 selector를 활용하여 서비스와 pod 간의 관계를 정의합니다.','["label만","selector만","label과 selector","annotation"]','{"correct":2}','REMEMBER',0.2,'["kubernetes-label-selector"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 배포 전략으로 사용할 수 있는 방식은?','["blue-green 배포","canary 배포","rolling 배포","full 배포"]','{"correct":0}','APPLY',0.4,'["ci-cd-strategy"]'),
('DEVOPS','MCQ','Kubernetes에서 Secret 객체를 활용할 때 다음 중 가장 효율적인 접근 방식은 무엇인가요?','["yaml 파일에 직접 기재","kubectl 명령어로 추가","ConfigMap을 사용","Secret 리소스 생성"]','{"correct":3}','UNDERSTAND',0.4,'["kubernetes-secret-management"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod이 계속 CrashLoop에 빠지는 경우 어떤 원인을 먼저 확인해야 하나요?','["리소스 제한 설정 오류","보안 정책 위반","ConfigMap 데이터 누락","서비스 라벨 불일치"]','{"correct":0}','ANALYZE',0.4,'["kubernetes-resource-requests-limits"]'),
('DEVOPS','MCQ','Prometheus가 제공하는 메트릭 타입 중 하나는?

- 각 서비스의 성능을 모니터링하고 분석하기 위해 사용됩니다.','["로그","메트릭","알림","대시보드"]','{"correct":1}','REMEMBER',0.4,'["prometheus-metric-types"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod를 정의할 때, livenessProbe와 readinessProbe는 어떤 차이가 있나?','["livenessProbe는 콘테이너가 실행 중인지를 확인하고, readinessProbe는 서비스에 대한 요청을 처리할 수 있는지 확인한다.","readinessProbe는 이미 설치된 애플리케이션의 상태를 확인하며, livenessProbe는 아직 설치되지 않은 애플리케이션의 상태를 확인한다.","livenessProbe와 readinessProbe는 같은 역할로 사용되며 구분되지 않는다.","Pod가 삭제되는 것을 방지하기 위해 사용된다."]','{"correct":0}','UNDERSTAND',0.4,'["kubernetes-probes"]'),
('DEVOPS','MCQ','Kubernetes에서 Deployment를 사용하여 애플리케이션을 업데이트할 때, 롤링 업데이트 전략의 단점은 무엇인가요?','["서비스 중단 시간 발생","데이터 손실 위험","스케일링 이슈","노드 리소스 오버헤드"]','{"correct":0}','EVALUATE',0.4,'["kubernetes-deployment-rolling-update"]'),
('DEVOPS','MCQ','Kubernetes에서 자원 사용량을 제한하기 위해 다음 중 가장 적절한 방법은?','["Pod를 별도로 실행하여 리소스를 분리.","ReplicaSet의 복제 수를 줄인다.","Pod에 requests/limits를 설정한다.","서비스에 대한 라벨을 변경한다."]','{"correct":2}','APPLY',0.4,'["kubernetes-resource-limits"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod의 상태를 확인하고 관리하기 위해 사용되는 프로브 유형은?','["livenessProbe","readinessProbe","startupProbe","execProbe"]','{"correct":0}','APPLY',0.4,'["kubernetes-probes"]'),
('DEVOPS','MCQ','Dockerfile에서 이미지를 최적화하기 위해 사용하는 명령어는 무엇인가?','["ADD","COPY","RUN","SHELL"]','{"correct":1}','REMEMBER',0.4,'["docker-image-optimization"]'),
('DEVOPS','MCQ','다음은 Kubernetes에서 Service를 정의하는 YAML 파일입니다. 이 설정의 문제점은 무엇인가요?','["Service는 항상 동작하지 않을 것이다.","Service는 여러 복제본이 필요하다.","Service는 특정 네트워크 이름을 사용해야 한다.","Service는 스펙에서 정의된 리소스를 지원하지 못한다."]','{"correct":0}','EVALUATE',0.4,'["kubernetes-service"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 아티팩트의 역할은?

- 배포 단계에서 사용되는 소스 코드나 실행 파일 같은 결과물을 말합니다.','["빌드 스크립트","실행 환경 설정","배포 대상","코드 리뷰"]','{"correct":2}','REMEMBER',0.4,'["ci-cd-artifact-management"]'),
('DEVOPS','MCQ','Docker 컨테이너에서 볼륨과 바인드 마운트의 차이는?

- 데이터 영속성을 보장하기 위한 방법으로 구분됩니다.','["볼륨은 호스트와 연결, 바인드는 컨테이너 내부","볼륨은 컨테이너 내부, 바인드는 호스트","볼륨은 영속성 보장, 바인드는 일시적 데이터 저장","볼륨은 일시적 데이터, 바인드는 영속성"]','{"correct":2}','REMEMBER',0.4,'["docker-volume-bind-mount"]'),
('DEVOPS','MCQ','Prometheus 메트릭 타입 중 하나인 Gauge는 무엇을 나타내는가?','["측정값이 증가하거나 감소할 수 있는 값.","특정 이벤트 발생 횟수를 추적하는 카운터.","시스템의 현재 상태를 표시하는 플래그.","시간에 따른 데이터 수집 결과."]','{"correct":0}','REMEMBER',0.4,'["prometheus-metrics"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod의 상태를 확인하고 관리하기 위한 Probe 중 다음 중 가장 적절한 사용 시나리오는?','["Pod가 실행되면 즉시 응답을 반환해야 할 때.","Pod가 준비되어 서비스에 대응할 수 있을 때.","Pod가 오류로 인해 계속 재생성되는 경우.","Pod가 정상적으로 작동하며 메모리를 사용하는 경우."]','{"correct":1}','APPLY',0.4,'["kubernetes-probes"]'),
('DEVOPS','MCQ','서비스 메트릭을 모니터링하고 알림을 설정하기 위해 사용할 수 있는 도구는?','["Prometheus","Grafana","Kibana","Elasticsearch"]','{"correct":0}','APPLY',0.4,'["observability-metrics"]'),
('DEVOPS','MCQ','Prometheus에서 메트릭 타입 중 CPU 사용률을 모니터링하기 위한 적절한 타입은 무엇인가요?','["Counter","Gauge","Histogram","Summary"]','{"correct":1}','UNDERSTAND',0.4,'["prometheus-metrics"]'),
('DEVOPS','MCQ','Kubernetes에서 Ingress를 사용하여 외부 트래픽을 처리할 때 주의해야 할 점은 무엇인가요?','["네트워크 정책 미사용","TLS 설정 누락","리소스 제한 설정 오류","서비스 선택자 불일치"]','{"correct":1}','EVALUATE',0.4,'["kubernetes-ingress-usage"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod를 시작할 때 livenessProbe, readinessProbe, startupProbe 각각의 목적은 무엇인가요?','["Pod가 정상적으로 실행되었는지를 확인한다.","서비스 준비 상태를 확인하고 요청을 받아들일 수 있는지 여부를 결정한다.","초기화 시간이 긴 서비스에 대해 추가적인 타임아웃을 설정한다.","리소스 사용량을 모니터링하여 과부하를 감지한다."]','{"correct":1}','ANALYZE',0.6,'["kubernetes-liveness-readiness-probe"]'),
('DEVOPS','MCQ','GitOps를 사용하여 애플리케이션 배포를 관리하고 있습니다. 이 방식의 문제점은 무엇인가요?','["GitOps는 배포 과정에서 실시간 동기화를 지원하지 않는다.","GitOps는 롤백을 지원하지 않는다.","GitOps는 보안을 충분히 고려하지 못한다.","GitOps는 다양한 CI/CD 도구와 호환되지 않는다."]','{"correct":1}','EVALUATE',0.6,'["gitops-concepts"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod의 상태를 확인하기 위해 사용하는 명령어는?','["kubectl get pod","kubectl describe node","kubectl logs container","kubectl exec -it"]','{"correct":0}','REMEMBER',0.6,'["kubernetes-pod-status"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod과 Deployment의 주요 차이점은 무엇인가?','["Deployment는 Pod을 관리하고 상태를 유지한다.","Pod은 고유한 네트워크 주소를 가지고 있다.","Deployment는 볼륨을 정의할 수 없다.","Pod은 스케일링을 지원한다"]','{"correct":0}','UNDERSTAND',0.6,'["kubernetes-pod-deployment"]'),
('DEVOPS','MCQ','Docker 이미지를 생성할 때 .dockerignore 파일은 어떤 역할을 합니다.','["컴파일 과정에서 발생하는 임시 파일과 빌드 디렉토리를 지정하여 빌드 시간을 줄입니다.","빌드 스크립트의 실행을 제어하고, 이미지 크기를 최적화합니다.","다중 이미지 빌드 시 서로 다른 이미지에 필요한 파일만 선택적으로 복제합니다.","특정 경로를 무시하여 Dockerfile에서 지정된 컨텍스트로부터 이미지를 건설할 때 불필요한 파일을 제외합니다."]','{"correct":3}','ANALYZE',0.6,'["docker-dockerignore"]'),
('DEVOPS','MCQ','Docker 컨테이너에서 볼륨 마운트를 사용하려 할 때 다음 중 가장 적절한 시나리오는?','["실행 중인 컨테이너의 변경 사항을 영구적으로 저장해야 할 때.","컨테이너 이미지 크기를 줄이는 방법으로 사용할 때.","동일한 볼륨을 여러 컨테이너 간에 공유해야 할 때.","기존 파일 시스템에 대한 직접 접근 권한 필요시."]','{"correct":0}','APPLY',0.6,'["docker-volume-mount"]'),
('DEVOPS','MCQ','Docker 컨테이너 네트워킹에서, Docker 서비스를 사용하면 어떤 이점을 얻을 수 있나?','["내부 DNS 이름을 통해 다른 도커 컨테이너에 대한 액세스를 제공한다.","서비스 간의 직접 연결을 쉽게 할 수 있다.","도커 이미지 크기를 줄일 수 있다.","컨테이너 실행 시간을 크게 단축시킬 수 있다."]','{"correct":0}','UNDERSTAND',0.6,'["docker-networking"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod의 상태를 관리하기 위해 여러 타입의 probe가 사용됩니다. livenessProbe와 readinessProbe의 주요 차이점은 무엇인가요?','["livenessProbe는 Pods가 실행 중인지 확인하고, readinessProbe는 서비스에 연결될 준비가 되었는지 확인한다","readinessProbe는 Pods가 실행 중인지 확인하고, livenessProbe는 서비스에 연결될 준비가 되었는지 확인한다","두 타입의 probe 모두 Pod의 상태를 확인하지만, 다른 방법으로 동작한다","livenessProbe와 readinessProbe는 같은 기능을 수행하며 주요 차이점은 없다"]','{"correct":0}','ANALYZE',0.6,'["kubernetes-probe"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod의 livenessProbe와 readinessProbe를 구분하는 가장 중요한 차이는 무엇인가요?','["상태 체크 시점의 차이","서비스 가용성 확인과 노드 상태 확인","리소스 사용량 차이","네트워크 연결 상태 차이"]','{"correct":1}','EVALUATE',0.6,'["kubernetes-probe-difference"]'),
('DEVOPS','MCQ','Docker 이미지 빌드 시 사용되는 Dockerfile 명령어 중, 볼륨 마운트를 설정하는데 사용되는 것은?','["VOLUME","ADD","COPY","ENV"]','{"correct":0}','REMEMBER',0.6,'["docker-volume-mounts"]'),
('DEVOPS','MCQ','Kubernetes의 Deployment에서 롤링 업데이트 기능을 활성화하기 위한 명령어는?','["kubectl apply -f deployment.yaml","kubectl rollout status deployment/name","kubectl set image deployment/name=image:tag","kubectl rollout undo deployment/name"]','{"correct":2}','REMEMBER',0.6,'["kubernetes-rolling-update"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 Blue-Green 배포 전략의 주요 특징은 무엇인가?','["기존 애플리케이션과 새 버전을 동시에 실행한다.","새로운 버전만 먼저 실행한 후 기존 버전을 삭제한다.","기존 애플리케이션을 완전히 중지한 다음 새로운 버전으로 대체한다.","신규 애플리케이션이 준비되면 트래픽을 빠르게 전환한다"]','{"correct":3}','UNDERSTAND',0.6,'["ci-cd-blue-green-deployment"]'),
('DEVOPS','MCQ','Kubernetes의 HPA(Horizontal Pod Autoscaler)가 수행하는 주요 기능은?','["Pod 수량 조정","서비스 종류 설정","네트워크 보안 정책 관리","로그 모니터링"]','{"correct":0}','REMEMBER',0.6,'["kubernetes-hpa"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서, 배포 전략으로 Blue-Green 배포를 사용하면 어떤 이점을 얻을 수 있나?','["배포 중 애플리케이션 서비스의 불연속성을 없앨 수 있다.","노드 간 자동 밸런싱이 가능해진다.","서비스 계약에 대한 변경사항을 쉽게 적용할 수 있다.","배포 시간을 크게 줄일 수 있다."]','{"correct":0}','UNDERSTAND',0.6,'["ci-cd-blue-green-deployment"]'),
('DEVOPS','MCQ','Kubernetes에서 livenessProbe와 readinessProbe의 주요 차이점은 무엇인가?','["livenessProbe는 애플리케이션 상태를 점검하고, readinessProbe는 트래픽을 수락할 준비가 되었는지 확인한다.","readinessProbe는 애플리케이션이 정상적으로 실행되고 있는지 확인하며, livenessProbe는 서비스의 상태를 나타낸다.","livenessProbe는 트래픽을 수락할 준비가 되었는지 확인하고, readinessProbe는 애플리케이션 상태를 점검한다.","readinessProbe와 livenessProbe는 동일한 역할을 수행한다"]','{"correct":0}','UNDERSTAND',0.6,'["kubernetes-liveness-readiness-probes"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 GitOps 개념의 주요 특징은 무엇인가요?','["변경사항을 추적하는 동일한 복사본 유지","코드 리뷰를 자동화","서비스 가용성 확인","스케일링 이슈 해결"]','{"correct":0}','EVALUATE',0.6,'["ci-cd-gitops-concept"]'),
('DEVOPS','MCQ','Docker 이미지의 크기를 줄이는 방법 중 하나로 볼륨 바인딩(volume binding)이 효과적이라는 문장은 맞는가?','["맞다. 볼륨 바인딩으로 불필요한 파일을 제거할 수 있다.","틀리다. 볼륨 바인딩은 이미지 크기에 영향을 미치지 않는다.","맞다. 볼륨 바인딩으로 이미지 레이어를 공유할 수 있다.","틀리다. 볼륨 바인딩은 이미지를 더 크게 만든다"]','{"correct":1}','UNDERSTAND',0.6,'["docker-volume-binding"]'),
('DEVOPS','MCQ','Docker 이미지 최적화를 위해 다음과 같은 Dockerfile을 사용합니다. 이 코드의 문제점은 무엇인가요?','["빌드 과정에서 캐시 활용이 제대로 이루어지지 않는다.","볼륨 마운트가 제대로 설정되어 있지 않다.","멀티스테이지 빌드를 지원하지 않는다.","Docker 이미지를 최적화할 수 없다."]','{"correct":0}','EVALUATE',0.6,'["docker-image-optimization"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 아티팩트가 무엇인지 설명해주세요.','["소스 코드","빌드된 소프트웨어","환경 변수","네트워크 설정"]','{"correct":1}','REMEMBER',0.6,'["ci-cd-artifact-management"]'),
('DEVOPS','MCQ','Kubernetes에서 Secret을 사용하는 주된 목적은?','["애플리케이션 코드의 성능 최적화.","데이터베이스 연결 문자열과 비밀번호를 안전하게 관리하기 위함.","네트워크 트래픽의 보안을 강화하기 위함.","웹 애플리케이션의 사용자 인증을 처리하는 데 사용됨."]','{"correct":1}','REMEMBER',0.6,'["kubernetes-secret"]'),
('DEVOPS','MCQ','Dockerfile에서 이미지 크기를 최적화하기 위해 사용할 수 있는 방법은 무엇인가요?','["VOLUME 명령어를 사용하여 불필요한 파일을 제거한다.",".dockerignore 파일을 사용하여 빌드시 불필요한 파일을 배제한다.","ENTRYPOINT 대신 CMD를 사용하여 실행 스크립트를 최적화한다.","CMD 명령어를 여러 개 정의하여 실행 시 선택적으로 적용할 수 있도록 한다."]','{"correct":1}','APPLY',0.6,'["docker-image-optimization"]'),
('DEVOPS','MCQ','Dockerfile에서 이미지 크기를 최적화하려고 여러 방법을 사용하고 있습니다. 다음 중 가장 효과적인 방법은 무엇인가요?','[".dockerignore 파일을 수정하여 불필요한 파일을 제거한다","이미지 빌드 시마다 모든 레이어를 새로 만든다","Dockerfile에서 불필요한 명령어를 줄인다","빌드 후에 이미지를 압축한다"]','{"correct":0}','ANALYZE',0.8,'["docker-ignore"]'),
('DEVOPS','MCQ','Docker 이미지를 빌드할 때, .dockerignore 파일을 사용하면 어떤 이점을 얻을 수 있나?','["빌드 시간을 줄일 수 있다.","이미지 크기를 늘릴 수 있다.","도커 파일의 복잡성을 증가시킬 수 있다.","컨테이너 실행 시 성능 향상을 가져올 수 있다."]','{"correct":0}','UNDERSTAND',0.8,'["dockerignore"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 GitOps 개념의 주요 특징은 무엇입니까?','["코드 커밋 후 바로 자동으로 배포되는 것입니다.","Git 저장소에 정의된 상태와 실제 운영 환경을 동기화하는 것입니다.","개발자들이 직접 서버에 코드를 푸시하여 배포할 수 있는 접근 권한이 주어집니다.","CI/CD 파이프라인에서 모든 작업은 Git 저장소 내부에서만 이루어져야 합니다."]','{"correct":1}','ANALYZE',0.8,'["ci-cd-gitops"]'),
('DEVOPS','MCQ','Kubernetes에서 Ingress를 설정할 때 다음 중 가장 적절한 방법은?','["Ingress 리소스에 외부 IP 주소를 직접 지정한다.","Service의 타겟 포트를 변경하여 접근을 제어한다.","IngressRule을 사용하여 호스트와 경로를 매핑한다.","ReplicaSet의 복제 수를 조절하여 트래픽 분산을 관리한다."]','{"correct":2}','APPLY',0.8,'["kubernetes-ingress"]'),
('DEVOPS','MCQ','Kubernetes에서 pod의 상태를 확인하고자 할 때 다음 중 가장 적합한 도구는 무엇인가요?','["kubectl get pods","kubectl describe node","kubectl logs deployment","kubectl rollout status"]','{"correct":0}','UNDERSTAND',0.8,'["kubernetes-pod"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 Blue-Green 배포 전략과 Canary 배포 전략은 어떻게 다릅니다.','["Blue-Green 배포는 새로운 버전의 서비스를 실행시키지 않고 기존 서비스만 업데이트한다.","Canary 배포는 전체 트래픽을 단번에 새 서비스로 이동시킨다.","Blue-Green 배포는 기존 서비스와 새 서비스가 동시에 동작하며 트래픽 분리를 허용하고, Canary 배포는 부분적인 트래픽만 새 서비스로 보내서 점진적으로 확장한다.","Canary 배포는 모든 트래픽을 단계별로 이동시키며 Blue-Green은 한 번에 전체를 업데이트한다."]','{"correct":2}','ANALYZE',0.8,'["ci-cd-blue-green-canary"]'),
('DEVOPS','MCQ','Dockerfile에서 이미지를 최적화하기 위해 다음 중 가장 효과적인 방법은?','["이미지 레이어를 최소화하는 명령을 사용한다.","도커 이미지를 항상 새로운 빌드로 생성한다.","볼륨 마운트를 사용하여 실행 시점의 변경 사항을 반영한다.","Dockerignore 파일을 통해 불필요한 파일을 배제한다."]','{"correct":3}','APPLY',0.8,'["docker-image-optimization"]'),
('DEVOPS','MCQ','Prometheus에서 메트릭 타입 중 Counter의 주요 특징은 무엇인가?','["값이 감소하는 메트릭이다.","값이 증가하는 메트릭이다.","특정 시간에 고정된 값을 갖는 메트릭이다.","서비스 상태를 나타내는 메트릭이다"]','{"correct":1}','UNDERSTAND',0.8,'["prometheus-metrics-types"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 다음 중 아티팩트를 안전하게 관리하고 공유하는 가장 효과적인 방법은?','["개별 작업자 별로 아티팩트 저장소 설정.","배포 전략을 blue-green으로 적용.","사용하지 않는 아티팩트를 정기적으로 삭제.","아티팩트 버전 정보를 항상 기록합니다."]','{"correct":3}','APPLY',0.8,'["ci-cd-artifact-management"]'),
('DEVOPS','MCQ','Kubernetes에서 livenessProbe와 readinessProbe의 차이는?

- Pod의 상태를 확인하는 프로브로, 각각 다른 목적을 가지고 있습니다.','["생명체크와 준비체크","생명체크와 대기체크","준비체크와 생명체크","대기체크와 생명체크"]','{"correct":0}','REMEMBER',0.8,'["kubernetes-probes"]'),
('DEVOPS','MCQ','네트워크 정책(NetworkPolicy)을 사용하여 Kubernetes 클러스터의 통신을 제어하는 방법은?','["iptables 규칙 설정","Kubernetes 네트워크 API 호출","CNI 플러그인 구성","NetworkPolicy YAML 파일 적용"]','{"correct":3}','APPLY',0.8,'["kubernetes-network-policy"]'),
('DEVOPS','MCQ','Docker 이미지를 최적화하기 위해 사용할 수 있는 방법은?','["다음 빌드 단계에서 이전 단계의 결과를 캐시하고 재사용함.","볼륨을 마운트하여 변경사항을 저장함.","이미지 크기를 크게 함.","Dockerfile에서 .dockerignore 파일을 사용하지 않음."]','{"correct":0}','REMEMBER',0.8,'["docker-build-optimization"]'),
('DEVOPS','MCQ','Docker 이미지 빌드 시 다음 명령어가 어떤 역할을 하는지 설명하세요.','["이미지를 실행합니다.","빌드 과정에서 변경된 파일만 새로 빌드합니다.","도커 이미지 레이어를 최적화합니다.","기존 이미지와 비교하여 새로운 이미지만 생성합니다."]','{"correct":1}','UNDERSTAND',0.8,'["docker-build"]'),
('DEVOPS','MCQ','DNS의 역할은 무엇인가?','["웹 페이지를 다운로드합니다.","호스트 이름을 IP 주소로 변환합니다.","데이터베이스에 연결하는 데 사용됩니다.","애플리케이션 코드를 컴파일합니다."]','{"correct":1}','REMEMBER',0.8,'["networking-dns"]'),
('DEVOPS','MCQ','Kubernetes에서 Horizontal Pod Autoscaler (HPA)가 어떤 목적으로 사용됩니까?','["Pod의 수를 일정하게 유지하여 고른 트래픽 분배를 제공한다.","Pod의 CPU 및 메모리 리소스 요구사항을 동적으로 조절한다.","각 Pod에 대해 실행 중인 컨테이너의 개수를 늘리거나 줄인다.","트래픽의 변동에 대응하여 필요한 만큼의 Pod 수를 자동으로 확장하거나 축소한다."]','{"correct":3}','ANALYZE',0.8,'["kubernetes-hpa"]'),
('DEVOPS','MCQ','Docker 이미지 크기를 줄이는 최선의 방법은 무엇인가요?','["이미지 레이어 최적화","Dockerfile에서 사용하지 않는 파일 제거","루트 권한으로 컨테이너 실행","도커 인스턴스 수 증가"]','{"correct":0}','EVALUATE',0.8,'["docker-image-size-optimization"]'),
('DEVOPS','MCQ','Kubernetes에서 ConfigMap 및 Secret이 어떤 역할을 담당하나요.','["ConfigMap은 환경 변수를 관리하고, Secret은 애플리케이션의 설정 파일을 보호한다.","ConfigMap은 비밀 정보와 같은 민감한 데이터를 안전하게 저장하며, Secret은 환경 변수 및 설정값들을 담당한다.","ConfigMap은 비공개 정보를 처리하고, Secret은 공통 구성 값과 설정 값을 관리한다.","ConfigMap은 애플리케이션의 동작을 위한 설정 값을 제공하고, Secret은 민감한 데이터 또는 비밀 키를 안전하게 저장합니다."]','{"correct":3}','ANALYZE',0.8,'["kubernetes-configmap-secret"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 Blue-Green 배포 전략의 장점은 무엇인가요?','["빠른 롤백 가능","서비스 중단 시간 최소화","데이터 동기화 문제 해결","스케일링 이슈 개선"]','{"correct":0}','EVALUATE',0.8,'["ci-cd-blue-green-deployment"]'),
('DEVOPS','MCQ','다음은 Kubernetes에서 HPA(Horizontal Pod Autoscaler)를 사용하여 자동 스케일링을 설정하는 방법입니다. 이 설정의 문제점은 무엇인가요?','["HPA는 CPU 사용률을 기준으로만 스케일링할 수 있다.","HPA는 메모리 사용률을 기준으로 스케일링할 수 없다.","HPA는 CPU와 메모리를 동시에 감지하지 못한다.","HPA 설정이 잘못되어 있어 실제로 동작하지 않을 것이다."]','{"correct":3}','EVALUATE',0.9,'["kubernetes-hpa"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 배포 전략으로 사용할 수 있는 방법 중 하나는 무엇인가요?','["Blue-Green Deployment","Canary Release","Rolling Update","GitOps"]','{"correct":0}','APPLY',0.9,'["ci-cd-blue-green-deployment"]'),
('DEVOPS','MCQ','Kubernetes에서 Pod 간 통신을 설정하기 위해 사용되는 주요 구성 요소는 무엇인가요?','["Service","Deployment","ConfigMap","Secret"]','{"correct":0}','APPLY',0.9,'["kubernetes-service"]'),
('DEVOPS','MCQ','Prometheus 메트릭 시스템에서, 각 주요 메트릭 타입은 어떤 용도로 사용되나?','["Counter: 증가만을 추적하는 측정값.","Gauge: 특정 시간에 대한 상태를 나타내는 값.","Histogram: 분산된 값을 그룹화하여 수집한다.","Summary: 분산된 값과 누적된 값을 동시에 제공한다."]','{"correct":0}','UNDERSTAND',0.9,'["prometheus-metrics"]'),
('DEVOPS','MCQ','CI/CD 파이프라인에서 단계별로 생성된 이미지를 저장하고 재사용하기 위한 전략은 무엇인가요?','["싱글스택 아키텍처 사용","배치 스크립트 활용","아티팩트 리포지토리 설정","개발 환경 별도 구축"]','{"correct":2}','UNDERSTAND',0.9,'["ci-cd-artifact-repository"]'),
('DEVOPS','MCQ','Kubernetes의 Pod와 Deployment 간의 주요 차이점은?','["Pod는 일회성 작업을 실행하며, Deployment는 지속적인 서비스를 제공한다.","Deployment는 Pods의 세트를 관리하고 스케일링하며, Pod는 단일 컨테이너 엔터티다.","Pod는 복수의 컨테이너를 포함할 수 있으며, Deployment는 단일 컨테이너만 가능하다.","Deployment는 상태를 유지하지 않으며, Pod는 항상 최신 상태로 유지된다."]','{"correct":1}','REMEMBER',0.9,'["kubernetes-pod-deployment"]'),
('DEVOPS','MCQ','관측성을 향상시키기 위해 사용할 수 있는 도구는 무엇인가요?','["Prometheus","Grafana","Elasticsearch","Kibana"]','{"correct":0}','APPLY',0.9,'["observability-prometheus"]'),
('DEVOPS','CODE_READING','다음 Jenkinsfile 파이프라인은 어떤 단계를 수행하고 있는가?

pipeline {
  agent any
  stages {
    stage(''Build'') {
      steps {
        sh ''mvn clean package''
      }
    }
    stage(''Test'') {
      steps {
        sh ''mvn test''
      }
    }
    stage(''Deploy'') {
      steps {
        sh ''scp target/*.jar user@server:/opt/myapp''
      }
    }
  }
}
','["Jenkins Pipeline에서 단일 빌드 단계를 정의한다.","Jenkins Pipeline에서 여러 개의 연속된 빌드 및 테스트 단계를 정의한다.","Jenkins Pipeline에서 배포 단계만을 정의한다.","Jenkins Pipeline에서 Jenkins Agent 설정을 변경한다."]','{"correct":1}','REMEMBER',0.2,'["ci-cd-pipeline"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일은 HorizontalPodAutoscaler를 정의합니다.

apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: hpa-example
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: example-deployment
  minReplicas: 1
  maxReplicas: 6
  targetCPUUtilizationPercentage: 50','["HPA는 CPU 사용률이 50%를 초과할 때마다 최대 6개의 Pod를 생성합니다.","HPA는 항상 최소 1개의 Pod만 유지하고, CPU 사용률이 50%가 넘을 경우 더 많은 Pod를 생성하지 않습니다.","HPA는 Deployment ''example-deployment''에 대한 CPU 사용률 기반으로 자동 스케일링을 수행하며, 최대 6개까지 증가할 수 있습니다.","HPA는 언제든지 모든 Pod를 제거합니다."]','{"correct":2}','EVALUATE',0.2,'["kubernetes-hpa"]'),
('DEVOPS','CODE_READING','다음 Nginx 셋팅은 어떤 역할을 수행하나요?

server {
  listen 80;
  server_name example.com;
  location / {
    proxy_pass http://localhost:3000;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
  }
}','["TLS를 처리합니다.","로드 밸런싱을 구현합니다.","리버스 프록시 역할을 수행합니다.","네트워크 트래픽의 로그를 기록합니다."]','{"correct":2}','UNDERSTAND',0.2,'["network-reverse-proxy"]'),
('DEVOPS','CODE_READING','다음 Jenkinsfile의 파이프라인 단계에서 build 과정은 어떻게 작동하나요?

pipeline {
  agent any
  stages {
    stage(''Build'') {
      steps {
        sh ''mvn clean install''
      }
    }
    stage(''Test'') {
      steps {
        sh ''mvn test''
      }
    }
  }
}','["build 단계에서 테스트를 실행합니다.","build 단계에서만 mvn clean install 명령어가 실행됩니다.","테스트 단계에서 build를 실행합니다.","각 단계에서 모두 mvn clean install을 실행합니다."]','{"correct":1}','UNDERSTAND',0.4,'["ci-cd-pipeline"]'),
('DEVOPS','CODE_READING','다음 Dockerfile은 무엇을 설정하고 있는가?

FROM node:14-alpine
WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .
CMD ["npm", "start"]','["Docker 이미지 빌드 시 실행할 기본 명령어를 지정한다.","Docker 컨테이너가 외부 네트워크에 접근하도록 설정한다.","Docker 컨테이너 내에서 필요한 패키지를 설치한다.","Docker 이미지의 작성을 위한 레이어를 구성한다."]','{"correct":0}','REMEMBER',0.4,'["docker-cmd-vs-entrypoint"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일에서 secret은 어떻게 사용되나요?

apiVersion: v1
kind: Secret
metadata:
  name: my-secret
type: Opaque
data:
  username: dmFsdWU=
  password: dmFsdWU=
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: secret-deployment
spec:
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-container
        image: myapp:v1
        envFrom:
        - secretRef:
            name: my-secret','["secret는 암호화되어 저장됩니다.","deployment에서 secret을 참조하여 환경 변수를 설정합니다.","container가 시작될 때 secret이 생성됩니다.","deployments의 모든 container에 공유됩니다."]','{"correct":1}','UNDERSTAND',0.4,'["kubernetes-secret"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Secret를 정의합니다. 문제가 될 부분은 무엇일까요?

apiVersion: v1
kind: Secret
metadata:
  name: myapp-secret
type: Opaque
data:
  db-password: QWxhZGRybGFjawo=
','["Secret의 타입이 잘못 설정되었습니다.","Base64 인코딩된 비밀번호가 누락되었습니다.","Secret 이름이 잘 설정되지 않았습니다.","Pod 또는 Deployment에서 Secret을 참조하지 않습니다."]','{"correct":3}','APPLY',0.4,'["kubernetes-secret"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Deployment를 정의합니다. 이 설정에서 문제는 무엇일까요?

apiVersion: apps/v1
kind: Deployment
metadata:
  name: myapp-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: app-container
        image: myregistry/myimage:latest
        ports:
        - containerPort: 8080','["replica 개수 설정이 잘못되었습니다.","selector와 template의 라벨이 일치하지 않습니다.","containerPort가 정확하게 지정되지 않았습니다.","deployment에 대한 resource limit이 누락되었습니다."]','{"correct":1}','APPLY',0.4,'["kubernetes-selector"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Deployment 리소스를 정의합니다.

apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-container
        image: my-image:latest
        ports:
        - containerPort: 8080','["리소스 요청이 설정되어 있지 않아 Kubernetes는 컨테이너에 리소스를 자동으로 할당할 것이다.","선택기와 템플릿 라벨이 일치하지 않아 Deployment가 실패할 것이다.","위 YAML은 정상적으로 동작하여 3개의 Pod을 생성한다.","컨테이너 포트가 설정되어 있지 않아 서비스를 통해 접근할 수 없다."]','{"correct":2}','ANALYZE',0.4,'["kubernetes-deployment"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes HorizontalPodAutoscaler를 정의합니다. 문제가 될 부분은 무엇일까요?

apiVersion: autoscaling/v2beta2
kind: HorizontalPodAutoscaler
metadata:
  name: myapp-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: myapp-deployment
  minReplicas: 2
  maxReplicas: 6
  targetCPUUtilizationPercentage: 50','["minReplicas와 maxReplicas 설정이 잘못되었습니다.","targetCPUUtilizationPercentage가 너무 낮습니다.","scaleTargetRef에 Deployment 이름을 잘못 지정했습니다.","HorizontalPodAutoscaler에 대한 metrics server가 설치되지 않았습니다."]','{"correct":3}','APPLY',0.4,'["kubernetes-hpa"]'),
('DEVOPS','CODE_READING','다음 Dockerfile은 이미지 크기를 최적화하기 위해 사용됩니다.

FROM node:14-alpine
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
CMD ["npm", "start"]','["Dockerfile는 모든 의존성을 설치한 후에 애플리케이션 소스 코드를 복사하므로 이미지 크기를 최적화한다.","Dockerfile은 먼저 모든 의존성을 설치하고 나서 애플리케이션 소스 코드를 복사하여 이미지 크기를 줄인다.","Dockerfile은 의존성 설치 전에 애플리케이션 소스 코드를 복사하므로 이미지 크기가 커진다.","Dockerfile는 이미지 빌드 과정에서 캐시 활용을 방해하는 명령어를 포함한다."]','{"correct":1}','ANALYZE',0.4,'["docker-image-optimization"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes ConfigMap을 정의합니다. 문제가 될 부분은 무엇일까요?

apiVersion: v1
kind: ConfigMap
metadata:
  name: myapp-config
data:
  application.properties: |-
    server.port=8080
    spring.datasource.url=jdbc:mysql://localhost:3306/mydb','["ConfigMap의 데이터 형식이 잘못되었습니다.","application.properties 파일에 변수가 누락되었습니다.","ConfigMap 이름이 잘 설정되지 않았습니다.","ConfigMap을 사용하는 Pod 또는 Deployment가 존재하지 않습니다."]','{"correct":3}','APPLY',0.6,'["kubernetes-configmap"]'),
('DEVOPS','CODE_READING','다음 Jenkinsfile은 파이프라인 단계를 정의합니다.

pipeline {
    agent any
    stages {
        stage(''Build'') {
            steps {
                sh ''npm install''
                sh ''npm run build''
            }
        }
        stage(''Test'') {
            steps {
                sh ''npm test''
            }
        }
    }
}
post {
    always {
        cleanWs()
    }
}','["Jenkinsfile은 빌드 단계에서 npm install을 수행하지 않습니다.","Jenkinsfile은 테스트 단계에서 build를 생성한 후 이를 실행합니다.","Jenkinsfile은 빌드 및 테스트 단계 모두에 npm 명령어를 사용합니다.","Jenkinsfile은 항상 작업스페이스를 깨끗이 정리합니다."]','{"correct":2}','EVALUATE',0.6,'["ci-cd-pipeline"]'),
('DEVOPS','CODE_READING','다음 Docker Compose 파일은 어떤 서비스를 구성하고 있는가?

version: ''3''
services:
  web:
    build: .
    ports:
      - "5000:5000"
    volumes:
      - .:/code
    environment:
      - DATABASE_HOST=db
  db:
    image: postgres','["Docker 서비스를 정의한다.","Docker Compose 프로젝트 설정을 구성한다.","Docker 컨테이너 네트워크를 생성한다.","Docker 이미지를 빌드한다."]','{"correct":1}','REMEMBER',0.6,'["docker-compose"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일은 무엇을 정의하고 있는가?

apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: my-app
        image: myregistry/myimage:v1','["Kubernetes Deployment 리소스를 정의한다.","Kubernetes ConfigMap을 생성한다.","Kubernetes Ingress 규칙을 설정한다.","Kubernetes Secret을 만든다."]','{"correct":0}','REMEMBER',0.6,'["kubernetes-deployment"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일은 ReplicaSet을 정의합니다.

apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: example-replicaset
spec:
  replicas: 3
  selector:
    matchLabels:
      app: example-app
  template:
    metadata:
      labels:
        app: another-app
    spec:
      containers:
      - name: example-container
        image: nginx:latest','["ReplicaSet은 정확히 3개의 Pod를 생성합니다.","ReplicaSet은 ''another-app'' 라벨을 가진 Pod 3개를 생성하려고 시도하지만 실패합니다.","ReplicaSet은 ''example-app'' 라벨을 가진 Pod 3개를 생성합니다.","ReplicaSet은 정확히 아무것도 생성하지 않습니다."]','{"correct":1}','EVALUATE',0.6,'["kubernetes-selector"]'),
('DEVOPS','CODE_READING','다음 Dockerfile은 이미지를 최적화하기 위해 캐시를 사용합니다. 이 코드의 동작으로 옳은 것은?

FROM node:14-alpine
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
CMD ["npm", "start"]','["이미지 크기를 줄이는 데 효과적이다.","도커 이미지를 최대한 빠르게 빌드한다.","코드를 실행할 때마다 모든 노드 모듈을 다시 설치한다.","도커 캐시가 무효화되어 각 단계에서 모든 파일이 새로 다운로드된다."]','{"correct":1}','UNDERSTAND',0.6,'["docker-cache"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Pod을 정의합니다.

apiVersion: v1
kind: Pod
metadata:
  name: my-pod
spec:
  containers:
  - name: app-container
    image: my-app:latest
    livenessProbe:
      httpGet:
        path: /healthz
        port: 8080','["Pod은 정상적으로 시작되지만, Liveness Probe가 동작하지 않아 컨테이너가 계속 재시작된다.","Pod은 정상적으로 생성되며, Liveness Probe는 지정된 경로와 포트를 통해 정기적으로 실행된다.","Liveness Probe의 httpGet 설정이 잘못되어 Pod은 생성되지 않는다.","Pod은 정상적으로 생성되지만, Readiness Probe가 없어 서비스에 노출되지 않는다."]','{"correct":1}','ANALYZE',0.6,'["kubernetes-liveness-probe"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일은 ConfigMap을 정의합니다.

apiVersion: v1
kind: ConfigMap
metadata:
  name: example-configmap
data:
  app-name: MyApp
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: example-deployment
spec:
  template:
    metadata:
      labels:
        app: example-app
    spec:
      containers:
      - name: example-container
        image: myapp:v1
        envFrom:
          - configMapRef:
              name: example-configmap','["Deployment는 ConfigMap에서 ''app-name'' 환경 변수를 가져와 사용합니다.","ConfigMap은 ''example-app'' 라벨을 가진 Deployment에 전달됩니다.","Deployment의 설정은 ConfigMap에서 ''app-name'' 데이터를 참조하지 않습니다.","ConfigMap은 항상 직접 특정 Pod에만 적용됩니다."]','{"correct":0}','EVALUATE',0.6,'["kubernetes-configmap"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일의 pod는 어떤 타이밍에 시작되나요?

apiVersion: v1
kind: Pod
metadata:
  name: example-pod
spec:
  containers:
  - name: app-container
    image: my-app:v1
    readinessProbe:
      httpGet:
        path: /healthz
        port: 8080
      initialDelaySeconds: 5
      periodSeconds: 10','["Pod가 생성되면 즉시 시작됩니다.","readinessProbe가 성공하면 시작됩니다.","initialDelaySeconds 이후에 시작됩니다.","periodSeconds마다 시작합니다."]','{"correct":2}','UNDERSTAND',0.6,'["kubernetes-readinessprobe"]'),
('DEVOPS','CODE_READING','다음 Prometheus 쿼리는 무엇을 나타내나요?

sum(rate(http_requests_total{status="200"}[5m])) by (handler)
','["전체 HTTP 요청의 처리 시간을 계산합니다.","상태 코드 200인 HTTP 요청의 처리 시간을 계산합니다.","handler별로 상태 코드 200인 HTTP 요청의 속도를 측정합니다.","각 핸들러 별로 모든 HTTP 요청의 속도를 측정합니다."]','{"correct":2}','UNDERSTAND',0.8,'["observability-metrics"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes의 ConfigMap을 정의합니다.

apiVersion: v1
kind: ConfigMap
metadata:
  name: my-configmap
data:
  CONFIG_KEY: value
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: config-deployment
spec:
  replicas: 2
  template:
    metadata:
      labels:
        app: config-app
    spec:
      containers:
      - name: my-config-container
        image: my-image:latest
        envFrom:
        - configMapRef:
            name: my-configmap','["ConfigMap의 데이터가 Deployment 템플릿에 올바르게 바인딩되어 환경 변수로 사용된다.","Deployment는 ConfigMap을 참조하지 않고, 따라서 환경 변수를 생성할 수 없다.","ConfigMap은 정상적으로 생성되지만, Deployment 템플릿에서 환경 변수가 제대로 설정되지 않는다.","ConfigMap의 데이터는 별도의 볼륨으로 마운트되어 애플리케이션에 접근한다."]','{"correct":0}','ANALYZE',0.8,'["kubernetes-configmap"]'),
('DEVOPS','CODE_READING','다음 Dockerfile은 이미지를 생성합니다.

FROM node:14-alpine
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
CMD ["node", "server.js"]
RUN npm run build
ENTRYPOINT ["npm", "start"]','["Dockerfile는 ''npm start'' 명령으로 애플리케이션을 시작합니다.","Dockerfile는 ''node server.js'' 명령으로 애플리케이션을 시작합니다.","Dockerfile는 ''npm run build'' 명령으로 애플리케이션을 빌드한 후 실행하지 않습니다.","Dockerfile는 이미지를 생성할 때 ''npm install''과 ''npm run build''를 모두 수행합니다."]','{"correct":1}','EVALUATE',0.8,'["docker-cmd-vs-entrypoint"]'),
('DEVOPS','CODE_READING','다음 Dockerfile은 멀티스테이지 빌드를 사용하여 애플리케이션을 빌드하고 배포합니다. 문제가 될 수 있는 부분은 무엇일까요?

FROM maven:3-jdk-11 AS build
COPY src /usr/src/myapp/
WORKDIR /usr/src/myapp
RUN mvn clean package -DskipTests
FROM openjdk:11-jre-slim
COPY --from=build /usr/src/myapp/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
','["빌드 이미지에서 테스트를 건너뛰는 설정이 잘못되었습니다.","배포 이미지에 JAR 파일을 복사하는 경로가 잘못되었습니다.","마운트된 볼륨이 사용되지 않았습니다.","Dockerfile의 레이어 캐시 최적화에 문제가 있습니다."]','{"correct":3}','APPLY',0.8,'["docker-multi-stage-build"]'),
('DEVOPS','CODE_READING','다음 Helm 템플릿은 Service를 정의합니다.

apiVersion: v1
kind: Service
metadata:
  name: example-service
spec:
  selector:
    app: example-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 9376','["Service는 ''example-app'' 라벨을 가진 Pod의 포트 9376에 트раффик을 전달합니다.","Service는 모든 Pod의 포트 80에 트래픽을 전달합니다.","Service는 ''example-app'' 라벨이 없는 Pod에 트래픽을 전달합니다.","Service는 포트 80에서 요청을 받지만, 실제 트래픽은 포트 9376으로 이동하지 않습니다."]','{"correct":0}','EVALUATE',0.8,'["kubernetes-service"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Secret을 어떻게 정의하고 있는가?

apiVersion: v1
kind: Secret
metadata:
  name: db-secret
data:
  password: cm9vdEBzcGVj
  username: YWRtaW4=','["Kubernetes ConfigMap를 생성한다.","Kubernetes Secret을 정의한다.","Kubernetes Deployment 리소스를 생성한다.","Kubernetes Service를 설정한다."]','{"correct":1}','REMEMBER',0.8,'["kubernetes-secret"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Ingress 리소스를 정의합니다.

apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
spec:
  rules:
  - host: example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: app-service
            port:
              number: 80','["Ingress는 정상적으로 동작하여 외부 요청을 내부 서비스로 리디렉션한다.","Ingress의 rule이 잘못 설정되어 외부 요청은 처리되지 않는다.","Ingress는 외부 요청을 받아들이지만, pathType 설정으로 인해 모든 경로가 무시된다.","Ingress는 정상적으로 동작하지만, 외부 호스트에 대한 SSL/TLS 설정이 필요하다."]','{"correct":0}','ANALYZE',0.8,'["kubernetes-ingress"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Pod을 정의합니다. 이 설정에서 문제가 발생할 수 있는 이유는 무엇일까요?

apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
spec:
  containers:
  - name: app-container
    image: myregistry/myimage:latest
    command: [''sh'', ''-c'']
    args: [''while true; do sleep 30; done'']','["command와 args가 잘못 설정되어 있습니다.","container의 이미지가 존재하지 않습니다.","Pod에서 사용 가능한 리소스가 부족합니다.","상태 확인 프로브(liveness/readiness)가 누락되었습니다."]','{"correct":3}','APPLY',0.9,'["kubernetes-liveness-readiness-probe"]'),
('DEVOPS','CODE_READING','다음 YAML 파일은 Kubernetes Secret을 정의합니다.

apiVersion: v1
kind: Secret
metadata:
  name: my-secret
type: Opaque
data:
  username: dmFsdWU=
  password: dmFsdWU=','["Secret은 암호화되어 저장되므로 안전하다.","Secret이 정상적으로 생성되지만, type 설정이 잘못되어 암호화되지 않는다.","Secret 데이터는 base64로 인코딩되어 있어 애플리케이션에서 직접 사용할 수 없다.","Secret은 정상적으로 생성되며, 애플리케이션이 이를 참조하여 환경 변수로 설정한다."]','{"correct":0}','ANALYZE',0.9,'["kubernetes-secret"]'),
('DEVOPS','CODE_READING','다음 Kubernetes YAML 파일은 무엇을 구성하고 있는가?

apiVersion: v1
kind: ConfigMap
metadata:
  name: my-configmap
  namespace: default
data:
  config.json: |
    {
      "app": "myapp",
      "version": "v1"
    }','["Kubernetes Secret을 생성한다.","Kubernetes Pod를 정의한다.","Kubernetes ConfigMap을 생성한다.","Kubernetes Deployment를 설정한다."]','{"correct":2}','REMEMBER',0.9,'["kubernetes-configmap"]'),
('FULLSTACK','MCQ','프론트-백 연동 시 실시간 통신을 구현할 때 가장 적합한 방식은 무엇인가요?','["XML-RPC를 사용한 요청 응답","WebSocket을 이용한 양방향 통신","REST API의 GET 요청 반복","HTTP POST 메서드를 이용한 데이터 전송"]','{"correct":1}','EVALUATE',0.2,'["front-back-real-time-concepts"]'),
('FULLSTACK','MCQ','데이터베이스 모델링 시 정규화와 비정규화의 선택을 할 때 고려해야 하는 주요 요소는 무엇인가요?','["성능 최적화를 위한 캐시 사용","트랜잭션 처리에 필요한 동시성 제어","DB 테이블 간 관계 구조","데이터 일관성을 유지하기 위한 트랜잭션 경계"]','{"correct":2}','EVALUATE',0.2,'["database-modeling-normalization-vs-denormalization"]'),
('FULLSTACK','MCQ','API 서버에서 가장 효과적인 성능 최적화 기법은 무엇인가요?','["모든 요청에 대해 동일한 처리 시간을 유지하기","캐싱을 사용하지 않고 모든 요청을 동기적으로 처리하기","불필요한 데이터를 제거하고 필요한 데이터만 전송하기","API 서버에서 로직을 복잡하게 구현하여 최적화 시도하기"]','{"correct":2}','ANALYZE',0.2,'["api-performance-optimization"]'),
('FULLSTACK','MCQ','JWT 토큰을 이용한 인증 과정에서 가장 중요한 보안 요소는 무엇인가요?','["토큰의 유효기간 설정","토큰 내 정보의 암호화","리프레시 토큰 회전","CORS 정책 설정"]','{"correct":1}','APPLY',0.2,'["jwt-authentication"]'),
('FULLSTACK','MCQ','데이터베이스에서 특정 사용자의 모든 게시물을 가져오려고 합니다. 가장 효율적인 방법은 무엇인가요?','["SELECT * FROM posts WHERE user_id = ?","SELECT post_id, content FROM posts WHERE user_id = ?","INNER JOIN users ON posts.user_id = users.id WHERE users.name = ?","LEFT JOIN comments ON posts.post_id = comments.post_id WHERE user_id = ?"]','{"correct":1}','APPLY',0.2,'["sql-query-optimization"]'),
('FULLSTACK','MCQ','데이터베이스 마이그레이션을 수행할 때, 어떤 단계가 가장 먼저 이루어져야 하는가요?','["테이블 생성 스크립트 작성","이전 상태 백업","변경사항 확인","마이그레이션 스크립트 실행"]','{"correct":1}','APPLY',0.2,'["database-migrations"]'),
('FULLSTACK','MCQ','다음 중 RESTful API 설계에서 가장 중요한 원칙은 무엇인가요?','["API의 모든 자원이 고유한 URL을 가지는 것","HTTP 메서드의 의미를 무시하고 사용하는 것","API 버전을 변경할 때마다 모든 클라이언트에게 알리지 않는 것","페이지네이션을 구현하지 않고 전체 데이터를 반환하는 것"]','{"correct":0}','ANALYZE',0.2,'["restful-api-design"]'),
('FULLSTACK','MCQ','백엔드에서 사용자의 로그인 상태를 확인할 때, 세션을 사용하는 대신 무엇을 권장합니까?','["쿠키","세션 스토리지","로컬 스토리지","토큰 기반 인증"]','{"correct":3}','APPLY',0.2,'["session-vs-token-authentication"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드 API를 호출할 때 가장 안전한 방법은 무엇인가?','["CORS 설정을 무시한 채 요청하기","백엔드 서버와 프론트엔드 사이의 CORS 설정 구현","API 키 사용하지 않고 접근하려 시도","웹 소켓만으로 모든 통신 수행"]','{"correct":1}','EVALUATE',0.4,'["cors-configuration"]'),
('FULLSTACK','MCQ','API 배포 시 무중단 배포를 위한 주요 기술은 무엇인가요?','["서비스 메시지 라우팅","트래픽 분할 및 스위치 오버","데이터베이스 백업 복구","웹 서버 설정 변경"]','{"correct":1}','APPLY',0.4,'["zero-downtime-deployment"]'),
('FULLSTACK','MCQ','다음 API 설계 방안 중 가장 효율적인 것은 무엇인가?','["매 요청마다 모든 데이터를 반환하는 방식","필요한 데이터만을 포함하는 응답 반환","데이터의 전체 범위를 한 번에 전송","페이지네이션을 사용하지 않은 채 무한 스크롤"]','{"correct":1}','EVALUATE',0.4,'["rest-api-design"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드와 통신할 때 가장 안전하게 세션 관리를 처리하는 방법은 무엇인가요?','["HTTP 상태 코드를 사용하여 세션을 관리합니다.","세션 쿠키를 클라이언트에 저장하고 이를 서버에서 확인합니다.","JWT 토큰을 사용하여 클라이언트와 서버 간의 자격 증명을 전달합니다.","클라이언트 측 세션 ID를 사용하여 서버와 통신합니다."]','{"correct":2}','ANALYZE',0.4,'["authentication-authorization"]'),
('FULLSTACK','MCQ','데이터베이스 설계 시 어떤 상황에서 비정규화(normalization)보다 정규화(normal form)를 선호해야 하는가?','["데이터의 일관성이 중요한 경우","속도와 가용성 요구사항이 높은 경우","복잡한 계층 구조가 필요한 경우","다중 연결 관계가 있는 데이터 모델링 시"]','{"correct":1}','UNDERSTAND',0.4,'["database-normalization"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드로 데이터 요청을 할 때 클라이언트 사이드 캐시 사용은 어떤 이점을 가져옵니다.','["API 호출 수를 줄이고 응답 시간을 단축할 수 있습니다","서버의 부하를 증가시키며 성능이 저하됩니다","데이터 일관성이 향상되어 데이터베이스에 직접 접근해야 합니다","클라이언트와 서버 사이의 통신량을 늘리게 됩니다"]','{"correct":0}','APPLY',0.4,'["client-side-caching"]'),
('FULLSTACK','MCQ','프론트-백 연동에서 CORS(Cross-Origin Resource Sharing) 정책을 사용하는 이유는?','["데이터 암호화","리소스 공유 제한","세션 관리","인터셉터 활용"]','{"correct":1}','REMEMBER',0.4,'["cors-policy"]'),
('FULLSTACK','MCQ','HTTP 상태 코드 201 Created는 언제 사용해야 하나?','["데이터 읽기 요청 성공 시","데이터 수정 요청 성공 시","새 리소스 생성 요청 성공 시","리소스 삭제 요청 성공 시"]','{"correct":2}','UNDERSTAND',0.4,'["http-status-code"]'),
('FULLSTACK','MCQ','데이터베이스 모델링에서 어떤 유형의 인덱싱은 정확도와 성능 사이의 트레이드오프를 제공한다.','["UNIQUE 인덱스","CLUSTERED 인덱스","NONCLUSTERED 인덱스","FULLTEXT 인덱스"]','{"correct":2}','REMEMBER',0.4,'["database-modeling"]'),
('FULLSTACK','MCQ','다음 중 REST API 설계 시 가장 중요한 원칙은 무엇인가요?','["API의 단순성과 일관성을 유지한다.","API를 너무 세세하게 나누어 설계한다.","모든 요청에 대한 응답 형식을 다르게 만든다.","API 버전을 매번 변경하여 업데이트한다."]','{"correct":0}','UNDERSTAND',0.4,'["rest-api-design"]'),
('FULLSTACK','MCQ','OAuth2 흐름에서 액세스 토큰은 어떤 역할을 한다.','["사용자의 비밀번호를 확인한다.","사용자의 자격 증명을 저장한다.","API에 대한 접근 권한을 인증한다.","데이터베이스의 사용자 정보를 업데이트한다."]','{"correct":2}','REMEMBER',0.4,'["oauth2"]'),
('FULLSTACK','MCQ','프론트-백 연동에서 서버 측에 요청이 들어왔을 때, 클라이언트가 응답을 받지 못하면 어떤 상태 코드를 반환해야 하는가.','["200 OK","401 Unauthorized","500 Internal Server Error","408 Request Timeout"]','{"correct":3}','REMEMBER',0.4,'["frontend-backend-integration"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드 API 호출 시 가장 좋은 에러 핸들링 방법은 무엇인가요?','["서버 오류만 처리하고 클라이언트 오류는 무시하기","모든 HTTP 상태 코드에 대해 동일한 응답 메커니즘 사용하기","API 서버에서 발생하는 모든 오류를 자세히 설명하여 클라이언트에게 전달하기","HTTP 4xx 에러에 대한 자세한 정보 제공하고 5xx 에러는 기본적인 메시지로 처리하기"]','{"correct":3}','ANALYZE',0.4,'["error-handling-in-apis"]'),
('FULLSTACK','MCQ','프론트엔드에서 페이지 네이션을 구현하는 가장 일반적인 방법은?','["무한 스크롤 기능을 이용한다","페이지 번호를 클릭하여 데이터를 가져온다","각 페이지별로 고유한 URL을 생성한다","DB에서 전체 데이터를 받아서 로컬에서 필터링한다"]','{"correct":1}','UNDERSTAND',0.4,'["pagination-frontend-implementations"]'),
('FULLSTACK','MCQ','정규화 데이터베이스 설계에서 3NF(제3규범)를 적용하면 어떤 이점이 있나?','["불필요한 중복 제거","속도 향상","쿼리 단순화","사용자 접근성 개선"]','{"correct":0}','UNDERSTAND',0.4,'["database-normalization"]'),
('FULLSTACK','MCQ','JWT (JSON Web Token)의 J는 무엇을 의미하는가?','["JavaScript","Java","JellyBean","JSON"]','{"correct":3}','REMEMBER',0.4,'["jwt-authentication"]'),
('FULLSTACK','MCQ','REST API 설계 시 HTTP 메서드 중 POST는 어떤 동작을 수행하는가?','["자원의 상태를 조회한다.","새로운 자원을 생성한다.","특정 자원을 수정한다.","기존 자원을 삭제한다."]','{"correct":1}','REMEMBER',0.4,'["rest-api-design"]'),
('FULLSTACK','MCQ','다음 인증 방법 중 가장 보안성이 높은 것은 무엇인가?','["세션 기반 인증","HTTP 헤더를 통한 JWT 전송","쿼리 스트링을 통한 JWT 전송","클라이언트 쿠키 저장"]','{"correct":1}','EVALUATE',0.4,'["authentication-authorization"]'),
('FULLSTACK','MCQ','리소스 경쟁 상황에서 가장 안정적인 해결 방법은?','["락(Lock)을 사용한다","메모리를 많이 사용한다","동시성 문제를 무시한다","다른 리소스로 작업을 분산한다"]','{"correct":0}','UNDERSTAND',0.6,'["concurrency-locking"]'),
('FULLSTACK','MCQ','서비스의 성능을 최적화하기 위한 가장 효과적인 방법은 무엇인가?','["불필요한 API 호출 최소화","데이터베이스 쿼리 최적화만 진행","클라이언트 캐싱 비활성화","서버 캐시를 사용하지 않음"]','{"correct":0}','EVALUATE',0.6,'["performance-optimization"]'),
('FULLSTACK','MCQ','프론트엔드에서 서버 API 호출이 실패할 때 가장 좋은 UX 처리 방법은?','["백그라운드 로딩 표시","즉시 오류 메시지 출력","자동 재요청","사용자에게 선택권 제공"]','{"correct":3}','UNDERSTAND',0.6,'["frontend-api-interaction"]'),
('FULLSTACK','MCQ','JWT 토큰 인증에서 리프레시 토큰은 어떤 역할을 하는가?','["액세스 토큰 대체","사용자 정보 확인","토큰 유효성 검사","비밀번호 재설정"]','{"correct":0}','UNDERSTAND',0.6,'["jwt-authentication"]'),
('FULLSTACK','MCQ','데이터베이스 트랜잭션의 ACID 속성 중 ''A''는 무엇을 의미하는가?','["Atomicity","Consistency","Isolation","Durability"]','{"correct":0}','REMEMBER',0.6,'["database-transaction-acid"]'),
('FULLSTACK','MCQ','API 설계 시 페이지네이션 기법의 주요 목적은 무엇인가요?','["HTTP 요청 수를 줄이는 것","응답 시간을 최소화하는 것","클라이언트 측 메모리 사용량 감소","데이터 검색 효율성 향상"]','{"correct":1}','EVALUATE',0.6,'["api-design-pagination"]'),
('FULLSTACK','MCQ','클라이언트-서버 인증 흐름에서 가장 안전한 방법은 무엇인가요?','["세션 기반 인증 사용하기","JWT 토큰을 클라이언트에 저장하지 않고 서버만 사용하기","토큰 만료 시간이 짧은 JWT를 사용하여 자주 새로 고침하도록 하기","프론트엔드에서 암호를 보관하고 직접 로그인 요청 보내기"]','{"correct":2}','ANALYZE',0.6,'["authentication-authorization"]'),
('FULLSTACK','MCQ','데이터베이스 모델링에서 정규화와 비정규화의 주요 차이는 무엇인가요?','["데이터 중복을 줄이는 것과 향상된 성능을 추구하는 것","데이터 일관성을 유지하는 것과 데이터 저장 공간을 절약하는 것","데이터 조회 속도를 높이는 것과 데이터 수정 복잡성을 늘리는 것","데이터 무결성 보장와 데이터 검색 성능 향상"]','{"correct":0}','APPLY',0.6,'["database-normalization"]'),
('FULLSTACK','MCQ','서버 캐싱을 사용하면 어떤 성능 개선 효과가 있는가?','["데이터베이스 접근 빈도 증가","응답 시간 감소","SQL 쿼리 복잡성 증가","HTTP 요청 수 증가"]','{"correct":1}','UNDERSTAND',0.6,'["server-side-caching"]'),
('FULLSTACK','MCQ','API의 성능 최적화를 위해 어떤 기술이 가장 효과적인가요?','["클라이언트 측 캐싱","서버 사이드 캐싱","데이터베이스 인덱스 사용","모든 요청에 대한 로직 단순화"]','{"correct":1}','UNDERSTAND',0.6,'["api-performance-optimization"]'),
('FULLSTACK','MCQ','웹 애플리케이션에서 사용자 세션이 저장되는 방법으로 가장 일반적으로 사용되는 것은?','["Database Session Store","Cookie-based Session Store","Memory-based Session Store","File System Session Store"]','{"correct":1}','REMEMBER',0.6,'["session-management"]'),
('FULLSTACK','MCQ','배포 과정에서 무중단 배포를 달성하기 위해 어떤 접근법을 사용할 수 있나요?','["전체 서버를 단일 배치로 업데이트합니다.","수동으로 모든 서비스를 순차적으로 업데이트합니다.","병렬로 여러 버전의 애플리케이션을 실행하고 트래픽을 점진적으로 이동시킵니다.","배포 후 전체 시스템을 재기동하여 변경사항 적용합니다."]','{"correct":2}','ANALYZE',0.6,'["deployment-strategies"]'),
('FULLSTACK','MCQ','데이터베이스 트랜잭션(Transaction)의 주요 특징 중 하나는?','["비동기 처리","불변성","원자성","결합성"]','{"correct":2}','REMEMBER',0.6,'["database-transactions"]'),
('FULLSTACK','MCQ','다음 중 REST API 설계 시 가장 중요한 고려사항은 무엇인가요?','["API의 효율적인 성능 최적화","HTTP 메서드와 상태 코드를 의미 있게 사용","사용자의 인터페이스 경험 개선","API 버전 관리 및 호환성"]','{"correct":1}','APPLY',0.6,'["rest-api-design"]'),
('FULLSTACK','MCQ','REST API 설계 시, 자원 모델링의 가장 중요한 원칙 중 하나는 무엇인가.','["API에 대한 모든 요청을 단일 엔드포인트로 처리한다.","HTTP 메서드를 사용하여 리소스 동작을 정의한다.","비정규화 데이터 구조를 유지한다.","데이터베이스 트랜잭션을 자주 사용한다."]','{"correct":1}','REMEMBER',0.6,'["rest-api-design"]'),
('FULLSTACK','MCQ','REST API 설계 시 HTTP 메서드 중 데이터를 읽기 위한 요청을 나타내는 것은?','["POST","PUT","GET","DELETE"]','{"correct":2}','REMEMBER',0.6,'["rest-api-design"]'),
('FULLSTACK','MCQ','API 버전 관리를 위해 가장 흔히 사용되는 방식은?','["서브도메인 기반 (v1.example.com)","쿼리 파라미터 기반 (/api/v1/resource)","헤더 기반 (X-API-Version)","path 인자 기반 (/v1/api/resource)"]','{"correct":2}','REMEMBER',0.6,'["api-versioning"]'),
('FULLSTACK','MCQ','데이터베이스 모델링에서 트랜잭션 경계를 잘못 설정하면 어떤 문제가 발생할 수 있나요?','["데이터 무결성 위반","데이터의 일관성이 향상됨","응답 시간이 크게 줄어듬","캐시 효율성 증가"]','{"correct":0}','ANALYZE',0.6,'["database-modeling-transactions"]'),
('FULLSTACK','MCQ','프론트엔드에서 캐싱 전략을 구현할 때, 가장 효율적인 HTTP 요청 메서드는 무엇인가요?','["GET","POST","PUT","DELETE"]','{"correct":0}','APPLY',0.6,'["http-caching-strategies"]'),
('FULLSTACK','MCQ','다음 REST API 설계 중 가장 좋은 것은?','["/users/{id}/posts","/post/user/id","/user/posts?id=123","/users/posts"]','{"correct":0}','UNDERSTAND',0.6,'["rest-api-design"]'),
('FULLSTACK','MCQ','JWT 토큰 인증에서 클라이언트가 서버에 요청을 보낼 때 어떤 정보를 포함해야 하는가?','["세션 ID만","쿠키 값","토큰 자체","사용자 이름과 비밀번호"]','{"correct":2}','UNDERSTAND',0.8,'["jwt-authentication"]'),
('FULLSTACK','MCQ','다음 REST API 설계에서 가장 좋은 품질 표준은 무엇인가요?','["API 버전을 요청 URL에 포함하기","HTTP 상태 코드를 사용하지 않기","매개변수의 모든 오류를 400 Bad Request로 처리하기","응답 본문에 에러 세부 정보를 제공하지 않기"]','{"correct":0}','ANALYZE',0.8,'["rest-api-design"]'),
('FULLSTACK','MCQ','배포 프로세스를 개선하기 위한 가장 효과적인 방법은 무엇인가?','["변경사항 없음에도 배포 수행","CI/CD 파이프라인 자동화","수작업을 통한 배포","배포 프로세스의 문서화만"]','{"correct":1}','EVALUATE',0.8,'["continuous-integration-delivery"]'),
('FULLSTACK','MCQ','환경 분리를 통해 개발자에게 가장 유익한 이점은 무엇인가요?','["모든 환경에서 동일한 설정 사용하기","개발, 테스트, 운영 각 단계별로 독립된 환경 설정 유지하기","운영 환경만 최적화하고 개발/테스트 환경에서는 그대로 두기","환경 변경을 자주 적용하여 코드의 최신 버전을 유지하기"]','{"correct":1}','ANALYZE',0.8,'["environment-separation"]'),
('FULLSTACK','MCQ','프론트엔드에서 API를 호출할 때 가장 일반적으로 사용되는 HTTP 상태 코드는?','["201 CREATED","404 NOT FOUND","500 INTERNAL SERVER ERROR","200 OK"]','{"correct":3}','REMEMBER',0.8,'["http-status-codes"]'),
('FULLSTACK','MCQ','API 호출 시 클라이언트에서 서버로 데이터를 전송하는 가장 효율적인 HTTP 메서드는?','["GET","POST","PUT","PATCH"]','{"correct":1}','UNDERSTAND',0.8,'["http-methods-post"]'),
('FULLSTACK','MCQ','JWT 토큰의 주요 특징 중 하나는?','["세션 기반","비동기 통신","상태 없는","서버 사이드 렌더링"]','{"correct":2}','REMEMBER',0.8,'["jwt-authentication"]'),
('FULLSTACK','MCQ','데이터베이스 마이그레이션의 목적이 무엇인가.','["데이터베이스 스키마를 변경하거나 업데이트한다.","데이터베이스에서 데이터를 삭제한다.","데이터베이스 연결을 테스트한다.","데이터베이스 성능을 최적화한다."]','{"correct":0}','REMEMBER',0.8,'["database-migration"]'),
('FULLSTACK','MCQ','API 설계 시 REST 자원 모델링을 진행할 때 가장 중요한 고려사항은 무엇인가요?','["API의 성능 최적화","API와 클라이언트 간 계약(contract) 정합성","클라이언트 측 코드의 복잡성 감소","서버 측 로직 구현의 용이함"]','{"correct":1}','EVALUATE',0.8,'["api-design-restful-contract"]'),
('FULLSTACK','MCQ','인증 및 인가 처리 시 세션 기반 접근과 토큰(JWT) 기반 접근의 주요 차이는 무엇인가요?','["세션이 보안 위험을 더 낮추는 것","토큰이 사용자 경험을 개선하는 것","세션이 상태를 유지해야 하는 서버 부담을 증가시키는 것","토큰이 클라이언트 측 코드 복잡성을 늘리는 것"]','{"correct":2}','EVALUATE',0.8,'["authentication-session-vs-token-jwt"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드 API를 호출할 때 서버의 응답을 최적화하기 위한 가장 효과적인 방법은 무엇인가요?','["API 요청마다 모든 데이터를 반환합니다.","페이지네이션과 캐싱을 사용하여 효율성을 개선합니다.","HTTP 상태 코드를 무시하고 항상 성공으로 처리합니다.","클라이언트에서 직접 응답을 수정하여 최적화합니다."]','{"correct":1}','ANALYZE',0.8,'["front-end-api-optimization"]'),
('FULLSTACK','MCQ','API 테스트에서 계약 테스트가 주로 어떤 상황에서 사용되는가요?','["서비스 간의 호환성을 확인하는 경우","클라이언트 코드의 성능을 개선하기 위해","서버 측 로직 구현을 검증할 때","사용자 인터페이스의 테스트를 진행할 때"]','{"correct":0}','APPLY',0.8,'["contract-testing"]'),
('FULLSTACK','MCQ','프론트엔드에서 백엔드 API 호출 시 어떤 HTTP 메서드를 사용하여 리소스를 읽어올까요?','["GET","PATCH","OPTIONS","HEAD"]','{"correct":0}','UNDERSTAND',0.8,'["http-methods"]'),
('FULLSTACK','MCQ','JWT 토큰 인증을 구현할 때, 클라이언트가 서버에 액세스하기 위해 보내야 하는 HTTP 요청 헤더는 무엇인가요?','["Authorization: Basic auth-token","Authorization: Bearer jwt-token","X-Auth-Token: jwt-token","Cookie: token=jwt-token"]','{"correct":1}','APPLY',0.8,'["jwt-authentication"]'),
('FULLSTACK','MCQ','다음 SQL 쿼리의 목적은 무엇인가? SELECT * FROM users WHERE email = ''user@example.com'' AND verified_at IS NOT NULL;','["비활성화된 사용자 정보 가져오기","활성화된 사용자의 이메일 확인","사용자의 비밀번호 변경","사용자가 로그인한 횟수 카운트"]','{"correct":1}','UNDERSTAND',0.8,'["sql-query-verification"]'),
('FULLSTACK','MCQ','JWT 토큰이 만료되었을 때 프론트엔드에서 처리할 수 있는 방법은?','["새로 로그인하라는 메시지를 띄운다","프론트에서 JWT 리프레시 토큰을 사용하여 새 액세스 토큰을 발급한다","백엔드에 요청해 세션 정보를 재생성한다","현재 페이지는 유지하고 다른 모든 페이지에는 로그인 화면으로 이동한다"]','{"correct":1}','UNDERSTAND',0.8,'["jwt-refresh-token"]'),
('FULLSTACK','MCQ','프론트엔드에서 API 호출 시 동작하는 HTTP 상태 코드를 선택하세요.','["200 OK","302 Found","401 Unauthorized","500 Internal Server Error"]','{"correct":0}','APPLY',0.8,'["http-status-codes"]'),
('FULLSTACK','MCQ','API 테스트에서 가장 중요한 부분은 무엇인가요?','["단위 테스트만 수행합니다.","통합 테스트를 주로 사용하여 모든 기능을 검증합니다.","계약 테스트를 통해 API의 변경에 따른 영향을 확인합니다.","E2E 테스트만으로 전체 시스템을 검사합니다."]','{"correct":2}','ANALYZE',0.9,'["api-testing-contract"]'),
('FULLSTACK','MCQ','데이터베이스 설계에서 가장 효율적인 인덱싱 전략은 무엇인가요?','["모든 열에 대해 인덱스 생성하기","주로 검색되는 열만 선택적으로 인덱싱 하기","최대한 많은 테이블을 정규화하여 관리하기","비정규화된 데이터를 레디메이드 쿼리를 위해 미리 준비하기"]','{"correct":1}','ANALYZE',0.9,'["database-indexing"]'),
('FULLSTACK','MCQ','데이터베이스 설계 시 정규화(normalization)의 목표는 무엇인가?','["복잡한 쿼리 최적화","데이터 무결성 유지","속도 향상","사용자 경험 개선"]','{"correct":1}','REMEMBER',0.9,'["database-normalization"]'),
('FULLSTACK','MCQ','프론트엔드에서 데이터베이스 쿼리를 직접 실행하지 않기 위해 사용되는 패턴은?','["CRUD API","Event-Driven Architecture","Serverless Functions","Direct Database Access"]','{"correct":0}','REMEMBER',0.9,'["rest-api-design"]'),
('FULLSTACK','MCQ','성능 최적화 시 캐시 계층 구조를 설계할 때 가장 중요한 고려사항은 무엇인가요?','["캐시 크기의 증가로 인한 성능 개선","브라우저와 서버 사이의 통신 줄이기","데이터 무효화 전략","모든 쿼리에 대한 캐싱 적용"]','{"correct":2}','EVALUATE',0.9,'["performance-caching-strategy"]'),
('FULLSTACK','MCQ','HTTP 상태 코드 중 200 OK를 의미하는 것은?','["100 Continue","200 OK","304 Not Modified","404 Not Found"]','{"correct":1}','REMEMBER',0.9,'["http-status-codes"]'),
('FULLSTACK','MCQ','데이터베이스 트랜잭션을 관리하는 가장 안전한 방법은 무엇인가?','["트랜잭션이 없는 상태에서 예외 처리","모든 CRUD 작업에 대해 트랜잭션 감싸기","특정 시나리오만 트랜잭션 사용","데이터베이스 커넥션 공유"]','{"correct":1}','EVALUATE',0.9,'["database-transaction-management"]'),
('FULLSTACK','CODE_READING','다음은 클라이언트가 서버로 POST 요청을 보낼 때 서버에서 처리하는 핸들러입니다.

@PostMapping("/items")
public Item createItem(@RequestBody Item item) {
  return itemRepository.save(item);
}

서버는 클라이언트로부터 받은 데이터를 DB에 저장합니다.','["API는 POST 요청으로 새 항목을 생성합니다.","API는 PUT 요청으로 기존 항목을 업데이트합니다.","API는 DELETE 요청으로 항목을 삭제합니다.","API는 GET 요청으로 항목 정보를 가져옵니다."]','{"correct":0}','UNDERSTAND',0.2,'["rest-api-design"]'),
('FULLSTACK','CODE_READING','다음 코드 스니펫은 JWT 토큰 생성 및 발급에 관한 것입니다.

public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
            .setClaims(createClaims(userDetails))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
}','["JWT 토큰을 생성합니다.","사용자의 세션을 만듭니다.","HTTP 요청에 대한 응답 헤더를 설정합니다.","JWT 토큰의 유효성을 검증합니다."]','{"correct":0}','REMEMBER',0.2,'["jwt-authentication"]'),
('FULLSTACK','CODE_READING','다음 JavaScript 코드는 프론트엔드에서 API 호출을 수행합니다.

fetch(''/api/user'', { method: ''GET'' })
.then(response => response.json())
.catch(error => console.log(''Error:'', error));

이 코드의 문제점은 무엇인가요?
','["API 호출 시 오류 처리를 하지 않는다.","response 객체를 json으로 변환하지 않는다.","fetch 메서드는 GET 요청을 지원하지 않는다.","API 경로가 잘못되어 응답을 받지 못한다."]','{"correct":0}','EVALUATE',0.4,'["rest-api-contract"]'),
('FULLSTACK','CODE_READING','다음 Node.js 코드 스니펫은 서버 측의 CORS 설정을 정의하고 있습니다.

app.use((req, res, next) => {
    res.header(''Access-Control-Allow-Origin'', ''*'');
    res.header(''Access-Control-Allow-Methods'', ''GET, POST, PUT, DELETE, OPTIONS'');
    next();
});','["서버에서 모든 메소드에 대한 CORS 헤더를 설정합니다.","HTTP 요청을 처리하는 핸들러를 만듭니다.","사용자의 세션 정보를 검증합니다.","응답의 상태 코드를 변경합니다."]','{"correct":0}','REMEMBER',0.4,'["cors-configuration"]'),
('FULLSTACK','CODE_READING','다음 코드 스니펫은 MySQL에서 데이터베이스 트랜잭션을 처리하는데 사용됩니다.

@Transactional
public void updateUserData(User user) {
    user.setLastLogin(new Date());
    userRepository.save(user);
}','["트랜잭션 안에서 사용자 정보를 업데이트합니다.","사용자의 모든 데이터를 삭제합니다.","트랜잭션이 실패할 경우 롤백됩니다.","HTTP 요청을 처리합니다."]','{"correct":0}','REMEMBER',0.4,'["database-transaction"]'),
('FULLSTACK','CODE_READING','다음 코드는 PostgreSQL 데이터베이스에서 데이터를 쿼리하는 SQL 문장입니다.

SELECT * FROM users WHERE email = ''user@example.com'' AND status = ''active'';

이 쿼리는 어떤 문제점을 가지고 있나요?
','["쿼리가 비정규화된 데이터베이스에 적합하지 않다.","email 필드와 status 필드를 인덱싱해야 한다.","users 테이블에서 모든 컬럼을 가져오는 것이 비효율적이다.","WHERE 절에서 email만 검색하는 것이 더 효율적이다."]','{"correct":1}','EVALUATE',0.4,'["database-indexing"]'),
('FULLSTACK','CODE_READING','다음 코드는 클라이언트 측에서 REST API를 호출하는 JavaScript 코드입니다.

fetch(''/api/users'', {
  method: ''GET'',
  headers: {''Authorization'': `Bearer ${token}`} 
})
.then(response => response.json())
.catch(error => console.error(''Error:'', error));','["클라이언트는 GET 요청으로 사용자 정보를 가져옵니다.","클라이언트는 POST 요청으로 새로운 사용자를 생성합니다.","클라이언트는 DELETE 요청으로 사용자를 삭제합니다.","클라이언트는 PUT 요청으로 사용자 정보를 업데이트합니다."]','{"correct":0}','UNDERSTAND',0.4,'["rest-api-consumption"]'),
('FULLSTACK','CODE_READING','다음 코드는 서버에서 클라이언트에게 데이터를 반환하는 API 핸들러입니다.

@GetMapping("/users")
public List<User> getUsers() {
    return userRepository.findAll();
}

userRepository.findAll() 메서드는 모든 사용자 정보를 가져옵니다.','["API는 GET 요청에 대해 200 OK 상태 코드와 데이터를 반환합니다.","API는 POST 요청에 대해 405 Method Not Allowed 상태 코드를 반환합니다.","API는 GET 요청에 대해 500 Internal Server Error 상태 코드를 반환합니다.","API는 모든 사용자 정보를 DB에서 가져오지만 클라이언트에게 아무 것도 전송하지 않습니다."]','{"correct":0}','UNDERSTAND',0.4,'["rest-api-design"]'),
('FULLSTACK','CODE_READING','다음 JavaScript 코드는 프론트엔드에서 WebSocket을 사용하여 실시간 데이터를 전송합니다.

const socket = new WebSocket(''ws://localhost:8080/socket'');
someFunction() {
    socket.send(JSON.stringify(data));
}

이 코드의 문제점은 무엇인가요?
','["WebSocket 연결을 브라우저에서 지원하지 않는다.","JSON.stringify 메서드를 사용하여 데이터를 직렬화하지 않았다.","데이터 전송 후 서버로부터 응답을 받지 못한다.","WebSocket 연결이 정상적으로 설정되지 않아 연결이 실패할 수 있다."]','{"correct":3}','EVALUATE',0.4,'["websocket-realtime"]'),
('FULLSTACK','CODE_READING','다음은 JWT를 이용한 인증 토큰을 발급하는 API의 일부입니다. 

@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody UserCredentials userCreds) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userCreds.getUsername(), userCreds.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenService.generateJwt(authentication);
    return ResponseEntity.ok(Map.of("token", jwt));
}','["1. JWT 토큰을 생성하지 않습니다.","2. 세션 기반 인증 방식을 사용합니다.","3. 성공시 JWT 토큰을 반환합니다.","4. 실패할 경우 500 Internal Server Error를 발생시킵lke."]','{"correct":2}','ANALYZE',0.6,'["jwt-authentication"]'),
('FULLSTACK','CODE_READING','다음 코드가 Spring Boot에서 사용되는 @Transactional 어노테이션을 사용하여 트랜잭션 관리를 구현하고 있습니다.

@Transactional(rollbackFor = Exception.class)
public void saveUser(User user) {
    userRepository.save(user);
}

이 코드의 문제점은 무엇인가요?
','["트랜잭션이 예외 발생 시 롤백되지 않는다.","메서드명에서 saveUser 대신 save를 사용해야 한다.","userRepository에 대한 의존성 주입이 누락되었다.","@Transactional 어노테이션을 사용했지만, 예외가 발생해도 항상 롤백된다."]','{"correct":0}','EVALUATE',0.6,'["spring-transaction"]'),
('FULLSTACK','CODE_READING','다음은 데이터베이스 트랜잭션을 처리하는 코드 스니펫입니다. 

@Transactional
public void updateProduct(int productId, String newDescription) {
    Product product = productService.findById(productId);
    if (product != null) {
        product.setDescription(newDescription);
    }
}','["1. 트랜잭션 범위 내에서 제품 정보를 업데이트합니다.","2. 트랜잭션 범위 밖에서 제품 정보를 업데이트합니다.","3. 항상 성공적으로 완료됩니다.","4. 예외 발생시 변경 사항을 롤백하지 않습니다."]','{"correct":0}','ANALYZE',0.6,'["spring-transaction"]'),
('FULLSTACK','CODE_READING','다음 코드는 REST API에서 사용자 정보를 가져오는 메소드입니다. 

@GetMapping("/users/{userId}")
public User getUser(@PathVariable String userId) {
    User user = userService.findUserById(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    return user;
}','["1. 404 NOT FOUND 상태 코드를 반환합니다.","2. 사용자 정보가 존재하지 않을 때 예외가 발생합니다.","3. 사용자 정보가 없을 경우 500 Internal Server Error 상태 코드를 반환합니다.","4. 항상 사용자 정보를 정상적으로 반환합니다."]','{"correct":1}','ANALYZE',0.6,'["rest-api-design"]'),
('FULLSTACK','CODE_READING','다음 Java 코드 스니펫은 데이터베이스 마이그레이션의 일부입니다.

public class CreateUsersTable extends AbstractMigration {
    @Override
    public void up() {
        createTable("users", table -> table
            .addColumn("id", INTEGER)
            .addColumn("username", VARCHAR)
            .addColumn("password", VARCHAR));
    }
}','["사용자 테이블을 생성합니다.","사용자의 로그인 세션을 만듭니다.","HTTP 요청의 응답 상태 코드를 설정합니다.","데이터베이스 트랜잭션을 시작합니다."]','{"correct":0}','REMEMBER',0.6,'["database-migration"]'),
('FULLSTACK','CODE_READING','다음은 배포 환경에서 헬스 체크를 수행하는 코드 스니펫입니다. 

@GetMapping("/actuator/health")
public Health health() {
    return new Health.Builder()
            .up()
            .withDetail("database", "connected")
            .build();
}','["1. 배포 환경에서 데이터베이스 연결 상태를 확인하지 않습니다.","2. 항상 ''DOWN'' 상태를 반환합니다.","3. 헬스 체크 결과가 JSON 형식으로 반환됩니다.","4. ''database''와 같은 세부 정보는 없을 수 있습니다."]','{"correct":0}','ANALYZE',0.6,'["health-check-deployment"]'),
('FULLSTACK','CODE_READING','다음 Java 코드는 Spring Boot에서 사용되는 @RequestMapping 어노테이션을 사용하여 REST API를 정의하고 있습니다.

@RequestMapping(value = "/api", method = RequestMethod.GET)
public String getApi() { return "Hello, World!"; }

이 코드의 문제점은 무엇인가요?
','["@RequestMapping 어노테이션에서 GET 메서드를 지원하지 않는다.","punic String 대신 public ResponseEntity<String>을 사용해야 한다.","\"Hello, World!\" 대신 JSON 형태로 반환해야 한다.","API 경로가 잘못되어 요청을 받지 못한다."]','{"correct":1}','EVALUATE',0.6,'["rest-api-contract"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 이메일 주소 변경을 처리하는 API 입니다.

@PutMapping("/profile/email")
public ResponseEntity<Void> updateEmail(@RequestParam String userId, @RequestBody EmailDTO email) {
    User user = userRepository.findByUserId(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    // 이메일 변경 로직
    return ResponseEntity.ok().build();
}

코드를 분석하고, 해당 API의 문제점을 지적하세요.','["사용자의 ID가 존재하지 않을 경우 적절한 HTTP 상태 코드를 반환하지 않습니다.","이메일 변경 후 사용자의 데이터베이스 정보를 갱신하지 않습니다.","변경된 이메일 주소를 검증하는 로직이 필요합니다.","위 모든 문제점이 있습니다."]','{"correct":3}','APPLY',0.6,'["api-design","email-update"]'),
('FULLSTACK','CODE_READING','다음은 사용자 로그인 정보를 데이터베이스에서 검색하는 SQL 쿼리입니다.

SELECT * FROM users WHERE username = ? AND password = PASSWORD(?)

WHERE 절의 비밀번호 필드는 해시 함수로 처리됩니다.','["쿼리는 인증된 사용자에게 로그인 세션을 생성합니다.","쿼리는 데이터베이스에서 사용자의 암호 해시를 검색합니다.","쿼리는 사용자가 입력한 패스워드와 일치하는지 확인합니다.","쿼리는 비밀번호 필드에 저장된 원문 암호를 반환합니다."]','{"correct":1}','UNDERSTAND',0.6,'["sql-authentication"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 프로필 정보를 가져오는 API 입니다.

@GetMapping("/profile")
public UserDTO getUserProfile(@RequestParam String userId) {
    User user = userRepository.findByUserId(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    return modelMapper.map(user, UserDTO.class);
}

userRepository.findByUserId() 메서드가 존재하지 않는 경우에 대한 처리를 추가해야 합니다.','["@GetMapping(\"/profile\")\npublic UserDTO getUserProfile(@RequestParam String userId) {\n    User user = userRepository.findByUserId(userId);\n    if (user == null) {\n        throw new ResourceNotFoundException();\n    }\n    return modelMapper.map(user, UserDTO.class);\n}","@GetMapping(\"/profile\")\npublic UserDTO getUserProfile(@RequestParam String userId) throws UserRepositoryException {\n    try {\n        User user = userRepository.findByUserId(userId);\n        if (user == null) {\n            throw new ResourceNotFoundException();\n        }\n        return modelMapper.map(user, UserDTO.class);\n    } catch (UserRepositoryException e) {\n        throw new ResourceNotFoundException(e.getMessage());\n    }\n}","@GetMapping(\"/profile\")\npublic ResponseEntity<UserDTO> getUserProfile(@RequestParam String userId) throws UserRepositoryException {\n    try {\n        User user = userRepository.findByUserId(userId);\n        if (user == null) {\n            throw new ResourceNotFoundException();\n        }\n        return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));\n    } catch (UserRepositoryException e) {\n        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);\n    }\n}","@GetMapping(\"/profile\")\npublic UserDTO getUserProfile(@RequestParam String userId) throws UserRepositoryException {\n    User user = userRepository.findByUserId(userId);\n    if (user == null) {\n        throw new ResourceNotFoundException();\n    }\n    return modelMapper.map(user, UserDTO.class);\n}"]','{"correct":1}','APPLY',0.6,'["api-design","exception-handling"]'),
('FULLSTACK','CODE_READING','다음은 프론트엔드에서 REST API로 데이터를 요청하는 코드입니다. 

fetch(`/api/products/${productId}`, {
  method: ''GET'',
  headers: {
    ''Authorization'': `Bearer ${token}`
  }
})
.then(response => response.json())','["1. 토큰이 유효하지 않으면 오류를 처리합니다.","2. 모든 HTTP 응답 코드에 대해 성공으로 처리됩니다.","3. 요청한 데이터가 존재하지 않을 때도 항상 정상적으로 반환합니다.","4. 인증 헤더를 제대로 설정하여 API 호출을 보냅니다."]','{"correct":3}','ANALYZE',0.8,'["cors-authentication"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 프로필 사진을 삭제하는 API 입니다.

@DeleteMapping("/profile/photo")
public ResponseEntity<Void> deleteUserProfilePhoto(@RequestParam String userId) {
    User user = userRepository.findByUserId(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    // 프로필 사진 삭제 로직
    return ResponseEntity.ok().build();
}

코드를 분석하고, 해당 API의 문제점을 지적하세요.','["사용자의 ID가 존재하지 않을 경우 적절한 HTTP 상태 코드를 반환하지 않습니다.","프로필 사진 삭제 후 사용자의 데이터베이스 정보를 갱신하지 않습니다.","삭제된 프로필 사진 파일을 제거하는 로직이 필요합니다.","위 모든 문제점이 있습니다."]','{"correct":3}','APPLY',0.8,'["api-design","file-delete"]'),
('FULLSTACK','CODE_READING','다음 코드는 Redis 캐시에 데이터를 저장하고 이를 읽어오는 Java 코드입니다.

redisTemplate.opsForValue().set("user:123", user);
String cachedUser = (String) redisTemplate.opsForValue().get("user:123");','["코드는 Redis에서 사용자 정보를 삭제합니다.","코드는 Redis에 데이터를 쓰고 읽습니다.","코드는 Redis에서 데이터의 유효성을 검사합니다.","코드는 Redis에서 키-값 저장소를 생성합니다."]','{"correct":1}','UNDERSTAND',0.8,'["cache-redis"]'),
('FULLSTACK','CODE_READING','다음 Java 코드는 Spring Boot에서 사용되는 @RestController 어노테이션을 사용하여 REST API를 정의하고 있습니다.

@RestController
public class UserController {
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) { return userRepository.findById(id).orElse(null); }
}

이 코드의 문제점은 무엇인가요?
','["@GetMapping 어노테이션이 잘못 사용되어 있다.","userRepository.findById 메서드를 호출하지 못한다.","null 값을 반환하는 것이 안전하지 않다.","@PathVariable Long id 대신 String id로 선언해야 한다."]','{"correct":2}','EVALUATE',0.8,'["rest-api-contract"]'),
('FULLSTACK','CODE_READING','다음 JavaScript 코드 스니펫은 API 요청을 보내고 응답을 처리하는 것입니다.

fetch(''/api/users'', {
    method: ''GET'',
}).then(response => response.json())
.then(data => console.log(data))','["API에서 사용자 정보를 가져옵니다.","사용자의 로그인 세션을 만듭니다.","HTTP 요청이 실패할 경우 에러 메시지를 출력합니다.","응답의 상태 코드가 200일 때만 데이터를 처리합니다."]','{"correct":0}','REMEMBER',0.8,'["api-fetching"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 주소 정보를 변경하는 API 입니다.

@PutMapping("/profile/address")
public ResponseEntity<Void> updateAddress(@RequestParam String userId, @RequestBody AddressDTO address) {
    User user = userRepository.findByUserId(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    // 주소 정보 업데이트 로직
    return ResponseEntity.ok().build();
}

코드를 분석하고, 해당 API의 문제점을 지적하세요.','["사용자의 ID가 존재하지 않을 경우 적절한 HTTP 상태 코드를 반환하지 않습니다.","주소 정보 업데이트 후 사용자의 데이터베이스 정보를 갱신하지 않습니다.","업데이트된 주소 정보를 저장할 위치를 지정하지 않았습니다.","위 모든 문제점이 있습니다."]','{"correct":3}','APPLY',0.8,'["api-design","address-update"]'),
('FULLSTACK','CODE_READING','다음은 OAuth2 인증 흐름을 위한 프론트엔드 JavaScript 코드입니다.

fetch("/oauth/token", {
  method: "POST",
  headers: {"Content-Type": "application/x-www-form-urlencoded"},
  body: new URLSearchParams({grant_type: ''password'', username: ''user123'', password: ''pass456''})
})
.then(response => response.json())
.catch(error => console.error(''Error:'', error));','["프론트엔드는 POST 요청으로 토큰을 발급받습니다.","프론트엔드는 GET 요청으로 세션 정보를 가져옵니다.","프론트엔드는 POST 요청으로 세션 생성을 시도합니다.","프론트엔드는 POST 요청으로 인증 코드를 받습니다."]','{"correct":0}','UNDERSTAND',0.8,'["authentication-oauth2"]'),
('FULLSTACK','CODE_READING','다음 코드는 REST API에서 사용자 정보를 불러오는 핸들러입니다.

@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id);
}','["사용자의 모든 데이터를 가져옵니다.","특정 ID의 사용자를 조회합니다.","사용자 목록을 반환합니다.","HTTP 요청이 실패할 경우 404 상태 코드를 리턴합니다."]','{"correct":1}','REMEMBER',0.8,'["rest-api-handling"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 비밀번호 변경을 처리하는 API 입니다.

@PutMapping("/profile/password")
public ResponseEntity<Void> updatePassword(@RequestParam String userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
    User user = userRepository.findByUserId(userId);
    if (user == null || !encoder.matches(oldPassword, user.getPassword())) {
        throw new ResourceNotFoundException();
    }
    // 비밀번호 변경 로직
    return ResponseEntity.ok().build();
}

코드를 분석하고, 해당 API의 문제점을 지적하세요.','["사용자의 현재 비밀번호가 올바르지 않더라도 적절한 HTTP 상태 코드를 반환하지 않습니다.","비밀번호 업데이트 후 사용자 정보를 데이터베이스에 저장하지 않습니다.","변경된 비밀번호를 암호화하여 저장하지 않습니다.","위 모든 문제점이 있습니다."]','{"correct":3}','APPLY',0.9,'["api-design","password-management"]'),
('FULLSTACK','CODE_READING','다음 코드는 사용자의 프로필 사진을 업데이트하는 API 입니다.

@PostMapping("/profile/photo")
public ResponseEntity<Void> updateUserProfilePhoto(@RequestParam String userId, @RequestParam MultipartFile photo) {
    User user = userRepository.findByUserId(userId);
    if (user == null) {
        throw new ResourceNotFoundException();
    }
    // 프로필 사진 업데이트 로직
    return ResponseEntity.ok().build();
}

코드를 분석하고, 해당 API의 문제점을 지적하세요.','["사용자 ID가 잘못되었을 경우 적절한 HTTP 상태 코드를 반환하지 않습니다.","프로필 사진 업데이트 이후 사용자의 데이터베이스 정보를 갱신하지 않습니다.","업데이트된 프로필 사진을 저장할 위치를 지정하지 않았습니다.","위 모든 문제점이 있습니다."]','{"correct":3}','APPLY',0.9,'["api-design","file-upload"]'),
('FULLSTACK','CODE_READING','다음은 프론트엔드에서 WebSocket을 이용해 실시간 데이터를 처리하는 코드 스니펫입니다. 

socket.on(''newProduct'', data => {
  console.log(data);
});
someButton.addEventListener(''click'', () => {
  socket.emit(''addProduct'', {name: ''New Product''});
});','["1. 클릭 이벤트가 발생할 때마다 새로운 WebSocket 연결을 생성합니다.","2. 새 제품이 추가될 때마다 서버로부터 메시지를 받습니다.","3. 모든 클라이언트에서 새 제품 정보를 공유하지 않습니다.","4. 클릭 시 새로운 제품을 서버에 등록하지 않습니다."]','{"correct":1}','ANALYZE',0.9,'["websocket-realtime-communication"]'),
('PYTHON_BACKEND','MCQ','WSGI와 ASGI의 주요 차이점은 무엇인가?','["WSGI는 요청마다 워커 스레드를 블로킹하는 동기 방식의 호출만 지원하며, 비동기·WebSocket 같은 프로토콜은 애초에 처리할 수 없는 구조다.","ASGI는 이름과 달리 실제로는 WSGI와 동일하게 동기 방식의 호출만 지원하고 비동기 프로토콜은 다루지 못한다.","WSGI는 HTTP 요청 응답 프로토콜을 사용하며, ASGI는 HTTP 및 WebSocket 연결을 동시에 처리할 수 있다.","ASGI는 WSGI와 마찬가지로 HTTP 요청·응답 프로토콜만 지원하며 WebSocket 같은 장수명 연결은 다루지 못한다."]','{"correct":2}','REMEMBER',0.1,'["wsgi-asgi"]'),
('PYTHON_BACKEND','MCQ','pytest 픽스처(fixture)의 정의와 목적은 무엇인가?','["픽스처는 테스트가 모두 끝난 뒤 실행 결과를 파일로 저장해 리포트를 생성하는 도구다.","픽스처는 pytest가 내부적으로 사용하는 변수·객체를 관리하는 별도의 설정 파일이다.","픽스처는 테스트 코드에서 공통적으로 사용되는 변수나 객체를 미리 준비해두는 함수이다.","픽스처는 프로젝트 설정 파일에 나열된 모든 단위 테스트를 실행하는 스크립트다."]','{"correct":2}','REMEMBER',0.1,'["pytest-fixtures"]'),
('PYTHON_BACKEND','MCQ','GIL(Global Interpreter Lock)의 주요 기능은 무엇인가?','["파이썬 인터프리터가 한 번에 하나의 스레드만 실행하게 만든다.","파이썬 코드에서 동기 호출을 비동기로 변환한다.","파이썬 코드를 컴파일하여 빠른 실행을 가능하게 한다.","파이썬 프로그램에서 동시에 여러 스레드를 실행할 수 있게 한다."]','{"correct":0}','REMEMBER',0.1,'["gil"]'),
('PYTHON_BACKEND','MCQ','Celery 워커(worker)와 브로커(broker, 예: Redis/RabbitMQ)의 역할은 무엇인가?','["워커는 작업을 실행하고 결과를 반환하며, 브로커는 작업 요청(메시지)을 전달하는 큐 역할을 한다.","브로커가 작업을 직접 실행해 결과를 반환하고, 워커는 그 작업 요청을 큐에 밀어 넣는 역할만 한다.","워커와 브로커는 역할 구분 없이 둘 다 똑같이 작업을 큐에 넣고 실행하는 동일한 컴포넌트다.","워커가 작업 요청을 처리하면서 동시에 브로커의 큐 역할까지 스스로 겸해서 수행한다."]','{"correct":0}','REMEMBER',0.1,'["celery-worker-broker"]'),
('PYTHON_BACKEND','MCQ','캐시 무효화(cache invalidation) 전략 중 TTL(Time To Live)의 의미는?','["TTL은 캐시가 담을 수 있는 최대 메모리 용량 한도를 의미하는 값이다.","TTL은 캐시 키가 만료될 시점만 미리 표시해둘 뿐, 실제로 그 시점에 캐시에서 제거하는 동작까지 보장하지는 않는다고 잘못 알려져 있다.","TTL은 캐시 키의 유효기간을 관리하며, 시간 제한이 경과되면 자동으로 캐시를 무효화한다.","TTL은 캐시가 만료되기 전까지 조회될 수 있는 최대 횟수를 세는 값이다."]','{"correct":2}','REMEMBER',0.1,'["cache-ttl"]'),
('PYTHON_BACKEND','MCQ','파이썬 제너레이터(`yield`)의 주요 특징은 무엇인가?','["함수 내부의 로컬 변수 상태를 유지하지 않는다.","함수 호출 시 함수 본체 코드가 즉시 실행된다.","제너레이터는 모든 값을 한 번에 계산해 반환하는 함수다.","함수가 여러 값을 순차적으로 반환할 수 있도록 한다."]','{"correct":3}','REMEMBER',0.2,'["generator"]'),
('PYTHON_BACKEND','MCQ','HTTP 상태 코드 401과 403의 의미 차이는 무엇인가요?','["401은 권한 없음(Forbidden)을, 403은 무효한 요청 메서드(Method Not Allowed)를 나타낸다.","401은 서버 내부에서 발생한 오류를, 403은 요청이 그냥 거절된 경우를 나타낸다.","401은 무효한 요청 메서드 사용을, 403은 서버가 무시해버린 권한 문제를 나타낸다.","401은 인증되지 않은 상태에서 접근 시도를 나타내며, 403은 사용자에게 권한이 없는 리소스에 대한 요청을 나타낸다."]','{"correct":3}','REMEMBER',0.2,'["http-status-codes"]'),
('PYTHON_BACKEND','MCQ','파이썬 컨텍스트 매니저(`with` 문)는 어떤 기능을 제공하는가?','["파일을 읽고 쓰는 동안 발생하는 모든 오류를 자동으로 무시해버린다.","특정 스코프에서 리소스를 안전하게 정리하거나 초기화할 수 있게 한다.","동기적으로 작성된 코드를 파이썬이 알아서 비동기 호출로 바꿔 실행해준다.","with 문을 쓰면 함수 실행 결과가 자동 캐싱되어 재사용된다."]','{"correct":1}','REMEMBER',0.2,'["context-manager"]'),
('PYTHON_BACKEND','MCQ','FastAPI에서 경로 매개변수(path parameter)를 선언하는 방법은 무엇인가요?','["경로 매개변수는 반드시 Query 클래스로 감싸서 지정해야만 인식된다고 잘못 알려져 있다.","path parameter는 @path_parameter 전용 데코레이터를 붙여야만 지정된다고 오해하기 쉽다.","경로 템플릿의 {변수명}과 이름이 같은 함수 매개변수를 선언하고 타입 힌트를 붙이면 FastAPI가 자동으로 경로 매개변수로 인식한다.","경로 매개변수는 path parameter라는 키워드를 함수 앞에 붙여야 설정된다고 오해하기 쉽다."]','{"correct":2}','REMEMBER',0.2,'["fastapi-path-parameters"]'),
('PYTHON_BACKEND','MCQ','파이썬 가상환경(venv)을 만드는 표준 명령어는 무엇인가요?','["pipenv shell","conda create --name myenv","python -m venv myenv","virtualenv myenv"]','{"correct":2}','REMEMBER',0.2,'["python-virtual-environment"]'),
('PYTHON_BACKEND','MCQ','파이썬 예외 계층에서 커스텀 예외를 정의할 때 어떤 규칙을 따라야 하는가?','["커스텀 예외는 적절한 부모 클래스를 상속 받아야 한다.","이름이 `Exception`으로 시작해야 인식된다.","반드시 object만 상속해야 한다고 잘못 알려져 있다.","`BaseException`만 직접 상속해야 한다고 오해한다."]','{"correct":0}','UNDERSTAND',0.3,'["custom-exception"]'),
('PYTHON_BACKEND','MCQ','Celery 태스크가 실패했을 때 자동으로 재시도되도록 설정하는 방법은?','["Celery는 재시도 기능을 아예 지원하지 않아 실패한 태스크는 수동으로 다시 호출해야 한다고 오해하기 쉽다.","@app.task(autoretry_for=(Exception,), max_retries=3)와 같이 데코레이터에 재시도 대상 예외와 최대 횟수를 지정한다.","태스크 함수 안에서 try/except로 감싸고 아무 것도 하지 않으면 Celery가 알아서 재시도해준다고 잘못 알려져 있다.","브로커(Redis) 설정에서 retry=true라는 전역 옵션을 켜면 모든 태스크가 자동 재시도된다고 오해하기 쉽다."]','{"correct":1}','UNDERSTAND',0.3,'["celery-retry-task"]'),
('PYTHON_BACKEND','MCQ','파이썬 데코레이터가 함수를 감싸는 방식은 어떻게 작동하는가?','["데코레이터는 단순히 함수의 결과를 반환한다.","데코레이터는 함수 호출 전후로 코드를 추가할 수 있다.","데코레이터는 함수 내부에서 동작을 변경하지 않는다.","데코레이터는 클래스에는 적용할 수 없고 함수에만 사용할 수 있다."]','{"correct":1}','UNDERSTAND',0.3,'["decorator"]'),
('PYTHON_BACKEND','MCQ','pip과 poetry의 의존성 관리 방식의 근본적인 차이는?','["poetry는 poetry.lock으로 전체 의존성 트리를 고정해 재현 가능한 설치를 보장하지만, pip은 기본적으로 lock 파일 없이 requirements.txt만으로 설치한다.","pip은 가상환경을 자동으로 만들어주는 반면 poetry는 가상환경 기능 자체를 전혀 지원하지 않는다고 잘못 알려져 있다.","poetry는 내부가 C 언어로 작성되어 있어서 pip보다 설치 속도가 항상 더 빠르다고 오해하기 쉽다.","pip과 poetry는 pyproject.toml이라는 같은 파일 형식을 쓰기 때문에 실질적 차이가 전혀 없다고 오해하기 쉽다."]','{"correct":0}','UNDERSTAND',0.3,'["poetry-vs-pip-dependency-management"]'),
('PYTHON_BACKEND','MCQ','Django 시그널(signal)이 이벤트 발생 시 리시버를 호출하는 방식은?','["특정 이벤트에 대해 시그널 등록 후, 해당 이벤트 발생 시 자동 호출","시그널 없이 리시버 함수를 개발자가 매번 직접 호출해야 동작한다.","모든 이벤트에 대한 시그널 등록 후, 단일 리시버로 모든 이벤트 처리","뷰(View)에서 수동으로 시그널 객체를 생성해 매번 전송해야 한다."]','{"correct":0}','UNDERSTAND',0.3,'["django-signal-function-calling"]'),
('PYTHON_BACKEND','MCQ','CPU 바운드 작업과 I/O 바운드 작업 중 각각 멀티프로세싱과 asyncio 중 어느 것이 더 적합한가?','["I/O 바운드 작업은 asyncio, CPU 바운드 작업은 멀티프로세싱이 더 적합합니다.","멀티프로세싱과 asyncio는 모든 유형의 작업에 동일하게 적용할 수 있습니다.","CPU 바운드와 I/O 바운드 모두 스레딩만으로 충분히 처리할 수 있습니다.","CPU 바운드 작업은 asyncio, I/O 바운드 작업은 멀티프로세싱이 더 적합합니다."]','{"correct":0}','EVALUATE',0.3,'["python-cpu-iobound-processing"]'),
('PYTHON_BACKEND','MCQ','Django REST Framework Serializer의 주요 역할은 무엇인가?','["뷰(View) 로직 처리","쿼리셋 필터링","데이터 직렬화와 검증","데이터베이스 쿼리 생성"]','{"correct":2}','UNDERSTAND',0.3,'["drf-serializer-role"]'),
('PYTHON_BACKEND','MCQ','중첩된 리스트를 포함한 객체를 얕은 복사(shallow copy)했을 때 나타나는 현상으로 옳은 것은?','["최상위 객체는 새로 생성되지만 내부의 중첩 객체는 원본과 참조를 공유한다.","얕은 복사는 모든 계층을 새로 생성해 원본과 완전히 무관해진다.","얕은 복사는 실제로 아무 것도 복사하지 않고 원본의 참조만 그대로 반환한다.","얕은 복사가 깊은 복사보다 항상 느리게 동작하는 것이 파이썬의 기본 동작이다."]','{"correct":0}','UNDERSTAND',0.3,'["shallow-deep-copy"]'),
('PYTHON_BACKEND','MCQ','JWT(JSON Web Token) 기반 인증에서 서버가 토큰의 유효성을 검증하는 방식으로 옳은 것은?','["서버가 발급한 모든 토큰을 DB에 저장해두고 매 요청마다 세션 테이블과 대조한다고 잘못 알려져 있다.","클라이언트가 자신의 개인키로 서명을 검증한 뒤 결과만 서버에 통보하면 신뢰한다고 오해하기 쉽다.","대칭키(HMAC)라면 발급 때와 같은 비밀키로, 비대칭키(RS256 등)라면 그에 대응하는 공개키로 토큰의 서명을 검증해 위조 여부를 확인한다.","토큰의 만료 시간(exp) 클레임만 확인하고 서명 검증은 생략한다고 잘못 알려져 있다."]','{"correct":2}','UNDERSTAND',0.3,'["jwt-authentication"]'),
('PYTHON_BACKEND','MCQ','FastAPI Pydantic 모델이 요청 바디를 자동으로 검증하는 방식은 무엇인가요?','["Pydantic 모델은 미들웨어에 전역 등록해야만 검증에 사용할 수 있다고 잘못 알려져 있다.","Pydantic 모델 인스턴스를 직접 생성해 매 요청마다 수동으로 유효성 검사를 호출해야 한다고 오해하기 쉽다.","request_body 데코레이터로 타입 힌트를 지정하면 Pydantic 모델을 자동 생성·검증한다고 잘못 알려져 있다.","함수 매개변수에 Pydantic 모델 타입 힌트를 사용하여 지정하면, FastAPI는 자동으로 요청 바디를 검증한다."]','{"correct":3}','UNDERSTAND',0.3,'["fastapi-pydantic-models"]'),
('PYTHON_BACKEND','MCQ','Django에서 트랜잭션을 시작하고 특정 로직이 완료되면 자동으로 커밋되도록 설정하려면 어떻게 해야 하나?','["session.commit() 메서드를 직접 호출하기","db.session.commit() 메서드를 그대로 호출하기","@transaction.atomic() 데코레이터 사용","@atomic이라는 축약 데코레이터를 바로 사용하기"]','{"correct":2}','APPLY',0.3,'["django-transactions"]'),
('PYTHON_BACKEND','MCQ','Django ORM 쿼리셋을 즉시 평가하도록 강제하는 방법은 무엇인가?','["list() 호출하기","filter() 사용하기","values_list() 메서드만 사용하기","all() 메서드 사용하기"]','{"correct":0}','APPLY',0.3,'["django-orm-querysets"]'),
('PYTHON_BACKEND','MCQ','파이썬 패키지 구조에서 ''__init__.py'' 파일의 역할은 무엇인가요?','["패키지 경로를 sys.path에 추가한다.","모듈 import 시 실행되도록 한다.","패키지 내부 모든 모듈을 자동 import한다. 실제로는 명시적 import가 필요하다.","파이썬 인터프리터에게 이 디렉토리를 패키지로 처리하도록 알려준다."]','{"correct":3}','UNDERSTAND',0.4,'["python-package-structure"]'),
('PYTHON_BACKEND','MCQ','Django 미들웨어가 요청/응답 처리 흐름에서 어떤 순서로 실행되는지?','["모든 미들웨어가 스레드로 동시에 병렬 실행되어 순서가 전혀 보장되지 않는다.","미들웨어는 등록된 순서대로 요청 처리 시에도 응답 처리 시에도 항상 동일한 방향으로 순차 실행된다.","등록 순서와 관계없이 매 요청마다 미들웨어 실행 순서가 무작위로 바뀐다.","요청은 등록 순서대로 위에서 아래로 통과하고, 응답은 그 역순으로 되돌아간다."]','{"correct":3}','UNDERSTAND',0.4,'["django-middleware-execution-order"]'),
('PYTHON_BACKEND','MCQ','비동기 함수에서 `await asyncio.sleep(1)` 호출이 실행될 때 이벤트 루프는 어떻게 동작하는가?','["루프가 즉시 KeyboardInterrupt에 준하는 예외를 던지며 코루틴 실행을 강제로 중단시켜버린다.","코루틴이 일시 중단(suspend)되고, 이벤트 루프는 그동안 다른 태스크를 실행한다.","루프 자체가 완전히 멈추어 다른 모든 비동기 작업까지 함께 중단된 채로 계속 대기하게 된다.","루프는 계속 돌지만 asyncio.sleep 호출은 아무 효과 없이 무시되고 다음 줄로 바로 넘어간다."]','{"correct":1}','UNDERSTAND',0.4,'["python-async-await"]'),
('PYTHON_BACKEND','MCQ','Django ORM 쿼리셋은 언제 데이터베이스에 실제 쿼리를 실행하나?','["쿼리셋을 순회하거나 리스트로 변환할 때","쿼리셋을 정의(할당)하는 시점","조회 메서드를 호출하는 시점","필터링(filter)을 적용하는 순간 곧바로 실행된다고 오해하기 쉽다."]','{"correct":0}','UNDERSTAND',0.4,'["django-orm-lazy-evaluation"]'),
('PYTHON_BACKEND','MCQ','Celery 결과 백엔드(result backend)의 역할은 무엇인가?','["결과 백엔드는 큐에 쌓인 워커 프로세스들을 직접 관리하고 감독한다.","결과 백엔드는 브로커를 대체해 작업 자체를 워커들에게 직접 배포하고 스케줄링하는 역할까지 담당한다.","결과 백엔드는 새로운 작업 요청을 받아 큐에 추가하는 입구 역할을 한다.","결과 백엔드는 태스크 실행 상태와 반환값을 저장하고 조회 가능하게 한다."]','{"correct":3}','UNDERSTAND',0.4,'["celery-result-backend"]'),
('PYTHON_BACKEND','MCQ','DRF ViewSet과 라우터(Router)가 URL을 자동 생성하는 방식은?','["라우터에 등록된 모든 ViewSet에 대해 라우트 자동 생성","라우터를 등록할 때 개발자가 URL 패턴을 하나하나 직접 지정해야 한다.","ViewSet 클래스 안에 urls.py 등록 코드를 직접 작성해 넣어야만 라우트가 생성된다.","별도 설정 없이도 라우터 없이 URL이 완전히 자동으로 생성된다."]','{"correct":0}','UNDERSTAND',0.4,'["drf-viewset-router-url-generation"]'),
('PYTHON_BACKEND','MCQ','스레드(thread)와 코루틴(coroutine)의 동시성 처리 차이점은 무엇인가?','["스레드는 결코 비동기 작업을 지원할 수 없으며, 언제나 동기적인 순차 처리 방식으로만 동작한다.","코루틴은 스레드와 달리 이벤트 루프 없이는 여러 작업을 동시에 실행할 수 없다.","스레드는 CPU 스케줄링에 의존하여 실행되지만, 코루틴은 프로그래머가 직접 제어를 넘겨받는다.","코루틴은 스레드보다 항상 메모리 사용량이 훨씬 적어 무조건 더 효율적으로 동작한다."]','{"correct":2}','UNDERSTAND',0.4,'["thread-coroutine"]'),
('PYTHON_BACKEND','MCQ','FastAPI에서 백그라운드 태스크를 실행하려면 어떻게 해야 하는가?','["@background 데코레이터를 함수 위에 적용하기","threading.Thread로 별도 스레드를 직접 생성하기","multiprocessing.Process로 별도 프로세스 생성하기","background_tasks.add_task() 호출하기"]','{"correct":3}','APPLY',0.4,'["fastapi-background-tasks"]'),
('PYTHON_BACKEND','MCQ','`asyncio.gather` 함수의 주요 기능은 무엇인가?','["작업들을 순서대로 호출해 실행한다. 실제로는 동시 실행이 핵심이다.","작업들의 결과를 무시한다.","단일 작업만 동기 실행한다.","여러 비동기 작업 결과를 동시에 처리하고 반환한다."]','{"correct":3}','UNDERSTAND',0.4,'["asyncio-gather"]'),
('PYTHON_BACKEND','MCQ','프로세스 기반(gunicorn worker) 병렬 처리와 스레드 기반 병렬 처리 중 GIL의 영향을 받는 것은 무엇인가요?','["GIL은 멀티프로세스 환경에서도 각 프로세스 사이에 공유되어 동일하게 적용됩니다.","GIL은 스레드·프로세스·비동기 코루틴 구분 없이 파이썬이 실행되는 모든 환경에 항상 예외 없이 동일하게 적용됩니다.","gunicorn worker가 여러 개여도 하나의 프로세스 안에서만 GIL 없이 동작하도록 자동으로 통합됩니다.","스레드 기반 병렬 처리는 GIL의 영향으로 인해 CPU 바운드 작업에서 성능 저하가 발생합니다."]','{"correct":3}','ANALYZE',0.4,'["python-gil-threading"]'),
('PYTHON_BACKEND','MCQ','정적 파일(static files)을 Gunicorn/Uvicorn 같은 애플리케이션 서버가 아니라 Nginx나 CDN으로 서빙해야 하는 주된 이유는?','["애플리케이션 서버는 동적 요청 처리에 최적화되어 있어, 정적 파일을 대량으로 서빙하면 요청 처리 성능이 저하되기 때문이다.","정적 파일을 애플리케이션 서버에서 서빙하면 파일 경로 노출 같은 보안 취약점이 항상 자동으로 생겨난다. 실제로는 위치와 취약점이 자동 연결되지 않는다.","정적 파일은 반드시 데이터베이스에 저장해야만 하므로 별도의 서버가 필요해지기 때문이다.","정적 파일은 오직 파이썬 코드로만 처리할 수 있어 다른 서버로는 처리할 수 없기 때문이다."]','{"correct":0}','UNDERSTAND',0.4,'["static-files-serving"]'),
('PYTHON_BACKEND','MCQ','FastAPI가 Pydantic 모델로부터 OpenAPI 스키마를 자동 생성하는 원리는 무엇인가요?','["FastAPI는 Pydantic 모델의 메서드를 직접 호출해 연결된 데이터베이스 테이블 정의에서 스키마 정보를 가져와 자동으로 생성한다.","Pydantic 모델과 전혀 무관하게 각 엔드포인트의 함수 이름과 docstring만 파싱해 스키마를 자동으로 만든다.","함수 매개변수와 반환값(response_model)에 사용된 Pydantic 모델의 필드·타입을 분석해 OpenAPI 스키마를 생성한다.","OpenAPI 스키마는 Pydantic 모델과는 무관하게 매 요청이 들어올 때마다 그때그때 새로 추론된다."]','{"correct":2}','UNDERSTAND',0.4,'["fastapi-pydantic-openapi"]'),
('PYTHON_BACKEND','MCQ','다음 HTTP 메서드 중 멱등성(idempotency)이 보장되지 않는 것은?','["PUT","DELETE","POST","GET"]','{"correct":2}','APPLY',0.5,'["rest-api-idempotency"]'),
('PYTHON_BACKEND','MCQ','Django에서 OneToOneField 또는 ForeignKey 관계의 N+1 문제를 해결하기 위해 select_related를 사용하는 방법은?','["queryset.filter(field_name__in=values)","queryset.select_related(''field_name'')","queryset.prefetch_related(''related_field'')","model.objects.annotate(field_name=''value'')"]','{"correct":1}','APPLY',0.5,'["django-select-related-n-plus-one"]'),
('PYTHON_BACKEND','MCQ','DRF 권한 클래스(permission class)로 접근을 제어하는 방법은?','["view 함수에서 직접 체크","permission_classes 속성을 통해 적용","urls.py 라우팅에서 설정","settings.py에 전역으로만 정의해두면 충분하며, 실제로는 View·ViewSet 단위의 세밀한 제어가 불가능해진다."]','{"correct":1}','APPLY',0.5,'["drf-permission-classes"]'),
('PYTHON_BACKEND','MCQ','uvicorn이 ASGI 애플리케이션을 실행하는 방식과, gunicorn과 함께 사용할 때의 설정은 무엇인가요?','["ASGI는 WSGI와 완전히 같은 프레임워크 규격이라서 uvicorn을 그냥 직접 실행하면 충분하다고 오해하기 쉽다.","uvicorn은 자체 이벤트 루프를 사용하여 ASGI 애플리케이션을 구동합니다. gunicorn에서는 -k 옵션으로 ''uvicorn.workers.UvicornWorker''를 지정해야 합니다.","ASGI는 uvicorn과 애초에 호환되지 않아서 gunicorn이 별도의 브릿지 기능으로 억지로 연결해준다고 잘못 알려져 있다.","gunicorn에서 uvicorn을 쓸 때는 ASGI를 직접 실행할 수 없어 반드시 WSGI로 먼저 변환해야만 구동된다고 오해하기 쉽다."]','{"correct":1}','UNDERSTAND',0.5,'["python-uvicorn-gunicorn"]'),
('PYTHON_BACKEND','MCQ','FastAPI `Depends()`로 의존성 주입을 구성하는 방법은 무엇인가요?','["매개변수의 기본값으로 Depends(function)을 지정하면 FastAPI가 이를 해석해 자동으로 의존성을 주입한다.","depends()라는 전역 함수를 먼저 호출해 반환값을 매개변수에 수동으로 대입해야 한다고 오해하기 쉽다. 실제로는 그런 전역 함수가 없다.","함수 내부에서 의존성 객체를 생성한 뒤 다른 메서드에 전달해야 한다고 잘못 알려져 있다.","@depends() 데코레이터를 함수 위에 붙여 의존성을 선언해야 주입된다고 오해하기 쉽다."]','{"correct":0}','APPLY',0.5,'["fastapi-dependency-injection"]'),
('PYTHON_BACKEND','MCQ','pytest에서 외부 API 호출을 목(mock)으로 대체하는 방법은?','["pytest가 자체적으로 제공하는 @patch라는 전용 메서드를 이용하면 된다.","mock 모듈을 import하고, 함수 내에서 직접 API 호출 부분을 대체하도록 설정한다.","외부 API 호출 부분을 mock 객체로 직접 교체하기만 하면 충분하다.","@mock.patch 데코레이터를 사용하여 필요한 위치에 테스트용 객체를 삽입한다."]','{"correct":3}','APPLY',0.5,'["pytest-mock-external-api"]'),
('PYTHON_BACKEND','MCQ','pytest-cov 플러그인이 설치된 상태에서, 테스트 실행과 동시에 커버리지를 측정하려면 어떤 명령을 쓰는가?','["pytest --cov","coverage report","pytest --coverage","coverage html"]','{"correct":0}','APPLY',0.5,'["python-coverage-reporting"]'),
('PYTHON_BACKEND','MCQ','DRF ModelSerializer로 모델 필드를 자동 매핑하는 방법은?','["모든 필드를 수동으로 다시 선언해야 매핑된다.","ListSerializer로 목록 형식을 정의한다.","ModelViewSet에 필드를 나열해야 매핑된다. 실제로는 Meta.model 지정만으로 충분하다.","serializers.ModelSerializer에서 모델 클래스 정의"]','{"correct":3}','APPLY',0.5,'["drf-modelserializer-mapping"]'),
('PYTHON_BACKEND','MCQ','Celery beat로 주기적 작업(periodic task)을 예약하는 표준적인 방법은?','["beat_schedule 설정 또는 on_after_configure 시그널에서 sender.add_periodic_task()로 스케줄을 등록한다.","@periodic_task 데코레이터(레거시 API)만 붙이면 beat_schedule 등록 없이도 자동으로 스케줄링된다고 잘못 알려져 있다. 실제로는 별도 등록 절차가 여전히 필요하다.","워커 프로세스를 여러 개 띄우기만 하면 그 자체로 주기적 실행이 자동 활성화된다고 오해하기 쉽다.","태스크 함수 본문 안에 while True와 sleep()을 직접 넣어 무한 반복시켜야 한다고 잘못 알려져 있다."]','{"correct":0}','APPLY',0.5,'["celery-beat-periodic-task"]'),
('PYTHON_BACKEND','MCQ','파이썬 로깅에서 print() 대신 logging 모듈을 사용해야 하는 이유는 무엇인가요?','["print()를 사용하면 그 자체만으로 프로그램 실행 성능이 눈에 띄게 저하될 수 있다.","파이썬 로깅 시스템은 print()를 아예 인식하지 못하도록 막아두어 logging 모듈만 지원한다.","logging 모듈은 오직 파일에만 메시지를 기록할 수 있고 콘솔 출력은 지원하지 않는다.","print() 함수보다 logging 모듈은 더 다양한 출력 포맷과 필터링 옵션을 제공합니다."]','{"correct":3}','APPLY',0.5,'["python-logging-module"]'),
('PYTHON_BACKEND','MCQ','대용량 파일 업로드를 처리할 때 서버 메모리 사용을 줄이는 방법은 무엇인가요?','["파일 전송이 끝난 뒤 전체를 메모리에 통째로 올려 처리해야 스트리밍보다 안전하다고 오해하기 쉽다.","대용량 업로드는 서버 네트워크 대역폭만 고려하면 되고 메모리와는 무관하다고 잘못 알려져 있다.","파일을 파트로 나눠 전송해도 서버가 즉시 하나의 버퍼로 재조합해 절감 효과가 없다고 오해하기 쉽다.","스트림(Stream) 방식으로 파일을 읽어올 경우, 전체 데이터를 메모리에 올리지 않고 부분적으로 처리할 수 있어 메모리를 절약할 수 있다."]','{"correct":3}','APPLY',0.5,'["file-upload-streaming"]'),
('PYTHON_BACKEND','MCQ','CORS 오류를 해결하기 위해 서버에서 설정해야 하는 것은 무엇인가요?','["서버는 모든 출처에 대해 무조건 접근 권한을 열어두면 CORS 문제가 저절로 해결된다.","서버 측에 CORS 미들웨어를 설치하고, 원하는 출처에 대한 허용 목록을 설정한다.","클라이언트 측에서 요청 시 Access-Control-Allow-Origin 헤더를 직접 붙여 서버 응답을 대체한다.","서버가 자동으로 CORS 문제를 알아서 해결해주므로 개발자가 따로 설정할 필요가 없다."]','{"correct":1}','APPLY',0.5,'["cors-configuration"]'),
('PYTHON_BACKEND','MCQ','함수가 정수 id를 받아 User 객체 또는 None을 반환할 수 있을 때, 이를 타입 힌트로 정확히 표현한 것은?','["def get_user(id: int) -> User: ...  # None 가능성은 반환 타입에 반영하지 않아도 된다","def get_user(id: int) -> Optional[User]: ...","def get_user(id) -> int: ...  # id 타입과 반환 타입 모두 실제와 다르게 적어도 된다","def get_user(id: int) -> None: ...  # 반환값이 있어도 항상 None으로 명시해야 한다"]','{"correct":1}','APPLY',0.5,'["type-hinting"]'),
('PYTHON_BACKEND','MCQ','웹 애플리케이션에서 SQLAlchemy 세션(Session)을 요청 단위로 관리하는 일반적인 패턴은?','["요청이 시작될 때 세션을 새로 생성하고, 요청이 끝나면 커밋 또는 롤백한 뒤 세션을 닫는다.","세션은 스레드와 무관하게 항상 전역 공유되며 종료할 필요가 없다고 잘못 알려져 있다.","세션은 첫 쿼리 실행 시점에만 생성되고 이후 재사용하지 않는다고 오해하기 쉽다.","애플리케이션 시작 시 세션 하나만 만들어 전체 요청에서 재사용한다고 잘못 알려져 있다. 실제로는 세션 공유가 데이터 오염을 부른다."]','{"correct":0}','UNDERSTAND',0.5,'["sqlalchemy-session-lifecycle"]'),
('PYTHON_BACKEND','MCQ','FastAPI `response_model`로 응답 스키마를 제한하는 방법은 무엇인가요?','["함수 매개변수에 Pydantic 모델 타입 힌트를 사용하여 지정하면, FastAPI는 그것만으로 자동으로 응답 스키마를 제한한다고 잘못 알려져 있다.","경로 데코레이터(`@app.get(..., response_model=Model)`)에 Pydantic 모델을 지정하면 FastAPI가 응답을 그 스키마에 맞춰 직렬화·검증한다.","Pydantic 모델 클래스 변수에 직접 타입을 정의한 뒤 함수 매개변수로 써야 하며, response_model 인자는 이 경우 완전히 무시된다고 오해하기 쉽다. 실제로는 response_model이 그대로 적용된다.","함수 내부에서 Pydantic 모델을 직접 반환하기만 하면 되고, response_model이라는 인자 자체는 실제로 아무 효과가 없다고 잘못 알려져 있다."]','{"correct":1}','APPLY',0.5,'["fastapi-response-model"]'),
('PYTHON_BACKEND','MCQ','가변 기본 인자(mutable default argument) 문제를 피하는 방법은 무엇인가?','["모든 가변 객체는 무조건 불변 객체로 바꿔야 하며 다른 방법은 없다.","가변 객체는 절대 기본값으로 쓸 수 없으므로 항상 함수 외부에서 미리 생성한 뒤 인자로만 전달해야 한다.","매개변수의 기본 값으로 `None`을 사용하고, 함수 내에서 필요할 때 값을 초기화한다.","함수 매개변수에 무결성 검사 로직을 추가하면 가변 기본 인자 문제가 자동으로 해결된다."]','{"correct":2}','APPLY',0.5,'["mutable-default-argument"]'),
('PYTHON_BACKEND','MCQ','Django 프로젝트에서 개발(dev)과 운영(prod) 환경의 설정값을 분리해 관리하는 가장 흔한 방법은?','["settings/base.py에 공통 설정을 두고, settings/dev.py·settings/prod.py가 이를 상속한 뒤 DJANGO_SETTINGS_MODULE 환경변수로 선택한다.","환경에 관계없이 settings.py 파일 하나만 두고 조건 분기 없이 쓰는 것이 Django가 유일하게 공식 권장하는 방식이라고 잘못 알려져 있다.","운영 서버에 배포할 때마다 개발자가 settings.py 내용을 직접 손으로 하나하나 덮어써야 한다고 오해하기 쉽다.","settings.py 내용을 데이터베이스 테이블에 저장해두고 서버 시작 시점마다 런타임으로 조회해 읽어와야 한다고 오해하기 쉽다."]','{"correct":0}','APPLY',0.5,'["django-settings-environment-separation"]'),
('PYTHON_BACKEND','MCQ','Redis를 이용한 cache-aside(캐시 조회 후 없으면 DB 조회 후 캐시 저장) 패턴 구현 순서는?','["1. DB에서 데이터를 먼저 가져온 뒤, 2. Redis에서 해당 데이터의 캐시 키를 조회, 3. 없으면 Redis에 저장.","1. Redis에서 캐시 키 조회, 5초 동안 대기 후 다시 Redis 조회, 2. 없다면 DB에서 데이터 가져오기, 3. Redis에 저장.","1. 캐시부터 무효화한 뒤 DB에서 데이터 가져오기, 2. Redis에 키가 있는지 재확인, 3. 없으면 캐시 저장.","1. Redis에서 캐시 키 조회, 2. 없다면 DB에서 데이터 가져오기, 3. 가져온 데이터를 Redis에 저장."]','{"correct":3}','APPLY',0.5,'["redis-cache-aside"]'),
('PYTHON_BACKEND','MCQ','Kubernetes 스타일의 readiness 체크(다른 서비스로 요청을 받을 준비가 됐는지 판단하는 프로브)가 확인해야 할 항목은 무엇인가요?','["readiness 체크는 애플리케이션 서버의 CPU 사용량 하나만 확인하면 충분하다.","데이터베이스 연결, 캐시 연결 등 핵심 의존 서비스의 가용성을 체크합니다.","readiness 체크는 오직 네트워크 트래픽량만 모니터링하면 되는 지표다.","프로세스가 살아있는지만 확인하며 DB·캐시 같은 의존 서비스 상태는 절대 보지 않습니다."]','{"correct":1}','APPLY',0.5,'["python-health-check"]'),
('PYTHON_BACKEND','MCQ','환경변수로 민감한 설정값(DB 비밀번호 등)을 관리해야 하는 이유는 무엇인가요?','["환경변수는 여러 개발자가 함께 작업할 때 서로 다른 설정 값을 관리하기 쉽습니다.","환경변수를 사용하면 설정 값이 런타임에 동적으로 변경될 수 있습니다.","환경 변수를 사용하면 설정 값이 소스 코드에 노출되지 않아 보안 위험이 줄어듭니다.","환경 변수는 테스트 환경에서만 작동하므로 개발과 프로덕션 사이의 차이를 방지합니다."]','{"correct":2}','APPLY',0.6,'["python-environment-variables"]'),
('PYTHON_BACKEND','MCQ','웹 요청 핸들러 안에서 처리하면 안 되고 Celery 태스크로 분리해야 하는 작업의 특징은 무엇인가?','["작업이 데이터베이스 트랜잭션 커밋 이전 시점에 반드시 동기적으로 끝나야 하는 경우","작업이 오래 걸리거나 외부 서비스 호출처럼 응답 시간을 예측하기 어려운 경우","작업이 매우 단순한 계산이라 밀리초 단위로 곧바로 끝나버리는 경우","작업 결과를 응답 본문에 그대로 담아 즉시 반환해야만 하는 경우"]','{"correct":1}','EVALUATE',0.6,'["celery-web-requests"]'),
('PYTHON_BACKEND','MCQ','커넥션 풀(connection pool) 크기를 실제 동시 요청 수보다 너무 작게 설정했을 때 발생하는 문제는?','["커넥션 풀이 작으면 오히려 쿼리 실행 속도가 항상 더 빨라진다.","동시 요청이 몰릴 때 커넥션을 얻지 못한 요청이 대기하거나 타임아웃 오류가 발생한다.","커넥션 풀이 작을수록 서버 메모리 사용량이 오히려 더 늘어나는 부작용이 생긴다.","데이터베이스가 부하를 감지하면 커넥션 풀 크기를 자동으로 늘려 요청을 모두 받아준다."]','{"correct":1}','APPLY',0.6,'["connection-pool-sizing"]'),
('PYTHON_BACKEND','MCQ','migrations 작업을 수행하기 위해 Alembic의 주요 명령어들은 무엇인가요?','["migrations 작업에는 alembic upgrade 명령 하나만 있으면 그것만으로 스키마가 최신이 된다고 오해하기 쉽다.","Alembic에서는 Flask-Migrate처럼 python manage.py db init·migrate·upgrade 명령으로 진행해야 한다고 잘못 알려져 있다.","Alembic에는 migrations 전용 명령어가 없어서 개발자가 SQL 스크립트를 직접 작성해야 한다고 오해하기 쉽다.","Alembic에서 migrations 작업은 alembic init 명령으로 시작하고, 이후 alembic revision 및 upgrade 명령으로 업데이트한다."]','{"correct":3}','APPLY',0.6,'["alembic-migrations"]'),
('PYTHON_BACKEND','MCQ','gunicorn 워커 프로세스 수를 `(2 × CPU 코어 수) + 1` 같은 공식으로 설정하는 이유는?','["워커 수는 CPU 코어 수와 무관하게 하드웨어 한계까지 많이 띄울수록 처리량이 계속 증가하기 때문이다. 실제로는 스위칭 비용이 늘어난다.","I/O로 블로킹되는 동안에도 CPU를 놀리지 않으면서, 과도한 워커로 인한 컨텍스트 스위칭 비용은 억제하려는 경험칙이다.","gunicorn은 워커 수가 CPU 코어 수를 초과하면 프로세스 생성을 거부하고 에러를 낸다.","워커 수는 초당 요청 수와 정확히 1:1 비율로 맞춰야만 정상 동작하기 때문이다."]','{"correct":1}','APPLY',0.6,'["gunicorn-workers-cpu-cores"]'),
('PYTHON_BACKEND','MCQ','DRF에서 커서 기반 페이지네이션(cursor-based pagination)과 오프셋 기반 페이지네이션(offset-based pagination)의 주요 차이는?','["커서는 무작위 접근 가능, 오프셋은 순차적 접근만 가능","커서는 총 페이지 수를 항상 정확히 계산해 알려준다","커서는 특정 객체 ID를 사용, 오프셋은 객체 수를 사용","오프셋은 특정 객체 ID를 사용, 커서는 객체 수를 사용"]','{"correct":2}','APPLY',0.6,'["drf-pagination-strategy"]'),
('PYTHON_BACKEND','MCQ','블로킹 호출(예: 동기 I/O)이 이벤트 루프를 멈추는 문제를 피하는 방법은 무엇인가?','["블로킹 호출을 그냥 무시하고 계속 실행시키기만 해도 시간이 지나면 파이썬이 알아서 문제를 자동으로 해결해준다고 오해하기 쉽다.","동기 함수 정의 앞에 async 키워드만 붙이면, 함수 내부의 모든 블로킹 호출까지 파이썬이 자동으로 논블로킹 코드로 바꿔준다고 잘못 알려져 있다. 실제로는 그렇게 자동 변환되지 않는다.","비동기 함수를 도로 동기 함수로 되돌려 놓기만 하면 이벤트 루프가 막히는 문제 자체가 저절로 사라진다고 오해하기 쉽다.","블로킹 함수 호출을 asyncio.to_thread() 또는 loop.run_in_executor()로 별도 스레드에 위임해 이벤트 루프를 막지 않는다."]','{"correct":3}','APPLY',0.6,'["blocking-call"]'),
('PYTHON_BACKEND','MCQ','캐시 키(cache key)를 설계할 때 반드시 고려해야 할 사항은?','["캐시 키에는 콜론(:)이나 구분자 문자를 절대로 사용해서는 안 된다고 잘못 알려져 있다.","결과값에 영향을 주는 모든 파라미터(예: 사용자 ID, 필터 조건)를 키에 포함해 서로 다른 요청이 같은 키를 공유하지 않도록 한다.","키는 짧을수록 무조건 좋으므로 파라미터 없이 고정된 짧은 문자열 하나만 계속 재사용해야 한다고 오해하기 쉽다. 실제로는 요청 결과가 뒤섞여버린다.","캐시 키는 대소문자를 전혀 구분하지 않으니 설계할 때 신경 쓸 필요가 없다고 잘못 알려져 있다."]','{"correct":1}','APPLY',0.6,'["cache-key-design"]'),
('PYTHON_BACKEND','MCQ','Django 마이그레이션을 생성하고 적용하기 위한 순서는?','["makemigrations -> runserver","syncdb -> migrate","makemigrations -> migrate","migrate -> makemigrations"]','{"correct":2}','APPLY',0.6,'["django-migration-management"]'),
('PYTHON_BACKEND','MCQ','Django에서 시그널 대신 명시적 함수 호출을 선택해야 하는 상황은?','["특정 상황을 가리지 않고 모든 시나리오에서 항상 시그널을 사용해야 한다.","특정 이벤트에 대한 복잡한 처리나 실행 순서 보장이 필요할 때","뷰(View)에서 단순 데이터 조회만 수행하고 별도의 부수 효과가 전혀 없을 때","API 엔드포인트로 단순 데이터를 요청받아 그대로 반환할 때"]','{"correct":1}','EVALUATE',0.7,'["django-signal-usage-scenarios"]'),
('PYTHON_BACKEND','MCQ','인덱스가 없는 컬럼으로 WHERE 조건을 거는 쿼리에서 발생할 수 있는 성능 문제는?','["인덱스가 없으면 쿼리 자체가 실행되지 않고 곧바로 오류를 반환한다.","쿼리 옵티마이저가 실행 시점에 필요한 인덱스를 자동으로 만들어주므로 성능 문제가 발생하지 않는다.","전체 테이블을 순차 스캔(full scan)하여 응답 시간이 늘어난다.","인덱스가 없어도 데이터베이스가 항상 캐시된 결과를 반환해 오히려 더 빨라진다."]','{"correct":2}','UNDERSTAND',0.7,'["sql-indexing"]'),
('PYTHON_BACKEND','MCQ','Celery 태스크가 재시도될 때 멱등성(idempotency)이 없으면 발생하는 문제는 무엇인가?','["재시도된 태스크가 다른 태스크와 충돌해 전체 작업이 실패할 위험이 생긴다.","태스크가 재시도될 때마다 이전 실행의 메모리가 해제되지 않고 계속 쌓여 결국 워커 프로세스가 다운된다.","태스크가 중복 실행되어 결제·적립 등의 부작용이 두 번 이상 반영될 수 있다.","재시도 결과가 예측 불가능해져서 시스템 전체의 안정성이 저하된다."]','{"correct":2}','APPLY',0.7,'["celery-idempotency-retry"]'),
('PYTHON_BACKEND','MCQ','DRF 중첩 Serializer(nested serializer)가 N+1 쿼리를 유발하는 상황은?','["모든 모델 필드를 관계 없이 그대로 단순 직렬화만 하는 경우","직렬화 대상 모델 사이에 아무런 연관 관계가 없는 경우","하나의 모델에서 단일 필드 하나만 골라 직렬화하는 경우","하나의 모델이 다른 여러 모델과 연관되어 있고, 목록을 직렬화할 때마다 각 항목의 연관 객체를 별도로 조회할 때"]','{"correct":3}','ANALYZE',0.7,'["drf-nested-serializer-n-plus-one"]'),
('PYTHON_BACKEND','MCQ','@transaction.atomic() 블록 안에서 예외가 발생했을 때의 동작은?','["변경 사항 유지 또는 롤백은 설정에 따라 달라짐","예외 발생 시 모든 변경사항 롤백","예외 발생 시 변경 사항 유지","예외 발생 여부와 관계없이 항상 변경 사항 저장"]','{"correct":1}','APPLY',0.7,'["django-transaction-management"]'),
('PYTHON_BACKEND','MCQ','캐시 스탬피드(cache stampede)가 발생하는 조건과 이를 완화하는 방법으로 옳은 것은?','["인기 키가 만료된 순간 다수의 요청이 동시에 캐시 미스를 겪어 DB로 몰리는 현상이며, 락(뮤텍스)이나 조기 재계산으로 완화한다.","캐시 서버 자체가 완전히 다운되었을 때만 발생하며, 복제본을 추가하기만 하면 항상 확실히 방지된다.","캐시 키의 TTL이 아직 충분히 남아있을 때 발생하며, 단순히 캐시 크기를 키우면 항상 해결된다.","여러 요청이 서로 다른 캐시 키를 동시에 조회할 때 발생하며, 모든 키를 하나로 통합해버리면 완전히 사라진다. 실제로는 적중률이 떨어지는 부작용이 남는다."]','{"correct":0}','ANALYZE',0.8,'["cache-stampede"]'),
('PYTHON_BACKEND','MCQ','여러 개발자가 각자 브랜치에서 동시에 Alembic 마이그레이션을 작성해 리비전 히스토리에 여러 head가 생겼을 때 해결 방법은?','["가장 최근에 병합된 마이그레이션 파일 하나만 남기고 나머지 파일은 전부 지워버리면 된다.","`alembic merge heads` 명령으로 여러 head를 하나의 병합 리비전으로 합친다.","각 개발자가 서로 다른 데이터베이스를 쓰기만 하면 head 충돌이 자동으로 해소된다.","down_revision 값은 그냥 참고용이므로 무시하고 최신 파일부터 순서대로 실행하면 된다."]','{"correct":1}','ANALYZE',0.8,'["alembic-migration-conflicts"]'),
('PYTHON_BACKEND','MCQ','세션 기반 인증과 JWT 기반 인증 중 수평 확장(scale-out) 환경에 더 적합한 방식은 무엇이며 그 이유는 무엇인가요?','["세션이나 JWT나 수평 확장 환경에서 차이 없이 완전히 동일하게 쓸 수 있다고 오해하기 쉽다.","세션 인증이 애초에 수평 확장을 염두에 두고 설계됐으므로 선호해야 한다고 잘못 알려져 있다.","JWT 인증은 토큰의 무상태성(statelessness)으로 인해 여러 서버 간에 별도 세션 저장소 없이 검증될 수 있으므로 JWT를 사용하는 것이 적합합니다.","세션이나 JWT나 단일 서버에서만 유용해서 서버가 늘어나면 둘 다 곧바로 멈춘다고 오해하기 쉽다."]','{"correct":2}','EVALUATE',0.8,'["python-session-jwt-authentication"]'),
('PYTHON_BACKEND','CODE_READING','다음 Django settings.py를 운영 서버에 그대로 배포했을 때 발생할 수 있는 보안 문제는?

from django.shortcuts import render

DEBUG = True
ALLOWED_HOSTS = []

def custom_404(request, exception):
    return render(request, ''404.html'', status=404)','["DEBUG는 로컬 개발 편의를 위한 옵션일 뿐 운영 환경 보안과는 아무 관련이 없다고 오해하기 쉽다.","ALLOWED_HOSTS가 비어 있으면 DEBUG 설정과 무관하게 모든 요청이 자동 차단되어 안전하다고 잘못 알려져 있다.","custom_404 핸들러가 정의돼 있으면 DEBUG 값과 무관하게 항상 그 핸들러만 노출된다고 오해하기 쉽다.","DEBUG=True 상태에서 처리되지 않은 예외가 발생하면, 소스 코드 경로·설정값·스택트레이스 등 민감한 정보가 담긴 디버그 페이지가 그대로 클라이언트에 노출된다."]','{"correct":3}','UNDERSTAND',0.4,'["django-debug-settings"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드를 실행하면 nested_list는 어떻게 되는가?

import copy
nested_list = [[1, 2], [3, 4]]
copied_list = copy.copy(nested_list)
copied_list[0].append(5)
print(nested_list)','["append(5) 호출은 항상 새 리스트를 반환할 뿐 기존 객체는 절대 변경하지 않는다고 오해하기 쉽다.","copied_list와 nested_list는 완전히 독립된 메모리를 쓰기 때문에 서로 영향이 전혀 없다고 잘못 알려져 있다.","copy.copy()는 항상 깊은 복사를 수행하는 함수라서 nested_list는 전혀 변경되지 않는다고 오해하기 쉽다.","copy.copy()는 최상위 리스트만 새로 만들고 내부의 중첩 리스트는 원본과 참조를 공유하므로, copied_list[0]을 변경하면 nested_list에도 반영된다."]','{"correct":3}','ANALYZE',0.5,'["shallow-copy"]'),
('PYTHON_BACKEND','CODE_READING','다음 SQLAlchemy 세션 관리 코드에서는 어떤 문제가 발생할까요?

from sqlalchemy.orm import sessionmaker

Session = sessionmaker(bind=engine)

def get_session():
    db_session = Session()
    return db_session','["요청마다 커넥션 수가 자동으로 5개로 제한되어 예외 없이 안전하게 동작합니다.","get_session 호출 시점에 SQLAlchemy가 자동으로 예외를 발생시켜 세션 누수를 막아줍니다.","세션과 커넥션이 close되지 않고 계속 누적되어 결국 커넥션 풀이 고갈됩니다.","매 호출마다 이전 세션이 자동으로 재사용되어 조회 결과가 정상적으로 반환됩니다."]','{"correct":2}','ANALYZE',0.6,'["sqlalchemy-session-management"]'),
('PYTHON_BACKEND','CODE_READING','두 마이그레이션 파일이 각각 아래와 같이 작성되었다. 이 상태에서 `alembic upgrade head`를 실행하면 어떤 문제가 발생하는가?

# migration_a.py
revision = ''a1b2''
down_revision = ''base_rev''

# migration_b.py
revision = ''c3d4''
down_revision = ''base_rev''','["두 마이그레이션이 같은 down_revision(''base_rev'')을 가리켜 리비전 히스토리에 head가 두 개 생기고, Alembic이 어느 것을 최신으로 적용해야 할지 알 수 없다는 오류를 낸다.","Alembic이 두 마이그레이션 파일의 생성 시간을 자동으로 비교해 시간순으로 정렬한 뒤 아무 충돌 없이 순차 적용한다고 잘못 알려져 있다.","down_revision 값이 서로 같더라도 revision 식별자(a1b2, c3d4) 자체는 다르니 아무 충돌 없이 순서대로 적용된다고 오해하기 쉽다.","먼저 작성된 마이그레이션 파일은 자동으로 무시되고 더 나중에 작성된 파일만 골라서 적용된다고 잘못 알려져 있다."]','{"correct":0}','EVALUATE',0.7,'["alembic-multiple-heads"]'),
('PYTHON_BACKEND','CODE_READING','다음 SQLAlchemy 엔진 설정에서 동시 요청이 10개 몰릴 때 어떤 문제가 발생할 수 있는가?

from sqlalchemy import create_engine, text

engine = create_engine(''postgresql://user:pass@localhost/dbname'', pool_size=5, max_overflow=0)

def fetch_data():
    conn = engine.connect()
    try:
        result = conn.execute(text(''SELECT * FROM users''))
        return result.fetchall()
    finally:
        conn.close()','["max_overflow=0은 오버플로우를 무제한으로 허용한다는 뜻이라 문제가 없다고 오해하기 쉽다.","pool_size는 요청 수와 무관하게 데이터베이스가 자동으로 늘려준다고 잘못 알려져 있다.","conn.close()가 finally에 있으니 커넥션이 항상 즉시 반환돼 풀 부족은 절대 없다고 오해하기 쉽다.","pool_size=5, max_overflow=0으로 최대 5개 커넥션만 허용되어, 6번째 이상 요청은 커넥션을 기다리다 타임아웃될 수 있다."]','{"correct":3}','ANALYZE',0.7,'["sqlalchemy-connection-pooling"]'),
('PYTHON_BACKEND','CODE_READING','다음 Redis 캐시 코드에서는 어떤 문제가 발생할까요?

import redis

cache = redis.Redis()
db = get_database_handle()  # 외부에서 주입되는 DB 핸들

def get_value(key):
    value = cache.get(key)
    if not value:
        result = db.query(key)
        cache.set(key, result, ex=60)
        return result
    return value','["락을 쓰지 않아도 Redis가 내부적으로 요청을 자동 직렬화해줘서 문제가 없다고 잘못 알려져 있다.","캐시 키가 만료된 순간 동시에 몰린 다수의 요청이 전부 캐시 미스를 겪어 db.query가 동시에 여러 번 호출되는 캐시 스탬피드가 발생할 수 있다.","ex=60 옵션이 없으면 캐시가 영구히 만료되지 않아 오히려 안전하다고 오해하기 쉽다.","cache.get()이 항상 예외를 던지도록 구현돼 db.query가 절대 호출 못 된다고 잘못 알려져 있다."]','{"correct":1}','EVALUATE',0.7,'["redis-cache-stampede"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드를 실행하면 어떤 일이 벌어지는가?

def get_user_age(age: int) -> int:
    return age * 2

result = get_user_age(''25'')','["타입 힌트가 문자열을 파이썬 내부적으로 자동 변환해줘서 result가 정수 50이 된다고 오해하기 쉽다.","mypy 같은 별도의 정적 검사기 없이도 인터프리터가 실행 시점에 타입을 검증해 오류를 낸다고 오해하기 쉽다.","타입 힌트가 int로 지정되어 있으니, 인터프리터가 호출 시점에 인자 타입을 검사해 문자열을 넘기면 TypeError를 던진다고 잘못 알려져 있다.","타입 힌트는 런타임에 강제되지 않으므로 문자열 ''25''가 그대로 전달되고, age * 2는 문자열을 반복해 ''2525''를 반환한다."]','{"correct":3}','EVALUATE',0.7,'["type-hint-not-enforced-runtime"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드의 동작으로 옳은 것은?

def generate_data():
    for i in range(10):
        yield i

gen = generate_data()
first_three = [next(gen) for _ in range(3)]
rest = list(gen)','["제너레이터는 멈춘 지점(yield 위치)의 상태를 유지하므로, next()로 3개를 먼저 소비한 뒤 남은 값을 순회하면 rest는 [3, 4, ..., 9]가 된다.","제너레이터는 next()를 한 번이라도 호출하면 상태가 완전히 소진되어, 이후 list(gen)은 항상 빈 리스트만 반환한다고 잘못 알려져 있다. 실제로는 멈춘 지점에서 이어진다.","first_three와 rest 둘 다 range(10) 전체를 담고 있어 두 리스트에 겹치는 값이 있다고 오해하기 쉽다.","제너레이터는 순회할 때마다 매번 처음부터 다시 시작해서 rest에도 [0, ..., 9]가 담긴다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.7,'["generator-behavior"]'),
('PYTHON_BACKEND','CODE_READING','다음 데코레이터를 적용한 뒤 example.__name__을 출력하면 어떻게 되는가?

def simple_decorator(func):
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper

@simple_decorator
def example():
    """This is an example function."""
    pass

print(example.__name__)','["데코레이터가 example을 두 번 호출하도록 만들어서 부작용이 두 번 중복 발생한다고 오해하기 쉽다.","functools.wraps를 쓰지 않아도 파이썬이 원본 함수의 메타데이터를 자동으로 보존해 __name__이 여전히 ''example''로 남는다고 잘못 알려져 있다.","functools.wraps로 감싸지 않아 wrapper가 example을 대체하면서 __name__이 ''wrapper''가 되고 __doc__도 사라진다.","wrapper 함수가 example의 실제 로직을 실행하지 않아 항상 None만 반환하게 된다고 오해하기 쉽다."]','{"correct":2}','ANALYZE',0.7,'["decorator-no-wraps"]'),
('PYTHON_BACKEND','CODE_READING','다음 pytest 픽스처 코드에서는 어떤 문제가 발생할까요?

import pytest

class TestOrders:
    @pytest.fixture(scope=''session'')
    def cart(self):
        items = []
        yield items

    def test_add_item(self, cart):
        cart.append(''apple'')
        assert cart == [''apple'']

    def test_cart_starts_empty(self, cart):
        assert cart == []','["픽스처가 session 스코프이므로 테스트 간에 cart 리스트 상태가 공유되어, 실행 순서에 따라 test_cart_starts_empty가 실패할 수 있다.","pytest가 클래스 안 픽스처는 자동으로 새 cart를 만들어줘서 두 테스트 모두 통과한다고 오해하기 쉽다.","session 스코프도 클래스 컨텍스트에서는 자동으로 function 스코프로 낮춰진다고 잘못 알려져 있다.","픽스처가 제너레이터(yield)면 스코프와 무관하게 매 테스트마다 새로 생성된다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.7,'["pytest-fixtures-scope"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드를 실행하면 어떤 일이 벌어지는가?

class CustomError(object):
    pass

try:
    raise CustomError(''Custom error'')
except Exception as e:
    print(e)','["CustomError가 Exception을 상속하지 않았으니 except Exception 절을 그대로 통과해버려서 예외가 잡히지 않은 채 프로그램이 그대로 종료된다고 오해하기 쉽다.","raise CustomError(...) 시점에 파이썬이 ''exceptions must derive from BaseException'' TypeError를 발생시키고, 이 TypeError가 except Exception에 잡혀 출력된다.","CustomError가 아무 문제 없이 정상적으로 발생하고 except Exception이 이를 그대로 잡아 ''Custom error''라는 문자열을 화면에 곧바로 출력해버린다고 잘못 알려져 있다.","object를 상속한 클래스도 인터프리터가 자동으로 BaseException 서브클래스처럼 취급해줘서 아무 문제 없이 동작한다고 오해하기 쉽다."]','{"correct":1}','UNDERSTAND',0.7,'["custom-exception-must-inherit-baseexception"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드에서 might_fail() 태스크가 예외를 던지면 results 변수에는 무엇이 담기는가?

import asyncio

async def might_fail():
    raise ValueError(''boom'')

async def succeed():
    return ''ok''

async def main():
    results = await asyncio.gather(might_fail(), succeed(), return_exceptions=True)
    print(results)','["return_exceptions=True는 예외를 통째로 삭제해버려 results에는 정상 결과 ''ok'' 하나만 담긴다고 오해하기 쉽다.","return_exceptions=True이므로 gather()는 예외를 던지지 않고, results에 [ValueError(''boom''), ''ok'']처럼 예외 객체와 정상 결과가 함께 담긴다.","return_exceptions 값과 무관하게 첫 예외가 나면 gather()가 즉시 던져 results에는 아무 값도 안 담긴다고 잘못 알려져 있다.","예외가 발생한 태스크는 자동 재시도되어 results에는 정상 결과 두 개만 담긴다고 오해하기 쉽다."]','{"correct":1}','ANALYZE',0.7,'["asyncio-gather-exceptions"]'),
('PYTHON_BACKEND','CODE_READING','다음 Django 시그널 리시버에서는 어떤 문제가 발생할까요?

from django.db.models import signals
from .models import Product

def product_saved(sender, instance, created, **kwargs):
    if not created:
        instance.last_synced = True
        instance.save()

signals.post_save.connect(product_saved, sender=Product)','["instance.save()가 post_save 시그널을 다시 트리거해 무한 재귀 호출되고 결국 RecursionError로 이어질 수 있다.","업데이트된 제품에 한해서만 시그널 리시버가 정확히 한 번 호출되고, Django가 재귀 호출을 자동으로 감지해 두 번째 호출부터 조용히 무시해준다고 오해하기 쉽다.","제품 저장은 시그널 등록 여부와 무관하게 언제나 정확히 딱 한 번만 수행된다고 잘못 알려져 있다.","뷰 함수 쪽에서 처리되지 않은 예외가 나서 요청 전체가 실패로 끝난다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.7,'["django-signal-recursion"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드에서 lambdas[0](1)의 결과는 무엇인가?

numbers = [1, 2, 3]
lambdas = []
for number in numbers:
    lambdas.append(lambda x: x + number)
print(lambdas[0](1))','["for문이 끝나는 순간 number 변수 자체가 사라져서 lambdas[0](1) 호출 시 NameError가 난다고 오해하기 쉽다.","람다는 number를 지연 바인딩(late binding)하므로 모든 람다가 반복문 종료 시점의 마지막 number 값(3)을 참조해, lambdas[0](1)은 4를 반환한다.","리스트에 저장된 람다들은 언제나 numbers의 첫 번째 값(1)만 사용해 2를 반환한다고 잘못 알려져 있다.","각 람다가 생성 시점의 number 값을 즉시 복사해 저장하므로 서로 다른 값으로 2를 반환한다고 오해하기 쉽다."]','{"correct":1}','ANALYZE',0.7,'["lambda-late-binding"]'),
('PYTHON_BACKEND','CODE_READING','다음 함수를 여러 번 호출할 때 어떤 문제가 발생하는가?

def add(item, items=[]):
    items.append(item)
    return items

add(''a'')
add(''b'')','["함수를 호출할 때마다 items 매개변수가 매번 새로운 빈 리스트로 초기화된다고 알려져 있다.","add 함수는 호출될 때마다 완전히 독립적인 items 리스트를 만들어 반환하므로 문제가 없다고 오해하기 쉽다.","items 매개변수의 기본값(list)은 함수 정의 시점에 단 한 번만 생성되어 호출 간에 공유·누적된다.","append() 호출 직후 items가 자동으로 새 빈 리스트로 교체되어 이전 값이 사라진다고 잘못 알려져 있다."]','{"correct":2}','ANALYZE',0.7,'["mutable-default-argument"]'),
('PYTHON_BACKEND','CODE_READING','다음 FastAPI 엔드포인트에서는 어떤 문제가 발생할까요?

from fastapi import APIRouter
import time

router = APIRouter()

@router.get(''/slow'')
async def read_slow():
    time.sleep(5)
    return {''message'': ''done''}','["time.sleep(5)는 5초가 지나면 파이썬이 자동으로 이를 취소해 응답이 즉시 반환된다고 오해하기 쉽다.","time.sleep()이 코루틴이 아니어서 서버가 곧바로 TypeError를 던지며 요청 자체가 실패한다고 잘못 알려져 있다.","time.sleep()은 동기(블로킹) 호출이라 5초 동안 이벤트 루프 전체가 멈춰, 같은 워커가 처리 중인 다른 요청들도 함께 지연된다.","async def로만 선언하면 FastAPI가 함수 안의 모든 동기 호출을 알아서 별도 스레드로 옮겨 실행해준다고 오해하기 쉽다. 실제로는 그런 자동 감지 기능이 없다."]','{"correct":2}','ANALYZE',0.7,'["fastapi-blocking-calls"]'),
('PYTHON_BACKEND','CODE_READING','다음 엔드포인트를 호출했을 때, some_background_task 내부에서 예외가 발생하면 클라이언트는 어떤 응답을 받는가?

from fastapi import APIRouter, BackgroundTasks

def some_background_task():
    raise ValueError(''background failure'')

router = APIRouter()

@router.post(''/endpoint'')
def endpoint(background_tasks: BackgroundTasks):
    background_tasks.add_task(some_background_task)
    return {''status'': ''Task started''}','["add_task 함수는 응답을 만들기 전에 즉시 동기적으로 먼저 실행돼, 예외가 나면 엔드포인트가 실패한다고 오해하기 쉽다.","예외가 발생하면 FastAPI가 자동으로 재시도해 성공할 때까지 응답을 지연시킨다고 잘못 알려져 있다.","백그라운드 작업은 응답이 클라이언트로 전송된 이후에 실행되므로, 예외가 발생해도 클라이언트는 이미 200과 {''status'': ''Task started''}를 받은 뒤다.","백그라운드 작업의 예외는 응답 전송 전에 처리돼 클라이언트가 500 오류를 받는다고 오해하기 쉽다."]','{"correct":2}','ANALYZE',0.8,'["fastapi-background-tasks"]'),
('PYTHON_BACKEND','CODE_READING','다음 인증 함수를 실행하면 만료된 토큰에 대해 어떤 결과가 나오는가?

import jwt

SECRET = ''my-secret''

def authenticate(token):
    try:
        payload = jwt.decode(token, SECRET, algorithms=[''HS256''], options={''verify_exp'': False})
        return True
    except jwt.InvalidSignatureError:
        return False','["서명이 유효하지 않으면 만료 여부와 무관하게 언제나 인증에 실패한다고만 알려져 있어 다른 함정은 놓치기 쉽다.","options 파라미터는 서명 알고리즘 이름만 지정하는 용도이며 만료 검증과는 전혀 무관하다고 오해하기 쉽다.","options={''verify_exp'': False}로 인해 만료 시간 검증이 꺼져 있어, 서명만 유효하면 만료된 토큰도 인증에 통과한다.","PyJWT는 언제나 만료 시간을 강제로 검증하므로 이 옵션은 실질적으로 아무 효과가 없다고 잘못 알려져 있다."]','{"correct":2}','ANALYZE',0.8,'["jwt-expiration-not-verified"]'),
('PYTHON_BACKEND','CODE_READING','다음 FastAPI 의존성 주입에서는 어떤 동작이 일어날까요?

from fastapi import APIRouter, Depends

call_count = 0

async def get_user_data():
    global call_count
    call_count += 1
    return {''count'': call_count}

router = APIRouter()

@router.get(''/users'')
async def read_users(a=Depends(get_user_data), b=Depends(get_user_data)):
    return {''a'': a, ''b'': b}','["call_count 값이 요청이 들어올 때마다 자동으로 0으로 초기화된다고 오해하기 쉽다.","두 개의 Depends를 한 엔드포인트에서 동시에 쓰면 FastAPI가 예외를 던진다고 잘못 알려져 있다.","get_user_data가 매 요청마다는 물론 같은 요청 안 a와 b 계산 때도 매번 새로 호출된다고 오해하기 쉽다.","같은 요청 안에서 get_user_data가 두 번 쓰였지만 FastAPI가 결과를 캐시해 실제로는 한 번만 호출되므로 a와 b는 같은 값을 갖는다."]','{"correct":3}','UNDERSTAND',0.8,'["fastapi-dependency-injection-caching"]'),
('PYTHON_BACKEND','CODE_READING','이 태스크가 네트워크 오류로 실패해 Celery가 자동으로 재시도하면 어떤 문제가 발생할 수 있는가?

from celery import shared_task
from .models import User

@shared_task(bind=True, max_retries=3)
def add_credit(self, user_id, amount):
    user = User.objects.get(id=user_id)
    user.balance += amount
    user.save()','["balance 필드는 재시도 시점마다 자동으로 초기화되어 금액이 누적되지 않는다고 오해하기 쉽다.","user.balance += amount 연산은 실행할 때마다 잔액을 더하므로, 재시도로 같은 태스크가 두 번 실행되면 금액이 중복 반영된다.","Celery는 같은 태스크 ID의 재시도를 자동 감지해 중복 실행을 스스로 막아준다고 잘못 알려져 있다.","재시도가 일어나면 user_id 자체가 자동으로 바뀌어 다른 사용자에게 적립된다고 오해하기 쉽다."]','{"correct":1}','EVALUATE',0.8,'["celery-idempotency"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드를 프로덕션에서 실행하면 종종(항상은 아니게) notify_later가 실행되지 않고 조용히 사라진다. 원인은 무엇인가?

import asyncio

async def notify_later(user_id):
    await asyncio.sleep(5)
    await send_notification(user_id)

async def handle_event(user_id):
    asyncio.create_task(notify_later(user_id))
    return {''status'': ''accepted''}','["asyncio.create_task()로 만든 태스크의 참조를 어디에도 보관하지 않아, 이벤트 루프의 가비지 컬렉션이 태스크를 실행 도중 수거해버려 콜백이 예고 없이 취소될 수 있다.","await asyncio.sleep(5)는 5초가 지나기 전에 런타임이 강제로 타임아웃시켜 send_notification 호출이 아예 이뤄지지 못한다고 오해하기 쉽다. 실제로는 타임아웃이 아니라 참조 소실이 원인이다.","asyncio.create_task()는 handle_event가 반환된 뒤에는 코루틴을 예약할 수 없어 태스크가 애초에 생성되지 않는다고 잘못 알려져 있다.","send_notification 안 예외가 이미 완료된 handle_event의 응답까지 역전파되어 응답을 소급 취소시킨다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.9,'["asyncio-task-lost-reference"]'),
('PYTHON_BACKEND','CODE_READING','다음 설정에서 send_report 태스크가 정상 처리되고 있음에도(예외 없이) 같은 작업이 다른 워커에 또 배달되어 두 번 실행되는 일이 반복된다. 왜 그런가?

# celeryconfig.py
broker_transport_options = {''visibility_timeout'': 30}  # 초

@app.task(acks_late=True)
def send_report(report_id):
    generate_and_send(report_id)  # 평균 90초 소요','["acks_late=True는 태스크가 실제로 실패했을 때만 재전달을 트리거하는 옵션이라서, 정상적으로 처리되고 있는 태스크가 중복 배달될 리는 전혀 없으며 visibility_timeout 값 자체가 무의미해진다고 오해하기 쉽다.","acks_late=True에서는 워커가 태스크를 ''완료한 뒤''에 ack을 보낸다. visibility_timeout(30초)이 이 태스크의 평균 처리 시간(90초)보다 훨씬 짧아, 브로커가 30초 안에 ack을 못 받으면 다른 워커가 아직 죽지 않은 태스크를 재실행 대상으로 간주해 다시 배달한다.","visibility_timeout이라는 설정은 결과 백엔드 쪽에만 영향을 줄 뿐 태스크가 실제로 배달되는 방식과는 아무 관련이 없으며 브로커는 이 값을 참조조차 하지 않는다고 잘못 알려져 있다.","코드 안에 명시적인 재시도 로직이 없으니 Celery가 이를 스스로 감지해 안전을 위해 알아서 태스크를 복제한 뒤 여러 워커에서 동시에 병렬로 실행하도록 판단해서 처리한다고 오해하기 쉽다."]','{"correct":1}','EVALUATE',0.9,'["celery-acks-late-visibility-timeout"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드에서 두 번째 for 루프는 몇 번의 추가 쿼리를 실행하는가?

qs = Order.objects.prefetch_related(''items'').filter(status=''paid'')

for order in qs.iterator():
    pass

for order in qs:
    print(order.items.count())','["iterator()로 이미 한 번 순회했으므로 그 결과가 내부 결과 캐시에 자동으로 남아, 두 번째 루프는 추가 쿼리 없이 캐시된 결과만 그대로 재사용한다고 오해하기 쉽다. 실제로는 iterator가 캐시를 아예 만들지 않는다.","iterator()는 쿼리셋의 내부 결과 캐시를 채우지 않고 매번 새로 스트리밍하므로, 두 번째 for 루프는 order 목록을 다시 조회하는 쿼리 1번과, prefetch가 다시 적용되지 않아 order.items.count() 호출마다 별도 쿼리가 추가로 발생한다.","iterator()를 한 번이라도 호출한 쿼리셋은 그 뒤로 완전히 재사용이 금지되어 두 번째 순회를 시도하는 시점에 곧바로 예외가 발생한다고 잘못 알려져 있다.","prefetch_related는 iterator() 사용 여부와 완전히 무관하게 항상 내부적으로 결과를 캐시해두므로, 두 번째 순회에서도 캐시된 prefetch 결과가 재사용되어 추가 쿼리가 전혀 발생하지 않는다고 오해하기 쉽다."]','{"correct":1}','ANALYZE',0.9,'["django-queryset-iterator-prefetch"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드에서 마지막 Item.objects.create(sku=''B1'') 줄에서 어떤 일이 벌어지는가?

from django.db import transaction, IntegrityError
from .models import Item

def create_items():
    with transaction.atomic():
        Item.objects.create(sku=''A1'')
        try:
            Item.objects.create(sku=''A1'')  # sku는 unique 제약
        except IntegrityError:
            pass
        Item.objects.create(sku=''B1'')
    return ''done''','["atomic 블록 안에서 IntegrityError를 그 자리에서 잡았으니 트랜잭션 상태가 완전히 정상으로 복구되어 B1 생성도 아무 문제 없이 수행된다고 오해하기 쉽다.","atomic 블록 내부에서 DB 수준 예외가 발생하면 해당 트랜잭션이 rollback 대상으로 표시되어, 이후 같은 블록 안의 쿼리 실행 시 TransactionManagementError가 발생한다.","A1이라는 sku가 중복 생성되어 unique 제약을 어긴 채로 데이터베이스에 두 레코드가 그대로 남는다고 잘못 알려져 있다.","IntegrityError는 Django의 트랜잭션 매니저가 알아서 조용히 무시해버려서 이후 코드는 영향받지 않는다고 오해하기 쉽다."]','{"correct":1}','ANALYZE',0.9,'["django-atomic-exception-inside-block"]'),
('PYTHON_BACKEND','CODE_READING','다음 엔드포인트를 호출하면 응답 JSON에 is_admin 필드가 포함되는가?

class UserOut(BaseModel):
    id: int
    email: str

class AdminUser(BaseModel):
    id: int
    email: str
    is_admin: bool

@app.get(''/users/{user_id}'', response_model=UserOut)
def get_user(user_id: int):
    return AdminUser(id=user_id, email=''a@b.com'', is_admin=True)','["함수가 실제로 반환한 값이 is_admin 필드까지 포함한 AdminUser 인스턴스이므로 그 필드도 그대로 응답에 실린다고 오해하기 쉽다.","response_model=UserOut이 실제로 반환된 값의 타입(AdminUser)과 정확히 일치하지 않으므로, FastAPI가 이를 스키마 위반으로 간주해 응답 직렬화 단계에서 즉시 500 오류를 던진다고 잘못 알려져 있다.","response_model은 문서화용 스키마 생성에만 관여할 뿐 실제 직렬화 결과는 반환값 그대로 나간다고 오해하기 쉽다.","FastAPI는 response_model에 선언된 필드(id, email)만 직렬화해 응답에 담고, is_admin은 조용히 걸러져 응답에 포함되지 않는다."]','{"correct":3}','EVALUATE',0.9,'["fastapi-response-model-field-truncation"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드에서 태스크가 외부에서 task.cancel()로 취소되면 finally 블록은 실행되는가, 그리고 except Exception은 취소를 잡아 삼키는가?

async def worker():
    conn = await acquire_connection()
    try:
        while True:
            await process_next(conn)
    except Exception:
        logger.exception(''worker failed'')
    finally:
        await conn.close()','["asyncio.CancelledError는 파이썬 3.8부터 Exception이 아닌 BaseException을 직접 상속하므로 except Exception에 잡히지 않고 그대로 전파되며, finally의 conn.close()는 정상적으로 실행된다.","취소 시 CancelledError는 여전히 Exception의 서브클래스로 취급되어서 except Exception이 이를 그대로 잡아 로깅한 뒤 루프를 조용히 종료한다고 오해하기 쉽다.","task.cancel()이 호출되는 순간 런타임이 finally 블록 실행을 건너뛰고 즉시 태스크를 종료해버려서 conn.close()는 절대 호출되지 않는다고 잘못 알려져 있다.","CancelledError는 async 함수 문맥 안에서는 애초에 발생할 수 없는 개념이며 task.cancel() 호출은 사실상 아무 효과가 없다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.9,'["asyncio-cancelled-error-baseexception"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드는 주문 저장 후 이메일을 보낸다. 이 코드를 TestCase 기반 테스트에서 실행하면 이메일 발송 함수가 호출되는가?

from django.db import transaction

def place_order(order):
    order.save()
    transaction.on_commit(lambda: send_order_email(order.id))

class OrderTest(django.test.TestCase):
    def test_place_order_sends_email(self):
        place_order(make_order())
        assert email_was_sent()','["on_commit에 등록한 콜백은 save() 함수 호출 직후 예외 없이 즉시 동기적으로 실행되므로, 이 테스트는 어떤 상황에서도 항상 통과한다고 오해하기 쉽다.","django.test.TestCase는 각 테스트를 트랜잭션으로 감싸고 테스트가 끝나면 항상 롤백하므로, 실제 커밋이 일어나지 않아 on_commit 콜백이 전혀 실행되지 않는다. 운영에서는 되는데 테스트에서만 실패하는 전형적인 원인이다.","on_commit은 Django 시그널 시스템의 일종이라서 signals.py 파일에 별도로 등록하지 않으면 어떤 경우에도 무시된다고 잘못 알려져 있다.","TestCase는 각 테스트 메서드가 시작·종료될 때마다 실제 데이터베이스에 진짜 커밋을 수행하도록 설계되어 있어, 콜백도 매 테스트마다 정상 호출된다고 오해하기 쉽다."]','{"correct":1}','ANALYZE',0.9,'["django-on-commit-hook"]'),
('PYTHON_BACKEND','CODE_READING','Order 모델은 nullable ForeignKey인 coupon(Coupon, null=True)을 갖고, Coupon은 여러 Order와 연결되는 역참조 관계다. 다음 코드에서 문제가 되는 부분은?

orders = Order.objects.select_related(''coupon'', ''coupon__campaigns'')

for order in orders:
    print(order.coupon.code if order.coupon else ''no coupon'')
    for campaign in order.coupon.campaigns.all() if order.coupon else []:
        print(campaign.name)','["select_related(''coupon'')는 coupon 필드가 null인 주문을 만나는 순간 예외를 던져버려서 이 쿼리 자체가 실행 도중 실패한다고 오해하기 쉽다. 실제로는 nullable FK에도 select_related가 정상적으로 동작한다.","coupon__campaigns가 역참조(reverse FK) 또는 M2M 관계라면 select_related로는 가져올 수 없어 무시되고, campaigns.all() 순회 시 각 쿠폰마다 별도 쿼리가 실행되는 N+1이 그대로 발생한다. 그 경우 prefetch_related로 바꿔야 한다.","coupon이 nullable FK이니 select_related가 항상 INNER JOIN을 강제해서 coupon이 없는 주문은 결과 집합에서 자동으로 완전히 제외되고 반환되지 않는다고 잘못 알려져 있다. 실제로는 LEFT OUTER JOIN을 사용한다.","select_related에 필드를 여러 개 한꺼번에 넘기면 두 번째 인자부터는 조용히 무시되어 coupon만 조인되고 campaigns는 애초에 요청조차 되지 않는다고 오해하기 쉽다. 실제로는 두 필드 모두 처리를 시도한다."]','{"correct":1}','ANALYZE',0.9,'["django-select-related-limits"]'),
('PYTHON_BACKEND','CODE_READING','다음 코드를 실행하면 마지막 print(user.name)에서 어떤 일이 벌어지는가?

from sqlalchemy.orm import sessionmaker

Session = sessionmaker(bind=engine)  # expire_on_commit 기본값(True) 그대로

def get_username(user_id):
    with Session() as session:
        user = session.query(User).get(user_id)
        session.commit()
    return user

user = get_username(1)
print(user.name)','["expire_on_commit 기본값(True) 때문에 commit() 시점에 user 인스턴스의 속성이 만료(expire)되고, with 블록을 벗어나 세션이 닫힌 뒤 user.name에 접근하면 새로 SELECT를 시도하다가 DetachedInstanceError가 발생한다.","session.commit()은 인스턴스 속성에 어떤 영향도 주지 않으므로 print(user.name)은 세션이 닫힌 뒤에도 항상 처음에 캐시된 값을 그대로 출력한다고 오해하기 쉽다. 실제로는 expire_on_commit 기본값이 속성을 만료시킨다.","with 블록을 벗어나 세션이 닫히는 순간 user 객체 자체가 자동으로 None으로 바뀌어버려서, print(user.name)이 AttributeError를 낸다고 잘못 알려져 있다. 실제로는 객체 자체는 살아있고 속성 접근만 실패한다.","get_username이 값을 반환하기 전에 SQLAlchemy가 세션이 추적하던 모든 속성 값을 미리 순수 파이썬 타입으로 자동 복사해두어서, 세션이 닫힌 뒤에도 print(user.name)이 언제나 안전하게 출력된다고 오해하기 쉽다."]','{"correct":0}','ANALYZE',0.9,'["sqlalchemy-expire-on-commit"]'),
('PYTHON_BACKEND','CODE_READING','다음 gunicorn 설정으로 서비스를 띄운 뒤 얼마 지나지 않아 워커들이 무작위로 ''connection already closed'' 또는 ''server closed the connection unexpectedly'' 오류를 던진다. 가장 유력한 원인은?

# gunicorn.conf.py
preload_app = True
workers = 4

# app.py (모듈 최상단, import 시점에 실행됨)
engine = create_engine(DATABASE_URL)
db_connection = engine.connect()','["preload_app=True는 오히려 각 워커가 자기 몫의 커넥션을 새로 여는 것 자체를 원천적으로 차단해버려서 워커가 뜰 때마다 언제나 즉시 오류가 발생한다고 오해하기 쉽다. 실제로는 커넥션을 여는 것을 막지 않는다.","workers=4로 설정하기만 하면 gunicorn이 데이터베이스 커넥션 풀 크기를 자동으로 4등분해 각 워커에게 정확히 나눠 배분해주므로 별도 설정이 전혀 필요 없다고 잘못 알려져 있다.","preload_app=True는 마스터 프로세스가 앱을 한 번만 로드한 뒤 fork로 워커를 만든다. 이때 import 시점에 이미 연 DB 커넥션(db_connection)까지 그대로 fork되어, 여러 워커가 같은 소켓을 공유하다가 한쪽이 사용하거나 닫으면 다른 워커의 연결이 깨진다.","gunicorn은 preload_app 설정과 무관하게 fork() 시점에 항상 부모의 모든 전역 변수(열린 커넥션 포함)를 완전히 새로 초기화해 자식에게 넘기도록 설계되어 있어, 이 코드에서는 충돌이 절대 일어날 수 없다고 잘못 알려져 있다."]','{"correct":2}','ANALYZE',0.9,'["gunicorn-preload-app-fork"]'),
('NODE_TYPESCRIPT','MCQ','Express.js 애플리케이션에 미들웨어를 추가할 때 주로 사용하는 함수 이름은 무엇인가?','["app.use()","app.addMiddleware()","app.middleware()","useApp()"]','{"correct":0}','REMEMBER',0.1,'["expressjs-middleware"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript에서 두 개의 유니온 타입 A와 B가 주어졌을 때, 이들을 교차(Cross)하는 방법은?','["A & B","A + B","A | B","union<A,B>"]','{"correct":0}','REMEMBER',0.1,'["typescript-union-intersection"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript에서 ''unknown'' 타입은 어떤 용도로 사용됩니까?','["변수의 타입을 모를 때 사용합니다.","any 타입과 동일하게 모든 유형에 대해 호환됩니다.","불확실한 값이나 데이터 입력 시 검사해야 할 경우 사용합니다.","never 타입과 마찬가지로 존재할 수 없는 상태를 표현하는 데 사용됩니다."]','{"correct":2}','REMEMBER',0.1,'["unknown"]'),
('NODE_TYPESCRIPT','MCQ','스트림(Stream)을 처리하기 위해 Node.js에서 Buffer 클래스의 어떤 메서드가 자주 이용되는가?','["write()","pipe()","toString()","concat()"]','{"correct":1}','REMEMBER',0.15,'["nodejs-streams-buffer"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 작업이 동기를 대체하는 주요 이점은 무엇인가?','["성능 향상","코드의 가독성 증가","자원 사용량 감소","응답 시간 단축"]','{"correct":0}','REMEMBER',0.15,'["nodejs-asynchronous-programming"]'),
('NODE_TYPESCRIPT','MCQ','Node.js의 process.nextTick() 메서드는 무엇을 호출하는 데 사용되는가?','["기존 콜백 함수 대신 실행할 새로운 콜백 함수를 설정한다.","다음 이벤트 루프 반복에서 처리해야 할 작업을 추가한다.","즉시 실행된 후 다음 주 동작이 수행되기 전에 실행될 작업을 정의한다.","현재 메서드가 종료되면 즉시 호출된다."]','{"correct":2}','REMEMBER',0.17,'["nodejs-process-nexttick"]'),
('NODE_TYPESCRIPT','MCQ','TS의 타입 애너테이션 ''@module''은 무엇을 정의하는가?','["모듈 수준에서 변수나 함수의 타입을 지정한다.","클래스의 멤버 함수에 대한 반환타입을 명시적으로 선언한다.","ES6 모듈 내부에서 import/export 문법을 사용하여 다른 모듈과 상호작용할 때 필수적인 타입 정보를 제공한다.","주석 블록 안에 추가적인 메타데이터 정보를 포함합니다."]','{"correct":2}','REMEMBER',0.18,'["typescript-modules"]'),
('NODE_TYPESCRIPT','MCQ','Node.js 모듈 시스템에서 CommonJS 와 ESM 의 주요 차이점은 무엇인가?','["CommonJS 는 `require`를 사용하고, ESM 은 `import/export 문법을 사용한다.","ESM 만 비동기 방식으로 동작하며, CommonJS는 동기화된다.","CommonJS는 .js 확장자를 지원하지만 ESM은 .mjs 를 요구한다.","모듈 캐시와 로딩 시점이 다르다."]','{"correct":0}','UNDERSTAND',0.3,'["node-module-system"]'),
('NODE_TYPESCRIPT','MCQ','스트림(Stream) 작업에서 백프레셔(backpressure)는 어떤 상황에 발생하나?','["데이터를 소비하는 쪽이 데이터 생산자보다 느린 경우.","스트림을 통한 모든 요청이 성공적으로 처리된 후.","소스 스트림의 크기 제한을 초과할 때.","응답 스트림이 대량의 데이터로 인해 멈춤."]','{"correct":0}','UNDERSTAND',0.3,'["node-streams-backpressure"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 클래스 메서드를 가르키는 타입을 정확히 표현하려면 어떻게 해야 하나?','["`Function`\n\n    `Function`만 사용하면 실제 함수의 구체적 형태에 대한 정보가 사라진다.","`(this: ClassName, args...) => returnType;`\n\n    메서드에서 사용할 this 타입과 인자 반환 타입을 함께 지정한다.","`ClassName.methodName`","`(args) => returnType`"]','{"correct":1}','UNDERSTAND',0.3,'["typescript-function-types"]'),
('NODE_TYPESCRIPT','MCQ','Node.js의 이벤트 루프에서 `setTimeout(fn, 0)`은 어떻게 동작하나?','["함수 fn이 즉시 실행된다.","현재 단계(`poll` 또는 `check`)가 끝난 후 마이크로 태스크 대기열에 추가된다.","다음 매크로태스크까지의 디바운싱을 한다.","코드가 정상적으로 작동하지 않게 만든다."]','{"correct":1}','UNDERSTAND',0.3,'["node-event-loop"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 `type` 속성이 올바르게 설정되었는지를 확인하는 TypeScript 유틸리티 타입은 무엇인가?','["Partial<T>","Pick<T, K>","Record<K,T>","ReturnType<>()"]','{"correct":1}','UNDERSTAND',0.3,'["typescript-utility-types-pick"]'),
('NODE_TYPESCRIPT','MCQ','Express.js 애플리케이션에서 미들웨어의 실행 순서를 결정하는 요소는 무엇입니까?','["1) 미들웨어가 등록된 순서","2) 라우트 매핑 우선순위","3) 미들웨어의 이름 길이","4) 요청 URL의 정규표현식 패턴"]','{"correct":0}','UNDERSTAND',0.3,'["express-middleware-order"]'),
('NODE_TYPESCRIPT','MCQ','다음 TypeScript 코드에서 `Person` 타입의 인스턴스는 어떠한 조건을 가진가?
type Person = {
  name: string;
  age?: number;
};','["name 이 string, age 는 필수적으로 number.","name 이 string, age 는 선택적 number.","age 이 number로 존재해야 하지만 name 은 없어도 됨.","age 와 함께 필수적인 다른 속성이 필요하다."]','{"correct":1}','UNDERSTAND',0.3,'["typescript-optional-properties"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript의 `readonly<T>` 타입을 사용할 때, 다음 중 올바른 설명은 무엇인가?','["T 속성 모두 읽기 전용으로 제한","T 객체 자체를 읽기 전용으로 변경한다.","변수에 대한 T 타입에서 읽기 전용 속성을 정의합니다.","read-only는 배열 내부 원소도 함께 제한합니다."]','{"correct":2}','UNDERSTAND',0.3,'["typescript-readonly"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript 에서 `unknown` 와 `any` 의 주요 차이는 무엇인가?','["`unknown`은 모든 타입과 호환되지만, 안전하게 처리하기 위해 좁히기 필요.","`any`는 어떤 작업도 실행 가능하지만, 런타임 오류를 발생시킬 위험이 있다.","`unknown`을 사용하면 코드의 가독성이 향상되고, 모든 타입과 호환된다.","`any`은 안전성을 위해 좁히기가 필요하며, `unknown` 은 그렇지 않다."]','{"correct":1}','UNDERSTAND',0.35,'["typescript-unknown-type"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript에서 `unknown` 타입은 어떤 상황에 주로 사용됩니까?','["변수의 타입이 아직 알려지지 않은 경우","어떤 값도 할당될 수 있는 유연한 변수를 만들 때","타입 안전성을 보장하기 위해 런타임에서 모든 값을 검사할 필요가 있을 때","배열이나 객체 내부 타입을 정의하는 상황"]','{"correct":2}','UNDERSTAND',0.35,'["typescript-unknown-type"]'),
('NODE_TYPESCRIPT','MCQ','Express.js 미들웨어에서 4번째 인자를 받은 함수에 대한 역할은 무엇인가?','["응답 객체를 반환하는 경로 설정.","요청 객체만을 수정하고 계속 진행한다.","요청과 응답 모두 수정하며, 처리 중단.","다음 미들웨어 함수 호출을 무시한다."]','{"correct":2}','UNDERSTAND',0.35,'["express-middleware"]'),
('NODE_TYPESCRIPT','MCQ','비동기 작업이 여러 개가 있을 때, 일부 작업에 실패하면 즉시 에러 처리하는 방법으로 적절한 API는 무엇인가?','["Promise.all()","Promise.any()","Promise.race()","Promise.allSettled()"]','{"correct":1}','UNDERSTAND',0.35,'["node-asynchronous-control-any-promise"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 `never` 타입은 어떤 상황에서 사용되는가?','["함수의 반환 타입으로 쓰여 함수의 종료를 의미하거나 에러 처리에 활용한다.","비동기 작업이 완료될 때까지 대기를 위한 특별한 타입이다.","타입스크립트에서 유효하지 않은 타입이나 결론 없음 상태만을 표현하기 위해 쓰인다.","클래스 생성자 함수의 반환 타입으로 사용된다."]','{"correct":0}','UNDERSTAND',0.35,'["typescript-never-type"]'),
('NODE_TYPESCRIPT','MCQ','Express.js의 미들웨어 체인에서, 오류 핸들링 라우터는 어떤 형태를 취하나?','["`function(err: Error, req: Request, res: Response) => void;`\n\n    이 모듈은 오직 에러만을 처리하고 실행되지 않을 때 사용된다.","`(req: Request, res: Response, next: NextFunction) => Promise<void>;`\n\n    비동기 작업이 완료된 후에 예외를 처리하게 한다.","`function(err: Error, req: Request, res: Response, next: NextFunction) => void;`\n\n    오류 발생 시 사용되며 에러와 요청 및 응답 객체를 받는다.","`(req: Request, err?: Error, res: Response) => void;`\n\n    모든 가능성을 처리하는데 유용하며, 예외가 없을 수도 있다."]','{"correct":2}','UNDERSTAND',0.35,'["express-error-handling"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기적으로 작업을 처리할 때, `Promise`와 `async/await` 구문 중 어떤 것이 순차적인 코드 작성에 더 적합한가?','["`Promise`\n\n    프로미스는 비동기 상태를 관리하기 위한 최소 단위로서 데드락 위험을 증가시킬 수 있다.","`async/await`\n\n    `async/await`은 비동기에 동기적인 코드 작성 스타일이 가능하도록 만들어진 구문으로 순차적 처리에 적합하다.","둘 다 비슷한 역할을 하지만, 특정 상황에서 더 효율적인 기법이 있다.","비교 불가능\n\n    `Promise`와 `async/await`는 동등하게 비동기 작업 처리를 위해 사용되며 각각의 장단점은 없다."]','{"correct":1}','UNDERSTAND',0.35,'["node-asynchronous-programming"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 ''never'' 타입은 언제 사용됩니까?','["1) 함수가 결코 반환하지 않는 경우","2) 모든 가능한 유니온 타입을 나타낼 때","3) 변수를 아직 초기화하지 않은 상태일 때","4) 객체의 특정 프로퍼티를 무시할 수 없게 할 때"]','{"correct":0}','UNDERSTAND',0.35,'["typescript-never-type"]'),
('NODE_TYPESCRIPT','MCQ','Express 미들웨어에서 4개 인자(`req`, `res`, `next`)가 모두 필요할 때, 각각 어떤 역할을 하는지 제대로 설명한 것은?','["요청 객체, 응답 객체, 오류 처리 함수.","응답 객체, 요청 객체, 프로세스 종료 함수.","모든 HTTP 메소드의 공통 처리를 위한 컨트롤러.","요청 객체, 응답 객체, 다음 미들웨어 호출을 위해 사용되는 함수."]','{"correct":3}','UNDERSTAND',0.35,'["express-middlewares"]'),
('NODE_TYPESCRIPT','MCQ','Node.js 프로세스에서 `setTimeout` 이 실행되는 타이밍은 언제인가?','["다음에 바로 실행되는 마이크로태스크.","지정된 시간 경과 후 매크로태스크로서 큐에 추가됨.","프로세스 초기 직후에 가장 먼저 실행 됨.","Promise.then 의 이후 빈도를 결정하는 타이밍으로 설정된다."]','{"correct":1}','UNDERSTAND',0.4,'["node-event-loop"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 `unknown` 타입은 어떤 상황에서 유용하게 쓸 수 있나?','["스트링이나 숫자 같은 원시 타입과 구분할 때 사용.","비교적 안전한 값을 받을 때 무언가를 대체해야 할 때 사용.","타입이 불확실하거나 확인하기 어려운 경우, 향후 형상화나 검사를 위해 적합하다.","환경 변수처럼 외부 입력에 대한 동적으로 결정되는 타입의 복잡한 경우"]','{"correct":3}','UNDERSTAND',0.4,'["typescript-unknown-type"]'),
('NODE_TYPESCRIPT','MCQ','Promise.all이 여러 프ром리스를 받았을 때 어떤 경우에 거부(reject)된다?','["모든 프로미스가 거절될 때.","하나라도 거절되면 즉시 거절한다.","프로미스들이 모두 성공하면 거절한다.","여러 프로미스 중 하나만 실패해도 거절되지 않는다."]','{"correct":1}','APPLY',0.5,'["node-asynchronous-control"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 프로세스 모델에 대한 설명 중 올바른 것은 무엇인가?','["단일 스레드 멀티태스킹 환경이다.","다중 쓰레드를 통해 병렬 처리가 가능하다.","worker_threads 모듈을 사용하면 동기적으로 작업이 수행된다.","프로세스는 비동기에만 작동한다."]','{"correct":0}','APPLY',0.5,'["node-process-model"]'),
('NODE_TYPESCRIPT','MCQ','NestJS에서 컨트롤러와 서비스 간 의존성 주입(injection)이 일어날 때 사용되는 DI(Dependency Injection) 모듈은 다음과 같다.','["CommonJS 모듈 시스템","@nestjs/common과 @nestjs/core","Express 미들웨어 팩토리","TypeORM"]','{"correct":1}','APPLY',0.5,'["nestjs-dependency-injection"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 `User` 타입의 모든 프로퍼티를 옵셔널(`?`)으로 만드는 유틸리티 타입은 무엇인가?
type Nullable<T> = { [P in keyof T]?: T[P]; }','["Partial<User>","Pick<User, keyof User>","Nullable<User>","Readonly<User>"]','{"correct":2}','APPLY',0.5,'["typescript-utility-types"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 작업을 제어하기 위해 사용할 수 있는 메소드 중 하나는 `Promise.all` 이다. 여러 프라미스를 동시에 실행하고 모두 성공해야 다음 단계로 넘어갈 때 사용하는 것이 있다.','["Promise.allSettled()","Promise.race()","Promise.any()","Promise.all()"]','{"correct":3}','APPLY',0.5,'["promise-api"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 TypeScript의 readonly 키워드를 사용하면 어떻게 되는가? ```ts const person = {readonly name: ''Alice'', age: 25};```','["name 속성은 읽기만 가능하게 된다.","age 속성도 읽기 전용으로 변한다.","person 객체 자체가 readonly로 바뀐다.","이 코드는 TypeScript에서 유효하지 않다."]','{"correct":0}','APPLY',0.5,'["typescript-readonly"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 Pick 타입은 무엇을 제공하는가?','["특정 프로퍼티만 포함된 객체의 타입.","모든 키를 readonly 속성으로 만드는 타입.","객체 내의 일부 값을 제외한 나머지 타입을 정의한다.","비어있는 인터페이스 또는 타입이다."]','{"correct":0}','APPLY',0.5,'["typescript-utility-types"]'),
('NODE_TYPESCRIPT','MCQ','스트림 처리에서 백프레셔(backpressure)는 스트림이 데이터의 속도에 따라 읽기나 쓰기를 제어하는 기제로 작동한다. `ReadableStream` 객체가 생성되면 끊임없이 데이터를 제공하기 때문에, 이를 제어해야 하는 방법은 무엇인가.','["스트림을 바로 종료한다","데이터 소비자가 스트림의 속도에 맞춰 읽기 위한 시그널을 발생시킨다","스트림에서 직접 데이터 처리량을 늘린다","Stream API를 사용하지 않고 파일 시스템으로 직접 쓴다"]','{"correct":1}','APPLY',0.5,'["nodejs-streams"]'),
('NODE_TYPESCRIPT','MCQ','Node.js 이벤트 루프에서 `process.nextTick`과 `Promise.then`은 어떤 차이점으로 인해 동작 방식이 다르게 되는가?','["macro-task 대 micro-task 순서의 우선순위","스케줄링 타입의 편리성","비동기 작업 처리 시간","실행 시점에 의존성"]','{"correct":0}','APPLY',0.52,'["process-nexttick-promise-then-nodejs"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 배열 내부 요소를 비동기적으로 처리하는 함수는 어떤 것인가?
promises.forEach(async (promise) => await promise;)
주어진 `promises`의 모든 요소가 성공할 때까지 기다리는 가장 효과적인 방법은 무엇일까?','["Promise.all(promises)","promises.map(promise => promise.catch(() => null))","promises.reduce((acc, curr) => acc.then(curr), Promise.resolve())","promises.filter(async (promise) => await promise;).length === promises.length"]','{"correct":0}','APPLY',0.53,'["async-await-promise","node-asynchronous-control-flow"]'),
('NODE_TYPESCRIPT','MCQ','타입스크립트에서 `readonly` 키워드를 사용하여 객체 프로퍼티의 읽기 전용성을 강제하는 경우, 이는 어떤 상황을 방지합니다.','["불필요한 속성 수정","타이핑 오류 무시","클래스 인스턴스 간 의존성 문제","속성을 삭제하거나 재할당하기"]','{"correct":0}','APPLY',0.54,'["readonly-property-typescript"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 함수 내부에서 `throw new Error(''error'')`가 발생했을 때 이를 처리하기 위해선 어떤 문법을 사용해야 하는지?
async function example() {
  // ...
}

example().then(() => console.log(''resolved''))
.catch((err) => { 
    console.error(err);
});','["Promise.prototype.catch()",".finally()","throw를 try/catch 블록 안에서 실행","(이미 정답인) .catch()"]','{"correct":3}','APPLY',0.56,'["async-await-promise-error-handling"]'),
('NODE_TYPESCRIPT','MCQ','TypeScript에서 어떤 유형의 키와 값이 모두 문자열인 객체 타입은 어떻게 정의할 수 있을까?','["Record<string, string>","Pick<object, ''string''>","Map<string, string>","Partial<{}>"]','{"correct":0}','APPLY',0.56,'["typescript-record-partial-type"]'),
('NODE_TYPESCRIPT','MCQ','익스프레스 미들웨어에서 오류를 전달하기 위한 핸들러는 몇 개의 파라미터를 받나?','["2개","3개","4개","5개"]','{"correct":2}','APPLY',0.57,'["express-middleware-error-handling"]'),
('NODE_TYPESCRIPT','MCQ','Express에서 라우팅 경로를 정할 때, 루트 경로 `/`와 `/:id` 로 구분되는 두 개의 GET 요청 핸들러가 동시에 정의되었을 경우, 먼저 일치하는 경로는 어디서부터 검사되나?','["경로 우선순위에 따라 결정","정확한 매칭에서부터 와일드카드로 이동","/:id 루트를 무시하고 / 처리","/:id의 모든 요청을 먼저 보냄"]','{"correct":1}','APPLY',0.57,'["express-routing-priority"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 `process.nextTick` 이 실행되는 시점을 고려하면, 어떤 함수가 먼저 실행되나?
```javascript
function firstFunction() {
  console.log(''First function'');
}
setTimeout(firstFunction);
process.nextTick(() => console.log(''Next tick''));
```','["firstFunction","nextTick 콜백","둘 다 동시에","정답이 없음"]','{"correct":1}','APPLY',0.57,'["node-event-loop"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 `Promise.all`과 비슷하지만 하나라도 거부되면 즉시 모든 작업이 취소되는 메서드는 무엇인가?','["Promise.race","Promise.any","Promise.settled","AbortController"]','{"correct":1}','APPLY',0.58,'["promise-all-settled-nodejs"]'),
('NODE_TYPESCRIPT','MCQ','스트림 API를 사용하여 대용량 파일을 읽고 쓸 때 발생하는 주요 문제 중 하나인 백프레셔(backpressure)는 무엇을 의미하나?','["데이터의 오버플로우 방지","서버와 클라이언트 간 통신 지연 시간 최소화","스트림 데이터가 잘못 전송될 위험 감소","파일 시스템에서 I/O 에러를 줄이는 것"]','{"correct":0}','APPLY',0.58,'["nodejs-streams-backpressure"]'),
('NODE_TYPESCRIPT','MCQ','Node.js 프로그램이 `SIGTERM` 시그널을 받았을 때, 모든 쓰레드가 정상적으로 종료될 수 있도록 필요한 방법은 무엇인가?','["process.exit()","throw new Error(\"shutdown\");","process.on(''SIGTERM'', () => process.abort());","require(''worker_threads'').Worker.terminateAll()"]','{"correct":1}','APPLY',0.59,'["node-process-model-graceful-shutdown"]'),
('NODE_TYPESCRIPT','MCQ','Node.js의 cluster 모듈이 주로 어떤 문제를 해결하나?','["복잡한 순환 참조를 처리하기 위해.","프론트엔드 라우팅 로직을 분리하기 위한 용도이다.","웹 스케일링 시 여러 프로세스 간 작업 분배 및 병렬화한다.","비동기 요청의 성능 최적화에 활용된다."]','{"correct":2}','APPLY',0.6,'["node-process-model"]'),
('NODE_TYPESCRIPT','MCQ','스트림 모듈에서 백프레셔(backpressure)는 무엇을 의미하는가?','["데이터를 무시할 수 있는 상황.","스트림이 데이터의 전송 속도를 제어한다.","스트림은 항상 최대한 많은 데이터를 처리해야 한다.","스트림은 모든 입력을 보장하며 버퍼링하지 않는다."]','{"correct":1}','APPLY',0.6,'["node-streams-and-buffers"]'),
('NODE_TYPESCRIPT','MCQ','모듈 시스템에 대한 이해 중 올바르지 않은 것은 무엇인가?','["CommonJS 모듈은 동기적으로 로딩되며, ESM는 비동기로 로딩된다.","package.json의 main 필드는 기본으로 실행할 모듈을 지정한다.","순환 의존성은 모든 모듈이 정상적으로 초기화될 때까지 불가능하다.","최상위 await를 사용하면 프로세스 종료 시점에서 비동기 작업 완료를 기다릴 수 있다."]','{"correct":2}','ANALYZE',0.7,'["module-system"]'),
('NODE_TYPESCRIPT','MCQ','다음 TypeScript 코드에서 ''value'' 변수의 타입은 무엇인가?
```ts
const obj = { value: 123 };
type ObjValue<T> = T extends object ? (T[keyof T] | undefined) : never;
type Value = ObjValue<typeof obj>;
```','["number","(number|undefined)","never","any"]','{"correct":1}','ANALYZE',0.7,'["typescript-utilities-types"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 `process.nextTick` 의 역할은 무엇인가?

```typescript
function asyncFunc() {
  console.log(''Task1'');
}
setTimeout(asyncFunc, 0);
asyncFunc();
setImmediate(() => console.log(''task2''));
```
- 이 코드에서 `console.log` 의 출력 순서는?','["''Task1'', ''task2''","''task2'', ''Task1''","''Task1'', 콘솔에 아무 것도 출력되지 않음","''task2'', 콘솔에 아무 것도 출력되지 않음"]','{"correct":0}','ANALYZE',0.7,'["node-process-model"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 프로세스 모델에 대한 이해는 어떻게 달라질 수 있는가?','["단일 스레드 모드에서는 비동기 작업이 순차적으로 실행된다.","worker_threads를 사용하면 모든 비동기 호출은 병렬로 처리될 것이다.","SIGTERM 신호를 받으면 프로세스는 자동으로 종료되지만 unhandled rejection에 대해서는 그렇지 않다.","프로세스 모델은 개발 환경과 동일하게 운영환경에서 작동한다."]','{"correct":2}','ANALYZE',0.7,'["process-model"]'),
('NODE_TYPESCRIPT','MCQ','스트림과 버퍼에 대한 이해 중 옳지 않은 것은 무엇인가?','["Buffer는 고정 길이로 데이터를 저장한다.","pipeline 함수는 복잡한 스트림의 연결을 단순화할 수 있다.","백프레셔는 출력된 데이터와 입력되는 데이터 양 사이에 항상 일치해야 한다.","스트리밍 응답을 사용하면 대용량 파일을 메모리에 올릴 필요가 없다."]','{"correct":2}','ANALYZE',0.75,'["streams-buffers"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 코드를 작성할 때 다음 중 제어 흐름을 올바르게 이해하고 있는 것은 무엇인가?','["비동기 콜백은 반드시 try-catch로 감싸서 사용해야 한다.","Promise.all이 거부된 한 개의 Promise만 취소한다.","await를 이용한 순차적인 비동기 작업에서는 병렬 처리 기회를 잃을 수 있다.","모든 비동기 함수는 unhandled rejection을 발생시킬 가능성이 있다."]','{"correct":2}','ANALYZE',0.75,'["async-control"]'),
('NODE_TYPESCRIPT','MCQ','다음 TypeScript 코드에서 ''UserDetails'' 인터페이스는 어떤 종류의 타입 시스템을 사용하고 있는가?
```ts
interface UserDetails {
  name?: string;
  age: number | null;
}
const userDetails: UserDetails = {};
details.name; // (string | undefined)
details.age; // (number | null)```
','["타입 단언(typed assertions)","확장된 타입(extension types)","필수 값은 대체 가능하고 옵셔널 속성의 조합으로 표현됨.","매핑 형식(mapping types)"]','{"correct":2}','ANALYZE',0.75,'["typescript-interface-declarations"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 프로세스 모델이 어떻게 동작하는지 설명하십시오.

```typescript
const spawn = require(''child_process'').spawn;

function startServer() {
  const childProcess = spawn(''node'', [''app.js''], {stdio: ''inherit''});
}
```
- 위 코드에서 `startServer` 함수가 실행되면서 발생하는 프로세스 상태는?','["서버 프로세스와 부모 프로세스가 동일한 메모리 공간을 공유","부모 프로세스가 종료될 때 자식 프로세스도 함께 종료된다.","자식 프로세스의 표준 입력, 출력, 오류를 부모 프로세스에게 전송한다.","서버 프로세스가 실행되고 부모 프로세스는 다른 작업을 계속 진행"]','{"correct":2}','ANALYZE',0.75,'["node-process-model"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 테스크의 우선순위를 비교할 때, `process.nextTick`과 `Promise.then` 사이에 어떤 차이점이 있나?
```ts
console.log(''A'');
promise1.then(() => console.log(''B''));
timer1 = setTimeout(() => console.log(''C''), 0);
nextTickCallback(); // process.nextTick 호출
process.on(''exit'', () => console.log(''D''));```
위 코드에서 출력 순서는?
','["A -> B -> C -> D","A -> D -> B -> C","A -> C -> B -> D","A -> B -> D -> C"]','{"correct":2}','ANALYZE',0.75,'["node-event-loop"]'),
('NODE_TYPESCRIPT','MCQ','비동기 연산을 병렬적으로 수행하려고 `Promise.all`에서 배열 안의 거부를 처리하고 나머지 요청은 계속 진행시키는 코드 조각을 보았습니다. 그 구조에 맞게 다음 중 어떤 방법이 올바르게 동작할까요?
```ts
const results = await Promise.all(promises.map(p => p.catch(err => console.error(''Failed'', err))));```
','["모든 promise들이 catch 되어 처리되며 나머지는 진행됨.","Promise.any로 대체되어야 함.","catch 안에서의 에러 처리는 각각 별개라는 점을 고려해야 하므로, 비효율적입니다.","결과 배열이 undefined들로 채워질 것입니다."]','{"correct":2}','ANALYZE',0.75,'["typescript-promise-handling"]'),
('NODE_TYPESCRIPT','MCQ','다음 Node.js 코드에서 ''cluster'' 모듈을 사용하여 쓰레드 별로 분산 처리를 수행하는데, 각 워커 프로세스는 어떻게 서로 다른 요청들을 처리할 수 있나?
```ts
const cluster = require(''cluster'');
count = 1;
if (cluster.isMaster) {
    console.log(`Master ${count} is running`);
    count++;
 
   for(let i=0; i<4; i++){
       const worker = cluster.fork();
       worker.send(''some data'');
      }
}
else if(cluster.isWorker){
  process.on(''message'', function(data) {
    console.log(`Received ${data} from master`); 
});```
','["워커 프로세스는 채널을 통해 서로 통신하며, 각각 다른 요청을 처리한다.","마스터가 워커에게 특정 작업을 분배하는 로직이 필요하다.","워커들은 공유 메모리 공간을 사용하여 상태를 유지하며 서로 다른 요청을 다룬다.","각 마스터와 워커는 별도의 프로세서에서 실행되어 독립적으로 동작한다."]','{"correct":1}','ANALYZE',0.75,'["node-cluster-module"]'),
('NODE_TYPESCRIPT','MCQ','다음 스트림 코드 스니펫에서 `pipeline` 메소드의 역할은 무엇인가?

```typescript
const fs = require(''fs'');
const { pipeline } = require(''stream''); 
pipeline(  // 파일을 읽고, 변환하여 쓰기 위한 파이프라인이 생성됨.
    fs.createReadStream(''/etc/passwd''),   
    new Transform({                 
        objectMode: true,
        transform(chunk, enc, cb) {
            this.push(`Received ${chunk.toString()}\n`);
            return cb();
        }
    }),  
    fs.createWriteStream(''/file/output'') ,
    (err) => { if (err) console.error(''pipeline failed'', err);});
```
- 이 스트림 코드에서 `pipeline()` 메소드의 주요 기능은?','["스트림들을 연결하고, 순차적으로 처리 및 쓰기","스트림들을 병렬로 동시에 처리한다.","쓰레드를 생성하여 각 스트림을 독립적으로 관리","스트림들 사이의 데이터 전송만 수행"]','{"correct":0}','ANALYZE',0.76,'["node-streams-and-buffers"]'),
('NODE_TYPESCRIPT','MCQ','Express API에서 다음 코드 조각이 어떤 작동을 하는지 설명하십시오.

```typescript
app.get(''/users'', (req, res) => {
  User.find({}, function(err: any, users?: Array<any>) { 
    if (!err && users.length > 0) {
      res.json(users);
    } else {
      res.status(404).send(''Not Found'');
    }
  });
});
```
- 이 코드에서 만약 DB 쿼리가 실패하거나, 유저 목록이 비어있다면 HTTP 응답은?','["200 OK 상태와 유저들 JSON 배열","404 Not Found","500 Internal Server Error","301 Moved Permanently"]','{"correct":1}','ANALYZE',0.78,'["node-web-frameworks","http-api"]'),
('NODE_TYPESCRIPT','MCQ','다음 TypeScript 코드 스니펫에서 `AbortController` 객체가 어떻게 활용되는지 설명하십시오.

```typescript
class NetworkClient {
  private controller?: AbortController;
 
  async request(url: string, signal: AbortSignal) {
    this.controller = new AbortController();
    const abortTimeoutId = setTimeout(() => {this.abortRequest() },1000);
    return fetch(url, {signal}); // 비동기 요청이 여기서 발생
}
  
 private abortRequest(){
     console.log(''Request timed out.'');
     this.controller?.abort();
 }
```
- 위 코드에서 `AbortController` 객체는 어떤 역할을 수행하며 타이머에 의해 비동기 요청은 어떻게 처리되는지?','["타이머 후 abort() 메소드를 호출하여 모든 요청 취소","비동기 콜백만 중단되고, 다른 요청들은 계속 진행","request method에서 제공된 `AbortSignal`을 통해 요청의 상태를 확인","''abort'' 이후에도 fetch request가 완료될 때까지 기다림"]','{"correct":0}','ANALYZE',0.78,'["node-asynchronous-control","http-api"]'),
('NODE_TYPESCRIPT','MCQ','다음의 TypeScript code 스니펫에서 `Promise.all`과 Promise 객체들이 어떻게 작동하는지 설명하십시오.

```typescript
const promises: Array<Promise<number>> = [];
promises.push(Promise.resolve(1));
promises.push(new Promise((resolve) => setTimeout(() => resolve(2), 500)));
promises.push(Promise.reject(3)); 
await Promise.all(promises); // 이 코드가 실행될 때 발생하는 결과는?','["결과 배열 [1, undefined]을 반환하고 모든 promise 가 해결됨","Promise.all 은 첫 번째 거부를 받고 즉시 종료한다.","첫 번째 거부 이후 비동기 처리 후의 2를 포함한 결괏값이 반환됨","무한히 실행된다"]','{"correct":1}','ANALYZE',0.79,'["node-asynchronous-control"]'),
('NODE_TYPESCRIPT','MCQ','Node.js의 비동기 I/O 작업과 스트림 버퍼링 사이에서 발생할 수 있는 백프레셔(backpressure) 문제를 해결하기 위한 적절한 방법은?
```ts
const readable = fs.createReadStream(''bigfile.txt'', { highWaterMark: 256 });
readable.on(''data'', chunk => process.stdout.write(chunk));```
위 코드에서 백프레셔가 발생하지 않게 하기 위해서는?','["process.stdout.write에 Buffer를 직접 넘겨준다","chunk의 크기를 줄여 스트림의 highWaterMark 값을 낮춘다.","chunks 사이에 적절한 기록을 추가한다.","data 이벤트 핸들러에서 읽어온 데이터를 처리하는 비동기 작업이 완료될 때까지 대기를 강제로 한다"]','{"correct":3}','ANALYZE',0.8,'["node-stream-backpressure"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드에서 발생할 오류를 설명하라.

```typescript
import fs from ''fs'';
const data = fs.readFileSync(''/nonexistent/file'', {encoding: ''utf8''});
class DataProcessor {
    constructor(private rawData: string) {}
}
new DataProcessor(data);
```
위 코드의 실행 결과는 무엇인가?','["1. 에러 없이 정상 동작.","2. `fs.readFileSync` 호출 시 파일이 존재하지 않아서 런타임 오류 발생.","3. TypeScript 컴파일 타입 검사에서 데이터가 undefined로 초기화되지 않을 경우의 경고를 받는다.","4. 자바스크립트 엔진이 프로세스 종료시에 비정상적으로 종료된다."]','{"correct":2}','EVALUATE',0.9,'["typescript-strict-null-checks"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드 조각은 어떤 문제를 나타내나?
```typescript
import type { Pool } from ''pg'';
class DatabaseConnection {
  private pool: Promise<Pool>;
  constructor() {
    this.pool = new Promise((resolve) => {
      require(''pg'').connect(process.env.DATABASE_URL, (err, client) => resolve(client));
    });
  }

  public async query(sqlQuery: string): Promise<object[]> { 
    const poolClient = await this.pool; // 비동기적으로 클라이언트를 획득합니다.
    try {
      return (await poolClient.query(sqlQuery)).rows;
    } finally {
      poolClient.release();
    }
  }
}
```
- 위 코드는 PostgreSQL Client 객체와 Promise의 구문을 이용한 비동기 데이터베이스 연결입니다.','["비동기적으로 획득된 클라이언트 `poolClient`를 동일하게 처리하려는 시도로 인해 실수성 높은 코드가 됩니다.","쿼리 결과로 받아온 객체 배열 `rows`의 타입 정보가 명시되어 있지 않아서 TypeScript 컴파일러에 의해 문제가 발생합니다.","`query` 메소드에서 Promise 사용 시 비동기 처리를 제대로 이해하지 못해 비정상적인 프로그램 흐름이 발생할 수 있습니다.","비동기 처리 과정에서 잘못된 클라이언트 반환으로 인해 실제 데이터베이스 연결의 문제가 발생합니다."]','{"correct":1}','EVALUATE',0.9,'["db-orm"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드 조각에서, 어떤 타입의 문제가 발생하나?
```typescript
function getUserProfile(userId: string): Promise<User | null> {
  return db.query(`SELECT * FROM users WHERE id = $1`, [userId], (error, results) => {
    if(error) throw error;
    return results.rows[0];
  });
}
```','["사용자가 존재하지 않을 때 `null`을 반환하기 때문에 타입 오류가 발생한다.","결과 객체의 형상이 명시적으로 지정되지 않아서 코드는 컴파일할 수 없다.","Promise 결과에 대한 타입 정보가 부족하여 TypeScript가 이를 검증할 수 없다.","db.query 호출 시 전달되는 콜백 함수에서 반환하는 값의 타입과 Promise 의 리턴 타입이 불일치한다."]','{"correct":3}','EVALUATE',0.9,'["typescript-type-system"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드 스니펫에서 어떤 부분이 안정적인 연결 관리를 위한 최선의 방법을 제안하지 못하고 있나?
```typescript
import { Pool } from ''pg'';
class DBConnection {
  pool: Pool;
  constructor(connectionString) {
    this.pool = new Pool({ connectionString });
  }
}
class UserManagementDB extends DBConnection {
  async getUserProfile(userId): Promise<UserProfile> {
    const result = await this.query(`SELECT * FROM users WHERE user_id=$1`, [userId]);
    return { userId: result.rows[0].user_id, name: result.rows[0].name };
  }
}
class AuthDB extends DBConnection {
  async authenticate(username, password): Promise<JwtToken> {
    // 여기서 JWT 토큰 발급 로직이 있음
  }
}```
- 위 코드 스니펫은 여러 데이터베이스 연결을 위해 클래스와 확장 클래스를 이용한 설계입니다.','["`DBConnection` 생성자의 `this.pool = new Pool({ connectionString });` 부분에서 비동기 작업 처리가 제대로 이루어지지 않음","클래스 확장을 통해 인터페이스로 교체할 수 있는 코드 구조임에도 불구하고 실제 구현부는 클래스 기반인 문제점","비밀번호 확인 로직이 별도의 비즈니스 로직으로 분리되지 않은 흔한 보안 취약점 이슈","`UserManagementDB.authenticate()`에서 인자 타입 추론 오류로 인해 런타임에 문제가 발생할 수 있음."]','{"correct":0}','EVALUATE',0.9,'["typescript-type-system"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드 스니펫은 어떤 문제점을 드러내나?
```typescript
const db = require(''pg'').Pool;

db.query(`SELECT * FROM users WHERE active=true`, (err, result) => {
  if(err) throw err;
  console.log(result.rows);
});
```
- `db`는 PostgreSQL 데이터베이스 풀 객체입니다.','["결과 집합의 모든 행을 내부적으로 메모리에 로드하므로 대용량 데이터 처리 시 성능 저하가 발생한다.","비동기 쿼리를 동기식으로 처리하려고 하여 Node.js 이벤트 루프를 차단할 위험이 있다.","`active=true` 조건문에서 타입 안전성이 보장되지 않아서 컴파일 오류가 발생합니다.","결과 객체 `result.rows`에 대한 접근이 동기적으로 이루어지므로 결과 처리 시간 지연이 있을 수 있다."]','{"correct":0}','EVALUATE',0.9,'["buffer-streams"]'),
('NODE_TYPESCRIPT','MCQ','다음 코드 스니펫에서 어떤 부분이 잘못 작성되었나?
```typescript
import { strict as assert } from ''assert'';
type Config = {
  port: number;
  dbUrl: string;
}
const configFromEnv: Partial<Config> = process.env.NODE_ENV === ''test''
  ? { port: 4001, mockDbUrl: ''mock://db'' }
  : { realPort: parseInt(process.env.PORT as unknown as string), dbUrl: process.env.DATABASE_URL! };
assert.deepEqual(configFromEnv.port !== undefined && configFromEnv.dbUrl !== undefined);```
- `process.env`는 환경 변수 객체입니다.','["Partial 타입을 사용하면서 모든 필드를 정의하지 않아서 Assertion 오류가 발생한다.","Assertion 확인 부분이 잘못 작성되어 있어 실제로는 아무런 검증도 이루어지지 않는다.","환경 변수에서 값에 대한 형변환 처리 과정에서 runtime error 가 발생할 가능성이 있다.","dbUrl 키 값을 무시한 채로 mockDbUrl을 사용하려 하여 타입 오류가 발생한다."]','{"correct":1}','EVALUATE',0.9,'["typescript-type-system"]'),
('NODE_TYPESCRIPT','MCQ','Node.js에서 비동기 작업을 제어하는 방법 중 가장 효과적인 하나가 무엇인가?
```typescript
import { AbortController } from ''abort-controller'';
const controller = new AbortController();
const signal = controller.signal;

fetch(''https://example.com/api'', {
  signal,
}).then(response => response.json()).catch(error => console.log(`Request aborted: ${error.message}`));
controller.abort(); // 요청 중단
```','["Promise.all()를 사용하여 여러 작업을 동시에 실행한다.","setTimeout 또는 setInterval로 주기적으로 비동기 작업의 상태를 체크한다.","process.nextTick()을 사용해 동기적 코드 블록에서 바로 다음 단계로 이동시킨다.","abortController와 signal API를 통해 특정 요청의 중간에 그만두는 것을 가능하게 한다."]','{"correct":3}','EVALUATE',0.9,'["web-frameworks"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `foo` 함수가 호출되면 반환하는 값은 무엇인가?

```typescript
function foo() {
  return new Promise((resolve, reject) => setTimeout(() => resolve(10), 50));
}
```

setTimeout이 주어진 시간 이후에 실행되므로, `foo` 함수에서 반환되는 promise는 `then` 메서드를 통해 결과가 반환된다.','["Promise 객체","number 10","undefined","null"]','{"correct":1}','REMEMBER',0.1,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드가 실행될 때 프로세스 이벤트 루프에서 발생하는 단계를 고려하세요.

setTimeout(() => {
  console.log(''timer task'');
}, 0);
process.nextTick(() => {
  console.log(''nextTick task'');
});
console.log(''immediate task'');','["''immediate task'', ''timer task'', ''nextTick task''","''immediate task'', ''nextTick task'', ''timer task''","''nextTick task'', ''immediate task'', ''timer task''","''nextTick task'', ''timer task'', ''immediate task''"]','{"correct":2}','REMEMBER',0.1,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `createReadStream()` 메서드를 사용하여 파일을 읽으려고 할 때 발생하는 동작은 무엇인가?

```typescript
const fs = require(''fs'');
const readStream = fs.createReadStream(''/path/to/file'', {encoding: ''utf8''});
readStream.on(''data'', (chunk) => {
  console.log(chunk.length);
});```

이 코드는 데이터가 버퍼로 읽혀진 후 이벤트 핸들러를 통해 콘솔에 출력된다.','["파일의 전체 길이가 한 번에 콘솔에 출력됨","데이터가 별도의 페이치 단위로 버퍼링되고, 각각이 ''data'' 이벤트에서 처리됨","파일 내용이 직접 메모리에 로드되어 읽힘","스트림 작업이 실패하고 에러 발생"]','{"correct":1}','REMEMBER',0.2,'["node-streams-buffers"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드는 비동기 호출의 순서를 출력합니다.

async function logOrder() {
    console.log(''First'');
    await new Promise((resolve) => setTimeout(resolve, 10));
    console.log(''Second'');
    process.nextTick(() => console.log(''Third''));
}
logOrder();

이 코드의 실행 결과로 올바른 것은?
','["First Second Third","First Third Second","Third First Second","First Second"]','{"correct":0}','UNDERSTAND',0.3,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드는 스트림을 사용하여 파일을 읽고 쓰려 합니다.

const fs = require(''fs'');
const readStream = fs.createReadStream(''input.txt'', {encoding: ''utf-8''});
codepipeline(readStream, fs.createWriteStream(''output.txt''));
function codepipeline(stream, out) {
    stream.on(''data'', (chunk) => {
        out.write(chunk.toUpperCase());
    });
}

위 코드에서 백프레셔(backpressure)가 발생할 수 있는 상황은?
','["출력 파일이 느리게 쓰일 때","입력 스트림 데이터를 정상적으로 읽을 때","시스템 메모리를 완전히 사용할 때","파일 시스템에 오류가 발생했을 때"]','{"correct":0}','UNDERSTAND',0.3,'["node-streams-buffers"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드는 비동기 콜백으로 오류 처리를 시도합니다.

async function readData() {
    try{
        const data = await fs.promises.readFile(''file.txt'');
        console.log(data.toString());
    } catch (error) {
        console.error(`Error reading file: ${error.message}`);
    }
}
readData();

이 코드에서 unhandled rejection 오류가 발생하는 상황은 어떤 것인가요?
','["데이터 파일이 존재하지 않을 때","데이터를 읽을 수 없는 시스템 제한에 도달했을 때","예외 처리 블록 안에서 예외가 무시되었을 때","위 모든 경우"]','{"correct":0}','UNDERSTAND',0.3,'["node-error-handling"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드 스니펫의 동작으로 옳은 것을 선택하세요.

```typescript
function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(() => resolve(), ms));
}

async function example() {
  console.log(''Start'');
  await delay(100);
  console.log(''Mid'');
  await delay(200);
  console.log(''End'');
}
```
`example()` 함수의 출력 순서는 무엇인가요?','["''Start''       ''Mid''     ''End''","''Start'';  100ms 뒤 ''Mid'';  200ms 뒤 ''End''","''Start'';  300ms 뒤 전체적으로 한번에 '' ''Mid '' ''End;","위 코드는 오류를 발생시킵니다."]','{"correct":1}','UNDERSTAND',0.35,'["nodejs-timers"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드는 비동기 콜백을 사용하여 작업의 상태를 확인합니다.

function checkFile(file, callback) {
    fs.stat(file, (err, stats) => {
        if (!err && stats.isFile())
            callback(null, true);
        else if(err)
            return callback(`Error: ${err}`);
        else 
            callback(null, false);  // 이 라인은 파일이 디렉토리일 때 처리됨
    });
}
checkFile(''some/path'', (message, isFile) => {
    console.log(message ? message : `${isFile} file detected`);
}); 
위 코드에서 callback 함수가 실행될 시점과 파라미터는?
','["파일이 존재하지 않을 때 실패 메시지 전달","파일의 경로에 접근할 수 없는 경우 오류 메세지를 반환","파일이 디렉토리인 경우, isFile을 false로 처리하고 메세지 없음.","위 모두"]','{"correct":3}','UNDERSTAND',0.4,'["node-error-handling","callback-functions"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드는 비동기 호출의 결과를 출력합니다.

async function logResult() {
    try{
        console.log(''Start'');
        await new Promise((resolve) => setTimeout(resolve, 0)); // 주의: 여기서 바로 실행되지 않을 수 있습니다.
        console.log(''Middle'');
    } catch (error) {} 
    console.log(''End'');
}
logResult();

이 코드의 예상 출력 결과는 무엇인가요?
','["Start Middle End","Start End Middle","Start End","Error Start End"]','{"correct":2}','UNDERSTAND',0.4,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드가 비동기를 처리하는 방식은 무엇인가?

const fetchData = async () => {
  try{
    const response = await fetch(''http://example.com/data'');
    return response.json();
  } catch(error) {
    console.error(`Error fetching data: ${error}`);
  }
};','["비동기 요청에서 오류가 발생할 때 예외를 처리하고 로그를 기록한다.","비동기 요청의 결과를 단순히 반환하거나 콘솔에 출력한다.","비동기 작업을 순차적으로 실행하며, 하나의 작업이 실패하면 모두 중단된다.","비동기 호출에서 오류가 발생해도 아무런 처리 없이 통과한다."]','{"correct":0}','APPLY',0.5,'["async-await"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 함수에서 Promise.allSettled의 역할은 무엇인가?

const promises = [
  fetch(''http://api.example.com/data1''),
  fetch(''http://api.example.com/data2'')
];
Promise.allSettled(promises).then(results => {
  results.forEach(result => console.log(`Result: ${result.status}`));
});','["비동기 작업들이 모두 완료되었을 때, 각 요청의 성공 여부와 상태를 수집한다.","여러 비동기 작업들 중 하나라도 실패하면 모든 나머지 작업들을 취소하고 오류 처리 후 종료한다.","모든 Promise가 동시에 실행되도록 보장하며 그 결과는 첫 번째로 해결된 순서대로 반환된다.","비동기적인 모든 태스크들이 완료되어야만 다음 단계를 진행하도록 스프링보드 역할을 한다."]','{"correct":0}','APPLY',0.5,'["promise-allsettled"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 비동기 작업의 실행 순서는 어떻게 되나?

```typescript
function asyncTask1() {
  return new Promise((resolve) => setTimeout(resolve, 20));
}
function asyncTask2() {
  return new Promise((resolve) => process.nextTick(resolve));
}
asynctask2().then(() => console.log(''async task2 resolved''));
asynctask1().then(() => console.log(''async task1 resolved''));
```','["process.nextTick이 먼저 실행되고, 그 후에 setTimeout으로 인해 Promise가 해결된다.","setTimeout이 먼저 실행되지만, process.nextTick을 통해 즉시 처리된 다음에 코드가 계속 진행된다.","두 함수 모두 동기적으로 실행되어 두 작업은 동시에 해결된다.","결과는 랜덤하며 실행 환경에 따라 달라진다."]','{"correct":1}','APPLY',0.5,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 스트림 코드의 동작으로 옳은 것은?

const fs = require(''fs'');
fs.createReadStream(''./bigfile.txt'')
  .on(''data'', chunk => {
    console.log(`Chunk received: ${chunk.length} bytes`);
  })
  .pipe(fs.createWriteStream(''./output.txt''));
','["스트림은 대량의 데이터를 메모리에 모두 로딩하지 않고 부분적으로 처리할 수 있다.","스트림이 개별 채크로 전달되는 모든 데이터는 파일 시스템에서 즉시 순서대로 덮어쓰기 된다.","스트림을 처리하는 동안 다른 스트림 작업은 중단되며, 이 작업 이후에만 재개된다.","스트림에서 읽어온 뒤 작성하는 과정이 완전히 비동기를 유지한다."]','{"correct":0}','APPLY',0.5,'["stream-processing"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드 스니펫에서, `process.nextTick` 이 호출되면 바로 실행되는 것은 어떤 작업을 의미합니까?

```typescript
function task() {
  console.log(''Task started'');
}

setTimeout(() => {
  process.nextTick(task);
}, 0);
```
','["마이크로태스크로서 직접 실행됨","매크로태스크의 끝에서 직접 실행됨","다음 이벤트 루프 루턴에 등록됨","타이머 설정 후 바로 실행됨"]','{"correct":0}','APPLY',0.5,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 process.nextTick 과 setTimeout(0) 의 차이는 무엇인가?

process.nextTick(() => console.log(''Next tick'')); // 1.
setTimeout(() => { console.log(''Timeout''); }, 0); // 2.
console.log(''Immediate execution'');
','["timeout은 비동기 작업으로 기능이 실행되는데, nextTick는 동기적으로 코드 블록 내에서 처리된다.","두 개의 호출 모두 즉시 이벤트 루프 타스크 콜백 테이블에 추가되고 순서대로 실행된다.","process.nextTick은 같은 CPU 사이클 내에서 실행하지만 setTimeout는 기다려야 함.","timeout(0)과 nextTick은 비슷한 동작을 하지만 timeout은 더 느리고 우선순위가 낮다."]','{"correct":2}','APPLY',0.5,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드의 `process.nextTick` 와 `Promise` 가 섞인 순서로 출력되는 메시지를 정확히 선택하시오.

```typescript
function logAfterDelay(ms: number, message: string) {
  setTimeout(() => console.log(message), ms);
}

logAfterDelay(0, ''timeout delay 0'');
process.nextTick(() => console.log(''next tick''));
promise1 = new Promise(resolve => resolve()).then(() => console.log(''promise then''));
promise2 = new Promise((resolve) => setTimeout(resolve, 5, ''delayed resolve'')).then(() => {
  logAfterDelay(0, ''inside promise timeout delay 0'');
});```
','["''next tick'', ''timeout delay 0'', ''promise then'', ''inside promise timeout delay 0''","''timeout delay 0'', ''next tick'', ''promise then'', ''inside promise timeout delay 0''","''next tick'', ''promise then'', ''timeout delay 0'', ''inside promise timeout delay 0''","순서가 불확실해 예측할 수 없습니다."]','{"correct":0}','APPLY',0.54,'["node-event-loop"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 TypeScript 코드 스니펫을 분석하고 결과를 추론하라.

```typescript
interface Person {
  name: string;
}
type Greeting<T> = T extends {name: infer U} ? `Hello, ${U}` : never;
class Greeter<P extends Person> {
  greet(person: P): void {
    console.log(Greeting<Person & Record<"age", number>>);
  }
}
```','["결과는 ''never'' 타입이 된다.","결과로 `Hello, ${string}`와 같은 문자열 리터럴 타입을 갖는다.","코드가 컴파일 에러를 일으킨다. 타입 추론에 실패한다.","결과값은 ''any''타입으로 표기된다."]','{"correct":1}','APPLY',0.56,'["typescript-utilities"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 비동기 처리의 문제점으로 무엇을 지목하시나요?

```typescript
function readStreamChunkByChunk(stream: Readable) {
  stream.on(''data'', (chunk) => console.log(`Chunk received ${chunk}`));
}
readStreamChunkByChunk(fs.createReadStream(''largefile.txt''));```
','["버퍼를 넘겨받은 데이터가 너무 많아 메모리 부족을 일으킬 수 있음","스트림 이벤트의 동작이 예상보다 느려 타임아웃 문제가 발생할 수 있음.","읽기 스트림에서 읽어오는 모든 chunk들이 console.log로 출력되지 않을 가능성이 있다.","스트림 이벤트 핸들러가 비동기 호출을 제대로 처리하지 못해 오류를 일으킬 가능성"]','{"correct":0}','APPLY',0.59,'["stream-handling-in-nodejs"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `fetchData` 함수가 호출될 때, HTTP 요청을 처리하는 콜백이 정상적으로 동작하며 응답 데이터를 완전히 다룰 수 있는지 분석하십시오. 

```typescript
function fetchData(url: string): Promise<void> {
  return new Promise((resolve, reject) => {
    const req = require(''https'').request(url, (res: any) => {
      let data = '''';
      res.on(''data'', chunk => { data += chunk; });
      res.once(''end'', () => resolve(data)); // 완료 시 `resolve` 호출
    }).on(''error'', err => reject(err));
  });
}
```','["완전한 응답 데이터를 받아 처리하려면, ''data'' 이벤트에 대한 핸들러가 필요하다.","응답 스트림이 완료될 때까지 기다린 후 `resolve` 호출을 해야 한다.","HTTP 요청 콜백은 항상 성공적으로 동작하며 `resolve(data)`로 끝난다.","스트림 처리의 병렬성에 의해 응답 데이터가 잘못 조립되는 경우가 있다."]','{"correct":1}','ANALYZE',0.7,'["http-request-callbacks"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드 스니펫은 어떤 동작을 수행하나 선택하세요.

const readFile = () => {
 fs.readFile(''data.json'', ''utf8'', (err, data) => { // 주석 부착 위치
 if (err) throw err;
 console.log(JSON.parse(data));
 });
};','["주어진 파일이 UTF-8 형식으로 읽혀지며 정상적으로 콘솔에 출력된다.","readFile 함수는 비동기로 동작하므로, 데이터 처리가 이전 로직을 대기시키지 않는다.","비동기 콜백에서 에러 처리를 하지 않아 프로그램 전체의 실행이 중단될 위험이 있다.","주석 부착된 라인은 fs.readFile 메소드를 동기적으로 호출하여 파일 읽기를 수행한다."]','{"correct":2}','ANALYZE',0.73,'["fs-modules"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 비동기 호출에 대한 순차적인 복잡성과 실행 순서가 어떻게 영향을 미치는지 분석하십시오. 

```typescript
const processQueue = async () => {
  let remainingTasks: number;
  while (remainingTasks--) await performTask();
};
```
`processQueue` 함수에서 발생하는 가장 중요한 문제를 지목하십시오.','["동기 `while` 루프는 비동기 작업을 제대로 처리하지 못한다.","비동기 호출은 반드시 `await` 키워드로 순차적으로 실행되어야 한다.","`remainingTasks--`가 끝나면 프로그램이 종료된다.","여러번의 호출 후에 모든 태스크가 정상적으로 완료되지 않을 수 있다."]','{"correct":0}','ANALYZE',0.75,'["asynchronous-execution"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드를 분석하여 주석이 부착된 라인에 문제점은 무엇인지 선택하세요.

const fetchUser = async (userId: string) => {
 try {
 const response = await axios.get(`/users/${userId}`);
 if (!response.data.user) throw new Error(''No user found'');
 return response.data.user;
 } catch (error) { // 주석 부착 위치
 console.error(error);
 }
};','["비동기 오류가 콘솔 로그만 출력되고, 이를 처리하는 라우터 미들웨어에서 접근할 수 없다.","외부 호출이 실패했을 때 사용자에게 적절한 HTTP 상태 코드를 리턴하지 못한다.","응답 데이터에 대한 검증 단계가 누락되어 잘못된 입력으로 인해 오류가 발생할 위험이 있다.","위 모든 문제점들을 포함하고 있으며, 콘솔 로그는 비동기 처리 과정에서 중요한 정보를 제거한다."]','{"correct":1}','ANALYZE',0.75,'["http-api"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `Promise.all`이 여러 비동기 작업의 결과를 처리하는 방식과 각각의 Promise가 실패할 경우 전체 작업 흐름에 어떤 영향을 미치는지 분석하십시오. 

```typescript
const tasks = [
  () => fetch(''https://api.example.com/tasks/1''),
  () => fetch(''https://api.example.com/tasks/2'')
];
promiseAllTasks(tasks)
```
definition of `promiseAllTasks`:
```typescript
function promiseAllTasks(taskPromises: (() => Promise<any>)[]): void {
  return new Promise(async resolve => {
    const results = await Promise.all(taskPromises.map(promiseGenerator => promiseGenerator()));
    console.log(results);
 });
}
```
promiseAllTasks에서 일부 작업이 실패했을 때 전체 프로세스의 작동을 분석하십시오.','["모든 작업이 완료될 때까지 `Promise.all`은 결과를 정상적으로 반환한다.","한 개라도 비정상 종료되거나 거부되면, 모든 Promise가 실패하고 에러 처리 중지된다.","각 작업은 독립적이므로 하나의 작업만 성공하면 전체 프로세스는 성공으로 간주되어야 한다.","실패하는 한 가지도 각 작업을 병렬적으로 계속 실행해야 한다."]','{"correct":1}','ANALYZE',0.75,'["promise-parallel-execution"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `AbortController`를 사용하여 비동기 함수 호출을 취소하는 메커니즘이 어떻게 작동하는지 분석하십시오. 

```typescript
const controller = new AbortController();
function longRunningTask(signal: any) {
  return new Promise((resolve, reject) => setTimeout(() => resolve(''done''), 5000));
}
longRunningTask(controller.signal).then(result => console.log(result)).catch(error => console.error(error));
clearTimeout(setImmediate(() => controller.abort()));```
위 코드에서 비동기 작업이 정상적으로 완료되는지, 아니면 `AbortController`가 호출되어 일찍 종료될 수 있는지를 분석하십시오.','["비동기 태스크는 항상 `AbortController`에 의해 취소된다.","`clearTimeout(setImmediate(() => controller.abort()));`에서 발생하는 오류로 인해 비동기 작업이 중단될 수 있다.","타임아웃을 정상적으로 설정하고 이벤트 핸들러를 통해 비동기작업이 실행되는지 분석해야 한다.","비동기 태스크는 `AbortController.signal`의 취소 신호에 의해 일찍 종료되거나 완전히 수행될 수 있다."]','{"correct":3}','ANALYZE',0.75,'["async-cancelation"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 TypeScript 함수에서 주석 부착된 줄의 동작은 무엇인가 선택하세요.

const processPayment = async (order: Order) => {
 try {
 await db.transaction(async tx => { // 주석 부착 위치
 const paymentResult = await tx.executeSql(`INSERT INTO payments (orderId, amount) VALUES (?, ?)`, [order.id, order.amount]);
 return paymentResult;
 });
 } catch (error) {
 console.error(''Failed to process payment:'', error);
 }
};','["트랜잭션에서 발생한 모든 오류는 외부의 catch 블록으로 전달된다.","외부 콜백 함수가 트랜잭션 내에서 실행되는 동안 비동기 작업을 처리한다.","db.transaction 함수는 항상 성공하고, 주어진 SQL 명령은 반드시 데이터베이스에 적용된다.","데이터베이스 트랜잭션이 중단되면 주석된 라인에서는 오류가 발생하지 않고 정상적으로 반환된다."]','{"correct":0}','ANALYZE',0.78,'["prisma-transactions"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 순환 참조가 발생하는 경우 프로세스 모델이 어떻게 동작하는지 분석하십시오. 

```typescript
import { Worker } from ''worker_threads'';
const worker = new Worker(__filename);
```
위 코드의 `Worker` 생성 과정에서 순환 참조가 발생하고, 이로 인해 프로세스 모델이 어떻게 영향을 받는지를 분석하십시오.','["순환 참조에 의해 작업자가 잘못 초기화된다.","프로세스 스레드가 별도로 동작하기 때문에 문제 없다.","`Worker_threads`에서 생성된 모든 작업은 순환 참조를 방지한다.","프로세스의 메모리 누수가 발생하고, 이는 프로세스 모델에 영향을 미친다."]','{"correct":3}','ANALYZE',0.78,'["process-model-threading"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 HTTP 요청이 성공적으로 완료되었는지를 분석하십시오. 

```typescript
import { request } from ''https'';
function performRequest(url: string) {
  return new Promise((resolve, reject) => {
    const req = request({ method: ''GET'', host: ''example.com'', path: url }, res => {
      let data;
      res.on(''data'', chunk => { if(data === undefined) data = ''''; });
      res.once(''end'', () => resolve(`Data received`));
    }).on(''error'', err => reject(err.message || ''Request error''));
  });
}
```
data 변수가 정상적으로 초기화되어 데이터를 받는지 분석하십시오.','["데이터 처리에서 `data === undefined` 체크로 인해 빈 문자열에 할당이 제대로 이루어진다.","`data = ''''` 부분은 항상 실행되므로 비정상적인 조건에서도 정상적으로 작동한다.","HTTP 요청의 `end` 이벤트가 발생할 때만 데이터를 받는다.","코드에서 첫 번째 `chunk` 를 처리하지 못하면 결과로 빈 문자열이 반환된다."]','{"correct":0}','ANALYZE',0.8,'["http-request-handling"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 `Promise.all` 이 반환하는 값과 각 promise의 상태를 설명하세요.

const p1 = Promise.resolve(42);
const p2 = new Promise((resolve, reject) => {
  setTimeout(() => resolve(''resolved''), 500);
});
const p3 = new Promise((resolve, reject) => {
  console.log(''P3 start'');
  process.nextTick(() => {
    throw new Error(''Error in promise P3'')
  });
});
promisesArray = [p1,p2,p3];
Promise.all(promisesArray).then(values => {console.log(`Promise all result: ${values}`)}).
catch(error => console.error(''Uncaught error:'', error));','["Promise.all은 모든 프로미스가 성공적으로 해결되면 결과 배열을 반환한다.","결과 배열에는 오류와 같이 문제가 발생한 promise의 정보도 포함된다.","`p3`에서의 예외는 `all` 내부에 unhandled rejection이 되어 catch 블록으로 전달된다.","Promise.all은 첫 번째 비동기 작업 완료 후 결과를 반환한다."]','{"correct":2}','EVALUATE',0.9,'["async-control"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드에서 프로세스가 종료되는 시점과 원인에 대해 설명하세요.

const worker = require(''worker_threads'');
node { 
  process.on(''SIGTERM'', () => {
    console.log(''Worker received SIGTERM signal.'');
    throw new Error(''Uncaught exception in worker thread.'')});
}
if (worker.isMainThread) {
  setTimeout(() => {
    console.log(`Main thread sending SIGTERM to worker`);
    process.kill(worker.threadId, ''SIGTERM'');}, 500);','["프로세스는 main thread 의 `setTimeout`이 실행된 후에 종료된다.","프로세스는 worker thread 에서 예외가 발생한 시점에서 종료된다.","worker thread 가 SIGTERM 신호를 받은 즉시 종료된다.","프로세스는 모든 작업 완료 후 정상적으로 종료된다."]','{"correct":1}','EVALUATE',0.9,'["process-model"]'),
('NODE_TYPESCRIPT','CODE_READING','다음 코드의 동작과 결과를 설명하세요.

const fs = require(''fs'');
class DataProcessor {
  process() {
    const dataStream = fs.createReadStream(''./largeFile.txt'', {encoding: ''utf8''});
    let bufferString = '''';
    return new Promise((resolve, reject) => {
      dataStream.on(''data'', (chunk) => {
        console.log(`Received chunk ${Buffer.from(chunk).toString()}`);
      });
      dataStream.once(''end'', () => resolve(bufferString));
    }).then(result => {console.log(`Processed file content: ${result}.`)}, error=>{console.error(error)})} }','["스트림이 읽히는 대로 버퍼링하며 파일 내용을 완전히 처리한다.","스트림은 데이터를 부분적으로만 처리하고 파일의 일부가 누락될 수 있다.","`once(''end'')` 이벤트 핸들러에서 `resolve(bufferString)` 가 호출됨으로써 결과 전체가 전달된다.","스트림이 콜백을 종료할 때 프로세싱에 필요한 모든 데이터는 완전히 읽힌다."]','{"correct":1}','EVALUATE',0.9,'["streams-buffers"]');
