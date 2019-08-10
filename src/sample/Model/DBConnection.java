package sample.Model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connection() throws Exception{
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:users.db");
    }
}
