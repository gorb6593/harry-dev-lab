package harry.backend.rab.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import harry.backend.rab.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
