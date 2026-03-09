package harry.backend.rab.jpa.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    protected Product() {
    }

    private Product(String name, int price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, int price) {
        return new Product(name, price);
    }

    public void changePrice(int newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
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

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank.");
        }
    }

    private static void validatePrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Product price must be zero or positive.");
        }
    }
}
