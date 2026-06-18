package harry.backend.rab.chat.dto;

import harry.backend.rab.chat.domain.ChatRoom;

public record RoomResponse(Long id, String name) {

	public static RoomResponse from(ChatRoom room) {
		return new RoomResponse(room.getId(), room.getName());
	}
}
