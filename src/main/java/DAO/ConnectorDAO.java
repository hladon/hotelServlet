package DAO;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectorDAO {
    private final static Logger LOGGER = Logger.getLogger(ConnectorDAO.class);

    private static Connection connection;
    private static final String DB_PROPERTY = "connection.url";
    private static final String DB_DRIVER = "driver.class";
    private static final String PROPERTY_FILE = "app.properties";


    public static Connection getConnection() throws SQLException {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(PROPERTY_FILE);
            Properties properties = new Properties();
            properties.load(input);
            Class.forName(properties.getProperty(DB_DRIVER));
            String uriConnection = properties.getProperty(DB_PROPERTY);
            connection = DriverManager.getConnection(uriConnection);
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Wrong properties load! " + e.getMessage());
            throw new SQLException("Wrong properties load! ");
        }


        return connection;
    }
}
