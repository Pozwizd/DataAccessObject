package OrmDao;

import dao.hibernate.ProductOrmDao;
import models.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.HibernateUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;

public class ProductHibernateTest {

    private static final Logger logger = LogManager.getLogger(ProductHibernateTest.class);

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final ProductOrmDao productOrmDao = new ProductOrmDao(sessionFactory);

    @AfterEach
    public void clearDatabase() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Product").executeUpdate();
            transaction.commit();
            session.close();
            logger.info("Database cleared");
        } catch (HibernateException e) {
            logger.error("Error clearing database", e);
        }
    }

    @Test
    public void createProductTest() {
        Product expectedProduct = new Product(
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        productOrmDao.createProduct(expectedProduct);

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product actualProduct = session.get(Product.class, expectedProduct.getId());

            assertNotNull(actualProduct);
            assertEquals(1, actualProduct.getId());
            assertEquals(expectedProduct.getProductName(), actualProduct.getProductName());
            assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
            assertEquals(expectedProduct.getPrice(), actualProduct.getPrice(), 0.001);
            assertEquals(expectedProduct.getQuantity(), actualProduct.getQuantity());

            logger.info("Product successfully created");
        } catch (HibernateException e) {
            logger.error("Product creation error", e);
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

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(product1);
            session.save(product2);
            transaction.commit();

            List<Product> products = productOrmDao.getAllProducts();

            assertEquals(2, products.size());

            Product retrievedProduct1 = session.get(Product.class, 1);
            Product retrievedProduct2 = session.get(Product.class, 2);

            assertEquals(1, products.get(0).getId());
            assertEquals(retrievedProduct1.getProductName(), products.get(0).getProductName());
            assertEquals(retrievedProduct1.getDescription(), products.get(0).getDescription());
            assertEquals(retrievedProduct1.getPrice(), products.get(0).getPrice(), 0.001);
            assertEquals(retrievedProduct1.getQuantity(), products.get(0).getQuantity());

            assertEquals(2, products.get(1).getId());
            assertEquals(retrievedProduct2.getProductName(), products.get(1).getProductName());
            assertEquals(retrievedProduct2.getDescription(), products.get(1).getDescription());
            assertEquals(retrievedProduct2.getPrice(), products.get(1).getPrice(), 0.001);
            assertEquals(retrievedProduct2.getQuantity(), products.get(1).getQuantity());
            session.close();
            logger.info("All products successfully retrieved");
        } catch (HibernateException e) {
            logger.error("Error retrieving all products", e);
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

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(product1);
            session.save(product2);
            transaction.commit();

            Product retrievedProduct1 = productOrmDao.getProductById(1);
            Product retrievedProduct2 = productOrmDao.getProductById(2);

            assertEquals(1, retrievedProduct1.getId());
            assertEquals(product1.getProductName(), retrievedProduct1.getProductName());
            assertEquals(product1.getDescription(), retrievedProduct1.getDescription());
            assertEquals(product1.getPrice(), retrievedProduct1.getPrice(), 0.001);
            assertEquals(product1.getQuantity(), retrievedProduct1.getQuantity());

            assertEquals(2, retrievedProduct2.getId());
            assertEquals(product2.getProductName(), retrievedProduct2.getProductName());
            assertEquals(product2.getDescription(), retrievedProduct2.getDescription());
            assertEquals(product2.getPrice(), retrievedProduct2.getPrice(), 0.001);
            assertEquals(product2.getQuantity(), retrievedProduct2.getQuantity());

            logger.info("Product successfully retrieved by ID");
        } catch (HibernateException e) {

        }
    }
}