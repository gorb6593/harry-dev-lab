package harry.backend.rab.chat.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅 사용자. 지금은 username/displayName만 가진 단순 신원이다.
 * 인증(비밀번호, 토큰)은 이후 단계에서 붙인다.
 */
@Entity
@Table(
	name = "chat_user",
	uniqueConstraints = @UniqueConstraint(name = "uk_chat_user_username", columnNames = "username")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String displayName;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	public ChatUser(String username, String displayName) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("username은 비어 있을 수 없습니다.");
		}
		if (displayName == null || displayName.isBlank()) {
			throw new IllegalArgumentException("displayName은 비어 있을 수 없습니다.");
		}
		this.username = username.trim();
		this.displayName = displayName.trim();
	}

	@PrePersist
	void onCreate() {
		this.createdAt = Instant.now();
	}
}
