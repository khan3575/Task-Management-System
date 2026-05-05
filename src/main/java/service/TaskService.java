package service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dao.TaskDAO;
import dto.TaskDTO;
import model.Task;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskService {

    private static final Logger logger = LogManager.getLogger(TaskService.class);

    private TaskDAO taskDAO;

    public boolean addTask(String title, String description, String priority, String status, String dueDateStr) {
        taskDAO = new TaskDAO();
        TaskDTO dto = new TaskDTO();

        dto.setTitle(title != null ? title.trim() : null);
        dto.setDescription(description != null ? description.trim() : null);
        dto.setPriority(priority != null ? priority : "MEDIUM");
        dto.setStatus(status != null ? status : "PENDING");

        if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
            try {
                dto.setDueDate(Date.valueOf(dueDateStr));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid due date format: {}", dueDateStr);
                dto.setDueDate(null);
            }
        }

        logger.info("Adding task with title: {}", dto.getTitle());

        try {
            boolean result = taskDAO.addTask(dto);
            logger.info("Task add result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error while adding task", e);
            return false;
        }
    }

    public boolean updateTask(int id, String title, String description, String priority, String status,
                             String dueDateStr) {

        taskDAO = new TaskDAO();
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
                logger.warn("Invalid due date format: {}", dueDateStr);
                dto.setDueDate(null);
            }
        }

        logger.info("Updating task ID: {}, Title: {}", dto.getId(), dto.getTitle());

        try {
            boolean result = taskDAO.updateTask(dto);
            logger.info("Task update result for ID {}: {}", id, result);
            return result;
        } catch (Exception e) {
            logger.error("Error while updating task ID {}", id, e);
            return false;
        }
    }

    public TaskDTO getTaskById(int taskId) {
        taskDAO = new TaskDAO();

        if (taskId <= 0) {
            logger.warn("Invalid task ID requested: {}", taskId);
            return null;
        }

        logger.debug("Fetching task by ID: {}", taskId);

        return taskDAO.getTaskById(taskId);
    }

    public boolean deleteTask(int id) {
        taskDAO = new TaskDAO();

        if (id < 0) {
            logger.warn("Invalid task ID for deletion: {}", id);
            return false;
        }

        if (!taskDAO.taskExists(id)) {
            logger.warn("Task ID {} does not exist", id);
            return false;
        }

        logger.info("Deleting task ID: {}", id);

        try {
            boolean result = taskDAO.deleteTask(id);
            logger.info("Task delete result for ID {}: {}", id, result);
            return result;
        } catch (Exception e) {
            logger.error("Error while deleting task ID {}", id, e);
            return false;
        }
    }

    public List<TaskDTO> getAllTasks() {
        taskDAO = new TaskDAO();
        logger.debug("Fetching all tasks");
        return taskDAO.getAllTasks();
    }

    public int getTaskCount() {
        taskDAO = new TaskDAO();
        logger.debug("Fetching task count");
        return taskDAO.getTaskCount();
    }

    public List<TaskDTO> searchTasks(String column, String value) {
        taskDAO = new TaskDAO();

        Set<String> ALLOWED_COLUMNS = Set.of("id", "title", "priority", "status", "due_date", "created_at");

        if (column == null || value == null || value.trim().isEmpty()) {
            logger.warn("Search called with invalid input - column: {}, value: {}", column, value);
            return new ArrayList<>();
        }

        if (!ALLOWED_COLUMNS.contains(column)) {
            logger.error("Invalid column for search: {}", column);
            throw new IllegalArgumentException("Invalid column");
        }

        logger.info("Searching tasks by {} = {}", column, value);

        try {
            List<TaskDTO> result = taskDAO.searchTasks(column, value.trim());
            logger.info("Search result count: {}", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error during search operation", e);
            return new ArrayList<>();
        }
    }

    public List<TaskDTO> findPaginated(int page, int size) {
        taskDAO = new TaskDAO();

        logger.debug("Fetching paginated tasks - page: {}, size: {}", page, size);

        return taskDAO.findPaginated(page, size);
    }
}