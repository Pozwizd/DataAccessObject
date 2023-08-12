package dao.hibernate;

import Entity.User;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.List;


public class UserDaoJpa implements UserDao {


    @Override
    public void createUser(User user) {
        User newUser = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone_number());
        EntityManager em = null;

        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(newUser);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if(em != null) {
                em.close();
            }
        }

    }



    @Override
    public void updateUser(User user) {
        User newUser = new User(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone_number());
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Override
    public User getUserById(long id) {

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            return em.find(User.class, id);
        } finally {
            if(em != null) {
                em.close();
            }
        }

    }

    @Override
    public List<User> getAllUsers() {

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            return em.createQuery("from User", User.class).getResultList();
        } finally {
            if(em != null) {
                em.close();
            }
        }

    }

    @Override
    public void deleteUser(User user) {

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            User newUser = em.merge(user);
            em.getTransaction().begin();
            em.remove(newUser);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }
}