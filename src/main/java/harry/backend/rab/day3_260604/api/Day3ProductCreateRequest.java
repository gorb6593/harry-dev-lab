package harry.backend.rab.day3_260604.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record Day3ProductCreateRequest(
	@NotBlank(message = "상품명은 비어 있을 수 없습니다.")
	String name,

	@Positive(message = "가격은 0보다 커야 합니다.")
	int price
) {
}
