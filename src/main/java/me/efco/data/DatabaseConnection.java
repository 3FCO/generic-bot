package me.efco.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    public void userKicked(String userId, String userName, String reason, String adminId, String adminName) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO kicks (user_id, user_name, admin_id, admin_name, reason) VALUES (?,?,?,?,?);")) {
            statement.setString(1, userId);
            statement.setString(2, userName);
            statement.setString(3, adminId);
            statement.setString(4, adminName);
            statement.setString(5, reason);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void userBanned(String userId, String userName, String adminId, String adminName, String reason, int duration) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO bans (user_id, user_name, admin_id, admin_name, reason, duration) VALUES (?,?,?,?,?,?);")) {
            statement.setString(1, userId);
            statement.setString(2, userName);
            statement.setString(3, adminId);
            statement.setString(4, adminName);
            statement.setString(5, reason);
            statement.setInt(6, duration);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
