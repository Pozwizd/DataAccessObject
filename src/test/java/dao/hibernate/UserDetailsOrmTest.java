package dao.hibernate;

import Entity.Gender;
import Entity.User;
import Entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import utils.ConnectionPool;
import utils.ConnectionPoolForOrm;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDetailsOrmTest {

    private static final Logger logger = LogManager.getLogger(UserDetailsOrmTest.class);

    UserDetailsOrmDao userDetailsOrmDao = new UserDetailsOrmDao();


    @AfterEach
    public void deleteAfterTest() {
        try (Connection connection = ConnectionPoolForOrm.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM user_details");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE user_details AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            logger.error("Error in deleteAfterTest", e);
        }
    }


    @Test
    public void testCreateUserDetails() {

        User user = new User(
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        UserDetails userDetails = new UserDetails(
                user,
                "Іван",
                "Петренко",
                Gender.MALE,
                LocalDate.of(1980, 1, 1),
                "Київ, вул. Шевченка 10");



        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();
            userDetailsOrmDao.createUserDetails(userDetails);
            em = EntityManagerUtil.getEntityManager();
            User userFromDB = em.find(User.class, 1L);
            UserDetails userDetailsFromDB = em.find(UserDetails.class, userFromDB.getId());
            Assertions.assertEquals(userDetails.getFirstName(), userDetailsFromDB.getFirstName());
            Assertions.assertEquals(userDetails.getLastName(), userDetailsFromDB.getLastName());
            Assertions.assertEquals(userDetails.getGender(), userDetailsFromDB.getGender());
            Assertions.assertEquals(userDetails.getDateOfBirth(), userDetailsFromDB.getDateOfBirth());
            Assertions.assertEquals(userDetails.getAddress(), userDetailsFromDB.getAddress());
            Assertions.assertEquals(userDetails.getUser().getId(), userDetailsFromDB.getUser().getId());
            logger.info("UserDetails created successfully");
            em.close();
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

        User user = new User(
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        UserDetails userDetails = new UserDetails(user, "Іван",
                "Петренко",
                Gender.MALE,
                LocalDate.of(1980, 1, 1),
                "Київ, вул. Шевченка 10");


        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.persist(userDetails);
            em.getTransaction().commit();
            em.close();
            UserDetails userDetailsFromDB = userDetailsOrmDao.getUserDetailsById(1);
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Assertions.assertEquals(userDetails.getFirstName(), userDetailsFromDB.getFirstName());
            Assertions.assertEquals(userDetails.getLastName(), userDetailsFromDB.getLastName());
            Assertions.assertEquals(userDetails.getGender(), userDetailsFromDB.getGender());
            Assertions.assertEquals(userDetails.getDateOfBirth(), userDetailsFromDB.getDateOfBirth());
            Assertions.assertEquals(userDetails.getAddress(), userDetailsFromDB.getAddress());
            Assertions.assertEquals(userDetails.getUser().getId(), userDetailsFromDB.getUser().getId());
            logger.info("UserDetails retrieved by id successfully");
        } catch (Exception e) {
            logger.error("UserDetails error retrieved by id", e);
        } finally {
            if (em != null) {
                em.close();

            }
        }
    }

    @Test
    public void testUpdateUserDetails() {

        User user = new User(
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        UserDetails userDetails = new UserDetails(user,
                "Іван",
                "Петренко",
                Gender.MALE,
                LocalDate.of(1980, 1, 1),
                "Київ, вул. Шевченка 10");

        UserDetails updateUserDetails = new UserDetails(user,
                "Педро",
                "Іванович",
                Gender.MALE,
                LocalDate.of(1980, 12, 19),
                "Київ, вул. Бандери 10"
        );



        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.persist(userDetails);
            em.getTransaction().commit();
            em.close();
            userDetailsOrmDao.updateUserDetails(updateUserDetails);
            em = EntityManagerUtil.getEntityManager();
            UserDetails userDetails1 = em.find(UserDetails.class, 1L);
            Assertions.assertEquals(updateUserDetails.getFirstName(), userDetails1.getFirstName());
            Assertions.assertEquals(updateUserDetails.getLastName(), userDetails1.getLastName());
            Assertions.assertEquals(updateUserDetails.getGender(), userDetails1.getGender());
            Assertions.assertEquals(updateUserDetails.getDateOfBirth(), userDetails1.getDateOfBirth());
            Assertions.assertEquals(updateUserDetails.getAddress(), userDetails1.getAddress());
            Assertions.assertEquals(updateUserDetails.getUser().getId(), userDetails1.getUser().getId());
            logger.info("UserDetails updated successfully");
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error("UserDetails error updated", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}