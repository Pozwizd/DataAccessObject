package dao.jdbc;

import models.Order;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderJdbcDao implements OrderDao {

    public void createOrder(Order order) {


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO orders (user_id, order_list, total_price) VALUES (?, ?, ?)");

            stmt.setInt(1, order.getUser_Id());
            stmt.setString(2, order.getOrderList());
            stmt.setDouble(3, order.getTotalPrice());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }

    }

    @Override
    public List<Order> getUserOrders(int userId) {

        List<Order> userOrders = new ArrayList<>();

        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                userId = rs.getInt("user_id");
                String orderList = rs.getString("order_list");
                int totalPrice = rs.getInt("total_price");

                Order order = new Order(orderId, userId, orderList, totalPrice);
                userOrders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userOrders;

    }

    @Override
    public List<Order> getAllOrders() {

        List<Order> allOrders = new ArrayList<>();
        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String orderList = rs.getString("order_list");
                int totalPrice = rs.getInt("total_price");

                Order order = new Order(orderId, userId, orderList, totalPrice);
                allOrders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allOrders;
    }
}