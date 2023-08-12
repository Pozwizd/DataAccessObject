package Entity;

import lombok.Getter;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_list")
    private String orderList;

    @Column(name = "total_price")
    private double totalPrice;

    public Order() {
    }

    public Order(int id, User user, String orderList, double totalPrice) {
        this.id = id;
        this.user = user;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}