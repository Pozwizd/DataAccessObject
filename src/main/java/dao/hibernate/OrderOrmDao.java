package dao.hibernate;

import dao.OrderDao;
import models.Order;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class OrderOrmDao implements OrderDao{

    SessionFactory sessionFactory;

    public OrderOrmDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createOrder(Order order) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(order);
        tx.commit();
        session.close();
    }

    @Override
    public List<Order> getUserOrders(int userId) {
        Session session = sessionFactory.openSession();
        String hql = "from Order where userId = :userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        List<Order> orders = query.list();
        session.close();
        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        Session session = sessionFactory.openSession();
        List<Order> orders = session.createQuery("from Order").list();
        session.close();
        return orders;
    }
}
