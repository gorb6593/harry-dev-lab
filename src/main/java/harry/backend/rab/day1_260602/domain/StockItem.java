package harry.backend.rab.day1_260602.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StockItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int quantity;

	protected StockItem() {
	}

	public StockItem(String name, int quantity) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("상품명은 비어 있을 수 없습니다.");
		}
		if (quantity < 0) {
			throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
		}
		this.name = name;
		this.quantity = quantity;
	}

	public void decrease(int requestedQuantity) {
		if (requestedQuantity <= 0) {
			throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
		}
		if (quantity < requestedQuantity) {
			throw new IllegalStateException("재고가 부족합니다.");
		}
		quantity -= requestedQuantity;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}
}
