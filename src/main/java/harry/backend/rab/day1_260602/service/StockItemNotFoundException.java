package harry.backend.rab.day1_260602.service;

public class StockItemNotFoundException extends RuntimeException {

	public StockItemNotFoundException(Long stockItemId) {
		super("재고 상품을 찾을 수 없습니다. id=" + stockItemId);
	}
}
