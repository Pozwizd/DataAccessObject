package dao.jdbc;

import dao.UserDao;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DBConnection;

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


    public void openConnection() {
        DBConnection dbConnection = DBConnection.getInstance();
        connection = dbConnection.getConnection();
    }

    public void closeConnection() {
        DBConnection.closeConnection(connection);
    }

    @Override
    public void createUser(User user) {

        openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (username, password, email, phone_number) VALUES (?, ?, ?, ?)");

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        } finally {
            closeConnection();
        }

    }

    @Override
    public User getUserById(int id) {

        User user = null;
        openConnection();
        try {
            try {
                boolean isValid = connection.isValid(3); // Проверяем соединение, ждем не более 3 секунд
                if (isValid) {
                    System.out.println("Соединение с базой данных действительно");
                } else {
                    System.out.println("Соединение с базой данных недействительно");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при установлении соединения с базой данных: " + e.getMessage());
            }

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

                rs.close();
                stmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return user;
    }

    @Override
    public void updateUser(User user) {

        openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, email = ?, phone_number = ? WHERE id = ?");

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        } finally {
            closeConnection();
        }

    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        openConnection();
        try {
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
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return users;
    }

    @Override
    public void deleteUser(User user) {

        openConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM shop.user_details WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

            stmt = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        } finally {
            closeConnection();
        }
    }

}