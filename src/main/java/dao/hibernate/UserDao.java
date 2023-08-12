package dao.hibernate;

import Entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    void createUser(User user);

    void updateUser(User user);

    User getUserById(long id);

    List<User> getAllUsers() throws SQLException;

    void deleteUser(User user);
}
