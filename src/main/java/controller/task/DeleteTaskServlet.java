package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.TaskService;
import util.AppLogger;

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
            throws IOException {
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
            sendResult(request, response, false, "Not logged in");
            return;
        }

       
        String taskIdParam = request.getParameter("id");
        if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
            sendResult(request, response, false, "Invalid task ID");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);
            boolean deleted = taskService.deleteTask(taskId);

            if (deleted) {
            	String path = request.getServletContext().getRealPath("/logs/app.log");
                AppLogger.log(
                            path,
                            "TASK DELETED",
                            "A task deleted",
                            (String)session.getAttribute("username"),
                            "title= taskId: "+ taskId
                );
                sendResult(request, response, true, "Task deleted successfully");
            } else {
                sendResult(request, response, false, "Deletion failed");
            }

        } catch (NumberFormatException e) {
            sendResult(request, response, false, "Invalid task ID format");
        }
    }

   
    private void sendResult(HttpServletRequest request, HttpServletResponse response,
                            boolean success, String message) throws IOException {

        boolean isAjax = "true".equals(request.getParameter("ajax"));

        if (isAjax) {
         
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(message);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(message);
            }
        } else {
            String type = success ? "success" : "error";
            response.sendRedirect(request.getContextPath() + "/dashboard?" + type + "=" + message);
        }
    }
}