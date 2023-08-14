package dao.hibernate;

import Entity.ShoppingCart;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartOrmDao implements ShoppingCartDao {




    @Override
    public void addProductToCart(ShoppingCart shoppingCart) {

        EntityManager  em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.merge(shoppingCart);
            em.getTransaction().begin();
            em.persist(shoppingCart);
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

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.merge(shoppingCart);
            em.getTransaction().begin();
            em.remove(shoppingCart);
            em.getTransaction().commit();
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
    public List<ShoppingCart> getUserCartProducts(long userId) {
        EntityManager em = null;
        ArrayList<ShoppingCart> shoppingCartList = new ArrayList<>();
        try {
            em = EntityManagerUtil.getEntityManager();
            Query query = em.createQuery("SELECT c FROM shopping_cart c JOIN c.user u WHERE u.id = :userId");
//            Query query = em.createQuery("from shopping_cart where user.id =:userId");
            query.setParameter("userId", userId);
            shoppingCartList.add((ShoppingCart) query.getSingleResult());
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return shoppingCartList;
    }

    @Override
    public void clearUserCart(long userId) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            Query query = em.createQuery("delete from shopping_cart where user.id =:userId");
            query.setParameter("userId", userId);
            query.executeUpdate();
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
}
