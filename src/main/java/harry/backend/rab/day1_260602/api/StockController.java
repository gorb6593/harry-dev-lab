package harry.backend.rab.day1_260602.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import harry.backend.rab.day1_260602.domain.StockItem;
import harry.backend.rab.day1_260602.service.StockService;
import harry.backend.rab.day1_260602.service.command.StockCreateCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/day1/stocks")
public class StockController {

	private final StockService stockService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@PostMapping
	public ResponseEntity<StockItemResponse> create(@Valid @RequestBody StockCreateRequest request) {
		StockItem stockItem = stockService.create(new StockCreateCommand(request.name(), request.quantity()));
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{stockItemId}")
			.buildAndExpand(stockItem.getId())
			.toUri();

		return ResponseEntity.created(location).body(StockItemResponse.from(stockItem));
	}

	@GetMapping("/{stockItemId}")
	public StockItemResponse findById(
		@Positive(message = "재고 상품 ID는 1 이상이어야 합니다.")
		@PathVariable Long stockItemId
	) {
		return StockItemResponse.from(stockService.findById(stockItemId));
	}

	@PostMapping("/{stockItemId}/decrease")
	public StockItemResponse decrease(
		@Positive(message = "재고 상품 ID는 1 이상이어야 합니다.")
		@PathVariable Long stockItemId,
		@Valid @RequestBody StockDecreaseRequest request
	) {
		stockService.decrease(stockItemId, request.quantity());
		return StockItemResponse.from(stockService.findById(stockItemId));
	}
}
