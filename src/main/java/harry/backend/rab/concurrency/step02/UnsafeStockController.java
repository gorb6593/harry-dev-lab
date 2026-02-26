package harry.backend.rab.concurrency.step02;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/concurrency/unsafe")
public class UnsafeStockController {

	private final UnsafeStockService unsafeStockService;

	public UnsafeStockController(UnsafeStockService unsafeStockService) {
		this.unsafeStockService = unsafeStockService;
	}

	@PostMapping("/init")
	public ResponseEntity<Map<String, Object>> init(@RequestBody InitRequest request) {
		unsafeStockService.initStock(request.productId(), request.quantity());
		return ResponseEntity.ok(Map.of(
				"productId", request.productId(),
				"stock", unsafeStockService.currentStock(request.productId())
		));
	}

	@PostMapping("/decrease")
	public ResponseEntity<Map<String, Object>> decrease(@RequestBody DecreaseRequest request) {
		unsafeStockService.decrease(request.productId(), request.quantity());
		return ResponseEntity.ok(Map.of(
				"productId", request.productId(),
				"stock", unsafeStockService.currentStock(request.productId())
		));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<Map<String, Object>> current(@PathVariable long productId) {
		return ResponseEntity.ok(Map.of(
				"productId", productId,
				"stock", unsafeStockService.currentStock(productId)
		));
	}

	record InitRequest(long productId, int quantity) {
	}

	record DecreaseRequest(long productId, int quantity) {
	}
}
