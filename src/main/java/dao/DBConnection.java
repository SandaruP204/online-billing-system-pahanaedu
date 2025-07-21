package dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class DBConnection {
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
}
