package harry.backend.rab.chat.dto;

/**
 * WebSocket(STOMP)로 들어오는 메시지 전송 요청.
 * 지금은 senderId를 본문에 담는다. 인증을 붙이면 서버 세션에서 보낸 사람을 식별하게 바꾼다.
 */
public record SendMessageRequest(Long senderId, String content) {
}
