<%@ page import="dto.UserDTO" %>

<%
	UserDTO currentUser = (UserDTO) session.getAttribute("user");
	
	String displayName = (currentUser != null) ? currentUser.getUsername() : "Guest";
	String displayTime = (currentUser != null) ? currentUser.getLastLoginDisplay() : "N/A";
%>

<div class="navbar">

	
		<div class= "nav-logo">
			<h1> Task-Management </h1>
		</div>	
		<div class="user-info">
			<span class="username"> <%= displayName %></span>
			<span class ="lastLogin"> Login At: <%= displayTime %></span>
		</div>
		
</div>
