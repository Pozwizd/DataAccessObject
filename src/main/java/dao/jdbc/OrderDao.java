package dao.jdbc;

import models.Order;

import java.util.List;

public interface OrderDao {

    void createOrder(Order order);

    List<Order> getUserOrders(int userId);

    List<Order> getAllOrders();

}