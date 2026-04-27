<%@ page import="dto.UserDTO" %>
<%
	String displayName = (String) session.getAttribute("username");
    String displayTime = (String) session.getAttribute("lastLogin");
	
	if(displayName == null) displayName = "Guest";
	if(displayTime == null) displayTime = "N/A";
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
