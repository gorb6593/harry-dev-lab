package harry.backend.rab.chat.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import harry.backend.rab.chat.domain.ChatRoom;
import harry.backend.rab.chat.domain.ChatRoomMember;
import harry.backend.rab.chat.domain.ChatUser;
import harry.backend.rab.chat.dto.RoomResponse;
import harry.backend.rab.chat.dto.UserResponse;
import harry.backend.rab.chat.repository.ChatRoomMemberRepository;
import harry.backend.rab.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository roomRepository;
	private final ChatRoomMemberRepository memberRepository;
	private final ChatUserService userService;

	@Transactional
	public RoomResponse create(String name) {
		ChatRoom saved = roomRepository.save(new ChatRoom(name));
		return RoomResponse.from(saved);
	}

	@Transactional(readOnly = true)
	public List<RoomResponse> findAll() {
		return roomRepository.findAll().stream().map(RoomResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ChatRoom getOrThrow(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다: " + roomId));
	}

	/** 사용자를 방에 참여시킨다. 이미 참여 중이면 조용히 무시(멱등). */
	@Transactional
	public void join(Long roomId, Long userId) {
		ChatRoom room = getOrThrow(roomId);
		ChatUser user = userService.getOrThrow(userId);
		if (memberRepository.existsByRoomIdAndUserId(roomId, userId)) {
			return;
		}
		memberRepository.save(new ChatRoomMember(room, user));
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findMembers(Long roomId) {
		getOrThrow(roomId);
		return memberRepository.findUsersByRoomId(roomId).stream().map(UserResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public boolean isMember(Long roomId, Long userId) {
		return memberRepository.existsByRoomIdAndUserId(roomId, userId);
	}
}
