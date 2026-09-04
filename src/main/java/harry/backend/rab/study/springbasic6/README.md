# Spring Basic 6: DataSource, JdbcTemplate, HikariCP 커넥션 관찰

이번 단계의 목표는 DB CRUD가 아니다. Java 애플리케이션이 DB 커넥션을 언제 빌리고, SQL 작업이 끝난 뒤 어떻게 반환하는지 직접 관찰하는 것이다.

## 핵심 구분

```text
JdbcTemplate 객체 생성
 ≠ 커넥션을 계속 하나 보관

query/update/execute 호출
 → DataSource에 커넥션 요청
 → HikariCP에서 커넥션 대여
 → JDBC 작업 실행
 → 작업 종료
 → 커넥션을 HikariCP에 반환
```

`ConnectionObservationService.observe()`의 콜백 내부가 커넥션을 빌린 상태이고, 콜백이 끝난 뒤에는 `JdbcTemplate`이 정리 작업을 수행한다.

## 실행

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

## 1. 커넥션 관찰

```bash
curl -s http://localhost:25000/study/spring-basic6/connection/observe
```

응답에서 확인할 값:

- `connection_identity`: JDBC 커넥션 객체 식별값
- `jdbc_url`: 실제 연결된 DB 주소
- `auto_commit`: 트랜잭션을 명시하지 않았을 때의 기본 상태
- `active_connections_before`: 콜백 실행 전 사용 중인 커넥션 수
- `active_connections_inside`: 커넥션을 빌린 콜백 내부의 사용 중인 커넥션 수
- `active_connections_after`: 콜백 종료 후 풀에 반환된 뒤의 사용 중인 커넥션 수

대부분 다음처럼 관찰된다.

```text
before 0
inside 1
after 0
```

요청마다 숫자가 다를 수 있다. 다른 요청이 동시에 실행 중일 수 있기 때문이다.

## 2. 풀 상태 확인

```bash
curl -s http://localhost:25000/study/spring-basic6/connection/pool
```

`active_connections`는 현재 사용 중인 커넥션 수이고, `idle_connections`는 풀에 대기 중인 커넥션 수다.

## 3. 커넥션 점유 실험

터미널 A에서 5초 동안 커넥션을 점유한다.

```bash
curl -i -X POST 'http://localhost:25000/study/spring-basic6/connection/hold?seconds=5'
```

5초가 끝나기 전에 터미널 B에서 실행한다.

```bash
curl -s http://localhost:25000/study/spring-basic6/connection/pool
```

터미널 A의 요청이 진행되는 동안 `active_connections`가 증가하는 것을 확인한다. 이 API는 학습용이므로 점유 시간은 1~30초로 제한했다.

## 4. 커넥션은 누가 닫는가?

이 예제에서 Repository 또는 Service가 직접 `connection.close()`를 호출하지 않는다.

```text
JdbcTemplate.execute(callback)
 → Spring이 커넥션 획득
 → callback 실행
 → callback 종료
 → Spring이 Statement/ResultSet 정리
 → 커넥션 반환
```

여기서 '반환'은 실제 TCP 연결을 매번 끊는다는 의미가 아니다. HikariCP가 다음 요청에서 재사용할 수 있도록 풀에 돌려놓는다는 의미다.

## 5. 문제를 직접 생각해보기

1. 커넥션을 빌린 뒤 반환하지 않으면 풀에는 어떤 일이 생기는가?
2. 요청 수가 커넥션 풀 크기보다 많으면 대기와 타임아웃이 어떻게 발생하는가?
3. `auto_commit=true` 상태에서 SQL 한 번이 끝나면 언제 커밋되는가?
4. `@Transactional`을 추가하면 여러 Repository 호출에서 커넥션 사용 방식이 어떻게 달라지는가?
5. 커넥션을 직접 생성하는 것보다 커넥션 풀을 사용하는 이유는 무엇인가?

다음 단계에서는 이 관찰 코드를 바탕으로 순수 JDBC와 `JdbcTemplate`의 자원 관리 차이를 비교한다.
