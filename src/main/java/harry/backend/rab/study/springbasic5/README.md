# Spring Basic 5: 학생 한 명의 생성·조회·수정·삭제 흐름

이번 단계는 메모 예제를 복사하는 것이 목적이 아니다. 학생 한 명의 데이터를 기준으로 HTTP 요청이 Java 코드를 통과해 실제 MySQL에 저장되고, DB 오류가 다시 HTTP 오류 응답으로 변환되는 전체 흐름을 확인한다.

## 전체 흐름

```text
HTTP 요청
 → StudentController
 → StudentCreateRequest(JSON → Java 변환, @Valid 검증)
 → StudentService(업무 흐름)
 → StudentRepository(SQL 작성)
 → JdbcTemplate
 → HikariCP(DataSource의 커넥션 풀)
 → MySQL springbasic5_student 테이블
```

응답은 반대 방향으로 흐른다.

```text
MySQL ResultSet
 → RowMapper
 → Student
 → StudentResponse(Java → JSON 변환)
 → HTTP 응답
```

## 테이블 확인

애플리케이션 시작 시 다음 SQL이 실행된다.

```sql
CREATE TABLE IF NOT EXISTS springbasic5_student (...)
```

직접 확인한다.

```bash
docker exec -it rab-mysql-1 mysql -urab -prab1234 rab \
  -e 'DESCRIBE springbasic5_student;'
```

## 생성

```bash
curl -i -X POST http://localhost:25000/study/spring-basic5/students \
  -H 'Content-Type: application/json' \
  -d '{"student_number":"S-001","student_name":"해리"}'
```

처리 순서:

1. Jackson이 JSON을 `StudentCreateRequest`로 변환한다.
2. `@Valid`가 빈 학번과 이름을 검사한다.
3. Controller가 Service에 Java 값만 전달한다.
4. Repository가 `INSERT` SQL의 `?`에 값을 바인딩한다.
5. MySQL이 `AUTO_INCREMENT`로 ID를 만든다.
6. `GeneratedKeyHolder`로 생성된 ID를 받는다.
7. 생성된 학생을 다시 `SELECT`하여 응답한다.
8. Controller가 201과 `Location` 헤더를 반환한다.

응답 JSON은 API camel_case 규칙에 맞춘다.

```json
{
  "student_id": 1,
  "student_number": "S-001",
  "student_name": "해리",
  "created_at": "2026-09-03T16:00:00"
}
```

## 조회

```bash
curl -i http://localhost:25000/study/spring-basic5/students/1
curl -i http://localhost:25000/study/spring-basic5/students
```

Repository의 `RowMapper`가 ResultSet의 컬럼 하나씩을 읽어 `Student`를 만든다.

## 수정

```bash
curl -i -X POST http://localhost:25000/study/spring-basic5/students/1/update \
  -H 'Content-Type: application/json' \
  -d '{"student_number":"S-001","student_name":"해리 수정"}'
```

`UPDATE` 결과가 0행이면 해당 학생이 없다고 판단하여 `StudentNotFoundException`을 던진다.

## 삭제

```bash
curl -i -X POST http://localhost:25000/study/spring-basic5/students/1/delete
```

삭제된 행 수가 1이면 204를 반환하고, 0이면 404를 반환한다.

## 오류 흐름

### 입력 검증 오류: 400

```bash
curl -i -X POST http://localhost:25000/study/spring-basic5/students \
  -H 'Content-Type: application/json' \
  -d '{"student_number":"","student_name":""}'
```

Controller 메서드에 도착하기 전에 `@Valid`가 실패하므로 Repository까지 내려가지 않는다.

### 없는 학생: 404

```bash
curl -i http://localhost:25000/study/spring-basic5/students/99999
```

Service가 `StudentNotFoundException`을 던지고 `SpringBasic5ExceptionHandler`가 이를 Problem Details 응답으로 바꾼다.

### 중복 학번: 409

같은 학번으로 두 번 생성하면 MySQL의 UNIQUE 제약 조건이 위반된다.

```bash
curl -i -X POST http://localhost:25000/study/spring-basic5/students \
  -H 'Content-Type: application/json' \
  -d '{"student_number":"S-001","student_name":"중복 학생"}'
```

`DuplicateKeyException`을 애플리케이션 예외로 바꾸고 409 Conflict를 반환한다. DB 제약 조건은 애플리케이션의 사전 검사만으로 대체할 수 없다. 동시에 같은 학번이 들어오는 상황을 DB가 최종적으로 막아야 한다.

## DB에서 실제 데이터 확인

```bash
docker exec -it rab-mysql-1 mysql -urab -prab1234 rab \
  -e 'SELECT id, student_number, student_name, created_at FROM springbasic5_student ORDER BY id;'
```

## 다음 단계에서 개선할 점

- 지금은 요청마다 필요한 SQL을 직접 작성한다.
- 여러 변경 작업을 하나로 묶는 트랜잭션이 아직 없다.
- 테스트는 현재 HTTP 계약 테스트와 실제 DB 확인을 분리해서 추가할 예정이다.
- 다음 단계에서 JDBC의 커넥션·트랜잭션을 확인한 뒤 같은 학생 기능을 JPA로 다시 구현한다.
