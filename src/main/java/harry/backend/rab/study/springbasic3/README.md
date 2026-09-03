# Spring Basic 3: JSON 요청, ResponseEntity, 상태 코드, 표준 에러 응답

Spring Basic 2에서는 쿼리 파라미터로 POST를 보내고 항상 200으로 응답했다.
이번 단계는 "서버가 요청을 받으면 어떤 모양으로 응답해야 하는가"를 표준에 맞춰 익힌다.

- 요청: JSON Body → `@RequestBody` + DTO → `@Valid` 검증
- 응답: `ResponseEntity`로 상태 코드와 헤더를 직접 결정
- 상태 코드: 200 / 201 / 204 / 400 / 404
- 에러 형식: RFC 9457 Problem Details (`ProblemDetail`, `application/problem+json`)
- 메서드 전략: 조회는 GET, 상태 변경은 전부 POST (POST-only 방식)

## 공식 문서

| 주제 | 링크 |
|---|---|
| HTTP 메서드·상태 코드 표준 (RFC 9110) | https://www.rfc-editor.org/rfc/rfc9110.html |
| GET 정의 (9.3.1) | https://www.rfc-editor.org/rfc/rfc9110.html#name-get |
| POST 정의 (9.3.3) | https://www.rfc-editor.org/rfc/rfc9110.html#name-post |
| 상태 코드 (15장) | https://www.rfc-editor.org/rfc/rfc9110.html#name-status-codes |
| 에러 응답 표준 (RFC 9457 Problem Details) | https://www.rfc-editor.org/rfc/rfc9457.html |
| Spring `ProblemDetail` / 에러 응답 | https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html |
| Spring `ResponseEntity` | https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responseentity.html |
| Spring `@RequestBody` | https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/requestbody.html |
| Spring 검증 (`@Valid`) | https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html |
| Google API 설계 가이드: 커스텀 메서드 (POST + 동사) | https://google.aip.dev/136 |
| Idempotency-Key 헤더 (IETF 초안) | https://datatracker.ietf.org/doc/draft-ietf-httpapi-idempotency-key-header/ |

## 실행

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

서버 포트는 `25000`이다. 이미 떠 있는 서버가 있다면 새 코드가 반영되도록 재시작한다.

전체 시나리오를 한 번에 돌려보려면:

```bash
bash src/main/java/harry/backend/rab/study/springbasic3/curl-examples.sh
```

## API 목록

| 동작 | 메서드 | URL | 성공 응답 |
|---|---|---|---|
| 목록 조회 | GET | `/study/spring-basic3/memos` | 200 + 배열 |
| 단건 조회 | GET | `/study/spring-basic3/memos/{id}` | 200 + 객체 |
| 생성 | POST | `/study/spring-basic3/memos` | 201 + `Location` 헤더 + 객체 |
| 수정 | POST | `/study/spring-basic3/memos/{id}/update` | 200 + 객체 |
| 삭제 | POST | `/study/spring-basic3/memos/{id}/delete` | 204 (Body 없음) |

## 1. 생성: POST + JSON Body → 201 Created

```bash
curl -i -X POST http://localhost:25000/study/spring-basic3/memos \
  -H "Content-Type: application/json" \
  -d '{"content":"Spring 공부"}'
```

응답:

```text
HTTP/1.1 201
Location: /study/spring-basic3/memos/1
Content-Type: application/json

{"id":1,"content":"Spring 공부"}
```

읽을 점:
- `-H "Content-Type: application/json"`이 없으면 Spring이 Body를 JSON으로 해석하지 못해 415가 난다.
- `@RequestBody`가 JSON을 `MemoCreateRequest`로 바꾼다. 이 변환을 `HttpMessageConverter`(Jackson)가 담당한다.
- 새 리소스를 만들었으니 200이 아니라 **201 Created**를 보내고, `Location` 헤더로 "어디에 생겼는지" 알려준다. RFC 9110 9.3.3이 권장하는 형태다.

```java
URI location = URI.create("/study/spring-basic3/memos/" + created.id());
return ResponseEntity.created(location).body(MemoResponse.from(created));
```

## 2. 조회: GET → 200 OK

```bash
curl -i http://localhost:25000/study/spring-basic3/memos
curl -i http://localhost:25000/study/spring-basic3/memos/1
```

Controller가 객체만 반환하면 Spring이 알아서 200 + JSON으로 보낸다. 상태 코드를 바꿀 일이 없으면 `ResponseEntity`를 쓰지 않아도 된다.

## 3. 수정: POST + 경로 동사 → 200 OK

```bash
curl -i -X POST http://localhost:25000/study/spring-basic3/memos/1/update \
  -H "Content-Type: application/json" \
  -d '{"content":"Spring 공부 (수정)"}'
```

응답:

```json
{"id":1,"content":"Spring 공부 (수정)"}
```

REST 관례라면 `PUT /memos/1`이지만, 이 예제는 PUT/DELETE 없이 POST만 쓰는 방식을 보여준다.
메서드가 의미를 실어주지 못하므로 URL 끝의 동사(`update`, `delete`)가 그 역할을 한다.
표준(RFC 9110)은 이 방식을 금지하지 않는다. POST는 "리소스가 자기 의미론대로 처리한다"로 정의되어 있다.

## 4. 삭제: POST + 경로 동사 → 204 No Content

```bash
curl -i -X POST http://localhost:25000/study/spring-basic3/memos/1/delete
```

응답:

```text
HTTP/1.1 204
```

Body가 없다. "성공했지만 돌려줄 내용이 없다"는 뜻이 **204**다.

```java
return ResponseEntity.noContent().build();
```

## 5. 없는 리소스: 404 + Problem Details

```bash
curl -i http://localhost:25000/study/spring-basic3/memos/999
```

응답:

```text
HTTP/1.1 404
Content-Type: application/problem+json

{
  "type": "https://example.com/problems/memo-not-found",
  "title": "Memo Not Found",
  "status": 404,
  "detail": "메모를 찾을 수 없습니다. id=999",
  "instance": "/study/spring-basic3/memos/999",
  "memoId": 999
}
```

흐름:

```text
GET /memos/999
 → MemoController.findById()
 → MemoService.findById()   → MemoNotFoundException 던짐
 → MemoExceptionHandler.handleNotFound()  → ProblemDetail(404) 반환
```

`Service`는 HTTP를 모른다. "없다"는 사실만 예외로 표현하고, 그것을 404로 바꾸는 일은 `@RestControllerAdvice`가 한다.

## 6. 검증 실패: 400 + 필드별 메시지

```bash
curl -i -X POST http://localhost:25000/study/spring-basic3/memos \
  -H "Content-Type: application/json" \
  -d '{"content":""}'
```

응답:

```json
{
  "type": "about:blank",
  "title": "Validation Failed",
  "status": 400,
  "detail": "요청 값이 올바르지 않습니다.",
  "instance": "/study/spring-basic3/memos",
  "errors": {"content": "content는 비어 있을 수 없습니다."}
}
```

DTO의 `@NotBlank`는 Controller 파라미터에 `@Valid`가 붙어 있을 때만 실행된다. 실패하면 `MethodArgumentNotValidException`이 발생하고 핸들러가 400으로 바꾼다.

## 7. 깨진 JSON: 400

```bash
curl -i -X POST http://localhost:25000/study/spring-basic3/memos \
  -H "Content-Type: application/json" \
  -d '{"content": '
```

`HttpMessageNotReadableException` → 400 "Malformed Request Body".

## 에러 응답은 왜 이 모양인가: RFC 9457

에러 응답 본문은 IETF 공식 표준이 있다. **RFC 9457 Problem Details for HTTP APIs** (2023, RFC 7807 대체).

| 필드 | 의미 |
|---|---|
| `type` | 에러 유형을 식별하는 URI (기본 `about:blank`) |
| `title` | 유형에 대한 짧은 설명. 같은 type이면 항상 같다 |
| `status` | HTTP 상태 코드 |
| `detail` | 이번 발생 건에 대한 구체 설명 |
| `instance` | 이번 발생 건을 식별하는 URI (Spring이 요청 경로를 자동으로 채운다) |
| 확장 필드 | 자유롭게 추가 (`errors`, `memoId`, `traceId` 등) |

Spring 6 / Boot 3의 `ProblemDetail` 클래스가 이 표준을 그대로 구현한다. 별도 에러 DTO를 만들 필요가 없다.

성공 응답 본문은 공식 표준이 없다. 표준이 정하는 것은 상태 코드와 헤더까지이고, JSON 구조는 관례를 고른다. 이 예제는 가장 흔한 "리소스를 그대로 + 정확한 상태 코드" 방식(Stripe, GitHub 방식)을 따른다.

## 상태 코드 선택 기준 (RFC 9110 15장)

| 상황 | 코드 | 이 예제 |
|---|---|---|
| 조회·수정 성공, Body 있음 | 200 OK | GET, update |
| 생성 성공 | 201 Created + `Location` | create |
| 성공, Body 없음 | 204 No Content | delete |
| 요청 형식·값 오류 | 400 Bad Request | 검증 실패, 깨진 JSON |
| 리소스 없음 | 404 Not Found | 없는 id |
| Content-Type 불일치 | 415 Unsupported Media Type | JSON 헤더 누락 (Spring 기본 처리) |
| 중복·상태 충돌 | 409 Conflict | (다음 단계) |

## POST-only 방식에서 지켜야 할 것

1. **조회는 GET, 상태 변경은 POST.** 이 선은 절대 넘지 않는다. GET으로 삭제를 만들면 크롤러·프리페치가 실행해버린다.
2. **URL 동사 규칙을 하나로 통일한다.** 이 예제는 `/{id}/update`, `/{id}/delete` 형태. Google 스타일 `/{id}:delete`, Slack 스타일 `/memo.delete`도 있다. 섞지 않는다.
3. **상태 코드는 메서드와 무관하게 정확히.** POST라도 생성은 201, 삭제는 204.
4. **재시도가 필요한 POST는 멱등키.** 결제·주문처럼 타임아웃 후 재전송이 생기는 곳은 `Idempotency-Key` 헤더로 중복 처리를 막는다. (4단계 동시성·멱등성에서 다룬다.)

## Bean 이름 충돌: 왜 `@Service("springBasic3MemoService")`인가

`@Service`, `@RestController`의 기본 Bean 이름은 클래스명의 첫 글자를 소문자로 바꾼 것이다(`memoService`).
Bean 이름은 애플리케이션 전체에서 유일해야 하므로, `springbasic2.MemoService`와 `springbasic3.MemoService`처럼
**패키지만 다르고 클래스명이 같으면 기동이 실패한다.**

```text
ConflictingBeanDefinitionException: Annotation-specified bean name 'memoController'
for bean class [...springbasic3.MemoController] conflicts with existing,
non-compatible bean definition of same name and class [...springbasic2.MemoController]
```

바깥에 보이는 메시지는 `BeanDefinitionStoreException: Failed to parse configuration class`이고,
진짜 원인은 그 아래 `Caused by:`에 있다. Spring 기동 에러는 항상 마지막 `Caused by`부터 읽는다.

해결은 둘 중 하나다.
- 클래스명을 다르게 짓는다 (`MemoApiController` 등)
- 어노테이션에 Bean 이름을 명시한다. 이 예제는 `springbasic2`를 건드리지 않기 위해 이 방법을 택했다.

`@RestControllerAdvice`는 이름 속성이 없다(`value`는 `basePackages`의 별칭). 다른 패키지에 같은 이름의 핸들러 클래스를 만들면 클래스명을 바꿔야 한다.

## 코드 읽는 순서

1. `MemoCreateRequest` / `MemoUpdateRequest`: 요청 DTO와 검증 어노테이션
2. `MemoResponse`: 응답 DTO. 내부 객체를 그대로 내보내지 않는 경계
3. `MemoController`: `@RequestBody`, `@Valid`, `ResponseEntity`
4. `MemoService`: HTTP를 모르는 순수 로직. 없으면 `MemoNotFoundException`
5. `MemoExceptionHandler`: 예외 → `ProblemDetail`

## 확인할 질문

1. `Content-Type: application/json` 헤더를 빼면 왜 415가 날까?
2. `@Valid`를 지우면 빈 content가 어떻게 처리될까?
3. 생성 응답이 200이 아니라 201이어야 하는 이유는? `Location` 헤더는 누가 쓰는가?
4. 삭제를 `GET /memos/1/delete`로 만들면 어떤 사고가 날 수 있을까?
5. `MemoService`가 직접 `ResponseStatusException(404)`를 던지지 않고 `MemoNotFoundException`을 던지는 이유는?
6. `ProblemDetail`의 `instance`는 누가 채우는가?
7. 같은 `POST /memos` 요청을 네트워크 타임아웃 후 다시 보내면 무슨 일이 생기는가?

## 이번 단계의 한계

- 여전히 메모리에 저장한다 (재시작하면 초기화).
- 멱등키 처리가 없다.
- 409 Conflict 같은 상태 충돌 케이스가 없다.
- 테스트는 MockMvc 단위 테스트만 있다 (`src/test/java/.../springbasic3`).

다음 단계에서는 JPA로 저장하고, 동시 요청과 멱등키를 다룬다.
