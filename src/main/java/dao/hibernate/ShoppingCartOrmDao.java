package dao.hibernate;

import dao.ShoppingCartDao;
import models.ShoppingCart;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ShoppingCartOrmDao implements ShoppingCartDao {

    SessionFactory sessionFactory;

    @Override
    public void addProductToCart(ShoppingCart shoppingCart) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(shoppingCart);

        tx.commit();
        session.close();
    }

    @Override
    public void removeProductFromCart(ShoppingCart shoppingCart) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(shoppingCart);

        tx.commit();
        session.close();
    }

    @Override
    public List<ShoppingCart> getUserCartProducts(int userId) {
        Session session = sessionFactory.openSession();
        String hql = "from ShoppingCart where userId =:userId";

        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);

        List<ShoppingCart> carts = query.list();
        session.close();
        return carts;
    }

    @Override
    public void clearUserCart(int userId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        String hql = "delete from ShoppingCart where userId =:userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.executeUpdate();

        tx.commit();
        session.close();
    }
}
