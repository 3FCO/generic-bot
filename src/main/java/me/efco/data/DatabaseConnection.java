package me.efco.data;

import java.sql.*;

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

    public void userWarned(String userId, String userName, String adminId, String adminName, String reason) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO warnings (user_id, user_name, admin_id, admin_name, reason) VALUES (?,?,?,?,?);")) {
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

    public int getUserActiveWarnings(String userId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as warnings FROM warnings WHERE user_id=? AND active=true;")) {
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("warnings");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public void resetUserWarnings(String userId) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE warnings SET active=false WHERE user_id=?;")) {
            statement.setString(1, userId);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
