package OrmDao;

import dao.hibernate.UserDetailsOrmDao;
import models.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import utils.HibernateUtil;

import java.util.Date;

public class UserDetailsOrmTest {


    @Test
    public void testCreateUserDetails() {
        LogManager.getLogger(org.hibernate.Version.class);
        SessionFactory factory = HibernateUtil.getSessionFactory();
        UserDetailsOrmDao dao = new UserDetailsOrmDao(factory);

        UserDetails details = new UserDetails("Іван",
                "Петренко",
                "male",
                new Date(1980,1,1),
                "Київ, вул. Шевченка 10");

        dao.createUserDetails(details);

        UserDetails saved = dao.getUserDetailsById(details.getUserId());

        Assert.assertEquals(details.getFirstName(), saved.getFirstName());
        Assert.assertEquals(details.getLastName(), saved.getLastName());
        Assert.assertEquals(details.getGender(), saved.getGender());

    }

}
