package validator;

import java.time.LocalDate;

public class TaskValidator {

    private static final String[] PRIORITIES = {"LOW", "MEDIUM", "HIGH"};
    private static final String[] STATUSES = {"PENDING", "IN_PROGRESS", "COMPLETED"};

    // ================= ADD TASK =================
    public String validateAdd(String title, String description, String priority, String status, String dueDate) {

        String error;

        error = validateTitle(title);
        if (error != null) return error;

        error = validatePriority(priority);
        if (error != null) return error;

        error = validateStatus(status);
        if (error != null) return error;

        error = validateDescription(description);
        if (error != null) return error;

        error = validateDueDateFormat(dueDate);
        if (error != null) return error;

        return null;
    }

    // ================= UPDATE TASK =================
    public String validateUpdate(String title, String priority, String status, String dueDate) {

        String error;

        error = validateTitle(title);
        if (error != null) return error;

        error = validatePriority(priority);
        if (error != null) return error;

        error = validateStatus(status);
        if (error != null) return error;

        error = validateDueDateFull(dueDate);
        if (error != null) return error;

        return null;
    }

    // ================= COMMON METHODS =================

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty())
            return "Task title is required";

        if (title.trim().length() < 3)
            return "Task title must be at least 3 characters";

        if (title.trim().length() > 100)
            return "Task title must not exceed 100 characters";

        return null;
    }

    private String validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty())
            return "Priority is required";

        for (String p : PRIORITIES) {
            if (p.equals(priority)) return null;
        }

        return "Invalid priority value. Must be LOW, MEDIUM, or HIGH";
    }

    private String validateStatus(String status) {
        if (status == null || status.trim().isEmpty())
            return "Status is required";

        for (String s : STATUSES) {
            if (s.equals(status)) return null;
        }

        return "Invalid status value. Must be PENDING, IN_PROGRESS, or COMPLETED";
    }

    private String validateDescription(String description) {
        if (description != null && description.length() > 1000)
            return "Description must not exceed 1000 characters";

        return null;
    }

    private String validateDueDateFormat(String dueDate) {
        if (dueDate != null && !dueDate.trim().isEmpty()) {
            if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Invalid date format. Use YYYY-MM-DD";
            }
        }
        return null;
    }

    private String validateDueDateFull(String dueDate) {
        if (dueDate != null && !dueDate.trim().isEmpty()) {

            if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "Invalid date format. Use YYYY-MM-DD";
            }

            try {
                LocalDate date = LocalDate.parse(dueDate);
                if (date.isBefore(LocalDate.now())) {
                    return "Due date cannot be before today's date";
                }
            } catch (Exception e) {
                return "Invalid date format. Use YYYY-MM-DD";
            }
        }
        return null;
    }
}
