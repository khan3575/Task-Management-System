<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.TaskDTO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search Task</title>
 <link rel="stylesheet" href="<%= request.getContextPath() %>/css/search.css">
<script>
function handleInputChange() {
    const column = document.getElementById("column").value;

    const textInput = document.getElementById("textInput");
    const priorityInput = document.getElementById("priorityInput");
    const statusInput = document.getElementById("statusInput");

    textInput.style.display = "none";
    priorityInput.style.display = "none";
    statusInput.style.display = "none";

    textInput.disabled = true;
    priorityInput.disabled = true;
    statusInput.disabled = true;

    if (column === "priority") {
        priorityInput.style.display = "inline";
        priorityInput.disabled = false;
    } else if (column === "status") {
        statusInput.style.display = "inline";
        statusInput.disabled = false;
    } else if (column === "due_date") {
        dateInput.style.display = "inline";  
        dateInput.disabled = false;
	} else {
        textInput.style.display = "inline";
        textInput.disabled = false;
    }
}
</script>
</head>
<body>
	<%
	    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
	%>

	<form action="searchTask" method="get">
	    <label>Select Search Option:</label>
	
	    <select name="column" id="column" onchange="handleInputChange()">
	        <option value="">-- Search Task By --</option>
	        <option value="id">ID</option>
        	<option value="title">Title</option>
	        <option value="priority">Priority</option>
	        <option value="status">Status</option>
	        <option value="due_date">Due Date</option>
	        <option value="created_at">Created At</option>
	    </select>
	    
		<select id="priorityInput" name="value" style="display:none;">
		    <option value="">-- Select Priority --</option>
		    <option value="high">High</option>
		    <option value="medium">Medium</option>
		    <option value="low">Low</option>
		</select>

		<select id="statusInput" name="value" style="display:none;">
		    <option value="">-- Select Status --</option>
		    <option value="pending">Pending</option>
		    <option value="in_progress">In Progress</option>
		    <option value="completed">Completed</option>
		</select>
		
		<input type="text" id="textInput" name="value" placeholder="Enter value">
		<input type="date" id="dateInput" name="value" style="display:none;">
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