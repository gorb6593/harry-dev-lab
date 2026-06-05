package harry.backend.rab.day3_260604.api;

import harry.backend.rab.day3_260604.domain.Day3Product;

public record Day3ProductResponse(
	Long id,
	String name,
	int price
) {

	public static Day3ProductResponse from(Day3Product product) {
		return new Day3ProductResponse(product.getId(), product.getName(), product.getPrice());
	}
}
