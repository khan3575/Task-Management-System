package controller.task;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import service.TaskService;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private TaskService taskService;
    
    @Override
    public void init() {
        taskService = new TaskService();
        System.out.println("DeleteTaskServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("DeleteTaskServlet: doGet called");
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null) {
            System.out.println("DeleteTaskServlet: User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get task ID from request parameter
        String taskIdParam = request.getParameter("id");
        
        if(taskIdParam == null || taskIdParam.trim().isEmpty()) {
            System.out.println("DeleteTaskServlet: No task ID provided");
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
            return;
        }
        
        try {
            int taskId = Integer.parseInt(taskIdParam);
            System.out.println("DeleteTaskServlet: Attempting to delete task ID: " + taskId);
            
            // Delete the task
            boolean isDeleted = taskService.deleteTask(taskId);
            
            if(isDeleted) {
                System.out.println("DeleteTaskServlet: Task ID " + taskId + " deleted successfully");
                response.sendRedirect(request.getContextPath() + "/dashboard?success=Task deleted successfully");
            } else {
                System.out.println("DeleteTaskServlet: Failed to delete task ID " + taskId);
                response.sendRedirect(request.getContextPath() + "/dashboard?error=Failed to delete task");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("DeleteTaskServlet: Invalid task ID format: " + taskIdParam);
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID format");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}