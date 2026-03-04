package harry.backend.rab.jpa.domain.product;

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

    private String name;

    private int price;

    protected Product() {
    }

    private Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, int price) {
        return new Product(name, price);
    }

    public void changePrice(int newPrice) {
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
}
