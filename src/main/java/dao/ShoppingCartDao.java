package dao;

import models.Product;
import models.ShoppingCart;

import java.sql.SQLException;
import java.util.List;

public interface ShoppingCartDao {


    void addProductToCart(int userId, int productId);

    void removeProductFromCart(int userId, int productId);

    List<Product> getUserCartProducts(int userId);

    void clearUserCart(int userId);

}
