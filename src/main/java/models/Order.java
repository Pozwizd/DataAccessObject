package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private int orderId;

    private int user_Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="order_list")
    private String orderList;

    @Column(name="total_price")
    private double totalPrice;

    public Order() {}

    public Order(int orderId, int user_Id, String orderList, double totalPrice) {
        this.orderId = orderId;
        this.user_Id = user_Id;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

    public Order(int user_Id, String orderList, double totalPrice) {
        this.user_Id = user_Id;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

}