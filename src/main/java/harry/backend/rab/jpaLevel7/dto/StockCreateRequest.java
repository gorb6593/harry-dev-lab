package harry.backend.rab.jpaLevel7.dto;

public record StockCreateRequest(
        String name,
        int quantity
) {
}
