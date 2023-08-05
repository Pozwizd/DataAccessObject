package OrmDao;

import dao.hibernate.UserOrmDao;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;
import utils.HibernateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


public class UserOrmTest {
    private static final Logger logger = LogManager.getLogger(UserOrmTest.class);

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    UserOrmDao userOrmDao = new UserOrmDao(sessionFactory);


    @AfterEach
    public void deleteAfterTest(){
        try(Connection connection = ConnectionPool.getConnection()) {

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
    public void testCreateUser() {
        LogManager.getLogger(org.hibernate.Version.class);

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");


        userOrmDao.createUser(user);
        User savedUser = userOrmDao.getUserById(user.getId());
        System.out.println(savedUser.getUsername());
        assertEquals(1, savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

}
