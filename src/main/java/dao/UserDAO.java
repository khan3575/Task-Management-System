package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO {

	private static final Logger logger =
	        LoggerFactory.getLogger(UserDAO.class);

    public User findByUsername(String username) {

        String sql = "SELECT id, username, email, password FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    logger.debug("User found for username: {}", username);
                    return user;
                }
            }

            
            logger.info("No user found for username: {}", username);

        } catch (SQLException e) {
            logger.error("DB error in findByUsername for username: {}", username, e);
        }

        return null;
    }
}