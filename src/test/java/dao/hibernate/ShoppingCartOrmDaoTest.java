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
import utils.ConnectionPoolForOrm;
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

    User user = new User(
            "user1",
            "userPassword1",
            "user1@example.com",
            "123456");

    User user2 = new User(
            "user2",
            "userPassword2",
            "user2@example.com",
            "654321");

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

    @BeforeEach
    public void createObjectsForTesting(){


        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(em.merge(user));
            em.persist(em.merge(user2));
            em.persist(em.merge(product));
            em.persist(em.merge(product2));
            em.getTransaction().commit();
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

        try(Connection connection = ConnectionPoolForOrm.getConnection()) {
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
        } catch (SQLException e) {
            logger.info("Error deleting user and product after testing");
        }

    }

    @Test
    void addProductToCart() {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            ShoppingCart shoppingCart = new ShoppingCart(em.find(User.class, 1L), em.find(Product.class, 1L),4);
            em.close();

            shoppingCartOrmDao.addProductToCart(shoppingCart);

            em = EntityManagerUtil.getEntityManager();
            ShoppingCart shoppingCartFromDb = em.find(ShoppingCart.class, 1L);
            assertEquals(shoppingCartFromDb.getQuantity(), shoppingCart.getQuantity());
            logger.info("Product is successfully added to the shopping cart");
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
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            User user1 = em.find(User.class, 1L);

            ShoppingCart shoppingCart = new ShoppingCart(user1,em.find(Product.class, 1L),4);
            ShoppingCart shoppingCart2 = new ShoppingCart(user1,em.find(Product.class, 2L),1);

            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.getTransaction().commit();
            em.close();

            List<ShoppingCart> shoppingCartList = shoppingCartOrmDao.getUserCartProducts(1L);

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
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            User user1 = em.find(User.class, 1L);
            User user2 = em.find(User.class, 2L);

            Product product1 = em.find(Product.class, 1L);
            Product product2 = em.find(Product.class, 2L);

            ShoppingCart shoppingCart = new ShoppingCart(user1,product1,2);
            ShoppingCart shoppingCart2 = new ShoppingCart(user1,product2,3);

            ShoppingCart shoppingCartUser2 = new ShoppingCart(user2,product1,1);
            ShoppingCart shoppingCartUser2_2 = new ShoppingCart(user2,product2,1);

            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.persist(shoppingCartUser2);
            em.persist(shoppingCartUser2_2);
            em.getTransaction().commit();
            em.close();


            shoppingCartOrmDao.clearUserCart(2L);
            em = EntityManagerUtil.getEntityManager();
            List<ShoppingCart> shoppingCartList = em.find(User.class, 2L).getShoppingCarts();
            assertEquals(0, shoppingCartList.size());
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
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            User user1 = em.find(User.class, 1L);

            Product product1 = em.find(Product.class, 1L);
            Product product2 = em.find(Product.class, 2L);

            ShoppingCart shoppingCart = new ShoppingCart(user1,product1,2);
            ShoppingCart shoppingCart2 = new ShoppingCart(user1,product2,3);

            em.persist(shoppingCart);
            em.persist(shoppingCart2);
            em.getTransaction().commit();
            em.close();

            shoppingCartOrmDao.removeProductFromCart(shoppingCart);

            em = EntityManagerUtil.getEntityManager();
            List<ShoppingCart> actualShoppingCart = em.find(User.class, 1L).getShoppingCarts();
            assertEquals(actualShoppingCart.get(0).getUser().getId(), shoppingCart2.getUser().getId());
            assertEquals(actualShoppingCart.get(0).getProduct().getId(), shoppingCart2.getProduct().getId());
            assertEquals(actualShoppingCart.get(0).getQuantity(), shoppingCart2.getQuantity());
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

