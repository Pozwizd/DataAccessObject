package jdbcDao;

import dao.jdbc.OrderJdbcDao;
import models.Order;
import models.Product;
import models.ShoppingCart;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class orderServiceTest {

    private static final Logger logger = LogManager.getLogger(orderServiceTest.class);

    OrderJdbcDao orderJdbcDao = new OrderJdbcDao();
    OrderService orderService = new OrderService();


    @BeforeEach
    public void createObjectsForTesting(){

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        User user2 = new User(2,
                "user2",
                "userPassword2",
                "user2@example.com",
                "654321");

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone_number());
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user2.getId());
            stmt.setString(2, user2.getUsername());
            stmt.setString(3, user2.getPassword());
            stmt.setString(4, user2.getEmail());
            stmt.setString(5, user2.getPhone_number());
            stmt.executeUpdate();
            stmt.close();

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

            ShoppingCart shoppingCart = new ShoppingCart(1,1,4);
            ShoppingCart shoppingCart2 = new ShoppingCart(1,2,1);

            ShoppingCart shoppingCartUser2 = new ShoppingCart(2,1,4);
            ShoppingCart shoppingCartUser2_2 = new ShoppingCart(2,2,1);

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

            stmt.setInt(1, shoppingCartUser2.getUserId());
            stmt.setInt(2, shoppingCartUser2.getProductId());
            stmt.setInt(3, shoppingCartUser2.getQuantity());
            stmt.executeUpdate();

            stmt.setInt(1, shoppingCartUser2_2.getUserId());
            stmt.setInt(2, shoppingCartUser2_2.getProductId());
            stmt.setInt(3, shoppingCartUser2_2.getQuantity());
            stmt.executeUpdate();
            stmt.close();
            logger.info("User and Product for the test has been successfully added");
        } catch (SQLException e) {
            logger.info("Error adding user and product for test");
        }

    }

    @AfterEach
    public void deleteObjectsForTesting(){
        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

            logger.info("Deleting the User and the product after a test");
        } catch (SQLException e) {
            logger.info("Error deleting user and product after testing");

        }
    }

    @Test
    public void makeOrderTest(){
        Order order = orderService.makeOrder(1);;
        orderJdbcDao.createOrder(order);
        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders " +
                    "WHERE user_id = 1");
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                int order_id = rs.getInt("order_id");
                int user_id = rs.getInt("user_id");
                String order_list = rs.getString("order_list");
                double total_price = rs.getInt("total_price");
                order = new Order(order_id, user_id, order_list, total_price);
                logger.info(order.getOrderList());
            }
        } catch (SQLException e) {
            logger.info("Error deleting user and product after testing");

        }

    }

}
