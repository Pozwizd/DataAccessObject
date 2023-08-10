package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity(name = "orders")
@Table(schema = "shop", name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private int orderId;

    private int userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private User user;

    @Column(name="order_list")
    private String orderList;

    @Column(name="total_price")
    private double totalPrice;

    public Order() {}

    public Order(int orderId, int userId, String orderList, double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

    public Order(int userId, String orderList, double totalPrice) {
        this.userId = userId;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

}