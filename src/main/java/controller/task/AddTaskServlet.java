package controller.task;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dto.TaskDTO;
import service.TaskService;
import validator.TaskValidator;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private TaskService taskService;
    private TaskValidator taskValidator;
    
    @Override
    public void init() {
        taskValidator = new TaskValidator();
        System.out.println("AddTaskServlet initialized");
    }
    
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        System.out.println("Session: " + session);
        System.out.println("Username: " + (session != null ? session.getAttribute("username") : "null"));

        //Not logged in
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        // Not logged in
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");
        
        // NEW LOGIC: Validate due date (cannot be before today)
        if(dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                LocalDate today = LocalDate.now();
                LocalDate dueLocalDate = LocalDate.parse(dueDate);
                
                if(dueLocalDate.isBefore(today)) {
                    System.out.println("Due date validation failed - date is in past");
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
                System.out.println("Date parsing error - " + e.getMessage());
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
            // <sakib> changes = context path to addTask and status = success
            response.sendRedirect(request.getContextPath() + "/addTask?status=success");
        } else {
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