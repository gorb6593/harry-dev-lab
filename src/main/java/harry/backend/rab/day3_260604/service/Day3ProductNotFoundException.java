package harry.backend.rab.day3_260604.service;

public class Day3ProductNotFoundException extends RuntimeException {

	public Day3ProductNotFoundException(Long productId) {
		super("Day3 상품을 찾을 수 없습니다. id=" + productId);
	}
}
