package harry.backend.rab.jpaLevel7.repository;

import harry.backend.rab.jpaLevel7.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
}
