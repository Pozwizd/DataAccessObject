package dao;

import models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    List<User> getAllUsers() throws SQLException;

    User getUserById(int id);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(User user);
}
