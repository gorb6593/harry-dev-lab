package harry.backend.rab.day2_260604.service;

import org.springframework.stereotype.Component;

@Component
public class FixedDiscountPolicy implements DiscountPolicy {

	private static final int DISCOUNT_AMOUNT = 1_000;

	@Override
	public int discount(int originalPrice) {
		if (originalPrice <= DISCOUNT_AMOUNT) {
			return 0;
		}
		return originalPrice - DISCOUNT_AMOUNT;
	}
}
