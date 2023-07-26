package dao.jdbc;


import dao.ShoppingCartDao;
import models.Product;
import java.util.ArrayList;
import java.util.List;
import utils.ConnectionPool;
import java.sql.*;


public class ShoppingCartJdbcDao implements ShoppingCartDao {


    public void addProductToCart(int userId, int productId) {


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO shop.shopping_cart (user_id, product_id) VALUES (?, ?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeProductFromCart(int userId, int productId) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM shop.shopping_cart " +
                            "WHERE user_id = ? AND product_id = ?");
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления из корзины", e);
        }
    }

    @Override
    public List<Product> getUserCartProducts(int userId) {

        List<Product> userProducts = new ArrayList<>();

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT p.* FROM shop.shopping_cart c " +
                    "JOIN product p ON c.product_id = p.id " +
                    "WHERE c.user_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product product = new Product(id, product_name, description, price, quantity);
                userProducts.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userProducts;
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