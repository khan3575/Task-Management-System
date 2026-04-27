package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.TaskService;

import java.io.IOException;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
        System.out.println("DeleteTaskServlet initialized");
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
    	
    		HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        doDelete(request, response);
    			
     }
   
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

       
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

       
        String taskIdParam = request.getParameter("id");
        if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid+task+ID");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);

            boolean deleted = taskService.deleteTask(taskId);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/dashboard?success=Task+deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard?error=Deletion+failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid+task+ID+format");
        }
    }
}