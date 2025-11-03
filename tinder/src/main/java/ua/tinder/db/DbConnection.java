package ua.tinder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection get() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String user = System.getenv("JDBC_DATABASE_USERNAME");
            String pass = System.getenv("JDBC_DATABASE_PASSWORD");


            if (dbUrl == null) {
                dbUrl = "jdbc:postgresql://localhost:5432/tinder_db";
            }
            if (user == null) {
                user = "Oleksii";
            }
            if (pass == null) {
                pass = "123456789";
            }

            return DriverManager.getConnection(dbUrl, user, pass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgresSQL JDBC Driver not found", e);
        }
    }
}