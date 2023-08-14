package dao.hibernate;

import Entity.Order;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public List<Order> getUserOrders(long userId) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT o FROM Order o WHERE o.user.id = :userId");
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

    /**
     *
     */
    @Override
    public List<Order> getAllOrders() {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("from Order");
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