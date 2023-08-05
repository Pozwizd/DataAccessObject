package OrmDao;


import dao.hibernate.OrderOrmDao;
import models.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import utils.HibernateUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderHibernateTest {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private OrderOrmDao orderOrmDao = new OrderOrmDao(sessionFactory);



    @Test
    public void createOrderTest() {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        orderOrmDao.createOrder(order);
        transaction.commit();
        Order actualOrder = session.get(Order.class, order.getOrderId());
        session.close();
        assertEquals(order.getOrderId(), actualOrder.getOrderId());
        assertEquals(order.getUserId(), actualOrder.getUserId());
        assertEquals(order.getOrderList(), actualOrder.getOrderList());
        assertEquals(order.getTotalPrice(), actualOrder.getTotalPrice());

    }

    @Test
    public void getUserOrdersTest() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        session.save(order);
        transaction.commit();
        List<Order> orders = orderOrmDao.getUserOrders(1);
        session.close();
        assertEquals(orders.get(0).getOrderId(), order.getOrderId());
        assertEquals(orders.get(0).getUserId(), order.getUserId());
        assertEquals(orders.get(0).getOrderList(), order.getOrderList());
        assertEquals(orders.get(0).getTotalPrice(), order.getTotalPrice());
    }

    @Test
    public void getAllOrdersTest() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);

        Order order2 = new Order(2,
                2,
                "AMD Ryzen 5 5950X * 5, Intel Core i9-11900K * 2",
                5000);

        Order order3 = new Order(3,
                1,
                "AMD Ryzen 7 5950X * 1, Intel Core i9-11900K * 1",
                6000);

        session.save(order);
        session.save(order2);
        session.save(order3);
        transaction.commit();
        List<Order> orders = orderOrmDao.getAllOrders();
        List<Order> expectedOrders = List.of(order, order2, order3);
        session.close();
        for (int i = 0; i < orders.size(); i++) {
            assertEquals(orders.get(i).getOrderId(), expectedOrders.get(i).getOrderId());
            assertEquals(orders.get(i).getUserId(), expectedOrders.get(i).getUserId());
            assertEquals(orders.get(i).getOrderList(), expectedOrders.get(i).getOrderList());
            assertEquals(orders.get(i).getTotalPrice(), expectedOrders.get(i).getTotalPrice());
        }
    }
}