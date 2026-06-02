package harry.backend.rab.day1_260602.service;

import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import harry.backend.rab.day1_260602.domain.StockItem;
import harry.backend.rab.day1_260602.repository.StockItemRepository;

@Service
public class StockService {

	private final StockItemRepository stockItemRepository;

	public StockService(StockItemRepository stockItemRepository) {
		this.stockItemRepository = stockItemRepository;
	}

	@Transactional
	public StockItem create(String name, int quantity) {
		return stockItemRepository.save(new StockItem(name, quantity));
	}

	@Transactional(readOnly = true)
	public StockItem findById(Long stockItemId) {
		return getStockItem(stockItemId);
	}

	@Transactional
	public void decrease(Long stockItemId, int quantity) {
		decreaseWithProcessingDelay(stockItemId, quantity, Duration.ZERO);
	}

	@Transactional
	public void decreaseWithProcessingDelay(Long stockItemId, int quantity, Duration processingDelay) {
		StockItem stockItem = getStockItemForUpdate(stockItemId);
		stockItem.decrease(quantity);
		sleep(processingDelay);
	}

	private StockItem getStockItem(Long stockItemId) {
		return stockItemRepository.findById(stockItemId)
			.orElseThrow(() -> new StockItemNotFoundException(stockItemId));
	}

	private StockItem getStockItemForUpdate(Long stockItemId) {
		return stockItemRepository.findByIdForUpdate(stockItemId)
			.orElseThrow(() -> new StockItemNotFoundException(stockItemId));
	}

	private void sleep(Duration processingDelay) {
		try {
			Thread.sleep(processingDelay.toMillis());
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("재고 처리 대기 중 인터럽트가 발생했습니다.", exception);
		}
	}
}
