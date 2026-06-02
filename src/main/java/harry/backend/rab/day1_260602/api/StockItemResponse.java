package harry.backend.rab.day1_260602.api;

import harry.backend.rab.day1_260602.domain.StockItem;

public record StockItemResponse(
	Long id,
	String name,
	int quantity
) {

	public static StockItemResponse from(StockItem stockItem) {
		return new StockItemResponse(stockItem.getId(), stockItem.getName(), stockItem.getQuantity());
	}
}
