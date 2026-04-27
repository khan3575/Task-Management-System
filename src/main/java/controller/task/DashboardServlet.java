package controller.task;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dto.TaskDTO;
import model.User;
import service.TaskService;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private TaskService taskService;
    
    @Override
    public void init() {
        taskService = new TaskService();
        System.out.println("✅ DashboardServlet INITIALIZED - Mapping: /dashboard");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check login
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get page number
        int page = 1;
        String pageParam = request.getParameter("page");
        if(pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 5;
        
        // Get all tasks
        List<TaskDTO> allTasks = taskService.getAllTasks();
        int totalTasks = allTasks != null ? allTasks.size() : 0;
        int totalPages = (int) Math.ceil((double) totalTasks / pageSize);
        
        // Paginate
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalTasks);
        
        List<TaskDTO> tasks = null;
        if(allTasks != null && start < totalTasks) {
            tasks = allTasks.subList(start, end);
        }
        
        // Set attributes
        request.setAttribute("tasks", tasks);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalTasks", totalTasks);
        
        // Forward to JSP
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}