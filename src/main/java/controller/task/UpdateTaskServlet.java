package controller.task;

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

	private static final long serialVersionUID = 1L;
	private TaskService taskService;
	private TaskValidator taskValidator;

	@Override
	public void init() {
		taskService = new TaskService();
		taskValidator = new TaskValidator();
		System.out.println("UpdateTaskServlet initialized");
	}

	// 🔹 GET → load edit page
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		// ✅ FIX 1: session check
		if (session == null || session.getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String taskIdParam = request.getParameter("id");

		if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
			return;
		}

		try {
			int taskId = Integer.parseInt(taskIdParam);

			TaskDTO task = taskService.getTaskById(taskId);

			if (task == null) {
				response.sendRedirect(request.getContextPath() + "/dashboard?error=Task not found");
				return;
			}

			request.setAttribute("task", task);
			request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);

		} catch (NumberFormatException e) {
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
			response.sendRedirect(request.getContextPath() + "/dashboard?error=Invalid task ID");
			return;
		}

		// ✅ NEW: Due date validation - cannot be before today
		if (dueDate != null && !dueDate.trim().isEmpty()) {
			try {
				LocalDate today = LocalDate.now();
				LocalDate dueLocalDate = LocalDate.parse(dueDate);

				if (dueLocalDate.isBefore(today)) {
					TaskDTO task = taskService.getTaskById(taskId);
					request.setAttribute("task", task);
					request.setAttribute("error", "Due date cannot be before today's date!");
					request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
					return;
				}
			} catch (Exception e) {
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

		boolean isUpdated = taskService.updateTask(taskId, title, description, priority, status, dueDate);

		if (isUpdated) {
			request.setAttribute("success", "Task updated successfully");
		} else {
			request.setAttribute("error", "Update failed");
		}
		TaskDTO task = taskService.getTaskById(taskId);
		request.setAttribute("task", task);

		request.getRequestDispatcher("/views/editTask.jsp").forward(request, response);
	}


    }


