package harry.backend.rab.jpaLevel7.service;

import harry.backend.rab.jpaLevel7.dto.ConcurrencyObservationResponse;
import harry.backend.rab.jpaLevel7.dto.StockCreateRequest;
import harry.backend.rab.jpaLevel7.dto.StockDecreaseRequest;
import harry.backend.rab.jpaLevel7.dto.StockDecreaseWithDelayRequest;
import harry.backend.rab.jpaLevel7.dto.StockResponse;
import harry.backend.rab.jpaLevel7.entity.StockItem;
import harry.backend.rab.jpaLevel7.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockLevel7Service {

    private final StockItemRepository stockItemRepository;

    public StockResponse create(StockCreateRequest request) {
        StockItem stockItem = StockItem.create(request.name(), request.quantity());
        StockItem savedStockItem = stockItemRepository.save(stockItem);
        return StockResponse.from(savedStockItem);
    }

    @Transactional(readOnly = true)
    public StockResponse findById(Long stockItemId) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found. id=" + stockItemId));
        return StockResponse.from(stockItem);
    }

    public ConcurrencyObservationResponse decrease(Long stockItemId, StockDecreaseRequest request) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found. id=" + stockItemId));

        int beforeQuantity = stockItem.getQuantity();
        stockItem.decrease(request.quantity());

        return new ConcurrencyObservationResponse(
                "JPA를 사용한다고 해서 왜 동시성 문제가 자동으로 해결되지 않는가?",
                stockItemId,
                beforeQuantity,
                request.quantity(),
                stockItem.getQuantity(),
                "지금 코드는 한 트랜잭션 안에서 read-modify-write를 수행하지만, 동시에 두 요청이 같은 재고를 읽으면 마지막 커밋이 앞선 변경을 덮어쓸 수 있다. JPA의 dirty checking은 update SQL을 자동화할 뿐, 동시성 제어 자체를 보장하지 않는다.",
                "다음 질문: 동시에 같은 row를 수정할 때 lost update를 막으려면 왜 낙관적 락이나 비관적 락이 필요한가?"
        );
    }

    public ConcurrencyObservationResponse decreaseWithDelay(Long stockItemId, StockDecreaseWithDelayRequest request) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found. id=" + stockItemId));

        int beforeQuantity = stockItem.getQuantity();
        sleep(request.delayMillis());
        stockItem.decrease(request.quantity());

        return new ConcurrencyObservationResponse(
                "조회 후 잠시 대기하면 왜 lost update가 더 쉽게 재현되는가?",
                stockItemId,
                beforeQuantity,
                request.quantity(),
                stockItem.getQuantity(),
                "두 요청이 같은 시점의 수량을 먼저 읽고 각각 대기한 뒤 수정하면, 둘 다 같은 beforeQuantity를 기준으로 계산할 수 있다. 이 상태에서 마지막 커밋이 앞선 커밋을 덮어써 lost update가 더 쉽게 드러난다.",
                "다음 질문: 이 충돌을 막으려면 낙관적 락과 비관적 락 중 어떤 전략이 필요한가?"
        );
    }

    private void sleep(long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Sleep interrupted.", exception);
        }
    }
}
