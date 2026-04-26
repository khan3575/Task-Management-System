package service;

import dao.UserDAO;
import dto.UserDTO;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserService {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserDTO login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.err.println("UserService.login: username is blank");
            return null;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("UserService.login: password is blank");
            return null;
        }

        User user = userDAO.findByUsername(username.trim());

        if (user == null) {
            System.err.println("UserService.login: no user found for username='" + username + "'");
            return null;
        }

        if (!Boolean.TRUE.equals(user.passwordVerification(password))) {
            System.err.println("UserService.login: wrong password for username='" + username + "'");
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

//        String lastLoginDisplay = "Not available";
//        if (user.getCreate_At() != null) {
//            lastLoginDisplay = "Last login: " + user.getCreate_At().format(DISPLAY_FORMAT);
//        }
//        dto.setLastLoginDisplay(lastLoginDisplay);
        
        String lastLogin = LocalDateTime.now().format(DISPLAY_FORMAT);
        dto.setLastLoginDisplay(lastLogin);

        return dto;
    }
}