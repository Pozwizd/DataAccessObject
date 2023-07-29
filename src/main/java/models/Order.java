package models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Order {

    @Id
    @GeneratedValue
    private int orderId;

    @Column(name="user_id")
    private int userId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column
    private String orderList;
    @Column
    private BigDecimal totalPrice;

    public Order() {}

    public Order(int orderId, int userId, String orderList, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

    public Order(int userId, String orderList, BigDecimal totalPrice) {
        this.userId = userId;

        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

}