package jdbcDao;

import dao.jdbc.UserDaoJdbc;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;




public class UserJdbcTest {

    private static final Logger logger = LogManager.getLogger(UserJdbcTest.class);
    UserDaoJdbc userDao = new UserDaoJdbc();


    @Test
    public void testCreateUser(){
        User extentedUser;
        User actualUser;
        extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userDao.createUser(extentedUser);


        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_user = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                actualUser = new User(id_user,
                        username,
                        password,
                        email,
                        phoneNumber);
                assertEquals(extentedUser.getUsername(), actualUser.getUsername());
                logger.info("Пользователь успешно создан");
            }
            rs.close();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testGetUserById(){
        User extentedUser;

        extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, extentedUser.getId());
            stmt.setString(2, extentedUser.getUsername());
            stmt.setString(3, extentedUser.getPassword());
            stmt.setString(4, extentedUser.getEmail());
            stmt.setString(5, extentedUser.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();

            User actualUser = userDao.getUserById(1);
            assertEquals(extentedUser.getUsername(), actualUser.getUsername());

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();


        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
        logger.info("Пользователь успешно создан");

    }



    @Test
    public void testUpdateUser(){

        User extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        User updateExtentedUser = new User(1,
                "Roman",
                "wed436t34grh437tre34tg524",
                "Roman@example.com",
                "Roman");


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (id, username, password, email, phone_number) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, extentedUser.getId());
            stmt.setString(2, extentedUser.getUsername());
            stmt.setString(3, extentedUser.getPassword());
            stmt.setString(4, extentedUser.getEmail());
            stmt.setString(5, extentedUser.getPhoneNumber());
            stmt.executeUpdate();
            stmt.close();

            userDao.updateUser(updateExtentedUser);

            stmt = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            stmt.setInt(1, updateExtentedUser.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_user = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                User actualUser = new User(id_user,
                        username,
                        password,
                        email,
                        phoneNumber);
                assertEquals(updateExtentedUser.getUsername(), actualUser.getUsername());
            }
            rs.close();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement("ALTER TABLE product AUTO_INCREMENT = 1;");
            stmt.executeUpdate();
            stmt.close();


        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
        logger.info("Пользователь успешно создан");
    }



    @Test
    public void testDeleteUser(){
        List<User> users = userDao.getAllUsers();
        for (User user: users) {
            userDao.deleteUser(user);
        }
        for (int i = 0; i < 10; i++){
            assertNull(userDao.getUserById(i));
        }

    }

}