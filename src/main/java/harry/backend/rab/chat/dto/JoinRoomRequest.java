package harry.backend.rab.chat.dto;

import jakarta.validation.constraints.NotNull;

public record JoinRoomRequest(
	@NotNull Long userId
) {
}
