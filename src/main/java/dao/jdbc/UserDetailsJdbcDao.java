package dao.jdbc;

import dao.UserDetailsDao;
import models.UserDetails;
import utils.ConnectionPool;

import java.sql.*;

public class UserDetailsJdbcDao implements UserDetailsDao {


    @Override
    public void createUserDetails(int userId, UserDetails details) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO user_details (user_id, first_name, last_name, date_of_birth, address) " +
                            "VALUES (?, ?, ?, ?, ?)");

            stmt.setInt(1, userId);
            stmt.setString(2, details.getFirstName());
            stmt.setString(3, details.getLastName());
            stmt.setDate(4, details.getDateOfBirth());
            stmt.setString(5, details.getAddress());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания user details", e);
        } finally {

        }
    }

    public UserDetails getUserDetailsById(int userId) {


        UserDetails userDetails = null;
        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user_details WHERE user_id=?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                Date dateOfBirth = rs.getDate("dateOfBirth");
                String address = rs.getString("address");
                userDetails = new UserDetails(firstName, lastName, gender, dateOfBirth, address);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return userDetails;
    }

    @Override
    public void updateUserDetails(int userId, UserDetails userDetails) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE user_details " +
                            "SET first_name = ?, last_name = ?, date_of_birth = ?, address = ? " +
                            "WHERE user_id = ?");

            stmt.setString(1, userDetails.getFirstName());
            stmt.setString(2, userDetails.getLastName());
            stmt.setDate(3, userDetails.getDateOfBirth());
            stmt.setString(4, userDetails.getAddress());
            stmt.setInt(5, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления деталей пользователя", e);
        }

    }
}