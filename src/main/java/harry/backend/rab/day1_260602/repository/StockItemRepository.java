package harry.backend.rab.day1_260602.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import harry.backend.rab.day1_260602.domain.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select stockItem from StockItem stockItem where stockItem.id = :stockItemId")
	Optional<StockItem> findByIdForUpdate(@Param("stockItemId") Long stockItemId);
}
