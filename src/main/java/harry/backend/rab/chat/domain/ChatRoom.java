package harry.backend.rab.chat.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅방. 지금은 그룹방 하나의 형태만 다룬다.
 * 1:1 방, 공개/비공개 구분 등은 이후 단계에서 확장한다.
 */
@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	public ChatRoom(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("방 이름은 비어 있을 수 없습니다.");
		}
		this.name = name.trim();
	}

	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
	}
}
