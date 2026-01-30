# Spring Boot 3.x (Java 21) 개발 가이드

이 지침은 Java 21, Maven, PostgreSQL, MyBatis, Spring Security, Thymeleaf를 기반으로 하는 프로젝트의 코드 생성 및 아키텍처 가이드라인입니다. Gemini는 모든 응답에서 이 규칙을 최우선으로 준수합니다.

1. 기술 스택 (Tech Stack)

```
Language: Java 21 (Virtual Threads 활용 권장)

Framework: Spring Boot 3.x

Build Tool: Maven (pom.xml)

Persistence: MyBatis (PostgreSQL)

Security: Spring Security 6.x (Lambda DSL 방식 설정)

View: Thymeleaf

Database: PostgreSQL
```

1. 프로젝트 구조 및 아키텍처 (Project Structure)

```
표준적인 계층형 아키텍처를 따르며, 설정(Configuration)보다는 어노테이션 기반 개발을 지향합니다.

Controller: @Controller 또는 @RestController. @GetMapping, @PostMapping 등 사용.

Service: @Service. 비즈니스 로직과 트랜잭션(@Transactional) 관리.

Mapper (Persistence): * XML 매퍼보다는 **인터페이스 기반 어노테이션(@Mapper)**을 우선 사용.

복잡한 쿼리에 한해서만 XML 매퍼 활용.

Domain/DTO: * Java 21의 record 키워드를 DTO 및 공통 객체에 적극 활용.

엔티티 및 가변 객체에는 Lombok 활용 (@Getter, @Builder, @RequiredArgsConstructor).
```

3. 핵심 코딩 원칙 (Key Principles)

```
A. 설정 및 어노테이션 (Annotation over Code)
application.properties 또는 application.yml을 최대한 활용하여 외부 설정을 관리합니다.

Bean 등록은 @Bean 보다는 @Component, @Service, @Repository 어노테이션 스캔을 우선합니다.

Spring Security 설정 시 최신 Lambda DSL 스타일을 사용합니다.

B. Java 21 특화
전통적인 Thread 대신 Virtual Threads 도입을 고려한 설정을 제안하세요. (예: spring.threads.virtual.enabled=true)

switch 패턴 매칭, record, sealed classes 등 최신 문법을 적극 활용합니다.

C. MyBatis & PostgreSQL
데이터베이스 스키마는 PostgreSQL의 특성(JSONB, Array 등)을 고려합니다.

Mapper 인터페이스에서 @Select, @Insert 등을 사용하여 직관적으로 구현합니다.

Snake Case(DB)와 Camel Case(Java) 간의 자동 매핑 설정을 준수합니다.
```

4. Gemini 전용 실행 지침 (Instructions for Gemini)

```
Security First: 모든 새로운 컨트롤러나 엔드포인트 생성 시, Spring Security 권한 설정(@PreAuthorize 등)이 필요한지 먼저 검토하세요.

Modern Security Config: WebSecurityConfigurerAdapter는 더 이상 사용하지 않습니다. SecurityFilterChain Bean 등록 방식을 사용하세요.

Thymeleaf Integration: View 로직 작성 시 Thymeleaf의 th: 속성과 Layout Dialect를 활용하여 코드 중복을 최소화하세요.

Error Handling: @ControllerAdvice와 @ExceptionHandler를 사용하여 일관된 에러 응답 구조를 유지하세요.

Clean Code: 의존성 주입은 필드 주입(@Autowired) 대신 생성자 주입을 사용하세요. (Lombok의 @RequiredArgsConstructor 활용)
```
