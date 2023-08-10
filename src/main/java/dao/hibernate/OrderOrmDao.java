package dao.hibernate;

import dao.OrderDao;
import models.Order;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import utils.EntityManagerUtil;

import java.util.List;

public class OrderOrmDao implements OrderDao{

    @Override
    public void createOrder(Order order) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(order);
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
    public List<Order> getUserOrders(int userId) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT o FROM orders o WHERE o.userId = :userId");
            query.setParameter("userId", userId);
            List<Order> orders = query.getResultList();
            em.getTransaction().commit();
            return orders;

        } catch (Exception ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Order> getAllOrders() {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT o FROM orders o");
            List<Order> orders = query.getResultList();
            em.getTransaction().commit();
            return orders;

        } catch (Exception ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}