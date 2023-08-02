package utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {

            try {

                Configuration configuration = new Configuration();

                // Настройки подключения к БД
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/shop");
                configuration.setProperty("hibernate.connection.username", "root");
                configuration.setProperty("hibernate.connection.password", "0000");

                // Настройки Hibernate
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");


                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.format_sql", "true");
                configuration.setProperty("hibernate.generate_statistics", "true");

// Включаем DEBUG логирование
                configuration.setProperty("hibernate.globally_quoted_identifiers", "true");
                configuration.setProperty("hibernate.use_sql_comments", "true");
                configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

// Уровень логирования
                configuration.setProperty("hibernate.hbm2ddl.auto", "validate");


                // Указываем пакет с классами сущностей
                configuration.addPackage("models");

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.err.println("Исключение при создании SessionFactory");
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }

}