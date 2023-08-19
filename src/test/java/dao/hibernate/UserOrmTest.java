package dao.hibernate;

import Entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.ConnectionPoolForOrm;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class UserOrmTest {
    private static final Logger logger = LogManager.getLogger(UserOrmTest.class);

    UserDaoJpa userDaoJpa = new UserDaoJpa();

    @AfterEach
    public void deleteAfterTest(){
        try(Connection connection = ConnectionPoolForOrm.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE users AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.error("Error in deleteAfterTest", e);
        }
    }

    @Test
    public void testCreateUser() {
        LogManager.getLogger(org.hibernate.Version.class);

        User user = new User(1L,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userDaoJpa.createUser(user);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            User savedUser = em.find(User.class, user.getId());
            assertEquals(user.getId(), savedUser.getId());
            assertEquals(user.getUsername(), savedUser.getUsername());
            assertEquals(user.getPassword(), savedUser.getPassword());
            assertEquals(user.getEmail(), savedUser.getEmail());
            assertEquals(user.getPhone_number(), savedUser.getPhone_number());
            logger.info("User successfully created");
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error("Error during creating user", e);
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testGetUserById() {
        User user = new User(1L,
                "user1",
                "password1",
                "user1@email.com",
                "123456");
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            User user1 = em.merge(user);
            em.persist(user1);
            em.getTransaction().commit();
            User foundUser = userDaoJpa.getUserById(user.getId());
            assertEquals(user.getId(), foundUser.getId());
            assertEquals(user.getUsername(), foundUser.getUsername());
            assertEquals(user.getPassword(), foundUser.getPassword());
            assertEquals(user.getEmail(), foundUser.getEmail());
            assertEquals(user.getPhone_number(), foundUser.getPhone_number());
            logger.info("User successfully retrieved by id");
        } catch (Exception ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error("Error during retrieving user by id", ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    public void testUpdateUser() {

        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        try {
            User user = new User(
                    "user1",
                    "userPassword1",
                    "user1@example.com",
                    "123456");
            em.persist(user);
            em.getTransaction().commit();
            em.close();

            User updateUser = new User(1L,
                    "Roman",
                    "newPassword",
                    "newEmail@example.com",
                    "Roman");
            userDaoJpa.updateUser(updateUser);
            em = EntityManagerUtil.getEntityManager();
            User mergedUser = em.find(User.class, updateUser.getId());
            assertEquals(mergedUser.getUsername(), updateUser.getUsername());
            assertEquals(mergedUser.getPassword(), updateUser.getPassword());
            assertEquals(mergedUser.getEmail(), updateUser.getEmail());
            assertEquals(mergedUser.getPhone_number(), updateUser.getPhone_number());
            logger.info("User successfully updated");
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error("Error during updating user", e);
        } finally {
            em.close();
        }
    }

    @Test
    public void testDeleteUser() {
        LogManager.getLogger(org.hibernate.Version.class);

        User user = new User(
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
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

        userDaoJpa.deleteUser(user);
        em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            User actualUser = em.find(User.class, 1L);
            assertNull(actualUser);
            logger.info("User deleted successfully");
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            logger.error("Error during deleting user", e);
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }
}
