package harry.backend.rab.jpa.service;

import harry.backend.rab.jpa.api.ProductChangePriceRequest;
import harry.backend.rab.jpa.api.ProductCreateRequest;
import harry.backend.rab.jpa.api.ProductPersistenceContextResponse;
import harry.backend.rab.jpa.api.ProductPriceChangeResponse;
import harry.backend.rab.jpa.api.ProductResponse;
import harry.backend.rab.jpa.domain.product.Product;
import harry.backend.rab.jpa.infra.persistence.ProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
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

    @Transactional(readOnly = true)
    public ProductPersistenceContextResponse inspectPersistenceContext(Long productId) {
        Product firstLoad = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + productId));
        Product secondLoad = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + productId));

        return new ProductPersistenceContextResponse(
                productId,
                firstLoad == secondLoad,
                System.identityHashCode(firstLoad),
                System.identityHashCode(secondLoad),
                firstLoad.getName(),
                firstLoad.getPrice()
        );
    }

    public ProductPriceChangeResponse changePrice(Long productId, ProductChangePriceRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found. id=" + productId));
        int beforePrice = product.getPrice();

        product.changePrice(request.price());
        entityManager.flush();

        return new ProductPriceChangeResponse(
                productId,
                beforePrice,
                product.getPrice(),
                "Dirty checking updated the managed entity and flush forced SQL emission."
        );
    }
}
