package dao.hibernate;

import Entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductOrmTest {

    private static final Logger logger = LogManager.getLogger(ProductOrmTest.class);

    ProductOrmDao productOrmDao = new ProductOrmDao();

    @AfterEach
    public void deleteProductAfterTest(){
        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();


            stmt = connection.prepareStatement("DELETE FROM orders");
            stmt.executeUpdate();
            stmt.close();


            stmt = connection.prepareStatement("ALTER TABLE orders AUTO_INCREMENT = 1");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createProductTest() {
        Product product = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        EntityManager em = null;

        productOrmDao.createProduct(product);

        try {
            em = EntityManagerUtil.getEntityManager();
            Product expectedProduct = em.find(Product.class, 1);
            assertNotNull(expectedProduct);
            assertEquals(product.getProductName(), expectedProduct.getProductName());
            assertEquals(product.getDescription(), expectedProduct.getDescription());
            assertEquals(product.getPrice(), expectedProduct.getPrice());
            logger.info("Product successfully created");
        } catch (Exception e) {
            logger.error("Error creating product", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(product1);
            em.persist(product2);
            em.getTransaction().commit();
            List<Product> products = productOrmDao.getAllProducts();
            for (Product product : products) {
                assertNotNull(product);
                assertEquals(product.getProductName(), product1.getProductName());
                assertEquals(product.getDescription(), product1.getDescription());
                assertEquals(product.getPrice(), product1.getPrice());
                assertEquals(product.getQuantity(), product1.getQuantity());
                assertEquals(product.getProductName(), product2.getProductName());
                assertEquals(product.getDescription(), product2.getDescription());
                assertEquals(product.getPrice(), product2.getPrice());
                assertEquals(product.getQuantity(), product2.getQuantity());
                logger.info("Product successfully retrieved");
            }
            logger.info("All products successfully retrieved");
        } catch (HibernateException e) {
            logger.error("Error retrieving all products", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testGetProductById() {
        Product product1 = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);
        EntityManager em  = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(product1);
            em.persist(product2);
            em.getTransaction().commit();
            Product expectedProduct = productOrmDao.getProductById(1);
            assertNotNull(expectedProduct);
            assertEquals(expectedProduct.getProductName(), product1.getProductName());
            assertEquals(expectedProduct.getDescription(), product1.getDescription());
            assertEquals(expectedProduct.getPrice(), product1.getPrice());
            assertEquals(expectedProduct.getQuantity(), product1.getQuantity());
            logger.info("Product successfully retrieved by ID");
        } catch (HibernateException e) {
            logger.info("Error getting Product by id");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testUpdateProduct() {
        Product product1 = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(1,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);
        EntityManager em  = null;
        try {
            em  = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Product product1Merge = em.merge(product1);
            em.persist(product1Merge);
            em.getTransaction().commit();
            em.close();

            productOrmDao.updateProduct(product2);

            em = EntityManagerUtil.getEntityManager();
            Product expectedProduct = em.find(Product.class, 1);
            assertNotNull(expectedProduct);
            assertEquals(expectedProduct.getProductName(), product2.getProductName());
            assertEquals(expectedProduct.getDescription(), product2.getDescription());
            assertEquals(expectedProduct.getPrice(), product2.getPrice());
            assertEquals(expectedProduct.getQuantity(), product2.getQuantity());
            logger.info("Product successfully updated");
        } catch (HibernateException e) {
            logger.info("Error updating product");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testDeleteProduct() {
        Product product1 = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(product1);
            em.getTransaction().commit();
            productOrmDao.deleteProduct(1);
            Product expectedProduct = em.find(Product.class, 1);
            assertNotNull(expectedProduct);
            logger.info("Product successfully deleted");
        } catch (HibernateException e) {
            logger.info("Error deleting product");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}