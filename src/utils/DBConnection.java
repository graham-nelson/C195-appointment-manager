package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class used to build database connection.
 * Contains methods to start, close, and get database connection.
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ06czE";

    private static final String jdbcURL = protocol + vendorName + ipAddress;

    private static final String MYSQLJDBCDRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //username
    private static String username = "U06czE";
    //password
    private static String password = "53688729426";

    /**
     * Method for starting connection to database.
     * @return Connection object for database connection
     */
    public static Connection startConnection(){

        try {
            Class.forName(MYSQLJDBCDRIVER);
            System.out.println("Driver Loaded");
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful!");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Method for closing connection to database when program is closed.
     */
    public static void closeConnection(){
        try {
            conn.close();
            System.out.println("Connection Closed.");
        }
        catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Method for retrieving Connection object
     * @return current Connection object connection
     */
    public static Connection getConnection(){
        return conn;
    }
}
