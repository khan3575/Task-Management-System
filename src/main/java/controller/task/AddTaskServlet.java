package controller.task;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import dto.TaskDTO;
import service.TaskService;
import validator.TaskValidator;

/**
 * Servlet implementation class AddTaskServlet
 */
@WebServlet("/AddTaskServlet")
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
	        if(session == null || session.getAttribute("user") == null) {
	            response.sendRedirect("login.jsp");
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
	        
	        // STEP 1: Check if user is logged in
	        HttpSession session = request.getSession(false);
	        if(session == null || session.getAttribute("user") == null) {
	            response.sendRedirect("login.jsp");
	            return;
	        }
	        
	        // STEP 2: Get form parameters
	        String title = request.getParameter("title");
	        String description = request.getParameter("description");
	        String priority = request.getParameter("priority");
	        String status = request.getParameter("status");
	        String dueDate = request.getParameter("dueDate");
	        
	        System.out.println("AddTaskServlet: Title=" + title + ", Priority=" + priority + ", Status=" + status);
	        
	        // STEP 3: Validate input using TaskValidator
	        String validationError = taskValidator.validateAdd(title, description, priority, status, dueDate);
	        
	        if(validationError != null) {
	            // Validation failed - send back to form with error
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
	        
	        // STEP 4: Create DTO using forAddTask() method
	        TaskDTO taskDTO = TaskDTO.forAddTask(title, description, priority, status, dueDate);
	        
	        // STEP 5: Call Service layer to add task
	        boolean isAdded = taskService.addTask(taskDTO);
	        
	        // STEP 6: Redirect based on result
	        if(isAdded) {
	            // Success - redirect to dashboard
	            System.out.println("AddTaskServlet: Task added successfully");
	            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=Task added successfully");
	        } else {
	            // Failure - show error
	            System.out.println("AddTaskServlet: Failed to add task");
	            request.setAttribute("error", "Failed to add task. Please try again.");
	            request.setAttribute("title", title);
	            request.setAttribute("description", description);
	            request.setAttribute("priority", priority);
	            request.setAttribute("status", status);
	            request.setAttribute("dueDate", dueDate);
	            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
	        }
	    }

    

}
