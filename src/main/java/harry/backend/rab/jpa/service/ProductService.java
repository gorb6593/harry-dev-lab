package harry.backend.rab.jpa.service;

import harry.backend.rab.jpa.api.ProductCreateRequest;
import harry.backend.rab.jpa.api.ProductResponse;
import harry.backend.rab.jpa.domain.product.Product;
import harry.backend.rab.jpa.infra.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductCreateRequest request) {
        Product product = Product.create(request.name(), request.price());
        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + productId));
        return ProductResponse.from(product);
    }
}
