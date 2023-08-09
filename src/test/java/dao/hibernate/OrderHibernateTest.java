package dao.hibernate;


import models.Order;
import org.junit.jupiter.api.Test;

public class OrderHibernateTest {



    @Test
    public void createOrderTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);
    }

    @Test
    public void getUserOrdersTest() {
        Order order = new Order(1,
                1,
                "AMD Ryzen 9 5950X * 4, Intel Core i9-11900K * 1",
                4695.00);
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
                1,
                "AMD Ryzen 7 5950X * 1, Intel Core i9-11900K * 1",
                6000);
    }
}