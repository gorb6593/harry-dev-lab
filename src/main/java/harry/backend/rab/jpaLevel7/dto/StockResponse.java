package harry.backend.rab.jpaLevel7.dto;

import harry.backend.rab.jpaLevel7.entity.StockItem;

public record StockResponse(
        Long id,
        String name,
        int quantity
) {
    public static StockResponse from(StockItem stockItem) {
        return new StockResponse(stockItem.getId(), stockItem.getName(), stockItem.getQuantity());
    }
}
