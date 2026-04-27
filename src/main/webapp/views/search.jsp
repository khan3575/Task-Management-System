<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Task" %>
<%@ page import="model.User" %>
<%
    // Check if user is logged in
    String loggedInUser = (String) session.getAttribute("username");
    if(loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    String searchColumn = request.getParameter("column");
    String searchValue = request.getParameter("value");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Task - Task Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Include Navbar and Sidebar -->
    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %>
    
    <!-- Main Content -->
    <div class="main-content">
        <div class="search-container">
            <h2>🔍 Search Tasks</h2>
            <p class="home-subtitle">Search tasks by ID, Title, Priority, Status, Due Date, or Created Date</p>
            
            <!-- Search Form Card -->
            <div class="search-card">
                <h3>Search Criteria</h3>
                <form action="searchTask" method="get" class="search-form">
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
                        <a href="searchTask" class="reset-btn">🔄 Reset</a>
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
                                for (Task t : tasks) {
                        %>
                            <tr>
                                <td><%= t.getId() %></td>
                                <td><%= t.getTitle() %></td>
                                <td class="priority-<%= t.getPriority() != null ? t.getPriority().toLowerCase() : "medium" %>">
                                    <%= t.getPriority() != null ? t.getPriority() : "MEDIUM" %>
                                </td>
                                <td class="status-<%= t.getStatus() != null ? t.getStatus().toLowerCase().replace("_", "") : "pending" %>">
                                    <%= t.getStatus() != null ? t.getStatus() : "PENDING" %>
                                </td>
                                <td><%= t.getDueDate() != null ? t.getDueDate() : "-" %></td>
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