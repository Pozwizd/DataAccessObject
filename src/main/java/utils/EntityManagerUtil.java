package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;

public class EntityManagerUtil {

    private static EntityManagerFactory emf;

    static {
        emf = createEntityManagerFactory("myApp");
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}