package models;


public class Order {

    private int orderId;

    private int user_Id;

    private User user;

    private String orderList;

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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
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