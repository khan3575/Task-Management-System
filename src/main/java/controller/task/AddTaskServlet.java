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
        taskService = new TaskService();
        taskValidator = new TaskValidator();
        System.out.println("AddTaskServlet initialized");
    }
    
    // 🔹 GET → Show Add Task Page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        System.out.println("Session: " + session);
        System.out.println("Username: " + (session != null ? session.getAttribute("username") : "null"));

        // ❌ Not logged in
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
       // response.getWriter().println("ADD TASK PAGE WORKING");

        
        // ✅ Show form
        request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
    }
    
    // 🔹 POST → Add Task
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        // ❌ Not logged in
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // ✅ Get form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");
        
        // ✅ Validate
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
        
        // ✅ Create DTO
        TaskDTO taskDTO = TaskDTO.forAddTask(title, description, priority, status, dueDate);
        
        // ✅ Call service
        boolean isAdded = taskService.addTask(taskDTO);
        
        if (isAdded) {
            response.sendRedirect(request.getContextPath() + "/home?success=Task added");
        } else {
            request.setAttribute("error", "Failed to add task");
            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
        }
    }
}
