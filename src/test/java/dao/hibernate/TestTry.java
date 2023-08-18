package dao.hibernate;

import Entity.*;
import org.junit.jupiter.api.Test;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;


public class TestTry {

    @Test
    void test() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        entityManager.getTransaction().begin();

        User user = new User(
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");


        Product product = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        Product product2 = new Product(
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);



        entityManager.persist(user);
        entityManager.persist(product);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManager = EntityManagerUtil.getEntityManager();
        entityManager.getTransaction().begin();
        ShoppingCart shoppingCart = new ShoppingCart(user,product,4);

        ShoppingCart shoppingCartMerge = entityManager.merge(shoppingCart);
        entityManager.persist(shoppingCartMerge);

        entityManager.getTransaction().commit();
        System.out.println(entityManager.find(ShoppingCart.class, 1L));
        entityManager.close();

    }
}
