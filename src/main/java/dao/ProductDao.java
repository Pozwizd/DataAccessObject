package dao;

import models.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAllProducts();

    Product getProductById(int productId);

    void createProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(int productId);

}