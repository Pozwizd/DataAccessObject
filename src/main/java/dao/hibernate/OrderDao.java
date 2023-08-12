package dao.hibernate;

import Entity.Order;

import java.util.List;

public interface OrderDao {

    void createOrder(Order order);

    List<Order> getUserOrders(long userId);

    List<Order> getAllOrders();

}