package jdbcDao;

import dao.jdbc.ProductJdbcDao;
import models.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ProductJdbcTest {

    private static final Logger logger = LogManager.getLogger(ProductJdbcTest.class);
    private final ProductJdbcDao productJdbcDao = new ProductJdbcDao();

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
    public void createProductTest() throws Exception {
        Product ExtendProduct = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);

        productJdbcDao.createProduct(ExtendProduct);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE id=?");
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product actualProduct = new Product(id, product_name, description, price, quantity);
                assertEquals(ExtendProduct.getId(), actualProduct.getId());
                assertEquals(ExtendProduct.getProductName(), actualProduct.getProductName());
                assertEquals(ExtendProduct.getDescription(), actualProduct.getDescription());
                assertEquals(ExtendProduct.getPrice(), actualProduct.getPrice(), 0.001);
                assertEquals(ExtendProduct.getQuantity(), actualProduct.getQuantity());
            }
            rs.close();
            stmt.close();
            logger.info("Product successfully created");

        } catch (SQLException e) {
            logger.info("Product creation error");
        }
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product(1,"AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.executeUpdate();

            stmt.setString(1, product2.getProductName());
            stmt.setString(2, product2.getDescription());
            stmt.setDouble(3, product2.getPrice());
            stmt.setInt(4, product2.getQuantity());
            stmt.executeUpdate();
            stmt.close();


            List<Product> products = productJdbcDao.getAllProducts();
            assertEquals(products.get(1).getId(), product2.getId());
            assertEquals(products.get(1).getProductName(), product2.getProductName());
            assertEquals(products.get(1).getDescription(), product2.getDescription());
            assertEquals(products.get(1).getPrice(), product2.getPrice(), 0.001);
            assertEquals(products.get(1).getQuantity(), product2.getQuantity());


            assertEquals(products.get(0).getId(), product.getId());
            assertEquals(products.get(0).getProductName(), product.getProductName());
            assertEquals(products.get(0).getDescription(), product.getDescription());
            assertEquals(products.get(0).getPrice(), product.getPrice(), 0.001);
            assertEquals(products.get(0).getQuantity(), product.getQuantity());
            logger.info("Product successfully created");
        } catch (SQLException e) {
            logger.info("Product successfully created");
        }
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1,"AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();


            stmt.setString(1, product2.getProductName());
            stmt.setString(2, product2.getDescription());
            stmt.setDouble(3, product2.getPrice());
            stmt.setInt(4, product2.getQuantity());

            stmt.executeUpdate();
            stmt.close();

            List<Product> products = productJdbcDao.getAllProducts();
            assertEquals(products.remove(1).getId(), product2.getId());
            assertEquals(products.remove(0).getId(), product.getId());

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();
            logger.info("Product successfully received by id");
        } catch (SQLException e) {
            logger.info("Error getting Product by id");
        }
    }


    @Test
    public void testUpdateProduct() {

        Product product = new Product(1,
                "AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product updateProduct = new Product(1,
                "ASUS ROG Strix Z590-E Gaming WiFi 6E",
                "ATX Motherboard",
                449.99,
                15);


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();
            stmt.close();

            productJdbcDao.updateProduct(updateProduct);

            stmt = connection.prepareStatement("SELECT * FROM product WHERE id=?");
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product actualProduct = new Product(id, product_name, description, price, quantity);
                assertEquals(updateProduct.getId(), actualProduct.getId());
                assertEquals(updateProduct.getProductName(), actualProduct.getProductName());
                assertEquals(updateProduct.getDescription(), actualProduct.getDescription());
                assertEquals(updateProduct.getPrice(), actualProduct.getPrice(), 0.001);
                assertEquals(updateProduct.getQuantity(), actualProduct.getQuantity());
            }
            rs.close();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM product");
            stmt.executeUpdate();
            stmt.close();
            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();
            logger.info("Product successfully updated");

        } catch (SQLException e) {
            logger.info("Product update error");
        }

    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(1,"AMD Ryzen 9 5950X",
                "16-Core 3.4 GHz CPU",
                999.99,
                10);
        Product product2 = new Product(2,
                "Intel Core i9-11900K",
                "8-Core 3.5 GHz CPU",
                699.99,
                12);

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();

            stmt.setString(1, product2.getProductName());
            stmt.setString(2, product2.getDescription());
            stmt.setDouble(3, product2.getPrice());
            stmt.setInt(4, product2.getQuantity());

            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("SELECT * FROM product");
            ResultSet rs = stmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product actualProduct = new Product(id, product_name, description, price, quantity);
                products.add(actualProduct);
            }
            rs.close();
            stmt.close();

            for (Product product1: products) {
                productJdbcDao.deleteProduct(product1.getId());
            }

            stmt = connection.prepareStatement("SELECT * FROM product");
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                assertNull(new Product(id, product_name, description, price, quantity));
            }
            rs.close();
            stmt.close();
            logger.info("Product successfully deleted");

        } catch (SQLException e) {
            logger.info("Product deletion error");
        }
    }
}