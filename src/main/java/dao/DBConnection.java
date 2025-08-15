package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // JNDI connection (production)
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            connection = ds.getConnection();
            System.out.println("✅ Got connection from JNDI!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Direct JDBC connection (for tests)
    public static Connection getConnection(String schema) {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/" + schema;
            String username = "root";      // change to your DB user
            String password = "";  // change to your DB password
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Got connection for schema: " + schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
