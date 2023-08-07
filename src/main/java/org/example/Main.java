package org.example;

import dao.jdbc.OrderJdbcDao;
import dao.jdbc.ProductJdbcDao;
import dao.jdbc.ShoppingCartJdbcDao;
import dao.jdbc.UserDaoJdbc;
import models.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
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

        ShoppingCart shoppingCartUser2 = new ShoppingCart(2,1,4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(2,2,1);

        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.createUser(user);
        userDaoJdbc.createUser(user2);
        ProductJdbcDao productJdbcDao = new ProductJdbcDao();
        productJdbcDao.createProduct(product);
        productJdbcDao.createProduct(product2);
        ShoppingCartJdbcDao shoppingCartJdbcDao = new ShoppingCartJdbcDao();
        shoppingCartJdbcDao.addProductToCart(shoppingCart);
        shoppingCartJdbcDao.addProductToCart(shoppingCart2);
        shoppingCartJdbcDao.addProductToCart(shoppingCartUser2);
        shoppingCartJdbcDao.addProductToCart(shoppingCartUser2_2);
        OrderJdbcDao orderJdbcDao = new OrderJdbcDao();
        Order order = new OrderService().makeOrder(1);
        orderJdbcDao.createOrder(order);
        }
    }
