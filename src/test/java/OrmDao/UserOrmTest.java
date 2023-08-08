package OrmDao;

import dao.hibernate.UserDaoJpa;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import utils.EntityManagerUtil;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@Transactional
public class UserOrmTest {
    private static final Logger logger = LogManager.getLogger(UserOrmTest.class);

    UserDaoJpa userDaoJpa = new UserDaoJpa();


    @Rollback(true)
    @Test
    public void testCreateUser() {
        LogManager.getLogger(org.hibernate.Version.class);

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userDaoJpa.createUser(user);

        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            User savedUser = em.find(User.class, user.getId());
            logger.info(savedUser.getUsername());
            assertEquals(1, savedUser.getId());
            assertEquals(user.getEmail(), savedUser.getEmail());
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Rollback(true)
    @Test
    public void testGetUserById() {

        EntityManager em = EntityManagerUtil.getEntityManager();

        em.getTransaction().begin();
        User user = new User(
                "user1",
                "password1",
                "user1@email.com",
                "123456");

        try {



            em.persist(user);
            em.getTransaction().commit(); // сохраняем User

            User foundUser = userDaoJpa.getUserById(1);

            assertEquals(1, foundUser.getId());
            assertEquals("user1@email.com", foundUser.getEmail());

        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }

    }

    @Rollback(true)
    @Test
    public void testUpdateUser() {

        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        try {

            // Создать нового пользователя
            User user = new User(
                    "user1",
                    "userPassword1",
                    "user1@example.com",
                    "123456");
            em.persist(user);

            // Обновить данные того же пользователя
            User updateUser = new User(1,
                    "Roman",
                    "newPassword",
                    "newEmail@example.com",
                    "Roman");

            em.merge(updateUser);

            // Найти обновленного пользователя
            User mergedUser = em.find(User.class, 1);
            em.getTransaction().commit();

            // Проверки
            assertEquals(updateUser.getUsername(), mergedUser.getUsername());
            assertEquals(updateUser.getEmail(), mergedUser.getEmail());

        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }



    }

    @Rollback(true)
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
            User actualUser = em.find(User.class, 1);
            assertNull(actualUser);
        } finally {
            if(em != null) {
                em.close();
            }
        }


    }

}
