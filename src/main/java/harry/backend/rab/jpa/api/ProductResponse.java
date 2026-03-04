package harry.backend.rab.jpa.api;

import harry.backend.rab.jpa.domain.product.Product;

public record ProductResponse(
        Long id,
        String name,
        int price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
