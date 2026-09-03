# Spring Basic 4: JdbcTemplate으로 실제 MySQL 연결

이번 단계부터 메모를 Java 메모리가 아니라 Docker MySQL에 저장한다.

## 목표

- `JdbcTemplate`이 DataSource를 통해 MySQL에 연결되는 흐름 이해
- SQL을 직접 작성하고 파라미터를 안전하게 바인딩
- `INSERT` 후 생성된 DB ID를 받기
- 조회 결과를 `Memo` 객체로 변환하기
- 애플리케이션을 재시작해도 데이터가 유지되는지 확인하기

## 연결 설정

`docker-compose.yml`의 MySQL을 사용한다.

```text
host: localhost
port: 13306
database: rab
username: rab
password: rab1234
```

Spring Boot의 `DataSourceAutoConfiguration`이 `application.yml`의 설정을 읽어 `DataSource`와 `JdbcTemplate`을 자동으로 등록한다.

```text
MemoController
 → MemoService
 → MemoRepository
 → JdbcTemplate
 → DataSource
 → MySQL
```

## 테이블

애플리케이션 시작 시 `src/main/resources/db/springbasic4-schema.sql`이 실행되어 다음 테이블을 준비한다.

```sql
SELECT * FROM springbasic4_memo ORDER BY id;
```

## 실행과 API 확인

```bash
cd /Users/kiwoong/Downloads/rab
./gradlew bootRun
```

메모 생성:

```bash
curl -i -X POST http://localhost:25000/study/spring-basic4/memos \
  -H 'Content-Type: application/json' \
  -d '{"content":"실제 MySQL에 저장되는 메모"}'
```

목록 조회:

```bash
curl -s http://localhost:25000/study/spring-basic4/memos
```

MySQL에서 직접 확인:

```bash
docker exec -it rab-mysql-1 mysql -urab -prab1234 rab \
  -e 'SELECT id, content, created_at FROM springbasic4_memo ORDER BY id;'
```

## 이번 단계에서 확인할 질문

1. `JdbcTemplate`은 어떤 `DataSource`를 사용해 DB에 연결하는가?
2. `?` 파라미터를 사용하는 이유는 무엇인가?
3. `GeneratedKeyHolder`가 필요한 이유는 무엇인가?
4. `RowMapper`는 DB의 한 행을 Java 객체로 어떻게 바꾸는가?
5. `springbasic3`의 `ConcurrentHashMap` 저장과 어떤 차이가 있는가?

## 다음 단계

다음 `springbasic5`에서는 현재 Repository에 모여 있는 SQL을 별도 분석하고, JDBC 계층 테스트와 트랜잭션을 추가한다. 그 후 같은 기능을 JPA Entity와 Spring Data JPA로 다시 구현하여 세 방식을 비교한다.
