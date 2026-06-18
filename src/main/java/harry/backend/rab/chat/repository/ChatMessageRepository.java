package harry.backend.rab.chat.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import harry.backend.rab.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	/**
	 * 방의 최신 메시지부터 size개. sender를 fetch join 해서 N+1을 막는다.
	 */
	@Query("select m from ChatMessage m join fetch m.sender "
		+ "where m.room.id = :roomId order by m.id desc")
	List<ChatMessage> findRecentByRoom(@Param("roomId") Long roomId, Pageable pageable);

	/**
	 * beforeId보다 과거(작은 id)의 메시지 size개. 위로 스크롤(더 불러오기)용 커서 페이징.
	 */
	@Query("select m from ChatMessage m join fetch m.sender "
		+ "where m.room.id = :roomId and m.id < :beforeId order by m.id desc")
	List<ChatMessage> findBeforeByRoom(@Param("roomId") Long roomId,
		@Param("beforeId") Long beforeId, Pageable pageable);
}
