package Entity;

import javax.persistence.*;

@Entity
@Table(name = "orders", schema = "shop2")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

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

    public Order(User user, String orderList, double totalPrice) {
        this.user = user;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}