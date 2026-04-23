package validator;

public class TaskValidator {
	 // FAHIM's method: Validate add task input
    public String validateAdd(String title, String description, String priority, 
                               String status, String dueDate) {
        
        // Validate Title (required, min 3 chars, max 100 chars)
        if(title == null || title.trim().isEmpty()) {
            return "Task title is required";
        }
        if(title.trim().length() < 3) {
            return "Task title must be at least 3 characters";
        }
        if(title.trim().length() > 100) {
            return "Task title must not exceed 100 characters";
        }
        
        // Validate Priority (must be valid value)
        if(priority == null || priority.trim().isEmpty()) {
            return "Priority is required";
        }
        String[] validPriorities = {"LOW", "MEDIUM", "HIGH"};
        boolean validPriority = false;
        for(String p : validPriorities) {
            if(p.equals(priority)) {
                validPriority = true;
                break;
            }
        }
        if(!validPriority) {
            return "Invalid priority value. Must be LOW, MEDIUM, or HIGH";
        }
        
        // Validate Status (must be valid value)
        if(status == null || status.trim().isEmpty()) {
            return "Status is required";
        }
        String[] validStatuses = {"PENDING", "IN_PROGRESS", "COMPLETED"};
        boolean validStatus = false;
        for(String s : validStatuses) {
            if(s.equals(status)) {
                validStatus = true;
                break;
            }
        }
        if(!validStatus) {
            return "Invalid status value. Must be PENDING, IN_PROGRESS, or COMPLETED";
        }
        
        // Validate Description (optional, but max 1000 chars)
        if(description != null && description.length() > 1000) {
            return "Description must not exceed 1000 characters";
        }
        
        // Validate Due Date (optional, but if provided must be valid)
        if(dueDate != null && !dueDate.trim().isEmpty()) {
            if(!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Invalid date format. Use YYYY-MM-DD";
            }
        }
        
        return null; // No error
    }
    
    // FAHIM's method: Validate update task input
    public String validateUpdate(String title, String priority, String status, String dueDate) {
        
        // Validate Title (even though it's disabled in form)
        if(title == null || title.trim().isEmpty()) {
            return "Task title is required";
        }
        if(title.trim().length() < 3) {
            return "Task title must be at least 3 characters";
        }
        if(title.trim().length() > 100) {
            return "Task title must not exceed 100 characters";
        }
        
        // Validate Priority
        if(priority == null || priority.trim().isEmpty()) {
            return "Priority is required";
        }
        String[] validPriorities = {"LOW", "MEDIUM", "HIGH"};
        boolean validPriority = false;
        for(String p : validPriorities) {
            if(p.equals(priority)) {
                validPriority = true;
                break;
            }
        }
        if(!validPriority) {
            return "Invalid priority value";
        }
        
        // Validate Status
        if(status == null || status.trim().isEmpty()) {
            return "Status is required";
        }
        String[] validStatuses = {"PENDING", "IN_PROGRESS", "COMPLETED"};
        boolean validStatus = false;
        for(String s : validStatuses) {
            if(s.equals(status)) {
                validStatus = true;
                break;
            }
        }
        if(!validStatus) {
            return "Invalid status value";
        }
        
        // Validate Due Date (optional)
        if(dueDate != null && !dueDate.trim().isEmpty()) {
            if(!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Invalid date format. Use YYYY-MM-DD";
            }
        }
        
        return null;
    }
    
    // Team's basic validation method
    public boolean validateBasic(String input) {
        return input != null && !input.trim().isEmpty();
    }

}
