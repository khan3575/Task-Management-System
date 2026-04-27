<%@ page import="dto.UserDTO" %>
<%	
	Integer displayId = (Integer) session.getAttribute("userId");
	String displayName = (String) session.getAttribute("username");
    String displayTime = (String) session.getAttribute("lastLogin");
	
	if(displayName == null) displayName = "Guest";
	if(displayTime == null) displayTime = "N/A";
%>

<div class="navbar">
	<div class = "nav-container"> 
	
		<div class= "nav-logo">
			<a href = "#"> Task-Management </a>
		</div>	
		<div class="user-info">
			<span class="userId"> User Id: <%= displayId  %> </span>
			<span class="username">User Name: <%= displayName %></span>
			<span class ="lastLogin"> Login At: <%= displayTime %></span>
		</div>
		
	</div>
</div>
