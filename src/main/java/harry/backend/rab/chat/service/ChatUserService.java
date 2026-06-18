package harry.backend.rab.chat.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import harry.backend.rab.chat.domain.ChatUser;
import harry.backend.rab.chat.dto.UserResponse;
import harry.backend.rab.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatUserService {

	private final ChatUserRepository userRepository;

	@Transactional
	public UserResponse create(String username, String displayName) {
		if (userRepository.existsByUsername(username)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 username입니다: " + username);
		}
		ChatUser saved = userRepository.save(new ChatUser(username, displayName));
		return UserResponse.from(saved);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findAll() {
		return userRepository.findAll().stream().map(UserResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ChatUser getOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다: " + userId));
	}
}
