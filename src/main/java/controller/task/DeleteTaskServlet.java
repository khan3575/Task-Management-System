package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.TaskService;

import java.io.IOException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    
    private static final Logger logger = LogManager.getLogger(DeleteTaskServlet.class);

    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
        logger.info("DeleteTaskServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logger.debug("Received GET request for deleteTask");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            logger.warn("Unauthorized access attempt to deleteTask (GET)");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        
        doDelete(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logger.debug("Processing DELETE request");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            logger.warn("Unauthorized delete attempt");
            sendResult(request, response, false, "Not logged in");
            return;
        }

        String username = (String) session.getAttribute("username");

        String taskIdParam = request.getParameter("id");

        if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
            logger.warn("Invalid task ID received from user: {}", username);
            sendResult(request, response, false, "Invalid task ID");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);

            logger.info("User '{}' attempting to delete task ID: {}", username, taskId);

            boolean deleted = taskService.deleteTask(taskId);

            if (deleted) {
                logger.info("Task ID {} deleted successfully by user '{}'", taskId, username);
                sendResult(request, response, true, "Task deleted successfully");
            } else {
                logger.error("Failed to delete task ID {} by user '{}'", taskId, username);
                sendResult(request, response, false, "Deletion failed");
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid task ID format: {}", taskIdParam, e);
            sendResult(request, response, false, "Invalid task ID format");
        } catch (Exception e) {
            
            logger.fatal("Unexpected error while deleting task", e);
            sendResult(request, response, false, "Something went wrong");
        }
    }

    private void sendResult(HttpServletRequest request, HttpServletResponse response,
                            boolean success, String message) throws IOException {

        boolean isAjax = "true".equals(request.getParameter("ajax"));

        logger.debug("Sending response (ajax={}): {}", isAjax, message);

        if (isAjax) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            response.getWriter().write(message);
        } else {
            String type = success ? "success" : "error";
            response.sendRedirect(request.getContextPath() + "/dashboard?" + type + "=" + message);
        }
    }
}