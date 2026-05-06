<%@ page import="java.util.List"%>
<%@ page import="dto.TaskDTO"%>

<%
List<TaskDTO> tasks = (List<TaskDTO>) request.getAttribute("tasks");
int currentPage = (int) request.getAttribute("currentPage");
int totalPages = (int) request.getAttribute("totalPages");
%>

<div id="meta"
     data-page="<%=currentPage%>"
     data-total="<%=totalPages%>"
     style="display:none;"></div>

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
        <%-- <a href="updateTask?id=<%=task.getId()%>" class="btn edit">Edit</a> --%>
        <button onclick="updateTask(<%=task.getId()%>)" class="btn edit">Update</button>
        <button onclick="deleteTask(<%=task.getId()%>)" class="btn delete">Delete</button>
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