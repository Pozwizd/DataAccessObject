package org.example;


import dao.OrderDao;
import dao.ShoppingCartDao;
import models.Order;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private ShoppingCartDao cartDao;
    private OrderDao orderDao;

    private Order order;



    public Order makeOrder(int userId) {
        List<String> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT sc.user_id, p.product_name, p.price, sc.quantity" +
                            " FROM shopping_cart sc " +
                            "JOIN product p ON sc.product_id = p.id " +
                            "WHERE user_id = ?");
            stmt.setInt(1, userId);
            int totalPrice = 0;
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int id_user = rs.getInt("user_id");
                String product_name = rs.getString("product_name");
                products.add(product_name);
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                quantities.add(quantity);
                totalPrice += (price * quantity);

                order = new Order(id_user,getProductNames(products, quantities),totalPrice);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;

    }

    private String getProductNames(List<String> products, List<Integer> quantity) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < products.size(); i++){
            builder.append(products.get(i));
            builder.append(" * ");
            builder.append(quantity.get(i));
            builder.append(", ");
        }

        if(builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }
}