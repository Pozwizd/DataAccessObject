package dao.hibernate;

import Entity.ShoppingCart;
import Entity.User;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class ShoppingCartOrmDao implements ShoppingCartDao {

    @Override
    public void addProductToCart(ShoppingCart shoppingCart) {

        EntityManager  em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(em.merge(shoppingCart));
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            User user = em.find(User.class, shoppingCart.getUser().getId());

            for (ShoppingCart cart : user.getShoppingCarts()) {
                if (cart.getProduct().getId() == shoppingCart.getProduct().getId()
                        && cart.getQuantity() == shoppingCart.getQuantity()) {
                    em.remove(cart);
                    user.getShoppingCarts().remove(cart);
                }
            }
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

    }

    @Override
    public List<ShoppingCart> getUserCartProducts(long userId) {

        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(User.class, userId).getShoppingCarts();
    }

    @Override
    public void clearUserCart(long userId) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            User user = em.find(User.class, userId);

            for (ShoppingCart cart : user.getShoppingCarts()) {
                em.remove(cart);
            }
            user.getShoppingCarts().clear();
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
