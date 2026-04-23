package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.TaskService;
import validator.TaskValidator;

import java.io.IOException;

import dto.TaskDTO;

/**
 * Servlet implementation class UpdateTaskServlet
 */
@WebServlet("/UpdateTaskServlet")
public class UpdateTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private TaskService taskService;
    private TaskValidator taskValidator;
    
    @Override
    public void init() {
        taskService = new TaskService();
        taskValidator = new TaskValidator();
        System.out.println("UpdateTaskServlet initialized");
    }
    
    // Handle GET request - Show edit form with task data
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get task ID from request
        String taskIdParam = request.getParameter("id");
        
        if(taskIdParam == null || taskIdParam.trim().isEmpty()) {
            response.sendRedirect("DashboardServlet?error=Invalid task ID");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(taskIdParam);
            System.out.println("UpdateTaskServlet: Fetching task ID: " + taskId);
            
            // Fetch task from database
            TaskDTO task = taskService.getTaskById(taskId);
            
            if(task == null) {
                response.sendRedirect("DashboardServlet?error=Task not found");
                return;
            }
            
            // Store task in request and forward to edit page
            request.setAttribute("task", task);
            request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.out.println("UpdateTaskServlet: Invalid task ID format");
            response.sendRedirect("DashboardServlet?error=Invalid task ID format");
        }
    }
    
    // Handle POST request - Update the task
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("UpdateTaskServlet: Received POST request");
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get form parameters
        String taskIdStr = request.getParameter("taskId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");
        
        System.out.println("UpdateTaskServlet: TaskID=" + taskIdStr + ", Title=" + title);
        
        // Validate task ID
        if(taskIdStr == null || taskIdStr.trim().isEmpty()) {
            response.sendRedirect("DashboardServlet?error=Task ID is required");
            return;
        }
        
        int taskId;
        try {
            taskId = Integer.parseInt(taskIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("DashboardServlet?error=Invalid task ID format");
            return;
        }
        
        // Validate input using TaskValidator
        String validationError = taskValidator.validateUpdate(title, priority, status, dueDate);
        
        if(validationError != null) {
            System.out.println("UpdateTaskServlet: Validation failed - " + validationError);
            // Fetch task again and show error
            TaskDTO task = taskService.getTaskById(taskId);
            request.setAttribute("task", task);
            request.setAttribute("error", validationError);
            request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
            return;
        }
        
        // Create DTO using forUpdateTask() method
        TaskDTO taskDTO = TaskDTO.forUpdateTask(taskId, title, description, priority, status, dueDate);
        
        // Call Service layer to update task
        boolean isUpdated = taskService.updateTask(taskDTO);
        
        // Redirect based on result
        if(isUpdated) {
            System.out.println("UpdateTaskServlet: Task updated successfully");
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=Task updated successfully");
        } else {
            System.out.println("UpdateTaskServlet: Failed to update task");
            TaskDTO task = taskService.getTaskById(taskId);
            request.setAttribute("task", task);
            request.setAttribute("error", "Failed to update task. Please try again.");
            request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
        }
    }

}
