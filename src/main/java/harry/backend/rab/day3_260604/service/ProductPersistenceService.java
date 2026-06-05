package harry.backend.rab.day3_260604.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import harry.backend.rab.day3_260604.domain.Day3Product;
import harry.backend.rab.day3_260604.repository.Day3ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPersistenceService {

	private final Day3ProductRepository productRepository;
	private final EntityManager entityManager;

	@Transactional
	public Day3Product create(String name, int price) {
		return productRepository.save(new Day3Product(name, price));
	}

	@Transactional(readOnly = true)
	public Day3Product findById(Long productId) {
		return getProduct(productId);
	}

	@Transactional
	public PersistenceObservationResponse changePriceWithDirtyChecking(Long productId, int newPrice) {
		Day3Product product = getProduct(productId);
		boolean containsAfterFind = entityManager.contains(product);
		int priceBeforeChange = product.getPrice();

		product.changePrice(newPrice);
		boolean containsAfterChange = entityManager.contains(product);
		int priceInsideTransaction = product.getPrice();

		entityManager.flush();

		int priceAfterTransaction = findPriceInNewPersistenceContext(productId);
		return new PersistenceObservationResponse(
			productId,
			"writeTransaction",
			containsAfterFind,
			containsAfterChange,
			priceBeforeChange,
			newPrice,
			priceInsideTransaction,
			priceAfterTransaction,
			"영속 상태 엔티티의 값을 바꾸면 save() 없이도 flush 시점에 변경 감지로 update SQL이 실행된다."
		);
	}

	@Transactional(readOnly = true)
	public PersistenceObservationResponse changePriceInsideReadOnlyTransaction(Long productId, int newPrice) {
		Day3Product product = getProduct(productId);
		boolean containsAfterFind = entityManager.contains(product);
		int priceBeforeChange = product.getPrice();

		product.changePrice(newPrice);
		boolean containsAfterChange = entityManager.contains(product);
		int priceInsideTransaction = product.getPrice();

		int priceAfterTransaction = findPriceInNewPersistenceContext(productId);
		return new PersistenceObservationResponse(
			productId,
			"readOnlyTransaction",
			containsAfterFind,
			containsAfterChange,
			priceBeforeChange,
			newPrice,
			priceInsideTransaction,
			priceAfterTransaction,
			"readOnly 트랜잭션 안에서도 객체 필드는 바뀔 수 있지만, flush 모드와 dirty checking 동작은 쓰기 트랜잭션과 다르게 관찰될 수 있다."
		);
	}

	private Day3Product getProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new Day3ProductNotFoundException(productId));
	}

	private int findPriceInNewPersistenceContext(Long productId) {
		entityManager.flush();
		entityManager.clear();
		return getProduct(productId).getPrice();
	}
}
