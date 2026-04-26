package util;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class DBConnection {
 
    private static volatile DBConnection instance;
 
    private Connection connection;
 
    private static final String URL = "jdbc:mysql://localhost:3306/task_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root1234";
 
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database created and connected.");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed");
            throw new RuntimeException(e);
        }
    }
 
   
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) instance = new DBConnection();
            }
        }
        return instance;
    }
 
   
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
 
    
     // one shared connection causing issues
//    public void closeConnection() {
//        if (connection != null) {
//            try {
//                connection.close();
//                System.out.println("Database connection closed.");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
 
}