<%-- File: webapp/views/dashboard.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="dto.TaskDTO"%>
<%@ page import="model.User"%>

<%
// Check if user is logged in
String loggedInUser = (String) session.getAttribute("username");
if (loggedInUser == null) {
	response.sendRedirect(request.getContextPath() + "/login");
	return;
}
// Get tasks from request attribute (set by DashboardServlet)
List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
// Get pagination parameters
Integer currentPage = (Integer) request.getAttribute("currentPage");
Integer totalPages = (Integer) request.getAttribute("totalPages");
Integer totalTasks = (Integer) request.getAttribute("totalTasks");
// Set default values if null
if (currentPage == null)
	currentPage = 1;
if (totalPages == null)
	totalPages = 1;
if (totalTasks == null)
	totalTasks = 0;
// Date formatter for display
SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard - Task Management System</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<!-- Include Navbar and Sidebar -->
	<%@ include file="components/navbar.jsp"%>
	<%@ include file="components/sidebar.jsp"%>
	<!-- Main Content -->
	<div class="main-content">
		<div class="dashboard-header">
			<h2>Task Dashboard</h2>
			<div class="task-stats">
				<span class="stat-box">Total Tasks: <%=totalTasks%></span>
				<button class="refresh-btn" onclick="location.reload();">🔄
					Refresh</button>
			</div>
		</div>
		<!-- Success/Error Messages -->
		<%
		if (request.getParameter("success") != null) {
		%>
		<div class="success-message">
			✓
			<%=request.getParameter("success")%>
		</div>
		<%
		}
		%>
		<%
		if (request.getParameter("error") != null) {
		%>
		<div class="error-message">
			✗
			<%=request.getParameter("error")%>
		</div>
		<%
		}
		%>
		<!-- Tasks Table -->
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
					<%
					if (tasks != null && !tasks.isEmpty()) {
						for (TaskDTO task : tasks) {
							// Format due date for display
							String formattedDueDate = "";
							if (task.getDueDate() != null) {
						java.util.Date utilDate = new java.util.Date(task.getDueDate().getTime());
						formattedDueDate = displayFormat.format(utilDate);
							}
							// Format created date (remove time if needed)
							String formattedCreatedAt = "";
							if (task.getCreatedAt() != null) {
						if (task.getCreatedAt().length() > 10) {
							formattedCreatedAt = task.getCreatedAt().substring(0, 10);
						} else {
							formattedCreatedAt = task.getCreatedAt();
						}
							}
					%>
					<tr>
						<td class="task-id"><%=task.getId()%></td>
						<td class="task-title"><%=task.getTitle()%></td>
						<td class="task-desc"><%=task.getDescription() != null ? task.getDescription() : "-"%></td>
						<td
							class="priority-<%=task.getPriority() != null ? task.getPriority().toLowerCase() : "medium"%>">
							<%=task.getPriority() != null ? task.getPriority() : "MEDIUM"%>
						</td>
						<td
							class="status-<%=task.getStatus() != null ? task.getStatus().toLowerCase().replace("_", "") : "pending"%>">
							<%=task.getStatus() != null ? task.getStatus() : "PENDING"%>
						</td>
						<td><%=formattedDueDate.isEmpty() ? "-" : formattedDueDate%></td>
						<td><%=formattedCreatedAt.isEmpty() ? "-" : formattedCreatedAt%></td>
						<td class="action-buttons"><a
							href="${pageContext.request.contextPath}/updateTask?id=<%= task.getId() %>"
							class="btn-edit">Edit</a> <a href="#"
							onclick="confirmDelete(<%=task.getId()%>)" class="btn-delete">Delete</a>
						</td>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="8" class="no-data">
							<div class="empty-state">
								<span class="empty-icon">📋</span>
								<p>No tasks found. Click "Add Task" to create your first
									task!</p>
							</div>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>
		<!-- Pagination -->
		<%
		if (tasks != null && !tasks.isEmpty() && totalPages > 1) {
		%>
		<div class="pagination-container">
			<div class="pagination-info">
				Showing page
				<%=currentPage%>
				of
				<%=totalPages%>
				(Total:
				<%=totalTasks%>
				tasks)
			</div>
			<div class="pagination">
				<%
				if (currentPage > 1) {
				%>
				<a
					href="${pageContext.request.contextPath}/DashboardServlet?page=<%= currentPage - 1 %>"
					class="page-link">« Previous</a>
				<%
				} else {
				%>
				<span class="page-link disabled">« Previous</span>
				<%
				}
				%>
				<%
				int startPage = Math.max(1, currentPage - 2);
				int endPage = Math.min(totalPages, currentPage + 2);
				if (startPage > 1) {
				%>
				<a href="${pageContext.request.contextPath}/DashboardServlet?page=1"
					class="page-link">1</a>
				<%
				if (startPage > 2) {
				%>
				<span class="page-dots">...</span>
				<%
				}
				%>
				<%
				}
				%>
				<%
				for (int i = startPage; i <= endPage; i++) {
				%>
				<a
					href="${pageContext.request.contextPath}/DashboardServlet?page=<%= i %>"
					class="page-link <%= i == currentPage ? "active" : "" %>"> <%=i%>
				</a>
				<%
				}
				%>
				<%
				if (endPage < totalPages) {
				%>
				<%
				if (endPage < totalPages - 1) {
				%>
				<span class="page-dots">...</span>
				<%
				}
				%>
				<a
					href="${pageContext.request.contextPath}/DashboardServlet?page=<%= totalPages %>"
					class="page-link"><%=totalPages%></a>
				<%
				}
				%>
				<%
				if (currentPage < totalPages) {
				%>
				<a
					href="${pageContext.request.contextPath}/DashboardServlet?page=<%= currentPage + 1 %>"
					class="page-link">Next »</a>
				<%
				} else {
				%>
				<span class="page-link disabled">Next »</span>
				<%
				}
				%>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<!-- Delete Confirmation JavaScript -->
	<script>
        function confirmDelete(taskId) {
            if(confirm("⚠️ Are you sure you want to delete this task?\n\nThis action cannot be undone!")) {
                window.location.href = "${pageContext.request.contextPath}/deleteTask?id=" + taskId;
            }
        }
</script>
</body>
</html>