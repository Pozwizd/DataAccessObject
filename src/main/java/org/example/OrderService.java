package org.example;


import dao.OrderDao;
import dao.ShoppingCartDao;
import models.Order;
import models.Product;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private ShoppingCartDao cartDao;
    private OrderDao orderDao;

    public void makeOrder(int userId) {

        List<Product> products = cartDao.getUserCartProducts(userId);

        String orderList = getProductNames(products);

        BigDecimal totalPrice = BigDecimal.valueOf(calculateTotalSum(products));

        Order order = new Order(userId, orderList, totalPrice);

        orderDao.createOrder(order);

        cartDao.clearUserCart(userId);

    }

    private double calculateTotalSum(List<Product> products) {

        double total = 0;

        for(Product product : products) {
            total += product.getPrice();
        }

        return total;
    }

    private String getProductNames(List<Product> products) {

        StringBuilder builder = new StringBuilder();

        for(Product product : products) {
            builder.append(product.getProductName());
            builder.append(" * ");
            builder.append(product.getQuantity());
            builder.append(", ");
        }

        if(builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }
}