package service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dao.TaskDAO;
import dto.TaskDTO;
import model.Task;

public class TaskService {
	private TaskDAO taskDAO;

	/*
	 * public TaskService() { this.taskDAO = new TaskDAO(); }
	 */

	// FAHIM's method: Add a new task
	public boolean addTask(String title, String description, String priority, String status, String dueDateStr) {
		TaskDTO dto = new TaskDTO();

		dto.setTitle(title != null ? title.trim() : null);
		dto.setDescription(description != null ? description.trim() : null);
		dto.setPriority(priority != null ? priority : "MEDIUM");
		dto.setStatus(status != null ? status : "PENDING");

		if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
			try {
				dto.setDueDate(Date.valueOf(dueDateStr));
			} catch (IllegalArgumentException e) {

				dto.setDueDate(null);
			}
		}

		System.out.println("TaskService: Adding task - " + dto.getTitle());

		// Call DAO to insert

		return taskDAO.addTask(dto);
	}

	// FAHIM's method: Update an existing task
	public boolean updateTask(int id, String title, String description, String priority, String status,
			String dueDateStr) {
		TaskDTO dto = new TaskDTO();
		dto.setId(id);
		dto.setTitle(title != null ? title.trim() : null);
		dto.setDescription(description != null ? description.trim() : null);
		dto.setPriority(priority != null ? priority : "MEDIUM");
		dto.setStatus(status != null ? status : "PENDING");

		if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
			try {
				dto.setDueDate(Date.valueOf(dueDateStr));
			} catch (IllegalArgumentException e) {
				dto.setDueDate(null);

			}
		}

		System.out.println("TaskService: Updating task - ID: " + dto.getId() + ", Title: " + dto.getTitle());

		// Call DAO to update
		return taskDAO.updateTask(dto);
	}

	// FAHIM's method: Get task by ID
	public TaskDTO getTaskById(int taskId) {
		if (taskId <= 0) {
			return null;
		}
		return taskDAO.getTaskById(taskId);
	}

	// mehedi-deleteTask
	public boolean deleteTask(int id) {
		if (id < 0) {
			System.err.println("TaskService: Invalid task DTO for deletion");
			return false;
		}
		if (!taskDAO.taskExists(id)) {
			System.err.println("TaskService: Task ID " + id + " does not exist");
			return false;
		}
		return taskDAO.deleteTask(id);
	}

	// For Dashboard (helping Mahmud)
	public List<TaskDTO> getAllTasks() {
		return taskDAO.getAllTasks();
	}

	// search task service
	public List<TaskDTO> searchTasks(String column, String value) {
		Set<String> ALLOWED_COLUMNS = Set.of("id", "title", "priority", "status", "due_date", "created_at");

		if (column == null || value == null || value.trim().isEmpty()) {
			return new ArrayList<>();
		}

		if (!ALLOWED_COLUMNS.contains(column)) {
			throw new IllegalArgumentException("Invalid column");
		}

		return taskDAO.searchTasks(column, value.trim());
	}

}
