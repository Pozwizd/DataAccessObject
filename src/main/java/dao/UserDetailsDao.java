package dao;

import models.User;
import models.UserDetails;

import java.util.List;

public interface UserDetailsDao {

    // Получить детали пользователя по id
    UserDetails getUserDetailsById(int userId);

    // Обновить детали пользователя
    void updateUserDetails(int userId, UserDetails userDetails);

    // Создать новые детали для пользователя
    void createUserDetails(int userId, UserDetails details);

}