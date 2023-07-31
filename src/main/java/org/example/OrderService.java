package org.example;


import dao.OrderDao;
import dao.ShoppingCartDao;
import models.Order;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private ShoppingCartDao cartDao;
    private OrderDao orderDao;

    private Order order;

    List<String> products;

    public Order makeOrder(int userId) {

        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT sc.user_id, p.product_name, p.price, sc.quantity" +
                            " FROM shopping_cart sc " +
                            "JOIN product p ON sc.product_id = p.id " +
                            "WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id_user = rs.getInt("user_id");
                String product_name = rs.getString("product_name");
                products.add(product_name);
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                int totalPrice = 0;
                totalPrice += (price * quantity);



                order = new Order(id_user,getProductNames(products),totalPrice);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;

    }

    private String getProductNames(List<String> products) {

        StringBuilder builder = new StringBuilder();

        for(String product : products) {
            builder.append(product);
            builder.append(" * ");
            builder.append(product);
            builder.append(", ");
        }

        if(builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }
}