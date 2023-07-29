package dao;

import models.UserDetails;

public interface UserDetailsDao {

    void createUserDetails(int userId, UserDetails details);

    UserDetails getUserDetailsById(int userId);

    void updateUserDetails(int userId, UserDetails userDetails);



}