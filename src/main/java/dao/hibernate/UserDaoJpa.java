package dao.hibernate;

import dao.UserDao;
import models.User;
import javax.persistence.*;
import utils.EntityManagerUtil;

import java.sql.SQLException;
import java.util.List;


public class UserDaoJpa implements UserDao {

    private EntityManager em;

    public UserDaoJpa() {
        em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void createUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("from User", User.class).getResultList();
    }

    @Override
    public void deleteUser(User user) {
        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
    }
    public void close() {
        em.close();
    }

}