package harry.backend.rab.chat.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 한 건의 채팅 메시지.
 * (room_id, id) 복합 인덱스: 방별 히스토리 조회/커서 페이징을 빠르게 한다.
 */
@Entity
@Table(
	name = "chat_message",
	indexes = @Index(name = "idx_message_room_id", columnList = "room_id, id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", nullable = false)
	private ChatRoom room;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sender_id", nullable = false)
	private ChatUser sender;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	public ChatMessage(ChatRoom room, ChatUser sender, String content) {
		if (room == null || sender == null) {
			throw new IllegalArgumentException("room과 sender는 필수입니다.");
		}
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("메시지 내용은 비어 있을 수 없습니다.");
		}
		this.room = room;
		this.sender = sender;
		this.content = content;
	}

	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
	}
}
