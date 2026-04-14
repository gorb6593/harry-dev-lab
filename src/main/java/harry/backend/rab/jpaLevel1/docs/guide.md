# JPA Level 1 - 학습 가이드

## 1. Spring Boot 기초

### 1.1 프로젝트 구조
```
rab/
├── src/main/java/harry/backend/rab/
│   ├── jpaLevel1/
│   │   ├── controller/  # REST API 엔드포인트
│   │   ├── entity/      # JPA 엔티티
│   │   ├── repository/  # 데이터 액세스 레이어
│   │   └── service/     # 비즈니스 로직
│   └── RabApplication.java
├── src/main/resources/
│   └── application.yml  # 설정 파일
└── build.gradle         # 의존성 관리
```

---

## 2. JPA 엔티티 만들기

### 2.1 Post 엔티티 생성

**목표**: Post 엔티티 클래스를 생성하세요.

```java
package harry.backend.rab.jpaLevel1.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // 생성자, Getter, Setter 필요
}
```

**이수 기준**:
- [ ] @Entity 어노테이션 사용
- [ ] @Id 와 @GeneratedValue 사용
- [ ] id, title, content 필드 포함

---

## 3. Repository 만들기

### 3.1 PostRepository 생성

**목표**: JpaRepository 를 상속한 Repository 인터페이스를 생성하세요.

```java
package harry.backend.rab.jpaLevel1.repository;

import harry.backend.rab.jpaLevel1.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // TODO: 쿼리 메서드 추가
    // List<Post> findByTitleContaining(String keyword);
}
```

**이수 기준**:
- [ ] JpaRepository<Post, Long> 상속
- [ ] findByTitleContaining 메서드 추가

---

## 4. CRUD API 구현

### 4.1 DTO 생성 (선택)

**목표**: 요청/응답을 위한 DTO 클래스를 생성하세요.

```java
// PostRequest.java
public record PostRequest(String title, String content) {}

// PostResponse.java
public record PostResponse(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt
) {}
```

### 4.2 Controller 확장

**목표**: PostController 에 CRUD 메서드를 추가하세요.

```java
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // GET /api/v1/posts - 전체 조회
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // GET /api/v1/posts/{id} - 단일 조회
    @GetMapping("/{id}")
    public Optional<Post> getPost(@PathVariable Long id) {
        return postRepository.findById(id);
    }

    // POST /api/v1/posts - 생성
    @PostMapping
    public Post createPost(@RequestBody PostRequest request) {
        Post post = new Post(request.title(), request.content());
        return postRepository.save(post);
    }

    // PUT /api/v1/posts/{id} - 수정
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        post.update(request.title(), request.content());
        return post;
    }

    // DELETE /api/v1/posts/{id} - 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
```

**이수 기준**:
- [ ] Constructor Injection으로 Repository 주입
- [ ] 전체 조회 API 구현
- [ ] 단일 조회 API 구현
- [ ] 생성 API 구현
- [ ] 수정 API 구현
- [ ] 삭제 API 구현

---

## 5. 테스트 가이드

### 5.1 서버 실행
```bash
./gradlew bootRun
```

### 5.2 API 테스트 (cURL 예시)

**Post 생성**:
```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -d '{"title": "첫 번째 포스트", "content": "내용입니다"}'
```

**전체 조회**:
```bash
curl http://localhost:8080/api/v1/posts
```

**단일 조회**:
```bash
curl http://localhost:8080/api/v1/posts/1
```

**수정**:
```bash
curl -X PUT http://localhost:8080/api/v1/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "수정된 제목", "content": "수정된 내용"}'
```

**삭제**:
```bash
curl -X DELETE http://localhost:8080/api/v1/posts/1
```

### 5.3 H2 Console 확인
1. 브라우저에서 `http://localhost:8080/h2-console` 접속
2. JDBC URL: `jdbc:h2:mem:testdb`
3. Username: `sa`
4. Password: (비어있음)
5. 테이블 확인: `SELECT * FROM posts;`

---

## 🎯 완료 체크리스트

- [ ] Post 엔티티 생성
- [ ] PostRepository 생성
- [ ] findByTitleContaining 메서드 구현
- [ ] CRUD API 모두 구현
- [ ] cURL 또는 Postman 으로 테스트 완료
- [ ] H2 Console 에서 데이터 확인
