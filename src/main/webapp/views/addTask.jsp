<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    String loggedInUser = (String) session.getAttribute("username");
    if(loggedInUser == null) {
        response.sendRedirect(request.getContextPath()+"/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Task</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %> 

    <div class="main-content">
    
    
    
    	
   	 	<!-- adding success or error message  -->
           <% 
	    String status = request.getParameter("status");
		if(status != null) {
           %>
		
		<div id="popup-message" class="popup <%= status.equals("success")? "success" : "error" %> ">
			<%= status.equals("success") ? "Successfully added a task" : "Failed to add Task" %>
		</div>
		<% } %>
            
            

        <div class="form-container">
            <h2>Add New Task</h2>
            
            <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            
            
            
            <form action="${pageContext.request.contextPath}/addTask" method="POST">
                <div class="form-group">
                    <label>Task Title</label>
                    <input type="text" name="title" required 
                           value="<%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %>">
                </div>
                
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" rows="5"><%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %></textarea>
                </div>
                
                <div class="form-group">
                    <label>Priority</label>
                    <select name="priority" required>
                        <option value="LOW" <%= request.getAttribute("priority") != null && request.getAttribute("priority").equals("LOW") ? "selected" : "" %>>Low</option>
                        <option value="MEDIUM" <%= request.getAttribute("priority") == null || request.getAttribute("priority").equals("MEDIUM") ? "selected" : "" %>>Medium</option>
                        <option value="HIGH" <%= request.getAttribute("priority") != null && request.getAttribute("priority").equals("HIGH") ? "selected" : "" %>>High</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Status</label>
                    <select name="status" required>
                        <option value="PENDING" <%= request.getAttribute("status") == null || request.getAttribute("status").equals("PENDING") ? "selected" : "" %>>Pending</option>
                        <option value="IN_PROGRESS" <%= request.getAttribute("status") != null && request.getAttribute("status").equals("IN_PROGRESS") ? "selected" : "" %>>In Progress</option>
                        <option value="COMPLETED" <%= request.getAttribute("status") != null && request.getAttribute("status").equals("COMPLETED") ? "selected" : "" %>>Completed</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Due Date</label>
                    <input type="date" name="dueDate" value="<%= request.getAttribute("dueDate") != null ? request.getAttribute("dueDate") : "" %>">
                </div>
                
                <div class="form-actions">
                    <button type="submit">Save Task</button>
                    <a href="${pageContext.request.contextPath}/home">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    
    
    <!-- Adding timer to auto clear the status  -->
    
    <script>
    window.onload = function() {
        const popup = document.getElementById('popup-message');

        if (popup) {
            setTimeout(function() {
                popup.style.transition = "opacity 0.5s ease";
                popup.style.opacity = "0";

                setTimeout(() => popup.remove(), 500);

                const url = new URL(window.location);
                url.searchParams.delete('status');
                window.history.replaceState({}, '', url);
                
            }, 3000); 
        }
    };
</script>
</body>
</html>