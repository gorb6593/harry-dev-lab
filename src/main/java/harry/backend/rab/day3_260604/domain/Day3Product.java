package harry.backend.rab.day3_260604.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Day3Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int price;

	protected Day3Product() {
	}

	public Day3Product(String name, int price) {
		validateName(name);
		validatePrice(price);
		this.name = name;
		this.price = price;
	}

	public void changePrice(int price) {
		validatePrice(price);
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("상품명은 비어 있을 수 없습니다.");
		}
	}

	private void validatePrice(int price) {
		if (price <= 0) {
			throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
		}
	}
}
