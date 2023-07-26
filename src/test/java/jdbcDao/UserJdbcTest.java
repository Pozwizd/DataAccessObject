package jdbcDao;

import dao.jdbc.UserDaoJdbc;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;




public class UserJdbcTest {

    private static final Logger logger = LogManager.getLogger(UserJdbcTest.class);
    UserDaoJdbc userDao = new UserDaoJdbc();
    User testUser;
    List<User> testUsers;


    @Test
    public void testCreateUser(){

                User user1;

        user1 = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userDao.createUser(user1);
        logger.info("Пользователь успешно создан");
        User user = userDao.getUserById(1);

        assertEquals(user.getUsername(), user1.getUsername());
    }





}