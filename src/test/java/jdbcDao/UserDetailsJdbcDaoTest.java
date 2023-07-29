package jdbcDao;

import dao.jdbc.UserDaoJdbc;
import dao.jdbc.UserDetailsJdbcDao;
import models.User;
import models.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import utils.ConnectionPool;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsJdbcDaoTest {

    private static final Logger logger = LogManager.getLogger(UserDetailsJdbcDaoTest.class);
    UserDetailsJdbcDao UserDetailsDao = new UserDetailsJdbcDao();
    UserDaoJdbc userDao = new UserDaoJdbc();
    User testUser;

    @Test
    public void testCreateUserDetails() {
        User extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");
        UserDetails userDetails = new UserDetails("Іван", "Петренко", "male", new Date(1980,1,1), "Київ, вул. Шевченка 10");

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
            UserDetailsDao.createUserDetails(1, userDetails);

            stmt = connection.prepareStatement("SELECT * FROM user_details WHERE user_id=?");
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                Date dateOfBirth = rs.getDate("date_Of_birth");
                String address = rs.getString("address");
                UserDetails actualUserDetails =
                        new UserDetails(firstName,
                        lastName,
                        gender,
                        dateOfBirth,
                        address);
                assertEquals(userDetails.getUserId(), actualUserDetails.getUserId());
                assertEquals(userDetails.getFirstName(), actualUserDetails.getFirstName());
            }
            rs.close();
            stmt.close();

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetUserDetailsById() {
        User extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");
        UserDetails userDetails = new UserDetails("Іван",
                "Петренко",
                "male",
                new Date(1980,1,1),
                "Київ, вул. Шевченка 10");


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

            stmt = connection.prepareStatement(
                    "INSERT INTO user_details (first_name, last_name, date_of_birth, address, user_id) " +
                            "VALUES (?, ?, ?, ?, ?)");

            stmt.setString(1, userDetails.getFirstName());
            stmt.setString(2, userDetails.getLastName());
            stmt.setDate(3, userDetails.getDateOfBirth());
            stmt.setString(4, userDetails.getAddress());
            stmt.setInt(5, 1);
            stmt.executeUpdate();
            stmt.close();

            UserDetails actualUserDetails = UserDetailsDao.getUserDetailsById(1);
            assertEquals(userDetails.getFirstName(), actualUserDetails.getFirstName());
            assertEquals(userDetails.getLastName(), actualUserDetails.getLastName());

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания user details", e);
        }

    }



    @Test
    public void testUpdateUserDetails() {

        User extentedUser = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");
        UserDetails userDetails = new UserDetails("Іван",
                "Петренко",
                "male",
                new Date(1980,1,1),
                "Київ, вул. Шевченка 10");
        UserDetails updateUserDetails = new UserDetails("Іван",
                "Петренко",
                "male",
                new Date(1980,12,19),
                "Київ, вул. Бандери 10");


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

            stmt = connection.prepareStatement(
                    "INSERT INTO user_details (first_name, last_name, date_of_birth, address, user_id) " +
                            "VALUES (?, ?, ?, ?, ?)");

            stmt.setString(1, userDetails.getFirstName());
            stmt.setString(2, userDetails.getLastName());
            stmt.setDate(3, userDetails.getDateOfBirth());
            stmt.setString(4, userDetails.getAddress());
            stmt.setInt(5, 1);
            stmt.executeUpdate();
            stmt.close();

            UserDetailsDao.updateUserDetails(1,updateUserDetails);

            stmt = connection.prepareStatement("SELECT * FROM user_details WHERE user_id=?");
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                Date dateOfBirth = rs.getDate("date_Of_birth");
                String address = rs.getString("address");
                UserDetails actualUserDetails =
                        new UserDetails(firstName,
                                lastName,
                                gender,
                                dateOfBirth,
                                address);
                assertEquals(updateUserDetails.getAddress(), actualUserDetails.getAddress());
                assertEquals(updateUserDetails.getDateOfBirth(), actualUserDetails.getDateOfBirth());
            }

            stmt = connection.prepareStatement("DELETE FROM users");
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания user details", e);
        }
    }
}
