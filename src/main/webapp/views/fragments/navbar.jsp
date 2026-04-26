<%@ page import="dto.UserDTO" %>

<%
	UserDTO currentUser = (UserDTO) session.getAttribute("user");
	String lastLogin = (String) session.getAttribute("last_login");
	
	String displayName = (currentUser != null) ? currentUser.getUsername() : "Guest";
	String displayTime = (lastLogin != null) ? lastLogin : "N/A";
%>

<div class="navbar">
	<div class = "nav-container"> 
	
		<div class= "nav-logo">
			<a href = "#"> Task-Management </a>
		</div>	
		<div class="user-info">
			<span class="username"> <%= displayName %></span>
			<span class ="lastLogin"> Login At: <%= lastLogin %></span>
		</div>
		
	</div>
</div>
