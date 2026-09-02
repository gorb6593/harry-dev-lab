# Spring 기본 개념 비교 예제

Java 17과 Spring Boot 3.x 기준으로, 같은 기능을 직접 객체 생성하는 방식과 Spring Bean 방식으로 비교한다.

## 실행

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

현재 서버 포트는 `25000`이다.

별도 API 도구 없이 터미널에서 호출한다.

```bash
curl -i http://localhost:25000/study/spring-basic/harry
```

응답:

```json
{"message":"안녕하세요, harry님"}
```

응답 JSON만 확인하려면 다음처럼 호출한다.

```bash
curl -s http://localhost:25000/study/spring-basic/harry
```

이 요청을 보낸 뒤 Spring Boot 터미널 로그에서 AOP 로그도 확인할 수 있다.

```text
study_start method=GreetingController.greet(..)
study_start method=GreetingService.greet(..)
study_start method=MessageSender.send(..)
study_end method=MessageSender.send(..)
study_end method=GreetingService.greet(..)
study_end method=GreetingController.greet(..)
```

## 1. 적용 전: 수동 객체 생성

`manual/ManualUsage`에서는 개발자가 직접 객체를 생성하고 의존성을 연결한다.

```java
ManualMessageSender sender = new ManualMessageSender();
ManualGreetingService service = new ManualGreetingService(sender);
```

이 방식은 구조가 단순하지만 다음을 개발자가 직접 관리해야 한다.

- 객체 생성
- 의존성 연결
- 구현체 교체
- 객체 생명주기
- 공통 기능 적용

## 2. 적용 후: Spring Bean

`MessageSender`는 `@Component`, `GreetingService`는 `@Service`를 사용한다.

```java
@Service
public class GreetingService {
    private final MessageSender messageSender;
    private final GreetingFormatter greetingFormatter;
}
```

Spring이 컴포넌트를 찾아 Bean으로 등록하고, 생성자 파라미터를 보고 필요한 Bean을 주입한다.

## 3. IoC와 DI

- IoC: 객체 생성과 관리의 주도권을 개발자 코드에서 Spring 컨테이너로 넘긴다.
- DI: `GreetingService`가 필요한 `MessageSender`와 `GreetingFormatter`를 생성자를 통해 전달받는다.

## 4. `@Configuration`과 `@Bean`

`GreetingFormatter`는 `@Component` 대신 `SpringBasicConfig`의 `@Bean` 메서드로 등록했다.
직접 만든 라이브러리 객체나 설정값을 Bean으로 등록할 때 유용하다.

## 5. AOP 로그

`LoggingAspect`는 Spring Bean 메서드 호출을 감싸서 실행 전후 로그를 남긴다.
수동 객체인 `ManualGreetingService`에는 적용되지 않고, Spring이 관리하는 Bean에만 적용된다.

## 6. 시작과 요청 흐름

애플리케이션 시작 시:

```text
SpringApplication.run()
 → Component Scan
 → BeanDefinition 등록
 → Bean 생성 및 의존성 주입
 → AOP Proxy 생성
 → 서버 시작
```

HTTP 요청 시:

```text
GET /study/spring-basic/harry
 → DispatcherServlet
 → GreetingController 찾기
 → Controller AOP
 → GreetingController
 → GreetingService AOP
 → GreetingService
 → MessageSender
 → JSON 응답
```

## 학습 질문

1. `GreetingService` 생성자에서 `new MessageSender()`를 하지 않는 이유는 무엇인가?
2. `GreetingFormatter`를 `@Component`로 바꾸면 `@Bean` 설정과 어떤 차이가 생기는가?
3. `ManualGreetingService`에 AOP 로그가 남지 않는 이유는 무엇인가?
4. `GreetingService`를 `new GreetingService(...)`로 Controller에서 직접 생성하면 무엇이 사라지는가?
