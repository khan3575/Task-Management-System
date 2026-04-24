package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) {
        String sql = "SELECT id, username, email, password, created_at FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    
                    if (ts != null) user.setCreated_at(ts.toLocalDateTime());
                    
                    return user;
                }
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.findByUsername: DB error for username=" + username + " " + e.getMessage());
        }

        return null;
    }
}