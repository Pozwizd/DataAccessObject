package dao.hibernate;

import Entity.Order;
import Entity.Product;
import Entity.ShoppingCart;
import Entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.OrderServiceOrm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceOrmTest {


    private static final Logger logger = LogManager.getLogger(OrderServiceOrmTest.class);

    OrderOrmDao orderOrmDao = new OrderOrmDao();

    OrderServiceOrm orderServiceOrm = new OrderServiceOrm();


    @BeforeEach
    public void createObjectsForTesting() {

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

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.persist(user2);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
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

        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(product);
            em.persist(product2);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }

        ShoppingCart shoppingCart = new ShoppingCart(1, 1, 4);
        ShoppingCart shoppingCart2 = new ShoppingCart(1, 2, 1);

        ShoppingCart shoppingCartUser2 = new ShoppingCart(2, 1, 4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(2, 2, 1);

        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.persist(shoppingCartUser2);
            em.persist(shoppingCartUser2_2);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    @AfterEach
    public void deleteObjectsForTesting() {
        try (Connection connection = ConnectionPool.getConnection()) {
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
    public void makeOrderTest() {
        Order order = orderServiceOrm.makeOrder(1);
        ;
        orderOrmDao.createOrder(order);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Order orderFromDB = em.find(Order.class, order.getOrderId());
            em.getTransaction().commit();
            em.close();
            assertEquals(orderFromDB.getOrderList(), order.getOrderList());
            assertEquals(orderFromDB.getTotalPrice(), order.getTotalPrice());
            assertEquals(orderFromDB.getUser_Id(), order.getUser_Id());
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }
}