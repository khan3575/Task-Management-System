package controller.task;

import java.io.IOException;
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
        
        // Validate
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
            response.sendRedirect(request.getContextPath() + "/home?success=Task added");
        } else {
            request.setAttribute("error", "Failed to add task");
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
        }
    }
}
