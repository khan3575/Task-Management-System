<%-- File: webapp/views/editTask.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="dto.TaskDTO"%>
<%
String username = (String) session.getAttribute("username");
if (username == null) {
	response.sendRedirect(request.getContextPath() + "/login");
	return;
}

TaskDTO task = (TaskDTO) request.getAttribute("task");
if (task == null) {
	response.sendRedirect(request.getContextPath() + "/dashboard?error=Task not found");

	return;
}

String dueDateValue = "";
if (task.getDueDate() != null) {
	dueDateValue = task.getDueDate().toString();
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Task</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
	<script src="${pageContext.request.contextPath}/js/showMessage.js"></script>
<<<<<<< Updated upstream
	
=======
>>>>>>> Stashed changes
</head>
<body>
	<%@ include file="components/navbar.jsp"%>
	<%@ include file="components/sidebar.jsp"%>

	<div class="main-content">
		<div class="form-container">
			<h2>Edit Task</h2>

			<div id="messageBox" style="display: none;"></div>

			<%
			if (request.getAttribute("success") != null) {
			%>
			<script>
    showMessage("<%=request.getAttribute("success")%>", true);
           </script>
			<%
			}
			%>

			<%
			if (request.getAttribute("error") != null) {
			%>
			<script>
    showMessage("<%=request.getAttribute("error")%>", false);
			</script>
			<%
			}
			%>

			<div class="info-note">Note: Task ID and Title cannot be
				modified</div>

			<form action="${pageContext.request.contextPath}/updateTask"
				method="POST">
				<!-- Hidden field to pass task ID -->
				<input type="hidden" name="taskId" value="<%=task.getId()%>">
				<input type="hidden" name="title" value="<%=task.getTitle()%>">

				<div class="form-group">
					<label>Task ID</label> <input type="text" value="<%=task.getId()%>"
						disabled readonly> <small>Task ID cannot be
						modified</small>
				</div>

				<div class="form-group">
					<label>Task Title</label> <input type="text"
						value="<%=task.getTitle()%>" disabled readonly> <small>Task
						title cannot be modified</small>
				</div>

				<div class="form-group">
					<label>Description</label>
					<textarea name="description" rows="5"><%=task.getDescription() != null ? task.getDescription() : ""%></textarea>
				</div>

				<div class="form-group">
					<label>Priority</label> <select name="priority" required>
						<option value="LOW"
							<%=task.getPriority() != null && task.getPriority().equals("LOW") ? "selected" : ""%>>Low</option>
						<option value="MEDIUM"
							<%=task.getPriority() != null && task.getPriority().equals("MEDIUM") ? "selected" : ""%>>Medium</option>
						<option value="HIGH"
							<%=task.getPriority() != null && task.getPriority().equals("HIGH") ? "selected" : ""%>>High</option>
					</select>
				</div>

				<div class="form-group">
					<label>Status</label> <select name="status" required>
						<option value="PENDING"
							<%=task.getStatus() != null && task.getStatus().equals("PENDING") ? "selected" : ""%>>Pending</option>
						<option value="IN_PROGRESS"
							<%=task.getStatus() != null && task.getStatus().equals("IN_PROGRESS") ? "selected" : ""%>>In
							Progress</option>
						<option value="COMPLETED"
							<%=task.getStatus() != null && task.getStatus().equals("COMPLETED") ? "selected" : ""%>>Completed</option>
					</select>
				</div>

				<div class="form-group">
					<label>Due Date</label> <input type="date" name="dueDate"
						value="<%=dueDateValue%>">
				</div>

				<div class="form-group">
					<label>Created At</label> <input type="text"
						value="<%=task.getCreatedAt() != null ? task.getCreatedAt() : ""%>"
						disabled readonly>
				</div>

				<div class="form-actions">
					<button type="submit">Update Task</button>
					<a href="${pageContext.request.contextPath}/dashboard">Cancel</a>

				</div>
			</form>
		</div>
	</div>
</body>

</html>