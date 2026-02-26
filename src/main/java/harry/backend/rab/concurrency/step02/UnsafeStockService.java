package harry.backend.rab.concurrency.step02;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UnsafeStockService {

	// 실습용 인메모리 저장소.
	// ConcurrentHashMap 자체는 스레드 안전하지만, "조회 후 수정 후 저장" 복합 연산은 원자적이지 않다.
	private final Map<Long, Integer> stockStore = new ConcurrentHashMap<>();

	// 초기 재고를 세팅한다.
	// 웹에서 실험을 반복할 때 같은 productId를 재초기화하는 용도로 사용한다.
	public void initStock(long productId, int quantity) {
		stockStore.put(productId, quantity);
	}

	// 현재 재고를 조회한다.
	public int currentStock(long productId) {
		return stockStore.getOrDefault(productId, 0);
	}

	// 재고를 감소시킨다.
	// 내부적으로 read -> modify -> write 패턴을 수행하며, 동시성 제어가 없다.
	public void decrease(long productId, int quantity) {
		decrease(productId, quantity, () -> {
		});
	}

	// beforeWriteHook은 실험용 확장 포인트다.
	// 여러 요청이 같은 시점에 write 하도록 맞추면 Lost Update가 더 쉽게 드러난다.
	void decrease(long productId, int quantity, Runnable beforeWriteHook) {
		// 1) 현재 재고 읽기
		int current = stockStore.getOrDefault(productId, 0);
		// 2) (선택) 실험 훅 실행
		beforeWriteHook.run();
		// 3) 계산 후 저장
		// 동시 요청이 같은 current 값을 읽으면 마지막 저장이 이전 저장을 덮어써 정합성이 깨진다.
		stockStore.put(productId, current - quantity);
	}
}
