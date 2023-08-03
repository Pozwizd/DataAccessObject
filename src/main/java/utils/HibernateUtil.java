package utils;

import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory = null;

    static {
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserDetails.class);
        cfg.addAnnotatedClass(Product.class);
        cfg.addAnnotatedClass(Order.class);
        cfg.addAnnotatedClass(ShoppingCart.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}