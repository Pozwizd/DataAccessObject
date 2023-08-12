package dao.hibernate;

import Entity.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAllProducts();

    Product getProductById(long productId);

    void createProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(long productId);

}