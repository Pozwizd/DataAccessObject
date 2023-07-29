package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class ShoppingCart {

    @Column(name="user_id")
    private int userId;

    @Column(name="product_id")
    private int productId;

    @Column
    private int quantity;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="user_id", referencedColumnName="id"),
            @JoinColumn(name="product_id", referencedColumnName="id")
    })
    private User user;


    public ShoppingCart(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public ShoppingCart() {

    }

}