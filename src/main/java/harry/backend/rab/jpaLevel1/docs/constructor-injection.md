# 생성자 주입과 @RequiredArgsConstructor

## 1. @RequiredArgsConstructor 사용이 타당한가?

**네, 완전히 타당합니다.**

현재 `PostController` 의 코드:

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostRepository postRepository;
}
```

이 코드는 다음 명시적 생성자와 **완전히 동일**합니다:

```java
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
```

## 2. 왜 생성자가 필요한가?

### 2.1 Spring 의 의존성 주입 (Dependency Injection)

Spring 프레임워크는 IoC(Inversion of Control) 컨테이너로, 빈 (Bean) 의 생성과 의존성 관리를 자동으로 해줍니다. 하지만 **어떤 생성자를 통해 빈을 생성할지 알아야 합니다.**

```
Spring Container
       ↓
  PostController 생성
       ↓
  PostRepository 를 주입
       ↓
  완성된 빈 반환
```

### 2.2 생성자가 없으면 어떤 문제가 발생하는가?

#### 경우 1: 생성자完全没有 (컴파일 오류)

```java
public class PostController {
    private final PostRepository postRepository;  // final 필드

    // 생성자 없음
}
```

**결과**: 컴파일 오류!
- `final` 필드는 선언 시 또는 생성자에서 반드시 초기화되어야 함
- 생성자가 없으면 필드를 초기화할 수 없음

#### 경우 2: 기본 생성자만 있는 경우 (런타임 오류)

```java
public class PostController {
    private PostRepository postRepository;  // final 아님

    public PostController() {
        // 기본 생성자
    }
}
```

**결과**: `NullPointerException`
- Spring 이 기본 생성자로 객체 생성
- `postRepository` 는 `null` 상태로 남음
- 메서드 호출 시 NPE 발생

#### 경우 3: 여러 생성자가 있는 경우 (의도하지 않은 주입)

```java
public class PostController {
    private PostRepository postRepository;

    public PostController() { }

    public PostController(String someParam) { }
}
```

**결과**: 주입 실패 또는 예상치 못한 동작
- Spring 이 어떤 생성자를 사용할지 모호함
- `@Autowired` 가 필요한데, 그렇지 않으면 기본 생성자 사용

## 3. @RequiredArgsConstructor 의 역할

### 3.1 Lombok 이做什么?

`@RequiredArgsConstructor` 는 **최종 (final) 필드 또는 @NonNull 필드가 있는 생성자를 자동으로 생성**합니다.

```java
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;  // final 필드

    private String optionalField;  // final 아님 → 생성자에 포함 안 됨
}
```

컴파일 후 (Lombok 처리):

```java
public class PostController {
    private final PostRepository postRepository;
    private String optionalField;

    // Lombok 이 자동 생성
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
```

### 3.2 다른 Lombok 어노테이션 비교

| 어노테이션 | 생성하는 생성자 |
|-----------|---------------|
| `@NoArgsConstructor` | 인자가 없는 기본 생성자 |
| `@AllArgsConstructor` | 모든 필드를 인자로 받는 생성자 |
| `@RequiredArgsConstructor` | **final / @NonNull 필드만** 인자로 받는 생성자 |

## 4. Spring 의 권장 방식: 생성자 주입

Spring 공식 문서는 **생성자 주입을 권장**합니다.

### 장점

1. **필수 의존성을 명확하게 표현**
   ```java
   private final PostRepository postRepository;  // 반드시 필요함
   ```

2. **불변성 보장**
   - `final` 필드로 선언 가능
   - 생성 후 변경 불가능

3. **테스트 용이성**
   ```java
   @ExtendWith(MockitoExtension.class)
   class PostControllerTest {
       @Mock PostRepository postRepository;

       @InjectMocks PostController postController;  // 자동 주입
   }
   ```

4. **완전하지 않은 객체 방지**
   - 필수 의존성이 주입되지 않은 상태로 객체가 생성될 수 없음

### setter 주입과의 비교

```java
// ❌ setter 주입 (비권장)
@RestController
public class PostController {
    private PostRepository postRepository;

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
```

문제점:
- `postRepository` 가 `null` 일 수 있는 상태가 존재
- `final` 로 선언 불가
- 의존성이 필수인지 선택인지 불명확

## 5. 결론

```java
@RestController
@RequiredArgsConstructor  // ✅ 권장
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostRepository postRepository;  // ✅ final 사용
}
```

이 방식이 **가장 권장되는 방식**입니다:

1. ✅ 코드가 간결함
2. ✅ Spring 의 의존성 주입이 정상 작동
3. ✅ 필수 의존성을 명확하게 표현
4. ✅ 불변성을 보장
