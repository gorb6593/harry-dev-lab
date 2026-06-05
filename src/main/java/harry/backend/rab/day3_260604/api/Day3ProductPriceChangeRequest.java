package harry.backend.rab.day3_260604.api;

import jakarta.validation.constraints.Positive;

public record Day3ProductPriceChangeRequest(
	@Positive(message = "가격은 0보다 커야 합니다.")
	int price
) {
}
