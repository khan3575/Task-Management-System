package controller.task;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.TaskService;
import validator.TaskValidator;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private TaskService taskService;
    private TaskValidator taskValidator;
    
    @Override
    public void init() {
        taskService = new TaskService();
        taskValidator = new TaskValidator();
        System.out.println("AddTaskServlet initialized");
    }
    
    // Handle GET request - Show add task form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Forward to add task form
        request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
    }
    
    // Handle POST request - Process add task
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("AddTaskServlet: Received POST request");
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");
        
        System.out.println("AddTaskServlet: Title=" + title + ", Priority=" + priority + ", Status=" + status + ", DueDate=" + dueDate);
        
        // Validate due date (cannot be before today)
        if(dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                LocalDate today = LocalDate.now();
                LocalDate dueLocalDate = LocalDate.parse(dueDate);
                
                if(dueLocalDate.isBefore(today)) {
                    System.out.println("AddTaskServlet: Due date validation failed - date is in past");
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
                System.out.println("AddTaskServlet: Date parsing error - " + e.getMessage());
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
        
        // Validate input using TaskValidator
        String validationError = taskValidator.validateAdd(title, description, priority, status, dueDate);
        
        if(validationError != null) {
            System.out.println("AddTaskServlet: Validation failed - " + validationError);
            request.setAttribute("error", validationError);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
            return;
        }
        
        // Call service to add task
        boolean isAdded = taskService.addTask(title, description, priority, status, dueDate);
        
        if(isAdded) {
            System.out.println("AddTaskServlet: Task added successfully");
            response.sendRedirect(request.getContextPath() + "/dashboard?success=Task added successfully");
        } else {
            System.out.println("AddTaskServlet: Failed to add task");
            request.setAttribute("error", "Failed to add task. Please check your due date (cannot be in the past) and try again.");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
        }
    }
}