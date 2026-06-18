package harry.backend.rab.chat.dto;

import java.time.Instant;

import harry.backend.rab.chat.domain.ChatMessage;

public record MessageResponse(
	Long id,
	Long roomId,
	Long senderId,
	String senderName,
	String content,
	Instant createdAt
) {

	public static MessageResponse from(ChatMessage m) {
		return new MessageResponse(
			m.getId(),
			m.getRoom().getId(),
			m.getSender().getId(),
			m.getSender().getDisplayName(),
			m.getContent(),
			m.getCreatedAt()
		);
	}
}
