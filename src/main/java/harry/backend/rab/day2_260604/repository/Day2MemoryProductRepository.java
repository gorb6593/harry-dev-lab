package harry.backend.rab.day2_260604.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import harry.backend.rab.day2_260604.domain.Day2Product;

@Repository
public class Day2MemoryProductRepository implements Day2ProductRepository {

	private final Map<Long, Day2Product> products = Map.of(
		1L, new Day2Product(1L, "Spring Bean Guide", 30_000)
	);

	@Override
	public Optional<Day2Product> findById(Long productId) {
		return Optional.ofNullable(products.get(productId));
	}
}
