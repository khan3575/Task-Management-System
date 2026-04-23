package service;

import java.util.List;

import dao.TaskDAO;
import dto.TaskDTO;

public class TaskService {
private TaskDAO taskDAO;
    
    public TaskService() {
        this.taskDAO = new TaskDAO();
    }
    
    // FAHIM's method: Add a new task
    public boolean addTask(TaskDTO taskDTO) {
        // Business logic before adding
        if(taskDTO == null) {
            System.err.println("TaskService: TaskDTO is null");
            return false;
        }
        
        if(taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            System.err.println("TaskService: Task title is required");
            return false;
        }
        
        // Set default priority if not set
        if(taskDTO.getPriority() == null || taskDTO.getPriority().isEmpty()) {
            taskDTO.setPriority("MEDIUM");
        }
        
        // Set default status if not set
        if(taskDTO.getStatus() == null || taskDTO.getStatus().isEmpty()) {
            taskDTO.setStatus("PENDING");
        }
        
        System.out.println("TaskService: Adding task - " + taskDTO.getTitle());
        
        // Call DAO to insert
        return taskDAO.addTask(taskDTO);
    }
    
    // FAHIM's method: Update an existing task
    public boolean updateTask(TaskDTO taskDTO) {
        // Business logic before updating
        if(taskDTO == null || taskDTO.getId() <= 0) {
            System.err.println("TaskService: Invalid task ID");
            return false;
        }
        
        if(taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            System.err.println("TaskService: Task title is required");
            return false;
        }
        
        // Can't update if task doesn't exist
        if(!taskDAO.taskExists(taskDTO.getId())) {
            System.err.println("TaskService: Task with ID " + taskDTO.getId() + " does not exist");
            return false;
        }
        
        System.out.println("TaskService: Updating task - ID: " + taskDTO.getId() + ", Title: " + taskDTO.getTitle());
        
        // Call DAO to update
        return taskDAO.updateTask(taskDTO);
    }
    
    // FAHIM's method: Get task by ID
    public TaskDTO getTaskById(int taskId) {
        if(taskId <= 0) {
            return null;
        }
        return taskDAO.getTaskById(taskId);
    }
    
    // For Dashboard (helping Mahmud)
    public List<TaskDTO> getAllTasks() {
        return taskDAO.getAllTasks();
    }

}
