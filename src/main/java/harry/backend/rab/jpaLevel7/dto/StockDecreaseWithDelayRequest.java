package harry.backend.rab.jpaLevel7.dto;

public record StockDecreaseWithDelayRequest(
        int quantity,
        long delayMillis
) {
}
