package dao;

import models.Product;
import models.ShoppingCart;

import java.util.List;

public interface ShoppingCartDao {


    void addProductToCart(ShoppingCart shoppingCart);

    void removeProductFromCart(ShoppingCart shoppingCart);

    List<ShoppingCart> getUserCartProducts(int userId);

    void clearUserCart(int userId);

}
