package dao.jdbc;


import dao.ProductDao;
import models.Product;
import utils.ConnectionPool;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class ProductJdbcDAO implements ProductDao {

    private static final String SQL_GET_ALL = "SELECT * FROM product";


    @Override
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(SQL_GET_ALL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product product = new Product(id, product_name, description, price, quantity);
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения пользователей");
        }
        return products;
    }

    @Override
    public Product getProductById(int productId) {

        Product product = null;


        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM product WHERE id=?");
            stmt.setInt(1, productId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String product_name = rs.getString("product_name");
                String description = rs.getString("description");
                ;
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                product = new Product(id, product_name, description, price, quantity);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }


    @Override
    public void createProduct(Product product) {

        Connection conn = null;

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }

    }

    @Override
    public void updateProduct(Product product) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE product SET product_name = ? , description = ?, price = ?, quantity = ? WHERE id = ?");

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setInt(5, product.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
    }

    @Override
    public void deleteProduct(int productId) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM product WHERE id = ?");

            stmt.setInt(1, productId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }

    }
}