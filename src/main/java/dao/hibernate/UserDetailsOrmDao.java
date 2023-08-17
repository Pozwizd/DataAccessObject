package dao.hibernate;

import Entity.User;
import Entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.List;


public class UserDetailsOrmDao {

    private static final Logger logger = LogManager.getLogger(UserDetailsOrmDao.class);

    public void createUserDetails(UserDetails details) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            User attachedUser = em.merge(details.getUser());
            details.setUser(attachedUser);
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

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            UserDetails attachedDetails = em.find(UserDetails.class, details.getUser().getId());
            attachedDetails.setFirstName(details.getFirstName());
            attachedDetails.setLastName(details.getLastName());
            attachedDetails.setGender(details.getGender());
            attachedDetails.setDateOfBirth(details.getDateOfBirth());
            attachedDetails.setAddress(details.getAddress());
            attachedDetails.setUser(details.getUser());
            em.merge(details);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public UserDetails getUserDetailsById(long id) {
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