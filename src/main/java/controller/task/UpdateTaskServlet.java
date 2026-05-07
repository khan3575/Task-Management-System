package controller.task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.TaskService;
import validator.TaskValidator;

import java.io.IOException;
import java.time.LocalDate;

import dto.TaskDTO;

@WebServlet("/updateTask")
public class UpdateTaskServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(UpdateTaskServlet.class);
   
	private static final long serialVersionUID = 1L;
	private TaskService taskService;
	private TaskValidator taskValidator;

	@Override
	public void init() {
		taskService = new TaskService();
		taskValidator = new TaskValidator();
		logger.info("UpdateTaskServlet initialized.");
	}

	// 🔹 GET → load edit page
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

				if (session == null || session.getAttribute("username") == null) {
			logger.warn("Unauthorized GET access to /updateTask redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String taskIdParam = request.getParameter("id");

		if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
			logger.warn("User '{}' requested edit page without Task ID.", session.getAttribute("username"));
			response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
			return;
		}

		try {
			int taskId = Integer.parseInt(taskIdParam);

			TaskDTO task = taskService.getTaskById(taskId);

			if (task == null) {
				logger.warn("User '{}' attempted to edit Task ID that doesnt exit: {}", session.getAttribute("username"), taskId);
				response.sendRedirect(request.getContextPath() + "/dashboard?error=Task not found");
				return;
			}
			logger.debug("Loading edit page for Task ID: {} for user: {}", taskId, session.getAttribute("username"));
			request.setAttribute("task", task);
			request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			logger.error("Invalid task ID format '{}' requested by user '{}'", taskIdParam, session.getAttribute("username"));
			response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID format");
		}
	}

	// 🔹 POST → update task
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("UpdateTaskServlet: POST request");

		HttpSession session = request.getSession(false);

		// ✅ FIX 2: session check corrected
		if (session == null || session.getAttribute("username") == null) {
			logger.error("Unauthorized POST attempt to /updateTask.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String taskIdStr = request.getParameter("taskId");
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String priority = request.getParameter("priority");
		String status = request.getParameter("status");
		String dueDate = request.getParameter("dueDate");

		int taskId;

		try {
			taskId = Integer.parseInt(taskIdStr);
		} catch (Exception e) {
			logger.error("Update failed: Task ID '{}' from user '{}' maybe invalid", taskIdStr, session.getAttribute("username"));
			response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
			return;
		}
		logger.info("User '{}' is updating Task ID: {} (New Title: '{}')", session.getAttribute("username"), taskId, title);
		// ✅ NEW: Due date validation - cannot be before today
		if (dueDate != null && !dueDate.trim().isEmpty()) {
			try {
				LocalDate today = LocalDate.now();
				LocalDate dueLocalDate = LocalDate.parse(dueDate);

				if (dueLocalDate.isBefore(today)) {
					logger.warn("Update Validation Fail: Date '{}' is in the past for Task ID: {}", dueDate, taskId);
					TaskDTO task = taskService.getTaskById(taskId);
					request.setAttribute("task", task);
					request.setAttribute("error", "Due date cannot be before today's date!");
					request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
					return;
				}
			} catch (Exception e) {
				logger.error("Date parse error for Task ID: {} | Input: '{}'", taskId, dueDate, e);
				TaskDTO task = taskService.getTaskById(taskId);
				request.setAttribute("task", task);
				request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD");
				request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
				return;
			}
		}

		String error = taskValidator.validateUpdate(title, priority, status, dueDate);

		if (error != null) {
			TaskDTO task = taskService.getTaskById(taskId);
			request.setAttribute("task", task);
			request.setAttribute("error", error);
			request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
			return;
		}
		TaskDTO oldTask = taskService.getTaskById(taskId);
		logger.debug("[Before update] '{}' ", oldTask);
		boolean isUpdated = taskService.updateTask(taskId, title, description, priority, status, dueDate);
		TaskDTO newTask = taskService.getTaskById(taskId);
		if (isUpdated) {
			logger.debug("[Before update ]: '{}' ", oldTask);
			logger.debug("[After update ]: '{}'", newTask);
			request.setAttribute("success", "Task updated successfully");
		} else {
			logger.error("Task Update failed Database Error");
			request.setAttribute("error", "Update failed");
		}
		TaskDTO task = taskService.getTaskById(taskId);
		request.setAttribute("task", task);

		request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
	}


    }


