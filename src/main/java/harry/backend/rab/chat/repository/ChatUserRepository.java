package harry.backend.rab.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import harry.backend.rab.chat.domain.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

	Optional<ChatUser> findByUsername(String username);

	boolean existsByUsername(String username);
}
