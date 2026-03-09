package harry.backend.rab.jpa.api;

public record ProductPriceChangeResponse(
        Long productId,
        int beforePrice,
        int afterPrice,
        String message
) {
}
