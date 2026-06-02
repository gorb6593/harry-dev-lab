package harry.backend.rab.day1_260602.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import harry.backend.rab.day1_260602.domain.StockItem;
import harry.backend.rab.day1_260602.repository.StockItemRepository;

@SpringBootTest(properties = {
	"spring.datasource.url=jdbc:h2:mem:day1;MODE=MySQL;DB_CLOSE_DELAY=-1",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
class StockServiceConcurrencyTest {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockItemRepository stockItemRepository;

	@Test
	void 동시에_재고를_차감해도_정확한_수량이_남아야_한다() throws Exception {
		StockItem stockItem = stockItemRepository.save(new StockItem("Mechanical Keyboard", 20));

		int requestCount = 20;
		ExecutorService executorService = Executors.newFixedThreadPool(requestCount);
		CountDownLatch ready = new CountDownLatch(requestCount);
		CountDownLatch start = new CountDownLatch(1);
		List<Future<?>> futures = new ArrayList<>();

		try {
			for (int index = 0; index < requestCount; index++) {
				futures.add(executorService.submit(() -> {
					ready.countDown();
					start.await();
					stockService.decreaseWithProcessingDelay(stockItem.getId(), 1, Duration.ofMillis(100));
					return null;
				}));
			}

			ready.await();
			start.countDown();

			for (Future<?> future : futures) {
				future.get();
			}
		} finally {
			executorService.shutdown();
		}

		StockItem result = stockItemRepository.findById(stockItem.getId()).orElseThrow();
		assertThat(result.getQuantity()).isZero();
	}
}
