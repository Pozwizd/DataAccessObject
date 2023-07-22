package dao.jdbc;

import dao.OrderDao;
import models.Order;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utils.DBConnection.closeConnection;

public class OrderJdbcDAO implements OrderDao {
    DBConnection dbConnection;

    public void createOrder(Order order) {

        Connection conn = dbConnection.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO shop.orders (user_id, order_list, total_price) VALUES (?, ?, ?)");

            stmt.setInt(1, order.getUserId());
            stmt.setString(2, order.getOrderList());
            stmt.setBigDecimal(3, order.getTotalPrice());


            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        } finally {
            closeConnection(conn);
        }

    }

    @Override
    public List<Order> getUserOrders(int userId) {

        List<Order> userOrders = new ArrayList<>();
        Connection conn = dbConnection.getConnection();
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                userId = rs.getInt("userId");
                String orderList = rs.getString("orderList");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                String products = rs.getString("products");

                Order order = new Order(orderId, userId, orderList, totalPrice);
                userOrders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(conn);
        }

        return userOrders;

    }

    @Override
    public List<Order> getAllOrders() {

        List<Order> allOrders = new ArrayList<>();
        Connection conn = dbConnection.getConnection();
        try {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                int userId = rs.getInt("userId");
                String orderList = rs.getString("orderList");
                BigDecimal totalPrice = rs.getBigDecimal("total_price");
                String products = rs.getString("products");

                Order order = new Order(orderId, userId, orderList, totalPrice);
                allOrders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(conn);
        }

        return allOrders;
    }
}