package jdbcDao;

import dao.ProductDao;
import dao.jdbc.ProductJdbcDAO;
import models.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProductJdbcTest {

    private ProductDao productDao = new ProductJdbcDAO();

    @Before
    @Test
    public void createProductTest() throws Exception {

        Product product = new Product("AMD Ryzen 9 5950X", "16-Core 3.4 GHz CPU", 999.99, 10);
        productDao.createProduct(product);
        assertEquals(product, productDao.getProductById(1));

    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product("AMD Ryzen 9 5950X", "16-Core 3.4 GHz CPU", 999.99, 10);
        Product product2 = new Product("Intel Core i9-11900K", "8-Core 3.5 GHz CPU", 699.99, 12);
        productDao.createProduct(product2);

        List<Product> products = productDao.getAllProducts();
        assertEquals(products, List.of(product, product2));

    }

    @Test
    public void testGetProductById() {
        Product product = new Product("AMD Ryzen 9 5950X", "16-Core 3.4 GHz CPU", 999.99, 10);

        Product expectedProduct = productDao.getProductById(1);
        assertEquals(product, expectedProduct);
    }


    @Test
    public void testUpdateProduct() {
        Product product = new Product(1,"ASUS ROG Strix Z590-E Gaming WiFi 6E", "ATX Motherboard", 449.99, 15);

        productDao.updateProduct(product);

        assertEquals(product, productDao.getProductById(1));

    }

    @After
    @Test
    public void testDeleteProduct() {
        List<Product> products = productDao.getAllProducts();
        for (Product prd : products) {
            productDao.deleteProduct(prd.getId());
        }
        assertNull(productDao.getAllProducts());
    }
}