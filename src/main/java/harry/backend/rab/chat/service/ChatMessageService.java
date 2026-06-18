package harry.backend.rab.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import harry.backend.rab.chat.domain.ChatMessage;
import harry.backend.rab.chat.domain.ChatRoom;
import harry.backend.rab.chat.domain.ChatUser;
import harry.backend.rab.chat.dto.MessageResponse;
import harry.backend.rab.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private static final int MAX_PAGE_SIZE = 100;

	private final ChatMessageRepository messageRepository;
	private final ChatRoomService roomService;
	private final ChatUserService userService;

	/**
	 * 메시지를 저장하고 응답 DTO로 변환한다.
	 * 방/사용자 존재와 방 참여 여부를 검증한 뒤 저장한다.
	 */
	@Transactional
	public MessageResponse send(Long roomId, Long senderId, String content) {
		ChatRoom room = roomService.getOrThrow(roomId);
		ChatUser sender = userService.getOrThrow(senderId);
		if (!roomService.isMember(roomId, senderId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "방에 참여하지 않은 사용자입니다.");
		}
		ChatMessage saved = messageRepository.save(new ChatMessage(room, sender, content));
		return MessageResponse.from(saved);
	}

	/**
	 * 방의 메시지 히스토리. beforeId가 null이면 최신 size개,
	 * 있으면 그보다 과거 size개(위로 더 불러오기). 항상 과거→최신 순으로 정렬해 반환.
	 */
	@Transactional(readOnly = true)
	public List<MessageResponse> history(Long roomId, Long beforeId, int size) {
		roomService.getOrThrow(roomId);
		int limit = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
		PageRequest page = PageRequest.of(0, limit);

		List<ChatMessage> found = (beforeId == null)
			? messageRepository.findRecentByRoom(roomId, page)
			: messageRepository.findBeforeByRoom(roomId, beforeId, page);

		// 쿼리는 최신순(desc)으로 가져오므로, 화면 표시용으로 과거→최신으로 뒤집는다.
		List<MessageResponse> result = new ArrayList<>(found.size());
		for (int i = found.size() - 1; i >= 0; i--) {
			result.add(MessageResponse.from(found.get(i)));
		}
		return result;
	}
}
