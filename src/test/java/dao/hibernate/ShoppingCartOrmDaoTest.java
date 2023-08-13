package dao.hibernate;

import dao.jdbc.ShoppingCartJdbcDao;
import Entity.Product;
import Entity.ShoppingCart;
import Entity.User;
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

class ShoppingCartOrmDaoTest {

    private static final Logger logger = LogManager.getLogger(ShoppingCartJdbcDao.class);

    ShoppingCartOrmDao shoppingCartOrmDao = new ShoppingCartOrmDao();

    private final User user = new User(1,
            "user1",
            "userPassword1",
            "user1@example.com",
            "123456");

    private final User user2 = new User(2,
            "user2",
            "userPassword2",
            "user2@example.com",
            "654321");

    private final Product product = new Product(1,
            "AMD Ryzen 9 5950X",
            "16-Core 3.4 GHz CPU",
            999.99,
            10);

    private final Product product2 = new Product(2,
            "Intel Core i9-11900K",
            "8-Core 3.5 GHz CPU",
            699.99,
            12);

    @BeforeEach
    public void createObjectsForTesting(){
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.persist(user2);
            em.persist(product);
            em.persist(product2);
            em.getTransaction().commit();
            logger.info("Creating the User and the product for testing");
        } catch (Exception e) {
            logger.info("Error creating user and product for testing");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @AfterEach
    public void deleteObjectsForTesting(){

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM shopping_cart");
            stmt.executeUpdate();
            stmt.close();


            stmt = connection.prepareStatement("ALTER TABLE shopping_cart AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();
            logger.info("Deleting the User and the product after a test");
        } catch (SQLException e) {
            logger.info("Error deleting user and product after testing");

        }

    }

    @Test
    void addProductToCart() {
        ShoppingCart shoppingCart = new ShoppingCart(user,product,4);
        shoppingCartOrmDao.addProductToCart(shoppingCart);
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            ShoppingCart shoppingCartFromDb = em.find(ShoppingCart.class, 1);
            assertEquals(shoppingCartFromDb.getQuantity(), shoppingCart.getQuantity());
            logger.info("Testing addProductToCart method");
        } catch (Exception e) {
            logger.info("Error testing addProductToCart method");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    void getUserCartProducts() {
        ShoppingCart shoppingCart = new ShoppingCart(user,product,4);
        ShoppingCart shoppingCart2 = new ShoppingCart(user,product2,1);

        ShoppingCart shoppingCartUser2 = new ShoppingCart(user2,product,4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(user2,product2,1);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.persist(shoppingCartUser2);
            em.persist(shoppingCartUser2_2);
            em.getTransaction().commit();
            em.close();
            List<ShoppingCart> shoppingCartList = shoppingCartOrmDao.getUserCartProducts(1);
            assertEquals(shoppingCartList.size(), 2);
            assertEquals(shoppingCartList.get(0).getUser().getId(), shoppingCart.getUser().getId());
            assertEquals(shoppingCartList.get(0).getProduct().getId(), shoppingCart.getProduct().getId());
            assertEquals(shoppingCartList.get(0).getQuantity(), shoppingCart.getQuantity());
            assertEquals(shoppingCartList.get(1).getUser().getId(), shoppingCart2.getUser().getId());
            assertEquals(shoppingCartList.get(1).getProduct().getId(), shoppingCart2.getProduct().getId());
            assertEquals(shoppingCartList.get(1).getQuantity(), shoppingCart2.getQuantity());
            logger.info("User cart products are successfully retrieved");
        } catch (Exception e) {
            logger.info("Error testing getUserCartProducts method");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    void clearUserCart() {
        ShoppingCart shoppingCart = new ShoppingCart(user,product,4);
        ShoppingCart shoppingCart2 = new ShoppingCart(user,product2,1);

        ShoppingCart shoppingCartUser2 = new ShoppingCart(user2,product,4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(user2,product2,1);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.persist(shoppingCartUser2);
            em.persist(shoppingCartUser2_2);
            em.getTransaction().commit();
            em.close();
            shoppingCartOrmDao.clearUserCart(2);
            em = EntityManagerUtil.getEntityManager();
            List<ShoppingCart> shoppingCartList= em.createQuery("SELECT s FROM shopping_cart s WHERE shopping_cart.user.id = :userId", ShoppingCart.class)
                    .setParameter("userId", 2)
                    .getResultList();
            assertEquals(shoppingCartList.size(), 0);
            logger.info("User cart products are successfully cleared");
        } catch (Exception e) {
            logger.info("Error testing getUserCartProducts method");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    void removeProductFromCart() {
        ShoppingCart shoppingCart = new ShoppingCart(user,product,4);
        ShoppingCart shoppingCart2 = new ShoppingCart(user,product2,1);

        ShoppingCart shoppingCartUser2 = new ShoppingCart(user2,product,4);
        ShoppingCart shoppingCartUser2_2 = new ShoppingCart(user2,product2,1);
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.getTransaction().commit();
            em.close();
            shoppingCartOrmDao.removeProductFromCart(shoppingCart);
            em = EntityManagerUtil.getEntityManager();
            ShoppingCart actualShoppingCart = em.find(ShoppingCart.class, user);
            assertEquals(actualShoppingCart.getUser().getId(), shoppingCart2.getUser().getId());
            assertEquals(actualShoppingCart.getProduct().getId(), shoppingCart2.getProduct().getId());
            assertEquals(actualShoppingCart.getQuantity(), shoppingCart2.getQuantity());
            logger.info("Product is successfully removed from the user cart");
        } catch (Exception e) {
            logger.info("Error testing getUserCartProducts method");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

