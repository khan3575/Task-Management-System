package model;

import java.sql.Date;
import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private String title;
    private String description;
    private String priority;      
    private String status;      
    private Date dueDate;
    private LocalDateTime createdAt;

    public Task() { }

    public Task(String title, String description, String priority, String status, Date dueDate) {
        this.title  = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
    }


    public Integer getId() { 
    		return id;
    	}
    
    public String getTitle() { 
    		return title; 
    	}
    
    public String getDescription() { 
    		return description; 
    	}
    
    public String getPriority() { 
    		return priority; 
    	}
    
    public String getStatus() { 
    		return status; 
    	}
    
    public Date getDueDate() { 
    		return dueDate; 
    	}
    
    public LocalDateTime getCreatedAt() { 
    		return createdAt; 
    	}

 
    public void setId(Integer id) { 
    		this.id = id; 
    	}
    
    public void setTitle(String title) { 
    		this.title = title; 
    	}
    
    public void setDescription(String description) { 
    		this.description = description; 
    	}
    
    public void setPriority(String priority) { 
    		this.priority = priority; 
    	}
    
    public void setStatus(String status) { 
    		this.status = status; 
    	}
    
    public void setDueDate(Date dueDate) { 
    		this.dueDate = dueDate; 
    	}
    
    public void setCreatedAt(LocalDateTime createdAt){ 
    		this.createdAt = createdAt;
    	}
    
    public boolean isValid() {
        return title != null && !title.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', priority='" + priority
                + "', status='" + status + "'}";
    }
}
