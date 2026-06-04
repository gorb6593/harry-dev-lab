package harry.backend.rab.day2_260604.repository;

import java.util.Optional;

import harry.backend.rab.day2_260604.domain.Day2Product;

public interface Day2ProductRepository {

	Optional<Day2Product> findById(Long productId);
}
