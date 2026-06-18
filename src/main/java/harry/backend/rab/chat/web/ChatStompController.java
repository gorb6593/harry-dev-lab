package harry.backend.rab.chat.web;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import harry.backend.rab.chat.dto.MessageResponse;
import harry.backend.rab.chat.dto.SendMessageRequest;
import harry.backend.rab.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;

/**
 * WebSocket(STOMP) 메시지 처리.
 *
 * 흐름:
 *  1) 클라이언트가 "/app/rooms/{roomId}/send"로 메시지를 보낸다.
 *  2) 여기서 DB에 저장한다.
 *  3) 저장 결과(id, 시각 포함)를 "/topic/rooms/{roomId}" 구독자 전원에게 브로드캐스트한다.
 *
 * @SendTo 대신 SimpMessagingTemplate를 쓰는 이유: 저장 후 생성된 id/createdAt이 채워진
 * 응답을 보내기 위해서다.
 */
@Controller
@RequiredArgsConstructor
public class ChatStompController {

	private final ChatMessageService messageService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/rooms/{roomId}/send")
	public void send(@DestinationVariable Long roomId, @Payload SendMessageRequest request) {
		MessageResponse saved = messageService.send(roomId, request.senderId(), request.content());
		messagingTemplate.convertAndSend("/topic/rooms/" + roomId, saved);
	}
}
