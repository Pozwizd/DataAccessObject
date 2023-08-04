package OrmDao;

import dao.hibernate.OrderOrmDao;
import models.Order;
import models.Product;
import models.ShoppingCart;
import models.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderHibernateTest {
    private static final Logger logger = LogManager.getLogger(OrderHibernateTest.class);

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    private OrderOrmDao orderOrmDao;


    private void createObjectsForTesting() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

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

        entityManager.persist(user);
        entityManager.persist(user2);

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

        entityManager.persist(product);
        entityManager.persist(product2);

        ShoppingCart shoppingCart = new ShoppingCart(1, 1, 4);
        ShoppingCart shoppingCart2 = new ShoppingCart(1, 2, 1);

        ShoppingCart shoppingCartUser2 = new ShoppingCart(2, 1, 4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(2, 2, 1);

        entityManager.persist(shoppingCart);
        entityManager.persist(shoppingCart2);
        entityManager.persist(shoppingCartUser2);
        entityManager.persist(shoppingCartUser2_2);

        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        entityManager.persist(order);

        transaction.commit();
        logger.info("User, Product, ShoppingCart and Order for the test have been successfully added");
    }

    private void deleteObjectsForTesting() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.createQuery("DELETE FROM Order").executeUpdate();
        entityManager.createQuery("DELETE FROM ShoppingCart").executeUpdate();
        entityManager.createQuery("DELETE FROM Product").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();

        transaction.commit();
        logger.info("Deleting the User, Product, ShoppingCart and Order after a test");
    }

    @Test
    public void createOrderTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        orderOrmDao.createOrder(order);

        Order actualOrder = entityManager.find(Order.class, order.getOrderId());

        assertEquals(order.getOrderId(), actualOrder.getOrderId());
        assertEquals(order.getUserId(), actualOrder.getUserId());
        assertEquals(order.getOrderList(), actualOrder.getOrderList());
        assertEquals(order.getTotalPrice(), actualOrder.getTotalPrice());

        logger.info("Order has successfully been created");
    }

    @Test
    public void getUserOrdersTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(order);

        transaction.commit();

        List<Order> orders = orderOrmDao.getUserOrders(1);

        assertEquals(1, orders.size());

        Order actualOrder = orders.get(0);

        assertEquals(order.getOrderId(), actualOrder.getOrderId());
        assertEquals(order.getUserId(), actualOrder.getUserId());
        assertEquals(order.getOrderList(), actualOrder.getOrderList());
        assertEquals(order.getTotalPrice(), actualOrder.getTotalPrice());

        logger.info("Orders for the user have been retrieved");
    }

    @Test
    public void getAllOrdersTest() {
        Order order1 = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        Order order2 = new Order(2,
                2,
                "AMD Ryzen 9 5950X * 3, Intel Core i9-11900K * 2",
                6693.00);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(order1);
        entityManager.persist(order2);

        transaction.commit();

        List<Order> orders = orderOrmDao.getAllOrders();

        assertEquals(3, orders.size()); // Including the initial order created during setup

        logger.info("All orders have been retrieved");
    }
}