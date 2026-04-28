<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.TaskDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search Task</title>
</head>
<body>
	<%
	    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
	%>

	<form action="searchTask" method="get">
	    <label>Select Search Option:</label>
	
	    <select name="column">
	        <option value="">-- Search Task By --</option>
	        <option value="id">ID</option>
        	<option value="title">Title</option>
	        <option value="priority">Priority</option>
	        <option value="status">Status</option>
	        <option value="due_date">Due Date</option>
	        <option value="created_at">Created At</option>
	    </select>
	
	    <input type="text" name="value" placeholder="Enter value">
	    <input type="submit" value="Search">
	</form>
	
	<br><br>
	
	<table border="1">
	<tr>
	    <th>ID</th>
	    <th>Title</th>
	    <th>Priority</th>
	    <th>Status</th>
	    <th>Due Date</th>
	</tr>

	<%
	    if (tasks != null && !tasks.isEmpty()) {
	        for (TaskDTO t : tasks) {
	%>
	<tr>
	    <td><%= t.getId() %></td>
	    <td><%= t.getTitle() %></td>
	    <td><%= t.getPriority() %></td>
	    <td><%= t.getStatus() %></td>
	    <td><%= t.getDueDate() %></td>
	</tr>
	<%
	        }
	    } else {
	%>
	<tr>
	    <td colspan="5">No results found</td>
	</tr>
	<%
	    }
	%>
	</table>
</body>
</html>