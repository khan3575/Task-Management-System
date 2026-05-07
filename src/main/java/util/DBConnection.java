package util;
 
import java.sql.Connection;
/*import java.sql.DriverManager;*/
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class DBConnection {
	private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
	private static DataSource dataSource;
	
	static {
		
			try {
				logger.info("initializing JNDI DataSource for jdbc/TaskDB ");
				Context initial = new InitialContext();
				Context env = (Context) initial.lookup("java:/comp/env");
				dataSource = (DataSource) env.lookup("jdbc/TaskDB");
				logger.info("DataSource successfully initialized.");
				
			} catch (Exception e) {
				logger.error("Error: JNDI failed for jdbc/TaskDB ", e);
				throw new RuntimeException("JDNI failed ",e);
			}
		}
	
	public static Connection getConnection() throws SQLException
	{
		try {
            logger.debug(" Attempting database connection from DataSource");
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Failed database connection", e);
            throw e;
        }
	}
 
	/*
	 * private static volatile DBConnection instance;
	 * 
	 * private Connection connection;
	 * 
	 * private static final String URL =
	 * "jdbc:mysql://localhost:3306/task_management_system"; private static final
	 * String USERNAME = "root";
	 * 
	 * private static final String PASSWORD = "root";
	 * 
	 * private DBConnection() { try { Class.forName("com.mysql.cj.jdbc.Driver");
	 * this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	 * System.out.println("Database created and connected.");
	 * 
	 * } catch (ClassNotFoundException | SQLException e) {
	 * System.err.println("Database connection failed"); throw new
	 * RuntimeException(e); } }
	 * 
	 * 
	 * public static DBConnection getInstance() { if (instance == null) {
	 * synchronized (DBConnection.class) { if (instance == null) instance = new
	 * DBConnection(); } } return instance; }
	 * 
	 * 
	 * public Connection getConnection() throws SQLException { return
	 * DriverManager.getConnection(URL,USERNAME,PASSWORD); }
	 */
  
 
}