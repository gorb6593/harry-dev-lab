# Chat 서비스 — 검증 요청 (현재 상태)

> 실시간 텍스트 채팅 서비스의 1단계(MVP) + 기초 설계를 만들었습니다.
> **본격 확장 전에 설계·코드 방향이 맞는지 리뷰**를 받고 싶습니다. 아래 "리뷰어에게 묻고 싶은 것"이 핵심입니다.

관련 문서: 로드맵 [`README.md`](README.md) · 코딩 규칙 [`CONVENTIONS.md`](CONVENTIONS.md)

---

## 1. 한눈에 보는 상태

| 구분 | 상태 |
| --- | --- |
| 기능 범위 | 실시간 텍스트 채팅 (방 생성·참여, 메시지 송수신, 히스토리) |
| 동작 | ✅ 실행·검증 완료 (REST + WebSocket 실시간 + 에러 포맷) |
| 규모 목표 | 동접 ~1만 명 가정 (현재는 단일 노드) |
| 코드량 | chat 패키지 Java ~850줄 + 웹 클라이언트 1개 |
| 자동화 테스트 | ❌ 아직 없음 (수동 검증만) — 리뷰 포인트 |

## 2. 기술 스택

- Java 17, Spring Boot 3.5.x, Gradle
- Spring Web(REST) + Spring WebSocket(STOMP over SockJS)
- Spring Data JPA / Hibernate, MySQL 9 (docker-compose, 포트 13306)
- Lombok, Bean Validation
- 에러 응답: `ProblemDetail`(RFC 7807)

## 3. 아키텍처 (요청 흐름)

```text
[웹/앱 클라이언트]
   │ REST(JSON): 사용자/방/히스토리           │ WebSocket(STOMP): 실시간 송수신
   ▼                                          ▼
ChatUser/Room/MessageController        ChatStompController
   │                                          │  @MessageMapping /app/rooms/{id}/send
   ▼                                          ▼
Service (@Transactional) ── JPA ── MySQL    저장 후 SimpMessagingTemplate으로
                                            /topic/rooms/{id} 구독자에게 브로드캐스트
```

- 실시간 브로커: 내장 SimpleBroker(in-memory). **단일 노드 기준.** 다중 노드 확장 시 Redis/RabbitMQ 릴레이로 교체 예정.

## 4. 구현된 기능 / API

REST (`/api/chat`)

| Method | Path | 설명 | 성공 |
| --- | --- | --- | --- |
| POST | `/users` | 사용자 생성 (username 유니크) | 201 |
| GET | `/users` | 사용자 목록 | 200 |
| POST | `/rooms` | 방 생성 | 201 |
| GET | `/rooms` | 방 목록 | 200 |
| POST | `/rooms/{roomId}/members` | 방 참여 (멱등) | 204 |
| GET | `/rooms/{roomId}/members` | 방 멤버 목록 | 200 |
| GET | `/rooms/{roomId}/messages?beforeId=&size=30` | 히스토리 (커서 페이징) | 200 |

WebSocket

- 연결: `GET /ws` (SockJS) → STOMP CONNECT
- 구독: `SUBSCRIBE /topic/rooms/{roomId}`
- 전송: `SEND /app/rooms/{roomId}/send` body `{ "senderId": 1, "content": "..." }`

에러: 모두 `application/problem+json` (검증 실패 400 + 필드별 `errors`, 없음 404, 권한 403, 충돌 409)

## 5. 데이터 모델

```text
chat_user(id, username[uk], display_name, created_at)
chat_room(id, name, created_at)
chat_room_member(id, room_id, user_id, last_read_message_id, joined_at)   -- uk(room_id, user_id)
chat_message(id, room_id, sender_id, content(4000), created_at)           -- index(room_id, id)
```

연관관계는 모두 `@ManyToOne(LAZY)`. 히스토리 조회는 sender를 fetch join 해 N+1 차단.

## 6. 실행 & 검증 방법 (리뷰어용)

```bash
docker compose up -d            # MySQL :13306
./gradlew bootRun               # 앱 :25000
# 브라우저: http://localhost:25000/chat.html
#   → 탭 2개에서 다른 닉네임 로그인 → 같은 방 입장 → 실시간 대화 확인
```

빠른 API 확인:

```bash
curl -X POST localhost:25000/api/chat/users -H 'Content-Type: application/json' -d '{"username":"alice","displayName":"앨리스"}'
curl -X POST localhost:25000/api/chat/rooms -H 'Content-Type: application/json' -d '{"name":"일반"}'
curl localhost:25000/api/chat/rooms/1/messages
```

## 7. 검증 완료 항목

- REST 전체 흐름(사용자·방·참여·멤버·히스토리) 정상
- WebSocket 실시간 송수신: A 전송 → B 즉시 수신, 메시지 DB 영속화 확인
- 에러 포맷: 검증 실패 400(필드별 errors), 없음 404 — ProblemDetail로 일관
- 컴파일/기동 정상(~1.6s)

## 8. 의도적으로 아직 안 한 것 / 알려진 한계 (= 리뷰 후 진행 예정)

- **인증/인가 없음.** 지금은 `senderId`를 본문으로 신뢰 → 다음 단계에서 세션 기반으로 교체 예정
- **읽음 처리/안읽음 개수, presence(접속자), 입력 중 표시** 미구현 (`last_read_message_id` 필드만 존재)
- **캐치업 동기화(`afterId`) 미구현.** 현재 히스토리는 과거 방향(`beforeId`)만 → 백그라운드 복귀 시 누락분 동기화는 추후
- **단일 노드.** 다중 노드/Redis 백플레인/Kafka 미적용 — "부하 테스트로 한계 실측 후 필요하면 도입" 방침 (과설계 회피)
- **푸시 알림(FCM/APNs) 없음**
- **자동화 테스트 없음** (수동 검증만)
- 운영 설정 미정리: `ddl-auto: update`(개발용), CORS/WS Origin 전체 허용(개발용), 마이그레이션 도구 없음
- 방 타입(1:1/그룹) 구분 없음, 메시지 길이 4000 제한
- `day*` 연습 패키지 제거 진행 중 (deprecated)

## 9. 리뷰어에게 묻고 싶은 것 (핵심)

1. **설계 방향**: 패키지 구조(feature `chat` 안 layer 분리), 엔티티/DTO/예외 설계가 실무 기준으로 적절한가?
2. **임시 식별**: `senderId`를 메시지 본문으로 받는 구조의 위험과, 인증 도입 전까지 허용 가능한 수준인지?
3. **트랜잭션/JPA**: 트랜잭션 경계, fetch join을 통한 N+1 회피, 커서 페이징 방식이 올바른가?
4. **우선순위**: 다음으로 (a) 인증 (b) 캐치업 동기화/재연결 (c) 부하 테스트 실측 중 무엇을 먼저 해야 하나?
5. **테스트**: 자동화 테스트 부재를 지금 메꾸고 가야 하는가, 아니면 기능 더 쌓은 뒤인가?
6. **확장 시점**: "측정 후 Redis 도입" 접근이 합리적인가, 처음부터 다중 노드 대비를 해둬야 하나?

## 10. 파일 맵

```text
chat/
├─ README.md            로드맵·진행 상태
├─ CONVENTIONS.md       코딩 규칙
├─ REVIEW.md            (이 문서)
├─ domain/              ChatUser, ChatRoom, ChatRoomMember, ChatMessage
├─ dto/                 요청/응답 record 7개
├─ repository/          Spring Data JPA 4개
├─ service/             ChatUser/Room/Message Service
├─ web/                 REST 컨트롤러 3 + STOMP 컨트롤러 1 + 예외처리기
└─ config/              WebSocketConfig(STOMP), WebConfig(CORS)
resources/static/chat.html   테스트용 웹 클라이언트
```
