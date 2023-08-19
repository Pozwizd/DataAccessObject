package utils;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionPoolForOrm {

    private static final BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/shop2");
        dataSource.setUsername("root");
        dataSource.setPassword("0000");
        dataSource.setMinIdle(15);
        dataSource.setMaxIdle(25);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
