package harry.backend.rab.day1_260602.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record StockCreateRequest(
	@NotBlank(message = "상품명은 비어 있을 수 없습니다.")
	String name,

	@PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
	int quantity
) {
}
