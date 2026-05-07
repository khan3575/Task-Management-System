package controller.task;

import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.TaskService;

import java.io.IOException;
import dto.TaskDTO;



@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(DeleteTaskServlet.class);
    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
        System.out.println("DeleteTaskServlet initialized");
        logger.info("DeleteTaskServlet initialized.");
    }

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
        	logger.warn("Unauthorized access attempt to /deleteTask via GET redirecting to login");
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
        	logger.warn("Delete attempt blocked cause No active session Not loged in");
            sendResult(request, response, false, "Not logged in");
            return;
        }

       
        String taskIdParam = request.getParameter("id");
        if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
        	logger.error("Delete failed: No Task ID given by user '{}'", session.getAttribute("username"));
            sendResult(request, response, false, "Invalid task ID");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);
            TaskDTO task = taskService.getTaskById(taskId);
            boolean deleted = taskService.deleteTask(taskId);

            if (deleted) {
            	logger.info("User '{}' successfully deleted the task id: '{}'. Details: '{}'", session.getAttribute("username"), taskId, task);
                sendResult(request, response, true, "Task deleted successfully");
            } else {
            	logger.warn("User '{}' tried to delete non-existent Task ID: {}", session.getAttribute("username"), taskId);
                sendResult(request, response, false, "Deletion failed");
            }

        } catch (NumberFormatException e) {
        	logger.debug("invalid task id delete request by" + session.getAttribute("username"));
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