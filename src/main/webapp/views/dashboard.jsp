<%-- File: webapp/views/dashboard.jsp --%>
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
    
    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    
    if(currentPage == null) currentPage = 1;
    if(totalPages == null) totalPages = 1;
    
    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Task Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/deleteConfirm.js"></script>
</head>
<body>
     <%--   <%@ include file="../components/navbar.jsp" %>
    <%@ include file="../components/sidebar.jsp" %> --%>
    
    <div class="main-content">
        <h2>Task Dashboard</h2>
        
        <% if(request.getParameter("success") != null) { %>
            <div class="success-message">
                <%= request.getParameter("success") %>
            </div>
        <% } %>
        
        <% if(request.getParameter("error") != null) { %>
            <div class="error-message">
                <%= request.getParameter("error") %>
            </div>
        <% } %>
        
        <div class="table-container">
            <table>
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
                    %>
                        <tr>
                            <td><%= task.getId() %></td>
                            <td><%= task.getTitle() %></td>
                            <td><%= task.getDescription() != null ? task.getDescription() : "-" %></td>
                            <td class="priority-<%= task.getPriority() != null ? task.getPriority().toLowerCase() : "medium" %>">
                                <%= task.getPriority() != null ? task.getPriority() : "MEDIUM" %>
                            </td>
                            <td class="status-<%= task.getStatus() != null ? task.getStatus().toLowerCase() : "pending" %>">
                                <%= task.getStatus() != null ? task.getStatus() : "PENDING" %>
                            </td>
                            <td><%= formattedDueDate %></td>
                            <td><%= task.getCreatedAt() != null ? task.getCreatedAt() : "-" %></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/updateTask?id=<%= task.getId() %>" class="btn-edit">Edit</a>

                                <a href="#" onclick="confirmDelete(<%= task.getId() %>)" class="btn-delete">Delete</a>
                            </td>
                        </tr>
                    <% } 
                    } else { %>
                        <tr>
                            <td colspan="8">No tasks found. Click "Add Task" to create one.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <% if(tasks != null && !tasks.isEmpty() && totalPages > 1) { %>
            <div class="pagination">
                <% if(currentPage > 1) { %>
                    <a href="${pageContext.request.contextPath}/DashboardServlet?page=<%= currentPage - 1 %>">Previous</a>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <a href="${pageContext.request.contextPath}/DashboardServlet?page=<%= i %>" 
                       class="<%= i == currentPage ? "active" : "" %>">
                        <%= i %>
                    </a>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <a href="${pageContext.request.contextPath}/DashboardServlet?page=<%= currentPage + 1 %>">Next</a>
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>