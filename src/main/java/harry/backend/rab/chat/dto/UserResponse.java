package harry.backend.rab.chat.dto;

import harry.backend.rab.chat.domain.ChatUser;

public record UserResponse(Long id, String username, String displayName) {

	public static UserResponse from(ChatUser user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getDisplayName());
	}
}
