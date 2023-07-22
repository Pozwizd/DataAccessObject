package jdbcDao;

import dao.jdbc.UserDaoJdbc;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach
    @Test
    public void testCreateUser(){


        User user1;

        user1 = new User("user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userDao.createUser(user1);
        logger.info("Пользователь успешно создан");
        assertEquals(user1, userDao.getUserById(1));

    }

    @Test
    public void getUserByIdTest(){
        User user1 = new User(1,
                        "user1",
                        "userPassword1",
                        "user1@example.com",
                        "123456");

        testUser = userDao.getUserById(1);
        logger.info("Пользователь успешно получен по id");
        assertEquals(user1, testUser);
    }

    @Test
    public void testGetAllUsers() throws SQLException {

        List<User> expectedUsers = List.of(
                new User(1,
                        "user1",
                        "userPassword1",
                        "user1@example.com",
                        "123456"),
                new User(2,
                        "user2",
                        "userPassword1",
                        "user2@example.com",
                        "654321") );
        testUsers = userDao.getAllUsers();
        assertEquals(expectedUsers, testUsers);

    }
    @AfterEach
    @Test
    public void testDeleteUser() throws SQLException {
        testUsers = userDao.getAllUsers();
        for (User u: testUsers) {
            userDao.deleteUser(u);
        }
        assertNull(userDao.getAllUsers());
    }

}