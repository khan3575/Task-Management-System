package dto;

import java.sql.Date;

public class TaskDTO {
    private int id;
    private String title;
    private String description;
    private String priority;   
    private String status;     
    private Date dueDate;
    private String createdAt;
    
    
    public TaskDTO() {}
    
    public TaskDTO(int id, String title, String description, String priority, 
                   String status, Date dueDate, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }
    
    // FAHIM's method: Create DTO for adding a new task
    public static TaskDTO forAddTask(String title, String description, 
                                      String priority, String status, String dueDateStr) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(title != null ? title.trim() : null);
        dto.setDescription(description != null ? description.trim() : null);
        dto.setPriority(priority);
        dto.setStatus(status);
        
        if(dueDateStr != null && !dueDateStr.trim().isEmpty()) {
            try {
                dto.setDueDate(Date.valueOf(dueDateStr));
            } catch (IllegalArgumentException e) {
                
                dto.setDueDate(null);
            }
        }
        
        return dto;
    }
    
    // FAHIM's method: Create DTO for updating a task
    public static TaskDTO forUpdateTask(int id, String title, String description, 
                                         String priority, String status, String dueDateStr) {
        TaskDTO dto = new TaskDTO();
        dto.setId(id);
        dto.setTitle(title != null ? title.trim() : null);
        dto.setDescription(description != null ? description.trim() : null);
        dto.setPriority(priority);
        dto.setStatus(status);
        
        if(dueDateStr != null && !dueDateStr.trim().isEmpty()) {
            try {
                dto.setDueDate(Date.valueOf(dueDateStr));
            } catch (IllegalArgumentException e) {
                dto.setDueDate(null);
            }
        }
        
        return dto;
    }
    
    //mehedi
    public static TaskDTO forDeleteTask(int id) {
    		TaskDTO dto = new TaskDTO();
    		dto.setId(id);
    		return dto;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "TaskDTO [id=" + id + ", title=" + title + ", priority=" + priority + ", status=" + status + "]";
    }
}
