package harry.backend.rab.jpa.infra.persistence;

import harry.backend.rab.jpa.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
