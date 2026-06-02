package harry.backend.rab.day1_260602.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import harry.backend.rab.day1_260602.domain.StockItem;
import harry.backend.rab.day1_260602.service.StockService;

@RestController
@RequestMapping("/api/day1/stocks")
public class StockController {

	private final StockService stockService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public StockItemResponse create(@RequestBody StockCreateRequest request) {
		StockItem stockItem = stockService.create(request.name(), request.quantity());
		return StockItemResponse.from(stockItem);
	}

	@GetMapping("/{stockItemId}")
	public StockItemResponse findById(@PathVariable Long stockItemId) {
		return StockItemResponse.from(stockService.findById(stockItemId));
	}

	@PostMapping("/{stockItemId}/decrease")
	public StockItemResponse decrease(
		@PathVariable Long stockItemId,
		@RequestBody StockDecreaseRequest request
	) {
		stockService.decrease(stockItemId, request.quantity());
		return StockItemResponse.from(stockService.findById(stockItemId));
	}
}
