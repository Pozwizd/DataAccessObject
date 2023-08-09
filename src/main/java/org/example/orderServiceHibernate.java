package org.example;

import dao.hibernate.OrderOrmDao;
import dao.hibernate.ShoppingCartOrmDao;
import models.*;
import org.hibernate.SessionFactory;
import utils.EntityManagerUtil;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class orderServiceHibernate {

    private SessionFactory sessionFactory;

    private ShoppingCartOrmDao cartDao;
    private OrderOrmDao orderDao;


    public Order makeOrder(int userId) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createQuery( "SELECT p.productName, s.quantity " +
                "FROM Product p JOIN p.shoppingCarts s " +
                "WHERE s.user.id = :userId");

        query.setParameter("userId", userId);

        List<Object[]> results = query.getResultList();

        List<String> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        for(Object[] row : results) {
            products.add((String)row[0]);
            quantities.add((Integer)row[1]);
        }

        String productNames = getProductNames(products, quantities);

        int totalPrice = calculateTotalPrice(userId);

        Order order = new Order(userId, productNames, totalPrice);
        Query queryDelete = em.createQuery("DELETE FROM shopping_cart sc WHERE sc.user.id = :userId");
        queryDelete.setParameter("userId", userId);
        queryDelete.executeUpdate();
        em.getTransaction().commit();
        em.close();
        return order;
    }

    private int calculateTotalPrice(int userId) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createQuery( "SELECT SUM(p.price * s.quantity) " +
                "FROM Product p JOIN p.shoppingCarts s " +
                "WHERE s.user.id = :userId");

        query.setParameter("userId", userId);

        int totalPrice = (int)query.getSingleResult();

        em.getTransaction().commit();

        return totalPrice;
    }

    private String getProductNames(List<String> products, List<Integer> quantity) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            builder.append(products.get(i));
            builder.append(" * ");
            builder.append(quantity.get(i));
            builder.append(", ");
        }

        if (!builder.isEmpty()) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }
}

