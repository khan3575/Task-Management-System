package dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class TaskDTO {
    private int id;
    private String title;
    private String description;
    private String priority;   
    private String status;     
    private Date dueDate;
    private LocalDateTime createdAt;
    
    public TaskDTO() {}
    
    public TaskDTO(int id, String title, String description, String priority, 
                   String status, Date dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime localDateTime) { this.createdAt = localDateTime; }
    
    @Override
    public String toString() {
        return "TaskDTO [id=" + id + ", title=" + title + ", priority=" + priority + ", status=" + status + "]";
    }

	
}
