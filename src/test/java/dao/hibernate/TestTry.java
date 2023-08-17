package dao.hibernate;

import Entity.Gender;
import Entity.User;
import Entity.UserDetails;
import org.junit.jupiter.api.Test;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;

public class TestTry {

    @Test
    void test() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        entityManager.getTransaction().begin();

        User user = new User("user1",
                "userPassword1",
                "user1@example.com",
                "123456");

        UserDetails userDetails = new UserDetails(user,"Іван",
                "Петренко",
                Gender.MALE,
                LocalDate.of(1980, 1, 1),
                "Київ, вул. Шевченка 10"
                );


        entityManager.persist(user);
        entityManager.persist(userDetails);

        entityManager.getTransaction().commit();
        System.out.println(entityManager.find(UserDetails.class, 1L));
        entityManager.close();

    }
}
