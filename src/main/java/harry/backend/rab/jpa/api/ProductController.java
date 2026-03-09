package harry.backend.rab.jpa.api;

import harry.backend.rab.jpa.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jpa/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody ProductCreateRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{productId}")
    public ProductResponse findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @GetMapping("/{productId}/persistence-context")
    public ProductPersistenceContextResponse inspectPersistenceContext(@PathVariable Long productId) {
        return productService.inspectPersistenceContext(productId);
    }

    @PatchMapping("/{productId}/price")
    public ProductPriceChangeResponse changePrice(
            @PathVariable Long productId,
            @RequestBody ProductChangePriceRequest request
    ) {
        return productService.changePrice(productId, request);
    }
}
