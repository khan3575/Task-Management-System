package util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {

    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    private static DBConnection instance;
    private DataSource dataSource;

    private DBConnection() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");

            dataSource = (DataSource) envContext.lookup("jdbc/TaskDB");

            logger.info("JNDI DataSource initialized successfully");

        } catch (NamingException e) {
            logger.fatal("JNDI lookup failed for jdbc/TaskDB", e);
            throw new RuntimeException("JNDI DataSource initialization failed", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            Connection conn = dataSource.getConnection();
            logger.debug("Database connection retrieved from pool");
            return conn;

        } catch (SQLException e) {
            logger.error("Failed to get DB connection from pool", e);
            throw new RuntimeException("Failed to get DB connection", e);
        }
    }
}