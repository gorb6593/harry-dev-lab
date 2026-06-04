package harry.backend.rab.day2_260604.service;

public record PriceCalculationResponse(
	String sampleProductName,
	int originalPrice,
	int discountedPrice,
	String discountPolicy
) {
}
