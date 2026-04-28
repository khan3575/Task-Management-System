<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.TaskDTO"%>
<%@ page import="java.util.List"%>

<%
List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Task Management System</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/dashboard.css">
    <head>
    
    <script> var contextPath = "<%= request.getContextPath() %>"; </script>
    <script src="<%= request.getContextPath() %>/js/deleteConfirm.js"></script>
</head>
</head>
<body>

    <%@ include file="components/navbar.jsp"%>
    <%@ include file="components/sidebar.jsp"%>

    <div class="main-content">
        <h1>Task Dashboard</h1>

        <div id="messageBox" class="success-message" style="display:none;"></div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Due Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                if (tasks != null && !tasks.isEmpty()) {
                    for (TaskDTO task : tasks) {
                %>
                <tr id="row-<%=task.getId()%>">
                    <td><%=task.getId()%></td>
                    <td><%=task.getTitle()%></td>
                    <td><%=task.getDescription()%></td>
                    <td><%=task.getPriority()%></td>
                    <td><%=task.getStatus()%></td>
                    <td><%=task.getDueDate()%></td>
                    <td>
                        
                        <a href="<%=request.getContextPath()%>/updateTask?id=<%=task.getId()%>" class="btn edit">Edit</a>

                        <button type="button" class="btn delete"
                                onclick="deleteTask(<%=task.getId()%>)">
                            Delete
                        </button>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7">No tasks available</td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>

   
    <script>
    
    async function deleteTask(taskId) {
        if (!confirm("Are you sure you want to delete this task?")) {
            return;
        }

        try {
            const response = await fetch(
                contextPath + "/deleteTask?id=" + taskId + "&ajax=true",
                { method: "DELETE" }
            );

            if (response.ok) {
                const row = document.getElementById("row-" + taskId);
                if (row) row.remove();
                showMessage("Task deleted successfully", true);
            } else {
                const text = await response.text();
                showMessage(text || "Deletion failed", false);
            }
        } catch (error) {
            console.error("Delete error:", error);
            showMessage("Network error – please try again", false);
        }
    }

    function showMessage(message, isSuccess) {
        const box = document.getElementById("messageBox");
        box.textContent = message;
        box.className = isSuccess ? "success-message" : "error-message";
        box.style.display = "block";
        setTimeout(() => { box.style.display = "none"; }, 4000);
    }
   
    </script>

</body>
</html>