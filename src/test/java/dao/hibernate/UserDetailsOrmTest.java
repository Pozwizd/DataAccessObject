package dao.hibernate;

import Entity.Gender;
import Entity.User;
import Entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class UserDetailsOrmTest {

    private static final Logger logger = LogManager.getLogger(UserDetailsOrmTest.class);

    UserDetailsOrmDao userDetailsOrmDao = new UserDetailsOrmDao();

    private final User user = new User(1,
            "user1",
            "userPassword1",
            "user1@example.com",
            "123456");


    @BeforeEach
    public void createUserBeforeUserDetailsTest() {
        User extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(extentedUser);
            em.persist(extentedUser);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @AfterEach
    public void deleteAfterTest() {
        try (Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testCreateUserDetails() {
        LogManager.getLogger(org.hibernate.Version.class);
        UserDetails details = new UserDetails("Іван",
                "Петренко",
                Gender.MALE,
                new Date(1980, 1, 1),
                "Київ, вул. Шевченка 10",
                user);

        userDetailsOrmDao.createUserDetails(details);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            UserDetails userDetails = em.find(UserDetails.class, 1);
            Assertions.assertEquals(details.getFirstName(), userDetails.getFirstName());
            Assertions.assertEquals(details.getLastName(), userDetails.getLastName());
            Assertions.assertEquals(details.getGender(), userDetails.getGender());
            Assertions.assertEquals(details.getDateOfBirth(), userDetails.getDateOfBirth());
            Assertions.assertEquals(details.getAddress(), userDetails.getAddress());
            Assertions.assertEquals(details.getUser().getId(), userDetails.getUser().getId());

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void getUserDetailsByIdTest() {
        LogManager.getLogger(org.hibernate.Version.class);
        UserDetails details = new UserDetails("Іван",
                "Петренко",
                Gender.MALE,
                new Date(1980, 1, 1),
                "Київ, вул. Шевченка 10",
                user);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(details);
            em.persist(details);
            em.getTransaction().commit();
            UserDetails userDetails = userDetailsOrmDao.getUserDetailsById(1);
            Assertions.assertEquals(details.getFirstName(), userDetails.getFirstName());
            Assertions.assertEquals(details.getLastName(), userDetails.getLastName());
            Assertions.assertEquals(details.getGender(), userDetails.getGender());
            Assertions.assertEquals(details.getDateOfBirth(), userDetails.getDateOfBirth());
            Assertions.assertEquals(details.getAddress(), userDetails.getAddress());
            Assertions.assertEquals(details.getUser().getId(), userDetails.getUser().getId());
        } catch (Exception e) {
            logger.error(e);
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();

            }
        }
    }

    @Test
    public void testUpdateUserDetails() {
        UserDetails userDetails = new UserDetails("Іван",
                "Петренко",
                Gender.MALE,
                new Date(1980, 1, 1),
                "Київ, вул. Шевченка 10",
                user);
        UserDetails updateUserDetails = new UserDetails("Педро",
                "Іванович",
                Gender.MALE,
                new java.sql.Date(1980, 12, 19),
                "Київ, вул. Бандери 10",
                user);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(userDetails);
            em.persist(userDetails);
            em.getTransaction().commit();
            em.getTransaction().begin();
            userDetailsOrmDao.updateUserDetails(updateUserDetails);
            em.getTransaction().commit();
            UserDetails userDetails1 = em.find(UserDetails.class, 1);


            Assertions.assertEquals(updateUserDetails.getFirstName(), userDetails1.getFirstName());
            Assertions.assertEquals(updateUserDetails.getLastName(), userDetails1.getLastName());
            Assertions.assertEquals(updateUserDetails.getGender(), userDetails1.getGender());
            Assertions.assertEquals(updateUserDetails.getDateOfBirth(), userDetails1.getDateOfBirth());
            Assertions.assertEquals(updateUserDetails.getAddress(), userDetails1.getAddress());
            Assertions.assertEquals(updateUserDetails.getUser().getId(), userDetails1.getUser().getId());
        } catch (Exception e) {
            logger.error(e);
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}