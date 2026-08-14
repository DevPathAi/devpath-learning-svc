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
});','["1. 클릭 이벤트가 발생할 때마다 새로운 WebSocket 연결을 생성합니다.","2. 새 제품이 추가될 때마다 서버로부터 메시지를 받습니다.","3. 모든 클라이언트에서 새 제품 정보를 공유하지 않습니다.","4. 클릭 시 새로운 제품을 서버에 등록하지 않습니다."]','{"correct":1}','ANALYZE',0.9,'["websocket-realtime-communication"]');
