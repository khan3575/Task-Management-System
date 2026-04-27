<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="dto.TaskDTO" %>

<%
    String loggedInUser = (String) session.getAttribute("username");
    if(loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
    
    String searchColumn = request.getParameter("column");
    String searchValue = request.getParameter("value");
    
    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Task</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
</head>
<body>
    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %>
    
    <div class="main-content">
        <div class="search-container">
            <h2>🔍 Search Tasks</h2>
            <p class="home-subtitle">Search tasks by ID, Title, Priority, Status, Due Date, or Created Date</p>
            
            <!-- Search Form Card -->
            <div class="search-card">
                <h3>Search Criteria</h3>
                <form action="${pageContext.request.contextPath}/searchTask" method="get" class="search-form">
                    <div class="form-group">
                        <label>Search By:</label>
                        <select name="column">
                            <option value="">-- Select Option --</option>
                            <option value="id" <%= "id".equals(searchColumn) ? "selected" : "" %>>ID</option>
                            <option value="title" <%= "title".equals(searchColumn) ? "selected" : "" %>>Title</option>
                            <option value="priority" <%= "priority".equals(searchColumn) ? "selected" : "" %>>Priority</option>
                            <option value="status" <%= "status".equals(searchColumn) ? "selected" : "" %>>Status</option>
                            <option value="due_date" <%= "due_date".equals(searchColumn) ? "selected" : "" %>>Due Date</option>
                            <option value="created_at" <%= "created_at".equals(searchColumn) ? "selected" : "" %>>Created At</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>Search Value:</label>
                        <input type="text" name="value" placeholder="Enter search value..." value="<%= searchValue != null ? searchValue : "" %>">
                    </div>
                    
                    <div>
                        <button type="submit" class="search-btn">🔍 Search</button>
                        <a href="${pageContext.request.contextPath}/searchTask" class="reset-btn">🔄 Reset</a>
                    </div>
                </form>
            </div>
            
            <!-- Search Results Card -->
            <div class="search-results">
                <h4>Search Results <span class="result-count">(<%= tasks != null ? tasks.size() : 0 %> tasks found)</span></h4>
                
                <table class="search-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Due Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (tasks != null && !tasks.isEmpty()) {
                                for (TaskDTO task : tasks) { 
                                    String formattedDueDate = "";
                                    if(task.getDueDate() != null) {
                                        java.util.Date utilDate = new java.util.Date(task.getDueDate().getTime());
                                        formattedDueDate = displayFormat.format(utilDate);
                                    }
                        %>
                            <tr>
                                <td><%= task.getId() %></td>
                                <td><%= task.getTitle() %></td>
                                <td class="priority-<%= task.getPriority() != null ? task.getPriority().toLowerCase() : "medium" %>">
                                    <%= task.getPriority() != null ? task.getPriority() : "MEDIUM" %>
                                </td>
                                <td class="status-<%= task.getStatus() != null ? task.getStatus().toLowerCase().replace("_", "") : "pending" %>">
                                    <%= task.getStatus() != null ? task.getStatus() : "PENDING" %>
                                </td>
                                <td><%= formattedDueDate.isEmpty() ? "-" : formattedDueDate %></td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="5" class="no-results">
                                    <span class="empty-icon">🔍</span>
                                    <p>No results found. Try a different search term.</p>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>