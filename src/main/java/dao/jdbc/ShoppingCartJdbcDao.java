package dao.jdbc;


import dao.ShoppingCartDao;
import models.Product;
import models.ShoppingCart;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCartJdbcDao implements ShoppingCartDao {

    @Override
    public void addProductToCart(ShoppingCart shoppingCart) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)");
            stmt.setInt(1, shoppingCart.getUserId());
            stmt.setInt(2, shoppingCart.getProductId());
            stmt.setInt(3, shoppingCart.getQuantity());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM shop.shopping_cart " +
                            "WHERE user_id = ? AND product_id = ?");
            stmt.setInt(1, shoppingCart.getUserId());
            stmt.setInt(2, shoppingCart.getProductId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления из корзины", e);
        }
    }

    @Override
    public List<ShoppingCart> getUserCartProducts(int userId) {

        List<ShoppingCart> shoppingCarts = new ArrayList<>();



        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM shopping_cart WHERE user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                int product_id = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                ShoppingCart shoppingCart = new ShoppingCart(user_id, product_id, quantity);
                shoppingCarts.add(shoppingCart);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shoppingCarts;
    }

    @Override
    public void clearUserCart(int userId) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM shop.shopping_cart WHERE user_id = ?");
            stmt.setInt(1, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки корзины", e);
        }

    }
}