<%-- File: webapp/views/editTask.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="dto.TaskDTO" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    String username = (String) session.getAttribute("username");
    if(username == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    TaskDTO task = (TaskDTO) request.getAttribute("task");
    if(task == null) {
        response.sendRedirect(request.getContextPath() + "/dashboard?error=Task not found");
        return;
    }
    
    String dueDateValue = "";
    if(task.getDueDate() != null) {
        dueDateValue = task.getDueDate().toString();
    }
    
    // Get today's date for min attribute
    String todayDate = LocalDate.now().toString();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Task</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="form-container">
            <h2>Edit Task</h2>
            
            <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    ⚠️ <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if(request.getAttribute("dueDateError") != null) { %>
                <div class="error-message">
                    📅 <%= request.getAttribute("dueDateError") %>
                </div>
            <% } %>
            
            <div class="info-note">
                ℹ️ Note: Task ID and Title cannot be modified
            </div>
            
            <form action="${pageContext.request.contextPath}/updateTask" method="POST" onsubmit="return validateDueDate()">
                <!-- Hidden field to pass task ID and title -->
                <input type="hidden" name="taskId" value="<%= task.getId() %>">
                <input type="hidden" name="title" value="<%= task.getTitle() %>">
                
                <div class="form-group">
                    <label>Task ID</label>
                    <input type="text" value="<%= task.getId() %>" disabled readonly>
                    <small>Task ID cannot be modified</small>
                </div>
                
                <div class="form-group">
                    <label>Task Title</label>
                    <input type="text" value="<%= task.getTitle() %>" disabled readonly>
                    <small>Task title cannot be modified</small>
                </div>
                
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" rows="5"><%= task.getDescription() != null ? task.getDescription() : "" %></textarea>
                </div>
                
                <div class="form-group">
                    <label>Priority</label>
                    <select name="priority" required>
                        <option value="LOW" <%= task.getPriority() != null && task.getPriority().equals("LOW") ? "selected" : "" %>>Low</option>
                        <option value="MEDIUM" <%= task.getPriority() != null && task.getPriority().equals("MEDIUM") ? "selected" : "" %>>Medium</option>
                        <option value="HIGH" <%= task.getPriority() != null && task.getPriority().equals("HIGH") ? "selected" : "" %>>High</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Status</label>
                    <select name="status" required>
                        <option value="PENDING" <%= task.getStatus() != null && task.getStatus().equals("PENDING") ? "selected" : "" %>>Pending</option>
                        <option value="IN_PROGRESS" <%= task.getStatus() != null && task.getStatus().equals("IN_PROGRESS") ? "selected" : "" %>>In Progress</option>
                        <option value="COMPLETED" <%= task.getStatus() != null && task.getStatus().equals("COMPLETED") ? "selected" : "" %>>Completed</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Due Date</label>
                    <input type="date" name="dueDate" id="dueDate" value="<%= dueDateValue %>" min="<%= todayDate %>">
                    <small>Note: Due date cannot be before today's date (<%= todayDate %>)</small>
                </div>
                
                <div class="form-group">
                    <label>Created At</label>
                    <input type="text" value="<%= task.getCreatedAt() != null ? task.getCreatedAt() : "" %>" disabled readonly>
                </div>
                
                <div class="form-actions">
                    <button type="submit">Update Task</button>
                    <a href="${pageContext.request.contextPath}/dashboard">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function validateDueDate() {
            var dueDate = document.getElementById("dueDate").value;
            if (dueDate) {
                var today = new Date().toISOString().split('T')[0];
                if (dueDate < today) {
                    alert("Due date cannot be before today's date!");
                    return false;
                }
            }
            return true;
        }
    </script>
</body>
</html>