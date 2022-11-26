package me.efco.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    public DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/genericbot", "postgres", "admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }
}
