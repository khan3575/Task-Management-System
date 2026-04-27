<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
</head>

<body>

    <%@ include file="components/navbar.jsp"%>
    <%@ include file="components/sidebar.jsp"%>

    <div class="main-content">
        <h1>Task Dashboard</h1>

        <table>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Due Date</th>
                <th>Actions</th>
            </tr>

            <%
            if (tasks != null && !tasks.isEmpty()) {
                for (TaskDTO task : tasks) {
            %>
            <tr>
                <td><%=task.getId()%></td>
                <td><%=task.getTitle()%></td>
                <td><%=task.getDescription()%></td>
                <td><%=task.getPriority()%></td>
                <td><%=task.getStatus()%></td>
                <td><%=task.getDueDate()%></td>

                <td>
                    <a href="<%=request.getContextPath()%>/updateTask?id=<%=task.getId()%>" class="btn edit">Edit</a>

                    <form action="<%=request.getContextPath()%>/task" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="<%=task.getId()%>">
                        <button type="submit" class="btn delete"
                            onclick="return confirm('Are you sure you want to delete this task?');">
                            Delete
                        </button>
                    </form>
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
        </table>
    </div>

</body>
</html>