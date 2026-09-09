package controller.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dto.UserDTO;
import service.UserService;
import util.AppLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
	
	private static final long serialVersionUID = 1L;
	private UserService userService;
	 
	@Override
	public void init() {
		userService = new UserService();
		logger.info("loginServlet initialized and userService created");
	}
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	
    	
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
        	logger.warn("Login rejected: Missing username or password fields.");
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
        logger.info("Login attempt for username: {}", username);
        UserDTO user = null;
        
        try{
            user = userService.login(username, password);
//            HttpSession session = request.getSession();
//            session.setAttribute("user", user);
//            
        } catch (Exception e) {
        	
        	
        	logger.error("Unexpected error during login for user: {}", username, e);
            System.err.println("LoginServlet: unexpected error from UserService — " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }

        if(user == null) {
        	logger.warn("Failed login: incorrect username or password for username: {}", username);
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            return;
        }
        
     
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("lastLogin", user.getLastLoginDisplay());
        logger.info("User '{}' logged in successfully. Session ID: {}", username, session.getId());
        
        String path = request.getServletContext().getRealPath("/logs/app.log");
        AppLogger.log(
                    path,
                    "LOGIN",
                    "User Logged in",
                    (String)session.getAttribute("username"),
                    "title= "
        );
        
        response.sendRedirect(request.getContextPath()+"/home");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
        	
        	logger.debug("a session found for user " + session.getAttribute("username")+" Redirecting to home.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
 
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }
}