package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.TaskService;
import validator.TaskValidator;
import dto.TaskDTO;

import java.io.IOException;

@WebServlet("/updateTask")
public class UpdateTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TaskService taskService;
    private TaskValidator taskValidator;

    private static final Logger logger = LogManager.getLogger(UpdateTaskServlet.class);

    @Override
    public void init() {
        taskService = new TaskService();
        taskValidator = new TaskValidator();
        logger.info("UpdateTaskServlet initialized");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (isUnauthorized(session)) {
            logger.warn("Unauthorized access to update page");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String taskIdParam = request.getParameter("id");

        if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
            logger.warn("Missing task ID in update request");
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);

            TaskDTO task = taskService.getTaskById(taskId);

            if (task == null) {
                logger.warn("Task not found | taskId={}", taskId);
                response.sendRedirect(request.getContextPath() + "/dashboard?error=Task not found");
                return;
            }

            request.setAttribute("task", task);
            request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid task ID format: {}", taskIdParam);
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (isUnauthorized(session)) {
            logger.warn("Unauthorized update attempt");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = String.valueOf(session.getAttribute("username"));

        int taskId;

        try {
            taskId = Integer.parseInt(request.getParameter("taskId"));
        } catch (Exception e) {
            logger.warn("Invalid task ID in update request");
            response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");

        
        String error = taskValidator.validateUpdate(title, priority, status, dueDate);

        if (error != null) {
            logger.warn("Validation failed | taskId={} | error={}", taskId, error);
            forwardToEdit(request, response, taskId, error);
            return;
        }

       
        boolean isUpdated = taskService.updateTask(taskId, title, description, priority, status, dueDate);

        if (isUpdated) {
            logger.info("Task updated successfully | taskId={} | user={}", taskId, username);
            response.sendRedirect(request.getContextPath() + "/dashboard?success=Task updated");
        } else {
            logger.warn("Task update failed | taskId={}", taskId);
            forwardToEdit(request, response, taskId, "Update failed. Please try again.");
        }
    }

    

    private boolean isUnauthorized(HttpSession session) {
        return session == null || session.getAttribute("username") == null;
    }

    private void forwardToEdit(HttpServletRequest request, HttpServletResponse response,
                               int taskId, String error)
            throws ServletException, IOException {

        TaskDTO task = taskService.getTaskById(taskId);
        request.setAttribute("task", task);
        request.setAttribute("error", error);
        request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
    }
}
