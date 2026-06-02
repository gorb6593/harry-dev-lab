package harry.backend.rab.day1_260602.api;

import jakarta.validation.constraints.Positive;

public record StockDecreaseRequest(
	@Positive(message = "차감 수량은 1 이상이어야 합니다.")
	int quantity
) {
}
