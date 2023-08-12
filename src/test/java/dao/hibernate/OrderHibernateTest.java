package dao.hibernate;


import models.Order;
import models.Product;
import models.ShoppingCart;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderHibernateTest {

    private static final Logger logger = LogManager.getLogger(OrderHibernateTest.class);

    OrderOrmDao orderOrmDao = new OrderOrmDao();


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
            em.merge(user);
            em.merge(user2);
            em.persist(user);
            em.persist(user2);
            em.getTransaction().commit();
            logger.info("User created for testing");
        } catch (Exception e) {
            logger.info("Error creating user for testing");
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
            em.merge(product);
            em.merge(product2);
            em.persist(product);
            em.persist(product2);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.info("Error creating product for testing");
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
            em.merge(shoppingCart);
            em.merge(shoppingCart2);
            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.persist(shoppingCartUser2);
            em.persist(shoppingCartUser2_2);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.info("Error creating shopping cart for testing");
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

            stmt = connection.prepareStatement("DELETE FROM orders");
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

            stmt = connection.prepareStatement("ALTER TABLE orders AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();
            logger.info("Deleting the User and the product after a test");
        } catch (SQLException e) {
            logger.info("Error deleting user and product after testing");
        }
    }

    @Test
    public void createOrderTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        orderOrmDao.createOrder(order);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            Order orderFromDb = em.find(Order.class, 1);
            assertEquals(orderFromDb.getOrderId(), order.getOrderId());
            assertEquals(orderFromDb.getUser_Id(), order.getUser_Id());
            assertEquals(orderFromDb.getOrderList(), order.getOrderList());
            assertEquals(orderFromDb.getTotalPrice(), order.getTotalPrice());
            logger.info("Order created successfully");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void getUserOrdersTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(order);
            em.persist(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.info("Error creating order for testing");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        List<Order> orders = orderOrmDao.getUserOrders(1);
        assertEquals(orders.size(), 1);
        assertEquals(orders.get(0).getOrderId(), order.getOrderId());
        assertEquals(orders.get(0).getUser_Id(), order.getUser_Id());
        assertEquals(orders.get(0).getOrderList(), order.getOrderList());
        assertEquals(orders.get(0).getTotalPrice(), order.getTotalPrice());
        logger.info("Order retrieved successfully");
    }

    @Test
    public void getAllOrdersTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        Order order2 = new Order(2,
                2,
                "AMD Ryzen 5 5950X * 5, Intel Core i9-11900K * 2",
                5000);

        Order order3 = new Order(3,
                3,
                "AMD Ryzen 7 5950X * 1, Intel Core i9-11900K * 1",
                6000);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(order);
            em.merge(order2);
            em.merge(order3);
            em.persist(order);
            em.persist(order2);
            em.persist(order3);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.info("Error creating order for testing");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        List<Order> orders = orderOrmDao.getAllOrders();
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getOrderId(), order.getOrderId());
        assertEquals(orders.get(0).getUser_Id(), order.getUser_Id());
        assertEquals(orders.get(0).getOrderList(), order.getOrderList());
        assertEquals(orders.get(0).getTotalPrice(), order.getTotalPrice());
        assertEquals(orders.get(1).getOrderId(), order2.getOrderId());
        assertEquals(orders.get(1).getUser_Id(), order2.getUser_Id());
        assertEquals(orders.get(1).getOrderId(), order2.getOrderId());
        assertEquals(orders.get(1).getTotalPrice(), order2.getTotalPrice());
        assertEquals(orders.get(2).getOrderId(), order2.getOrderId());
        assertEquals(orders.get(2).getUser_Id(), order2.getUser_Id());
        assertEquals(orders.get(2).getOrderId(), order2.getOrderId());
        assertEquals(orders.get(2).getTotalPrice(), order2.getTotalPrice());
        logger.info("All orders retrieved successfully");
    }
}