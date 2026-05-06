<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dto.TaskDTO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Tasks</title>
    
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/search-style.css">
    
    <script>
        var contextPath = "<%= request.getContextPath() %>";
    </script>
</head>
<body>

   
    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %>

    <div class="main-content">
        <h1>Search Tasks</h1>

        
        <form action="<%= request.getContextPath() %>/searchTask" method="get">
            <label for="column">Search by:</label>
            <select name="column" id="column" onchange="handleInputChange()">
                <option value="">-- Choose --</option>
                <option value="id">ID</option>
                <option value="title">Title</option>
                <option value="priority">Priority</option>
                <option value="status">Status</option>
                <option value="due_date">Due Date</option>
                <option value="created_at">Created At</option>
            </select>
            
            <select id="priorityInput" name="value" style="display:none;">
                <option value="">-- Priority --</option>
                <option value="high">High</option>
                <option value="medium">Medium</option>
                <option value="low">Low</option>
            </select>

            <select id="statusInput" name="value" style="display:none;">
                <option value="">-- Status --</option>
                <option value="pending">Pending</option>
                <option value="in_progress">In Progress</option>
                <option value="completed">Completed</option>
            </select>
            
            <input type="text" id="textInput" name="value" placeholder="Enter value">
            <input type="date" id="dateInput" name="value" style="display:none;">
            <input type="submit" value="Search">
        </form>

      
        <table>
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
                    List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
                    if (tasks != null && !tasks.isEmpty()) {
                        for (TaskDTO task : tasks) {
                %>
                <tr>
                    <td><%= task.getId() %></td>
                    <td><%= task.getTitle() %></td>
                    <td><%= task.getPriority() %></td>
                    <td><%= task.getStatus() %></td>
                    <td><%= task.getDueDate() %></td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="5">No tasks found</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

   
    <script>
    function handleInputChange() {
        var column = document.getElementById("column").value;
        var textInput = document.getElementById("textInput");
        var priorityInput = document.getElementById("priorityInput");
        var statusInput = document.getElementById("statusInput");
        var dateInput = document.getElementById("dateInput");

       
        [textInput, priorityInput, statusInput, dateInput].forEach(function(input) {
            input.style.display = "none";
            input.disabled = true;
        });

       
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

</body>
</html>