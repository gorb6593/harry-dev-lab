package harry.backend.rab.chat.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import harry.backend.rab.chat.dto.MessageResponse;
import harry.backend.rab.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService messageService;

	/**
	 * 메시지 히스토리(커서 페이징).
	 * - beforeId 없음: 최신 size개
	 * - beforeId 있음: 그보다 과거 size개 (위로 스크롤)
	 */
	@GetMapping
	public List<MessageResponse> history(
		@PathVariable Long roomId,
		@RequestParam(required = false) Long beforeId,
		@RequestParam(defaultValue = "30") int size
	) {
		return messageService.history(roomId, beforeId, size);
	}
}
