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
            Query query = em.createQuery("from Order where userId = :userId");
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            Query query = em.createQuery("from Order");
            return query.getResultList();
        } catch(Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
}
