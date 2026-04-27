<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="dto.TaskDTO" %>
<%@ page import="model.User" %>

<%
    String loggedInUser = (String) session.getAttribute("username");
    if(loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
    
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer totalTasks = (Integer) request.getAttribute("totalTasks");
    
    if(currentPage == null) currentPage = 1;
    if(totalPages == null) totalPages = 1;
    if(totalTasks == null) totalTasks = 0;
    
    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
</head>
<body>
    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %>
    
    <div class="main-content">
        <h2>Task Dashboard</h2>
        
        <!-- Success/Error Messages -->
        <% if(request.getParameter("success") != null) { %>
            <div class="success-message"><%= request.getParameter("success") %></div>
        <% } %>
        <% if(request.getParameter("error") != null) { %>
            <div class="error-message"><%= request.getParameter("error") %></div>
        <% } %>
        
        <div class="table-container">
            <table class="task-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Priority</th>
                        <th>Status</th>
                        <th>Due Date</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(tasks != null && !tasks.isEmpty()) { 
                        for(TaskDTO task : tasks) { 
                            String formattedDueDate = "";
                            if(task.getDueDate() != null) {
                                java.util.Date utilDate = new java.util.Date(task.getDueDate().getTime());
                                formattedDueDate = displayFormat.format(utilDate);
                            }
                            
                            String formattedCreatedAt = "";
                            if(task.getCreatedAt() != null) {
                                formattedCreatedAt = task.getCreatedAt().toString();
                                if(formattedCreatedAt.length() > 10) {
                                    formattedCreatedAt = formattedCreatedAt.substring(0, 10);
                                }
                            }
                    %>
                    <tr>
                        <td class="task-id"><%= task.getId() %></td>
                        <td class="task-title"><%= task.getTitle() %></td>
                        <td class="task-desc"><%= task.getDescription() != null ? task.getDescription() : "-" %></td>
                        <td class="priority-<%= task.getPriority() != null ? task.getPriority().toLowerCase() : "medium" %>">
                            <%= task.getPriority() != null ? task.getPriority() : "MEDIUM" %>
                        </td>
                        <td class="status-<%= task.getStatus() != null ? task.getStatus().toLowerCase().replace("_", "") : "pending" %>">
                            <%= task.getStatus() != null ? task.getStatus() : "PENDING" %>
                        </td>
                        <td><%= formattedDueDate.isEmpty() ? "-" : formattedDueDate %></td>
                        <td><%= formattedCreatedAt.isEmpty() ? "-" : formattedCreatedAt %></td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/updateTask?id=<%= task.getId() %>" class="btn-edit">Edit</a>
                            <a href="#" onclick="confirmDelete(<%= task.getId() %>); return false;" class="btn-delete">Delete</a>
                        </td>
                    </tr>
                    <% } 
                    } else { %>
                    <tr>
                        <td colspan="8" style="text-align: center;">No tasks found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <% if(tasks != null && !tasks.isEmpty() && totalPages > 1) { %>
            <div class="pagination">
                <% if(currentPage > 1) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?page=<%= currentPage - 1 %>" class="page-link">Previous</a>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?page=<%= i %>" 
                       class="page-link <%= i == currentPage ? "active" : "" %>"><%= i %></a>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <a href="${pageContext.request.contextPath}/dashboard?page=<%= currentPage + 1 %>" class="page-link">Next</a>
                <% } %>
            </div>
        <% } %>
    </div>
    
    <script>
        function confirmDelete(taskId) {
            if(confirm("Are you sure you want to delete this task?")) {
                window.location.href = "${pageContext.request.contextPath}/deleteTask?id=" + taskId;
            }
        }
    </script>
</body>
</html>