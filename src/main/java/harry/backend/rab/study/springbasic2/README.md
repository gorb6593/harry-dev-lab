# Spring Basic 2: GET과 POST의 차이

이번 단계의 목표는 어노테이션을 외우는 것이 아니라, HTTP 요청의 목적에 맞게 GET과 POST를 선택하는 이유를 이해하는 것이다.

## 실행

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

서버 포트는 `25000`이다.

## GET: 메모 목록 조회

```bash
curl -i http://localhost:25000/study/spring-basic2/memos
```

처음에는 다음처럼 응답한다.

```json
[]
```

Controller의 `@GetMapping` 메서드가 Service의 `findAll()`을 호출한다.

```text
GET /memos
 → MemoController.findAll()
 → MemoService.findAll()
 → 메모 목록 반환
```

GET은 데이터를 조회할 때 사용한다. 같은 요청을 여러 번 보내도 서버의 메모가 새로 만들어지지 않는다.

## POST: 메모 생성

```bash
curl -i -X POST \
  "http://localhost:25000/study/spring-basic2/memos?content=Spring%20공부"
```

응답:

```json
{"id":1,"content":"Spring 공부"}
```

Controller의 `@PostMapping` 메서드가 Service의 `create()`를 호출하고, Service가 새로운 메모를 저장한다.

```text
POST /memos?content=Spring 공부
 → MemoController.create()
 → MemoService.create()
 → 메모 생성 및 저장
 → 생성된 메모 반환
```

다시 목록을 조회한다.

```bash
curl -s http://localhost:25000/study/spring-basic2/memos
```

응답:

```json
[{"id":1,"content":"Spring 공부"}]
```

## POST를 다시 보내면?

```bash
curl -s -X POST \
  "http://localhost:25000/study/spring-basic2/memos?content=Spring%20공부"
```

새로운 ID가 생성된다.

```json
{"id":2,"content":"Spring 공부"}
```

같은 내용이어도 POST는 새로운 생성 요청이므로 데이터가 추가된다.

## 왜 GET과 POST를 구분할까?

| 구분 | GET | POST |
|---|---|---|
| 목적 | 조회 | 생성·처리 |
| 데이터 변경 | 하지 않는 것이 원칙 | 변경할 수 있음 |
| 값 전달 | URL 경로·쿼리 | Body 또는 쿼리 |
| 브라우저 주소창 | 쉽게 호출 가능 | 일반적으로 직접 호출하지 않음 |
| 새 데이터 생성 | 적합하지 않음 | 적합함 |
| 예제 | `GET /memos` | `POST /memos` |

이번 예제의 POST는 이해를 위해 쿼리 파라미터를 사용했다. 실무에서는 생성 데이터가 많아지면 `@RequestBody`와 DTO를 사용하는 방식으로 발전한다.

## 어노테이션 주석 읽기

```java
@GetMapping
public List<Memo> findAll() {
    return memoService.findAll();
}
```

`@GetMapping`은 HTTP GET 요청과 Java 메서드를 연결한다.

```java
@PostMapping
public Memo create(@RequestParam String content) {
    return memoService.create(content);
}
```

`@PostMapping`은 HTTP POST 요청과 Java 메서드를 연결하고, `@RequestParam`은 요청의 `content` 값을 꺼낸다.

## 이번 단계의 한계

- 애플리케이션을 재시작하면 메모리가 초기화된다.
- 아직 데이터베이스를 사용하지 않는다.
- POST 데이터를 쿼리 파라미터로 받고 있다.
- 수정과 삭제 API는 아직 없다.

다음 단계에서는 DTO, JSON Body, `@RequestBody`, `ResponseEntity`, HTTP 상태 코드로 확장한다.
