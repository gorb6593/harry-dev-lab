package harry.backend.rab.jpaLevel7.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "jpa_level7_stock_items")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Version
    private Long version;

    protected StockItem() {
    }

    private StockItem(String name, int quantity) {
        validateName(name);
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
    }

    public static StockItem create(String name, int quantity) {
        return new StockItem(name, quantity);
    }

    public void decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Decrease amount must be positive.");
        }
        if (this.quantity - amount < 0) {
            throw new IllegalArgumentException("Quantity must not become negative.");
        }
        this.quantity -= amount;
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

    public Long getVersion() {
        return version;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Stock item name must not be blank.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be zero or positive.");
        }
    }
}
