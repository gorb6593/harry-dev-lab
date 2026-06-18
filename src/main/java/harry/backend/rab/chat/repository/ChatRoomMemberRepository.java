package harry.backend.rab.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import harry.backend.rab.chat.domain.ChatRoomMember;
import harry.backend.rab.chat.domain.ChatUser;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

	boolean existsByRoomIdAndUserId(Long roomId, Long userId);

	@Query("select m.user from ChatRoomMember m where m.room.id = :roomId order by m.joinedAt")
	List<ChatUser> findUsersByRoomId(Long roomId);
}
