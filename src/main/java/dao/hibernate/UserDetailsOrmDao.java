package dao.hibernate;


import Entity.UserDetails;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.List;


public class UserDetailsOrmDao {


    public void createUserDetails(UserDetails details) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(details);
            em.persist(details);
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

    public void updateUserDetails(UserDetails details) {
        UserDetails userDetails = new UserDetails(
                details.getFirstName(),
                details.getLastName(),
                details.getGender(),
                details.getDateOfBirth(),
                details.getAddress(),
                details.getUser());
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(userDetails);
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

    public UserDetails getUserDetailsById(int id) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            return em.find(UserDetails.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserDetails> getAllUserDetails() {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            return em.createQuery("from user_details", UserDetails.class).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}