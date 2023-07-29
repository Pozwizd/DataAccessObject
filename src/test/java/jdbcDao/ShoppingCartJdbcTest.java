package jdbcDao;

import dao.jdbc.ShoppingCartJdbcDao;
import models.Product;
import models.ShoppingCart;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartJdbcTest {

    private static final Logger logger = LogManager.getLogger(ShoppingCartJdbcDao.class);

    ShoppingCartJdbcDao shoppingCartJdbcDao = new ShoppingCartJdbcDao();

    @Test
    public void addProductToCartTest() {
        /*
         * 1. Создать пользователя
         * 2. Создать продукт
         * 3. Протестировать метод
         */

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        Product product = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        ShoppingCart shoppingCart = new ShoppingCart(1,1,4);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();
            stmt.close();

            // Сам метод
            shoppingCartJdbcDao.addProductToCart(shoppingCart);

            stmt = connection.prepareStatement(
                    "SELECT * from shopping_cart where user_id = ? AND product_id = ? AND quantity = ?");
            stmt.setInt(1, 1);
            stmt.setInt(2, 1);
            stmt.setInt(3, 4);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_user = rs.getInt("user_id");
                int product_id = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                ShoppingCart actualShoppingCart = new ShoppingCart(id_user,product_id,quantity);
                assertEquals(shoppingCart.getUserId(), actualShoppingCart.getUserId());
                assertEquals(shoppingCart.getProductId(), actualShoppingCart.getProductId());
                assertEquals(shoppingCart.getQuantity(), actualShoppingCart.getQuantity());
            }
            rs.close();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();


            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
    }

    @Test
    public void removeProductFromCartTest() {

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        Product product = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        ShoppingCart shoppingCart = new ShoppingCart(1,1,4);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)");
            stmt.setInt(1, shoppingCart.getUserId());
            stmt.setInt(2, shoppingCart.getProductId());
            stmt.setInt(3, shoppingCart.getQuantity());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "SELECT * from shopping_cart where user_id = ? AND product_id = ? AND quantity = ?");
            stmt.setInt(1, 1);
            stmt.setInt(2, 1);
            stmt.setInt(3, 4);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id_user = rs.getInt("user_id");
                int product_id = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                ShoppingCart actualShoppingCart = new ShoppingCart(id_user,product_id,quantity);
                assertEquals(shoppingCart.getUserId(), actualShoppingCart.getUserId());
                assertEquals(shoppingCart.getProductId(), actualShoppingCart.getProductId());
                assertEquals(shoppingCart.getQuantity(), actualShoppingCart.getQuantity());
            }
            rs.close();
            stmt.close();
            //=====================================================================================================================
            shoppingCartJdbcDao.removeProductFromCart(shoppingCart);

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================

            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }

    }

    @Test
    public void getUserCartProductsTest() {

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        Product product = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        ShoppingCart shoppingCart = new ShoppingCart(1,1,4);
        ShoppingCart shoppingCart2 = new ShoppingCart(1,2,1);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();

            stmt.setString(1, product2.getProductName());
            stmt.setString(2, product2.getDescription());
            stmt.setDouble(3, product2.getPrice());
            stmt.setInt(4, product2.getQuantity());

            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)");
            stmt.setInt(1, shoppingCart.getUserId());
            stmt.setInt(2, shoppingCart.getProductId());
            stmt.setInt(3, shoppingCart.getQuantity());
            stmt.executeUpdate();

            stmt.setInt(1, shoppingCart2.getUserId());
            stmt.setInt(2, shoppingCart2.getProductId());
            stmt.setInt(3, shoppingCart2.getQuantity());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================

            List<ShoppingCart> userProducts = shoppingCartJdbcDao.getUserCartProducts(1);

            assertEquals(userProducts.remove(1).getProductId(), shoppingCart2.getProductId());
            assertEquals(userProducts.remove(0).getProductId(), shoppingCart.getProductId());


            //=====================================================================================================================

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================

            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }

    }

    @Test
    public void clearUserCartTest() {

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        Product product = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        ShoppingCart shoppingCart = new ShoppingCart(1,1,4);
        ShoppingCart shoppingCart2 = new ShoppingCart(1,2,1);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();

            stmt.setString(1, product2.getProductName());
            stmt.setString(2, product2.getDescription());
            stmt.setDouble(3, product2.getPrice());
            stmt.setInt(4, product2.getQuantity());

            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================
            stmt = connection.prepareStatement(
                    "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)");
            stmt.setInt(1, shoppingCart.getUserId());
            stmt.setInt(2, shoppingCart.getProductId());
            stmt.setInt(3, shoppingCart.getQuantity());
            stmt.executeUpdate();

            stmt.setInt(1, shoppingCart2.getUserId());
            stmt.setInt(2, shoppingCart2.getProductId());
            stmt.setInt(3, shoppingCart2.getQuantity());
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================

            List<ShoppingCart> userProducts = shoppingCartJdbcDao.getUserCartProducts(1);

            assertEquals(userProducts.remove(1).getProductId(), shoppingCart2.getProductId());
            assertEquals(userProducts.remove(0).getProductId(), shoppingCart.getProductId());


            //=====================================================================================================================

            shoppingCartJdbcDao.clearUserCart(1);

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();
            //=====================================================================================================================

            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }






    }


}
