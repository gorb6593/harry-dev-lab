# Chat App — 실전 학습 프로젝트

`rab` 안에서 진행하는 메인 학습 프로젝트. 기존 `dayN_*` 연습 폴더는 무시하고,
여기서 하나의 실제 채팅 서비스를 처음부터 만들며 백엔드 핵심 역량을 쌓는다.

## 왜 채팅 앱인가

원래 배우고 싶던 주제(JPA, 영속성, N+1, 동시성, 트랜잭션, 스케일링)가
채팅 앱 안에 전부 자연스럽게 들어있기 때문이다.

- 메시지 저장·조회 → JPA, N+1, 페이징
- 읽음 처리·전송 정합성 → 트랜잭션
- 동시 전송·메시지 순서·접속 상태 → 동시성
- 동접 증가 대응 → 스케일링, 부하 테스트

## 채팅 앱의 4개 층 (서로 다른 기술)

| 층 | 내용 | 핵심 기술 | 상태 |
| --- | --- | --- | --- |
| 1. 텍스트 채팅 | 실시간 메시지 | WebSocket + STOMP, DB 저장 | 진행 예정 |
| 2. 미디어 첨부 | 이미지/동영상 파일 | HTTP 업로드 → 저장소 → URL 공유 | 보류 |
| 3. 실시간 통화 | 화상/음성 통화 | WebRTC (별도 큰 산) | 보류 |
| 4. 트래픽·스케일 | 동접 증가 대응 | 부하 테스트, Redis pub/sub, 수평 확장 | 보류 |

지금은 **층 1(텍스트)** 에 집중한다. 나머지는 텍스트가 탄탄해진 뒤에 연다.

## 로드맵 (텍스트 채팅 기준)

- [x] **1단계 — 실시간 텍스트 채팅 (MVP 완료)**: WebSocket+STOMP, 채팅방, 메시지 DB 저장, 히스토리 커서 페이징, 웹 클라이언트
  - [x] 도메인: ChatUser / ChatRoom / ChatRoomMember / ChatMessage
  - [x] REST: 사용자·방 생성/참여/멤버조회, 메시지 히스토리(커서 페이징)
  - [x] 실시간: STOMP `/app/rooms/{id}/send` → `/topic/rooms/{id}` 브로드캐스트
  - [x] 웹 클라이언트: `/chat.html` (SockJS+STOMP)
  - [x] N+1 회피: 히스토리 조회 시 sender fetch join
- [x] **기초 설계 고정**: 코딩 규칙 문서화(CONVENTIONS.md), ProblemDetail(RFC7807) 에러 표준, LOB 제거·텍스트블록 적용 — 검증 완료
- [ ] **1.5단계 — 다듬기**: 인증/로그인, 읽음 처리(lastReadMessageId)와 안읽음 개수, 접속 상태(presence), 입력 중 표시, 캐치업 동기화(afterId)
- [ ] **2단계 — 미디어 첨부**: 이미지/동영상 파일 업로드 → 저장소(MinIO) → URL 공유
- [ ] **3단계 — 트래픽·스케일링**: 부하 테스트로 한 대 한계 측정 → Redis 릴레이로 수평 확장
- [ ] (선택) **4단계 — 실시간 화상통화 (WebRTC)**

## 동작 방식 (1단계 구조)

```text
[브라우저/앱]
   │  REST(JSON)            │  WebSocket(STOMP over SockJS)
   │  사용자/방/히스토리     │  send → /app/rooms/{id}/send
   ▼                        ▼
[Spring Boot :25000]  ── ChatStompController ──┐
   ├─ ChatUser/Room/Message Controller         │ 저장 후
   ├─ Service(@Transactional) ── JPA ── MySQL  │ /topic/rooms/{id} 으로
   └─ SimpMessagingTemplate ───────────────────┘ 구독자 전원에게 브로드캐스트
```

확장 포인트: 지금은 in-memory 심플 브로커라 단일 서버. 1만 동접/다중 서버 단계(3단계)에서
`enableStompBrokerRelay` + Redis/RabbitMQ로 교체하면 서버 간 메시지가 공유된다.

## 코딩 규칙

이 프로젝트의 설계·코딩 표준은 [`CONVENTIONS.md`](CONVENTIONS.md)에 정리. (record DTO, 엔티티 setter 금지,
생성자 주입, ProblemDetail 에러, fetch join, 커서 페이징 등) 새 코드와 리뷰는 이 문서를 기준으로 한다.

## 진행 원칙 (멘토 방식)

1. 한 번에 한 Task만.
2. 개념 설명 → 사용자가 직접 구현 → 테스트로 검증 → 막히면 질문 → 리뷰.
3. 정답 코드를 먼저 쏟아내지 않는다. 힌트와 리뷰 중심.
4. 사용자가 "완료" 의사를 밝히기 전까지 다음 Task로 넘어가지 않는다.

## 기술 기준

- Java 17, Spring Boot 3.5.x, Gradle (build.gradle 그대로 사용)
- DB: MySQL (docker-compose, 포트 13306), 앱 포트 25000
- 테스트: JUnit 5

## 진행 상태

- **1단계(실시간 텍스트 채팅 MVP) 완료, 실행·검증됨.**
  - 앱 실행: `docker compose up -d` (MySQL) → `./gradlew bootRun` (포트 25000)
  - 사용해보기: 브라우저로 `http://localhost:25000/chat.html` 접속 → 두 탭에서 다른 닉네임으로 로그인 → 같은 방 입장 → 대화
- 다음 후보: 1.5단계(인증/읽음처리/presence) 또는 바로 3단계(부하 테스트로 한계 측정).
