package controller.task;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dto.TaskDTO;
import service.TaskService;
import util.AppLogger;
import validator.TaskValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(AddTaskServlet.class);
    private static final long serialVersionUID = 1L;
    private TaskService taskService;
    private TaskValidator taskValidator;
    
    @Override
    public void init() {
        taskValidator = new TaskValidator();
        System.out.println("AddTaskServlet initialized");
        logger.info("AddTaskServlet initialized");
    }
    
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        System.out.println("Session: " + session);
        System.out.println("Username: " + (session != null ? session.getAttribute("username") : "null"));

        //Not logged in
        if (session == null || session.getAttribute("username") == null) {
        	logger.warn("Unauthorized GET request to /addTask. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        logger.debug("User '{}' is valid acessing add task page.", session.getAttribute("username"));
        request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        // Not logged in
        if (session == null || session.getAttribute("username") == null) {
        	logger.error("Unauthorized POST attempt to add task redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");
        logger.info("User '{}' attempting to add task: '{}'", session.getAttribute("username"), title);
        
        // NEW LOGIC: Validate due date (cannot be before today)
        if(dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                LocalDate today = LocalDate.now();
                LocalDate dueLocalDate = LocalDate.parse(dueDate);
                
                if(dueLocalDate.isBefore(today)) {
                	logger.warn("Task creation has failed. Cause -> Due date '{}' is in the past for user '{}'", dueDate, session.getAttribute("username"));
                    request.setAttribute("error", "Due date cannot be before today's date!");
                    request.setAttribute("dueDateError", "Please select a date that is today or in the future.");
                    request.setAttribute("title", title);
                    request.setAttribute("description", description);
                    request.setAttribute("priority", priority);
                    request.setAttribute("status", status);
                    request.setAttribute("dueDate", dueDate);
                    request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
            	logger.error("Date parsing error for user '{}' with input '{}'", session.getAttribute("username"), dueDate, e);
                request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD format.");
                request.setAttribute("title", title);
                request.setAttribute("description", description);
                request.setAttribute("priority", priority);
                request.setAttribute("status", status);
                request.setAttribute("dueDate", dueDate);
                request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
                return;
            }
        }
        
        // OLD LOGIC: Validate using TaskValidator
        String error = taskValidator.validateAdd(title, description, priority, status, dueDate);
        
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
            return;
        }
        
        //Call service
        taskService = new TaskService();
        boolean isAdded = taskService.addTask(title, description, priority, status, dueDate);
         
        if (isAdded) {
        	
        	String path = request.getServletContext().getRealPath("/logs/app.log");
            AppLogger.log(
                        path,
                        "TASK ADDED",
                        "A new task added",
                        (String)session.getAttribute("username"),
                        "title= "
            );
            // <sakib> changes = context path to addTask and status = success
        	logger.info("Task '{}' successfully created by user '{}'", title, session.getAttribute("username"));
            response.sendRedirect(request.getContextPath() + "/addTask?status=success");
        } else {
        	logger.error("Adding task failed, Database failure : Could not add task '{}' for user '{}'", title, session.getAttribute("username"));
            request.setAttribute("error", "Failed to add task. Please check your due date (cannot be in the past) and try again.");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);
            String path = request.getServletContext().getRealPath("/logs/app.log");
            AppLogger.log(
                        path,
                        "TASK ADDED FAILED",
                        "A attempt of new task add but failed",
                        (String)session.getAttribute("username"),
                        "title= "+ error
            );
            
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
        }
    }
}