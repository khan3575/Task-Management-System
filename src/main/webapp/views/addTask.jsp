<%-- File: webapp/views/addTask.jsp --%>
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
    <style>
        /* Due date validation styles */
        .due-date-warning {
            background: #fff3cd;
            color: #856404;
            padding: 8px 12px;
            border-radius: 5px;
            font-size: 13px;
            margin-top: 5px;
            border-left: 3px solid #ffc107;
        }
        
        .due-date-warning::before {
            content: "⚠️ ";
        }
        
        input[type="date"].error {
            border-color: #dc3545;
            background-color: #fff8f8;
        }
        
        .field-error {
            color: #dc3545;
            font-size: 12px;
            margin-top: 5px;
            display: block;
        }
        
        .field-error::before {
            content: "❌ ";
        }
        
        .due-date-hint {
            font-size: 12px;
            color: #6c757d;
            display: block;
            margin-top: 5px;
        }
        
        .due-date-hint::before {
            content: "ℹ️ ";
        }
    </style>
</head>
<body>


    <div class="main-content">
     <%@ include file="components/navbar.jsp" %>
    <%@ include file="components/sidebar.jsp" %> 
    
    
    	
   	 	<!-- adding success or error message  -->
           <% 
	    String status = request.getParameter("status");
		if(status != null) {
           %>
		
		<div id="popup-message" class="popup <%= status.equals("success")? "success" : "error" %> ">
			<%= status.equals("success") ? "Successfully added a task" : "Failed to add Task" %>
		</div>
		<% } %>
            
            <!-- Display due date specific error if any -->
            <% 
                String dueDateError = (String) request.getAttribute("dueDateError");
                if(dueDateError != null) { 
            %>
                <div class="error-message">
                    📅 <%= dueDateError %>
                </div>
            <% } %>

        <div class="form-container">
            <h2>Add New Task</h2>
            
            <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/addTask" method="POST" id="addTaskForm">
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
                    <input type="date" name="dueDate" id="dueDate" 
                           value="<%= request.getAttribute("dueDate") != null ? request.getAttribute("dueDate") : "" %>"
                           <%= request.getAttribute("dueDateError") != null ? "class='error'" : "" %>>
                    <small class="due-date-hint">📅 Due date cannot be before today's date</small>
                    <div class="due-date-warning" id="dateWarning" style="display: none;">
                        Please select a date that is today or in the future
                    </div>
                    <% if(request.getAttribute("dueDateError") != null) { %>
                        <span class="field-error"><%= request.getAttribute("dueDateError") %></span>
                    <% } %>
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
    
    // Client-side due date validation
    document.addEventListener('DOMContentLoaded', function() {
        const dueDateInput = document.getElementById('dueDate');
        const dateWarning = document.getElementById('dateWarning');
        const form = document.getElementById('addTaskForm');
        
        if (dueDateInput) {
            // Set min date to today
            const today = new Date().toISOString().split('T')[0];
            dueDateInput.setAttribute('min', today);
            
            // Real-time validation when date changes
            dueDateInput.addEventListener('change', function() {
                const selectedDate = this.value;
                if (selectedDate && selectedDate < today) {
                    dateWarning.style.display = 'block';
                    this.style.borderColor = '#dc3545';
                    this.style.backgroundColor = '#fff8f8';
                } else {
                    dateWarning.style.display = 'none';
                    this.style.borderColor = '#ddd';
                    this.style.backgroundColor = 'white';
                }
            });
            
            // Validate on form submission
            if (form) {
                form.addEventListener('submit', function(e) {
                    const selectedDate = dueDateInput.value;
                    if (selectedDate && selectedDate < today) {
                        e.preventDefault();
                        dateWarning.style.display = 'block';
                        dueDateInput.style.borderColor = '#dc3545';
                        dueDateInput.style.backgroundColor = '#fff8f8';
                        dueDateInput.focus();
                        alert('Please select a due date that is today or in the future!');
                        return false;
                    }
                });
            }
        }
    });
    </script>
</body>
</html>