package harry.backend.rab.day1_260602.domain;

public class InsufficientStockException extends RuntimeException {

	public InsufficientStockException() {
		super("재고가 부족합니다.");
	}
}
