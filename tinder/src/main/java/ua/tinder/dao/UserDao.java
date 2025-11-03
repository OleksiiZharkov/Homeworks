package ua.tinder.dao;

import ua.tinder.db.DbConnection;
import ua.tinder.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    public Optional<User> getByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, name, photo_url FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DbConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> getNextUser(int currentUserId, int lastViewedId) {
        String sql = "SELECT * FROM users " +
                "WHERE id != ? AND id > ? " +
                "AND id NOT IN (SELECT user_to_id FROM likes WHERE user_from_id = ?) " +
                "ORDER BY id ASC LIMIT 1";
        try (Connection conn = DbConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ps.setInt(2, lastViewedId);
            ps.setInt(3, currentUserId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> getLikedUsers(int currentUserId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM users u " +
                "JOIN likes l ON u.id = l.user_to_id " +
                "WHERE l.user_from_id = ?";
        try (Connection conn = DbConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Optional<User> getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DbConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void addLike(int fromId, int toId) {
        String sql = "INSERT INTO likes (user_from_id, user_to_id) VALUES (?, ?)";
        try (Connection conn = DbConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("23505")) {
                e.printStackTrace();
            }
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("photo_url")
        );
    }
}