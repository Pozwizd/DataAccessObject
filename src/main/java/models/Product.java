package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String productName;

    @Column
    private String description;

    @Column
    private double price;

    @Column
    private int quantity;

    @OneToMany(mappedBy="product")
    private List<ShoppingCart> shoppingCarts;

    public Product() {
    }

    public Product(int id, String productName, String description, double price, int quantity) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String productName, String description, double price, int quantity) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

}