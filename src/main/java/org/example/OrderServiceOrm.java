package org.example;

import Entity.Order;
import Entity.Product;
import Entity.ShoppingCart;
import Entity.User;
import dao.hibernate.ShoppingCartOrmDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceOrm {

    private static final Logger logger = LogManager.getLogger(OrderServiceOrm.class);

    private SessionFactory sessionFactory;

    private ShoppingCartOrmDao shoppingCartOrmDao;

    public Order createOrder(long userId) {

        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            List<String> productNames = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            for (int i = 0; i < user.getShoppingCarts().size(); i++) {
                productNames.add(em.find(Product.class, user.getShoppingCarts().get(i).getProduct().getId()).getProductName());
                quantities.add(user.getShoppingCarts().get(i).getQuantity());
            }

            String productList = getProductNames(productNames, quantities);

            double totalPrice = 0;
            for (int i = 0; i < user.getShoppingCarts().size(); i++) {
                totalPrice += user.getShoppingCarts().get(i).getProduct().getPrice()
                        * user.getShoppingCarts().get(i).getQuantity();
            }
            Order order = new Order();
            order.setUser(user);
            order.setOrderList(productList);
            order.setTotalPrice(totalPrice);

            User user1 = em.find(User.class, userId);

            for (ShoppingCart cart : user1.getShoppingCarts()) {
                em.remove(cart);
            }
            user.getShoppingCarts().clear();
            em.merge(user);
            em.getTransaction().commit();
            em.close();

            return order;
        } catch (Exception e) {
            logger.error("Error while creating order", e);
            if(em != null) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            if(em != null)
                em.close();
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