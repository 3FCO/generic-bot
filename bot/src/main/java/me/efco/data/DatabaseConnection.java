package me.efco.data;

import me.efco.GiveawayHandler;
import me.efco.body.GiveawayBody;
import me.efco.body.WarnBody;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public void userKicked(long userId, String userName, long adminId, String adminName, String reason) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO kicks (user_id, user_name, admin_id, admin_name, reason) VALUES (?,?,?,?,?);")) {
            statement.setLong(1, userId);
            statement.setString(2, userName);
            statement.setLong(3, adminId);
            statement.setString(4, adminName);
            statement.setString(5, reason);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void userBanned(long userId, String userName, long adminId, String adminName, String reason, int duration) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO bans (user_id, user_name, admin_id, admin_name, reason, duration) VALUES (?,?,?,?,?,?);")) {
            statement.setLong(1, userId);
            statement.setString(2, userName);
            statement.setLong(3, adminId);
            statement.setString(4, adminName);
            statement.setString(5, reason);
            statement.setInt(6, duration);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void userWarned(long userId, String userName, long adminId, String adminName, String reason) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO warnings (user_id, user_name, admin_id, admin_name, reason) VALUES (?,?,?,?,?);")) {
            statement.setLong(1, userId);
            statement.setString(2, userName);
            statement.setLong(3, adminId);
            statement.setString(4, adminName);
            statement.setString(5, reason);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserActiveWarningsCount(long userId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as warnings FROM warnings WHERE user_id=? AND active=true;")) {
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("warnings");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public void resetUserWarnings(long userId) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE warnings SET active=false WHERE user_id=?;")) {
            statement.setLong(1, userId);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WarnBody> getUserActiveWarnings(long userId) {
        List<WarnBody> warnings = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warnings WHERE user_id=? AND active=true;")) {
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                warnings.add(new WarnBody(
                        resultSet.getInt("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getLong("admin_id"),
                        resultSet.getString("admin_name"),
                        resultSet.getString("reason"),
                        resultSet.getBoolean("active")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return warnings;
    }

    public List<WarnBody> getUserAllWarnings(long userId) {
        List<WarnBody> warnings = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM warnings WHERE user_id=?;")) {
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                warnings.add(new WarnBody(
                        resultSet.getInt("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getLong("admin_id"),
                        resultSet.getString("admin_name"),
                        resultSet.getString("reason"),
                        resultSet.getBoolean("active")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return warnings;
    }

    public void removeWarningById(int warnId) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE warnings SET active=false WHERE id=?;")) {
            statement.setInt(1, warnId);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertNewGiveaway(long id, String price, long ending, int winnersAmount) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO giveaways (id, price, end_time, winner_amount) VALUES (?,?,?,?);")) {
            statement.setLong(1, id);
            statement.setString(2, price);
            statement.setLong(3, ending);
            statement.setInt(4, winnersAmount);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void endGiveaway(long id, ArrayList<Long> winners) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE giveaways SET active=FALSE, winners=? WHERE id=?;")) {
            statement.setArray(1, connection.createArrayOf("bigint", winners.toArray()));
            statement.setLong(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void userJoinGiveaway(long userId, long giveawayId) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO giveaway_entries (user_id, giveaway_id) VALUES (?,?);")) {
            statement.setLong(1, userId);
            statement.setLong(2, giveawayId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Long, GiveawayBody> getActiveGiveaways() {
        HashMap<Long, GiveawayBody> giveaways = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT id, price, end_time, winner_amount, array_agg(user_id) as entries FROM giveaways LEFT JOIN giveaway_entries on id = giveaway_id WHERE active=TRUE GROUP BY id;")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long[] entriesArray = ((Long[]) resultSet.getArray("entries").getArray());
                if (entriesArray[0] == null) { //If no one enters, the database return an array with 1 entry => {null}
                    entriesArray = new Long[]{};
                }

                giveaways.put(resultSet.getLong("id"),
                        new GiveawayBody(
                                resultSet.getLong("id"),
                                resultSet.getString("price"),
                                resultSet.getLong("end_time"),
                                resultSet.getInt("winner_amount"),
                                new ArrayList<>(List.of(entriesArray))
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return giveaways;
    }
}
