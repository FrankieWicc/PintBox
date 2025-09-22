package Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;-

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:https://console.neon.tech/app/projects/wispy-smoke-30012418/branches/br-summer-queen-ac5u9y59/sql-editor?database=neondb";

        try {
            Class.forName("org.postgresql.Driver"); 

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado!", e);
        }

        return DriverManager.getConnection(url);
    }
}
