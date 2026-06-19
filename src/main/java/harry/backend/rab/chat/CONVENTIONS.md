# Chat 프로젝트 코딩 규칙 (Foundation)

이 프로젝트의 모든 코드는 아래 규칙을 따른다. 새 코드를 짤 때, 코드 리뷰할 때 이 문서를 기준으로 한다.
기준 스택: **Java 17, Spring Boot 3.5.x, Gradle, JPA(Hibernate), MySQL.**

## 1. 계층과 의존성 주입

- 흐름은 `Controller → Service → Repository` 한 방향. 역방향 의존 금지.
- 의존성 주입은 **생성자 주입만**. `@Autowired` 필드 주입 금지.
  - `@RequiredArgsConstructor` + `private final` 필드 사용.
- 컨트롤러는 얇게: 요청 받기/검증/DTO 변환만. 비즈니스 로직은 서비스·도메인에.

## 2. 엔티티 (JPA 도메인)

- **setter 금지.** 상태 변경은 의도를 드러내는 메서드로. (예: `member.markRead(id)`, `stock.decrease(n)`)
- Lombok은 `@Getter`만. **`@Data`/`@Setter`/엔티티에 `@Builder` 금지** (양방향 연관·지연로딩에서 equals/hashCode/toString 사고를 부른다).
- 기본 생성자는 `@NoArgsConstructor(access = AccessLevel.PROTECTED)`. 외부에서 빈 객체 생성 차단, JPA만 사용.
- public 생성자에서 **불변식 검증**(null/blank/범위). 잘못된 상태의 엔티티가 만들어지지 못하게.
- 연관관계는 **항상 `fetch = LAZY`**. `@ManyToOne`의 기본값(EAGER)을 명시적으로 LAZY로 덮는다.
- 시간은 `Instant`로 통일(UTC 저장). 생성 시각은 `@PrePersist`에서 채운다.
- 컬럼 길이를 명시. 짧은 텍스트에 `@Lob` 쓰지 않는다(LOB은 별도 스토리지/스트리밍 오버헤드).
- `equals`/`hashCode`는 **기본적으로 오버라이드하지 않는다.** Set에 담는 등 꼭 필요할 때만, 생성된 id가 아니라 비즈니스 키 기준으로 신중히 추가.
- **낙관적 락(`@Version`)** 은 "같은 행을 동시에 수정"하는 위험이 있을 때만 추가. 추가(append) 위주인 메시지엔 불필요.

## 3. DTO

- 요청/응답 DTO는 **`record`** 로. (불변, 보일러플레이트 없음 — 최신 권장)
- 엔티티를 컨트롤러 밖으로 노출하지 않는다. 항상 DTO로 변환해 반환.
- 응답 DTO는 정적 팩토리 `from(entity)` 제공.
- 요청 DTO는 Bean Validation(`@NotBlank`, `@Size`, `@NotNull` …) + 컨트롤러에서 `@Valid`.

## 4. 리포지토리

- Spring Data JPA 인터페이스 사용. 쿼리 메서드 이름 규칙 우선.
- 커스텀 JPQL은 `@Query` + **텍스트 블록**으로 가독성 있게.
- 컬렉션을 같이 쓰는 조회는 **fetch join으로 N+1 차단**. (모든 조회에 무조건은 금지 — 필요한 곳만.)
- 목록 조회는 **커서 페이징**(`id < :beforeId`) 우선. offset 페이징은 뒤로 갈수록 느려지므로 피한다.

## 5. 서비스와 트랜잭션

- 트랜잭션 경계는 **서비스 메서드**. 조회 전용은 `@Transactional(readOnly = true)`.
- 한 메서드 = 한 유스케이스. 외부에서 호출되는 public 메서드에 트랜잭션을 건다(self-invocation 주의).
- 멱등하게 설계할 수 있으면 그렇게 (예: 방 참여는 이미 멤버면 조용히 통과).

## 6. API와 에러 처리

- REST 경로는 자원 중심, 복수형. (`/api/chat/rooms/{roomId}/messages`)
- 생성은 `201 Created`, 본문 없는 성공은 `204 No Content`.
- **에러 응답은 `ProblemDetail`(RFC 7807, application/problem+json) 로 통일.**
  - 처리기: [`ChatApiExceptionHandler`](web/ChatApiExceptionHandler.java) (`@RestControllerAdvice(basePackages="...chat")`, 최우선 순위).
  - 검증 실패 400, 도메인 위반 400, 없음 404, 충돌 409, 권한 403, 그 외 500.
- 서비스에서 흐름 제어용 예외는 `ResponseStatusException`(상태 + 사유). 도메인 검증은 `IllegalArgumentException`.

## 7. 실시간(WebSocket/STOMP)

- 전송: 클라 → `/app/...`(@MessageMapping). 구독/브로드캐스트: `/topic/...`.
- 저장 후 생성된 결과(id/시각 포함)를 `SimpMessagingTemplate`로 publish.
- 인증 전까지는 본문의 senderId로 식별. 인증 도입 시 서버 세션(Principal) 기반으로 교체.

## 8. 설정

- `application.yml`의 `ddl-auto: update`는 **개발 전용**. 스키마가 안정되면 운영은 `validate` + 마이그레이션 도구(Flyway/Liquibase).
- 비밀값은 코드에 하드코딩하지 않는다(환경변수/설정 분리). 현재 로컬 docker 값은 학습용.

## 9. 테스트

- 핵심 로직(서비스/도메인)은 테스트로 검증. JUnit 5.
- 통합 테스트는 가능하면 Testcontainers(MySQL)로 실제 DB와 동일하게.

## 10. 알려진 정리 대상 (기술 부채)

- `day*` 패키지는 **사용하지 않는 연습 코드**. 특히 `day1...GlobalExceptionHandler`가 전역으로 떠 있어
  chat의 에러 포맷을 흐릴 수 있다. 현재는 chat 처리기를 최우선 순위로 두어 회피. 추후 day 패키지 일괄 제거 검토.
