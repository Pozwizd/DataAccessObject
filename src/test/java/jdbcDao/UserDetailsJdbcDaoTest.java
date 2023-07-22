package jdbcDao;

import dao.jdbc.UserDetailsJdbcDao;
import models.User;
import models.UserDetails;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsJdbcDaoTest {

    UserDetailsJdbcDao UserDetailsDao = new UserDetailsJdbcDao();
    User testUser;

    @Test
    @Before
    public void testCreateUserDetails() {
        UserDetails userDetails = new UserDetails("Іван", "Петренко", "male", new Date(1980,1,1), "Київ, вул. Шевченка 10");
        UserDetailsDao.createUserDetails(1, userDetails);
        assertEquals(userDetails, UserDetailsDao.getUserDetailsById(1));
    }

    @Test
    public void testGetUserDetailsById() {
        UserDetails userDetails = new UserDetails("Іван", "Петренко", "male", new Date(1980,1,1), "Київ, вул. Шевченка 10");
        assertEquals(userDetails, UserDetailsDao.getUserDetailsById(1));
    }

    @Test
    public void testUpdateUserDetails() {
        UserDetails userDetails = new UserDetails("Марія", "Ковальчук", "female", new Date(1990,5,6), "Львів, вул. Франка 5");
        UserDetailsDao.updateUserDetails(1,userDetails);
        assertEquals(userDetails, UserDetailsDao.getUserDetailsById(1));
    }
}
