# Spring Basic 1: 가장 간단한 Controller와 Service

초보자가 Spring 요청 흐름과 HTTP GET/POST 차이를 처음 이해하기 위한 단계다.

이번 단계에서는 아래 세 가지만 학습한다.

- `@RestController`: HTTP 요청을 받는 클래스
- `@RequestMapping`: URL의 공통 앞부분 지정
- `@GetMapping`: GET 요청 연결
- `@PostMapping`: POST 요청 연결
- `@PathVariable`: URL 안의 값 읽기
- `@RequestParam`: 쿼리 파라미터 읽기
- `@Service`: 비즈니스 로직을 담당하는 클래스
- 생성자 주입: Controller가 Service를 전달받는 방식

추가로 `@RestController`와 일반 `@Controller`의 반환 방식 차이도 비교한다.

## 실행

프로젝트 루트에서 실행한다.

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

서버 포트는 `25000`이다.

다른 터미널에서 요청한다.

```bash
curl http://localhost:25000/study/spring-basic1/greetings/harry
```

응답:

```text
안녕하세요, harry님
```

## `@RestController`란?

`@RestController`는 다음 두 기능을 합친 어노테이션이다.

```java
@Controller
@ResponseBody
```

따라서 아래 메서드의 반환값은 View 이름이 아니라 HTTP 응답 Body가 된다.

```java
@RestController
public class SimpleGreetingController {

    @GetMapping("/greetings/{name}")
    public String getGreeting(@PathVariable String name) {
        return "안녕하세요, " + name + "님";
    }
}
```

## `@Controller`만 사용하면 어떻게 될까?

일반 `@Controller`에서 문자열을 반환하면 Spring MVC는 그 문자열을 View 이름으로 해석한다.

```java
@Controller
public class SimpleController {

    @GetMapping("/view")
    public String view() {
        return "hello";
    }
}
```

위 코드는 다음 화면을 찾으려고 한다.

```text
src/main/resources/templates/hello.html
```

화면 파일이 없으면 View를 찾지 못해 오류가 발생한다.

일반 `@Controller`에서 문자열을 응답 Body로 보내고 싶다면 `@ResponseBody`를 추가한다.

```java
@Controller
public class SimpleController {

    @ResponseBody
    @GetMapping("/body")
    public String body() {
        return "응답 Body입니다.";
    }
}
```

터미널에서 확인한다.

```bash
curl -i http://localhost:25000/study/spring-basic1/controller/body
```

정리하면 다음과 같다.

| Controller 종류 | 반환값 처리 | 주 사용 목적 |
|---|---|---|
| `@Controller` | View 이름 | 서버 사이드 HTML |
| `@Controller` + `@ResponseBody` | 응답 Body | REST 응답 |
| `@RestController` | 응답 Body | REST API |

`@RestController`는 `@ResponseBody`가 모든 메서드에 적용된 것과 같은 효과가 있다.

## GET 요청

GET은 보통 데이터를 조회할 때 사용한다.

```bash
curl -i http://localhost:25000/study/spring-basic1/greetings/harry
```

`/greetings/harry`의 `harry`가 `@PathVariable`로 Controller에 전달된다.

## POST 요청

POST는 서버에 데이터를 전달하거나 새 데이터를 만들 때 사용한다.
이번 예제에서는 아직 데이터를 저장하지 않고, GET과 다른 요청 방식이라는 점만 확인한다.

```bash
curl -i -X POST \
  "http://localhost:25000/study/spring-basic1/greetings?name=harry"
```

응답:

```text
안녕하세요, harry님 (POST 요청)
```

`?name=harry`의 `name`이 `@RequestParam`으로 Controller에 전달된다.

## GET과 POST 비교

| 구분 | GET | POST |
|---|---|---|
| 목적 | 데이터 조회 | 데이터 전달·생성 |
| 예제 어노테이션 | `@GetMapping` | `@PostMapping` |
| 예제 값 전달 방식 | URL 경로 | 쿼리 파라미터 |
| 브라우저 주소창 호출 | 가능 | 일반적으로 어려움 |
| 예제 | `/greetings/harry` | `/greetings?name=harry` |

## 코드 읽는 순서

### 1. 어노테이션이란?

어노테이션은 클래스나 메서드에 붙여서 Spring에게 추가 정보를 알려주는 표시다.

```java
@Service
public class SimpleGreetingService {
}
```

위 코드는 `SimpleGreetingService`를 Spring이 관리하도록 알려준다.

### 2. Controller

`SimpleGreetingController`가 HTTP 요청을 받는다.

```java
@RestController
@RequestMapping("/study/spring-basic1")
public class SimpleGreetingController {
}
```

다음 요청이 들어오면:

```text
GET /study/spring-basic1/greetings/harry
```

`getGreeting()` 메서드가 실행된다.

### 3. Service 주입

Controller는 Service를 직접 만들지 않는다.

```java
private final SimpleGreetingService greetingService;

public SimpleGreetingController(SimpleGreetingService greetingService) {
    this.greetingService = greetingService;
}
```

Spring이 `SimpleGreetingService` 객체를 만들어 생성자에 넣어준다.

### 4. Service

실제 인사말을 만드는 일은 Service가 담당한다.

```java
@Service
public class SimpleGreetingService {
}
```

## 이번 요청의 전체 흐름

```text
1. curl이 GET 또는 POST 요청 전송
2. Spring이 URL과 HTTP 메서드에 맞는 Controller 탐색
3. `@PathVariable` 또는 `@RequestParam` 값 추출
4. Controller 메서드 실행
5. Controller가 SimpleGreetingService.greet() 호출
6. Service가 인사말 반환
7. Controller가 응답 반환
```

## 기초에서 고급으로 나눌 학습 순서

한 단계에서 모든 기능을 배우지 않고, 아래처럼 나누어 진행한다.

### Spring Basic 1: HTTP와 Controller 기초

- 어노테이션이란 무엇인가
- `@Controller`와 `@RestController`
- GET과 POST
- `@PathVariable`과 `@RequestParam`
- Controller와 Service 역할
- 생성자 주입

### Spring Basic 2: 요청과 응답 데이터

- `@RequestBody`
- DTO
- JSON과 `HttpMessageConverter`
- `@ResponseBody`
- `ResponseEntity`
- HTTP 상태 코드

### Spring Basic 3: 검증과 예외 처리

- `@Valid`
- `@NotBlank`, `@Email` 등 Bean Validation
- `@ExceptionHandler`
- `@RestControllerAdvice`
- 일관된 오류 응답

### Spring Intermediate 1: Bean과 의존성 주입

- IoC와 DI
- Component Scan
- Bean 생성 과정
- `@Component`, `@Service`, `@Repository`
- `@Configuration`, `@Bean`
- Bean Scope와 생명주기

### Spring Intermediate 2: Filter, Interceptor, AOP

- Servlet Filter
- HandlerInterceptor
- Spring AOP Proxy
- 실행 순서 비교
- 공통 로깅과 인증 처리

### Spring Advanced 1: 데이터베이스와 트랜잭션

- JPA Entity
- 영속성 컨텍스트
- `@Transactional`
- 변경 감지
- 지연 로딩
- N+1 문제

### Spring Advanced 2: 실무 운영

- Spring Security
- 세션과 JWT
- 테스트 전략
- 로그와 Trace ID
- 캐시
- 동시성
- 성능과 장애 대응

## 이번 단계에서 아직 사용하지 않는 것

- AOP
- Filter
- Interceptor
- `@ControllerAdvice`
- `@Configuration`, `@Bean`
- 데이터베이스
- DTO와 JSON 요청

## 확인할 질문

1. `SimpleGreetingController`에 `@RestController`가 없으면 어떻게 될까?
2. `@GetMapping`과 `@PostMapping`은 무엇이 다를까?
3. `/greetings/harry`의 `harry`는 어떤 어노테이션으로 읽을까?
4. `?name=harry`의 `harry`는 어떤 어노테이션으로 읽을까?
5. `SimpleGreetingService`에 `@Service`가 없으면 왜 주입되지 않을까?
6. Controller에서 `new SimpleGreetingService()`를 하지 않는 이유는 무엇일까?

## 완료 조건

- 애플리케이션이 정상적으로 시작된다.
- curl 요청이 성공한다.
- 응답이 `안녕하세요, harry님`으로 출력된다.
- Controller와 Service의 책임을 설명할 수 있다.
- 생성자 주입이 어떤 의미인지 설명할 수 있다.
