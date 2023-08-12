package dao.jdbc;

import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJdbc implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoJdbc.class);

    private Connection connection;

    public UserDaoJdbc() {

    }


    @Override
    public void createUser(User user) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users ( username, password, email, phone_number) VALUES (?, ?, ?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone_number());
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }

    }

    @Override
    public User getUserById(int id) {

        User user = null;
        try(Connection connection = ConnectionPool.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id_user = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");;
                String phoneNumber = rs.getString("phone_number");
                user = new User(id_user,
                        username,
                        password,
                        email,
                        phoneNumber);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void updateUser(User user) {


        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET id = ?, username = ?, password = ?, email = ?, phone_number = ? WHERE id = ?");

            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone_number());
            stmt.setInt(6, user.getId());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }

    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM shop.users");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");;
                String phoneNumber = rs.getString("phone_number");
                User user = new User(id,
                        username,
                        password,
                        email,
                        phoneNumber);
                users.add(user);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех пользователей", e);
        }
        return users;
    }

    @Override
    public void deleteUser(User user) {

        try(Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM shop.user_details WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

            stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }
    }

}