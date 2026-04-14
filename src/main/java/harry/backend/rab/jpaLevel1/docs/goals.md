# JPA Level 1 - 학습 목표 및 이수 기준

## 📋 학습 목표

### Spring Boot 기초
1. **Spring Boot 기본 구조 이해**
   - Spring Boot 프로젝트 구조 파악
   - application.yml 설정 파일 이해
   - Dependency 관리 방식 이해

2. **REST API 기본**
   - Controller 작성 및 REST endpoint 정의
   - @RestController, @RequestMapping, @GetMapping 등의 어노테이션 사용
   - HTTP 메서드 (GET, POST, PUT, DELETE) 이해

### JPA 기초
1. **엔티티 매핑**
   - @Entity, @Id, @GeneratedValue 사용
   - 필드 매핑 (@Column, @Basic)
   - 엔티티 라이프사이클 이해

2. **Repository 패턴**
   - JpaRepository 인터페이스 사용
   - 기본 CRUD 메서드 활용
   - 쿼리 메서드 네이밍 컨벤션

3. **CRUD 연동**
   - Create: 엔티티 저장
   - Read: 엔티티 조회 (단일, 전체)
   - Update: 엔티티 수정
   - Delete: 엔티티 삭제

4. **기본 쿼리**
   - 쿼리 메서드 (@Query Methods)
   - 기본 JPQL 이해
   - 조건 조회 (findBy~, existsBy~)

5. **관계 매핑 기초**
   - @ManyToOne, @OneToMany 기본 사용
   - 외래 키 매핑 이해
   - Cascade 옵션 기초

---

## ✅ 이수 기준

### Spring Boot 기초 (3/3 통과 시 이수)
- [ ] 1. REST Controller 생성 및 endpoint 정의
- [ ] 2. application.yml 데이터베이스 연결 설정
- [ ] 3. POST 요청으로 데이터 받기 (@RequestBody, @PostMapping)

### JPA 기초 (5/5 통과 시 이수)
- [ ] 1. 엔티티 클래스 정의 및 매핑 (Post 엔티티 생성)
- [ ] 2. Repository 인터페이스 생성 및 주입
- [ ] 3. Create/Read API 구현 (POST, GET)
- [ ] 4. Update/Delete API 구현 (PUT, DELETE)
- [ ] 5. 쿼리 메서드 사용 (조건별 조회 구현)

### 종합 과제 (선택)
- [ ] Post 엔티티에 Title, Content 필드 추가
- [ ] PostRepository에 findByTitleContaining 메서드 구현
- [ ] CRUD API 구현 및 테스트

---

## 📝 테스트 방법

1. **서버 실행**
   ```bash
   ./gradlew bootRun
   # 또는 IntelliJ IDEA 에서 Application 실행
   ```

2. **API 테스트**
   - 브라우저 또는 Postman 사용
   - `http://localhost:8080/api/v1/posts` 엔드포인트 호출

3. **데이터베이스 확인**
   - H2 Console: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`

---

## 📚 다음 단계

Level 1 이수 후 진행할 내용:
- JPA Level 2: 관계 매핑 심화 (Cascade, Fetch 전략)
- JPA Level 3: 쿼리 최적화 (N+1 문제, Join Fetch)
- Spring Boot 심화: Validation, Exception Handling
