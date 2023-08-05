package OrmDao;

import dao.hibernate.UserDetailsOrmDao;
import models.User;
import models.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.ConnectionPool;
import utils.HibernateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class UserDetailsOrmTest {

    private static final Logger logger = LogManager.getLogger(UserDetailsOrmTest.class);


    @BeforeEach
    public void createUserBeforeUserDetailsTest(){
        User extentedUser = new User(1,
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
            stmt.setString(5, extentedUser.getPhone_number());
            stmt.executeUpdate();
            stmt.close();
            logger.info("The user for the test has been successfully added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
    public void testCreateUserDetails() {
        LogManager.getLogger(org.hibernate.Version.class);
        SessionFactory factory = HibernateUtil.getSessionFactory();
        UserDetailsOrmDao dao = new UserDetailsOrmDao(factory);

        UserDetails details = new UserDetails("Іван",
                "Петренко",
                "male",
                new Date(1980,1,1),
                "Київ, вул. Шевченка 10");

        dao.createUserDetails(details);

        UserDetails saved = dao.getUserDetailsById(1);

        Assert.assertEquals(details.getFirstName(), saved.getFirstName());
        Assert.assertEquals(details.getLastName(), saved.getLastName());
        Assert.assertEquals(details.getGender(), saved.getGender());

    }

}
