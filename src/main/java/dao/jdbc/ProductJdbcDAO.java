package dao.jdbc;


import dao.ProductDao;
import models.Product;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static utils.DBConnection.closeConnection;


public class ProductJdbcDAO implements ProductDao {

    private static final String SQL_GET_ALL = "SELECT * FROM product";
    private DBConnection dbConnection;


    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_GET_ALL);
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
        } finally {
            closeConnection(conn);
        }
        return products;
    }

    @Override
    public Product getProductById(int productId) {

        Product product = null;
        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product WHERE id=?");
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
        } finally {
            closeConnection(conn);
        }

        return product;
    }


    @Override
    public void createProduct(Product product) {

        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO product (product_name, description, price, quantity) VALUES (?, ?, ?, ?)");

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        } finally {
            closeConnection(conn);
        }

    }

    @Override
    public void updateProduct(Product product) {

        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE product SET product_name = ? , description = ?, price = ?, quantity = ? WHERE id = ?");

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setInt(5, product.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void deleteProduct(int productId) {

        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM product WHERE id = ?");

            stmt.setInt(1, productId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        } finally {
            closeConnection(conn);
        }

    }
}