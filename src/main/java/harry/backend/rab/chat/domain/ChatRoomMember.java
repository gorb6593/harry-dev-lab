package harry.backend.rab.chat.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 방-사용자 참여 관계. (room, user) 조합은 유일하다.
 * lastReadMessageId는 안읽음 개수 계산에 쓸 예정(지금은 가입 시 0).
 */
@Entity
@Table(
	name = "chat_room_member",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_room_member",
		columnNames = {"room_id", "user_id"}
	)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", nullable = false)
	private ChatRoom room;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private ChatUser user;

	@Column(nullable = false)
	private Long lastReadMessageId;

	@Column(nullable = false, updatable = false)
	private Instant joinedAt;

	public ChatRoomMember(ChatRoom room, ChatUser user) {
		if (room == null || user == null) {
			throw new IllegalArgumentException("room과 user는 필수입니다.");
		}
		this.room = room;
		this.user = user;
		this.lastReadMessageId = 0L;
	}

	@PrePersist
	void onCreate() {
		this.joinedAt = Instant.now();
	}
}
