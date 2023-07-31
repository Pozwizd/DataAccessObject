package HormDao;

import dao.hibernate.UserOrmDao;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import utils.HibernateUtil;


public class UserOrmTest {

    @Test
    public void testCreateUser() {
        LogManager.getLogger(org.hibernate.Version.class);

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserOrmDao userOrmDao = new UserOrmDao(sessionFactory);

        User user = new User(1,
                "user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        userOrmDao.createUser(user);

        // загружаем сохраненного пользователя по id
        User savedUser = userOrmDao.getUserById(user.getId());

        Assert.assertEquals(user.getUsername(), savedUser.getUsername());
        Assert.assertEquals(user.getEmail(), savedUser.getEmail());
    }

}
