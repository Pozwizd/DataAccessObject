package dao.hibernate;


import models.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDetailsOrmDao {

    SessionFactory sessionFactory;

    public UserDetailsOrmDao(SessionFactory factory) {
        this.sessionFactory = factory;
    }

    public void createUserDetails(UserDetails details) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(details);
        tx.commit();
        session.close();
    }

    public void updateUserDetails(UserDetails details) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(details);
        tx.commit();
        session.close();
    }

    public UserDetails getUserDetailsById(int id) {
        Session session = sessionFactory.openSession();
        UserDetails details = session.get(UserDetails.class, id);
        session.close();
        return details;
    }

    public List<UserDetails> getAllUserDetails() {
        Session session = sessionFactory.openSession();
        List<UserDetails> details = session.createQuery("FROM UserDetails").list();
        session.close();
        return details;
    }
}