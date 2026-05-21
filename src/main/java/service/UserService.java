package service;

import dao.UserDAO;
import dto.UserDTO;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

	private static final Logger logger =
	        LoggerFactory.getLogger(UserService.class);

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserDTO login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            logger.warn("Login attempt with blank username");
            return null;
        }

        if (password == null || password.isEmpty()) {
            logger.warn("Login attempt with blank password for username: {}", username);
            return null;
        }

        username = username.trim();

        logger.info("Login attempt for username: {}", username);

        try {
            User user = userDAO.findByUsername(username);

            if (user == null) {
                logger.warn("Login failed - user not found: {}", username);
                return null;
            }

            if (!Boolean.TRUE.equals(user.passwordVerification(password))) {
                logger.warn("Login failed - incorrect password for username: {}", username);
                return null;
            }

            logger.info("Login successful for username: {}", username);

            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());

            String lastLogin = LocalDateTime.now().format(DISPLAY_FORMAT);
            dto.setLastLoginDisplay(lastLogin);

            return dto;

        } catch (Exception e) {
            logger.error("Unexpected error during login for username: {}", username, e);
            return null;
        }
    }
}