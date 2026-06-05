package harry.backend.rab.day3_260604.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import harry.backend.rab.day3_260604.domain.Day3Product;
import harry.backend.rab.day3_260604.service.PersistenceObservationResponse;
import harry.backend.rab.day3_260604.service.ProductPersistenceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/day3/products")
@RequiredArgsConstructor
public class Day3ProductController {

	private final ProductPersistenceService productPersistenceService;

	@PostMapping
	public ResponseEntity<Day3ProductResponse> create(@Valid @RequestBody Day3ProductCreateRequest request) {
		Day3Product product = productPersistenceService.create(request.name(), request.price());
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{productId}")
			.buildAndExpand(product.getId())
			.toUri();

		return ResponseEntity.created(location).body(Day3ProductResponse.from(product));
	}

	@GetMapping("/{productId}")
	public Day3ProductResponse findById(
		@Positive(message = "상품 ID는 1 이상이어야 합니다.")
		@PathVariable Long productId
	) {
		return Day3ProductResponse.from(productPersistenceService.findById(productId));
	}

	@PostMapping("/{productId}/price/dirty-checking")
	public PersistenceObservationResponse changePriceWithDirtyChecking(
		@Positive(message = "상품 ID는 1 이상이어야 합니다.")
		@PathVariable Long productId,
		@Valid @RequestBody Day3ProductPriceChangeRequest request
	) {
		return productPersistenceService.changePriceWithDirtyChecking(productId, request.price());
	}

	@PostMapping("/{productId}/price/read-only")
	public PersistenceObservationResponse changePriceInsideReadOnlyTransaction(
		@Positive(message = "상품 ID는 1 이상이어야 합니다.")
		@PathVariable Long productId,
		@Valid @RequestBody Day3ProductPriceChangeRequest request
	) {
		return productPersistenceService.changePriceInsideReadOnlyTransaction(productId, request.price());
	}
}
