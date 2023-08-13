package org.example;

import Entity.Order;
import Entity.User;
import org.hibernate.SessionFactory;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceOrm {

    private SessionFactory sessionFactory;

    public Order createOrder(int userId) {

        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            // Получить пользователя
            User user = em.find(User.class, userId);

            // Получить товары и кол-во из корзины
            Query query = em.createQuery("SELECT p.productName, sc.quantity" +
                    " FROM shopping_cart sc JOIN product p WHERE sc.user.id = :userId");
            query.setParameter("userId", userId);

            List<String> productNames = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for(Object[] row : (List<Object[]>)query.getResultList()) {
                productNames.add((String)row[0]);
                quantities.add((Integer)row[1]);
            }

            // Сформировать список товаров
            String productList = getProductNames(productNames, quantities);
            em.close();
            em = EntityManagerUtil.getEntityManager();
            // Рассчитать стоимость
            Query priceQuery = em.createQuery("SELECT SUM(p.price * s.quantity) FROM product p JOIN p.shoppingCarts s WHERE s.user.id = :userId");
            priceQuery.setParameter("userId", userId);
            double totalPrice = priceQuery.getFirstResult();

            // Создать и сохранить заказ
            em.close();
            em = EntityManagerUtil.getEntityManager();

            Order order = new Order();
            order.setUser(user);
            order.setOrderList(productList);
            order.setTotalPrice(totalPrice);

            // Очистить корзину
            Query deleteQuery = em.createQuery("DELETE FROM shopping_cart sc WHERE sc.user.id = :userId");
            deleteQuery.setParameter("userId", userId);
            deleteQuery.executeUpdate();
            em.getTransaction().commit();
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(em != null)
                em.close(); // (5)
        }
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