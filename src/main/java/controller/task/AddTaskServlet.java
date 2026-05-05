package controller.task;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.TaskService;
import validator.TaskValidator;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(AddTaskServlet.class);

    private TaskService taskService;
    private TaskValidator taskValidator;

    @Override
    public void init() {
        taskValidator = new TaskValidator();
        logger.info("AddTaskServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            logger.warn("Unauthorized access to /addTask");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            logger.warn("Unauthorized POST to /addTask");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = (String) session.getAttribute("username");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");

        logger.info("Task add attempt | user={} | title={}", username, title);

        // Validate due date (cannot be before today)
        if (dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                LocalDate today = LocalDate.now();
                LocalDate dueLocalDate = LocalDate.parse(dueDate);

                if (dueLocalDate.isBefore(today)) {

                    logger.warn("Invalid due date (past date) | user={}", username);

                    request.setAttribute("error", "Due date cannot be before today's date!");
                    request.setAttribute("title", title);
                    request.setAttribute("description", description);
                    request.setAttribute("priority", priority);
                    request.setAttribute("status", status);
                    request.setAttribute("dueDate", dueDate);

                    request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
                    return;
                }

            } catch (Exception e) {

                logger.error("Invalid date format | user={}", username, e);

                request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD.");
                request.setAttribute("title", title);
                request.setAttribute("description", description);
                request.setAttribute("priority", priority);
                request.setAttribute("status", status);
                request.setAttribute("dueDate", dueDate);

                request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
                return;
            }
        }

        
        String error = taskValidator.validateAdd(title, description, priority, status, dueDate);

        if (error != null) {

            logger.warn("Validation failed | user={} | error={}", username, error);

            request.setAttribute("error", error);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);

            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
            return;
        }

        // Save task
        taskService = new TaskService();
        boolean isAdded = taskService.addTask(title, description, priority, status, dueDate);

        if (isAdded) {

            logger.info("Task added successfully | user={} | title={}", username, title);

            response.sendRedirect(request.getContextPath() + "/addTask?status=success");

        } else {

            logger.error("Task creation failed | user={} | title={}", username, title);

            request.setAttribute("error", "Failed to add task. Try again.");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("priority", priority);
            request.setAttribute("status", status);
            request.setAttribute("dueDate", dueDate);

            request.getRequestDispatcher("/views/addTask.jsp").forward(request, response);
        }
    }
}
