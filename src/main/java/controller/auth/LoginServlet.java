package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.UserDTO;
import service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LogManager.getLogger(LoginServlet.class);
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
        logger.info("LoginServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        

        // Validation check
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            logger.warn("Login attempt with missing credentials.");
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        logger.info("Login attempt for user: {} ", username);

        UserDTO user = null;

        try {
            user = userService.login(username, password);

        } catch (Exception e) {
            logger.error("Error during login for user: {}", username, e);

            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // Invalid login
        if (user == null) {
            logger.warn("Invalid login attempt for user: {}", username);

            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        // Successful login
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("lastLogin", user.getLastLoginDisplay());

        logger.info("User logged in successfully: {}", 
                    username);

        response.sendRedirect(request.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            logger.info("User already logged in, redirecting to home");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        logger.info("Accessing login page");
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
}
