package dao.hibernate;

import Entity.Gender;
import Entity.User;
import Entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import utils.ConnectionPool;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDetailsOrmTest {

    private static final Logger logger = LogManager.getLogger(UserDetailsOrmTest.class);

    UserDetailsOrmDao userDetailsOrmDao = new UserDetailsOrmDao();

    private static final User user = new User(1,
            "user1",
            "userPassword1",
            "user1@example.com",
            "123456");

    private final  UserDetails userDetails = new UserDetails("Іван",
            "Петренко",
            Gender.MALE,
            LocalDate.of(1980, 1, 1),
            "Київ, вул. Шевченка 10",
            user);
    private final  UserDetails updateUserDetails = new UserDetails("Педро",
            "Іванович",
            Gender.MALE,
            LocalDate.of(1980, 12, 19),
            "Київ, вул. Бандери 10",
            user);


    @BeforeAll
    public static void createUserBeforeUserDetailsTest() {

        EntityManager em = null;

        try {

            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(user);

            em.getTransaction().commit();

        } catch (Exception e) {
            if(em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if(em != null) {
                em.close();
            }
        }

    }

    @AfterAll
    public static void deleteAfterTest() {
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

        userDetailsOrmDao.createUserDetails(userDetails);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            UserDetails userDetailsFromDB = em.find(UserDetails.class, 1L);
            Assertions.assertEquals(userDetails.getFirstName(), userDetailsFromDB.getFirstName());
            Assertions.assertEquals(userDetails.getLastName(), userDetailsFromDB.getLastName());
            Assertions.assertEquals(userDetails.getGender(), userDetailsFromDB.getGender());
            Assertions.assertEquals(userDetails.getDateOfBirth(), userDetailsFromDB.getDateOfBirth());
            Assertions.assertEquals(userDetails.getAddress(), userDetailsFromDB.getAddress());
            Assertions.assertEquals(userDetails.getUser().getId(), userDetailsFromDB.getUser().getId());
        } catch (Exception e) {
            logger.error(e);
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void getUserDetailsByIdTest() {
        LogManager.getLogger(org.hibernate.Version.class);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            UserDetails userDetailsFromDB = userDetailsOrmDao.getUserDetailsById(1);
            Assertions.assertEquals(userDetails.getFirstName(), userDetailsFromDB.getFirstName());
            Assertions.assertEquals(userDetails.getLastName(), userDetailsFromDB.getLastName());
            Assertions.assertEquals(userDetails.getGender(), userDetailsFromDB.getGender());
            Assertions.assertEquals(userDetails.getDateOfBirth(), userDetailsFromDB.getDateOfBirth());
            Assertions.assertEquals(userDetails.getAddress(), userDetailsFromDB.getAddress());
            Assertions.assertEquals(userDetails.getUser().getId(), userDetailsFromDB.getUser().getId());
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();

            }
        }
    }

    @Test
    public void testUpdateUserDetails() {

        userDetailsOrmDao.updateUserDetails(updateUserDetails);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            UserDetails userDetails1 = em.find(UserDetails.class, 1);
            Assertions.assertEquals(updateUserDetails.getFirstName(), userDetails1.getFirstName());
            Assertions.assertEquals(updateUserDetails.getLastName(), userDetails1.getLastName());
            Assertions.assertEquals(updateUserDetails.getGender(), userDetails1.getGender());
            Assertions.assertEquals(updateUserDetails.getDateOfBirth(), userDetails1.getDateOfBirth());
            Assertions.assertEquals(updateUserDetails.getAddress(), userDetails1.getAddress());
            Assertions.assertEquals(updateUserDetails.getUser().getId(), userDetails1.getUser().getId());
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}