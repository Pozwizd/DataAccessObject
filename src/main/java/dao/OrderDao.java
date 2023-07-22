package dao;

import models.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {

    void createOrder(Order order);

    List<Order> getUserOrders(int userId);

    List<Order> getAllOrders();

}